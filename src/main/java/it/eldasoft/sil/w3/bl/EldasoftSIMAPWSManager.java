package it.eldasoft.sil.w3.bl;

import it.eldasoft.gene.bl.GenChiaviManager;
import it.eldasoft.gene.bl.GeneManager;
import it.eldasoft.gene.bl.LoginManager;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.bl.system.LdapManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.db.datautils.DataColumnContainer;
import it.eldasoft.gene.db.domain.admin.Account;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.simap.ws.xmlbeans.AuthorityAType;
import it.eldasoft.simap.ws.xmlbeans.AuthorityType;
import it.eldasoft.simap.ws.xmlbeans.AvvisoAggiudicazioneDocument;
import it.eldasoft.simap.ws.xmlbeans.AvvisoAggiudicazioneLottoType;
import it.eldasoft.simap.ws.xmlbeans.AvvisoAggiudicazioneSettoriSpecialiDocument;
import it.eldasoft.simap.ws.xmlbeans.AvvisoAggiudicazioneSettoriSpecialiType;
import it.eldasoft.simap.ws.xmlbeans.AvvisoAggiudicazioneType;
import it.eldasoft.simap.ws.xmlbeans.AvvisoPeriodicoIndicativoDocument;
import it.eldasoft.simap.ws.xmlbeans.AvvisoPeriodicoIndicativoType;
import it.eldasoft.simap.ws.xmlbeans.AvvisoPreinformazioneDocument;
import it.eldasoft.simap.ws.xmlbeans.AvvisoPreinformazioneType;
import it.eldasoft.simap.ws.xmlbeans.AvvisoProfiloCommittenteDocument;
import it.eldasoft.simap.ws.xmlbeans.AvvisoProfiloCommittenteType;
import it.eldasoft.simap.ws.xmlbeans.BandoGaraDocument;
import it.eldasoft.simap.ws.xmlbeans.BandoGaraSettoriSpecialiDocument;
import it.eldasoft.simap.ws.xmlbeans.BandoGaraSettoriSpecialiType;
import it.eldasoft.simap.ws.xmlbeans.BandoGaraType;
import it.eldasoft.simap.ws.xmlbeans.CPVType;
import it.eldasoft.simap.ws.xmlbeans.CriteriaType;
import it.eldasoft.simap.ws.xmlbeans.EconomicOperatorType;
import it.eldasoft.simap.ws.xmlbeans.LottoAggiudicatoType;
import it.eldasoft.simap.ws.xmlbeans.LottoCommonType;
import it.eldasoft.utils.metadata.cache.DizionarioCampi;
import it.eldasoft.utils.metadata.domain.Campo;
import it.eldasoft.utils.properties.ConfigManager;
import it.eldasoft.utils.sicurezza.CriptazioneException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.CommunicationException;

public class EldasoftSIMAPWSManager {

  protected static final Long NOTICE_F01_PRI_ONLY                 = new Long(1);
  protected static final Long NOTICE_F01_PRI_REDUCING_TIME_LIMITS = new Long(2);
  protected static final Long NOTICE_F01_PRI_CALL_COMPETITION     = new Long(3);

  protected static final Long NOTICE_F04_PER_ONLY                 = new Long(1);
  protected static final Long NOTICE_F04_PER_CALL_COMPETITION     = new Long(2);
  protected static final Long NOTICE_F04_PER_REDUCING_TIME_LIMITS = new Long(3);

  static Logger               logger                              = Logger.getLogger(EldasoftSIMAPWSManager.class);

  private static final String PROP_PROTEZIONE_ARCHIVI             = "it.eldasoft.protezionearchivi";

  private GeneManager         geneManager;

  private SqlManager          sqlManager;

  protected LoginManager      loginManager;

  private LdapManager         ldapManager;

  protected GenChiaviManager  genChiaviManager;

  private W3Manager           w3Manager;

  public void setGeneManager(GeneManager geneManager) {
    this.geneManager = geneManager;
  }

  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  public void setLoginManager(LoginManager loginManager) {
    this.loginManager = loginManager;
  }

  public void setLdapManager(LdapManager ldapManager) {
    this.ldapManager = ldapManager;
  }

  public void setGenChiaviManager(GenChiaviManager genChiaviManager) {
    this.genChiaviManager = genChiaviManager;
  }

  public void setW3Manager(W3Manager w3Manager) {
    this.w3Manager = w3Manager;
  }

