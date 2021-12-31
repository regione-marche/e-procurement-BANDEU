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
import org.springframework.transaction.TransactionStatus;

/**
 * @author Stefano.Cestaro
 * 
 */
public class GestioneAlberoUnitGrpAction extends Action {

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
   * <li>0 - Livello (0, 10, 11, 20, 21, 22)</li>
   * <li>1 - Descrizione del nodo</li>
   * <li>2 - Tipo root (UNIT: unita', GRP: gruppo)</li>
   * <li>3 - Codice unita' organizzativa (UNIT.IDUNIT)</li>
   * <li>4 - Codice gruppo (GRP.IDGRP)</li>
   * <li>5 - Codice utente (USRSYS.SYSCON)</li>
   * </ul>
   */
  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    response.setHeader("cache-control", "no-cache");
    response.setContentType("text/text;charset=utf-8");
    PrintWriter out = response.getWriter();

    String operation = request.getParameter("operation");

    JSONArray jsonArray = new JSONArray();

    if ("load".equals(operation)) {

      Long livello = null;
      if (request.getParameter("livello") != "") {
        livello = new Long(request.getParameter("livello"));
      }

      Long idunit = null;
      if (request.getParameter("idunit") != null && request.getParameter("idunit") != "") {
        idunit = new Long(request.getParameter("idunit"));
      }

      Long idgrp = null;
      if (request.getParameter("idgrp") != null && request.getParameter("idgrp") != "") {
        idgrp = new Long(request.getParameter("idgrp"));
      }

      switch (livello.intValue()) {
      case 0:
        this.popolaRoot(jsonArray);
        break;

      case 10:
        this.popolaUnit(jsonArray);
        break;

      case 11:
        this.popolaUnitGrp(idunit, jsonArray);
        this.popolaUnitUsrsys(idunit, jsonArray);
        break;

      case 12:
        this.popolaUnitGrpUsrsys(idgrp, idgrp, jsonArray);
        break;

      case 20:
        this.popolaGrp(jsonArray);
        break;

      case 21:
        this.popolaGrpUsrsys(idgrp, jsonArray);
        break;

      default:
        break;
      }

    } else if ("search".equals(operation)) {
      String textsearch = request.getParameter("textsearch");
      this.search(textsearch, jsonArray);

    } else if ("delete".equals(operation)) {
      String tipo = request.getParameter("tipo");
      if ("UNIT".equals(tipo)) {
        Long idunit = null;
        if (request.getParameter("idunit") != null && request.getParameter("idunit") != "")
          idunit = new Long(request.getParameter("idunit"));
        if (idunit != null) this.eliminaUNIT(idunit);
      } else if ("GRP".equals(tipo)) {
        Long idgrp = null;
        if (request.getParameter("idgrp") != null && request.getParameter("idgrp") != "") idgrp = new Long(request.getParameter("idgrp"));
        if (idgrp != null) this.eliminaGRP(idgrp);
      }

    }

    out.println(jsonArray);
    out.flush();

    return null;
  }

