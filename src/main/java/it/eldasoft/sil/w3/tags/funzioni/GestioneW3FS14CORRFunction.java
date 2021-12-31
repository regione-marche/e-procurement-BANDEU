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

import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.utils.spring.UtilitySpring;

public class GestioneW3FS14CORRFunction extends AbstractFunzioneTag {

  public GestioneW3FS14CORRFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(pageContext,
        UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {
      String[] parametri = ((String) params[0]).split(";");

      SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

      Long id = new Long(parametri[0]);
      String selectW3FS14CORR = "select id, num, type, object, section, "
          + " text_old, text_new, date_old, date_new, "
          + " time_old, time_new, nuts_old, nuts_new, "
          + " cpv_old, cpvsupp1_old, cpvsupp2_old, "
          + " cpv_new, cpvsupp1_new, cpvsupp2_new, " 
          + " lot_no, label_text"
          + " from w3fs14corr where id = ? order by num";

      try {
        List<?> datiW3FS14CORR = sqlManager.getListVector(selectW3FS14CORR, new Object[] { id });

        if (datiW3FS14CORR != null && datiW3FS14CORR.size() > 0) {
          pageContext.setAttribute("datiW3FS14CORR", datiW3FS14CORR, PageContext.REQUEST_SCOPE);
        }

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura delle variazioni dalla tabella W3FS14CORR", e);
      }

    }
    return null;

  }
}