  /**
   * Inserimento di un bando di gara
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciBandoGara(String login, String password, String datiXML) throws XmlException, GestoreException, SQLException,
      Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciBandoGara: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    BandoGaraDocument bandoGaraDocument = BandoGaraDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = bandoGaraDocument.validate(validationOptions);

    if (isValid) {

      BandoGaraType bandoGara = bandoGaraDocument.getBandoGara();

      // Lettura UUID e controllo univocita'
      String uuid = bandoGara.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS2 = new DataColumn[] { new DataColumn("W3FS2.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS2 = new DataColumnContainer(dcW3FS2);
      DataColumn[] dcW3FS2S36 = new DataColumn[] { new DataColumn("W3FS2S36.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS2S36 = new DataColumnContainer(dcW3FS2S36);
      DataColumn[] dcW3LANGUAGE = new DataColumn[] { new DataColumn("W3LANGUAGE.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3LANGUAGE = new DataColumnContainer(dcW3LANGUAGE);

      // Identificativo gara SIMOG
      if (bandoGara.isSetIDGARA()) {
        String idgara = bandoGara.getIDGARA();
        dccW3FS2.addColumn("W3FS2.IDGARA", JdbcParametro.TIPO_TESTO, idgara);
      }

      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS2");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, bandoGara.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);
      dccW3SIMAP.addColumn("W3SIMAP.LEGAL_BASIS", JdbcParametro.TIPO_TESTO, "32014L0024");

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = bandoGara.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // Disponibilita' dei documenti di gara
      if (bandoGara.isSetDOCUMENTFURE()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_FU_RE", JdbcParametro.TIPO_NUMERICO, new Long(bandoGara.getDOCUMENTFURE().longValue()));
      }

      // URL dei documenti di gara
      if (bandoGara.isSetDOCUMENTURL()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_URL", JdbcParametro.TIPO_TESTO, bandoGara.getDOCUMENTURL());
      }

      // Ulteriori informazioni
      if (bandoGara.isSetFURTHERINFOIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "1");
      } else if (bandoGara.isSetFURTHERINFOADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "2");
        if (bandoGara.isSetFURTHERINFOBODY()) {
          String further_info_codein = this.gestioneUFFINT(bandoGara.getFURTHERINFOBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO_CODEIN", JdbcParametro.TIPO_TESTO, further_info_codein);
        }
      }

      // Offerte e domande di partecipazione
      if (bandoGara.isSetPARTECIPATIONIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "1");
      } else if (bandoGara.isSetPARTECIPATIONADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "2");
        if (bandoGara.isSetPARTECIPATIONBODY()) {
          String participarion_codein = this.gestioneUFFINT(bandoGara.getPARTECIPATIONBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION_CODEIN", JdbcParametro.TIPO_TESTO, participarion_codein);
        }
      }

      // SEZIONE II - OGGETTO
      // Titolo
      dccW3FS2.addColumn("W3FS2.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, bandoGara.getTITLECONTRACT());

      // CPV
      if (bandoGara.isSetCPV()) {
        dccW3FS2.addColumn("W3FS2.CPV", JdbcParametro.TIPO_TESTO, bandoGara.getCPV());
      }

      // Tipo di appalto
      dccW3FS2.addColumn("W3FS2.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, bandoGara.getTYPECONTRACT().toString());

      // Breve descrizione
      if (bandoGara.isSetSHORTDESCRIPTION()) {
        dccW3FS2.addColumn("W3FS2.SCOPE_TOTAL", JdbcParametro.TIPO_TESTO, bandoGara.getSHORTDESCRIPTION());
      }

      // Valore totale stimato
      if (bandoGara.isSetSCOPECOST()) {
        dccW3FS2.addColumn("W3FS2.SCOPE_COST", JdbcParametro.TIPO_DECIMALE, new Double(bandoGara.getSCOPECOST()));
      }

      // Lotto/i
      if (bandoGara.isSetDIVINTOLOTSNO() && bandoGara.isSetLOT()) {
        // Bando di gara non suddiviso in lotti
        dccW3FS2.addColumn("W3FS2.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "2");

        // Lotto
        LottoCommonType lot = bandoGara.getLOT();
        this.inserisciW3ANNEXB(id, new Long(1), lot);
      } else if (bandoGara.isSetDIVINTOLOTSYES()) {
        // Bando di gara suddiviso in lotti
        dccW3FS2.addColumn("W3FS2.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "1");

        // Le offerte vanno presentate per..
        if (bandoGara.isSetDIVLOTSVALUE()) {
          dccW3FS2.addColumn("W3FS2.DIV_LOTS_VALUE", JdbcParametro.TIPO_TESTO, bandoGara.getDIVLOTSVALUE().toString());
        }

        // Numero massimo di lotti per i quali presentare le offerte
        if (bandoGara.isSetDIVLOTSMAX()) {
          dccW3FS2.addColumn("W3FS2.DIV_LOTS_MAX", JdbcParametro.TIPO_NUMERICO, new Long(bandoGara.getDIVLOTSMAX()));
        }

        // Numero massimo di lotti che possono essere aggiudicati ad un
        // offerente
        if (bandoGara.isSetLOTSMAXTENDERER()) {
          dccW3FS2.addColumn("W3FS2.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO, new Long(bandoGara.getLOTSMAXTENDERER()));
        } else {
          if (bandoGara.isSetDIVLOTSVALUE() && bandoGara.getDIVLOTSVALUE().intValue() == 1) {
            dccW3FS2.addColumn("W3FS2.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO, new Long(1));
          }
        }

        // Lotti
        LottoCommonType[] lots = bandoGara.getLOTSArray();
        for (int lo = 0; lo < lots.length; lo++) {
          LottoCommonType lotto = lots[lo];
          this.inserisciW3ANNEXB(id, new Long(lo + 1), lotto);
        }
      }

      // SEZIONE IV - PROCEDURA
      // Tipo di procedura
      if (bandoGara.isSetTYPEPROCEDURE()) {
        dccW3FS2.addColumn("W3FS2.TYPE_PROCEDURE", JdbcParametro.TIPO_TESTO, bandoGara.getTYPEPROCEDURE().toString());
      }

      // Motivazioni per la procedura accelerata
      if (bandoGara.isSetACCELERATED()) {
        dccW3FS2.addColumn("W3FS2.ACCELERATED", JdbcParametro.TIPO_TESTO, bandoGara.getACCELERATED());
      }

      // L'appalto e' disciplinato dall'accordo sugli appalti pubblici ?
      if (bandoGara.isSetGPA()) {
        dccW3FS2.addColumn("W3FS2.GPA", JdbcParametro.TIPO_TESTO, bandoGara.getGPA().toString());
      }

      // Data termine per il ricevimento delle offerte o delle domande di
      // partecipazione
      if (bandoGara.isSetRECEIPTLIMITDATE()) {
        dccW3FS2.addColumn("W3FS2.RECEIPT_LIMIT_DATE", JdbcParametro.TIPO_DATA, bandoGara.getRECEIPTLIMITDATE().getTime());
      }

      // Ora termine per il ricevimento delle offerte o delle domande di
      // partecipazione
      if (bandoGara.isSetRECEIPTLIMITTIME()) {
        dccW3FS2.addColumn("W3FS2.RECEIPT_LIMIT_TIME", JdbcParametro.TIPO_TESTO, bandoGara.getRECEIPTLIMITTIME());
      }

      // Lingue utilizzabili- default IT
      dccW3LANGUAGE.addColumn("W3LANGUAGE.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));
      dccW3LANGUAGE.addColumn("W3LANGUAGE.LANGUAGE_EC", JdbcParametro.TIPO_TESTO, "IT");

      // Data di apertura delle offerte
      if (bandoGara.isSetOPENINGTENDERSDATE()) {
        dccW3FS2.addColumn("W3FS2.OPENING_TENDERS_DATE", JdbcParametro.TIPO_DATA, bandoGara.getOPENINGTENDERSDATE().getTime());
      }

      // Ora di apertura delle offerte
      if (bandoGara.isSetOPENINGTENDERSTIME()) {
        dccW3FS2.addColumn("W3FS2.OPENING_TENDERS_TIME", JdbcParametro.TIPO_TESTO, bandoGara.getOPENINGTENDERSTIME());
      }

      // Accordo quadro
      if (bandoGara.isSetFRAMEWORKNO()) {
        dccW3FS2.addColumn("W3FS2.FRAMEWORK", JdbcParametro.TIPO_TESTO, "2");
      } else if (bandoGara.isSetFRAMEWORKYES()) {
        dccW3FS2.addColumn("W3FS2.FRAMEWORK", JdbcParametro.TIPO_TESTO, "1");
        if (bandoGara.isSetFRAMESEVERALOPNO()) {
          dccW3FS2.addColumn("W3FS2.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "2");
        } else if (bandoGara.isSetFRAMESEVERALOPYES()) {
          dccW3FS2.addColumn("W3FS2.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "1");
          if (bandoGara.isSetFRAMEOPERATORSNUMBER()) {
            dccW3FS2.addColumn("W3FS2.FRAME_OPERATORS_NUMBER", JdbcParametro.TIPO_NUMERICO, new Long(bandoGara.getFRAMEOPERATORSNUMBER()));
          }
        }
      }

      // Asta elettronica ?
      if (bandoGara.isSetISELECTRONIC()) {
        dccW3FS2.addColumn("W3FS2.IS_ELECTRONIC", JdbcParametro.TIPO_TESTO, bandoGara.getISELECTRONIC().toString());
      }

      // SEZIONE VI - ALTRE INFORMAZIONI
      // Organismo responsabile delle procedure di ricorso
      if (bandoGara.isSetAPPEALPROCEDURE()) {
        String appeal_procedure_codein = this.gestioneUFFINT(bandoGara.getAPPEALPROCEDURE(), account);
        dccW3FS2S36.addColumn("W3FS2S36.APPEAL_PROCEDURE_CODEIN", JdbcParametro.TIPO_TESTO, appeal_procedure_codein);
      }

      // Appalto periodico ?
      if (bandoGara.isSetRECURRENTPROCNO()) {
        dccW3FS2S36.addColumn("W3FS2S36.RECURRENT_PROC", JdbcParametro.TIPO_TESTO, "2");
      } else if (bandoGara.isSetRECURRENTPROCYES()) {
        dccW3FS2S36.addColumn("W3FS2S36.RECURRENT_PROC", JdbcParametro.TIPO_TESTO, "1");
        if (bandoGara.isSetRECURRENTPROCDESC()) {
          dccW3FS2S36.addColumn("W3FS2S36.RECURRENT_PROC_DESC", JdbcParametro.TIPO_TESTO, bandoGara.getRECURRENTPROCDESC());
        }
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS2.insert("W3FS2", this.sqlManager);
      dccW3FS2S36.insert("W3FS2S36", this.sqlManager);
      dccW3LANGUAGE.insert("W3LANGUAGE", this.sqlManager);

      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciBandoGara: fine metodo");

  }

  /**
   * Inserimento del bando di gara per i settori speciali.
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciBandoGaraSettoriSpeciali(String login, String password, String datiXML) throws XmlException, GestoreException,
      SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciBandoGara: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    BandoGaraSettoriSpecialiDocument bandoGaraSettoriSpecialiDocument = BandoGaraSettoriSpecialiDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = bandoGaraSettoriSpecialiDocument.validate(validationOptions);

    if (isValid) {

      BandoGaraSettoriSpecialiType bandoGaraSettoriSpeciali = bandoGaraSettoriSpecialiDocument.getBandoGaraSettoriSpeciali();

      // Lettura UUID e controllo univocita'
      String uuid = bandoGaraSettoriSpeciali.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS5 = new DataColumn[] { new DataColumn("W3FS5.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS5 = new DataColumnContainer(dcW3FS5);
      DataColumn[] dcW3FS5S36 = new DataColumn[] { new DataColumn("W3FS5S36.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS5S36 = new DataColumnContainer(dcW3FS5S36);
      DataColumn[] dcW3LANGUAGE = new DataColumn[] { new DataColumn("W3LANGUAGE.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3LANGUAGE = new DataColumnContainer(dcW3LANGUAGE);

      // Identificativo gara SIMOG
      if (bandoGaraSettoriSpeciali.isSetIDGARA()) {
        String idgara = bandoGaraSettoriSpeciali.getIDGARA();
        dccW3FS5.addColumn("W3FS5.IDGARA", JdbcParametro.TIPO_TESTO, idgara);
      }
      
      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS5");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);
      dccW3SIMAP.addColumn("W3SIMAP.LEGAL_BASIS", JdbcParametro.TIPO_TESTO, "32014L0025");

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = bandoGaraSettoriSpeciali.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // Disponibilita' dei documenti di gara
      if (bandoGaraSettoriSpeciali.isSetDOCUMENTFURE()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_FU_RE", JdbcParametro.TIPO_NUMERICO, new Long(
            bandoGaraSettoriSpeciali.getDOCUMENTFURE().longValue()));
      }

      // URL dei documenti di gara
      if (bandoGaraSettoriSpeciali.isSetDOCUMENTURL()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_URL", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getDOCUMENTURL());
      }

      // Ulteriori informazioni
      if (bandoGaraSettoriSpeciali.isSetFURTHERINFOIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "1");
      } else if (bandoGaraSettoriSpeciali.isSetFURTHERINFOADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "2");
        if (bandoGaraSettoriSpeciali.isSetFURTHERINFOBODY()) {
          String further_info_codein = this.gestioneUFFINT(bandoGaraSettoriSpeciali.getFURTHERINFOBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO_CODEIN", JdbcParametro.TIPO_TESTO, further_info_codein);
        }
      }

      // Offerte e domande di partecipazione
      if (bandoGaraSettoriSpeciali.isSetPARTECIPATIONIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "1");
      } else if (bandoGaraSettoriSpeciali.isSetPARTECIPATIONADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "2");
        if (bandoGaraSettoriSpeciali.isSetPARTECIPATIONBODY()) {
          String participarion_codein = this.gestioneUFFINT(bandoGaraSettoriSpeciali.getPARTECIPATIONBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION_CODEIN", JdbcParametro.TIPO_TESTO, participarion_codein);
        }
      }

      // SEZIONE II - OGGETTO
      // Titolo
      dccW3FS5.addColumn("W3FS5.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getTITLECONTRACT());

      // CPV
      if (bandoGaraSettoriSpeciali.isSetCPV()) {
        dccW3FS5.addColumn("W3FS5.CPV", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getCPV());
      }

      // Tipo di appalto
      dccW3FS5.addColumn("W3FS5.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getTYPECONTRACT().toString());

      // Breve descrizione
      if (bandoGaraSettoriSpeciali.isSetSHORTDESCRIPTION()) {
        dccW3FS5.addColumn("W3FS5.SCOPE_TOTAL", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getSHORTDESCRIPTION());
      }

      // Valore totale stimato
      if (bandoGaraSettoriSpeciali.isSetSCOPECOST()) {
        dccW3FS5.addColumn("W3FS5.SCOPE_COST", JdbcParametro.TIPO_DECIMALE, new Double(bandoGaraSettoriSpeciali.getSCOPECOST()));
      }

      // Lotto/i
      if (bandoGaraSettoriSpeciali.isSetDIVINTOLOTSNO() && bandoGaraSettoriSpeciali.isSetLOT()) {
        // Bando di gara non suddiviso in lotti
        dccW3FS5.addColumn("W3FS5.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "2");

        // Lotto
        LottoCommonType lot = bandoGaraSettoriSpeciali.getLOT();
        this.inserisciW3ANNEXB(id, new Long(1), lot);
      } else if (bandoGaraSettoriSpeciali.isSetDIVINTOLOTSYES()) {
        // Bando di gara suddiviso in lotti
        dccW3FS5.addColumn("W3FS5.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "1");

        // Le offerte vanno presentate per..
        if (bandoGaraSettoriSpeciali.isSetDIVLOTSVALUE()) {
          dccW3FS5.addColumn("W3FS5.DIV_LOTS_VALUE", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getDIVLOTSVALUE().toString());
        }

        // Numero massimo di lotti per i quali presentare le offerte
        if (bandoGaraSettoriSpeciali.isSetDIVLOTSMAX()) {
          dccW3FS5.addColumn("W3FS5.DIV_LOTS_MAX", JdbcParametro.TIPO_NUMERICO, new Long(bandoGaraSettoriSpeciali.getDIVLOTSMAX()));
        }

        // Numero massimo di lotti che possono essere aggiudicati ad un
        // offerente
        if (bandoGaraSettoriSpeciali.isSetLOTSMAXTENDERER()) {
          dccW3FS5.addColumn("W3FS5.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO,
              new Long(bandoGaraSettoriSpeciali.getLOTSMAXTENDERER()));
        } else {
          if (bandoGaraSettoriSpeciali.isSetDIVLOTSVALUE() && bandoGaraSettoriSpeciali.getDIVLOTSVALUE().intValue() == 1) {
            dccW3FS5.addColumn("W3FS5.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO, new Long(1));
          }
        }

        // Lotti
        LottoCommonType[] lots = bandoGaraSettoriSpeciali.getLOTSArray();
        for (int lo = 0; lo < lots.length; lo++) {
          LottoCommonType lotto = lots[lo];
          this.inserisciW3ANNEXB(id, new Long(lo + 1), lotto);
        }
      }

      // SEZIONE III - INFORMAZIONI LEGATI
      // Norme e criteri oggettivi di partecipazione
      if (bandoGaraSettoriSpeciali.isSetRULESCRITERIA()) {
        dccW3FS5.addColumn("W3FS5.RULES_CRITERIA", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getRULESCRITERIA());
      }

      // Cauzioni e garanzie richieste
      if (bandoGaraSettoriSpeciali.isSetDEPOSITGUARANTEEREQUIRED()) {
        dccW3FS5.addColumn("W3FS5.DEPOSIT_GUARANTEE_REQUIRED", JdbcParametro.TIPO_TESTO,
            bandoGaraSettoriSpeciali.getDEPOSITGUARANTEEREQUIRED());
      }

      // Principali modalità di finanziamento e di pagamento e/o riferimenti
      // alle disposizioni applicabili in materia
      if (bandoGaraSettoriSpeciali.isSetMAINFINANCINGCONDITION()) {
        dccW3FS5.addColumn("W3FS5.MAIN_FINANCING_CONDITION", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getMAINFINANCINGCONDITION());
      }

      // Forma giuridica che dovrà assumere il raggruppamento di operatori
      // economici aggiudicatario dell'appalto
      if (bandoGaraSettoriSpeciali.isSetLEGALFORM()) {
        dccW3FS5.addColumn("W3FS5.LEGAL_FORM", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getLEGALFORM());
      }

      // SEZIONE IV - PROCEDURA
      // Tipo di procedura
      if (bandoGaraSettoriSpeciali.isSetTYPEPROCEDURE()) {
        dccW3FS5.addColumn("W3FS5.TYPE_PROCEDURE", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getTYPEPROCEDURE().toString());
      }

      // Motivazioni per la procedura accelerata
      if (bandoGaraSettoriSpeciali.isSetACCELERATED()) {
        dccW3FS5.addColumn("W3FS5.ACCELERATED", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getACCELERATED());
      }

      // L'appalto e' disciplinato dall'accordo sugli appalti pubblici ?
      if (bandoGaraSettoriSpeciali.isSetGPA()) {
        dccW3FS5.addColumn("W3FS5.GPA", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getGPA().toString());
      }

      // Data termine per il ricevimento delle offerte o delle domande di
      // partecipazione
      if (bandoGaraSettoriSpeciali.isSetRECEIPTLIMITDATE()) {
        dccW3FS5.addColumn("W3FS5.RECEIPT_LIMIT_DATE", JdbcParametro.TIPO_DATA, bandoGaraSettoriSpeciali.getRECEIPTLIMITDATE().getTime());
      }

      // Ora termine per il ricevimento delle offerte o delle domande di
      // partecipazione
      if (bandoGaraSettoriSpeciali.isSetRECEIPTLIMITTIME()) {
        dccW3FS5.addColumn("W3FS5.RECEIPT_LIMIT_TIME", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getRECEIPTLIMITTIME());
      }

      // Lingue utilizzabili- default IT
      dccW3LANGUAGE.addColumn("W3LANGUAGE.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));
      dccW3LANGUAGE.addColumn("W3LANGUAGE.LANGUAGE_EC", JdbcParametro.TIPO_TESTO, "IT");

      // Data di apertura delle offerte
      if (bandoGaraSettoriSpeciali.isSetOPENINGTENDERSDATE()) {
        dccW3FS5.addColumn("W3FS5.OPENING_TENDERS_DATE", JdbcParametro.TIPO_DATA,
            bandoGaraSettoriSpeciali.getOPENINGTENDERSDATE().getTime());
      }

      // Ora di apertura delle offerte
      if (bandoGaraSettoriSpeciali.isSetOPENINGTENDERSTIME()) {
        dccW3FS5.addColumn("W3FS5.OPENING_TENDERS_TIME", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getOPENINGTENDERSTIME());
      }

      // Accordo quadro
      if (bandoGaraSettoriSpeciali.isSetFRAMEWORKNO()) {
        dccW3FS5.addColumn("W3FS5.FRAMEWORK", JdbcParametro.TIPO_TESTO, "2");
      } else if (bandoGaraSettoriSpeciali.isSetFRAMEWORKYES()) {
        dccW3FS5.addColumn("W3FS5.FRAMEWORK", JdbcParametro.TIPO_TESTO, "1");
        if (bandoGaraSettoriSpeciali.isSetFRAMESEVERALOPNO()) {
          dccW3FS5.addColumn("W3FS5.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "2");
        } else if (bandoGaraSettoriSpeciali.isSetFRAMESEVERALOPYES()) {
          dccW3FS5.addColumn("W3FS5.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "1");
          if (bandoGaraSettoriSpeciali.isSetFRAMEOPERATORSNUMBER()) {
            dccW3FS5.addColumn("W3FS5.FRAME_OPERATORS_NUMBER", JdbcParametro.TIPO_NUMERICO,
                new Long(bandoGaraSettoriSpeciali.getFRAMEOPERATORSNUMBER()));
          }
        }
      }

      // Asta elettronica ?
      if (bandoGaraSettoriSpeciali.isSetISELECTRONIC()) {
        dccW3FS5.addColumn("W3FS5.IS_ELECTRONIC", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getISELECTRONIC().toString());
      }

      // SEZIONE VI - ALTRE INFORMAZIONI
      // Organismo responsabile delle procedure di ricorso
      if (bandoGaraSettoriSpeciali.isSetAPPEALPROCEDURE()) {
        String appeal_procedure_codein = this.gestioneUFFINT(bandoGaraSettoriSpeciali.getAPPEALPROCEDURE(), account);
        dccW3FS5S36.addColumn("W3FS5S36.APPEAL_PROCEDURE_CODEIN", JdbcParametro.TIPO_TESTO, appeal_procedure_codein);
      }

      // Appalto periodico ?
      if (bandoGaraSettoriSpeciali.isSetRECURRENTPROCNO()) {
        dccW3FS5S36.addColumn("W3FS5S36.RECURRENT_PROC", JdbcParametro.TIPO_TESTO, "2");
      } else if (bandoGaraSettoriSpeciali.isSetRECURRENTPROCYES()) {
        dccW3FS5S36.addColumn("W3FS5S36.RECURRENT_PROC", JdbcParametro.TIPO_TESTO, "1");
        if (bandoGaraSettoriSpeciali.isSetRECURRENTPROCDESC()) {
          dccW3FS5S36.addColumn("W3FS5S36.RECURRENT_PROC_DESC", JdbcParametro.TIPO_TESTO, bandoGaraSettoriSpeciali.getRECURRENTPROCDESC());
        }
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS5.insert("W3FS5", this.sqlManager);
      dccW3FS5S36.insert("W3FS5S36", this.sqlManager);
      dccW3LANGUAGE.insert("W3LANGUAGE", this.sqlManager);

      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciBandoGara: fine metodo");

  }

  /**
   * Inserimento del lotto.
   * 
   * @param id
   * @param lotto
   * @throws GestoreException
   * @throws SQLException
   */
  private void inserisciW3ANNEXB(Long id, Long num, LottoCommonType lotto) throws GestoreException, SQLException {
    DataColumn[] dcW3ANNEXB = new DataColumn[] { new DataColumn("W3ANNEXB.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
    DataColumnContainer dccW3ANNEXB = new DataColumnContainer(dcW3ANNEXB);

    dccW3ANNEXB.addColumn("W3ANNEXB.NUM", JdbcParametro.TIPO_NUMERICO, num);

    // Numero del lotto
    dccW3ANNEXB.addColumn("W3ANNEXB.LOTNUM", JdbcParametro.TIPO_NUMERICO, new Long(lotto.getLOTNO()));

    // Titolo del lotto
    dccW3ANNEXB.addColumn("W3ANNEXB.TITLE", JdbcParametro.TIPO_TESTO, lotto.getTITLE());

    // NUTS
    if (lotto.isSetNUTS()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.SITE_NUTS", JdbcParametro.TIPO_TESTO, lotto.getNUTS());
    }

    // Sito o luogo principale
    if (lotto.isSetSITELABEL()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.SITE_LABEL", JdbcParametro.TIPO_TESTO, lotto.getSITELABEL());
    }

    // Descrizione
    if (lotto.isSetDESCRIPTION()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.DESCRIPTION", JdbcParametro.TIPO_TESTO, lotto.getDESCRIPTION());
    }

    // CPV
    if (lotto.isSetCPV()) {
      this.inserisciW3CPV(lotto.getCPV(), "W3ANNEXB", id, num);
    }

    // Il prezzo non è il solo criterio di aggiudicazione e tutti i criteri sono
    // indicati solo nei documenti di gara
    if (lotto.isSetACDOC()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.AC_DOC", JdbcParametro.TIPO_TESTO, lotto.getACDOC().toString());
    }

    // Criteri
    CriteriaType[] criteriaArray = lotto.getAWCRITERIAArray();
    for (int cr = 0; cr < criteriaArray.length; cr++) {
      CriteriaType criteria = criteriaArray[cr];
      DataColumn[] dcW3AWCRITERIA = new DataColumn[] { new DataColumn("W3AWCRITERIA.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3AWCRITERIA = new DataColumnContainer(dcW3AWCRITERIA);
      dccW3AWCRITERIA.addColumn("W3AWCRITERIA.NUM", JdbcParametro.TIPO_NUMERICO, num);
      dccW3AWCRITERIA.addColumn("W3AWCRITERIA.ACNUM", JdbcParametro.TIPO_NUMERICO, new Long(cr + 1));
      dccW3AWCRITERIA.addColumn("W3AWCRITERIA.CRITERIA_TYPE", JdbcParametro.TIPO_TESTO, criteria.getTYPE().toString());
      if (criteria.isSetCRITERIA()) {
        dccW3AWCRITERIA.addColumn("W3AWCRITERIA.CRITERIA", JdbcParametro.TIPO_TESTO, criteria.getCRITERIA());
      }
      if (criteria.isSetWEIGHTING()) {
        dccW3AWCRITERIA.addColumn("W3AWCRITERIA.WEIGHTING", JdbcParametro.TIPO_NUMERICO, new Long(criteria.getWEIGHTING()));
      }
      dccW3AWCRITERIA.insert("W3AWCRITERIA", this.sqlManager);
    }

    // Valore stimato
    if (lotto.isSetCOST()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.COST", JdbcParametro.TIPO_DECIMALE, new Double(lotto.getCOST()));
    }

    // Durata del contratto d'appalto
    if (lotto.isSetWORKDAYS()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.WORK_DAYS", JdbcParametro.TIPO_NUMERICO, new Long(lotto.getWORKDAYS()));
    } else if (lotto.isSetWORKMONTH()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.WORK_MONTH", JdbcParametro.TIPO_NUMERICO, new Long(lotto.getWORKMONTH()));
    } else if (lotto.isSetWORKSTARTDATE() && lotto.isSetWORKENDDATE()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.WORK_START_DATE", JdbcParametro.TIPO_DATA, lotto.getWORKSTARTDATE().getTime());
      dccW3ANNEXB.addColumn("W3ANNEXB.WORK_END_DATE", JdbcParametro.TIPO_DATA, lotto.getWORKENDDATE().getTime());
    }

    // Informazioni relative ai limiti al numero di candidati che saranno
    // invitati a partecipare
    if (lotto.isSetNBMINCANDIDATE()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.NB_MIN_CANDIDATE", JdbcParametro.TIPO_NUMERICO, new Long(lotto.getNBMINCANDIDATE()));
    }
    if (lotto.isSetNBMAXCANDIDATE()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.NB_MAX_CANDIDATE", JdbcParametro.TIPO_NUMERICO, new Long(lotto.getNBMAXCANDIDATE()));
    }
    if (lotto.isSetCRITERIACANDIDATE()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.CRITERIA_CANDIDATE", JdbcParametro.TIPO_TESTO, lotto.getCRITERIACANDIDATE());
    }

    // Sono accettate varianti ?
    if (lotto.isSetACCVARIANTS()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.ACC_VARIANTS", JdbcParametro.TIPO_TESTO, lotto.getACCVARIANTS().toString());
    }

    // Il contratto d'appalto è oggetto di rinnovo ?
    if (lotto.isSetRENEWALNO()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.RENEWAL", JdbcParametro.TIPO_TESTO, "2");
    } else if (lotto.isSetRENEWALYES()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.RENEWAL", JdbcParametro.TIPO_TESTO, "1");
      if (lotto.isSetRENEWALDESCR()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.RENEWAL_DESCR", JdbcParametro.TIPO_TESTO, lotto.getRENEWALDESCR());
      }
    }

    // Informazioni relative alle opzioni
    if (lotto.isSetOPTIONSNO()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.OPTIONS", JdbcParametro.TIPO_TESTO, "2");
    } else if (lotto.isSetOPTIONSYES()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.OPTIONS", JdbcParametro.TIPO_TESTO, "1");
      if (lotto.isSetOPTIONSDESCR()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.OPTIONS_DESCR", JdbcParametro.TIPO_TESTO, lotto.getOPTIONSDESCR());
      }
    }

    // Informazioni relative ai fondi dell'Unione Europea
    if (lotto.isSetEUPROGRNO()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.EU_PROGR", JdbcParametro.TIPO_TESTO, "2");
    } else if (lotto.isSetEUPROGRYES()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.EU_PROGR", JdbcParametro.TIPO_TESTO, "1");
      if (lotto.isSetEUPROGRDESCR()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.EU_PROGR_DESCR", JdbcParametro.TIPO_TESTO, lotto.getEUPROGRDESCR());
      }
    }

    dccW3ANNEXB.insert("W3ANNEXB", this.sqlManager);

  }

  /**
   * Inserimento del lotto relativo all'avviso di aggiudicazione.
   * 
   * @param id
   * @param num
   * @param lotto
   * @throws GestoreException
   * @throws SQLException
   */
  private void inserisciW3ANNEXBAvvisoAggiudicazione(Long id, Long num, AvvisoAggiudicazioneLottoType lotto) throws GestoreException,
      SQLException {
    DataColumn[] dcW3ANNEXB = new DataColumn[] { new DataColumn("W3ANNEXB.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
    DataColumnContainer dccW3ANNEXB = new DataColumnContainer(dcW3ANNEXB);

    dccW3ANNEXB.addColumn("W3ANNEXB.NUM", JdbcParametro.TIPO_NUMERICO, num);

    // Numero del lotto
    dccW3ANNEXB.addColumn("W3ANNEXB.LOTNUM", JdbcParametro.TIPO_NUMERICO, new Long(lotto.getLOTNO()));

    // Titolo del lotto
    dccW3ANNEXB.addColumn("W3ANNEXB.TITLE", JdbcParametro.TIPO_TESTO, lotto.getTITLE());

    // NUTS
    if (lotto.isSetNUTS()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.SITE_NUTS", JdbcParametro.TIPO_TESTO, lotto.getNUTS());
    }

    // Sito o luogo principale
    if (lotto.isSetSITELABEL()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.SITE_LABEL", JdbcParametro.TIPO_TESTO, lotto.getSITELABEL());
    }

    // Descrizione
    if (lotto.isSetDESCRIPTION()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.DESCRIPTION", JdbcParametro.TIPO_TESTO, lotto.getDESCRIPTION());
    }

    // CPV
    if (lotto.isSetCPV()) {
      this.inserisciW3CPV(lotto.getCPV(), "W3ANNEXB", id, num);
    }

    // Consentire la pubblicazione dei criteri ?
    if (lotto.isSetAGREETOPUBLISH()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.AGREE_TO_PUBLISH", JdbcParametro.TIPO_TESTO, lotto.getAGREETOPUBLISH().toString());
    }

    // Criteri
    CriteriaType[] criteriaArray = lotto.getAWCRITERIAArray();
    for (int cr = 0; cr < criteriaArray.length; cr++) {
      CriteriaType criteria = criteriaArray[cr];
      DataColumn[] dcW3AWCRITERIA = new DataColumn[] { new DataColumn("W3AWCRITERIA.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3AWCRITERIA = new DataColumnContainer(dcW3AWCRITERIA);
      dccW3AWCRITERIA.addColumn("W3AWCRITERIA.NUM", JdbcParametro.TIPO_NUMERICO, num);
      dccW3AWCRITERIA.addColumn("W3AWCRITERIA.ACNUM", JdbcParametro.TIPO_NUMERICO, new Long(cr + 1));
      dccW3AWCRITERIA.addColumn("W3AWCRITERIA.CRITERIA_TYPE", JdbcParametro.TIPO_TESTO, criteria.getTYPE().toString());
      if (criteria.isSetCRITERIA()) {
        dccW3AWCRITERIA.addColumn("W3AWCRITERIA.CRITERIA", JdbcParametro.TIPO_TESTO, criteria.getCRITERIA());
      }
      if (criteria.isSetWEIGHTING()) {
        dccW3AWCRITERIA.addColumn("W3AWCRITERIA.WEIGHTING", JdbcParametro.TIPO_NUMERICO, new Long(criteria.getWEIGHTING()));
      }
      dccW3AWCRITERIA.insert("W3AWCRITERIA", this.sqlManager);
    }

    // Informazioni relative alle opzioni
    if (lotto.isSetOPTIONSNO()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.OPTIONS", JdbcParametro.TIPO_TESTO, "2");
    } else if (lotto.isSetOPTIONSYES()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.OPTIONS", JdbcParametro.TIPO_TESTO, "1");
      if (lotto.isSetOPTIONSDESCR()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.OPTIONS_DESCR", JdbcParametro.TIPO_TESTO, lotto.getOPTIONSDESCR());
      }
    }

    // Informazioni relative ai fondi dell'Unione Europea
    if (lotto.isSetEUPROGRNO()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.EU_PROGR", JdbcParametro.TIPO_TESTO, "2");
    } else if (lotto.isSetEUPROGRYES()) {
      dccW3ANNEXB.addColumn("W3ANNEXB.EU_PROGR", JdbcParametro.TIPO_TESTO, "1");
      if (lotto.isSetEUPROGRDESCR()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.EU_PROGR_DESCR", JdbcParametro.TIPO_TESTO, lotto.getEUPROGRDESCR());
      }
    }

    dccW3ANNEXB.insert("W3ANNEXB", this.sqlManager);

  }

  /**
   * Inserimento dei codici CPV degli oggetti complementari
   * 
   * 
   * @param cpvMainAdditionals
   * @param ent
   *        Entità di riferimento
   * @param id
   *        Chiave dell'entità di riferimento
   * @param num
   *        Numero progressivo (vale -1 se l'entità è di primo livello (W3FS2 e
   *        W3FS3)
   * @throws GestoreException
   * @throws SQLException
   */
  private void inserisciW3CPV(CPVType cpvMainAdditionals, String ent, Long id, Long num) throws GestoreException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciW3CPV: inizio metodo");

    // Inserimento del primo CPV
    Long numcpvMain = new Long(this.genChiaviManager.getMaxId("W3CPV", "NUMCPV") + 1);
    DataColumn[] dcW3CPVMain = new DataColumn[] { new DataColumn("W3CPV.NUMCPV", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, numcpvMain)) };
    DataColumnContainer dccW3CPVMain = new DataColumnContainer(dcW3CPVMain);
    dccW3CPVMain.addColumn("W3CPV.ENT", JdbcParametro.TIPO_TESTO, ent);
    dccW3CPVMain.addColumn("W3CPV.ID", JdbcParametro.TIPO_NUMERICO, id);
    dccW3CPVMain.addColumn("W3CPV.NUM", JdbcParametro.TIPO_NUMERICO, num);
    dccW3CPVMain.addColumn("W3CPV.TIPCPV", JdbcParametro.TIPO_NUMERICO, new Long(2));
    dccW3CPVMain.addColumn("W3CPV.CPV", JdbcParametro.TIPO_TESTO, cpvMainAdditionals.getCPVMAIN());
    dccW3CPVMain.insert("W3CPV", this.sqlManager);

    // Inserimento dei CPV aggiuntivi
    for (int i = 0; i < cpvMainAdditionals.getCPVADDITIONALArray().length; i++) {
      Long numcpvAdditional = new Long(this.genChiaviManager.getMaxId("W3CPV", "NUMCPV") + 1);
      DataColumn[] dcW3CPVAdditional = new DataColumn[] { new DataColumn("W3CPV.NUMCPV", new JdbcParametro(JdbcParametro.TIPO_NUMERICO,
          numcpvAdditional)) };
      DataColumnContainer dccW3CPVAdditional = new DataColumnContainer(dcW3CPVAdditional);
      dccW3CPVAdditional.addColumn("W3CPV.ENT", JdbcParametro.TIPO_TESTO, ent);
      dccW3CPVAdditional.addColumn("W3CPV.ID", JdbcParametro.TIPO_NUMERICO, id);
      dccW3CPVAdditional.addColumn("W3CPV.NUM", JdbcParametro.TIPO_NUMERICO, num);
      dccW3CPVAdditional.addColumn("W3CPV.TIPCPV", JdbcParametro.TIPO_NUMERICO, new Long(2));
      dccW3CPVAdditional.addColumn("W3CPV.CPV", JdbcParametro.TIPO_TESTO, cpvMainAdditionals.getCPVADDITIONALArray(i));
      dccW3CPVAdditional.insert("W3CPV", this.sqlManager);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciW3CPV: fine metodo");
  }

  /**
   * Gestione archivio IMPR per gli operatori economici
   * 
   * @param economicOperator
   * @param id
   * @throws GestoreException
   * @throws SQLException
   */
  private String gestioneIMPR(EconomicOperatorType economicOperator, Account account) throws GestoreException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.gestioneIMPR: inizio metodo");

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);

    boolean economicOperatorExist = false;
    String codimp = null;
    String cfimp = null;
    String pivimp = null;

    if (economicOperator.isSetCFIMP()) cfimp = economicOperator.getCFIMP();
    if (economicOperator.isSetPIVIMP()) pivimp = economicOperator.getPIVIMP();

    Long syscon = new Long(account.getIdAccount());
    String sysab3 = account.getAbilitazioneStd();

    if (cfimp != null) {
      String selectIMPR = "select codimp from impr where cfimp = ?";
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        String filtroPermessi = this.getFiltroPermessi(syscon, sysab3, "IMPR", "CODIMP", sqlManager);
        if (filtroPermessi != null) {
          selectIMPR += " and " + filtroPermessi;
        }
      }
      codimp = (String) sqlManager.getObject(selectIMPR, new Object[] { cfimp });
    } else if (pivimp != null) {
      String selectIMPR = "select codimp from impr where pivimp = ?";
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        String filtroPermessi = this.getFiltroPermessi(syscon, sysab3, "IMPR", "CODIMP", sqlManager);
        if (filtroPermessi != null) {
          selectIMPR += " and " + filtroPermessi;
        }
      }
      codimp = (String) sqlManager.getObject(selectIMPR, new Object[] { pivimp });
    }

    if (codimp != null) economicOperatorExist = true;

    if (!economicOperatorExist) {
      codimp = this.geneManager.calcolaCodificaAutomatica("IMPR", "CODIMP");

      DataColumn[] dcIMPR = new DataColumn[] { new DataColumn("IMPR.CODIMP", new JdbcParametro(JdbcParametro.TIPO_TESTO, codimp)) };
      DataColumnContainer dccIMPR = new DataColumnContainer(dcIMPR);
      DataColumn[] dcW3IMPR = new DataColumn[] { new DataColumn("W3IMPR.CODIMP", new JdbcParametro(JdbcParametro.TIPO_TESTO, codimp)) };
      DataColumnContainer dccW3IMPR = new DataColumnContainer(dcW3IMPR);

      dccIMPR.addColumn("IMPR.NOMEST", JdbcParametro.TIPO_TESTO, economicOperator.getNOMEST());
      if (economicOperator.isSetCFIMP()) dccIMPR.addColumn("IMPR.CFIMP", JdbcParametro.TIPO_TESTO, economicOperator.getCFIMP());
      if (economicOperator.isSetPIVIMP()) dccIMPR.addColumn("IMPR.PIVIMP", JdbcParametro.TIPO_TESTO, economicOperator.getPIVIMP());
      if (economicOperator.isSetINDIMP()) dccIMPR.addColumn("IMPR.INDIMP", JdbcParametro.TIPO_TESTO, economicOperator.getINDIMP());
      if (economicOperator.isSetNCIIMP()) dccIMPR.addColumn("IMPR.NCIIMP", JdbcParametro.TIPO_TESTO, economicOperator.getNCIIMP());
      if (economicOperator.isSetTOWN()) dccW3IMPR.addColumn("W3IMPR.TOWN", JdbcParametro.TIPO_TESTO, economicOperator.getTOWN());
      if (economicOperator.isSetNUTS()) dccW3IMPR.addColumn("W3IMPR.NUTS", JdbcParametro.TIPO_TESTO, economicOperator.getNUTS());
      if (economicOperator.isSetCAPIMP()) dccIMPR.addColumn("IMPR.CAPIMP", JdbcParametro.TIPO_TESTO, economicOperator.getCAPIMP());
      if (economicOperator.isSetNAZIMP())
        dccIMPR.addColumn("IMPR.NAZIMP", JdbcParametro.TIPO_NUMERICO, new Long(economicOperator.getNAZIMP()));
      if (economicOperator.isSetTELIMP()) dccIMPR.addColumn("IMPR.TELIMP", JdbcParametro.TIPO_TESTO, economicOperator.getTELIMP());
      if (economicOperator.isSetFAXIMP()) dccIMPR.addColumn("IMPR.FAXIMP", JdbcParametro.TIPO_TESTO, economicOperator.getFAXIMP());
      if (economicOperator.isSetEMAIIP()) dccIMPR.addColumn("IMPR.EMAIIP", JdbcParametro.TIPO_TESTO, economicOperator.getEMAIIP());
      if (economicOperator.isSetINDWEB()) dccIMPR.addColumn("IMPR.INDWEB", JdbcParametro.TIPO_TESTO, economicOperator.getINDWEB());

      dccIMPR.insert("IMPR", this.sqlManager);
      dccW3IMPR.insert("W3IMPR", this.sqlManager);

      // Gestione delle protezioni
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        this.w3Manager.gestioneProtezioneArchivi(syscon, "IMPR", codimp);
      }

      // Gestione dei diritti
      this.inserisciPermessi(syscon, "IMPR", "CODIMP", codimp);

    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.gestioneIMPR: fine metodo");

    return codimp;

  }

  /**
   * Gestione uffici.
   * 
   * @param authority
   * @param account
   * @return
   * @throws GestoreException
   * @throws SQLException
   */
  private String gestioneUFFINT(AuthorityType authority, Account account) throws GestoreException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.gestioneUFFINT: inizio metodo");

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);

    boolean authorityExist = false;
    String codein = null;
    String cfein = null;
    String ivaein = null;

    String nomein = authority.getNOMEIN();
    if (authority.isSetCFEIN()) cfein = authority.getCFEIN();
    if (authority.isSetIVAEIN()) ivaein = authority.getIVAEIN();

    Long syscon = new Long(account.getIdAccount());
    String sysab3 = account.getAbilitazioneStd();

    if (cfein != null) {
      String selectUFFINT = "select codein from uffint where cfein = ? and nomein = ? ";
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        String filtroPermessi = this.getFiltroPermessi(syscon, sysab3, "UFFINT", "CODEIN", sqlManager);
        if (filtroPermessi != null) {
          selectUFFINT += " and " + filtroPermessi;
        }
      }
      codein = (String) sqlManager.getObject(selectUFFINT, new Object[] { cfein, nomein });
    } else if (ivaein != null) {
      String selectUFFINT = "select codein from uffint where ivaein = ? and nomein = ? ";
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        String filtroPermessi = this.getFiltroPermessi(syscon, sysab3, "UFFINT", "CODEIN", sqlManager);
        if (filtroPermessi != null) {
          selectUFFINT += " and " + filtroPermessi;
        }
      }
      codein = (String) sqlManager.getObject(selectUFFINT, new Object[] { ivaein, nomein });
    }

