package it.eldasoft.sil.w3.web.struts;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;
import it.eldasoft.utils.metadata.cache.DizionarioCampi;
import it.eldasoft.utils.metadata.domain.Campo;
import net.sf.json.JSONObject;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GetListaPermessiUtentiAction extends Action {

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
    String tblname = request.getParameter("tblname");
    String clm1name = request.getParameter("clm1name");
    String clm1value_vc = null;
    Long clm1value_nu = null;
    Campo c = DizionarioCampi.getInstance().getCampoByNomeFisico(tblname + "." + clm1name);
    if (c.getTipoColonna() == 0) {
      clm1value_vc = request.getParameter("clm1value");
    } else if (c.getTipoColonna() == 2) {
      clm1value_nu = new Long(request.getParameter("clm1value"));
    }

    ProfiloUtente profilo = (ProfiloUtente) request.getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    String sysab3 = profilo.getAbilitazioneStd();
    Long syscon = new Long(profilo.getId());

    try {

      String selectUSR = "";
      Object params[] = null;

      if ("VISUALIZZA".equals(operation)) {
        selectUSR = "select usrsys.syscon, "
            + " usrsys.sysute, "
            + " usrsys.email, "
            + " perm.idperm, "
            + " perm.r, "
            + " perm.w, "
            + " perm.x "
            + " from usrsys, perm "
            + " where usrsys.syscon = perm.syscon "
            + " and perm.guperm = ? "
            + " and perm.tblname = ? "
            + " and perm.clm1name = ? "
            + " and (perm.clm1value_nu = ? or perm.clm1value_vc = ?) ";
        params = new Object[5];
        params[0] = "USR";
        params[1] = tblname;
        params[2] = clm1name;
        params[3] = clm1value_nu;
        params[4] = clm1value_vc;

      } else if ("MODIFICA".equals(operation)) {
        selectUSR = "select usrsys.syscon, "
            + " usrsys.sysute, "
            + " usrsys.email, "
            + " perm.idperm, "
            + " perm.r, "
            + " perm.w, "
            + " perm.x "
            + " from usrsys, perm "
            + " where usrsys.syscon = perm.syscon "
            + " and perm.guperm = ? "
            + " and perm.tblname = ? "
            + " and perm.clm1name = ? "
            + " and (perm.clm1value_nu = ? or perm.clm1value_vc = ?) "
            + " union "
            + " select usrsys.syscon, "
            + " usrsys.sysute, "
            + " usrsys.email, "
            + " perm.idperm, "
            + " perm.r, "
            + " perm.w, "
            + " perm.x "
            + " from perm right join usrsys on usrsys.syscon = perm.syscon "
            + " and perm.guperm = ? "
            + " and perm.tblname = ? "
            + " and perm.clm1name = ? "
            + " and (perm.clm1value_nu = ? or perm.clm1value_vc = ?) ";

        params = new Object[10];
        params[0] = "USR";
        params[1] = tblname;
        params[2] = clm1name;
        params[3] = clm1value_nu;
        params[4] = clm1value_vc;
        params[5] = "USR";
        params[6] = tblname;
        params[7] = clm1name;
        params[8] = clm1value_nu;
        params[9] = clm1value_vc;

        if ("U".equals(sysab3)) {
          selectUSR += " where (usrsys.syscon in "
              + " (select syscon from v_unit_all_usrsys where idunit in (select idunit from v_unit_all_usrsys where syscon = "
              + syscon.toString()
              + ")) or usrsys.syscon = "
              + syscon.toString()
              + ") ";
        }

      }

      List<?> hmUSR = sqlManager.getListHashMap(selectUSR, params);
      if (hmUSR != null && hmUSR.size() > 0) {
        total = hmUSR.size();
        totalAfterFilter = hmUSR.size();
      }

      result.put("iTotalRecords", total);
      result.put("iTotalDisplayRecords", totalAfterFilter);
      result.put("data", hmUSR);

    } catch (SQLException e) {
      throw new JspException("Errore durante la lettura dei permessi degli utenti", e);
    }

    out.println(result);
    out.flush();

    return null;

  }

}
