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

public class GestioneCPVComplementariFunction extends AbstractFunzioneTag {

  public GestioneCPVComplementariFunction() {
    super(1, new Class[] { String.class });
  }

  public String function(PageContext pageContext, Object[] params)
      throws JspException {

    if (!UtilityTags.SCHEDA_MODO_INSERIMENTO.equals(UtilityTags.getParametro(
        pageContext, UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {
      String[] parametri = ((String) params[0]).split(";");

      SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager",
          pageContext, SqlManager.class);

      String ent = parametri[0];
      Long id = new Long(parametri[1]);
      Long num = null;
      Object[] filtri = null;
      String selectW3CPV = null;

      if (("W3FS1S2".equals(ent) || "W3ANNEXB".equals(ent) || "W3FS8S2".equals(ent) || "W3FS20".equals(ent))
          && parametri.length > 2) {
        num = new Long(parametri[2]);
        selectW3CPV = "select numcpv, ent, id, num, tipcpv, cpv, cpvsupp1, cpvsupp2 from w3cpv where ent = ? and id = ? and num = ? order by numcpv";
        filtri = new Object[] { ent, id, num };
      } else {
        selectW3CPV = "select numcpv, ent, id, num, tipcpv, cpv, cpvsupp1, cpvsupp2 from w3cpv where ent = ? and id = ? and num = -1 order by numcpv";
        filtri = new Object[] { ent, id };
      }

      try {
        List datiW3CPV = sqlManager.getListVector(selectW3CPV, filtri);

        if (datiW3CPV != null && datiW3CPV.size() > 0) {
          pageContext.setAttribute("datiW3CPV", datiW3CPV,
              PageContext.REQUEST_SCOPE);
        }

      } catch (SQLException e) {
        throw new JspException(
            "Errore nella lettura dei codici CPV supplementari", e);
      }

    }
    return null;

  }

}
