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

public class GestoreW3FS1 extends AbstractGestorePreload {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS1.class);

  public GestoreW3FS1(BodyTagSupportGene tag) {
    super(tag);
  }

  public void doAfterFetch(PageContext page, String modoAperturaScheda) throws JspException {

  }

  public void doBeforeBodyProcessing(PageContext page, String modoAperturaScheda) throws JspException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS1: inizio metodo");

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", page, SqlManager.class);

    if (!(UtilityTags.SCHEDA_MODO_INSERIMENTO).equals(modoAperturaScheda)) {

      HashMap<?, ?> keyParent = UtilityTags.stringParamsToHashMap(
          (String) page.getAttribute(UtilityTags.DEFAULT_HIDDEN_KEY_TABELLA, PageContext.REQUEST_SCOPE), null);

      Long id = new Long(((JdbcParametro) keyParent.get("W3FS1.ID")).getStringValue());

      try {
        String furtherInfoNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3simap "
            + " where uffint.codein = w3simap.further_info_codein "
            + " and w3simap.id = ?", new Object[] { id });

        String participationNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3simap "
            + " where uffint.codein = w3simap.participation_codein "
            + " and w3simap.id = ?", new Object[] { id });

        String reviewBodyNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3fs1 "
            + " where uffint.codein = w3fs1.REVIEW_BODY_CODEIN "
            + " and w3fs1.id = ?", new Object[] { id });

        String mediationBodyNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3fs1 "
            + " where uffint.codein = w3fs1.MEDIATION_BODY_CODEIN "
            + " and w3fs1.id = ?", new Object[] { id });
        String reviewInfoNomein = (String) sqlManager.getObject("select uffint.nomein from uffint, w3fs1 "
            + " where uffint.codein = w3fs1.REVIEW_INFO_CODEIN "
            + " and w3fs1.id = ?", new Object[] { id });

        if (furtherInfoNomein != null) page.setAttribute("furtherInfoNomein", furtherInfoNomein, PageContext.REQUEST_SCOPE);
        if (participationNomein != null) page.setAttribute("participationNomein", participationNomein, PageContext.REQUEST_SCOPE);
        if (reviewBodyNomein != null) page.setAttribute("reviewBodyNomein", reviewBodyNomein, PageContext.REQUEST_SCOPE);
        if (mediationBodyNomein != null) page.setAttribute("mediationBodyNomein", mediationBodyNomein, PageContext.REQUEST_SCOPE);
        if (reviewInfoNomein != null) page.setAttribute("reviewInfoNomein", reviewInfoNomein, PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura dei punti di contatto", e);
      }
    }

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS1: fine metodo");

  }
}
