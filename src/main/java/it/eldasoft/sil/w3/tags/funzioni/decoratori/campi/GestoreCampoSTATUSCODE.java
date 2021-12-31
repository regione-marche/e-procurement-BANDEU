/*
 * Created on 03/nov/08
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import it.eldasoft.gene.tags.decorators.campi.AbstractGestoreCampoTabellato;

public class GestoreCampoSTATUSCODE extends AbstractGestoreCampoTabellato {

  public GestoreCampoSTATUSCODE() {
    super(false, "T30");
  }

  public SqlSelect getSql() {
    return new SqlSelect("select tab2d1, tab2d2 from tab2 where tab2cod = ? order by tab2tip", new Object[] { "W3z70" });
  }
}
