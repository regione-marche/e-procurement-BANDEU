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

public class GetContatoriSIMAPFunction extends AbstractFunzioneTag {

  public GetContatoriSIMAPFunction() {
    super(1, new Class[] { PageContext.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);

    Long totali = null;
    Long inviati = null;
    Long pubblicati = null;

    try {
      // Totali
      String _t = "select count(*) from w3simap";
      String _f_t = UtilityPermessi.getFiltroPermessi(profilo, "W3SIMAP", "ID", sqlManager);
      if (_f_t != null && !"".equals(_f_t)) {
        _t += " where " + _f_t;
      }
      totali = (Long) sqlManager.getObject(_t, new Object[] {});

      // Inviati
      String _i = "select count(*) from w3simap where EXISTS (SELECT num FROM w3simapws WHERE w3simap.id = w3simapws.id) ";
      String _f_i = UtilityPermessi.getFiltroPermessi(profilo, "W3SIMAP", "ID", sqlManager);
      if (_f_i != null && !"".equals(_f_i)) {
        _i += " and " + _f_i;
      }
      inviati = (Long) sqlManager.getObject(_i, new Object[] {});

      // Pubblicati
      String _p = "select count(*) from w3simap where notice_number_oj is not null and date_oj is not null";
      String _f_p = UtilityPermessi.getFiltroPermessi(profilo, "W3SIMAP", "ID", sqlManager);
      if (_f_p != null && !"".equals(_f_p)) {
        _p += " and " + _f_p;
      }
      pubblicati = (Long) sqlManager.getObject(_p, new Object[] {});

    } catch (SQLException e) {
      throw new JspException("Errore nel conteggio", e);
    }
    
    if (totali == null) totali = new Long(0);
    if (inviati == null) inviati = new Long(0);
    if (pubblicati == null) pubblicati = new Long(0);

    pageContext.setAttribute("totali", totali.toString(), PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("inviati", inviati.toString(), PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("pubblicati", pubblicati.toString(), PageContext.REQUEST_SCOPE);

    return null;

  }
}
