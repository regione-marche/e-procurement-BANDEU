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
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreW3ANNEXB extends AbstractGestoreChiaveNumerica {

  static Logger logger = Logger.getLogger(GestoreW3ANNEXB.class);

  public String[] getAltriCampiChiave() {
    return new String[] { "ID" };
  }

  public String getCampoNumericoChiave() {
    return "NUM";
  }

  public String getEntita() {
    return "W3ANNEXB";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    super.preInsert(status, datiForm);
    this.gestioneCPV(status, datiForm);
    this.gestioneCRITERIA(status, datiForm);

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3ANNEXB-preDelete: inizio metodo ");

    GeneManager geneManager = this.getGeneManager();

    Long id = datiForm.getLong("W3ANNEXB.ID");
    Long num = datiForm.getLong("W3ANNEXB.NUM");

    geneManager.deleteTabelle(new String[] { "W3CPV", "W3AWCRITERIA" }, "ID = ? AND NUM = ?", new Object[] { id, num });
    if (logger.isDebugEnabled()) logger.debug("GestoreW3ANNEXB-preDelete: fine metodo ");

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    this.gestioneCPV(status, datiForm);
    this.gestioneCRITERIA(status, datiForm);

  }

  /**
   * Gestione CPV
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneCPV(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    DataColumn dataColumnEntita = new DataColumn("ENT", new JdbcParametro(JdbcParametro.TIPO_TESTO, "W3ANNEXB"));

    AbstractGestoreChiaveNumerica gestoremultiploW3CPV = new DefaultGestoreEntitaChiaveNumerica("W3CPV", "NUMCPV", new String[] { "ENT",
        "ID", "NUM" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3CPV, "W3CPV", new DataColumn[] { dataColumnEntita,
        datiForm.getColumn("W3ANNEXB.ID"), datiForm.getColumn("W3ANNEXB.NUM") }, null);

  }

  private void gestioneCRITERIA(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    AbstractGestoreChiaveNumerica gestoremultiploW3CPV = new DefaultGestoreEntitaChiaveNumerica("W3AWCRITERIA", "ACNUM", new String[] {
        "ID", "NUM" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3CPV, "W3AWCRITERIA",
        new DataColumn[] { datiForm.getColumn("W3ANNEXB.ID"), datiForm.getColumn("W3ANNEXB.NUM") }, null);

  }

}
