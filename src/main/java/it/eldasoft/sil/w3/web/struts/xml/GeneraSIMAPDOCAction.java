/*
 * Created on 29/06/2011
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.web.struts.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.commons.web.struts.ActionBaseNoOpzioni;
import it.eldasoft.gene.commons.web.struts.CostantiGeneraliStruts;
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

public class GeneraSIMAPDOCAction extends ActionBaseNoOpzioni {

  static Logger                       logger = Logger.getLogger(GeneraSIMAPDOCAction.class);

  private SqlManager                  sqlManager;

  private GestioneServiziSIMAPManager gestioneServiziSIMAPManager;

  public void setGestioneServiziSIMAPManager(GestioneServiziSIMAPManager gestioneServiziSIMAPManager) {
    this.gestioneServiziSIMAPManager = gestioneServiziSIMAPManager;
  }

  /**
   * 
   * @param sqlManager
   */
  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  protected ActionForward runAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    if (logger.isDebugEnabled()) logger.debug("GeneraSIMAPDOCAction: inizio metodo");

    String target = null;
    String messageKey = null;

    try {
      Long id = new Long(request.getParameter("id"));
      String formulario = (String) this.sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });

      // Chiamata al servizio REST
      ProfiloUtente profilo = (ProfiloUtente) request.getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
      Long syscon = new Long(profilo.getId());
      byte[] pdf = gestioneServiziSIMAPManager.renderNotice(syscon, "BETA", id);

      // Restituisco il documento
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=\"" + formulario + "_" + id.toString() + ".pdf" + "\"");
      ServletOutputStream output = response.getOutputStream();
      output.write(pdf);
      output.flush();

    } catch (Throwable e) {
      target = CostantiGeneraliStruts.FORWARD_ERRORE_GENERALE;
      messageKey = "errors.applicazione.inaspettataException";
      logger.error(this.resBundleGenerale.getString(messageKey), e);
      this.aggiungiMessaggio(request, messageKey);
    }

    if (messageKey != null) response.reset();

    if (logger.isDebugEnabled()) logger.debug("GeneraSIMAPDOCAction: fine metodo");

    return mapping.findForward(target);

  }
}
