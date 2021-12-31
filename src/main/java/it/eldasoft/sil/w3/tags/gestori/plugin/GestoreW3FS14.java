/*
 * Created on 12/ott/2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.gestori.plugin;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.tags.BodyTagSupportGene;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestorePreload;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class GestoreW3FS14 extends AbstractGestorePreload {

  /** Logger */
  static Logger               logger                   = Logger.getLogger(GestoreW3FS14.class);

  private static final String PROP_SIMAP_ESENDER_LOGIN = "it.eldasoft.simap.esenderlogin";

  /**
   * 
   * @param tag
   */
  public GestoreW3FS14(BodyTagSupportGene tag) {
    super(tag);
  }

  /**
   * 
   * @see it.eldasoft.gene.web.struts.tags.gestori.AbstractGestorePreload#doAfterFetch(javax.servlet.jsp.PageContext,
   *      java.lang.String)
   */
  public void doAfterFetch(PageContext page, String modoAperturaScheda) throws JspException {

  }

  /**
   * 
   * @see it.eldasoft.gene.web.struts.tags.gestori.AbstractGestorePreload#doBeforeBodyProcessing(javax.servlet.jsp.PageContext,
   *      java.lang.String)
   */
  public void doBeforeBodyProcessing(PageContext page, String modoAperturaScheda) throws JspException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS14: inizio metodo");

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", page, SqlManager.class);

    try {
      Long id_rif = null;

      if ((UtilityTags.SCHEDA_MODO_INSERIMENTO).equals(modoAperturaScheda)) {
        id_rif = new Long(page.getRequest().getParameter("id_rif"));
      } else {
        HashMap keyParent = UtilityTags.stringParamsToHashMap(
            (String) page.getAttribute(UtilityTags.DEFAULT_HIDDEN_KEY_TABELLA, PageContext.REQUEST_SCOPE), null);
        Long id = new Long(((JdbcParametro) keyParent.get("W3FS14.ID")).getStringValue());
        id_rif = (Long) sqlManager.getObject("select id_rif from w3fs14 where id = ?", new Object[] { id });
      }

      String form_rif = (String) sqlManager.getObject("select w3simap.form from w3simap where w3simap.id = ?", new Object[] { id_rif });

      page.setAttribute("id_rif", id_rif, PageContext.REQUEST_SCOPE);
      page.setAttribute("form_rif", form_rif, PageContext.REQUEST_SCOPE);

    } catch (SQLException e) {
      throw new JspException("Errore nella lettura dei dati del bando avviso da rettificare", e);
    }

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS14: fine metodo");

  }
}
