package it.eldasoft.sil.w3.tags.gestori.submit;

import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

public class GestoreW3PERMESSI extends AbstractGestoreChiaveNumerica {

  public String[] getAltriCampiChiave() {
    return null;
  }

  public String getCampoNumericoChiave() {
    return "NUMPER";
  }

  public String getEntita() {
    return "W3PERMESSI";
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

    super.preInsert(status, datiForm);
    
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(
        CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    datiForm.addColumn("W3PERMESSI.SYSCON", JdbcParametro.TIPO_NUMERICO,
        new Long(profilo.getId()));

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm)
      throws GestoreException {

  }

}
