package it.eldasoft.sil.w3.web.struts;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.commons.web.spring.DataSourceTransactionManagerBase;
import it.eldasoft.sil.w3.bl.ValidazioneSIMAPManager;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GetStatoValidazioneW3ANNEXBSIMAPAction extends Action {

  private ValidazioneSIMAPManager validazioneSIMAPManager;

  public void setValidazioneSIMAPManager(ValidazioneSIMAPManager validazioneSIMAPManager) {
    this.validazioneSIMAPManager = validazioneSIMAPManager;
  }

  public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    DataSourceTransactionManagerBase.setRequest(request);

    response.setHeader("cache-control", "no-cache");
    response.setContentType("text/text;charset=utf-8");
    PrintWriter out = response.getWriter();

    String id = request.getParameter("id");
    String num = request.getParameter("num");
    ProfiloUtente profilo = (ProfiloUtente) request.getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    HashMap<?, ?> infoValidazione = validazioneSIMAPManager.validateW3ANNEXB(new Long(id), new Long(num), new Long(profilo.getId()));
    JSONObject jsonResult = JSONObject.fromObject(infoValidazione);
    out.println(jsonResult);
    out.flush();

    return null;

  }

}
