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

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class GetNumeroNotificheNonLetteFunction extends AbstractFunzioneTag {

  public GetNumeroNotificheNonLetteFunction() {
    super(1, new Class[] { PageContext.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);

    Long numeroNotifiche = null;

    try {
      String selectW3SIMAPWS = "select count(*) from w3simapws where stato = '2'";
      String filtroPermessi = UtilityPermessi.getFiltroPermessi(profilo, "W3SIMAPWS", "ID", sqlManager);
      if (filtroPermessi != null && !"".equals(filtroPermessi)) {
        selectW3SIMAPWS += " and " + filtroPermessi;
      }
      numeroNotifiche = (Long) sqlManager.getObject(selectW3SIMAPWS, new Object[] {});

    } catch (SQLException e) {
      throw new JspException("Errore nel conteggio delle notifiche non lette", e);
    }

    if (numeroNotifiche == null) numeroNotifiche = new Long(0);

    return numeroNotifiche.toString();

  }
}
