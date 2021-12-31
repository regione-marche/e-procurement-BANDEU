package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreW3FS6W3FS6AWARD extends AbstractGestoreEntita {

  static Logger logger = Logger.getLogger(GestoreW3FS6W3FS6AWARD.class);

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
    datiForm.setValue("W3FS6AWARD.ID", datiForm.getColumn("W3FS6.ID").getValue().getValue());
    datiForm.setValue("W3FS6AWARD.ITEM", new Long(1));
    this.inserisci(status, datiForm, new GestoreW3ANNEXB());

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (datiForm.getColumn("W3FS6AWARD.ID").getValue().getValue() == null) {
      datiForm.setValue("W3FS6AWARD.ID", datiForm.getColumn("W3FS6.ID").getValue().getValue());
      datiForm.setValue("W3FS6AWARD.ITEM", new Long(1));
      this.inserisci(status, datiForm, new GestoreW3FS6AWARD());
    } else {
      this.update(status, datiForm, new GestoreW3FS6AWARD());
    }
  }

}
