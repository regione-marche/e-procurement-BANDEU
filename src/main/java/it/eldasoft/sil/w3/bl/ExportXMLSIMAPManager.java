package it.eldasoft.sil.w3.bl;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.simap.xml.ExportFS1;
import it.eldasoft.sil.w3.simap.xml.ExportFS14;
import it.eldasoft.sil.w3.simap.xml.ExportFS2;
import it.eldasoft.sil.w3.simap.xml.ExportFS20;
import it.eldasoft.sil.w3.simap.xml.ExportFS3;
import it.eldasoft.sil.w3.simap.xml.ExportFS4;
import it.eldasoft.sil.w3.simap.xml.ExportFS5;
import it.eldasoft.sil.w3.simap.xml.ExportFS6;
import it.eldasoft.sil.w3.simap.xml.ExportFS7;
import it.eldasoft.sil.w3.simap.xml.ExportFS8;
import it.eldasoft.utils.properties.ConfigManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import eu.europa.publications.resource.schema.ted.r209.reception.F012014Document.F012014;
import eu.europa.publications.resource.schema.ted.r209.reception.F022014Document.F022014;
import eu.europa.publications.resource.schema.ted.r209.reception.F032014Document.F032014;
import eu.europa.publications.resource.schema.ted.r209.reception.F042014Document.F042014;
import eu.europa.publications.resource.schema.ted.r209.reception.F052014Document.F052014;
import eu.europa.publications.resource.schema.ted.r209.reception.F062014Document.F062014;
import eu.europa.publications.resource.schema.ted.r209.reception.F072014Document.F072014;
import eu.europa.publications.resource.schema.ted.r209.reception.F082014Document.F082014;
import eu.europa.publications.resource.schema.ted.r209.reception.F142014Document.F142014;
import eu.europa.publications.resource.schema.ted.r209.reception.F202014Document.F202014;
import eu.europa.publications.resource.schema.ted.r209.reception.FormSection;
import eu.europa.publications.resource.schema.ted.r209.reception.Sender;
import eu.europa.publications.resource.schema.ted.r209.reception.Sender.CONTACT;
import eu.europa.publications.resource.schema.ted.r209.reception.Sender.IDENTIFICATION;
import eu.europa.publications.resource.schema.ted.r209.reception.TEDESENDERSDocument;
import eu.europa.publications.resource.schema.ted.r209.reception.TedEsenders;

/**
 * Manager per la generazione del file XML dei dati da inviare al SIMAP.
 * 
 * 
 * 
 */
public class ExportXMLSIMAPManager {

  /** Logger */
  static Logger                 logger                       = Logger.getLogger(ExportXMLSIMAPManager.class);

  /** Manager SQL per le operazioni su database */
  private SqlManager            sqlManager;

  protected static final String PROP_SIMAP_TED_ESENDER_CLASS = "it.eldasoft.simap.tedesenderclass";
  protected static final String PROP_SIMAP_ESENDER_LOGIN     = "it.eldasoft.simap.esenderlogin";
  protected static final String PROP_SIMAP_EMAIL_TECHNICAL   = "it.eldasoft.simap.email.technical";

  /**
   * 
   * @return
   */
  public SqlManager getSqlManager() {
    return this.sqlManager;
  }

  /**
   * Set SqlManager
   * 
   * @param sqlManager
   */
  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  /**
   * Esportazione in formato XML
   * 
   * @param id
   * @param syscon
   * @return
   * @throws GestoreException
   */
  public String export(Long id, Long syscon, String no_doc_ext) throws GestoreException {

    String testoXML = "";

    try {

      TEDESENDERSDocument tedEsenderDocument = TEDESENDERSDocument.Factory.newInstance();
      tedEsenderDocument.documentProperties().setEncoding("UTF-8");
      TedEsenders tedEsenders = tedEsenderDocument.addNewTEDESENDERS();
      tedEsenders.addNewVERSION().setStringValue("R2.0.9.S04");

      // Identificazione
      Sender sender = tedEsenders.addNewSENDER();
      IDENTIFICATION identification = sender.addNewIDENTIFICATION();

      // Ted ESender qualificato
      String esender_login = ConfigManager.getValore(PROP_SIMAP_ESENDER_LOGIN);
      identification.setESENDERLOGIN(esender_login);

      // Identificazione cliente
      String form = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });
      String customer_login = null;

      if ("FS14".equals(form)) {
        Long id_rif = (Long) sqlManager.getObject("select id_rif from w3fs14 where id = ?", new Object[] { id });
        if (id_rif != null) {
          customer_login = (String) sqlManager.getObject(
              "select customer_login from w3ammi where codamm = (select codamm from w3simap where id = ?)", new Object[] { id_rif });
        }
      } else {
        customer_login = (String) sqlManager.getObject(
            "select customer_login from w3ammi where codamm = (select codamm from w3simap where id = ?)", new Object[] { id });
      }
      if (customer_login != null) {
        identification.setCUSTOMERLOGIN(customer_login);
      }

