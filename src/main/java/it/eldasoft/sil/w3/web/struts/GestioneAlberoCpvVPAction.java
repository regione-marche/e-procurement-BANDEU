package it.eldasoft.sil.w3.web.struts;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Stefano.Cestaro
 * 
 */
public class GestioneAlberoCpvVPAction extends Action {

  /**
   * Manager per la gestione delle interrogazioni di database.
   */
  private SqlManager sqlManager;

  /**
   * @param sqlManager
   *        the sqlManager to set
   */
  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  /**
   * Restituisce JSONArray strutturato nel seguente modo:
   * 
   * <ul>
   * <li>0 - Livello</li>
   * <li>1 - Codice CPV con la sola porzione del livello</li>
   * <li>2 - Codice CPV completo</li>
   * <li>3 - Descrizione</li>
   * <li>4 - Contatore elementi figli</li>
   * <li>5 - Contatore elementi totali su ricerca</li>
   * </ul>
   */
  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    response.setHeader("cache-control", "no-cache");
    response.setContentType("text/text;charset=utf-8");
    PrintWriter out = response.getWriter();

    String cpvvpgrpdiv = request.getParameter("cpvvpgrpdiv");
    JSONArray jsonArrayGrpDiv = JSONArray.fromObject(cpvvpgrpdiv);

    Long livello = new Long(request.getParameter("livello"));
    String id = request.getParameter("id");
    String cpvvpcod = request.getParameter("cpvvpcod");
    String textsearch = request.getParameter("textsearch");

    JSONArray jsonArray = new JSONArray();

    popolaCpvVP(jsonArrayGrpDiv, livello, id, cpvvpcod, textsearch, jsonArray);

    out.println(jsonArray);
    out.flush();

