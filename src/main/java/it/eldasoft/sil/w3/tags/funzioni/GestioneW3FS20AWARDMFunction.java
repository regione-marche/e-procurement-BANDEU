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

public class GestioneW3FS20AWARDMFunction extends AbstractFunzioneTag {

  public GestioneW3FS20AWARDMFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(pageContext, UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {

      String[] parametri = ((String) params[0]).split(";");

      Long id = new Long(parametri[0]);
      Long item = new Long(parametri[1]);

      String selectW3FS20AWARD_M = "select w3fs20award_m.id, "
          + " w3fs20award_m.item, "
          + " w3fs20award_m.num, "
          + " w3fs20award_m.contractor_codimp, "
          + " w3fs20award_m.contractor_sme, "
          + " impr.nomest "
          + " from w3fs20award_m, "
          + " impr "
          + " where w3fs20award_m.id = ? "
          + " and w3fs20award_m.item = ? "
          + " and w3fs20award_m.contractor_codimp = impr.codimp "
          + " and w3fs20award_m.contractor_codimp is not null"
          + " union "
          + " (select w3fs20award_m.id, "
          + " w3fs20award_m.item, "
          + " w3fs20award_m.num, "
          + " w3fs20award_m.contractor_codimp, "
          + " w3fs20award_m.contractor_sme, "
          + " null "
          + " from w3fs20award_m "
          + " where w3fs20award_m.id = ? "
          + " and w3fs20award_m.item = ? "
          + " and w3fs20award_m.contractor_codimp is null)";

      try {
        List<?> datiW3FS20AWARD_M = sqlManager.getListVector(selectW3FS20AWARD_M, new Object[] { id, item, id, item });

        if (datiW3FS20AWARD_M != null && datiW3FS20AWARD_M.size() > 0)
          pageContext.setAttribute("datiW3FS20AWARD_M", datiW3FS20AWARD_M, PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura della lista dei contraenti", e);
      }
    }

    return null;

  }

}
