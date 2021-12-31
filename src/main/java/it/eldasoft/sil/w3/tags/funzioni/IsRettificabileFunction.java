/*
 * Created on 14/nov/08
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
import it.eldasoft.utils.spring.UtilitySpring;

public class IsRettificabileFunction extends AbstractFunzioneTag {

  public IsRettificabileFunction() {
    super(2, new Class[] { PageContext.class, String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

    Long id = new Long((String) params[1]);
    String isRettificabile = "false";

    try {
      Long cnt = (Long) sqlManager.getObject(
          "select count(*) from w3simap where id = ? and notice_number_oj is not null and date_oj is not null", new Object[] { id });
      if (cnt != null && cnt.longValue() > 0) isRettificabile = "true";
    } catch (SQLException e) {
      throw new JspException("Errore nella verifica dello stato rettificabile", e);
    }

    return isRettificabile;

  }

}