      // Gestione della data di invio
      String updateNoticeDate = "update W3" + form.toUpperCase() + " set notice_date = ? where id = ?";
      sqlManager.update(updateNoticeDate, new Object[] { new Date(), id });

      // Versione del software
      String software_version = (String) sqlManager.getObject("select numver from eldaver where codapp = ?", new Object[] { "W3" });
      identification.setSOFTWAREVERSION(software_version);

      // Numero documento del cliente
      identification.setNODOCEXT(no_doc_ext);

      String email_technical = ConfigManager.getValore(PROP_SIMAP_EMAIL_TECHNICAL);
      CONTACT contact = sender.addNewCONTACT();
      contact.setORGANISATION("Maggioli S.p.A.");
      contact.addNewCOUNTRY().setVALUE("IT");
      contact.setEMAIL(email_technical);

      // Form section
      FormSection formSection = tedEsenders.addNewFORMSECTION();

      // Gestione formulari
      if ("FS1".equals(form)) {
        ExportFS1 exportFS1 = new ExportFS1();
        F012014 f01 = exportFS1.exportF01(sqlManager, id);
        formSection.addNewF012014();
        formSection.setF012014Array(0, f01);

      } else if ("FS2".equals(form)) {
        ExportFS2 exportFS2 = new ExportFS2();
        F022014 f02 = exportFS2.exportF02(sqlManager, id);
        formSection.addNewF022014();
        formSection.setF022014Array(0, f02);

      } else if ("FS3".equals(form)) {
        ExportFS3 exportFS3 = new ExportFS3();
        F032014 f03 = exportFS3.exportF03(sqlManager, id);
        formSection.addNewF032014();
        formSection.setF032014Array(0, f03);

      } else if ("FS4".equals(form)) {
        ExportFS4 exportFS4 = new ExportFS4();
        F042014 f04 = exportFS4.exportF04(sqlManager, id);
        formSection.addNewF042014();
        formSection.setF042014Array(0, f04);
        
      } else if ("FS5".equals(form)) {
        ExportFS5 exportFS5 = new ExportFS5();
        F052014 f05 = exportFS5.exportF05(sqlManager, id);
        formSection.addNewF052014();
        formSection.setF052014Array(0, f05);

      } else if ("FS6".equals(form)) {
        ExportFS6 exportFS6 = new ExportFS6();
        F062014 f06 = exportFS6.exportF06(sqlManager, id);
        formSection.addNewF062014();
        formSection.setF062014Array(0, f06);

      } else if ("FS7".equals(form)) {
        ExportFS7 exportFS7 = new ExportFS7();
        F072014 f07 = exportFS7.exportF07(sqlManager, id);
        formSection.addNewF072014();
        formSection.setF072014Array(0, f07);

      } else if ("FS8".equals(form)) {
        ExportFS8 exportFS8 = new ExportFS8();
        F082014 f08 = exportFS8.exportF08(sqlManager, id);
        formSection.addNewF082014();
        formSection.setF082014Array(0, f08);

      } else if ("FS14".equals(form)) {
        ExportFS14 exportFS14 = new ExportFS14();
        F142014 f14 = exportFS14.exportF14(sqlManager, id);
        formSection.addNewF142014();
        formSection.setF142014Array(0, f14);
        
      } else if ("FS20".equals(form)) {
        ExportFS20 exportFS20 = new ExportFS20();
        F202014 f20 = exportFS20.exportF20(sqlManager, id);
        formSection.addNewF202014();
        formSection.setF202014Array(0, f20);
      }

      // Esportazione del contenuto
      XmlOptions opts = new XmlOptions();
      opts.setUseDefaultNamespace();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      tedEsenderDocument.save(baos, opts);
      testoXML = baos.toString("UTF-8");
      baos.close();
      
      // Rimozione caratteri UNICODE non ammessi
      testoXML = testoXML.replaceAll("\uFFFD", " ");

      // Validazione del contenuto XML
      ArrayList<String> validationErrors = new ArrayList<String>();
      XmlOptions validationOptions = new XmlOptions();
      validationOptions.setErrorListener(validationErrors);
      boolean isValid = tedEsenderDocument.validate(validationOptions);

      if (!isValid) {
        String listaErroriValidazione = "";
        Iterator<?> iter = validationErrors.iterator();
        while (iter.hasNext()) {
          listaErroriValidazione += iter.next() + "\n";
        }
        logger.error("Il file XML non rispetta il formato previsto: " + testoXML + "\n" + listaErroriValidazione);
        throw new GestoreException("Il file XML non rispetta il formato previsto", "exportXML.validate",
            new Object[] { listaErroriValidazione }, null);
      }

      logger.debug(testoXML);

    } catch (GestoreException e) {
      throw e;
    } catch (IOException e) {
      throw new GestoreException("Errore nell'esportazione dei dati", "exportXML.ioerror", e);
    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati", "exportXML.sqlerror", e);
    }

    return testoXML;

  }

}
