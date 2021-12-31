package it.eldasoft.sil.w3.web.struts.xml;

import it.eldasoft.gene.commons.web.struts.ActionBaseNoOpzioni;
import it.eldasoft.gene.commons.web.struts.CostantiGeneraliStruts;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.bl.AttachmentManager;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Esegue il download di un file PDF allegato ad una gara o ad un appalto 
 */
public class VisualizzaAttachmentAction extends ActionBaseNoOpzioni {

  static Logger     logger = Logger.getLogger(VisualizzaAttachmentAction.class);

  private AttachmentManager attachmentManager;


  /**
   * 
   * @param fileAllegatoManager
   */
  public void setAttachmentManager(AttachmentManager attachmentManager) {
    this.attachmentManager = attachmentManager;
  }


  protected ActionForward runAction(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    if(logger.isDebugEnabled()) logger.debug("runAction: inizio metodo");

    String target = null;
    String messageKey = null;
    HashMap params = new HashMap();

    try {
      Long id = new Long(request.getParameter("id"));
      Long num = new Long(request.getParameter("num"));
      String attachment_name = new String(request.getParameter("attachment_name"));
      
      params.put("id", id);
      params.put("num", num);
      
      this.attachmentManager.downloadAttachment(attachment_name, params, response);

    } catch (IOException io){
      target = CostantiGeneraliStruts.FORWARD_ERRORE_GENERALE;
      messageKey = "errors.download";
      this.aggiungiMessaggio(request, messageKey);
    } catch (GestoreException e) {
   	 	logger.error("Errore nella visualizzazione/download del file allegato", e);
	    target = CostantiGeneraliStruts.FORWARD_ERRORE_GENERALE;
      	messageKey = "errors.download";
      	this.aggiungiMessaggio(request, messageKey);
	}
    if(logger.isDebugEnabled()) logger.debug("runAction: fine metodo");

    if(target != null)
      return mapping.findForward(target);
    else
      return null;
  }

}