  /**
   * Popola i nodi root.
   * 
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaRoot(JSONArray jsonArray) throws SQLException {

    Object[] rowUnit = new Object[6];
    rowUnit[0] = "10";
    rowUnit[1] = "Unità organizzative";
    rowUnit[2] = "UNIT";
    rowUnit[3] = "";
    rowUnit[4] = "";
    rowUnit[5] = "";
    jsonArray.add(rowUnit);

    Object[] rowGrp = new Object[6];
    rowGrp[0] = "20";
    rowGrp[1] = "Gruppi";
    rowGrp[2] = "GRP";
    rowGrp[3] = "";
    rowGrp[4] = "";
    rowGrp[5] = "";
    jsonArray.add(rowGrp);

  }

  /**
   * Lista dei gruppi.
   * 
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaGrp(JSONArray jsonArray) throws SQLException {
    List<?> datiGRP = this.sqlManager.getListVector("select idgrp, descgrp from grp order by descgrp", new Object[] {});
    if (datiGRP != null && datiGRP.size() > 0) {
      for (int i = 0; i < datiGRP.size(); i++) {
        Long idgrp = (Long) SqlManager.getValueFromVectorParam(datiGRP.get(i), 0).getValue();
        String descgrp = (String) SqlManager.getValueFromVectorParam(datiGRP.get(i), 1).getValue();
        Object[] rowGrp = new Object[6];
        rowGrp[0] = "21";
        rowGrp[1] = descgrp;
        rowGrp[2] = "GRP";
        rowGrp[3] = "";
        rowGrp[4] = idgrp;
        rowGrp[5] = "";
        jsonArray.add(rowGrp);
      }
    }
  }

  /**
   * Lista degli utenti del gruppo.
   * 
   * @param idgrp
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaGrpUsrsys(Long idgrp, JSONArray jsonArray) throws SQLException {
    String selectUSRSYS = "select usrsys.syscon, "
        + " usrsys.sysute "
        + " from usrsys, grpusrsys "
        + " where usrsys.syscon = grpusrsys.syscon "
        + " and grpusrsys.idgrp = ? "
        + " order by usrsys.sysute ";

    List<?> datiUSRSYS = this.sqlManager.getListVector(selectUSRSYS, new Object[] { idgrp });
    if (datiUSRSYS != null && datiUSRSYS.size() > 0) {
      for (int i = 0; i < datiUSRSYS.size(); i++) {
        Long syscon = (Long) SqlManager.getValueFromVectorParam(datiUSRSYS.get(i), 0).getValue();
        String sysute = (String) SqlManager.getValueFromVectorParam(datiUSRSYS.get(i), 1).getValue();
        Object[] rowUsr = new Object[6];
        rowUsr[0] = "22";
        rowUsr[1] = sysute;
        rowUsr[2] = "GRP";
        rowUsr[3] = "";
        rowUsr[4] = idgrp;
        rowUsr[5] = syscon;
        jsonArray.add(rowUsr);
      }
    }
  }

  /**
   * Lista delle unita' organizzative.
   * 
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaUnit(JSONArray jsonArray) throws SQLException {
    List<?> datiUNIT = this.sqlManager.getListVector("select idunit, descunit from unit order by descunit", new Object[] {});
    if (datiUNIT != null && datiUNIT.size() > 0) {
      for (int i = 0; i < datiUNIT.size(); i++) {
        Long idunit = (Long) SqlManager.getValueFromVectorParam(datiUNIT.get(i), 0).getValue();
        String descunit = (String) SqlManager.getValueFromVectorParam(datiUNIT.get(i), 1).getValue();
        Object[] rowUnit = new Object[6];
        rowUnit[0] = "11";
        rowUnit[1] = descunit;
        rowUnit[2] = "UNIT";
        rowUnit[3] = idunit;
        rowUnit[4] = "";
        rowUnit[5] = "";
        jsonArray.add(rowUnit);
      }
    }
  }

  /**
   * Lista dei gruppi delle unita' organizzative.
   * 
   * @param idunit
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaUnitGrp(Long idunit, JSONArray jsonArray) throws SQLException {
    String selectGRP = "select grp.idgrp, "
        + " grp.descgrp "
        + " from grp, unitgrp "
        + " where grp.idgrp = unitgrp.idgrp "
        + " and unitgrp.idunit = ? "
        + " order by grp.descgrp ";

    List<?> datiGRP = this.sqlManager.getListVector(selectGRP, new Object[] { idunit });
    if (datiGRP != null && datiGRP.size() > 0) {
      for (int i = 0; i < datiGRP.size(); i++) {
        Long idgrp = (Long) SqlManager.getValueFromVectorParam(datiGRP.get(i), 0).getValue();
        String descgrp = (String) SqlManager.getValueFromVectorParam(datiGRP.get(i), 1).getValue();
        Object[] rowGrp = new Object[6];
        rowGrp[0] = "12";
        rowGrp[1] = descgrp;
        rowGrp[2] = "UNIT";
        rowGrp[3] = idunit;
        rowGrp[4] = idgrp;
        rowGrp[5] = "";
        jsonArray.add(rowGrp);
      }
    }
  }

  /**
   * Lista degli utenti del gruppo.
   * 
   * @param idgrp
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaUnitGrpUsrsys(Long idunit, Long idgrp, JSONArray jsonArray) throws SQLException {
    String selectUSRSYS = "select usrsys.syscon, "
        + " usrsys.sysute "
        + " from usrsys, grpusrsys "
        + " where usrsys.syscon = grpusrsys.syscon "
        + " and grpusrsys.idgrp = ? "
        + " order by usrsys.sysute ";

    List<?> datiUSRSYS = this.sqlManager.getListVector(selectUSRSYS, new Object[] { idgrp });
    if (datiUSRSYS != null && datiUSRSYS.size() > 0) {
      for (int i = 0; i < datiUSRSYS.size(); i++) {
        Long syscon = (Long) SqlManager.getValueFromVectorParam(datiUSRSYS.get(i), 0).getValue();
        String sysute = (String) SqlManager.getValueFromVectorParam(datiUSRSYS.get(i), 1).getValue();
        Object[] rowUsr = new Object[6];
        rowUsr[0] = "13";
        rowUsr[1] = sysute;
        rowUsr[2] = "UNIT";
        rowUsr[3] = idunit;
        rowUsr[4] = idgrp;
        rowUsr[5] = syscon;
        jsonArray.add(rowUsr);
      }
    }
  }

  /**
   * Lista degli utenti delle unita' organizzative.
   * 
   * @param idunit
   * @param jsonArray
   * @throws SQLException
   */
  private void popolaUnitUsrsys(Long idunit, JSONArray jsonArray) throws SQLException {
    String selectGRP = "select usrsys.syscon, "
        + " usrsys.sysute "
        + " from usrsys, unitusrsys "
        + " where usrsys.syscon = unitusrsys.syscon "
        + " and unitusrsys.idunit = ? "
        + " order by usrsys.sysute ";

    List<?> datiUSRSYS = this.sqlManager.getListVector(selectGRP, new Object[] { idunit });
    if (datiUSRSYS != null && datiUSRSYS.size() > 0) {
      for (int i = 0; i < datiUSRSYS.size(); i++) {
        Long syscon = (Long) SqlManager.getValueFromVectorParam(datiUSRSYS.get(i), 0).getValue();
        String sysute = (String) SqlManager.getValueFromVectorParam(datiUSRSYS.get(i), 1).getValue();
        Object[] rowUsr = new Object[6];
        rowUsr[0] = "14";
        rowUsr[1] = sysute;
        rowUsr[2] = "UNIT";
        rowUsr[3] = idunit;
        rowUsr[4] = "";
        rowUsr[5] = syscon;
        jsonArray.add(rowUsr);
      }
    }
  }

