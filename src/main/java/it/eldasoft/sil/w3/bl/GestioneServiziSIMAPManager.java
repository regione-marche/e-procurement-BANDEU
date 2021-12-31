package it.eldasoft.sil.w3.bl;

import it.eldasoft.gene.bl.PropsConfigManager;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.db.domain.PropsConfig;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.properties.ConfigManager;
import it.eldasoft.utils.sicurezza.CriptazioneException;
import it.eldasoft.utils.sicurezza.FactoryCriptazioneByte;
import it.eldasoft.utils.sicurezza.ICriptazioneByte;
import it.eldasoft.utils.utility.UtilityDate;

import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;

import eu.europa.ec.publications.esentool.rest.client.EsentoolRestClient;
import eu.europa.ec.publications.esentool.rest.client.NoticeInformation;
import eu.europa.ec.publications.esentool.rest.client.PublicationInfo;
import eu.europa.ec.publications.esentool.rest.client.RenderFormat;
import eu.europa.ec.publications.esentool.rest.client.RestClientException;

public class GestioneServiziSIMAPManager {

  /** Logger */
  static Logger                 logger                                = Logger.getLogger(GestioneServiziSIMAPManager.class);

  private static final String   PROP_SIMAP_WS_ALFA_URL                = "it.eldasoft.simap.ws.alfa.url";
  private static final String   PROP_SIMAP_WS_BETA_URL                = "it.eldasoft.simap.ws.beta.url";
  private static final String   PROP_SIMAP_WS_GAMMA_URL               = "it.eldasoft.simap.ws.gamma.url";

  private static final String   PROP_SIMAP_WS_COMMON_CODAPP           = "W3";
  private static final String   PROP_SIMAP_NO_DOC_EXT_MAX             = "simap.no_doc_ext.max";
  private static final Long     PROP_SIMAP_NO_DOC_EXT_INITIAL         = new Long(500);

  private static final String   PROP_SIMAP_ESENDER_LOGIN              = "it.eldasoft.simap.esenderlogin";
  private static final String   PROP_SIMAP_WS_PASSWORD                = "it.eldasoft.simap.ws.password";

  private static final String   PROP_SIMAP_GET_REPORT_ALL_EXCLUSIONS  = "it.eldasoft.simap.getreportall.exclusions";

  private SqlManager            sqlManager;
  private PropsConfigManager    propsConfigManager;

  private static final String   STATUS_CODE_NOT_PUBLISHED             = "NOT_PUBLISHED";
  private static final String   STATUS_DESCRIPTION_CANCEL_PUBLICATION = "CP";

  private ExportXMLSIMAPManager exportXMLSIMAPManager;

  public SqlManager getSqlManager() {
    return this.sqlManager;
  }

  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  public void setExportXMLSIMAPManager(ExportXMLSIMAPManager exportXMLSIMAPManager) {
    this.exportXMLSIMAPManager = exportXMLSIMAPManager;
  }

  public void setPropsConfigManager(PropsConfigManager propsConfigManager) {
    this.propsConfigManager = propsConfigManager;
  }

  public PropsConfigManager getPropsConfigManager() {
    return propsConfigManager;
  }

  /**
   * Invio a SIMAP per ottenere l'anteprima del documento PDF.
   * 
   * @param syscon
   * @param phase
   * @param id
   * @throws GestoreException
   * @throws ServiceException
   * @throws SQLException
   */
  public byte[] renderNotice(Long syscon, String phase, Long id) throws GestoreException, ServiceException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("renderNotice: inizio metodo");

    byte[] pdf = null;

