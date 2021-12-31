/*
 * Created on 07/ott/10
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class GestioneFS7CondizioniFunction extends AbstractFunzioneTag {

  public GestioneFS7CondizioniFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

    Long id = new Long((String) params[0]);
    String selectW3FS7_Q = "select id, num, conditions, methods from w3fs7_q where id = ? order by num";

    try {
      List<?> datiW3FS7_Q = sqlManager.getListVector(selectW3FS7_Q, new Object[] { id });

      if (datiW3FS7_Q != null && datiW3FS7_Q.size() > 0) pageContext.setAttribute("datiW3FS7_Q", datiW3FS7_Q, PageContext.REQUEST_SCOPE);

    } catch (SQLException e) {
      throw new JspException("Errore nella lettura delle condizioni", e);
    }

    return null;

  }

}
