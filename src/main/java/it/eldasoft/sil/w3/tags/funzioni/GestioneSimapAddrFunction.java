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

public class GestioneSimapAddrFunction extends AbstractFunzioneTag {

  public GestioneSimapAddrFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(pageContext, UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {
      Long id = new Long((String) params[0]);
      String selectW3SIMAP_ADDR = "select w3simap_addr.id, "
          + " w3simap_addr.num, "
          + " w3simap_addr.codamm, "
          + " uffint.nomein "
          + " from w3simap_addr, "
          + " uffint,"
          + " w3ammi "
          + " where w3simap_addr.id = ? "
          + " and w3simap_addr.codamm = w3ammi.codamm "
          + " and w3ammi.codein = uffint.codein "
          + " order by num";

      try {
        List<?> datiW3SIMAP_ADDR = sqlManager.getListVector(selectW3SIMAP_ADDR, new Object[] { id });

        if (datiW3SIMAP_ADDR != null && datiW3SIMAP_ADDR.size() > 0)
          pageContext.setAttribute("datiW3SIMAP_ADDR", datiW3SIMAP_ADDR, PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura della lista delle amministrazioni", e);
      }
    }

    return null;

  }

}