    if (codein != null) authorityExist = true;

    if (!authorityExist) {
      codein = this.geneManager.calcolaCodificaAutomatica("UFFINT", "CODEIN");

      DataColumn[] dcUFFINT = new DataColumn[] { new DataColumn("UFFINT.CODEIN", new JdbcParametro(JdbcParametro.TIPO_TESTO, codein)) };
      DataColumnContainer dccUFFINT = new DataColumnContainer(dcUFFINT);
      DataColumn[] dcW3UFFINT = new DataColumn[] { new DataColumn("W3UFFINT.CODEIN", new JdbcParametro(JdbcParametro.TIPO_TESTO, codein)) };
      DataColumnContainer dccW3UFFINT = new DataColumnContainer(dcW3UFFINT);

      dccUFFINT.addColumn("UFFINT.NOMEIN", JdbcParametro.TIPO_TESTO, authority.getNOMEIN());
      if (authority.isSetCFEIN()) dccUFFINT.addColumn("UFFINT.CFEIN", JdbcParametro.TIPO_TESTO, authority.getCFEIN());
      if (authority.isSetIVAEIN()) dccUFFINT.addColumn("UFFINT.IVAEIN", JdbcParametro.TIPO_TESTO, authority.getIVAEIN());
      if (authority.isSetVIAEIN()) dccUFFINT.addColumn("UFFINT.VIAEIN", JdbcParametro.TIPO_TESTO, authority.getVIAEIN());
      if (authority.isSetNCIEIN()) dccUFFINT.addColumn("UFFINT.NCIEIN", JdbcParametro.TIPO_TESTO, authority.getNCIEIN());
      if (authority.isSetTOWN()) dccW3UFFINT.addColumn("W3UFFINT.TOWN", JdbcParametro.TIPO_TESTO, authority.getTOWN());
      if (authority.isSetNUTS()) dccW3UFFINT.addColumn("W3UFFINT.NUTS", JdbcParametro.TIPO_TESTO, authority.getNUTS());
      if (authority.isSetCAPEIN()) dccUFFINT.addColumn("UFFINT.CAPEIN", JdbcParametro.TIPO_TESTO, authority.getCAPEIN());
      if (authority.isSetCODNAZ()) dccUFFINT.addColumn("UFFINT.CODNAZ", JdbcParametro.TIPO_NUMERICO, new Long(authority.getCODNAZ()));
      if (authority.isSetTELEIN()) dccUFFINT.addColumn("UFFINT.TELEIN", JdbcParametro.TIPO_TESTO, authority.getTELEIN());
      if (authority.isSetFAXEIN()) dccUFFINT.addColumn("UFFINT.FAXEIN", JdbcParametro.TIPO_TESTO, authority.getFAXEIN());
      if (authority.isSetEMAIIN()) dccUFFINT.addColumn("UFFINT.EMAIIN", JdbcParametro.TIPO_TESTO, authority.getEMAIIN());
      if (authority.isSetINDWEB()) dccUFFINT.addColumn("UFFINT.INDWEB", JdbcParametro.TIPO_TESTO, authority.getINDWEB());
      if (authority.isSetCONTACTPOINT()) dccUFFINT.addColumn("UFFINT.NOMRES", JdbcParametro.TIPO_TESTO, authority.getCONTACTPOINT());

      dccUFFINT.insert("UFFINT", this.sqlManager);
      dccW3UFFINT.insert("W3UFFINT", this.sqlManager);

      // Gestione delle protezioni
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        this.w3Manager.gestioneProtezioneArchivi(syscon, "UFFINT", codein);
      }

