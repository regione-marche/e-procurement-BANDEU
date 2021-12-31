package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreUFFINT;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.properties.ConfigManager;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreUFFINTtoW3 extends GestoreUFFINT {

  /** Logger */
  static Logger               logger                  = Logger.getLogger(GestoreUFFINTtoW3.class);

  private static final String PROP_PROTEZIONE_ARCHIVI = "it.eldasoft.protezionearchivi";

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreUFFINTtoW3: inizio metodo");

    super.preDelete(status, datiForm);

    String codein = datiForm.getString("UFFINT.CODEIN");
    geneManager.deleteTabelle(new String[] { "W3PERMESSI", "W3UFFINT" }, "CODEIN = ?", new Object[] { codein });
    geneManager.deleteTabelle(new String[] { "PERM" }, "TBLNAME = ? AND CLM1NAME = ? AND CLM1VALUE_VC = ?", new Object[] { "UFFINT",
        "CODEIN", codein });

    if (logger.isDebugEnabled()) logger.debug("GestoreUFFINTtoW3: fine metodo");

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreUFFINTtoW3: inizio metodo");

    super.preInsert(status, datiForm);

    // Gestione entita' di estensione W3UFFIN
    AbstractGestoreEntita gestoreW3UFFINT = new DefaultGestoreEntita("W3UFFINT", this.getRequest());
    datiForm.setValue("W3UFFINT.CODEIN", datiForm.getColumn("UFFINT.CODEIN").getValue().getValue());
    this.inserisci(status, datiForm, gestoreW3UFFINT);
    
    // Gestione delle protezioni
    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);
    if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
      datiForm.addColumn("W3PERMESSI.CODEIN", JdbcParametro.TIPO_TESTO, datiForm.getString("UFFINT.CODEIN"));
      this.inserisci(status, datiForm, new GestoreW3PERMESSI());
    }

    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    UtilityPermessi.inserisciPermessi(new Long(profilo.getId()), "UFFINT", "CODEIN", datiForm.getString("UFFINT.CODEIN"), sqlManager);

    if (logger.isDebugEnabled()) logger.debug("GestoreUFFINTtoW3: fine metodo");

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    super.preUpdate(status, datiForm);
    
    AbstractGestoreEntita gestoreW3UFFINT = new DefaultGestoreEntita("W3UFFINT", this.getRequest());
    if (datiForm.getColumn("W3UFFINT.CODEIN").getValue().getValue() == null) {
      datiForm.setValue("W3UFFINT.CODEIN", datiForm.getColumn("UFFINT.CODEIN").getValue().getValue());
      this.inserisci(status, datiForm, gestoreW3UFFINT);
    } else {
      this.update(status, datiForm, gestoreW3UFFINT);
    }

  }

}
