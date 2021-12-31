package it.eldasoft.sil.w3.tags.funzioni;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.utils.properties.ConfigManager;

public class ListaIndirizziSIMAPFunction extends AbstractFunzioneTag {

  private static final String PROP_SIMAP_TEDESENDERCLASS    = "it.eldasoft.simap.tedesenderclass";
  private static final String PROP_SIMAP_DESTINATARIO_ALFA  = "it.eldasoft.simap.destinatario.alfa";
  private static final String PROP_SIMAP_DESTINATARIO_BETA  = "it.eldasoft.simap.destinatario.beta";
  private static final String PROP_SIMAP_DESTINATARIO_GAMMA = "it.eldasoft.simap.destinatario.gamma";

  public ListaIndirizziSIMAPFunction() {
    super(1, new Class[] { PageContext.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    pageContext.setAttribute("tedsenderclass", ConfigManager.getValore(PROP_SIMAP_TEDESENDERCLASS),
        PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("destinatario_alfa", ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_ALFA),
        PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("destinatario_beta", ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_BETA),
        PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("destinatario_gamma", ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_GAMMA),
        PageContext.REQUEST_SCOPE);

    return null;
  }

}
