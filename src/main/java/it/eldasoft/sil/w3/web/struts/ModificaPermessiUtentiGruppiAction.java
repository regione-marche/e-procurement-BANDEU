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
package it.eldasoft.sil.w3.web.struts;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.struts.ActionBaseNoOpzioni;
import it.eldasoft.gene.commons.web.struts.CostantiGeneraliStruts;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ModificaPermessiUtentiGruppiAction extends ActionBaseNoOpzioni {

  protected static final String FORWARD_MODIFICA = "modifica";

  static Logger                 logger             = Logger.getLogger(ModificaPermessiUtentiGruppiAction.class);

  protected ActionForward runAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    if (logger.isDebugEnabled()) logger.debug("ModificaPermessiUtentiGruppiAction: inizio metodo");

    String target = FORWARD_MODIFICA;
    String messageKey = null;

    String tblname = request.getParameter("tblname");
    String clm1name = request.getParameter("clm1name");
    String clm1value = request.getParameter("clm1value");
    String operation = "MODIFICA";

    try {

      request.getSession().setAttribute("tblname", tblname);
      request.getSession().setAttribute("clm1name", clm1name);
      request.getSession().setAttribute("clm1value", clm1value);
      request.getSession().setAttribute("operation", operation);
      request.setAttribute(CostantiGenerali.NAVIGAZIONE_DISABILITATA, CostantiGenerali.DISABILITA_NAVIGAZIONE);

    } catch (Throwable e) {
      target = CostantiGeneraliStruts.FORWARD_ERRORE_GENERALE;
      messageKey = "errors.applicazione.inaspettataException";
      logger.error(this.resBundleGenerale.getString(messageKey), e);
      this.aggiungiMessaggio(request, messageKey);
    }

    if (messageKey != null) response.reset();

    if (logger.isDebugEnabled()) logger.debug("ModificaPermessiUtentiGruppiAction: fine metodo");

    return mapping.findForward(target);

  }

}
