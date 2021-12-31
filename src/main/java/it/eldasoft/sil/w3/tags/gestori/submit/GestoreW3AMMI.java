package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntitaChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.properties.ConfigManager;

public class GestoreW3AMMI extends AbstractGestoreEntita {

  /** Logger */
  static Logger               logger                  = Logger.getLogger(GestoreW3AMMI.class);

  private static final String PROP_PROTEZIONE_ARCHIVI = "it.eldasoft.protezionearchivi";

  public String getEntita() {
    return "W3AMMI";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3AMMI: inizio metodo");

    String codamm = datiForm.getString("W3AMMI.CODAMM");
    geneManager.deleteTabelle(new String[] { "W3PERMESSI" }, "CODAMM = ?", new Object[] { codamm });
    geneManager.deleteTabelle(new String[] { "PERM" }, "TBLNAME = ? AND CLM1NAME = ? AND CLM1VALUE_VC = ?", new Object[] { "W3AMMI",
        "CODAMM", codamm });

    if (logger.isDebugEnabled()) logger.debug("GestoreW3AMMI: fine metodo");

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreW3AMMI: inizio metodo");

    if (geneManager.isCodificaAutomatica("W3AMMI", "CODAMM")) {
      datiForm.getColumn("W3AMMI.CODAMM").setChiave(true);
      datiForm.setValue("W3AMMI.CODAMM", geneManager.calcolaCodificaAutomatica("W3AMMI", "CODAMM"));
    }

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);
    if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
      datiForm.addColumn("W3PERMESSI.CODAMM", JdbcParametro.TIPO_TESTO, datiForm.getString("W3AMMI.CODAMM"));
      this.inserisci(status, datiForm, new GestoreW3PERMESSI());
    }
    
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    UtilityPermessi.inserisciPermessi(new Long(profilo.getId()), "W3AMMI", "CODAMM", datiForm.getString("W3AMMI.CODAMM"), sqlManager);
    
    this.gestioneW3AMMI_PB(status, datiForm);

    if (logger.isDebugEnabled()) logger.debug("GestoreW3AMMI: fine metodo");

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    datiForm.getColumn("W3AMMI.CODAMM").setChiave(true);
    this.gestioneW3AMMI_PB(status, datiForm);

  }

  private void gestioneW3AMMI_PB(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    AbstractGestoreChiaveNumerica gestoreMultiploW3AMMI_PB = new DefaultGestoreEntitaChiaveNumerica("W3AMMI_PB", "NUM",
        new String[] { "CODAMM" }, this.getRequest());
    this.gestisciAggiornamentiRecordSchedaMultipla(status, datiForm, gestoreMultiploW3AMMI_PB, "W3AMMI_PB",
        new DataColumn[] { datiForm.getColumn("W3AMMI.CODAMM"), }, null);
  }

}
