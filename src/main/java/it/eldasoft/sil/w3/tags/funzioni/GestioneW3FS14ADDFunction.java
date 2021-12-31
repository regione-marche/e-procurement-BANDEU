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

public class GestioneW3FS14ADDFunction extends AbstractFunzioneTag {

  public GestioneW3FS14ADDFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(pageContext,
        UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {
      String[] parametri = ((String) params[0]).split(";");

      SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

      Long id = new Long(parametri[0]);
      String selectW3FS14CORR = "select id, num, type, object, section, text_new "
          + " from w3fs14corr where id = ? and type = 'ADD' order by num";

      String selectW3SIMAP_FORM = "select w3simap.form from w3simap, w3fs14 where w3simap.id = w3fs14.id_rif and w3fs14.id = ?";

      try {
        List datiW3FS14CORR = sqlManager.getListVector(selectW3FS14CORR, new Object[] { id });

        if (datiW3FS14CORR != null && datiW3FS14CORR.size() > 0) {
          pageContext.setAttribute("datiW3FS14CORR", datiW3FS14CORR, PageContext.REQUEST_SCOPE);
        }

        String formulario = (String) sqlManager.getObject(selectW3SIMAP_FORM, new Object[] { id });
        pageContext.setAttribute("formulario", formulario, PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura delle variazioni dalla tabella W3FS14CORR", e);
      }

    }
    return null;

  }
}
