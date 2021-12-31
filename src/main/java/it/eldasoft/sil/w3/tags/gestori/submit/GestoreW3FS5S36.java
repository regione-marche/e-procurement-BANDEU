package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

public class GestoreW3FS5S36 extends AbstractGestoreEntita {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3FS5S36.class);

  public String getEntita() {
    return "W3FS5S36";
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

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm)
      throws GestoreException {

  }


}