      // Gestione dei diritti
      this.inserisciPermessi(syscon, "UFFINT", "CODEIN", codein);

    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.gestioneUFFINT: fine metodo");

    return codein;

  }

  /**
   * Gestione archivio W3AMMI per le amministrazioni aggiudicatrici
   * 
   * @param authority
   * @return
   * @throws GestoreException
   * @throws SQLException
   */
  private String gestioneW3AMMI(AuthorityAType authorityA, Account account) throws GestoreException, SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.gestisciW3AMMI: inizio metodo");

    String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);

    String codamm = null;
    String cfein = null;
    String ivaein = null;
    String nomein = authorityA.getNOMEIN();

    if (authorityA.isSetCFEIN()) cfein = authorityA.getCFEIN();
    if (authorityA.isSetIVAEIN()) ivaein = authorityA.getIVAEIN();

    Long syscon = new Long(account.getIdAccount());
    String sysab3 = account.getAbilitazioneStd();

    if (cfein != null) {
      String selectW3AMMI = "select codamm from w3ammi, uffint where w3ammi.codein = uffint.codein and uffint.cfein = ?";
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        String filtroPermessi = this.getFiltroPermessi(syscon, sysab3, "UFFINT", "CODEIN", sqlManager);
        if (filtroPermessi != null) {
          selectW3AMMI += " and " + filtroPermessi;
        }
      }
      codamm = (String) sqlManager.getObject(selectW3AMMI, new Object[] { cfein });
    } else if (ivaein != null) {
      String selectW3AMMI = "select codamm from w3ammi, uffint where w3ammi.codein = uffint.codein and uffint.ivaein = ?";
      if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
        String filtroPermessi = this.getFiltroPermessi(syscon, sysab3, "UFFINT", "CODEIN", sqlManager);
        if (filtroPermessi != null) {
          selectW3AMMI += " and " + filtroPermessi;
        }
      }
      codamm = (String) sqlManager.getObject(selectW3AMMI, new Object[] { ivaein });
    }

    if (codamm == null) {
      throw new Exception(UtilityTags.getResource("eldasoftsimapws.amministrazionenondefinita", new String[] { nomein }, false));
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.gestisciW3AMMI: fine metodo");

    return codamm;

  }

  /**
   * Procedura di autenticazione. Restituisce account.
   * 
   * @param login
   * @param password
   * @return
   * @throws Exception
   */
  private Account getAccount(String login, String password) throws Exception {

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
        throw new Exception(UtilityTags.getResource("errors.login.unknown", new String[] { login }, false));
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

    return account;

  }

  /**
   * Inserimento avviso di aggiudicazione
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciAvvisoAggiudicazione(String login, String password, String datiXML) throws XmlException, GestoreException,
      SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoAggiudicazione: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    AvvisoAggiudicazioneDocument avvisoAggiudicazioneDocument = AvvisoAggiudicazioneDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = avvisoAggiudicazioneDocument.validate(validationOptions);

    if (isValid) {
      AvvisoAggiudicazioneType avvisoAggiudicazione = avvisoAggiudicazioneDocument.getAvvisoAggiudicazione();

      // Lettura UUID e controllo univocita'
      String uuid = avvisoAggiudicazione.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS3 = new DataColumn[] { new DataColumn("W3FS3.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS3 = new DataColumnContainer(dcW3FS3);

      // Identificativo gara SIMOG
      if (avvisoAggiudicazione.isSetIDGARA()) {
        String idgara = avvisoAggiudicazione.getIDGARA();
        dccW3FS3.addColumn("W3FS3.IDGARA", JdbcParametro.TIPO_TESTO, idgara);
      }

      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS3");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);
      dccW3SIMAP.addColumn("W3SIMAP.LEGAL_BASIS", JdbcParametro.TIPO_TESTO, "32014L0024");

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = avvisoAggiudicazione.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // SEZIONE II - OGGETTO
      // Titolo
      dccW3FS3.addColumn("W3FS3.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getTITLECONTRACT());

      // CPV
      if (avvisoAggiudicazione.isSetCPV()) {
        dccW3FS3.addColumn("W3FS3.CPV", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getCPV());
      }

      // Tipo di appalto
      dccW3FS3.addColumn("W3FS3.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getTYPECONTRACT().toString());

      // Breve descrizione
      if (avvisoAggiudicazione.isSetSHORTDESCRIPTION()) {
        dccW3FS3.addColumn("W3FS3.SHORT_CONTRACT_DESCRIPTION", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getSHORTDESCRIPTION());
      }

      // Lotto/i
      if (avvisoAggiudicazione.isSetDIVINTOLOTSNO() && avvisoAggiudicazione.isSetLOT()) {
        // Bando di gara non suddiviso in lotti
        dccW3FS3.addColumn("W3FS3.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "2");

        // Lotto
        AvvisoAggiudicazioneLottoType lot = avvisoAggiudicazione.getLOT();
        this.inserisciW3ANNEXBAvvisoAggiudicazione(id, new Long(1), lot);
      } else if (avvisoAggiudicazione.isSetDIVINTOLOTSYES()) {
        // Bando di gara suddiviso in lotti
        dccW3FS3.addColumn("W3FS3.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "1");

        // Lotti
        AvvisoAggiudicazioneLottoType[] lots = avvisoAggiudicazione.getLOTSArray();
        for (int lo = 0; lo < lots.length; lo++) {
          AvvisoAggiudicazioneLottoType lotto = lots[lo];
          this.inserisciW3ANNEXBAvvisoAggiudicazione(id, new Long(lo + 1), lotto);
        }
      }

      // Valore finale
      if (avvisoAggiudicazione.isSetSCOPECOST()) {
        dccW3FS3.addColumn("W3FS3.SCOPE_COST", JdbcParametro.TIPO_DECIMALE, new Double(avvisoAggiudicazione.getSCOPECOST()));
      }

      // Offerta piu' bassa
      if (avvisoAggiudicazione.isSetSCOPELOW()) {
        dccW3FS3.addColumn("W3FS3.SCOPE_LOW", JdbcParametro.TIPO_DECIMALE, new Double(avvisoAggiudicazione.getSCOPELOW()));
      }

      // Offerta piu' alta
      if (avvisoAggiudicazione.isSetSCOPEHIGH()) {
        dccW3FS3.addColumn("W3FS3.SCOPE_HIGH", JdbcParametro.TIPO_DECIMALE, new Double(avvisoAggiudicazione.getSCOPEHIGH()));
      }

      // SEZIONE IV - PROCEDURA
      // Tipo di procedura
      if (avvisoAggiudicazione.isSetTYPEPROCEDURE()) {
        dccW3FS3.addColumn("W3FS3.TYPE_PROCEDURE", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getTYPEPROCEDURE().toString());
      }

      // Motivazioni per la procedura accelerata
      if (avvisoAggiudicazione.isSetACCELERATED()) {
        dccW3FS3.addColumn("W3FS3.ACCELERATED", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getACCELERATED());
      }

      // L'appalto e' disciplinato dall'accordo sugli appalti pubblici ?
      if (avvisoAggiudicazione.isSetGPA()) {
        dccW3FS3.addColumn("W3FS3.CONTRACT_COVERED_GPA", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getGPA().toString());
      }

      // Accordo quadro
      if (avvisoAggiudicazione.isSetFRAMEWORK()) {
        dccW3FS3.addColumn("W3FS3.FRAMEWORK", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getFRAMEWORK().toString());
      }

      // Asta elettronica ?
      if (avvisoAggiudicazione.isSetISELECTRONIC()) {
        dccW3FS3.addColumn("W3FS3.IS_ELECTRONIC", JdbcParametro.TIPO_TESTO, avvisoAggiudicazione.getISELECTRONIC().toString());
      }

      // SEZIONE VI - ALTRE INFORMAZIONI
      // Organismo responsabile delle procedure di ricorso
      if (avvisoAggiudicazione.isSetAPPEALPROCEDURE()) {
        String appeal_procedure_codein = this.gestioneUFFINT(avvisoAggiudicazione.getAPPEALPROCEDURE(), account);
        dccW3FS3.addColumn("W3FS3.APPEAL_PROCEDURE_CODEIN", JdbcParametro.TIPO_TESTO, appeal_procedure_codein);
      }

      // Gestione dei lotti aggiudicati
      if (avvisoAggiudicazione.getLOTTIAGGIUDICATIArray().length > 0) {
        for (int j = 0; j < avvisoAggiudicazione.getLOTTIAGGIUDICATIArray().length; j++) {
          DataColumn[] dcW3FS3AWARD = new DataColumn[] { new DataColumn("W3FS3AWARD.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
          LottoAggiudicatoType lottoAggiudicato = avvisoAggiudicazione.getLOTTIAGGIUDICATIArray(j);
          DataColumnContainer dccW3FS3AWARD = new DataColumnContainer(dcW3FS3AWARD);

          Long numeroLottoAggiudicato = new Long(j + 1);

          dccW3FS3AWARD.addColumn("W3FS3AWARD.ITEM", JdbcParametro.TIPO_NUMERICO, numeroLottoAggiudicato);

          // Numero del lotto
          if (lottoAggiudicato.isSetLOTNO()) {
            dccW3FS3AWARD.addColumn("W3FS3AWARD.LOT_NUMBER", JdbcParametro.TIPO_NUMERICO, new Long(lottoAggiudicato.getLOTNO()));
          }

          // Titolo del lotto
          dccW3FS3AWARD.addColumn("W3FS3AWARD.CONTRACT_TITLE", JdbcParametro.TIPO_TESTO, lottoAggiudicato.getTITLE());

          // Il contratto d'appalto/lotto e' stato aggiudicato ?
          if (lottoAggiudicato.isSetAWARDEDNO()) {
            dccW3FS3AWARD.addColumn("W3FS3AWARD.AWARDED", JdbcParametro.TIPO_TESTO, "2");
            if (lottoAggiudicato.isSetNOAWARDEDTYPE()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.NO_AWARDED_TYPE", JdbcParametro.TIPO_TESTO,
                  lottoAggiudicato.getNOAWARDEDTYPE().toString());
            }
          } else if (lottoAggiudicato.isSetAWARDEDYES()) {
            dccW3FS3AWARD.addColumn("W3FS3AWARD.AWARDED", JdbcParametro.TIPO_TESTO, "1");

            // Data di aggiudicazione
            if (lottoAggiudicato.isSetCONTRACTAWARDDATE()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.CONTRACT_AWARD_DATE", JdbcParametro.TIPO_DATA,
                  lottoAggiudicato.getCONTRACTAWARDDATE().getTime());
            }

            // Offerte ricevute
            if (lottoAggiudicato.isSetOFFERSRECEIVED()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.OFFERS_RECEIVED", JdbcParametro.TIPO_NUMERICO,
                  new Long(lottoAggiudicato.getOFFERSRECEIVED()));
            }

            // L'appalto e' stato aggiudicato a un raggruppamento di operatori
            // economici ?
            if (lottoAggiudicato.isSetAWARDEDGROUP()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.AWARDED_GROUP", JdbcParametro.TIPO_TESTO, lottoAggiudicato.getAWARDEDGROUP().toString());
            }

            // Valore totale del contratto d'appalto
            if (lottoAggiudicato.isSetFINALCOST()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.FINAL_COST", JdbcParametro.TIPO_DECIMALE, new Double(lottoAggiudicato.getFINALCOST()));
            }

            // Offerta piu' bassa
            if (lottoAggiudicato.isSetFINALLOW()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.FINAL_LOW", JdbcParametro.TIPO_DECIMALE, new Double(lottoAggiudicato.getFINALLOW()));
            }

            // Offerta piu' alta
            if (lottoAggiudicato.isSetFINALHIGH()) {
              dccW3FS3AWARD.addColumn("W3FS3AWARD.FINAL_HIGH", JdbcParametro.TIPO_DECIMALE, new Double(lottoAggiudicato.getFINALHIGH()));
            }

            // Operatore economico
            EconomicOperatorType[] economicOperatorsArray = lottoAggiudicato.getECONOMICOPERATORSArray();
            for (int ec = 0; ec < economicOperatorsArray.length; ec++) {
              EconomicOperatorType economicOperator = lottoAggiudicato.getECONOMICOPERATORSArray(ec);
              String codimp = this.gestioneIMPR(economicOperator, account);
              if (codimp != null) {
                DataColumn[] dcW3FS3AWARD_C = new DataColumn[] { new DataColumn("W3FS3AWARD_C.ID", new JdbcParametro(
                    JdbcParametro.TIPO_NUMERICO, id)) };
                DataColumnContainer dccW3FS3AWARD_C = new DataColumnContainer(dcW3FS3AWARD_C);
                dccW3FS3AWARD_C.addColumn("W3FS3AWARD_C.ITEM", JdbcParametro.TIPO_NUMERICO, numeroLottoAggiudicato);
                dccW3FS3AWARD_C.addColumn("W3FS3AWARD_C.NUM", JdbcParametro.TIPO_NUMERICO, new Long(ec + 1));
                dccW3FS3AWARD_C.addColumn("W3FS3AWARD_C.CONTRACTOR_CODIMP", JdbcParametro.TIPO_TESTO, codimp);
                if (economicOperator.isSetSME()) {
                  dccW3FS3AWARD_C.addColumn("W3FS3AWARD_C.CONTRACTOR_SME", JdbcParametro.TIPO_TESTO, economicOperator.getSME().toString());
                }
                dccW3FS3AWARD_C.insert("W3FS3AWARD_C", this.sqlManager);
              }
            }
          }
          dccW3FS3AWARD.insert("W3FS3AWARD", this.sqlManager);
        }
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS3.insert("W3FS3", this.sqlManager);

      // Gestione dei diritti
      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoAggiudicazione: fine metodo");

  }

  /**
   * Inserimento avviso di aggiudicazione per i settori speciali.
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciAvvisoAggiudicazioneSettoriSpeciali(String login, String password, String datiXML) throws XmlException,
      GestoreException, SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoAggiudicazioneSettoriSpeciali: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    AvvisoAggiudicazioneSettoriSpecialiDocument avvisoAggiudicazioneSettoriSpecialiDocument = AvvisoAggiudicazioneSettoriSpecialiDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = avvisoAggiudicazioneSettoriSpecialiDocument.validate(validationOptions);

    if (isValid) {
      AvvisoAggiudicazioneSettoriSpecialiType avvisoAggiudicazioneSettoriSpeciali = avvisoAggiudicazioneSettoriSpecialiDocument.getAvvisoAggiudicazioneSettoriSpeciali();

      // Lettura UUID e controllo univocita'
      String uuid = avvisoAggiudicazioneSettoriSpeciali.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS6 = new DataColumn[] { new DataColumn("W3FS6.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS6 = new DataColumnContainer(dcW3FS6);

      // Identificativo gara SIMOG
        if (avvisoAggiudicazioneSettoriSpeciali.isSetIDGARA()) {
        String idgara = avvisoAggiudicazioneSettoriSpeciali.getIDGARA();
        dccW3FS6.addColumn("W3FS6.IDGARA", JdbcParametro.TIPO_TESTO, idgara);
      }

      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS6");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);
      dccW3SIMAP.addColumn("W3SIMAP.LEGAL_BASIS", JdbcParametro.TIPO_TESTO, "32014L0025");

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = avvisoAggiudicazioneSettoriSpeciali.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // SEZIONE II - OGGETTO
      // Titolo
      dccW3FS6.addColumn("W3FS6.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getTITLECONTRACT());

      // CPV
      if (avvisoAggiudicazioneSettoriSpeciali.isSetCPV()) {
        dccW3FS6.addColumn("W3FS6.CPV", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getCPV());
      }

      // Tipo di appalto
      dccW3FS6.addColumn("W3FS6.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getTYPECONTRACT().toString());

      // Breve descrizione
      if (avvisoAggiudicazioneSettoriSpeciali.isSetSHORTDESCRIPTION()) {
        dccW3FS6.addColumn("W3FS6.SHORT_CONTRACT_DESCRIPTION", JdbcParametro.TIPO_TESTO,
            avvisoAggiudicazioneSettoriSpeciali.getSHORTDESCRIPTION());
      }

      // Lotto/i
      if (avvisoAggiudicazioneSettoriSpeciali.isSetDIVINTOLOTSNO() && avvisoAggiudicazioneSettoriSpeciali.isSetLOT()) {
        // Bando di gara non suddiviso in lotti
        dccW3FS6.addColumn("W3FS6.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "2");

        // Lotto
        AvvisoAggiudicazioneLottoType lot = avvisoAggiudicazioneSettoriSpeciali.getLOT();
        this.inserisciW3ANNEXBAvvisoAggiudicazione(id, new Long(1), lot);
      } else if (avvisoAggiudicazioneSettoriSpeciali.isSetDIVINTOLOTSYES()) {
        // Bando di gara suddiviso in lotti
        dccW3FS6.addColumn("W3FS6.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "1");

        // Lotti
        AvvisoAggiudicazioneLottoType[] lots = avvisoAggiudicazioneSettoriSpeciali.getLOTSArray();
        for (int lo = 0; lo < lots.length; lo++) {
          AvvisoAggiudicazioneLottoType lotto = lots[lo];
          this.inserisciW3ANNEXBAvvisoAggiudicazione(id, new Long(lo + 1), lotto);
        }
      }

      // Consentire la pubblicazione del valore totale dell'appalto
      if (avvisoAggiudicazioneSettoriSpeciali.isSetAGREETOPUBLISH()) {
        dccW3FS6.addColumn("W3FS6.AGREE_TO_PUBLISH", JdbcParametro.TIPO_TESTO,
            avvisoAggiudicazioneSettoriSpeciali.getAGREETOPUBLISH().toString());
      }

      // Valore finale
      if (avvisoAggiudicazioneSettoriSpeciali.isSetSCOPECOST()) {
        dccW3FS6.addColumn("W3FS6.SCOPE_COST", JdbcParametro.TIPO_DECIMALE, new Double(avvisoAggiudicazioneSettoriSpeciali.getSCOPECOST()));
      }

      // Offerta piu' bassa
      if (avvisoAggiudicazioneSettoriSpeciali.isSetSCOPELOW()) {
        dccW3FS6.addColumn("W3FS6.SCOPE_LOW", JdbcParametro.TIPO_DECIMALE, new Double(avvisoAggiudicazioneSettoriSpeciali.getSCOPELOW()));
      }

      // Offerta piu' alta
      if (avvisoAggiudicazioneSettoriSpeciali.isSetSCOPEHIGH()) {
        dccW3FS6.addColumn("W3FS6.SCOPE_HIGH", JdbcParametro.TIPO_DECIMALE, new Double(avvisoAggiudicazioneSettoriSpeciali.getSCOPEHIGH()));
      }

      // SEZIONE IV - PROCEDURA
      // Tipo di procedura
      if (avvisoAggiudicazioneSettoriSpeciali.isSetTYPEPROCEDURE()) {
        dccW3FS6.addColumn("W3FS6.TYPE_PROCEDURE", JdbcParametro.TIPO_TESTO,
            avvisoAggiudicazioneSettoriSpeciali.getTYPEPROCEDURE().toString());
      }

      // Motivazioni per la procedura accelerata
      if (avvisoAggiudicazioneSettoriSpeciali.isSetACCELERATED()) {
        dccW3FS6.addColumn("W3FS6.ACCELERATED", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getACCELERATED());
      }

      // L'appalto e' disciplinato dall'accordo sugli appalti pubblici ?
      if (avvisoAggiudicazioneSettoriSpeciali.isSetGPA()) {
        dccW3FS6.addColumn("W3FS6.CONTRACT_COVERED_GPA", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getGPA().toString());
      }

      // Accordo quadro
      if (avvisoAggiudicazioneSettoriSpeciali.isSetFRAMEWORK()) {
        dccW3FS6.addColumn("W3FS6.FRAMEWORK", JdbcParametro.TIPO_TESTO, avvisoAggiudicazioneSettoriSpeciali.getFRAMEWORK().toString());
      }

      // Asta elettronica ?
      if (avvisoAggiudicazioneSettoriSpeciali.isSetISELECTRONIC()) {
        dccW3FS6.addColumn("W3FS6.IS_ELECTRONIC", JdbcParametro.TIPO_TESTO,
            avvisoAggiudicazioneSettoriSpeciali.getISELECTRONIC().toString());
      }

      // SEZIONE VI - ALTRE INFORMAZIONI
      // Organismo responsabile delle procedure di ricorso
      if (avvisoAggiudicazioneSettoriSpeciali.isSetAPPEALPROCEDURE()) {
        String appeal_procedure_codein = this.gestioneUFFINT(avvisoAggiudicazioneSettoriSpeciali.getAPPEALPROCEDURE(), account);
        dccW3FS6.addColumn("W3FS6.APPEAL_PROCEDURE_CODEIN", JdbcParametro.TIPO_TESTO, appeal_procedure_codein);
      }

      // Gestione dei lotti aggiudicati
      if (avvisoAggiudicazioneSettoriSpeciali.getLOTTIAGGIUDICATIArray().length > 0) {
        for (int j = 0; j < avvisoAggiudicazioneSettoriSpeciali.getLOTTIAGGIUDICATIArray().length; j++) {
          DataColumn[] dcW3FS6AWARD = new DataColumn[] { new DataColumn("W3FS6AWARD.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
          LottoAggiudicatoType lottoAggiudicato = avvisoAggiudicazioneSettoriSpeciali.getLOTTIAGGIUDICATIArray(j);
          DataColumnContainer dccW3FS6AWARD = new DataColumnContainer(dcW3FS6AWARD);

          Long numeroLottoAggiudicato = new Long(j + 1);

          dccW3FS6AWARD.addColumn("W3FS6AWARD.ITEM", JdbcParametro.TIPO_NUMERICO, numeroLottoAggiudicato);

          // Numero del lotto
          if (lottoAggiudicato.isSetLOTNO()) {
            dccW3FS6AWARD.addColumn("W3FS6AWARD.LOT_NUMBER", JdbcParametro.TIPO_NUMERICO, new Long(lottoAggiudicato.getLOTNO()));
          }

          // Titolo del lotto
          dccW3FS6AWARD.addColumn("W3FS6AWARD.CONTRACT_TITLE", JdbcParametro.TIPO_TESTO, lottoAggiudicato.getTITLE());

          // Il contratto d'appalto/lotto e' stato aggiudicato ?
          if (lottoAggiudicato.isSetAWARDEDNO()) {
            dccW3FS6AWARD.addColumn("W3FS6AWARD.AWARDED", JdbcParametro.TIPO_TESTO, "2");
            if (lottoAggiudicato.isSetNOAWARDEDTYPE()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.NO_AWARDED_TYPE", JdbcParametro.TIPO_TESTO,
                  lottoAggiudicato.getNOAWARDEDTYPE().toString());
            }
          } else if (lottoAggiudicato.isSetAWARDEDYES()) {
            dccW3FS6AWARD.addColumn("W3FS6AWARD.AWARDED", JdbcParametro.TIPO_TESTO, "1");

            // Data di aggiudicazione
            if (lottoAggiudicato.isSetCONTRACTAWARDDATE()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.CONTRACT_AWARD_DATE", JdbcParametro.TIPO_DATA,
                  lottoAggiudicato.getCONTRACTAWARDDATE().getTime());
            }

            // Consentire la pubblicazione delle informazioni sulle offerte
            if (lottoAggiudicato.isSetAGREETOPUBLISHTENDERS()) {
              dccW3FS6.addColumn("W3FS6AWARD.AGREE_TO_PUBLISH_TENDERS", JdbcParametro.TIPO_TESTO,
                  lottoAggiudicato.getAGREETOPUBLISHTENDERS().toString());
            }

            // Offerte ricevute
            if (lottoAggiudicato.isSetOFFERSRECEIVED()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.OFFERS_RECEIVED", JdbcParametro.TIPO_NUMERICO,
                  new Long(lottoAggiudicato.getOFFERSRECEIVED()));
            }

            // L'appalto e' stato aggiudicato a un raggruppamento di operatori
            // economici ?
            if (lottoAggiudicato.isSetAWARDEDGROUP()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.AWARDED_GROUP", JdbcParametro.TIPO_TESTO, lottoAggiudicato.getAWARDEDGROUP().toString());
            }

            // Consentire la pubblicazione degli importi dell'appalto
            if (lottoAggiudicato.isSetAGREETOPUBLISHVALUE()) {
              dccW3FS6.addColumn("W3FS6AWARD.AGREE_TO_PUBLISH_VALUE", JdbcParametro.TIPO_TESTO,
                  lottoAggiudicato.getAGREETOPUBLISHVALUE().toString());
            }

            // Valore totale del contratto d'appalto
            if (lottoAggiudicato.isSetFINALCOST()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.FINAL_COST", JdbcParametro.TIPO_DECIMALE, new Double(lottoAggiudicato.getFINALCOST()));
            }

            // Offerta piu' bassa
            if (lottoAggiudicato.isSetFINALLOW()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.FINAL_LOW", JdbcParametro.TIPO_DECIMALE, new Double(lottoAggiudicato.getFINALLOW()));
            }

            // Offerta piu' alta
            if (lottoAggiudicato.isSetFINALHIGH()) {
              dccW3FS6AWARD.addColumn("W3FS6AWARD.FINAL_HIGH", JdbcParametro.TIPO_DECIMALE, new Double(lottoAggiudicato.getFINALHIGH()));
            }

            // Consentire la pubblicazione delle informazioni sui contraenti
            if (lottoAggiudicato.isSetAGREETOPUBLISHCONTRACTOR()) {
              dccW3FS6.addColumn("W3FS6AWARD.AGREE_TO_PUBLISH_CONTRACTOR", JdbcParametro.TIPO_TESTO,
                  lottoAggiudicato.getAGREETOPUBLISHCONTRACTOR().toString());
            }

            // Sono state escluse offerte perche' anormalmente basse
            if (lottoAggiudicato.isSetTENDERSEXCLUDED()) {
              dccW3FS6.addColumn("W3FS6AWARD.TENDERS_EXCLUDED", JdbcParametro.TIPO_TESTO, lottoAggiudicato.getTENDERSEXCLUDED().toString());
            }

            // Operatore economico
            EconomicOperatorType[] economicOperatorsArray = lottoAggiudicato.getECONOMICOPERATORSArray();
            for (int ec = 0; ec < economicOperatorsArray.length; ec++) {
              EconomicOperatorType economicOperator = lottoAggiudicato.getECONOMICOPERATORSArray(ec);
              String codimp = this.gestioneIMPR(economicOperator, account);
              if (codimp != null) {
                DataColumn[] dcW3FS6AWARD_C = new DataColumn[] { new DataColumn("W3FS6AWARD_C.ID", new JdbcParametro(
                    JdbcParametro.TIPO_NUMERICO, id)) };
                DataColumnContainer dccW3FS6AWARD_C = new DataColumnContainer(dcW3FS6AWARD_C);
                dccW3FS6AWARD_C.addColumn("W3FS6AWARD_C.ITEM", JdbcParametro.TIPO_NUMERICO, numeroLottoAggiudicato);
                dccW3FS6AWARD_C.addColumn("W3FS6AWARD_C.NUM", JdbcParametro.TIPO_NUMERICO, new Long(ec + 1));
                dccW3FS6AWARD_C.addColumn("W3FS6AWARD_C.CONTRACTOR_CODIMP", JdbcParametro.TIPO_TESTO, codimp);
                if (economicOperator.isSetSME()) {
                  dccW3FS6AWARD_C.addColumn("W3FS6AWARD_C.CONTRACTOR_SME", JdbcParametro.TIPO_TESTO, economicOperator.getSME().toString());
                }
                dccW3FS6AWARD_C.insert("W3FS6AWARD_C", this.sqlManager);
              }
            }
          }
          dccW3FS6AWARD.insert("W3FS6AWARD", this.sqlManager);
        }
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS6.insert("W3FS6", this.sqlManager);

      // Gestione dei diritti
      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoAggiudicazioneSettoriSpeciali: fine metodo");

  }

  /**
   * Inserimento diritto di controllo completo.
   * 
   * @param idAccount
   * @param id
   * @throws SQLException
   * @throws GestoreException
   */
  private void inserisciPermessi(Long idAccount, String tblname, String clm1name, Object clm1value) throws SQLException, GestoreException {

    Long nextIdPerm = (Long) sqlManager.getObject("select max(idperm) from perm", new Object[] {});
    if (nextIdPerm == null) nextIdPerm = new Long(0);
    nextIdPerm = new Long(nextIdPerm.longValue() + 1);
    DataColumn[] dcPERM = new DataColumn[] { new DataColumn("PERM.IDPERM", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, nextIdPerm)) };
    DataColumnContainer dccPERM = new DataColumnContainer(dcPERM);
    dccPERM.addColumn("PERM.GUPERM", JdbcParametro.TIPO_TESTO, "USR");
    dccPERM.addColumn("PERM.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
    dccPERM.addColumn("PERM.R", JdbcParametro.TIPO_NUMERICO, new Long(1));
    dccPERM.addColumn("PERM.W", JdbcParametro.TIPO_NUMERICO, new Long(1));
    dccPERM.addColumn("PERM.X", JdbcParametro.TIPO_NUMERICO, new Long(1));
    dccPERM.addColumn("PERM.TBLNAME", JdbcParametro.TIPO_TESTO, tblname);
    dccPERM.addColumn("PERM.CLM1NAME", JdbcParametro.TIPO_TESTO, clm1name);

    if (clm1value instanceof String) {
      dccPERM.addColumn("PERM.CLM1VALUE_VC", JdbcParametro.TIPO_TESTO, clm1value);
    } else if (clm1value instanceof Long) {
      dccPERM.addColumn("PERM.CLM1VALUE_NU", JdbcParametro.TIPO_NUMERICO, clm1value);
    }
    dccPERM.addColumn("PERM.RWX", JdbcParametro.TIPO_NUMERICO, new Long(7));
    dccPERM.insert("PERM", this.sqlManager);
  }

  /**
   * Inserimento avviso sul profilo di committente
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciAvvisoProfiloCommittente(String login, String password, String datiXML) throws XmlException, GestoreException,
      SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoProfiloCommittente: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    AvvisoProfiloCommittenteDocument avvisoProfiloCommittenteDocument = AvvisoProfiloCommittenteDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = avvisoProfiloCommittenteDocument.validate(validationOptions);

    if (isValid) {

      AvvisoProfiloCommittenteType avvisoProfiloCommittente = avvisoProfiloCommittenteDocument.getAvvisoProfiloCommittente();

      // Lettura UUID e controllo univocita'
      String uuid = avvisoProfiloCommittente.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS8 = new DataColumn[] { new DataColumn("W3FS8.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS8 = new DataColumnContainer(dcW3FS8);

      DataColumn[] dcW3FS8S2 = new DataColumn[] { new DataColumn("W3FS8S2.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS8S2 = new DataColumnContainer(dcW3FS8S2);
      dccW3FS8S2.addColumn("W3FS8S2.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));

      DataColumn[] dcW3ANNEXB = new DataColumn[] { new DataColumn("W3ANNEXB.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3ANNEXB = new DataColumnContainer(dcW3ANNEXB);
      dccW3ANNEXB.addColumn("W3ANNEXB.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));

      // Tipo di avviso
      if (avvisoProfiloCommittente.isSetNOTICERELATION()) {
        dccW3FS8.addColumn("W3FS8.NOTICE_RELATION", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getNOTICERELATION().toString());
      } else {
        dccW3FS8.addColumn("W3FS8.NOTICE_RELATION", JdbcParametro.TIPO_TESTO, "3");
      }

      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS8");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = avvisoProfiloCommittente.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // Ulteriori informazioni
      if (avvisoProfiloCommittente.isSetFURTHERINFOIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "1");
      } else if (avvisoProfiloCommittente.isSetFURTHERINFOADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "2");
        if (avvisoProfiloCommittente.isSetFURTHERINFOBODY()) {
          String further_info_codein = this.gestioneUFFINT(avvisoProfiloCommittente.getFURTHERINFOBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO_CODEIN", JdbcParametro.TIPO_TESTO, further_info_codein);
        }
      }

      // SEZIONE II: OGGETTO
      // Denominazione dell'appalto
      dccW3FS8S2.addColumn("W3FS8S2.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getTITLECONTRACT());

      // CPV
      if (avvisoProfiloCommittente.isSetCPV()) {
        dccW3FS8S2.addColumn("W3FS8S2.CPV", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getCPV());
      }

      // Tipo di contratto
      dccW3FS8S2.addColumn("W3FS8S2.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getTYPECONTRACT().toString());

      // NUTS
      if (avvisoProfiloCommittente.isSetNUTS()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.SITE_NUTS", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getNUTS());
      }

      // Sito o luogo principale
      if (avvisoProfiloCommittente.isSetSITELABEL()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.SITE_LABEL", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getSITELABEL());
      }

      // Descrizione
      if (avvisoProfiloCommittente.isSetSHORTDESCRIPTION()) {
        dccW3ANNEXB.addColumn("W3ANNEXB.DESCRIPTION", JdbcParametro.TIPO_TESTO, avvisoProfiloCommittente.getSHORTDESCRIPTION());
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS8.insert("W3FS8", this.sqlManager);
      dccW3FS8S2.insert("W3FS8S2", this.sqlManager);
      dccW3ANNEXB.insert("W3ANNEXB", this.sqlManager);

      // Gestione dei diritti
      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoProfiloCommittente: fine metodo");

  }

  /**
   * Inserimento avviso di preinformazione (formulario FS1)
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciAvvisoPreinformazione(String login, String password, String datiXML) throws XmlException, GestoreException,
      SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoPreinformazione: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    AvvisoPreinformazioneDocument avvisoPreinformazioneDocument = AvvisoPreinformazioneDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = avvisoPreinformazioneDocument.validate(validationOptions);

    if (isValid) {

      AvvisoPreinformazioneType avvisoPreinformazione = avvisoPreinformazioneDocument.getAvvisoPreinformazione();

      // Lettura UUID e controllo univocita'
      String uuid = avvisoPreinformazione.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS1 = new DataColumn[] { new DataColumn("W3FS1.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS1 = new DataColumnContainer(dcW3FS1);
      DataColumn[] dcW3FS1S2 = new DataColumn[] { new DataColumn("W3FS1S2.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS1S2 = new DataColumnContainer(dcW3FS1S2);
      dccW3FS1S2.addColumn("W3FS1S2.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));

      // Tipo di avviso di preinformazione
      if (avvisoPreinformazione.isSetNOTICEF01()) {
        dccW3FS1.addColumn("W3FS1.NOTICE_F01", JdbcParametro.TIPO_NUMERICO, new Long(avvisoPreinformazione.getNOTICEF01().longValue()));
      }

      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS1");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);
      dccW3SIMAP.addColumn("W3SIMAP.LEGAL_BASIS", JdbcParametro.TIPO_TESTO, "32014L0024");

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = avvisoPreinformazione.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // Disponibilita' dei documenti di gara
      if (avvisoPreinformazione.isSetDOCUMENTFURE()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_FU_RE", JdbcParametro.TIPO_NUMERICO, new Long(
            avvisoPreinformazione.getDOCUMENTFURE().longValue()));
      }

      // URL dei documenti di gara
      if (avvisoPreinformazione.isSetDOCUMENTURL()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_URL", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getDOCUMENTURL());
      }

      // Ulteriori informazioni
      if (avvisoPreinformazione.isSetFURTHERINFOIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "1");
      } else if (avvisoPreinformazione.isSetFURTHERINFOADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "2");
        if (avvisoPreinformazione.isSetFURTHERINFOBODY()) {
          String further_info_codein = this.gestioneUFFINT(avvisoPreinformazione.getFURTHERINFOBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO_CODEIN", JdbcParametro.TIPO_TESTO, further_info_codein);
        }
      }

      // Offerte e domande di partecipazione
      if (avvisoPreinformazione.isSetPARTECIPATIONIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "1");
      } else if (avvisoPreinformazione.isSetPARTECIPATIONADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "2");
        if (avvisoPreinformazione.isSetPARTECIPATIONBODY()) {
          String participarion_codein = this.gestioneUFFINT(avvisoPreinformazione.getPARTECIPATIONBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION_CODEIN", JdbcParametro.TIPO_TESTO, participarion_codein);
        }
      }

      // SEZIONE II - OGGETTO
      // Titolo
      dccW3FS1S2.addColumn("W3FS1S2.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getTITLECONTRACT());

      // CPV
      if (avvisoPreinformazione.isSetCPV()) {
        dccW3FS1S2.addColumn("W3FS1S2.CPV", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getCPV());
      }

      // Tipo di appalto
      dccW3FS1S2.addColumn("W3FS1S2.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getTYPECONTRACT().toString());

      // Breve descrizione
      if (avvisoPreinformazione.isSetSHORTDESCRIPTION()) {
        dccW3FS1S2.addColumn("W3FS1S2.SCOPE_TOTAL", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getSHORTDESCRIPTION());
      }

      // Valore totale stimato
      if (avvisoPreinformazione.isSetSCOPECOST()) {
        dccW3FS1S2.addColumn("W3FS1S2.SCOPE_COST", JdbcParametro.TIPO_DECIMALE, new Double(avvisoPreinformazione.getSCOPECOST()));
      }

      // Lotto/i
      if (avvisoPreinformazione.isSetDIVINTOLOTSNO() && avvisoPreinformazione.isSetLOT()) {
        // Bando di gara non suddiviso in lotti
        dccW3FS1S2.addColumn("W3FS1S2.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "2");

        // Lotto
        LottoCommonType lot = avvisoPreinformazione.getLOT();
        this.inserisciW3ANNEXB(id, new Long(1), lot);
      } else if (avvisoPreinformazione.isSetDIVINTOLOTSYES()) {
        // Bando di gara suddiviso in lotti
        dccW3FS1S2.addColumn("W3FS1S2.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "1");

        // Le offerte vanno presentate per..
        if (avvisoPreinformazione.isSetDIVLOTSVALUE()) {
          dccW3FS1S2.addColumn("W3FS1S2.DIV_LOTS", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getDIVLOTSVALUE().toString());
        }

        // Numero massimo di lotti per i quali presentare le offerte
        if (avvisoPreinformazione.isSetDIVLOTSMAX()) {
          dccW3FS1S2.addColumn("W3FS1S2.DIV_LOTS_MAX", JdbcParametro.TIPO_NUMERICO, new Long(avvisoPreinformazione.getDIVLOTSMAX()));
        }

        // Numero massimo di lotti che possono essere aggiudicati ad un
        // offerente
        if (avvisoPreinformazione.isSetLOTSMAXTENDERER()) {
          dccW3FS1S2.addColumn("W3FS1S2.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO,
              new Long(avvisoPreinformazione.getLOTSMAXTENDERER()));
        } else {
          if (avvisoPreinformazione.isSetDIVLOTSVALUE() && avvisoPreinformazione.getDIVLOTSVALUE().intValue() == 1) {
            dccW3FS1S2.addColumn("W3FS1S2.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO, new Long(1));
          }
        }

        // Lotti
        LottoCommonType[] lots = avvisoPreinformazione.getLOTSArray();
        for (int lo = 0; lo < lots.length; lo++) {
          LottoCommonType lotto = lots[lo];
          this.inserisciW3ANNEXB(id, new Long(lo + 1), lotto);
        }
      }

      // Data prevista di pubblicazione del bando di gara
      if (avvisoPreinformazione.isSetDATEPUBLI()) {
        dccW3FS1S2.addColumn("W3FS1S2.DATE_PUBLI", JdbcParametro.TIPO_DATA, avvisoPreinformazione.getDATEPUBLI().getTime());
      }

      // SEZIONE IV - PROCEDURA
      // Tipo di procedura
      if (avvisoPreinformazione.isSetTYPEPROCEDURE()) {
        dccW3FS1.addColumn("W3FS1.TYPE_PROCEDURE", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getTYPEPROCEDURE().toString());
      }

      // L'appalto e' disciplinato dall'accordo sugli appalti pubblici ?
      if (avvisoPreinformazione.isSetGPA()) {
        dccW3FS1.addColumn("W3FS1.GPA", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getGPA().toString());
      }

      // Data termine per la ricezione delle manifestazioni di interesse
      if (avvisoPreinformazione.isSetDATERECEIPT()) {
        dccW3FS1.addColumn("W3FS1.DATE_RECEIPT", JdbcParametro.TIPO_DATA, avvisoPreinformazione.getDATERECEIPT().getTime());
      }

      // Ora termine per la ricezione delle manifestazioni di interesse
      if (avvisoPreinformazione.isSetTIMERECEIPT()) {
        dccW3FS1.addColumn("W3FS1.TIME_RECEIPT", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getTIMERECEIPT());
      }

      // Accordo quadro
      if (avvisoPreinformazione.isSetFRAMEWORKNO()) {
        dccW3FS1.addColumn("W3FS1.FRAMEWORK", JdbcParametro.TIPO_TESTO, "2");
      } else if (avvisoPreinformazione.isSetFRAMEWORKYES()) {
        dccW3FS1.addColumn("W3FS1.FRAMEWORK", JdbcParametro.TIPO_TESTO, "1");
        if (avvisoPreinformazione.isSetFRAMESEVERALOPNO()) {
          dccW3FS1.addColumn("W3FS1.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "2");
        } else if (avvisoPreinformazione.isSetFRAMESEVERALOPYES()) {
          dccW3FS1.addColumn("W3FS1.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "1");
          if (avvisoPreinformazione.isSetFRAMEOPERATORSNUMBER()) {
            dccW3FS1.addColumn("W3FS1.FR_NB_PARTECIPANTS", JdbcParametro.TIPO_NUMERICO,
                new Long(avvisoPreinformazione.getFRAMEOPERATORSNUMBER()));
          }
        }
      }

      // Asta elettronica ?
      if (avvisoPreinformazione.isSetISELECTRONIC()) {
        dccW3FS1.addColumn("W3FS1.EAUCTION", JdbcParametro.TIPO_TESTO, avvisoPreinformazione.getISELECTRONIC().toString());
      }

      // SEZIONE VI - ALTRE INFORMAZIONI
      // Organismo responsabile delle procedure di ricorso
      if (avvisoPreinformazione.isSetREVIEWBODY()) {
        String appeal_procedure_codein = this.gestioneUFFINT(avvisoPreinformazione.getREVIEWBODY(), account);
        dccW3FS1.addColumn("W3FS1.REVIEW_BODY_CODEIN", JdbcParametro.TIPO_TESTO, appeal_procedure_codein);
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS1.insert("W3FS1", this.sqlManager);
      dccW3FS1S2.insert("W3FS1S2", this.sqlManager);

      // Lingue utilizzabili- default IT
      if (avvisoPreinformazione.isSetNOTICEF01()) {
        if (NOTICE_F01_PRI_CALL_COMPETITION.equals(new Long(avvisoPreinformazione.getNOTICEF01().longValue()))) {
          // Inserire la lingua
          DataColumn[] dcW3LANGUAGE = new DataColumn[] { new DataColumn("W3LANGUAGE.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
          DataColumnContainer dccW3LANGUAGE = new DataColumnContainer(dcW3LANGUAGE);
          dccW3LANGUAGE.addColumn("W3LANGUAGE.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));
          dccW3LANGUAGE.addColumn("W3LANGUAGE.LANGUAGE_EC", JdbcParametro.TIPO_TESTO, "IT");
          dccW3LANGUAGE.insert("W3LANGUAGE", this.sqlManager);
        }
      }

      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoPreinformazione: fine metodo");

  }

  /**
   * Avviso periodico indicativo (FS4).
   * 
   * @param login
   * @param password
   * @param datiXML
   * @throws XmlException
   * @throws GestoreException
   * @throws SQLException
   * @throws Exception
   */
  public void inserisciAvvisoPeriodicoIndicativo(String login, String password, String datiXML) throws XmlException, GestoreException,
      SQLException, Exception {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoPeriodicoIndicativo: inizio metodo");

    Account account = this.getAccount(login, password);
    Long idAccount = new Long(account.getIdAccount());

    AvvisoPeriodicoIndicativoDocument avvisoPeriodicoIndicativoDocument = AvvisoPeriodicoIndicativoDocument.Factory.parse(datiXML);

    ArrayList validationErrors = new ArrayList();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);
    boolean isValid = avvisoPeriodicoIndicativoDocument.validate(validationOptions);

    if (isValid) {

      AvvisoPeriodicoIndicativoType avvisoPeriodicoIndicativo = avvisoPeriodicoIndicativoDocument.getAvvisoPeriodicoIndicativo();

      // Lettura UUID e controllo univocita'
      String uuid = avvisoPeriodicoIndicativo.getUUID();
      Long cntBandoAvviso = (Long) sqlManager.getObject("select count(*) from w3simap where uuid = ?", new Object[] { uuid });
      if (cntBandoAvviso != null && cntBandoAvviso.longValue() > 0) {
        throw new Exception(UtilityTags.getResource("eldasoftsimapws.esisteuuid", new String[] {}, false));
      }

      Long id = this.nextvalW3SIMAP();

      DataColumn[] dcW3SIMAP = new DataColumn[] { new DataColumn("W3SIMAP.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3SIMAP = new DataColumnContainer(dcW3SIMAP);
      DataColumn[] dcW3FS4 = new DataColumn[] { new DataColumn("W3FS4.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS4 = new DataColumnContainer(dcW3FS4);
      DataColumn[] dcW3FS4S2 = new DataColumn[] { new DataColumn("W3FS4S2.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
      DataColumnContainer dccW3FS4S2 = new DataColumnContainer(dcW3FS4S2);
      dccW3FS4S2.addColumn("W3FS4S2.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));

      // Tipo di avviso di preinformazione
      if (avvisoPeriodicoIndicativo.isSetNOTICEF01()) {
        dccW3FS4.addColumn("W3FS4.NOTICE_F04", JdbcParametro.TIPO_NUMERICO, new Long(avvisoPeriodicoIndicativo.getNOTICEF01().longValue()));
      }

      // SEZIONE I: AMMINISTRAZIONE AGGIUDICATRICE
      dccW3SIMAP.addColumn("W3SIMAP.FORM", JdbcParametro.TIPO_TESTO, "FS4");
      dccW3SIMAP.addColumn("W3SIMAP.FSVERSION", JdbcParametro.TIPO_NUMERICO, new Long(5));
      dccW3SIMAP.addColumn("W3SIMAP.SYSCON", JdbcParametro.TIPO_NUMERICO, idAccount);
      dccW3SIMAP.addColumn("W3SIMAP.CTYPE", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getTYPECONTRACT().toString());
      dccW3SIMAP.addColumn("W3SIMAP.UUID", JdbcParametro.TIPO_TESTO, uuid);
      dccW3SIMAP.addColumn("W3SIMAP.LEGAL_BASIS", JdbcParametro.TIPO_TESTO, "32014L0025");

      // Amministrazione aggiudicatrice
      AuthorityAType authorityA = avvisoPeriodicoIndicativo.getAUTHORITYA();
      String codamm = this.gestioneW3AMMI(authorityA, account);
      dccW3SIMAP.addColumn("W3SIMAP.CODAMM", JdbcParametro.TIPO_TESTO, codamm);

      // Disponibilita' dei documenti di gara
      if (avvisoPeriodicoIndicativo.isSetDOCUMENTFURE()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_FU_RE", JdbcParametro.TIPO_NUMERICO, new Long(
            avvisoPeriodicoIndicativo.getDOCUMENTFURE().longValue()));
      }

      // URL dei documenti di gara
      if (avvisoPeriodicoIndicativo.isSetDOCUMENTURL()) {
        dccW3SIMAP.addColumn("W3SIMAP.DOCUMENT_URL", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getDOCUMENTURL());
      }

      // Ulteriori informazioni
      if (avvisoPeriodicoIndicativo.isSetFURTHERINFOIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "1");
      } else if (avvisoPeriodicoIndicativo.isSetFURTHERINFOADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO", JdbcParametro.TIPO_TESTO, "2");
        if (avvisoPeriodicoIndicativo.isSetFURTHERINFOBODY()) {
          String further_info_codein = this.gestioneUFFINT(avvisoPeriodicoIndicativo.getFURTHERINFOBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.FURTHER_INFO_CODEIN", JdbcParametro.TIPO_TESTO, further_info_codein);
        }
      }

      // Offerte e domande di partecipazione
      if (avvisoPeriodicoIndicativo.isSetPARTECIPATIONIDEM()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "1");
      } else if (avvisoPeriodicoIndicativo.isSetPARTECIPATIONADD()) {
        dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION", JdbcParametro.TIPO_TESTO, "2");
        if (avvisoPeriodicoIndicativo.isSetPARTECIPATIONBODY()) {
          String participarion_codein = this.gestioneUFFINT(avvisoPeriodicoIndicativo.getPARTECIPATIONBODY(), account);
          dccW3SIMAP.addColumn("W3SIMAP.PARTICIPATION_CODEIN", JdbcParametro.TIPO_TESTO, participarion_codein);
        }
      }

      // SEZIONE II - OGGETTO
      // Titolo
      dccW3FS4S2.addColumn("W3FS4S2.TITLE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getTITLECONTRACT());

      // CPV
      if (avvisoPeriodicoIndicativo.isSetCPV()) {
        dccW3FS4S2.addColumn("W3FS4S2.CPV", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getCPV());
      }

      // Tipo di appalto
      dccW3FS4S2.addColumn("W3FS4S2.TYPE_CONTRACT", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getTYPECONTRACT().toString());

      // Breve descrizione
      if (avvisoPeriodicoIndicativo.isSetSHORTDESCRIPTION()) {
        dccW3FS4S2.addColumn("W3FS4S2.SCOPE_TOTAL", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getSHORTDESCRIPTION());
      }

      // Valore totale stimato
      if (avvisoPeriodicoIndicativo.isSetSCOPECOST()) {
        dccW3FS4S2.addColumn("W3FS4S2.SCOPE_COST", JdbcParametro.TIPO_DECIMALE, new Double(avvisoPeriodicoIndicativo.getSCOPECOST()));
      }

      // Lotto/i
      if (avvisoPeriodicoIndicativo.isSetDIVINTOLOTSNO() && avvisoPeriodicoIndicativo.isSetLOT()) {
        // Bando di gara non suddiviso in lotti
        dccW3FS4S2.addColumn("W3FS4S2.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "2");

        // Lotto
        LottoCommonType lot = avvisoPeriodicoIndicativo.getLOT();
        this.inserisciW3ANNEXB(id, new Long(1), lot);
      } else if (avvisoPeriodicoIndicativo.isSetDIVINTOLOTSYES()) {
        // Bando di gara suddiviso in lotti
        dccW3FS4S2.addColumn("W3FS4S2.DIV_INTO_LOTS", JdbcParametro.TIPO_TESTO, "1");

        // Le offerte vanno presentate per..
        if (avvisoPeriodicoIndicativo.isSetDIVLOTSVALUE()) {
          dccW3FS4S2.addColumn("W3FS4S2.DIV_LOTS", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getDIVLOTSVALUE().toString());
        }

        // Numero massimo di lotti per i quali presentare le offerte
        if (avvisoPeriodicoIndicativo.isSetDIVLOTSMAX()) {
          dccW3FS4S2.addColumn("W3FS4S2.DIV_LOTS_MAX", JdbcParametro.TIPO_NUMERICO, new Long(avvisoPeriodicoIndicativo.getDIVLOTSMAX()));
        }

        // Numero massimo di lotti che possono essere aggiudicati ad un
        // offerente
        if (avvisoPeriodicoIndicativo.isSetLOTSMAXTENDERER()) {
          dccW3FS4S2.addColumn("W3FS4S2.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO,
              new Long(avvisoPeriodicoIndicativo.getLOTSMAXTENDERER()));
        } else {
          if (avvisoPeriodicoIndicativo.isSetDIVLOTSVALUE() && avvisoPeriodicoIndicativo.getDIVLOTSVALUE().intValue() == 1) {
            dccW3FS4S2.addColumn("W3FS4S2.LOTS_MAX_TENDERER", JdbcParametro.TIPO_NUMERICO, new Long(1));
          }
        }

        // Lotti
        LottoCommonType[] lots = avvisoPeriodicoIndicativo.getLOTSArray();
        for (int lo = 0; lo < lots.length; lo++) {
          LottoCommonType lotto = lots[lo];
          this.inserisciW3ANNEXB(id, new Long(lo + 1), lotto);
        }
      }

      // Data prevista di pubblicazione del bando di gara
      if (avvisoPeriodicoIndicativo.isSetDATEPUBLI()) {
        dccW3FS4S2.addColumn("W3FS4S2.DATE_PUBLI", JdbcParametro.TIPO_DATA, avvisoPeriodicoIndicativo.getDATEPUBLI().getTime());
      }

      // SEZIONE IV - PROCEDURA
      // Tipo di procedura
      if (avvisoPeriodicoIndicativo.isSetTYPEPROCEDURE()) {
        dccW3FS4.addColumn("W3FS4.TYPE_PROCEDURE", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getTYPEPROCEDURE().toString());
      }

      // L'appalto e' disciplinato dall'accordo sugli appalti pubblici ?
      if (avvisoPeriodicoIndicativo.isSetGPA()) {
        dccW3FS4.addColumn("W3FS4.GPA", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getGPA().toString());
      }

      // Data termine per la ricezione delle manifestazioni di interesse
      if (avvisoPeriodicoIndicativo.isSetDATERECEIPT()) {
        dccW3FS4.addColumn("W3FS4.DATE_RECEIPT", JdbcParametro.TIPO_DATA, avvisoPeriodicoIndicativo.getDATERECEIPT().getTime());
      }

      // Ora termine per la ricezione delle manifestazioni di interesse
      if (avvisoPeriodicoIndicativo.isSetTIMERECEIPT()) {
        dccW3FS4.addColumn("W3FS4.TIME_RECEIPT", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getTIMERECEIPT());
      }

      // Accordo quadro
      if (avvisoPeriodicoIndicativo.isSetFRAMEWORKNO()) {
        dccW3FS4.addColumn("W3FS4.FRAMEWORK", JdbcParametro.TIPO_TESTO, "2");
      } else if (avvisoPeriodicoIndicativo.isSetFRAMEWORKYES()) {
        dccW3FS4.addColumn("W3FS4.FRAMEWORK", JdbcParametro.TIPO_TESTO, "1");
        if (avvisoPeriodicoIndicativo.isSetFRAMESEVERALOPNO()) {
          dccW3FS4.addColumn("W3FS4.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "2");
        } else if (avvisoPeriodicoIndicativo.isSetFRAMESEVERALOPYES()) {
          dccW3FS4.addColumn("W3FS4.FR_SEVERAL_OP", JdbcParametro.TIPO_TESTO, "1");
          if (avvisoPeriodicoIndicativo.isSetFRAMEOPERATORSNUMBER()) {
            dccW3FS4.addColumn("W3FS4.FR_NB_PARTECIPANTS", JdbcParametro.TIPO_NUMERICO,
                new Long(avvisoPeriodicoIndicativo.getFRAMEOPERATORSNUMBER()));
          }
        }
      }

      // Asta elettronica ?
      if (avvisoPeriodicoIndicativo.isSetISELECTRONIC()) {
        dccW3FS4.addColumn("W3FS4.EAUCTION", JdbcParametro.TIPO_TESTO, avvisoPeriodicoIndicativo.getISELECTRONIC().toString());
      }

      // SEZIONE VI - ALTRE INFORMAZIONI
      // Organismo responsabile delle procedure di ricorso
      if (avvisoPeriodicoIndicativo.isSetREVIEWBODY()) {
        String appeal_procedure_codein = this.gestioneUFFINT(avvisoPeriodicoIndicativo.getREVIEWBODY(), account);
        dccW3FS4.addColumn("W3FS4.REVIEW_BODY_CODEIN", JdbcParametro.TIPO_TESTO, appeal_procedure_codein);
      }

      dccW3SIMAP.insert("W3SIMAP", this.sqlManager);
      dccW3FS4.insert("W3FS4", this.sqlManager);
      dccW3FS4S2.insert("W3FS4S2", this.sqlManager);

      // Lingue utilizzabili- default IT
      if (avvisoPeriodicoIndicativo.isSetNOTICEF01()) {
        if (NOTICE_F04_PER_CALL_COMPETITION.equals(new Long(avvisoPeriodicoIndicativo.getNOTICEF01().longValue()))) {
          // Inserire la lingua
          DataColumn[] dcW3LANGUAGE = new DataColumn[] { new DataColumn("W3LANGUAGE.ID", new JdbcParametro(JdbcParametro.TIPO_NUMERICO, id)) };
          DataColumnContainer dccW3LANGUAGE = new DataColumnContainer(dcW3LANGUAGE);
          dccW3LANGUAGE.addColumn("W3LANGUAGE.NUM", JdbcParametro.TIPO_NUMERICO, new Long(1));
          dccW3LANGUAGE.addColumn("W3LANGUAGE.LANGUAGE_EC", JdbcParametro.TIPO_TESTO, "IT");
          dccW3LANGUAGE.insert("W3LANGUAGE", this.sqlManager);
        }
      }

      this.inserisciPermessi(idAccount, "W3SIMAP", "ID", id);

    } else {
      String listaErroriValidazione = "";
      Iterator iter = validationErrors.iterator();
      while (iter.hasNext()) {
        listaErroriValidazione += iter.next() + " ";
      }
      throw new Exception(listaErroriValidazione);
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSManager.inserisciAvvisoPreinformazione: fine metodo");

  }

  /**
   * Calcola l'id di W3SIMAP
   * 
   * @return
   */
  private Long nextvalW3SIMAP() throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("nextvalW3SIMAP: inizio metodo");

    // Long id = new Long(this.genChiaviManager.getMaxId("W3SIMAP", "ID"));
    // id = new Long(id.longValue() + 1);

    Long id = null;

    try {
      id = new Long(genChiaviManager.getNextId("W3SIMAP"));
    } catch (DataAccessException e) {
      throw new GestoreException("Errore nel calcolo del nuovo id da usare per l'inserimento in W3SIMAP.ID", "calcoloIdAutoincrementante",
          e);
    }

    if (logger.isDebugEnabled()) logger.debug("nextvalW3SIMAP: inizio metodo");

    return id;

  }

  /**
   * Creazione del filtro permessi.
   * 
   * @param syscon
   * @param sysab3
   * @param tblname
   * @param clm1name
   * @param sqlManager
   * @return
   * @throws SQLException
   */
  private String getFiltroPermessi(Long syscon, String sysab3, String tblname, String clm1name, SqlManager sqlManager) throws SQLException {

    String filtroPermessi = null;

    if ("U".equals(sysab3)) {
      Campo c = DizionarioCampi.getInstance().getCampoByNomeFisico(tblname + "." + clm1name);

      String clm1value_perm = "";
      if (c.getTipoColonna() == 0) {
        clm1value_perm = "clm1value_vc";
      } else if (c.getTipoColonna() == 2) {
        clm1value_perm = "clm1value_nu";
      }

      filtroPermessi = " exists (select v_perm.tblname from v_perm where ";
      filtroPermessi += " v_perm.syscon = " + syscon.toString();
      filtroPermessi += " and v_perm.tblname = '" + tblname + "'";
      filtroPermessi += " and v_perm.clm1name = '" + clm1name + "'";
      filtroPermessi += " and v_perm." + clm1value_perm + " = " + tblname + "." + clm1name + ")";
    }

    return filtroPermessi;

  }

  public GeneManager getGeneManager() {
    return geneManager;
  }

  public SqlManager getSqlManager() {
    return sqlManager;
  }

  public LoginManager getLoginManager() {
    return loginManager;
  }

  public LdapManager getLdapManager() {
    return ldapManager;
  }

  public GenChiaviManager getGenChiaviManager() {
    return genChiaviManager;
  }

  public W3Manager getW3Manager() {
    return w3Manager;
  }

}
