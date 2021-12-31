/*
 * Created on 15/nov/2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.web.struts.xml;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.commons.web.struts.ActionBaseNoOpzioni;
import it.eldasoft.gene.commons.web.struts.CostantiGeneraliStruts;
import it.eldasoft.sil.w3.bl.ValidazioneSIMAPManager;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ComponiSIMAPEmailAction extends ActionBaseNoOpzioni {

  protected static final String    FORWARD_ERROR     = "componiemailsimaperror";
  protected static final String    FORWARD_CONTROLLO = "componiemailsimapcontrollo";
  protected static final String    FORWARD_SUCCESS   = "componiemailsimapsuccess";

  static Logger                    logger            = Logger.getLogger(ComponiSIMAPEmailAction.class);

  private ValidazioneSIMAPManager  validazioneSIMAPManager;

  public void setValidazioneSIMAPManager(ValidazioneSIMAPManager validazioneSIMAPManager) {
    this.validazioneSIMAPManager = validazioneSIMAPManager;
  }

  protected ActionForward runAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    if (logger.isDebugEnabled()) logger.debug("ComponiEmailSimapAction: inizio metodo");

    String target = FORWARD_SUCCESS;
    String messageKey = null;

    Long id = new Long(request.getParameter("id"));

    int numeroErrori = 0;
    int numeroWarning = 0;

    try {
      
      ProfiloUtente profilo = (ProfiloUtente) request.getSession().getAttribute(
          CostantiGenerali.PROFILO_UTENTE_SESSIONE);
      HashMap infoValidazione = validazioneSIMAPManager.validate(id, new Long(profilo.getId()));
      
      if (infoValidazione.get("numeroErrori") != null) {
        numeroErrori = ((Long) infoValidazione.get("numeroErrori")).intValue();
      }
      if (infoValidazione.get("numeroWarning") != null) {
        numeroWarning = ((Long) infoValidazione.get("numeroWarning")).intValue();
      }

      if (numeroErrori > 0 || numeroWarning > 0) {
        target = FORWARD_CONTROLLO;
        request.getSession().setAttribute("id", id);
        request.getSession().setAttribute("numeroPopUp", "1");
      } else {
        target = FORWARD_SUCCESS;
        request.getSession().setAttribute("id", id);
        request.getSession().setAttribute("numeroPopUp", "1");
      }

    } catch (Throwable e) {
      target = CostantiGeneraliStruts.FORWARD_ERRORE_GENERALE;
      messageKey = "errors.applicazione.inaspettataException";
      logger.error(this.resBundleGenerale.getString(messageKey), e);
      this.aggiungiMessaggio(request, messageKey);
    }

    if (messageKey != null) response.reset();

    if (logger.isDebugEnabled()) logger.debug("ComponiEmailSimapAction: fine metodo");

    return mapping.findForward(target);

  }

}
