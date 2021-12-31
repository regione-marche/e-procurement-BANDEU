package it.eldasoft.sil.w3.web.struts;

import java.sql.SQLException;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;
import it.eldasoft.utils.metadata.cache.DizionarioCampi;
import it.eldasoft.utils.metadata.domain.Campo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.transaction.TransactionStatus;

public class SetPermessiAction extends Action {

  private SqlManager sqlManager;

  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    String clm1value_vc = null;
    Long clm1value_nu = null;

    String operation = request.getParameter("operation");
    String guperm = request.getParameter("guperm");
    String tblname = request.getParameter("tblname");
    String clm1name = request.getParameter("clm1name");
    Campo c = DizionarioCampi.getInstance().getCampoByNomeFisico(tblname + "." + clm1name);
    if (c.getTipoColonna() == 0) {
      clm1value_vc = request.getParameter("clm1value");
    } else if (c.getTipoColonna() == 2) {
      clm1value_nu = new Long(request.getParameter("clm1value"));
    }

    TransactionStatus status = null;
    boolean commitTransaction = false;
    try {
      status = this.sqlManager.startTransaction();

      if ("DELETE".equals(operation)) {

        String deletePERM = "delete from perm where guperm = ? and tblname = ? and clm1name = ? and (clm1value_nu = ? or clm1value_vc = ?)";
        this.sqlManager.update(deletePERM, new Object[] { guperm, tblname, clm1name, clm1value_nu, clm1value_vc });

      } else if ("INSERT".equals(operation)) {

        String insertPERM = "insert into perm (idperm, guperm, idgrp, syscon, r, w, x, tblname, clm1name, clm1value_nu, clm1value_vc, rwx) values (?,?,?,?,?,?,?,?,?,?,?,?)";

        Long idgrp = null;
        Long syscon = null;
        if ("USR".equals(guperm)) {
          syscon = new Long(request.getParameter("syscon"));
        } else if ("GRP".equals(guperm)) {
          idgrp = new Long(request.getParameter("idgrp"));
        }
        
        Long r = new Long(0);
        Long w = new Long(0);
        Long x = new Long(0);
        
        if (request.getParameter("r") != null) {
          r = new Long(request.getParameter("r"));
        }
        if (request.getParameter("w") != null) {
          w = new Long(request.getParameter("w"));
        }
        if (request.getParameter("x") != null) {
          x = new Long(request.getParameter("x"));
        }

        Long rwx = new Long(r.longValue() * 4 + w.longValue() * 2 + x.longValue()) ;
        
        Object[] obj = new Object[12];
        obj[0] = _getNextIdPerm();
        obj[1] = guperm;
        obj[2] = idgrp;
        obj[3] = syscon;
        obj[4] = r;
        obj[5] = w;
        obj[6] = x;
        obj[7] = tblname;
        obj[8] = clm1name;
        obj[9] = clm1value_nu;
        obj[10] = clm1value_vc;
        obj[11] = rwx;

        this.sqlManager.update(insertPERM, obj);

      }

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
