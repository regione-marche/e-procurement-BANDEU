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

public class GestoreUNIT extends AbstractGestoreChiaveNumerica {

  static Logger logger = Logger.getLogger(GestoreUNIT.class);

  public String[] getAltriCampiChiave() {
    return null;
  }

  public String getCampoNumericoChiave() {
    return "IDUNIT";
  }

  public String getEntita() {
    return "UNIT";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {
    TransactionStatus status = null;
    boolean commitTransaction = false;
    try {
      status = this.sqlManager.startTransaction();
      this.gestioneUNITUSRSYS(datiForm);
      this.gestioneUNITGRP(datiForm);
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
    if (logger.isDebugEnabled()) logger.debug("GestoreUNIT-preDelete: inizio metodo ");
    GeneManager geneManager = this.getGeneManager();
    Long idunit = datiForm.getLong("UNIT.IDUNIT");
    geneManager.deleteTabelle(new String[] { "UNITUSRSYS", "UNITGRP" }, "IDUNIT = ?", new Object[] { idunit });
    if (logger.isDebugEnabled()) logger.debug("GestoreUNIT-preDelete: fine metodo ");
  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {
    this.gestioneUNITUSRSYS(datiForm);
    this.gestioneUNITGRP(datiForm);
  }

  private void gestioneUNITUSRSYS(DataColumnContainer datiForm) throws GestoreException {
    try {
      Long idunit = datiForm.getLong("UNIT.IDUNIT");
      String listasyscon = datiForm.getString("LISTASYSCON");

      sqlManager.update("delete from unitusrsys where idunit = ?", new Object[] { idunit });

      if (listasyscon != null && !"".equals(listasyscon.trim())) {
        String maxUNITUSRSYS = "select max(id) from unitusrsys";
        String insertUNITUSRSYS = "insert into unitusrsys (id, idunit, syscon) values (?,?,?)";
        String[] a_syscon = listasyscon.split(",");
        for (int i = 0; i < a_syscon.length; i++) {
          Long syscon = new Long(a_syscon[i]);
          Long id = (Long) this.sqlManager.getObject(maxUNITUSRSYS, new Object[] {});
          if (id == null) id = new Long(0);
          id = new Long(id.longValue() + 1);
          sqlManager.update(insertUNITUSRSYS, new Object[] { id, idunit, syscon });
        }
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella memorizzazione degli utenti dell'unita' organizzativa", null, e);
    }
  }

  private void gestioneUNITGRP(DataColumnContainer datiForm) throws GestoreException {
    try {
      Long idunit = datiForm.getLong("UNIT.IDUNIT");
      String listaidgrp = datiForm.getString("LISTAIDGRP");

      sqlManager.update("delete from unitgrp where idunit = ?", new Object[] { idunit });

      if (listaidgrp != null && !"".equals(listaidgrp.trim())) {
        String maxUNITGRP = "select max(id) from unitgrp";
        String insertUNITGRP = "insert into unitgrp (id, idunit, idgrp) values (?,?,?)";
        String[] a_idgrp = listaidgrp.split(",");
        for (int i = 0; i < a_idgrp.length; i++) {
          Long idgrp = new Long(a_idgrp[i]);
          Long id = (Long) this.sqlManager.getObject(maxUNITGRP, new Object[] {});
          if (id == null) id = new Long(0);
          id = new Long(id.longValue() + 1);
          sqlManager.update(insertUNITGRP, new Object[] { id, idunit, idgrp });
        }
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella memorizzazione dei gruppi dell'unita' organizzativa", null, e);
    }
  }

}
