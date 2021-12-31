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

/**
 * Gestore del campo codice NUTS (area geografica)
 * 

 * 
 */
public class GestoreCampoTABNUTS_PAESE extends AbstractGestoreCampoTAB {

  public SqlSelect getSql() {
    return new SqlSelect(
        "Select paese, paese, descrizione from tabnuts where area is null and "
            + " regione is null and provincia is null order by codice");
  }

}
