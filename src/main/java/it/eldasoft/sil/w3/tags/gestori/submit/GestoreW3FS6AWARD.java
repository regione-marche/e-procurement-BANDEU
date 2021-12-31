/*
 * Created on 08 Ott 2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.bl.GeneManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import org.springframework.transaction.TransactionStatus;

public class GestoreW3FS6AWARD extends AbstractGestoreChiaveNumerica {

  public String[] getAltriCampiChiave() {
    return new String[] { "ID" };
  }

  public String getCampoNumericoChiave() {
    return "ITEM";
  }

  public String getEntita() {
    return "W3FS6AWARD";
  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    super.preInsert(status, datiForm);
    this.gestioneW3FS6AWARD_C(status, datiForm);

  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    GeneManager geneManager = this.getGeneManager();

    Long id = datiForm.getLong("W3FS6AWARD.ID");
    Long item = datiForm.getLong("W3FS6AWARD.ITEM");
    geneManager.deleteTabelle(new String[] { "W3FS6AWARD_C" }, "ID = ? AND ITEM = ?", new Object[] { id, item });
    
  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    this.gestioneW3FS6AWARD_C(status, datiForm);

  }

  private void gestioneW3FS6AWARD_C(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoreMultiplo = new DefaultGestoreEntitaChiaveNumerica("W3FS6AWARD_C", "NUM", new String[] {
        "ID", "ITEM" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoreMultiplo, "W3FS6AWARD_C", new DataColumn[] {
        datiForm.getColumn("W3FS6AWARD.ID"), datiForm.getColumn("W3FS6AWARD.ITEM") }, null);
  }

}
