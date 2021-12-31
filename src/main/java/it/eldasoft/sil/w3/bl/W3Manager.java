/*
 * Created on 13/nov/08
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.bl;

import it.eldasoft.gene.bl.GenChiaviManager;
import it.eldasoft.gene.bl.LoginManager;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.bl.system.LdapManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.domain.admin.Account;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.sicurezza.CriptazioneException;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.CommunicationException;

public class W3Manager {

  /** Logger */
  static Logger              logger = Logger.getLogger(W3Manager.class);

  private SqlManager         sqlManager;

  protected GenChiaviManager genChiaviManager;

  protected LoginManager     loginManager;

  private LdapManager        ldapManager;

  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  public void setGenChiaviManager(GenChiaviManager genChiaviManager) {
    this.genChiaviManager = genChiaviManager;
  }

  public void setLoginManager(LoginManager loginManager) {
    this.loginManager = loginManager;
  }

  public void setLdapManager(LdapManager ldapManager) {
    this.ldapManager = ldapManager;
  }

  /**
   * Protezione archivi
   * 
   * @param idAccount
   * @param archivio
   * @param codice
   * @throws GestoreException
   * @throws SQLException
   */
  public void gestioneProtezioneArchivi(Long idAccount, String archivio, String codice) throws GestoreException, SQLException {

    Long numper = new Long(this.genChiaviManager.getMaxId("W3PERMESSI", "NUMPER") + 1);
    DataColumn[] dcW3PERMESSI = new DataColumn[] { new DataColumn("W3PERMESSI.NUMPER", new JdbcParametro(JdbcParametro.TIPO_NUMERICO,
        numper)) };
    DataColumnContainer dccW3PERMESSI = new DataColumnContainer(dcW3PERMESSI);
    dccW3PERMESSI.addColumn("W3PERMESSI.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);

    if ("IMPR".equals(archivio)) {
      dccW3PERMESSI.addColumn("W3PERMESSI.CODIMP", JdbcParametro.TIPO_TESTO, codice);
    } else if ("TECNI".equals(archivio)) {
      dccW3PERMESSI.addColumn("W3PERMESSI.CODTEC", JdbcParametro.TIPO_TESTO, codice);
    } else if ("UFFINT".equals(archivio)) {
      dccW3PERMESSI.addColumn("W3PERMESSI.CODEIN", JdbcParametro.TIPO_TESTO, codice);
    } else if ("W3AMMI".equals(archivio)) {
      dccW3PERMESSI.addColumn("W3PERMESSI.CODAMM", JdbcParametro.TIPO_TESTO, codice);
    }
    dccW3PERMESSI.insert("W3PERMESSI", this.sqlManager);
  }

  /**
   * Procedura di autenticazione. Restituisce l'idAccount. Serve per la
   * memorizzazione del proprietario della gara inserita
   * 
   * @param login
   * @param password
   * @return
   */
  public int getIdAccount(String login, String password) throws Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.getIdAccount: inizio metodo");

    if ("".equals(password)) password = null;
    Account account = null;

    try {
      if (login == null && password == null) {
        throw new Exception(UtilityTags.getResource("errors.login.mancanoCredenziali", null, false));
      }

      account = this.loginManager.getAccountByLoginEPassword(login, password);
      if (account != null) {
        if (account.getFlagLdap().intValue() == 1) {
          if (password == null && account.getPassword() == null) {
          } else {
            ldapManager.verificaAccount(account.getDn(), password);
          }
        }
      } else {
        throw new Exception(UtilityTags.getResource("errors.login.unknown", null, false));
      }

    } catch (CriptazioneException cr) {
      throw new Exception("Errore di autenticazione", cr);
    } catch (DataAccessException d) {
      throw new Exception(UtilityTags.getResource("errors.database.dataAccessException", null, false), d);
    } catch (AuthenticationException a) {
      throw new Exception(UtilityTags.getResource("errors.ldap.autenticazioneFallita", null, false), a);
    } catch (CommunicationException c) {
      throw new Exception(UtilityTags.getResource("errors.ldap.autenticazioneFallita", null, false), c);
    } catch (RuntimeException r) {
      throw new Exception(UtilityTags.getResource("errors.login.loginDoppia", null, false), r);
    } catch (Exception e) {
      throw e;
    } catch (Throwable t) {
      throw new Exception(UtilityTags.getResource("errors.applicazione.inaspettataException", null, false), t);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.getIdAccount: fine metodo");

    return account.getIdAccount();

  }

}
