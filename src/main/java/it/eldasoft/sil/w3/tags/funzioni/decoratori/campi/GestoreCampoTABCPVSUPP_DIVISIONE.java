/*
 * Created on 15/mar/07
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import java.util.HashMap;

import javax.servlet.jsp.PageContext;

/**
 * Gestore del campo codice CPV Supplementare (divisione)
 * 
 * 
 */
public class GestoreCampoTABCPVSUPP_DIVISIONE extends
    AbstractGestoreCampoTAB {

  public SqlSelect getSql() {
    HashMap datiRiga = (HashMap) this.getPageContext().getAttribute("datiRiga",
        PageContext.REQUEST_SCOPE);
    if (datiRiga != null) {
      return new SqlSelect(
          "Select divisione, divisione, descrizione from tabcpvsupp "
              + "where sezione = ? and gruppo = ? and divisione <> '0' order by codice",
          new Object[] { datiRiga.get("SEZIONE"), datiRiga.get("GRUPPO") });
    }
    return null;
  }

}
