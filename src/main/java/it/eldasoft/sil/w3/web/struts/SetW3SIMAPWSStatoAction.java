package it.eldasoft.sil.w3.web.struts;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.transaction.TransactionStatus;

public class SetW3SIMAPWSStatoAction extends Action {

  private SqlManager sqlManager;

  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    String id = request.getParameter("id");
    String num = request.getParameter("num");
    String stato = request.getParameter("stato");

    TransactionStatus status = null;
    boolean commitTransaction = false;
    try {
      status = this.sqlManager.startTransaction();

      String updateW3SIMAPWS = "update w3simapws set stato = ? where id = ? and num = ?";
      this.sqlManager.update(updateW3SIMAPWS, new Object[] { new Long(stato), new Long(id), new Long(num) });

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
    return null;
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private Long _getNextIdPerm() throws SQLException {
    Long nextIdPerm = (Long) this.sqlManager.getObject("select max(idperm) from perm", new Object[] {});
    if (nextIdPerm == null) nextIdPerm = new Long(0);
    nextIdPerm = new Long(nextIdPerm.longValue() + 1);
    return nextIdPerm;
  }

}
