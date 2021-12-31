/*
 * Creato 22/01/2009
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.utils.properties.ConfigManager;

public class IsProtezioneArchiviFunction extends AbstractFunzioneTag {

  public IsProtezioneArchiviFunction() {
    super(1, new Class[] { PageContext.class });
  }

  private static final String PROP_PROTEZIONE_ARCHIVI = "it.eldasoft.protezionearchivi";

  public String function(PageContext pageContext, Object[] params)
      throws JspException {

    String result = null;

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);
    if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
      result = "true";
    } else {
      result = "false";
    }

    return result;
  }

}