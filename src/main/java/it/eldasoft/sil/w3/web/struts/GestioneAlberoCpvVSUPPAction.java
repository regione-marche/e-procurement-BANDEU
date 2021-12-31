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
public class GestioneAlberoCpvVSUPPAction extends Action {

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
    String cpvvsuppsezione = request.getParameter("cpvvsuppsezione");
    String cpvvsuppgruppo = request.getParameter("cpvvsuppgruppo");
    String cpvvsuppdivisione = request.getParameter("cpvvsuppdivisione");
    String textsearch = request.getParameter("textsearch");

    JSONArray jsonArray = new JSONArray();

    popolaCpvVSUPP(livello, cpvvsuppsezione, cpvvsuppgruppo, cpvvsuppdivisione, textsearch, jsonArray);

    out.println(jsonArray);
    out.flush();

    return null;
  }

  /**
   * Carica i nodi dell'albero
   * 
   * @param livello
   * @param cpvvsuppsezione
   * @param cpvvsuppgruppo
   * @param cpvvsuppdivisione
   * @param textsearch
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaCpvVSUPP(Long livello, String cpvvsuppsezione, String cpvvsuppgruppo, String cpvvsuppdivisione, String textsearch,
      JSONArray jsonArray) throws SQLException {

    String selectTABCPVSUPP = "";

    switch (livello.intValue()) {

    case 1:
      // Sezione
      selectTABCPVSUPP = "select sezione, gruppo, divisione, codice, descrizione from tabcpvsupp where gruppo = '0' and divisione = '0' order by codice";
      this.popolaCpvVSUPP(livello, textsearch, jsonArray, selectTABCPVSUPP);
      break;

    case 2:
      // Gruppo
      selectTABCPVSUPP = "select sezione, gruppo, divisione, codice, descrizione from tabcpvsupp where sezione = '"
          + cpvvsuppsezione
          + "' and gruppo <> '0' and divisione = '0' order by codice";
      this.popolaCpvVSUPP(livello, textsearch, jsonArray, selectTABCPVSUPP);
      break;

    case 3:
      // Divisione
      selectTABCPVSUPP = "select sezione, gruppo, divisione, codice, descrizione from tabcpvsupp where sezione = '"
          + cpvvsuppsezione
          + "' and gruppo = '"
          + cpvvsuppgruppo
          + "' and divisione <> '0' order by codice";
      this.popolaCpvVSUPP(livello, textsearch, jsonArray, selectTABCPVSUPP);
      break;

    default:
      break;
    }

  }

   /**
   * Carica la lista dei CPV.
   * @param livello
   * @param textsearch
   * @param jsonArray
   * @param selectTABCPVSUPP
   * @throws SQLException
   */
  private void popolaCpvVSUPP(Long livello, String textsearch, JSONArray jsonArray, String selectTABCPVSUPP) throws SQLException {
    List<?> datiTABCPVSUPP = this.sqlManager.getListVector(selectTABCPVSUPP, new Object[] {});
    if (datiTABCPVSUPP != null && datiTABCPVSUPP.size() > 0) {
      for (int i = 0; i < datiTABCPVSUPP.size(); i++) {

        String cpvvsuppcodice = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 3).getValue();
        String cpvvsuppcodicesearch = cpvvsuppcodice + "%";
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
            addsearch += " (upper(descrizione) like '" + cpvdescsearch + "' escape '#' or codice like '" + cpvdescsearch + "' escape '#')";
          }
          addsearch += " )";
        }

        String selectCPVSUPPFigli = "select count(*) from tabcpvsupp where codice like ? and codice <> ?";
        if (addsearch != null && !"".equals(addsearch.trim())) selectCPVSUPPFigli += addsearch;
        Long cntCPVSUPPFigli = (Long) this.sqlManager.getObject(selectCPVSUPPFigli, new Object[] { cpvvsuppcodicesearch, cpvvsuppcodice });

        if (addsearch != null && !"".equals(addsearch.trim())) {
          String selectCPVSUPPRicerca = "select count(*) from tabcpvsupp where codice like ? " + addsearch;
          Long cntCPVSUPPRicerca = (Long) this.sqlManager.getObject(selectCPVSUPPRicerca, new Object[] { cpvvsuppcodicesearch });
          if (cntCPVSUPPRicerca != null && cntCPVSUPPRicerca.longValue() > 0) {
            Object[] rowCpvVSUPP = new Object[9];
            rowCpvVSUPP[0] = new Long(livello.longValue() + 1);
            rowCpvVSUPP[1] = cpvvsuppcodice;
            rowCpvVSUPP[2] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 0).getValue();
            rowCpvVSUPP[3] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 1).getValue();
            rowCpvVSUPP[4] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 2).getValue();
            rowCpvVSUPP[5] = cpvvsuppcodice;
            rowCpvVSUPP[6] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 4).getValue();
            rowCpvVSUPP[7] = cntCPVSUPPFigli;
            rowCpvVSUPP[8] = cntCPVSUPPRicerca;
            jsonArray.add(rowCpvVSUPP);
          }
        } else {
          Object[] rowCpvVSUPP = new Object[9];
          rowCpvVSUPP[0] = new Long(livello.longValue() + 1);
          rowCpvVSUPP[1] = cpvvsuppcodice;
          rowCpvVSUPP[2] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 0).getValue();
          rowCpvVSUPP[3] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 1).getValue();
          rowCpvVSUPP[4] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 2).getValue();
          rowCpvVSUPP[5] = cpvvsuppcodice;
          rowCpvVSUPP[6] = (String) SqlManager.getValueFromVectorParam(datiTABCPVSUPP.get(i), 4).getValue();
          rowCpvVSUPP[7] = cntCPVSUPPFigli;
          rowCpvVSUPP[8] = cntCPVSUPPFigli;
          jsonArray.add(rowCpvVSUPP);
        }
      }
    }
  }
}
