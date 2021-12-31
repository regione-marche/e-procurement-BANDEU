package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreW3FS20 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS20.class);

  public String getEntita() {
    return "W3FS20";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    // Gestione ed inserimento del primo lotto obbligatorio in W3ANNEXB
    datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS20.ID").getValue().getValue());
    datiForm.setValue("W3ANNEXB.NUM", new Long(1));
    datiForm.setValue("W3ANNEXB.LOTNUM", new Long(1));
    this.inserisci(status, datiForm, new GestoreW3ANNEXB());
    
    this.gestioneW3FS20AWARD_C(status, datiForm);
    this.gestioneW3FS20AWARD_M(status, datiForm);

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (datiForm.getColumn("W3ANNEXB.ID").getValue().getValue() == null) {
      datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS20.ID").getValue().getValue());
      datiForm.setValue("W3ANNEXB.NUM", new Long(1));
      this.inserisci(status, datiForm, new GestoreW3ANNEXB());
    } else {
      this.update(status, datiForm, new GestoreW3ANNEXB());
    }

    this.gestioneW3FS20AWARD_C(status, datiForm);
    this.gestioneW3FS20AWARD_M(status, datiForm);

  }

  private void gestioneW3FS20AWARD_C(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoreMultiplo = new DefaultGestoreEntitaChiaveNumerica("W3FS20AWARD_C", "NUM", new String[] { "ID",
        "ITEM" }, this.getRequest());

    DataColumn item_fitt = new DataColumn("ITEM_FITT", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, new Long(1)));

    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoreMultiplo, "W3FS20AWARD_C",
        new DataColumn[] { datiForm.getColumn("W3FS20.ID"), item_fitt }, null);

  }

  
  private void gestioneW3FS20AWARD_M(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoreMultiplo = new DefaultGestoreEntitaChiaveNumerica("W3FS20AWARD_M", "NUM", new String[] { "ID",
        "ITEM" }, this.getRequest());

    DataColumn item_fitt = new DataColumn("ITEM_FITT", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, new Long(1)));

    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoreMultiplo, "W3FS20AWARD_M",
        new DataColumn[] { datiForm.getColumn("W3FS20.ID"), item_fitt }, null);
  }
  
}
