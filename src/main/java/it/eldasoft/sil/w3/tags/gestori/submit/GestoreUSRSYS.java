package it.eldasoft.sil.w3.tags.gestori.submit;

import java.sql.SQLException;

import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

public class GestoreUSRSYS extends AbstractGestoreEntita {

  public String getEntita() {
    return "USRSYS";
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

    try {
      Long conteggio = (Long) sqlManager.getObject(
          "select count(*) from w3usrsys where syscon = ?",
          new Object[] { datiForm.getLong("USRSYS.SYSCON") });

      if (conteggio != null && conteggio.longValue() > 0) {
        datiForm.getColumn("W3USRSYS.SYSCON").setChiave(true);
        datiForm.setValue("W3USRSYS.SYSCON", datiForm.getLong("USRSYS.SYSCON"));
        datiForm.setOriginalValue("W3USRSYS.SYSCON", new JdbcParametro(
            JdbcParametro.TIPO_NUMERICO, datiForm.getLong("USRSYS.SYSCON")));
        this.update(status, datiForm, new GestoreW3USRSYS());
      } else {
        datiForm.setValue("W3USRSYS.SYSCON", datiForm.getLong("USRSYS.SYSCON"));
        this.inserisci(status, datiForm, new GestoreW3USRSYS());
      }

    } catch (SQLException e) {
      throw new GestoreException("Errore nella gestione del RUP",
          "gestioneIDGARACIG.sqlerror", e);
    }

  }

}
