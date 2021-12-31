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

public class DescrizioneCategoriaStazioneAppaltanteFunction extends
		AbstractFunzioneTag {

	public DescrizioneCategoriaStazioneAppaltanteFunction() {
		super(2, new Class[] { PageContext.class, String.class });
	}

	public String function(PageContext pageContext, Object[] params)
			throws JspException {

		SqlManager sqlManager = (SqlManager) UtilitySpring.getBean(
				"sqlManager", pageContext, SqlManager.class);

		String tabcod1 =  (String) params[1];
		String descrizione = null;

		try {
			descrizione = (String) sqlManager
					.getObject(
							"select tabdesc from tabsche where tabcod1 = ? and tabcod = 'S2002'",
							new Object[] { tabcod1 });
		} catch (SQLException e) {
			throw new JspException("Errore nella selezione del tipo contratto",
					e);
		}

		return descrizione;

	}

}
