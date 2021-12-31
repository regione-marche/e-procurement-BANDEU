package it.eldasoft.sil.w3.web.struts;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;
import it.eldasoft.utils.properties.ConfigManager;
import net.sf.json.JSONObject;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GetListaGruppiUnitaAction extends Action {

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

  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    response.setHeader("cache-control", "no-cache");
    response.setContentType("text/text;charset=utf-8");
    PrintWriter out = response.getWriter();

    JSONObject result = new JSONObject();
    int total = 0;
    int totalAfterFilter = 0;

    String operation = request.getParameter("operation");
    List<?> hmGRP = null;

    try {

      String selectGRP = "";

      if ("VISUALIZZA".equals(operation)) {
        selectGRP = "select grp.idgrp, "
            + " grp.descgrp, "
            + " unitgrp.idunit "
            + " from grp, unitgrp "
            + " where grp.idgrp = unitgrp.idgrp "
            + " and unitgrp.idunit = ? "
            + " order by grp.descgrp";
        Long idunit = new Long(request.getParameter("idunit"));
        hmGRP = sqlManager.getListHashMap(selectGRP, new Object[] { idunit });
      } else if ("MODIFICA".equals(operation)) {
        selectGRP = "select grp.idgrp, "
            + " grp.descgrp, "
            + " unitgrp.idunit "
            + " from unitgrp right join grp on grp.idgrp = unitgrp.idgrp "
            + " and unitgrp.idunit = ? "
            + " order by grp.descgrp";
        Long idunit = new Long(request.getParameter("idunit"));
        hmGRP = sqlManager.getListHashMap(selectGRP, new Object[] { idunit });
      } else if ("NUOVO".equals(operation)) {
        String dbms = ConfigManager.getValore(CostantiGenerali.PROP_DATABASE);
        if ("POS".equals(dbms)) {
          selectGRP = "select grp.idgrp, "
            + " grp.descgrp, "
            + " null::NUMERIC "
            + " from grp  "
            + " order by grp.descgrp";
        } else {
          selectGRP = "select grp.idgrp, "
              + " grp.descgrp, "
              + " null "
              + " from grp  "
              + " order by grp.descgrp";
        }
        hmGRP = sqlManager.getListHashMap(selectGRP, new Object[] {});
      }

      if (hmGRP != null && hmGRP.size() > 0) {
        total = hmGRP.size();
        totalAfterFilter = hmGRP.size();
      }

      result.put("iTotalRecords", total);
      result.put("iTotalDisplayRecords", totalAfterFilter);
      result.put("data", hmGRP);

    } catch (SQLException e) {
      throw new JspException("Errore durante la lettura della lista dei gruppi", e);
    }

    out.println(result);
    out.flush();

    return null;

  }

}
