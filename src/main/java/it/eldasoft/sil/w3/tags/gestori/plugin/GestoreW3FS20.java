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

public class GestoreW3FS20 extends AbstractGestorePreload {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS20.class);

  /**
   * 
   * @param tag
   */
  public GestoreW3FS20(BodyTagSupportGene tag) {
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

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS20: inizio metodo");

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", page, SqlManager.class);

    if ((UtilityTags.SCHEDA_MODO_INSERIMENTO).equals(modoAperturaScheda)) {

    }

    else

    {
      // Chiave dell'entità
      HashMap keyParent = UtilityTags.stringParamsToHashMap(
          (String) page.getAttribute(UtilityTags.DEFAULT_HIDDEN_KEY_TABELLA, PageContext.REQUEST_SCOPE), null);

      Long id = new Long(((JdbcParametro) keyParent.get("W3FS20.ID")).getStringValue());

      try {
        String address_review_bodyNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3fs20 "
            + " where uffint.codein = w3fs20.address_review_body "
            + " and w3fs20.id = ?", new Object[] { id });

        String address_mediation_bodyNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3fs20 "
            + " where uffint.codein = w3fs20.address_mediation_body "
            + " and w3fs20.id = ?", new Object[] { id });

        String address_review_infoNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3fs20 "
            + " where uffint.codein = w3fs20.address_review_info "
            + " and w3fs20.id = ?", new Object[] { id });

        if (address_review_bodyNomein != null)
          page.setAttribute("address_review_bodyNomein", address_review_bodyNomein, PageContext.REQUEST_SCOPE);
        if (address_mediation_bodyNomein != null)
          page.setAttribute("address_mediation_bodyNomein", address_mediation_bodyNomein, PageContext.REQUEST_SCOPE);
        if (address_review_infoNomein != null)
          page.setAttribute("address_review_infoNomein", address_review_infoNomein, PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura dei punti di contatto", e);
      }

    }

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS6: fine metodo");

  }
}
