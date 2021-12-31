/*
 * Created on 11-Ott-2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni;

import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.utils.spring.UtilitySpring;

public class GetDivIntoLotsFS5Function extends AbstractFunzioneTag {

  public GetDivIntoLotsFS5Function() {
    super(2, new Class[] { PageContext.class, String.class });
  }

  public String function(PageContext pageContext, Object[] params)
      throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager",
        pageContext, SqlManager.class);

    String div_into_lots = null;
    String chiave = (String) UtilityTags.getParametro(pageContext,
        UtilityTags.DEFAULT_HIDDEN_KEY_TABELLA);

    if (chiave != null && chiave.length() > 0) {
      Long id = new Long(chiave.substring(chiave.indexOf(":") + 1));

      try {
        div_into_lots = (String) sqlManager.getObject(
            "select div_into_lots from w3fs5 where id = ?",
            new Object[] { id });
      } catch (SQLException s) {
        throw new JspException(
            "Errore durante la lettura della divisione in lotti", s);
      }
    }
    return div_into_lots;

  }

}