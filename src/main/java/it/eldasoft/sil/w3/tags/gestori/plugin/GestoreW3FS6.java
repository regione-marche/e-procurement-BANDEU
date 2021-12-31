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

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.tags.BodyTagSupportGene;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestorePreload;
import it.eldasoft.utils.spring.UtilitySpring;

public class GestoreW3FS6 extends AbstractGestorePreload {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS6.class);

  /**
   * 
   * @param tag
   */
  public GestoreW3FS6(BodyTagSupportGene tag) {
    super(tag);
  }

  /**
   * 
   * @see it.eldasoft.gene.web.struts.tags.gestori.AbstractGestorePreload#doAfterFetch(javax.servlet.jsp.PageContext,
   *      java.lang.String)
   */
  public void doAfterFetch(PageContext page, String modoAperturaScheda)
      throws JspException {

  }

  /**
   * 
   * @see it.eldasoft.gene.web.struts.tags.gestori.AbstractGestorePreload#doBeforeBodyProcessing(javax.servlet.jsp.PageContext,
   *      java.lang.String)
   */
  public void doBeforeBodyProcessing(PageContext page, String modoAperturaScheda)
      throws JspException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS6: inizio metodo");
    
    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager",
        page, SqlManager.class);

    if ((UtilityTags.SCHEDA_MODO_INSERIMENTO).equals(modoAperturaScheda)) {

    }

    else

    {
      // Chiave dell'entità
      HashMap keyParent = UtilityTags.stringParamsToHashMap(
          (String) page.getAttribute(UtilityTags.DEFAULT_HIDDEN_KEY_TABELLA,
              PageContext.REQUEST_SCOPE), null);

      Long id = new Long(
          ((JdbcParametro) keyParent.get("W3FS6.ID")).getStringValue());

      try {
        String appealProcedureNomein = (String) sqlManager.getObject(
            "select uffint.nomein from uffint, w3fs6 "
                + " where uffint.codein = w3fs6.appeal_procedure_codein "
                + " and w3fs6.id = ?", new Object[] { id });

        String mediationProcedureNomein = (String) sqlManager.getObject(
            "select uffint.nomein from uffint, w3fs6 "
                + " where uffint.codein = w3fs6.mediation_procedure_codein "
                + " and w3fs6.id = ?", new Object[] { id });

        String appealServiceNomein = (String) sqlManager.getObject(
            "select uffint.nomein from uffint, w3fs6 "
                + " where uffint.codein = w3fs6.appeal_service_codein "
                + " and w3fs6.id = ?", new Object[] { id });

        if (appealProcedureNomein != null)
          page.setAttribute("appealProcedureNomein", appealProcedureNomein,
              PageContext.REQUEST_SCOPE);
        if (mediationProcedureNomein != null)
          page.setAttribute("mediationProcedureNomein",
              mediationProcedureNomein, PageContext.REQUEST_SCOPE);
        if (appealServiceNomein != null)
          page.setAttribute("appealServiceNomein", appealServiceNomein,
              PageContext.REQUEST_SCOPE);

      } catch (SQLException e) {
        throw new JspException("Errore nella lettura dei punti di contatto", e);
      }

    }

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS6: fine metodo");
    
  }
}
