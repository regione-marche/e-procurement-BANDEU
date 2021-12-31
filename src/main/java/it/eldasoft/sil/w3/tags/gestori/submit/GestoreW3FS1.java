package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreW3FS1 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS1.class);

  public String getEntita() {
    return "W3FS1";
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

    // Gestione ed inserimento della riga nella tabella di estensione W3FS1S2
    AbstractGestoreEntita gestoreW3FS1S2 = new DefaultGestoreEntita("W3FS1S2", this.getRequest());
    datiForm.setValue("W3FS1S2.ID", datiForm.getColumn("W3FS1.ID").getValue().getValue());
    this.inserisci(status, datiForm, gestoreW3FS1S2);

    // Gestione ed inserimento del primo lotto obbligatorio in W3ANNEXB
    datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS1.ID").getValue().getValue());
    datiForm.setValue("W3ANNEXB.NUM", new Long(1));
    datiForm.setValue("W3ANNEXB.LOTNUM", new Long(1));
    this.inserisci(status, datiForm, new GestoreW3ANNEXB());

    // Gestione informazioni sulle lingue UE ammesse
    this.gestioneW3LANGUAGE(status, datiForm);
  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    AbstractGestoreEntita gestoreW3FS1S2 = new DefaultGestoreEntita("W3FS1S2", this.getRequest());
    if (datiForm.getColumn("W3FS1S2.ID").getValue().getValue() == null) {
      datiForm.setValue("W3FS1S2.ID", datiForm.getColumn("W3FS1.ID").getValue().getValue());
      this.inserisci(status, datiForm, gestoreW3FS1S2);
    } else {
      this.update(status, datiForm, gestoreW3FS1S2);
    }

    if (datiForm.getColumn("W3ANNEXB.ID").getValue().getValue() == null) {
      datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS1.ID").getValue().getValue());
      datiForm.setValue("W3ANNEXB.NUM", new Long(1));
      this.inserisci(status, datiForm, new GestoreW3ANNEXB());
    } else {
      this.update(status, datiForm, new GestoreW3ANNEXB());
    }

    // Gestione informazioni sulle lingue UE ammesse
    this.gestioneW3LANGUAGE(status, datiForm);

  }

  /**
   * Gestione informazioni sulle lingue UE ammesse.
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneW3LANGUAGE(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoremultiploW3LANGUAGE = new DefaultGestoreEntitaChiaveNumerica("W3LANGUAGE", "NUM",
        new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3LANGUAGE, "W3LANGUAGE",
        new DataColumn[] { datiForm.getColumn("W3FS1.ID") }, null);
  }

}
