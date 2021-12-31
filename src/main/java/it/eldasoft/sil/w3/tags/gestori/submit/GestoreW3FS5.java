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

public class GestoreW3FS5 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS5.class);

  public String getEntita() {
    return "W3FS5";
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

    // Entità di estensione
    datiForm.setValue("W3FS5S36.ID", datiForm.getColumn("W3FS5.ID").getValue().getValue());
    this.inserisci(status, datiForm, new GestoreW3FS5S36());

    // Gestione ed inserimento del primo lotto obbligatorio in W3ANNEXB
    datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS5.ID").getValue().getValue());
    datiForm.setValue("W3ANNEXB.NUM", new Long(1));
    datiForm.setValue("W3ANNEXB.LOTNUM", new Long(1));
    this.inserisci(status, datiForm, new GestoreW3ANNEXB());

    // Gestione della sezione multipla dei criteri per OEPV
    gestioneW3CRITERIA(status, datiForm);

    // Gestione lingue utilizzabili
    gestioneW3LANGUAGE(status, datiForm);

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS5-preUpdate: inizio metodo ");

    if (datiForm.getColumn("W3FS5S36.ID").getValue().getValue() == null) {
      datiForm.setValue("W3FS5S36.ID", datiForm.getColumn("W3FS5.ID").getValue().getValue());
      this.inserisci(status, datiForm, new GestoreW3FS5S36());
    } else {
      this.update(status, datiForm, new GestoreW3FS5S36());
    }

    if (datiForm.getColumn("W3ANNEXB.ID").getValue().getValue() == null) {
      datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS5.ID").getValue().getValue());
      datiForm.setValue("W3ANNEXB.NUM", new Long(1));
      this.inserisci(status, datiForm, new GestoreW3ANNEXB());
    } else {
      this.update(status, datiForm, new GestoreW3ANNEXB());
    }

    this.update(status, datiForm, new GestoreW3FS5S36());

    // Gestione della sezione multipla dei criteri per OEPV
    gestioneW3CRITERIA(status, datiForm);

    // Gestione lingue utilizzabili
    gestioneW3LANGUAGE(status, datiForm);

    if (logger.isDebugEnabled()) logger.debug("GestoreW3FS5-preUpdate: fine metodo ");
  }

  /**
   * Gestione dei criteri.
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneW3CRITERIA(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoremultiploW3CRITERIA = new DefaultGestoreEntitaChiaveNumerica("W3CRITERIA", "NUM",
        new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3CRITERIA, "W3CRITERIA",
        new DataColumn[] { datiForm.getColumn("W3FS5.ID") }, null);
  }

  /**
   * Gestione delle lingue UE utilizzabili.
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneW3LANGUAGE(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoremultiploW3LANGUAGE = new DefaultGestoreEntitaChiaveNumerica("W3LANGUAGE", "NUM",
        new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3LANGUAGE, "W3LANGUAGE",
        new DataColumn[] { datiForm.getColumn("W3FS5.ID") }, null);
  }

}