    return null;
  }

  /**
   * Carica i nodi dell'albero
   * 
   * @param livello
   * @param cpvvpcod
   * @param testsearch
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaCpvVP(JSONArray jsonArrayGrpDiv, Long livello, String id, String cpvvpcod, String textsearch, JSONArray jsonArray)
      throws SQLException {

    String selectTABCPV = "";

    switch (livello.intValue()) {
    case 0:
      // Gruppi di divisioni
      for (int i = 0; i < jsonArrayGrpDiv.size(); i++) {
        String grpid = (String) jsonArrayGrpDiv.getJSONObject(i).get("grpid");
        String grpdesc = (String) jsonArrayGrpDiv.getJSONObject(i).get("grpdesc");
        JSONArray jsonArrayDiv = jsonArrayGrpDiv.getJSONObject(i).getJSONArray("grpdiv");

        String selectCPV = "select count(*) from tabcpv where (";

        // Lista delle divisioni afferenti al gruppo
        for (int j = 0; j < jsonArrayDiv.size(); j++) {
          if (j > 0) selectCPV += " or ";
          selectCPV += " cpvcod4 like '" + jsonArrayDiv.getString(j) + "%' ";
        }
        selectCPV += ") ";

        // Filtro di ricerca per descrizione anche parziale e multipla
        if (textsearch != null && !"".equals(textsearch.trim())) {
          selectCPV += " and (";
          String[] words = textsearch.split(" ");
          for (int w = 0; w < words.length; w++) {
            String cpvdescsearch = StringUtils.replace(words[w], "_", "#_");
            cpvdescsearch = StringUtils.replace(cpvdescsearch, "%", "#%");
            cpvdescsearch = "%" + cpvdescsearch.toUpperCase() + "%";
            if (w > 0) selectCPV += " and ";
            selectCPV += " (upper(cpvdesc) like '" + cpvdescsearch + "' escape '#' or cpvcod4 like '" + cpvdescsearch + "' escape '#')";
          }
          selectCPV += " )";
        }

        // Se per il gruppo c'e' almeno un CPV di qualsiasi livello,
        // carico la riga del gruppo
        Long cntCPV = (Long) this.sqlManager.getObject(selectCPV, new Object[] {});
        if (cntCPV != null && cntCPV.longValue() > 0) {
          Object[] rowCpvVP = new Object[6];
          rowCpvVP[0] = 1;
          rowCpvVP[1] = grpid;
          rowCpvVP[2] = grpid;
          rowCpvVP[3] = grpdesc;
          rowCpvVP[4] = cntCPV;
          rowCpvVP[5] = cntCPV;
          jsonArray.add(rowCpvVP);
        }

      }
      break;

    case 1:
      // Divisioni
      selectTABCPV = "select cpvcod4, cpvdesc from tabcpv where ";
      
      // Se il livello 1 - Divisioni viene caricato partendo dal livello 0 - Gruppi di divisioni
      // bisogna controllare quali divisioni sono contenute nel gruppo identificato da "id".
      // Altrimenti si caricano tutte le divisioni senza alcun raggruppamento.
      if (id != null && !"".equals(id.trim())) {
        for (int i = 0; i < jsonArrayGrpDiv.size(); i++) {
          String grpid = (String) jsonArrayGrpDiv.getJSONObject(i).get("grpid");
          if (id.equals(grpid)) {
            JSONArray jsonArrayDiv = jsonArrayGrpDiv.getJSONObject(i).getJSONArray("grpdiv");
            for (int j = 0; j < jsonArrayDiv.size(); j++) {
              if (j > 0) selectTABCPV += " or ";
              selectTABCPV += " cpvcod4 like '" + jsonArrayDiv.getString(j) + "000000-_' ";
            }
          }
        }
      } else {
        selectTABCPV += " cpvcod4 like '__000000-_' ";
      }
      selectTABCPV += " order by cpvcod4";
      this.popolaCpvVP(livello, textsearch, jsonArray, selectTABCPV);
      break;

    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
      String wherecpvvp = StringUtils.rightPad(cpvvpcod.substring(0, livello.intValue()) + "_", 8, "0") + "-_";
      selectTABCPV = "select cpvcod4, cpvdesc from tabcpv where (cpvcod4 like '" + wherecpvvp + "'";
      if ("601".equals(cpvvpcod.substring(0, 3))) {
        selectTABCPV += " or cpvcod4 like '6011_000-_'";
      }
      selectTABCPV += ") and cpvcod4 <> '" + cpvvpcod + "' order by cpvcod4";
      this.popolaCpvVP(livello, textsearch, jsonArray, selectTABCPV);
      break;

    default:
      break;
    }

  }

  /**
   * Carica la lista dei CPV in funzione del nodo chiamante e dell'eventuale
   * ricerca
   * 
   * @param livello
   * @param textsearch
   * @param jsonArray
   * @param selectTABCPV
   * @throws SQLException
   */
  private void popolaCpvVP(Long livello, String textsearch, JSONArray jsonArray, String selectTABCPV) throws SQLException {
    List<?> datiTABCPV = this.sqlManager.getListVector(selectTABCPV, new Object[] {});
    if (datiTABCPV != null && datiTABCPV.size() > 0) {
      for (int i = 0; i < datiTABCPV.size(); i++) {

        String cpvcod4 = (String) SqlManager.getValueFromVectorParam(datiTABCPV.get(i), 0).getValue();
        String cpvcod4search = cpvcod4.substring(0, livello.intValue() + 1) + "%";

        // Filtro di ricerca per descrizione anche parziale e multipla
        String addsearch = "";
        if (textsearch != null && !"".equals(textsearch.trim())) {
          addsearch = " and (";
          String[] words = textsearch.split(" ");
          for (int w = 0; w < words.length; w++) {
            String cpvdescsearch = StringUtils.replace(words[w], "_", "#_");
            cpvdescsearch = StringUtils.replace(cpvdescsearch, "%", "#%");
            cpvdescsearch = "%" + cpvdescsearch.toUpperCase() + "%";
            if (w > 0) addsearch += " and ";
            addsearch += " (upper(cpvdesc) like '" + cpvdescsearch + "' escape '#' or cpvcod4 like '" + cpvdescsearch + "' escape '#')";
          }
          addsearch += " )";
        }

        String selectCPVFigli = "select count(*) from tabcpv where cpvcod4 like ? and cpvcod4 <> ?";
        if (addsearch != null && !"".equals(addsearch.trim())) selectCPVFigli += addsearch;
        Long cntCPVFigli = (Long) this.sqlManager.getObject(selectCPVFigli, new Object[] { cpvcod4search, cpvcod4 });

        if (addsearch != null && !"".equals(addsearch.trim())) {
          String selectCPVRicerca = "select count(*) from tabcpv where cpvcod4 like ? " + addsearch;
          Long cntCPVRicerca = (Long) this.sqlManager.getObject(selectCPVRicerca, new Object[] { cpvcod4search });
          if (cntCPVRicerca != null && cntCPVRicerca.longValue() > 0) {
            Object[] rowCpvVP = new Object[6];
            rowCpvVP[0] = new Long(livello.longValue() + 1);
            rowCpvVP[1] = cpvcod4.substring(0, livello.intValue() + 1);
            rowCpvVP[2] = cpvcod4;
            rowCpvVP[3] = (String) SqlManager.getValueFromVectorParam(datiTABCPV.get(i), 1).getValue();
            rowCpvVP[4] = cntCPVFigli;
            rowCpvVP[5] = cntCPVRicerca;
            jsonArray.add(rowCpvVP);
          }

        } else {
          Object[] rowCpvVP = new Object[6];
          rowCpvVP[0] = new Long(livello.longValue() + 1);
          rowCpvVP[1] = cpvcod4.substring(0, livello.intValue() + 1);
          rowCpvVP[2] = cpvcod4;
          rowCpvVP[3] = (String) SqlManager.getValueFromVectorParam(datiTABCPV.get(i), 1).getValue();
          rowCpvVP[4] = cntCPVFigli;
          rowCpvVP[5] = cntCPVFigli;
          jsonArray.add(rowCpvVP);
        }

      }
    }
  }
}
