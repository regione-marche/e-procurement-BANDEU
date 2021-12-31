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
public class GestioneAlberoNUTSAction extends Action {

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
   * Avvia la richiesta.
   */
  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    response.setHeader("cache-control", "no-cache");
    response.setContentType("text/text;charset=utf-8");
    PrintWriter out = response.getWriter();

    Long livello = new Long(request.getParameter("livello"));
    String paese = request.getParameter("paese");
    String area = request.getParameter("area");
    String regione = request.getParameter("regione");
    String provincia = request.getParameter("provincia");
    String textsearch = request.getParameter("textsearch");
    String codsearch = request.getParameter("codsearch");
    String modopagina = request.getParameter("modopagina");

    JSONArray jsonArray = new JSONArray();

    popolaNUTS(modopagina, livello, paese, area, regione, provincia, textsearch, codsearch, jsonArray);

    out.println(jsonArray);
    out.flush();

    return null;
  }

  /**
   * Carica i nodi dell'albero.
   * 
   * @param livello
   * @param paese
   * @param area
   * @param regione
   * @param provincia
   * @param textsearch
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaNUTS(String modopagina, Long livello, String paese, String area, String regione, String provincia, String textsearch,
      String codsearch, JSONArray jsonArray) throws SQLException {

    String selectTABNUTS = "";

    switch (livello.intValue()) {

    case 1:
      // Paese
      selectTABNUTS = "select paese, area, regione, provincia, codice, descrizione from tabnuts where area is null and regione is null and provincia is null order by codice";
      this.popolaNUTS(modopagina, livello, textsearch, codsearch, jsonArray, selectTABNUTS);
      break;

    case 2:
      // Area
      selectTABNUTS = "select paese, area, regione, provincia, codice, descrizione from tabnuts where paese = '"
          + paese
          + "' and area is not null and regione is null and provincia is null order by codice";
      this.popolaNUTS(modopagina, livello, textsearch, codsearch, jsonArray, selectTABNUTS);
      break;

    case 3:
      // Regione
      selectTABNUTS = "select paese, area, regione, provincia, codice, descrizione from tabnuts where paese = '"
          + paese
          + "' and area = '"
          + area
          + "' and regione is not null and provincia is null order by codice";
      this.popolaNUTS(modopagina, livello, textsearch, codsearch, jsonArray, selectTABNUTS);
      break;

    case 4:
      // Provincia
      selectTABNUTS = "select paese, area, regione, provincia, codice, descrizione from tabnuts where paese = '"
          + paese
          + "' and area = '"
          + area
          + "' and regione = '"
          + regione
          + "' and provincia is not null order by codice";
      this.popolaNUTS(modopagina, livello, textsearch, codsearch, jsonArray, selectTABNUTS);
      break;

    default:
      break;
    }

  }

  /**
   * Carica la lista dei codici NUTS
   * 
   * @param livello
   * @param textsearch
   * @param jsonArray
   * @param selectTABCPVSUPP
   * @throws SQLException
   */
  private void popolaNUTS(String modopagina, Long livello, String textsearch, String codsearch, JSONArray jsonArray, String selectTABCPVSUPP)
      throws SQLException {
    List<?> datiNUTS = this.sqlManager.getListVector(selectTABCPVSUPP, new Object[] {});
    if (datiNUTS != null && datiNUTS.size() > 0) {
      for (int i = 0; i < datiNUTS.size(); i++) {

        String codice = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 4).getValue();
        String codicesearch = codice + "%";

        // Filtro di ricerca per descrizione anche parziale e multipla
        String addsearch = "";
        if ("VISUALIZZA".equals(modopagina)) {
          if (codsearch != null && !"".equals(codsearch.trim())) {
            addsearch = " and codice = '" + codsearch.trim() + "' ";
          }
        } else {
          if (codsearch != null && !"".equals(codsearch.trim())) {
            addsearch = " and codice = '" + codsearch.trim() + "' ";
          } else if (textsearch != null && !"".equals(textsearch.trim())) {
            addsearch = " and (";
            String[] words = textsearch.split(" ");
            for (int w = 0; w < words.length; w++) {
              String descsearch = StringUtils.replace(words[w], "_", "#_");
              descsearch = StringUtils.replace(descsearch, "%", "#%");
              descsearch = "%" + descsearch.toUpperCase() + "%";
              if (w > 0) addsearch += " and ";
              addsearch += " (upper(descrizione) like '" + descsearch + "' escape '#' or codice like '" + descsearch + "' escape '#')";
            }
            addsearch += " )";
          }
        }

        String selectNUTSFigli = "select count(*) from tabnuts where codice like ? and codice <> ?";
        if (addsearch != null && !"".equals(addsearch.trim())) selectNUTSFigli += addsearch;
        Long cntNUTSFigli = (Long) this.sqlManager.getObject(selectNUTSFigli, new Object[] { codicesearch, codice });

        if (addsearch != null && !"".equals(addsearch.trim())) {
          String selectNUTSRicerca = "select count(*) from tabnuts where codice like ? " + addsearch;
          Long cntNUTSRicerca = (Long) this.sqlManager.getObject(selectNUTSRicerca, new Object[] { codicesearch });
          if (cntNUTSRicerca != null && cntNUTSRicerca.longValue() > 0) {
            Object[] rowNUTS = new Object[10];
            rowNUTS[0] = new Long(livello.longValue() + 1);
            rowNUTS[1] = codice;
            rowNUTS[2] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 0).getValue();
            rowNUTS[3] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 1).getValue();
            rowNUTS[4] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 2).getValue();
            rowNUTS[5] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 3).getValue();
            rowNUTS[6] = codice;
            rowNUTS[7] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 5).getValue();
            rowNUTS[8] = cntNUTSFigli;
            rowNUTS[9] = cntNUTSRicerca;
            jsonArray.add(rowNUTS);
          }
        } else {
          Object[] rowNUTS = new Object[10];
          rowNUTS[0] = new Long(livello.longValue() + 1);
          rowNUTS[1] = codice;
          rowNUTS[2] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 0).getValue();
          rowNUTS[3] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 1).getValue();
          rowNUTS[4] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 2).getValue();
          rowNUTS[5] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 3).getValue();
          rowNUTS[6] = codice;
          rowNUTS[7] = (String) SqlManager.getValueFromVectorParam(datiNUTS.get(i), 5).getValue();
          rowNUTS[8] = cntNUTSFigli;
          rowNUTS[9] = cntNUTSFigli;
          jsonArray.add(rowNUTS);
        }
      }
    }
  }
}
