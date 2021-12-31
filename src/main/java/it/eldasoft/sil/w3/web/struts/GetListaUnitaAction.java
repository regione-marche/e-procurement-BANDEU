package it.eldasoft.sil.w3.web.struts;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;
import net.sf.json.JSONArray;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GetListaUnitaAction extends Action {

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

    JSONArray jsonArray = new JSONArray();

    try {
      String selectUNIT = "select idunit, descunit from unit order by descunit";
      List<?> datiUNIT = this.sqlManager.getListVector(selectUNIT, new Object[] {});
      if (datiUNIT != null && datiUNIT.size() > 0) {
        jsonArray = JSONArray.fromObject(datiUNIT.toArray());
      }
    } catch (SQLException e) {
      throw new JspException("Errore durante la lettura delle unita' organizzative", e);
    }
    out.println(jsonArray);
    out.flush();

    return null;

  }

}
