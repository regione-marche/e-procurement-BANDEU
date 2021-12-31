/*
 * Created on 08 Ott 2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.gestori.submit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.transaction.TransactionStatus;

import it.eldasoft.gene.bl.system.MailManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.domain.PropsConfig;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.utils.MailUtils;
import it.eldasoft.gene.web.struts.tags.gestori.AbstractGestoreChiaveNumerica;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.bl.ExportXMLSIMAPManager;
import it.eldasoft.utils.mail.IMailSender;
import it.eldasoft.utils.mail.MailSenderException;
import it.eldasoft.utils.properties.ConfigManager;
import it.eldasoft.utils.spring.UtilitySpring;
import it.eldasoft.utils.sql.comp.SqlComposer;
import it.eldasoft.utils.sql.comp.SqlManager;
import it.eldasoft.utils.utility.UtilityDate;
import it.eldasoft.utils.utility.UtilityStringhe;

public class GestoreW3SIMAPEMAIL extends AbstractGestoreChiaveNumerica {

  private static final String PROP_SIMAP_ESENDER_LOGIN      = "it.eldasoft.simap.esenderlogin";
  private static final String PROP_SIMAP_DESTINATARIO_ALFA  = "it.eldasoft.simap.destinatario.alfa";
  private static final String PROP_SIMAP_DESTINATARIO_BETA  = "it.eldasoft.simap.destinatario.beta";
  private static final String PROP_SIMAP_DESTINATARIO_GAMMA = "it.eldasoft.simap.destinatario.gamma";

  public String[] getAltriCampiChiave() {
    return new String[] { "ID" };
  }

  public String getCampoNumericoChiave() {
    return "NUM";
  }

  public String getEntita() {
    return "W3SIMAPEMAIL";
  }

  public void postDelete(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postInsert(DataColumnContainer datiForm) throws GestoreException {

  }

  public void postUpdate(DataColumnContainer datiForm) throws GestoreException {

  }

  public void preInsert(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

    super.preInsert(status, datiForm);

    this.getRequest().setAttribute("ID", datiForm.getLong("ID").toString());
    this.getRequest().setAttribute("NUM", datiForm.getLong("NUM"));

    try {

      ExportXMLSIMAPManager exportXMLSIMAPManager = (ExportXMLSIMAPManager) UtilitySpring.getBean("exportXMLSIMAPManager",
          this.getServletContext(), ExportXMLSIMAPManager.class);

      MailManager mailManager = (MailManager) UtilitySpring.getBean("mailManager", this.getServletContext(), MailManager.class);

      ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);

      Long syscon = new Long(profilo.getId());
      String personalReplyTo = profilo.getNome();
      String addressReplyTo = (String) sqlManager.getObject("select email from usrsys where syscon = ?", new Object[] { syscon });

      String esender_login = ConfigManager.getValore(PROP_SIMAP_ESENDER_LOGIN);
      if (esender_login == null || (esender_login != null && "".equals(esender_login))) {
        this.getRequest().setAttribute("RISULTATO", "ERRORI");
        throw new GestoreException("Non è definita la login del TED eSender", "mailSIMAP.nologinTEDeSender", null);
      }

      Long id = datiForm.getLong("ID");
      String customer_login = (String) sqlManager.getObject(
          "select customer_login from w3ammi where codamm = (select codamm from w3simap where id = ?)", new Object[] { id });

      String emailphase = datiForm.getString("EMAILPHASE");

      String emailto = null;
      if ("ALFA".equals(emailphase)) {
        emailto = ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_ALFA);
      } else if ("BETA".equals(emailphase)) {
        emailto = ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_BETA);
      } else if ("GAMMA".equals(emailphase)) {
        emailto = ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_GAMMA);
      }

      if (emailto == null || (emailto != null && "".equals(emailto))) {
        this.getRequest().setAttribute("RISULTATO", "ERRORI");
        throw new GestoreException("Non è definito alcun indirizzo email per il destinatario", "mailSIMAP.mailnoemailto", null);
      }

      // EMAILSUBJECT
      Long conteggio = (Long) sqlManager.getObject("select count(*) from w3simapemail", new Object[] {});
      if (conteggio == null) conteggio = new Long(0);
      conteggio = new Long(conteggio.longValue() + 1);

      String emailsubject = esender_login;
      if (customer_login != null && !"".equals(customer_login)) emailsubject += customer_login;
      emailsubject += "-XML";

      Date dataOdierna = UtilityDate.getDataOdiernaAsDate();
      Long progressivoGiornaliero = null;

      SqlComposer composer;
      composer = SqlManager.getComposer(ConfigManager.getValore(CostantiGenerali.PROP_DATABASE));
      String selectProgressivoGiornaliero = "select max(progressivog) from w3simapemail ";
      selectProgressivoGiornaliero += " where "
          + composer.getDateAsStringAAAAMMGG("W3SIMAPEMAIL.DATA_CREAZIONE")
          + " = '"
          + UtilityDate.convertiData(dataOdierna, UtilityDate.FORMATO_AAAAMMGG)
          + "'";
      progressivoGiornaliero = (Long) sqlManager.getObject(selectProgressivoGiornaliero, new Object[] {});

      if (progressivoGiornaliero == null) progressivoGiornaliero = new Long(0);
      progressivoGiornaliero = new Long(progressivoGiornaliero.longValue() + 1);
      NumberFormat progressivoGiornalieroF = new DecimalFormat("00");

      emailsubject += progressivoGiornalieroF.format(progressivoGiornaliero) + "-";
      emailsubject += new Long(dataOdierna.getYear() + 1900).toString();
      NumberFormat mesiGiorniF = new DecimalFormat("00");
      emailsubject += mesiGiorniF.format(new Long(dataOdierna.getMonth() + 1));
      emailsubject += mesiGiorniF.format(new Long(dataOdierna.getDate()));

      // NO_DOC_EXT
      String no_doc_ext = "";
      no_doc_ext = new Long(dataOdierna.getYear() + 1900).toString() + "-";
      NumberFormat conteggioF = new DecimalFormat("000000");
      no_doc_ext += conteggioF.format(conteggio);

      // EMAILBODY
      String emailbody = esender_login;
      if (customer_login != null && !"".equals(customer_login)) emailbody += customer_login;
      emailbody += "-" + no_doc_ext;

      // XML_NAME
      String xml_name = emailbody + ".xml";
      String xml = exportXMLSIMAPManager.export(id, syscon, no_doc_ext);

      // ATTACHMENT_NAME
      String attachment_name = emailsubject + ".zip";

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ZipOutputStream out = new ZipOutputStream(baos);
      out.putNextEntry(new ZipEntry(xml_name));
      out.write(xml.getBytes());
      out.flush();
      out.close();

      // Composizione ed invio mail
//      IMailSender mailSender = MailUtils.getInstance(mailManager, "W3");

      String emailfrom = null;
//      PropsConfig propEmailFrom = mailManager.getPropsConfigManager().getProperty("W3", "mail.indirizzoApplicativo");
//      if (propEmailFrom != null) {
//        emailfrom = propEmailFrom.getValore();
//      } else {
//        propEmailFrom = mailManager.getPropsConfigManager().getProperty("W_", "mail.indirizzoApplicativo");
//        if (propEmailFrom != null) emailfrom = propEmailFrom.getValore();
//      }

      datiForm.getColumn("W3SIMAPEMAIL.ID").setChiave(true);
      datiForm.getColumn("W3SIMAPEMAIL.NUM").setChiave(true);
      datiForm.addColumn("W3SIMAPEMAIL.PROGRESSIVOG", JdbcParametro.TIPO_NUMERICO, progressivoGiornaliero);
      datiForm.addColumn("W3SIMAPEMAIL.EMAILFROM", JdbcParametro.TIPO_TESTO, emailfrom);
      datiForm.addColumn("W3SIMAPEMAIL.EMAILTO", JdbcParametro.TIPO_TESTO, emailto);
      datiForm.addColumn("W3SIMAPEMAIL.EMAILSUBJECT", JdbcParametro.TIPO_TESTO, emailsubject);
      datiForm.addColumn("W3SIMAPEMAIL.EMAILBODY", JdbcParametro.TIPO_TESTO, emailbody);
      datiForm.addColumn("W3SIMAPEMAIL.XML_NAME", JdbcParametro.TIPO_TESTO, xml_name);
      datiForm.addColumn("W3SIMAPEMAIL.XML", JdbcParametro.TIPO_TESTO, xml);
      datiForm.addColumn("W3SIMAPEMAIL.ATTACHMENT_NAME", JdbcParametro.TIPO_TESTO, attachment_name);
      datiForm.addColumn("W3SIMAPEMAIL.ATTACHMENT", JdbcParametro.TIPO_BINARIO, baos);
      datiForm.addColumn("W3SIMAPEMAIL.DATA_CREAZIONE", new Timestamp(UtilityDate.getDataOdiernaAsDate().getTime()));
      datiForm.addColumn("W3SIMAPEMAIL.DATA_SPEDIZIONE", new Timestamp(UtilityDate.getDataOdiernaAsDate().getTime()));
      datiForm.addColumn("W3SIMAPEMAIL.DATA_SPEDIZIONE_S",
          UtilityDate.convertiData(UtilityDate.getDataOdiernaAsDate(), UtilityDate.FORMATO_GG_MM_AAAA_HH_MI_SS));

      baos.close();

//      mailSender.setNomeMittente(null);
//      mailSender.setMailRispondiA(addressReplyTo);
//      mailSender.setNomeRispondiA(personalReplyTo);
//
//      mailSender.send(new String[] { emailto }, new String[] { addressReplyTo }, null, emailsubject, emailbody,
//          new String[] { attachment_name }, new ByteArrayOutputStream[] { baos });

      this.getRequest().setAttribute("RISULTATO", "COMPOSIZIONEESEGUITA");

    } catch (GestoreException e) {
      this.getRequest().setAttribute("RISULTATO", "ERRORI");
      throw e;

    } catch (IOException io) {
      this.getRequest().setAttribute("RISULTATO", "ERRORI");
      throw new GestoreException("Errore nella composizione ed invio di una nuova email", "mailSIMAP.ioerror", io);

    } catch (SQLException sqle) {
      this.getRequest().setAttribute("RISULTATO", "ERRORI");
      throw new GestoreException("Errore nella composizione ed invio di una nuova email", "mailSIMAP.dberror", sqle);

//    } catch (MailSenderException mse) {
//      this.getRequest().setAttribute("RISULTATO", "ERRORI");
//
//      String logMessageKey = mse.getChiaveResourceBundle();
//      String logMessageError = this.resBundleGenerale.getString(logMessageKey);
//      for (int i = 0; mse.getParametri() != null && i < mse.getParametri().length; i++) {
//        logMessageError = logMessageError.replaceAll(UtilityStringhe.getPatternParametroMessageBundle(i), (String) mse.getParametri()[i]);
//      }
//
//      throw new GestoreException("Errore nella composizione ed invio di una nuova email", "mailSIMAP.mailsendererror",
//          new Object[] { logMessageError }, mse);

    } catch (Throwable t) {
      this.getRequest().setAttribute("RISULTATO", "ERRORI");
      throw new GestoreException("Errore nella composizione ed invio di una nuova email", "mailSIMAP.error", t);
    }

  }

  public void preDelete(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

  }

  public void preUpdate(TransactionStatus status, DataColumnContainer datiForm) throws GestoreException {

  }

}
