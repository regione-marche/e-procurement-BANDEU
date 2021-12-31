package it.eldasoft.sil.w3.tags.gestori.submit;

import it.eldasoft.gene.bl.GeneManager;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

public class GestoreW3SIMAP extends AbstractGestoreChiaveNumerica {

  static Logger logger = Logger.getLogger(GestoreW3SIMAP.class);

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

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAP-preDelete: inizio metodo ");

    GeneManager geneManager = this.getGeneManager();

    Long id = datiForm.getLong("W3SIMAP.ID");
    String form;

    try {

      // Cancellazione delle tabelle comuni
      geneManager.deleteTabelle(new String[] { "W3SIMAPEMAIL", "W3SIMAPWS", "W3SIMAP_ADDR", "W3ANNEXB", "W3CPV", "W3LANGUAGE",
          "W3AWCRITERIA" }, "ID = ?", new Object[] { id });

      form = (String) geneManager.getSql().getObject("select form from w3simap where id = ?", new Object[] { id });

      if ("FS1".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS1", "W3FS1S2" }, "ID = ?", new Object[] { id });
      }

      if ("FS2".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS2", "W3FS2S36", "W3CRITERIA" }, "ID = ?", new Object[] { id });
      }

      if ("FS3".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS3", "W3CRITERIA", "W3FS3AWARD", "W3FS3AWARD_C", "W3ANNEXD" }, "ID = ?", new Object[] { id });
      }
      
      if ("FS5".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS5", "W3FS5S36", "W3CRITERIA" }, "ID = ?", new Object[] { id });
      }
      
      if ("FS6".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS6", "W3CRITERIA", "W3FS6AWARD", "W3FS6AWARD_C", "W3ANNEXD" }, "ID = ?", new Object[] { id });
      }
      
      if ("FS7".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS7", "W3CRITERIA", "W3FS7_Q" }, "ID = ?", new Object[] { id });
      }      

      if ("FS8".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS8", "W3FS8S2" }, "ID = ?", new Object[] { id });
      }

      if ("FS9".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS9" }, "ID = ?", new Object[] { id });
      }

      if ("FS14".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS14", "W3FS14CORR" }, "ID = ?", new Object[] { id });
      }
      
      if ("FS20".equals(form)) {
        geneManager.deleteTabelle(new String[] { "W3FS20", "W3FS20AWARD_C", "W3FS20AWARD_C" }, "ID = ?", new Object[] { id });
      } 

      // Cancellazione dei permessi
      geneManager.deleteTabelle(new String[] { "PERM" }, "TBLNAME = ? AND CLM1NAME = ? AND CLM1VALUE_NU = ?", new Object[] { "W3SIMAP",
          "ID", id });

      // Cancellazione di tutte le rettifiche (se form è diverso da FS14)
      if (!"FS14".equals(form)) {
        // Cancellazione di tutte le rettifiche
        String selectW3FS14 = "select id from w3fs14 where id_rif = ?";
        List datiW3FS14 = geneManager.getSql().getListVector(selectW3FS14, new Object[] { id });
        if (datiW3FS14 != null && datiW3FS14.size() > 0) {
          for (int i = 0; i < datiW3FS14.size(); i++) {
            Long w3fs14_id = (Long) SqlManager.getValueFromVectorParam(datiW3FS14.get(i), 0).getValue();
            geneManager.deleteTabelle(new String[] { "W3SIMAP", "W3SIMAPEMAIL", "W3FS14CORR" }, "ID = ?", new Object[] { w3fs14_id });
          }
        }
        geneManager.deleteTabelle(new String[] { "W3FS14" }, "ID_RIF = ?", new Object[] { id });
      }

    } catch (SQLException e) {
      throw new GestoreException("Errore nella selezione del tipo di formulario", "", e);
    }

    if (logger.isDebugEnabled()) logger.debug("GestoreW3SIMAP-preDelete: fine metodo ");

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

  }

}
