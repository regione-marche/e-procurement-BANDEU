package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

public class GestoreW3FS9 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS9.class);

  public String getEntita() {
    return "W3FS9";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm)
      throws GestoreException {

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm)
      throws GestoreException {

    this.gestioneCPV(status, datiForm);

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm)
      throws GestoreException {
    
    // Gestione lingue utilizzabili
    AbstractGestoreChiaveNumerica gestoremultiploW3LANGUAGE = new DefaultGestoreEntitaChiaveNumerica(
        "W3LANGUAGE", "NUM", new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm,
        gestoremultiploW3LANGUAGE, "W3LANGUAGE",
        new DataColumn[] { datiForm.getColumn("W3FS9.ID") }, null);
    
    this.gestioneCPV(status, datiForm);

  }

  /**
   * Gestione CPV
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneCPV(TransactionStatus status,
      DataColumnContainer datiForm) throws GestoreException {
    
    DataColumn dataColumnEntita = new DataColumn("ENT", new JdbcParametro(
        JdbcParametro.TIPO_TESTO, "W3FS9"));
    DataColumn dataColumnNum = new DataColumn("NUM", new JdbcParametro(
        JdbcParametro.TIPO_NUMERICO, "-1"));

    AbstractGestoreChiaveNumerica gestoremultiploW3CPV = new DefaultGestoreEntitaChiaveNumerica(
        "W3CPV", "NUMCPV", new String[] { "ENT", "ID", "NUM" },
        this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm,
        gestoremultiploW3CPV, "W3CPV", new DataColumn[] { dataColumnEntita,
            datiForm.getColumn("W3FS9.ID"), dataColumnNum }, null);
  }

}
