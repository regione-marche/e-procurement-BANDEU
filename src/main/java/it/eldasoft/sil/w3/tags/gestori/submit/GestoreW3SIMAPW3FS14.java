package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveIDAutoincrementante;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.utils.UtilityPermessi;

public class GestoreW3SIMAPW3FS14 extends AbstractGestoreChiaveIDAutoincrementante {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3SIMAPW3FS14.class);

  public String[] getAltriCampiChiave() {
    return null;
  }

  public String getCampoNumericoChiave() {
    return "ID";
  }

  public String getEntita() {
    return "W3SIMAP";
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

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS14-preInsert: inizio metodo ");

    super.preInsert(status, datiForm);
    datiForm.setValue("W3FS14.ID", datiForm.getColumn("W3SIMAP.ID").getValue().getValue());
    this.inserisci(status, datiForm, new GestoreW3FS14());

    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    datiForm.addColumn("W3SIMAP.SYSCON", new Long(profilo.getId()));
    UtilityPermessi.inserisciPermessi(new Long(profilo.getId()), "W3SIMAP", "ID", datiForm.getLong("W3SIMAP.ID"), sqlManager);

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS14-preInsert: fine metodo ");

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS14-preUpdate: inizio metodo ");

    if (datiForm.getColumn("W3FS14.ID").getValue().getValue() == null) {
      datiForm.setValue("W3FS14.ID", datiForm.getColumn("W3SIMAP.ID").getValue().getValue());
      this.inserisci(status, datiForm, new GestoreW3FS14());
    } else {
      this.update(status, datiForm, new GestoreW3FS14());
    }

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS14-preUpdate: fine metodo ");

  }

}