    try {

      // Gestione numero documento interno
      String no_doc_ext = this.gestioneNumeroDocumentoInterno(id);

      // Indirizzi WS REST
      String url = "";
      if ("ALFA".equals(phase)) {
        url = ConfigManager.getValore(PROP_SIMAP_WS_ALFA_URL);
      } else if ("BETA".equals(phase)) {
        url = ConfigManager.getValore(PROP_SIMAP_WS_BETA_URL);
      } else if ("GAMMA".equals(phase)) {
        url = ConfigManager.getValore(PROP_SIMAP_WS_GAMMA_URL);
      }

      if (url != null) {
        url = url.trim();
      }

      if ("/".equals(url.substring(url.length() - 1))) {
        url = url.substring(0, url.length() - 1);
      }

      // Credenziali
      HashMap<String, String> hMapSIMAPWSUserPass = new HashMap<String, String>();
      hMapSIMAPWSUserPass = recuperaSIMAPWSUserPass(id);
      String username = ((String) hMapSIMAPWSUserPass.get("simapwsuser"));
      String password = ((String) hMapSIMAPWSUserPass.get("simapwspass"));

      // Generazione contenuto XML da inviare via WS
      String notice = exportXMLSIMAPManager.export(id, syscon, no_doc_ext);
      String noticeBase64 = Base64.encode(notice.getBytes("UTF-8"));

      // Richiesta render PDF
      EsentoolRestClient client = new eu.europa.ec.publications.esentool.rest.client.EsentoolRestClient.EsentoolRestClientBuilder(url,
          username, password).build();
      byte[] pdfBase64 = client.renderNotice(noticeBase64.getBytes("UTF-8"), RenderFormat.PDF, "IT");
      pdf = Base64.decode(new String(pdfBase64));

    } catch (RestClientException r) {
      String err = "";
      if (r.getError() != null) err = r.getError().toString();
      throw new GestoreException("Errore nell'invio dei dati a SIMAP: " + err, "gestioneSIMAPWS.rest.error", new Object[] { err }, r);
    } catch (Throwable t) {
      throw new GestoreException("Errore nell'invio dei dati a SIMAP", "gestioneSIMAPWS.remote.error", t);
    }

    if (logger.isDebugEnabled()) logger.debug("renderNotice: fine metodo");

