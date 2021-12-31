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

public class GestioneW3AMMI_PBFunction extends AbstractFunzioneTag {

  public GestioneW3AMMI_PBFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(pageContext, UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {

      String codamm = (String) params[0];

      SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);

      String selectW3AMMI_PB = "select w3ammi_pb.codamm, "
          + " w3ammi_pb.num, "
          + " w3ammi_pb.codein, "
          + " uffint.nomein "
          + " from w3ammi_pb, uffint"
          + " where w3ammi_pb.codamm = ? "
          + " and w3ammi_pb.codein = uffint.codein";

      try {
        List<?> datiW3AMMI_PB = sqlManager.getListVector(selectW3AMMI_PB, new Object[] { codamm });
        pageContext.setAttribute("datiW3AMMI_PB", datiW3AMMI_PB, PageContext.REQUEST_SCOPE);
      } catch (SQLException e) {
        throw new JspException("Errore nella lettura delle altre ammministrazioni aggiudicatrici", e);
      }

    }
    return null;

  }
}
