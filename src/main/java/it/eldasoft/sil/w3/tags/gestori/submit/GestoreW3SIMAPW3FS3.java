package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.bl.GeneManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveIDAutoincrementante;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.utils.UtilityPermessi;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreW3SIMAPW3FS3 extends AbstractGestoreChiaveIDAutoincrementante {

  /** Logger */
  static Logger logger = Logger.getLogger(GestoreW3SIMAPW3FS3.class);

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

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS3-preInsert: inizio metodo ");

    super.preInsert(status, datiForm);
    // Gestione dei permessi
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    datiForm.addColumn("W3SIMAP.SYSCON", new Long(profilo.getId()));
    UtilityPermessi.inserisciPermessi(new Long(profilo.getId()), "W3SIMAP", "ID", datiForm.getLong("W3SIMAP.ID"), sqlManager);

    // Salvataggio su W3FS3 e figlie
    datiForm.setValue("W3FS3.ID", datiForm.getColumn("W3SIMAP.ID").getValue().getValue());
    this.inserisci(status, datiForm, new GestoreW3FS3());
    
    this.gestioneW3SIMAP_ADDR(status, datiForm);

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS3-preInsert: fine metodo ");

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS3-preUpdate: inizio metodo ");

    if (datiForm.getColumn("W3FS3.ID").getValue().getValue() == null) {
      datiForm.setValue("W3FS3.ID", datiForm.getColumn("W3SIMAP.ID").getValue().getValue());
      this.inserisci(status, datiForm, new GestoreW3FS3());
    } else {
      this.update(status, datiForm, new GestoreW3FS3());
    }
    
    this.gestioneW3SIMAP_ADDR(status, datiForm);

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAPW3FS3-preUpdate: fine metodo ");

  }
  
  
  /**
   * Gestione delle amministrazioni aggiudicatrici congiunte.
   * 
   * @param status
   * @param datiForm
   * @throws GestoreException
   */
  private void gestioneW3SIMAP_ADDR(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    if (datiForm.isColumn("W3SIMAP.JOINT_PROCUREMENT")) {
      if ("2".equals(datiForm.getString("W3SIMAP.JOINT_PROCUREMENT"))) {
        GeneManager geneManager = this.getGeneManager();
        Long id = datiForm.getLong("W3SIMAP.ID");
        geneManager.deleteTabelle(new String[] { "W3SIMAP_ADDR" }, "ID = ?", new Object[] { id });
      } else {
        AbstractGestoreChiaveNumerica gestoremultiploW3SIMAP_ADDR = new DefaultGestoreEntitaChiaveNumerica("W3SIMAP_ADDR", "NUM",
            new String[] { "ID" }, this.getRequest());
        this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoremultiploW3SIMAP_ADDR, "W3SIMAP_ADDR",
            new DataColumn[] { datiForm.getColumn("W3SIMAP.ID") }, null);

      }
    }
  }

}
