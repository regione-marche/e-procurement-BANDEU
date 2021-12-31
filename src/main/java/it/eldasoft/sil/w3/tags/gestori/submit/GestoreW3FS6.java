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

public class GestoreW3FS6 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS6.class);

  public String getEntita() {
    return "W3FS6";
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
    datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS6.ID").getValue().getValue());
    datiForm.setValue("W3ANNEXB.NUM", new Long(1));
    datiForm.setValue("W3ANNEXB.LOTNUM", new Long(1));
    this.inserisci(status, datiForm, new GestoreW3ANNEXB());

    // Gestione della sezione multipla dei criteri per OEPV
    this.gestioneW3CRITERIA(status, datiForm);

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (datiForm.getColumn("W3ANNEXB.ID").getValue().getValue() == null) {
      datiForm.setValue("W3ANNEXB.ID", datiForm.getColumn("W3FS6.ID").getValue().getValue());
      datiForm.setValue("W3ANNEXB.NUM", new Long(1));
      this.inserisci(status, datiForm, new GestoreW3ANNEXB());
    } else {
      this.update(status, datiForm, new GestoreW3ANNEXB());
    }

    // Gestione della sezione multipla dei criteri per OEPV
    this.gestioneW3CRITERIA(status, datiForm);

    // Gestione dell'allegato D
    if (datiForm.isColumn("W3ANNEXD.ID")) {
      AbstractGestoreEntita gestoreW3ANNEXD = new DefaultGestoreEntita("W3ANNEXD", this.getRequest());
      if (datiForm.getColumn("W3ANNEXD.ID").getValue().getValue() == null) {
        datiForm.setValue("W3ANNEXD.ID", datiForm.getColumn("W3FS6.ID").getValue().getValue());
        this.inserisci(status, datiForm, gestoreW3ANNEXD);
      } else {
        this.update(status, datiForm, gestoreW3ANNEXD);
      }
    }

  }

  /**
   * Gestione dei criteri OEPV.
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneW3CRITERIA(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoremultiploW3CRITERIA = new DefaultGestoreEntitaChiaveNumerica("W3CRITERIA", "NUM",
        new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3CRITERIA, "W3CRITERIA",
        new DataColumn[] { datiForm.getColumn("W3FS6.ID") }, null);
  }

}
