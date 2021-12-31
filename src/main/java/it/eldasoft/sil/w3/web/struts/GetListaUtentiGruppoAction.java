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

public class GetListaUtentiGruppoAction extends Action {

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
    List<?> hmUSR = null;

    try {

      String selectUSR = "";

      if ("VISUALIZZA".equals(operation)) {
        selectUSR = "select usrsys.syscon, "
            + " usrsys.sysute, "
            + " usrsys.email, "
            + " grpusrsys.idgrp "
            + " from usrsys, grpusrsys "
            + " where usrsys.syscon = grpusrsys.syscon "
            + " and grpusrsys.idgrp = ? "
            + " order by usrsys.sysute";
        Long idgrp = new Long(request.getParameter("idgrp"));
        hmUSR = sqlManager.getListHashMap(selectUSR, new Object[] { idgrp });
      } else if ("MODIFICA".equals(operation)) {
        selectUSR = "select usrsys.syscon, "
            + " usrsys.sysute, "
            + " usrsys.email, "
            + " grpusrsys.idgrp "
            + " from grpusrsys right join usrsys on usrsys.syscon = grpusrsys.syscon "
            + " and grpusrsys.idgrp = ? "
            + " order by usrsys.sysute";
        Long idgrp = new Long(request.getParameter("idgrp"));
        hmUSR = sqlManager.getListHashMap(selectUSR, new Object[] { idgrp });
      } else if ("NUOVO".equals(operation)) {
        String dbms = ConfigManager.getValore(CostantiGenerali.PROP_DATABASE);
        if ("POS".equals(dbms)) {
          selectUSR = "select usrsys.syscon, "
              + " usrsys.sysute, "
              + " usrsys.email, "
              + " null::NUMERIC "
              + " from usrsys  "
              + " order by usrsys.sysute";
        } else {
          selectUSR = "select usrsys.syscon, "
              + " usrsys.sysute, "
              + " usrsys.email, "
              + " null "
              + " from usrsys  "
              + " order by usrsys.sysute";
        }
        hmUSR = sqlManager.getListHashMap(selectUSR, new Object[] {});
      }

      if (hmUSR != null && hmUSR.size() > 0) {
        total = hmUSR.size();
        totalAfterFilter = hmUSR.size();
      }

      result.put("iTotalRecords", total);
      result.put("iTotalDisplayRecords", totalAfterFilter);
      result.put("data", hmUSR);

    } catch (SQLException e) {
      throw new JspException("Errore durante la lettura della lista degli utenti", e);
    }

    out.println(result);
    out.flush();

    return null;

  }

}
