package it.eldasoft.sil.w3.tags.gestori.submit;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.bl.GeneManager;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.DefaultGestoreEntita;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.utils.UtilityPermessi;

public class GestoreGRP extends AbstractGestoreChiaveNumerica {

  static Logger logger = Logger.getLogger(GestoreGRP.class);

  public String[] getAltriCampiChiave() {
    return null;
  }

  public String getCampoNumericoChiave() {
    return "IDGRP";
  }

  public String getEntita() {
    return "GRP";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {
    TransactionStatus status = null;
    boolean commitTransaction = false;
    try {
      status = this.sqlManager.startTransaction();
      this.gestioneGRPUSRSYS(datiForm);
      commitTransaction = true;
    } catch (Exception e) {
      commitTransaction = false;
    } finally {
      if (status != null) {
        if (commitTransaction) {
          try {
            this.sqlManager.commitTransaction(status);
          } catch (SQLException e) {

          }
        } else {
          try {
            this.sqlManager.rollbackTransaction(status);
          } catch (SQLException e) {

          }
        }
      }
    }
  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("GestoreGRP-preDelete: inizio metodo ");
    GeneManager geneManager = this.getGeneManager();
    Long idgrp = datiForm.getLong("GRP.IDGRP");
    geneManager.deleteTabelle(new String[] { "GRPUSRSYS" }, "IDGRP = ?", new Object[] { idgrp });
    if (logger.isDebugEnabled()) logger.debug("GestoreGRP-preDelete: fine metodo ");
  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    this.gestioneGRPUSRSYS(datiForm);
  }

  private void gestioneGRPUSRSYS(DataColumnContainer datiForm) throws GestoreException {
    try {
      Long idgrp = datiForm.getLong("GRP.IDGRP");
      String listasyscon = datiForm.getString("LISTASYSCON");
      
      sqlManager.update("delete from grpusrsys where idgrp = ?", new Object[] { idgrp });
      
      if (listasyscon != null && !"".equals(listasyscon.trim())) {
        String maxGRPUSRSYS = "select max(idgrpusrsys) from grpusrsys";
        String insertGRPUSRSYS = "insert into grpusrsys (idgrpusrsys, idgrp, syscon) values (?,?,?)";
        String[] a_syscon = listasyscon.split(",");
        for (int i = 0; i < a_syscon.length; i++) {
          Long syscon = new Long(a_syscon[i]);
          Long idgrpusrsys = (Long) this.sqlManager.getObject(maxGRPUSRSYS, new Object[] {});
          if (idgrpusrsys == null) idgrpusrsys = new Long(0);
          idgrpusrsys = new Long(idgrpusrsys.longValue() + 1);
          sqlManager.update(insertGRPUSRSYS, new Object[] { idgrpusrsys, idgrp, syscon });
        }
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella memorizzazione degli utenti del gruppo", null, e);
    }
  }

}
