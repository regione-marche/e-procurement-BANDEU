package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreIMPR;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.properties.ConfigManager;

public class GestoreIMPRtoW3 extends GestoreIMPR {

  /** Logger */
  static Logger               logger                  = Logger.getLogger(GestoreIMPRtoW3.class);

  private static final String PROP_PROTEZIONE_ARCHIVI = "it.eldasoft.protezionearchivi";

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreIMPRtoW3: inizio metodo");

    super.preDelete(status, datiForm);

    String codimp = datiForm.getString("IMPR.CODIMP");
    geneManager.deleteTabelle(new String[] { "W3PERMESSI" }, "CODIMP = ?", new Object[] { codimp });
    geneManager.deleteTabelle(new String[] { "PERM" }, "TBLNAME = ? AND CLM1NAME = ? AND CLM1VALUE_VC = ?", new Object[] { "IMPR",
        "CODIMP", codimp });

    if (logger.isDebugEnabled()) logger.debug("GestoreIMPRtoW3: fine metodo");

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreIMPRtoW3: inizio metodo");

    super.preInsert(status, datiForm);
    
    // Gestione entita' di estensione W3UFFIN
    AbstractGestoreEntita gestoreW3IMPR = new DefaultGestoreEntita("W3IMPR", this.getRequest());
    datiForm.setValue("W3IMPR.CODIMP", datiForm.getColumn("IMPR.CODIMP").getValue().getValue());
    this.inserisci(status, datiForm, gestoreW3IMPR);

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);
    if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
      datiForm.addColumn("W3PERMESSI.CODIMP", JdbcParametro.TIPO_TESTO, datiForm.getString("IMPR.CODIMP"));
      this.inserisci(status, datiForm, new GestoreW3PERMESSI());
    }

    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    UtilityPermessi.inserisciPermessi(new Long(profilo.getId()), "IMPR", "CODIMP", datiForm.getString("IMPR.CODIMP"), sqlManager);

    if (logger.isDebugEnabled()) logger.debug("GestoreIMPRtoW3: fine metodo");
  }
  
  
  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    super.preUpdate(status, datiForm);
    
    AbstractGestoreEntita gestoreW3IMPR = new DefaultGestoreEntita("W3IMPR", this.getRequest());
    if (datiForm.getColumn("W3IMPR.CODIMP").getValue().getValue() == null) {
      datiForm.setValue("W3IMPR.CODIMP", datiForm.getColumn("IMPR.CODIMP").getValue().getValue());
      this.inserisci(status, datiForm, gestoreW3IMPR);
    } else {
      this.update(status, datiForm, gestoreW3IMPR);
    }

  }

}
