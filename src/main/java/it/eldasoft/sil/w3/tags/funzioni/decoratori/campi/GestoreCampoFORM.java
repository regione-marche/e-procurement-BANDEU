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

public class GestoreCampoFORM extends AbstractGestoreCampoTabellato {

  public GestoreCampoFORM() {
    super(false, "T5");
  }

  public SqlSelect getSql() {
    return new SqlSelect("select tab2tip, tab2d2 "
        + " from tab2 "
        + " where tab2cod = ? "
        + " and tab2tip not in ('FS14') "
        + " order by tab2nord, tab2tip", new Object[] { "W3z64" });
  }
}
