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
 * Gestore del campo codice CPV
 * 
 * @author Marco.Franceschin
 * 
 */
public class GestoreCampoTABCPV_TABCOD0 extends AbstractGestoreCampoTAB {

  public SqlSelect getSql() {
    return new SqlSelect(
        "Select CPVCOD0, CPVCOD4, CPVDESC from TABCPV Where CPVCOD1 = '00' and "
            + "CPVCOD2 = '00' and CPVCOD3 = '00' and CPVCOD0<>'00'  and CPVCOD = 'S2020' "
            + "Order by CPVCOD4");
  }

}