    return pdf;

  }

  /**
   * Invio dei dati all'ufficio pubblicazioni della comunita' Europea. Questo
   * metodo e' utilizzato nella fase ALFA, BETA e GAMMA.
   * 
   * @param syscon
   * @param phase
   * @param id
   * @throws GestoreException
   * @throws ServiceException
   * @throws SQLException
   */
  public void submitNotice(Long syscon, String phase, Long id) throws GestoreException, ServiceException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("submitNotice: inizio metodo");

    try {

      // Gestione numero documento interno
      String no_doc_ext = this.gestioneNumeroDocumentoInterno(id);

      // Indirizzi WS REST
      String url = "";
      if ("ALFA".equals(phase)) {
        url = ConfigManager.getValore(PROP_SIMAP_WS_ALFA_URL);
      } else if ("BETA".equals(phase)) {
        url = ConfigManager.getValore(PROP_SIMAP_WS_BETA_URL);
      } else if ("GAMMA".equals(phase)) {
        url = ConfigManager.getValore(PROP_SIMAP_WS_GAMMA_URL);
      }

      if (url != null) {
        url = url.trim();
      }

      if ("/".equals(url.substring(url.length() - 1))) {
        url = url.substring(0, url.length() - 1);
      }

      // Credenziali
      HashMap<String, String> hMapSIMAPWSUserPass = new HashMap<String, String>();
      hMapSIMAPWSUserPass = recuperaSIMAPWSUserPass(id);
      String username = ((String) hMapSIMAPWSUserPass.get("simapwsuser"));
      String password = ((String) hMapSIMAPWSUserPass.get("simapwspass"));

      // Generazione contenuto XML da inviare via WS
      String notice = exportXMLSIMAPManager.export(id, syscon, no_doc_ext);
      String noticeBase64 = Base64.encode(notice.getBytes("UTF-8"));

      // Invio a SIMAP
      EsentoolRestClient client = new eu.europa.ec.publications.esentool.rest.client.EsentoolRestClient.EsentoolRestClientBuilder(url,
          username, password).build();
      NoticeInformation noticeInformation = client.submitNotice(noticeBase64.getBytes("UTF-8"));

      // Memorizzazione della risposta
      if (noticeInformation != null) {
        String submissionId = noticeInformation.getSubmissionId();
        String statusCode = noticeInformation.getStatus().toString();
        String statusDescription = noticeInformation.getReasonCode();
        Date report_date = noticeInformation.getStatusUpdatedAt();

        StringWriter xmlStringWriter = new StringWriter();
        JAXBContext jaxbLocalContext = JAXBContext.newInstance(NoticeInformation.class);
        Marshaller jaxbMarshaller = jaxbLocalContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        QName qName = new QName("eu.europa.ec.publications.esentool.rest.client.NoticeInformation", "noticeInformation");
        JAXBElement<NoticeInformation> root = new JAXBElement<NoticeInformation>(qName, NoticeInformation.class, noticeInformation);
        jaxbMarshaller.marshal(root, xmlStringWriter);
        String noticeInfoXML = xmlStringWriter.toString();

        this.memorizzaSIMAPWS(id, submissionId, statusCode, statusDescription, notice, phase, report_date, noticeInfoXML);
      }

    } catch (RestClientException r) {
      String err = "";
      if (r.getError() != null) err = r.getError().toString();
      throw new GestoreException("Errore nell'invio dei dati a SIMAP: " + err, "gestioneSIMAPWS.rest.error", new Object[] { err }, r);
    } catch (Throwable t) {
      throw new GestoreException("Errore nell'invio dei dati a SIMAP", "gestioneSIMAPWS.remote.error", t);
    }

    if (logger.isDebugEnabled()) logger.debug("submitNotice: fine metodo");

  }

  /**
   * Recupera da SIMAP tutti i report dettagliati per gli invii con lo stato
   * differente da "PUBLISHED".
   * 
   * @param id
   * @throws SQLException
   */
  public void getNoticeReportAll() throws GestoreException, ServiceException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("getNoticeReport: inizio metodo");

    // Lettura degli stati esclusi dalla lettura periodica
    String exclusions = ConfigManager.getValore(PROP_SIMAP_GET_REPORT_ALL_EXCLUSIONS);
    if (exclusions == null || (exclusions != null && "".equals(exclusions.trim()))) {
      exclusions = "PUBLISHED, NOT_PUBLISHED";
    }

    String ex[] = exclusions.split(",");
    for (int i = 0; i < ex.length; i++) {
      ex[i] = ex[i].trim();
    }

    String selectW3SIMAPWS = "select id, num from w3simapws where submission_id is not null and (status_code is null or status_code not in (";
    for (int i = 0; i < ex.length; i++) {
      if (i > 0) selectW3SIMAPWS += ",";
      selectW3SIMAPWS += "?";
    }
    selectW3SIMAPWS += ")) order by num";

    List<?> datiW3SIMAPWS = this.sqlManager.getListVector(selectW3SIMAPWS, ex);

    if (datiW3SIMAPWS != null && datiW3SIMAPWS.size() > 0) {
      for (int i = 0; i < datiW3SIMAPWS.size(); i++) {
        Long id = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAPWS.get(i), 0).getValue();
        Long num = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAPWS.get(i), 1).getValue();
        this.getNoticeReport(id, num);
      }
    }

    if (logger.isDebugEnabled()) logger.debug("getNoticeReport: fine metodo");

  }

  /**
   * Recupera da SIMAP il report dettagliato corrispondente ad uno specifico
   * invio.
   * 
   * @param syscon
   * @param id
   * @param num
   * @throws SQLException
   */
  public void getNoticeReport(Long id, Long num) throws GestoreException, ServiceException, SQLException {

    if (logger.isDebugEnabled()) logger.debug("getNoticeReport: inizio metodo");

    try {

      String selectW3SIMAPWS = "select submission_id, phase from w3simapws where id = ? and num = ?";
      List<?> datiW3SIMAPWS = this.sqlManager.getVector(selectW3SIMAPWS, new Object[] { id, num });

      if (datiW3SIMAPWS != null && datiW3SIMAPWS.size() > 0) {

        // Identificativo presso SIMAP
        String submission_id = (String) SqlManager.getValueFromVectorParam(datiW3SIMAPWS, 0).getValue();

        // Fase di invio
        String phase = (String) SqlManager.getValueFromVectorParam(datiW3SIMAPWS, 1).getValue();

        // Indirizzi WS REST
        String url = "";
        if ("ALFA".equals(phase)) {
          url = ConfigManager.getValore(PROP_SIMAP_WS_ALFA_URL);
        } else if ("BETA".equals(phase)) {
          url = ConfigManager.getValore(PROP_SIMAP_WS_BETA_URL);
        } else if ("GAMMA".equals(phase)) {
          url = ConfigManager.getValore(PROP_SIMAP_WS_GAMMA_URL);
        }

        if (url != null) {
          url = url.trim();
        }

        if ("/".equals(url.substring(url.length() - 1))) {
          url = url.substring(0, url.length() - 1);
        }

        if (url != null && !"".equals(url.trim())) {
          // Credenziali
          HashMap<String, String> hMapSIMAPWSUserPass = new HashMap<String, String>();
          hMapSIMAPWSUserPass = recuperaSIMAPWSUserPass(id);
          String username = ((String) hMapSIMAPWSUserPass.get("simapwsuser"));
          String password = ((String) hMapSIMAPWSUserPass.get("simapwspass"));

          if (username != null && !"".equals(username.trim()) && password != null && !"".equals(password.trim())) {
            EsentoolRestClient client = new eu.europa.ec.publications.esentool.rest.client.EsentoolRestClient.EsentoolRestClientBuilder(
                url, username, password).build();
            NoticeInformation noticeInformation = client.getNoticeInformation(submission_id);

            if (noticeInformation != null) {
              String statusCode = noticeInformation.getStatus().toString();
              String statusDescription = noticeInformation.getReasonCode();
              Date report_date = noticeInformation.getStatusUpdatedAt();

              // Dati di pubblicazione sul supplemento della Gazzetta Ufficiale
              PublicationInfo publicationInfo = noticeInformation.getPublicationInfo();
              if (publicationInfo != null) {
                String noDocOjs = publicationInfo.getNoDocOjs();
                Date publicationDate = publicationInfo.getPublicationDate();
                String tedLink = null;
                // Link al documento ufficiale pubblicato su TED
                Map<String, String> tedLinks = publicationInfo.getTedLinks();
                if (tedLinks != null) {
                  try {
                    tedLink = tedLinks.get("IT");
                  } catch (Exception e) {

                  }
                }
                // Aggiornamento delle informazioni
                String updateW3SIMAP = "update w3simap set notice_number_oj = ?, date_oj = ?, ted_links = ? where id = ?";
                this.sqlManager.update(updateW3SIMAP, new Object[] { noDocOjs, publicationDate, tedLink, id });
              }

              StringWriter xmlStringWriter = new StringWriter();
              JAXBContext jaxbLocalContext = JAXBContext.newInstance(NoticeInformation.class);
              Marshaller jaxbMarshaller = jaxbLocalContext.createMarshaller();
              jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
              QName qName = new QName("eu.europa.ec.publications.esentool.rest.client.NoticeInformation", "noticeInformation");
              JAXBElement<NoticeInformation> root = new JAXBElement<NoticeInformation>(qName, NoticeInformation.class, noticeInformation);
              jaxbMarshaller.marshal(root, xmlStringWriter);
              String noticeInfoXML = xmlStringWriter.toString();

              String W3SIMAPWS_statusCode = (String) sqlManager.getObject("select status_code from w3simapws where id = ? and num = ?",
                  new Object[] { id, num });

              if (statusCode != null && !statusCode.equals(W3SIMAPWS_statusCode)) {
                String updateW3SIMAPWS = "update w3simapws set stato = '2', report_date = ?, status_code = ?, status_description = ?, noticeinfo = ? where id = ? and num = ?";
                this.sqlManager.update(updateW3SIMAPWS, new Object[] { report_date, statusCode, statusDescription, noticeInfoXML, id, num });
              } else {
                String updateW3SIMAPWS = "update w3simapws set report_date = ?, status_code = ?, status_description = ?, noticeinfo = ? where id = ? and num = ?";
                this.sqlManager.update(updateW3SIMAPWS, new Object[] { report_date, statusCode, statusDescription, noticeInfoXML, id, num });
              }
            }
          }
        }
      }

    } catch (RestClientException r) {
      // throw new GestoreException("Errore nell'invio dei dati a SIMAP: " +
      // err, "gestioneSIMAPWS.rest.error", new Object[] { err }, r);
      logger.error("Errore nella lettura dei dati da SIMAP", r);
    } catch (Throwable t) {
      logger.error("Errore nella lettura dei dati da SIMAP", t);
      // throw new GestoreException("Errore nell'invio dei dati a SIMAP",
      // "gestioneSIMAPWS.remote.error", t);
    }

    if (logger.isDebugEnabled()) logger.debug("getNoticeReport: fine metodo");

  }

  /**
   * Gestione del numero di documento interno. Questo numero deve essere
   * generato al primo tentativo di invio e riutilizzato le volte successive.
   * 
   * @param id
   * @return
   * @throws GestoreException
   */
  private String gestioneNumeroDocumentoInterno(Long id) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("gestioneNumeroDocumentoInterno: inizio metodo");

    String no_doc_ext = null;

    try {
      no_doc_ext = (String) this.sqlManager.getObject("select no_doc_ext from w3simap where id = ?", new Object[] { id });

      // 23/01/2020
      // Controllo che l'anno del numero documento sia uguale all'anno corrente.
      // Se e' quello dell'anno corrente non e' necessario ricalcolarlo,
      // altrimenti si rende "null" il valore
      // per procedere con la generazione di un nuovo numero documento
      if (no_doc_ext != null && !"".equals(no_doc_ext.trim())) {
        String annoCorrente = new Long(UtilityDate.getDataOdiernaAsDate().getYear() + 1900).toString();
        if (!annoCorrente.equals(no_doc_ext.substring(0, 4))) {
          no_doc_ext = null;
        }
      }

      // 21/01/2021
      // Verifica se l'ultimo tentativo di invio ha come risultato la non
      // pubblicazione (STATUS_CODE = NOT_PUBLISHED) perche' e' stata richiesto
      // lo stop della pubblicazione (STATUS_DESCRIPTION = CP)
      Long maxW3SIMAPWS_NUM = (Long) this.sqlManager.getObject("select max(num) from w3simapws where id = ?", new Object[] { id });
      if (maxW3SIMAPWS_NUM != null) {
        String status_code = (String) this.sqlManager.getObject("select status_code from w3simapws where id = ? and num = ?", new Object[] {
            id, maxW3SIMAPWS_NUM });
        if (status_code != null && STATUS_CODE_NOT_PUBLISHED.equals(status_code)) {
          String status_description = (String) this.sqlManager.getObject(
              "select status_description from w3simapws where id = ? and num = ?", new Object[] { id, maxW3SIMAPWS_NUM });
          if (status_description != null && STATUS_DESCRIPTION_CANCEL_PUBLICATION.equals(status_description)) {
            no_doc_ext = null;
          }
        }
      }

      if (no_doc_ext == null || (no_doc_ext != null && "".equals(no_doc_ext.trim()))) {

        Long no_doc_ext_max = null;

        /*
         * Lettura del valore da W_CONFIG e verifica se la stringa e' numerica
         */
        try {
          String no_doc_ext_max_s = ConfigManager.getValore(PROP_SIMAP_NO_DOC_EXT_MAX);
          no_doc_ext_max = Long.parseLong(no_doc_ext_max_s);
        } catch (NumberFormatException e) {

        }

        /*
         * Calcolo del contatore e memorizzazione in W_CONFIG
         */
        if (no_doc_ext_max == null) no_doc_ext_max = PROP_SIMAP_NO_DOC_EXT_INITIAL;
        no_doc_ext_max = no_doc_ext_max + 1;

        PropsConfig[] property = new PropsConfig[1];
        property[0] = new PropsConfig();
        property[0].setCodApp(PROP_SIMAP_WS_COMMON_CODAPP);
        property[0].setChiave(PROP_SIMAP_NO_DOC_EXT_MAX);
        property[0].setValore(no_doc_ext_max.toString());
        this.propsConfigManager.insertProperties(property);
        for (int i = 0; i < property.length; i++) {
          String propChiave = property[i].getChiave();
          String propValore = property[i].getValore();
          if (propValore == null) propValore = new String("");
          if (ConfigManager.esisteProprietaDB(propChiave)) {
            ConfigManager.ricaricaProprietaDB(propChiave, propValore);
          } else {
            ConfigManager.caricaProprietaDB(propChiave, propValore);
          }
        }

        /*
         * Gestione e formattazione del numero documento
         */
        no_doc_ext = new Long(UtilityDate.getDataOdiernaAsDate().getYear() + 1900).toString() + "-";
        NumberFormat idF = new DecimalFormat("000000");
        no_doc_ext += idF.format(no_doc_ext_max);
        this.sqlManager.update("update w3simap set no_doc_ext = ? where id = ?", new Object[] { no_doc_ext, id });
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella gestione del numero documento interno", "gestioneSIMAPWSNODOCEXT.error", e);
    }
    if (logger.isDebugEnabled()) logger.debug("gestioneNumeroDocumentoInterno: fine metodo");

    return no_doc_ext;
  }

  /**
   * Inserimento di un nuovo invio nella lista degli invii a SIMAP.
   * 
   * @param id
   * @param submissionId
   * @param statusCode
   * @param statusDescription
   * @throws GestoreException
   */
  private void memorizzaSIMAPWS(Long id, String submissionId, String statusCode, String statusDescription, String xml, String phase,
      Date report_date, String noticeInfoXML) throws GestoreException {

    try {

      Long num = (Long) this.sqlManager.getObject("select max(num) from w3simapws where id = ?", new Object[] { id });
      if (num == null) num = new Long(0);
      num = new Long(num.longValue() + 1);

      String insertW3SIMAPWS = "insert into w3simapws (id, num, submission_id, submission_date, status_code, status_description, xml, phase, report_date, noticeInfo) values (?,?,?,?,?,?,?,?,?,?)";
      this.sqlManager.update(insertW3SIMAPWS, new Object[] { id, num, submissionId, new Date(), statusCode, statusDescription, xml, phase,
          report_date, noticeInfoXML });

    } catch (SQLException e) {
      throw new GestoreException("Errore nella memorizzazione dei dati di invio", "gestioneSIMAPWS.sql.error", e);
    }

  }

  /**
   * Recupero delle credenziali di accesso al WS SIMAP
   * 
   * @param id
   * @return
   * @throws GestoreException
   */
  public HashMap<String, String> recuperaSIMAPWSUserPass(Long id) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("recuperaSIMAPWSUserPass: inizio metodo");

    HashMap<String, String> hMapSIMAPWSUserPass = new HashMap<String, String>();

    try {

      String simapwsuser = null;
      String simapwspass = null;

      // L'utente di accesso ai servizi e' un utente composto dal codice Esender
      // (nel nostro caso TED57) concatenato
      // con il codice del customer (memorizzato nella tabella W3AMMI)
      String esender_login = ConfigManager.getValore(PROP_SIMAP_ESENDER_LOGIN);

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

      simapwsuser = esender_login;
      if (customer_login != null) simapwsuser += customer_login;

      // La password e' generale per tutti i clienti di TED57 e memorizzata
      // criptata in W_CONFIG
      simapwspass = ConfigManager.getValore(PROP_SIMAP_WS_PASSWORD);

      String simapwspassDecriptata = null;
      if (simapwspass != null && simapwspass.trim().length() > 0) {
        ICriptazioneByte simapwspassICriptazioneByte = null;
        simapwspassICriptazioneByte = FactoryCriptazioneByte.getInstance(
            ConfigManager.getValore(CostantiGenerali.PROP_TIPOLOGIA_CIFRATURA_DATI), simapwspass.getBytes(),
            ICriptazioneByte.FORMATO_DATO_CIFRATO);
        simapwspassDecriptata = new String(simapwspassICriptazioneByte.getDatoNonCifrato());
      }

      hMapSIMAPWSUserPass.put("simapwsuser", simapwsuser);
      hMapSIMAPWSUserPass.put("simapwspass", simapwspassDecriptata);

    } catch (SQLException e) {
      throw new GestoreException("Errore nella gestione delle credenziali di accesso al servizio SIMAP", "gestioneSIMAPWSUserPass.error", e);
    } catch (CriptazioneException e) {
      throw new GestoreException("Errore nella gestione delle credenziali di accesso al servizio SIMAP", "gestioneSIMAPWSUserPass.error", e);
    }

    if (logger.isDebugEnabled()) logger.debug("recuperaSIMOGWSUserPass: fine metodo");

    return hMapSIMAPWSUserPass;

  }

}
