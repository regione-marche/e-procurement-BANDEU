package it.eldasoft.sil.w3.tags.gestori.submit;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreTECNI;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.properties.ConfigManager;

public class GestoreTECNItoW3 extends GestoreTECNI {

  /** Logger */
  static Logger               logger                  = Logger.getLogger(GestoreTECNItoW3.class);

  private static final String PROP_PROTEZIONE_ARCHIVI = "it.eldasoft.protezionearchivi";

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreTECNItoW3: inizio metodo");

    super.preDelete(status, datiForm);

    String codtec = datiForm.getString("TECNI.CODTEC");
    geneManager.deleteTabelle(new String[] { "W3PERMESSI" }, "CODTEC = ?", new Object[] { codtec });
    geneManager.deleteTabelle(new String[] { "PERM" }, "TBLNAME = ? AND CLM1NAME = ? AND CLM1VALUE_VC = ?", new Object[] { "TECNI",
        "CODTEC", codtec });

    if (logger.isDebugEnabled()) logger.debug("GestoreTECNItoW3: fine metodo");

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("GestoreTECNItoW3: inizio metodo");

    super.preInsert(status, datiForm);

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);
    if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
      datiForm.addColumn("W3PERMESSI.CODTEC", JdbcParametro.TIPO_TESTO, datiForm.getString("TECNI.CODTEC"));
      this.inserisci(status, datiForm, new GestoreW3PERMESSI());
    }

    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);
    UtilityPermessi.inserisciPermessi(new Long(profilo.getId()), "TECNI", "CODTEC", datiForm.getString("TECNI.CODTEC"), sqlManager);

    if (logger.isDebugEnabled()) logger.debug("GestoreTECNItoW3: fine metodo");

  }

}
