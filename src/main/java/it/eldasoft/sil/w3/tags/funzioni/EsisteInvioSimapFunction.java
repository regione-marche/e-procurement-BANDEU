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

public class EsisteInvioSimapFunction extends AbstractFunzioneTag {

	public EsisteInvioSimapFunction() {
		super(2, new Class[] { PageContext.class, String.class });
	}

	public String function(PageContext pageContext, Object[] params)
			throws JspException {

	     SqlManager sqlManager = (SqlManager) UtilitySpring.getBean(
             "sqlManager", pageContext, SqlManager.class);
	  
		Long id = new Long((String) params[1]);
		String esisteInvioSimap = "false";
		
		try {
		  Long conteggio = (Long) sqlManager.getObject("select count(*) from w3simapemail where id = ?",new Object[] { id });
		  if (conteggio != null && conteggio.longValue() > 0) esisteInvioSimap = "true";
		} catch (SQLException e) {
			 throw new JspException("Errore nel conteggio del numero di invii",e);
		}
	    
	    return esisteInvioSimap;

	}

}