  /**
   * Ricerca per descrizione.
   * 
   * @param textsearch_any
   * @param jsonArray
   * @throws SQLException
   */
  private void search(String textsearch_any, JSONArray jsonArray) throws SQLException {

    String[] words = textsearch_any.split(" ");

    for (int ia = 0; ia < words.length; ia++) {
      String textsearch = words[ia];
      if (textsearch != null && textsearch.trim().length() >= 3 && !"".equals(textsearch.trim())) {

        textsearch = StringUtils.replace(textsearch, "_", "#_");
        textsearch = StringUtils.replace(textsearch, "%", "#%");
        textsearch = "%" + textsearch.toUpperCase() + "%";

        // Unita' organizzative
        String selectUNIT = "select distinct idunit from unit where (upper(descunit) like ? escape '#')";
        List<?> datiUNIT = this.sqlManager.getListVector(selectUNIT, new Object[] { textsearch });
        if (datiUNIT != null && datiUNIT.size() > 0) {
          jsonArray.add(new Object[] { "UNIT", "", "" });
        }

        // Gruppi dell'unita' organizzativa
        String selectUNITGRP = "select distinct unitgrp.idunit from grp, unitgrp "
            + " where grp.idgrp = unitgrp.idgrp "
            + " and (upper(grp.descgrp) like ? escape '#')";
        List<?> datiUNITGRP = this.sqlManager.getListVector(selectUNITGRP, new Object[] { textsearch });
        if (datiUNITGRP != null && datiUNITGRP.size() > 0) {
          for (int i = 0; i < datiUNITGRP.size(); i++) {
            Long idunit = (Long) SqlManager.getValueFromVectorParam(datiUNITGRP.get(i), 0).getValue();
            jsonArray.add(new Object[] { "UNIT", idunit, "" });
          }
        }

        // Utenti dei gruppi dell'unita' organizzativa
        String selectUNITGRPUSERSYS = "select distinct unitgrp.idunit, unitgrp.idgrp from unitgrp, grpusrsys, usrsys "
            + " where unitgrp.idgrp = grpusrsys.idgrp "
            + " and grpusrsys.syscon = usrsys.syscon "
            + " and (upper(usrsys.sysute) like ? escape '#')";
        List<?> datiUNITGRPUSERSYS = this.sqlManager.getListVector(selectUNITGRPUSERSYS, new Object[] { textsearch });
        if (datiUNITGRPUSERSYS != null && datiUNITGRPUSERSYS.size() > 0) {
          for (int i = 0; i < datiUNITGRPUSERSYS.size(); i++) {
            Long idunit = (Long) SqlManager.getValueFromVectorParam(datiUNITGRPUSERSYS.get(i), 0).getValue();
            Long idgrp = (Long) SqlManager.getValueFromVectorParam(datiUNITGRPUSERSYS.get(i), 1).getValue();
            jsonArray.add(new Object[] { "UNIT", idunit, "" });
            jsonArray.add(new Object[] { "UNIT", idunit, idgrp });
          }
        }

        // Utenti dell'unita' organizzativa
        String selectUNITUSRSYS = "select distinct unitusrsys.idunit from usrsys, unitusrsys "
            + " where usrsys.syscon = unitusrsys.syscon "
            + " and (upper(usrsys.sysute) like ? escape '#')";
        List<?> datiUNITUSRSYS = this.sqlManager.getListVector(selectUNITUSRSYS, new Object[] { textsearch });
        if (datiUNITUSRSYS != null && datiUNITUSRSYS.size() > 0) {
          for (int i = 0; i < datiUNITUSRSYS.size(); i++) {
            Long idunit = (Long) SqlManager.getValueFromVectorParam(datiUNITUSRSYS.get(i), 0).getValue();
            jsonArray.add(new Object[] { "UNIT", idunit, "" });
          }
        }

        // Gruppi
        String selectGRP = "select distinct idgrp from grp where (upper(descgrp) like ? escape '#')";
        List<?> datiGRP = this.sqlManager.getListVector(selectGRP, new Object[] { textsearch });
        if (datiGRP != null && datiGRP.size() > 0) {
          jsonArray.add(new Object[] { "GRP", "", "" });
        }

        // Utenti del gruppo
        String selectGRPUSRSYS = "select distinct grpusrsys.idgrp from usrsys, grpusrsys "
            + " where usrsys.syscon = grpusrsys.syscon "
            + " and (upper(usrsys.sysute) like ? escape '#')";
        List<?> datiGRPUSRSYS = this.sqlManager.getListVector(selectGRPUSRSYS, new Object[] { textsearch });
        if (datiGRPUSRSYS != null && datiGRPUSRSYS.size() > 0) {
          for (int i = 0; i < datiGRPUSRSYS.size(); i++) {
            Long idgrp = (Long) SqlManager.getValueFromVectorParam(datiGRPUSRSYS.get(i), 0).getValue();
            jsonArray.add(new Object[] { "GRP", "", idgrp });
          }
        }

      }
    }
  }

