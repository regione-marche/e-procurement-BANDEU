package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.bl.GeneManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

public class GestoreW3FS14 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS14.class);

  public String[] getAltriCampiChiave() {
    return null;
  }


  public String getEntita() {
    return "W3FS14";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    GeneManager geneManager = this.getGeneManager();
    Long id = datiForm.getLong("W3FS14.ID");
    geneManager.deleteTabelle(new String[] {"W3SIMAP", "W3FS14CORR" }, "ID = ?", new Object[] { id });

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    
    AbstractGestoreChiaveNumerica gestoremultiploW3FS14CORR = new DefaultGestoreEntitaChiaveNumerica("W3FS14CORR",
        "NUM", new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3FS14CORR, "W3FS14CORR",
        new DataColumn[] { datiForm.getColumn("W3FS14.ID") }, null);

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    AbstractGestoreChiaveNumerica gestoremultiploW3FS14CORR = new DefaultGestoreEntitaChiaveNumerica("W3FS14CORR",
        "NUM", new String[] { "ID" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3FS14CORR, "W3FS14CORR",
        new DataColumn[] { datiForm.getColumn("W3FS14.ID") }, null);

  }

}
