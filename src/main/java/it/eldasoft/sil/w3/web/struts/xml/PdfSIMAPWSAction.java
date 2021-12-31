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

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.commons.web.struts.ActionBaseNoOpzioni;
import it.eldasoft.gene.commons.web.struts.CostantiGeneraliStruts;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.bl.GestioneServiziSIMAPManager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PdfSIMAPWSAction extends ActionBaseNoOpzioni {

  protected static final String       FORWARD_SUCCESS = "pdfsimapwssuccess";
  protected static final String       FORWARD_ERROR   = "pdfsimapwserror";

  static Logger                       logger          = Logger.getLogger(InviaSIMAPWSAction.class);

  private GestioneServiziSIMAPManager gestioneServiziSIMAPManager;

  public void setGestioneServiziSIMAPManager(GestioneServiziSIMAPManager gestioneServiziSIMAPManager) {
    this.gestioneServiziSIMAPManager = gestioneServiziSIMAPManager;
  }

  protected ActionForward runAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    if (logger.isDebugEnabled()) logger.debug("InviaSIMAPWSAction: inizio metodo");

    String target = null;
    String messageKey = null;

    try {
      String entita = request.getParameter("entita");
      Long id = new Long(request.getParameter("id"));
      String phase = request.getParameter("phase");
      String operation = request.getParameter("operation");

      request.getSession().setAttribute("entita", entita);
      request.getSession().setAttribute("id", id);
      request.getSession().setAttribute("numeroPopUp", "1");
      request.getSession().setAttribute("phase", phase);
      request.getSession().setAttribute("operation", operation);

      ProfiloUtente profilo = (ProfiloUtente) request.getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
      Long syscon = new Long(profilo.getId());

      byte[] pdf = gestioneServiziSIMAPManager.renderNotice(syscon, phase, id);

      // Restituisco il documento
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=\"SIMAP_" + id.toString() + ".pdf" + "\"");
      ServletOutputStream output = response.getOutputStream();
      output.write(pdf);
      output.flush();

    } catch (GestoreException e) {
      target = FORWARD_ERROR;
      messageKey = "errors.gestioneInviaSIMAPWS.error";
      logger.error(this.resBundleGenerale.getString(messageKey), e);
      this.aggiungiMessaggio(request, messageKey, e.getMessage());
    } catch (Throwable e) {
      target = CostantiGeneraliStruts.FORWARD_ERRORE_GENERALE;
      messageKey = "errors.applicazione.inaspettataException";
      logger.error(this.resBundleGenerale.getString(messageKey), e);
      this.aggiungiMessaggio(request, messageKey);
    }

    if (messageKey != null) response.reset();

    if (logger.isDebugEnabled()) logger.debug("InviaSIMAPWSAction: fine metodo");

    return mapping.findForward(target);

  }

}