  /**
   * Eliminazione delle'unita' organizzativa.
   * 
   * @param idunit
   * @throws SQLException
   */
  private void eliminaUNIT(Long idunit) throws SQLException {
    if (idunit != null) {
      TransactionStatus status = null;
      boolean commitTransaction = false;
      try {
        status = this.sqlManager.startTransaction();
        this.sqlManager.update("delete from unitusrsys where idunit = ?", new Object[] { idunit });
        this.sqlManager.update("delete from unitgrp where idunit = ?", new Object[] { idunit });
        this.sqlManager.update("delete from unit where idunit = ?", new Object[] { idunit });
        commitTransaction = true;
      } catch (Exception e) {
        commitTransaction = false;
      } finally {
        if (status != null) {
          if (commitTransaction) {
            this.sqlManager.commitTransaction(status);
          } else {
            this.sqlManager.rollbackTransaction(status);
          }
        }
      }
    }
  }

  /**
   * Eliminazione del gruppo.
   * 
   * @param idgrp
   * @throws SQLException
   */
  private void eliminaGRP(Long idgrp) throws SQLException {
    if (idgrp != null) {
      TransactionStatus status = null;
      boolean commitTransaction = false;
      try {
        status = this.sqlManager.startTransaction();
        this.sqlManager.update("delete from grpusrsys where idgrp = ?", new Object[] { idgrp });
        this.sqlManager.update("delete from grp where idgrp = ?", new Object[] { idgrp });
        commitTransaction = true;
      } catch (Exception e) {
        commitTransaction = false;
      } finally {
        if (status != null) {
          if (commitTransaction) {
            this.sqlManager.commitTransaction(status);
          } else {
            this.sqlManager.rollbackTransaction(status);
          }
        }
      }
    }
  }

}
