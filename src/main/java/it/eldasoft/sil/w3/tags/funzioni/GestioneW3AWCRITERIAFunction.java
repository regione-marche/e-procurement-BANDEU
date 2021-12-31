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
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class GestioneW3AWCRITERIAFunction extends AbstractFunzioneTag {

  public GestioneW3AWCRITERIAFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(pageContext, UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {

      String[] parametri = ((String) params[0]).split(";");

      Long id = new Long((String) parametri[0]);
      Long num = new Long((String) parametri[1]);
      String selectW3AWCRITERIA = "select id, num, acnum, criteria_type, criteria, weighting from w3awcriteria where id = ? and num = ? order by criteria_type desc";

      try {
        List<?> datiW3AWCRITERIA = sqlManager.getListVector(selectW3AWCRITERIA, new Object[] { id, num });

        if (datiW3AWCRITERIA != null && datiW3AWCRITERIA.size() > 0)
          pageContext.setAttribute("datiW3AWCRITERIA", datiW3AWCRITERIA, PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura dei criteri di aggiudicazione", e);
      }
    }
    return null;

  }

}
