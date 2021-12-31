package it.eldasoft.sil.w3.tags.funzioni;


import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.sil.w3.bl.ValidazioneSIMAPManager;
import it.eldasoft.utils.spring.UtilitySpring;
import java.util.HashMap;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

public class GestioneValidazioneSIMAPFunction extends AbstractFunzioneTag {

  static Logger logger = Logger.getLogger(GestioneValidazioneSIMAPFunction.class);

  public GestioneValidazioneSIMAPFunction() {
    super(3, new Class[] { PageContext.class, String.class, Integer.class });
  }

  public String function(PageContext pageContext, Object params[])
      throws JspException {
    
    ValidazioneSIMAPManager validazioneSIMAPManager = (ValidazioneSIMAPManager) UtilitySpring.getBean(
        "validazioneSIMAPManager", pageContext,
        ValidazioneSIMAPManager.class);

    HashMap infoValidazione = validazioneSIMAPManager.validate(params);
    pageContext.setAttribute("titolo", infoValidazione.get("titolo"));
    pageContext.setAttribute("listaControlli", infoValidazione.get("listaControlli"));
    pageContext.setAttribute("numeroWarning", infoValidazione.get("numeroWarning"));
    pageContext.setAttribute("numeroErrori", infoValidazione.get("numeroErrori"));

    return null;
    
  }

}