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
import it.eldasoft.utils.spring.UtilitySpring;

public class GestioneLanguageFunction extends AbstractFunzioneTag {

	public GestioneLanguageFunction() {
		super(1, new Class[] { String.class });
	}

	public String function(PageContext pageContext, Object[] params)
			throws JspException {

		SqlManager sqlManager = (SqlManager) UtilitySpring.getBean(
				"sqlManager", pageContext, SqlManager.class);

		Long id = new Long((String) params[0]);
		String selectW3LANGUAGE = "select id, num, language_ec from w3language where id = ? order by num";

		try {
			List datiW3LANGUAGE = sqlManager.getListVector(selectW3LANGUAGE,new Object[] { id });

			if (datiW3LANGUAGE != null && datiW3LANGUAGE.size() > 0) pageContext.setAttribute("datiW3LANGUAGE", datiW3LANGUAGE,PageContext.REQUEST_SCOPE);
		
		} catch (SQLException e) {
			throw new JspException("Errore nella lettura delle lingue utilizzabili", e);
		}

		return null;

	}

}
