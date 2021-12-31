package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ValidazioneFormulariStandard {

  protected static final Long   NOTICE_F01_PRI_ONLY                 = new Long(1);
  protected static final Long   NOTICE_F01_PRI_REDUCING_TIME_LIMITS = new Long(2);
  protected static final Long   NOTICE_F01_PRI_CALL_COMPETITION     = new Long(3);

  protected static final Long   NOTICE_F04_PRI_ONLY                 = new Long(1);
  protected static final Long   NOTICE_F04_PRI_REDUCING_TIME_LIMITS = new Long(2);
  protected static final Long   NOTICE_F04_PRI_CALL_COMPETITION     = new Long(3);
  
  protected static final String F02_PT_OPEN                         = "1";
  protected static final String F02_PT_RESTRICTED                   = "2";
  protected static final String F02_PT_RESTRICTED_ACC               = "3";
  protected static final String F02_PT_NEGOTIATION                  = "4";
  protected static final String F02_PT_NEGOTIATION_ACC              = "5";
  protected static final String F02_PT_COMPETITIVE                  = "6";
  protected static final String F02_PT_OPEN_ACC                     = "7";
  protected static final String F02_PT_INNOVATION                   = "8";

  protected static final String F05_PT_OPEN                         = "1";
  protected static final String F05_PT_RESTRICTED                   = "2";
  protected static final String F05_PT_COMPETITIVE                  = "6";
  protected static final String F05_PT_INNOVATION                   = "8";
  protected static final String F05_PT_NEGOTIATED_WITH_PRIOR_CALL   = "11";

  public ValidazioneFormulariStandard() {
  }

  static Logger logger = Logger.getLogger(ValidazioneFormulariStandard.class);

  /**
   * Validazione della sezione I: amministrazione aggiudicatrice.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  public void validazione_Sezione_I(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    try {
      String selectW3SIMAP = "select form, " // 0
          + " codamm, " // 1
          + " further_info,  " // 2
          + " further_info_codein, " // 3
          + " participation, " // 4
          + " participation_codein, " // 5
          + " participation_el, " // 6
          + " participation_url, " // 7
          + " document_url, " // 8
          + " url_tool, " // 9
          + " document_fu_re, " // 10
          + " joint_procurement, " // 11
          + " procurement_law, " // 12
          + " legal_basis " // 13
          + " from w3simap where id = ?";

      String pagina = "";

      List<?> datiW3SIMAP = sqlManager.getVector(selectW3SIMAP, new Object[] { id });
      if (datiW3SIMAP != null && datiW3SIMAP.size() > 0) {

        String form = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 0).getValue();
        form = form.toLowerCase();

        // Base legale
        String legal_basis = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 13).getValue();
        if (!"FS8".equals(form.toUpperCase())) {
          if (legal_basis == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "LEGAL_BASIS", "");
          }
        }

        String type_ca_ce = null;
        if ("FS1".equals(form.toUpperCase()) || "FS2".equals(form.toUpperCase()) || "FS3".equals(form.toUpperCase())) {
          type_ca_ce = "CA";
        } else if ("FS5".equals(form.toUpperCase()) || "FS6".equals(form.toUpperCase()) || "FS7".equals(form.toUpperCase())) {
          type_ca_ce = "CE";
        } else if ("FS8".equals(form.toUpperCase())) {
          String notice_relation = (String) sqlManager.getObject("select notice_relation from w3fs8 where id = ?", new Object[] { id });
          if (notice_relation != null) {
            if ("3".equals(notice_relation)) {
              type_ca_ce = "CA";
            } else if ("4".equals(notice_relation)) {
              type_ca_ce = "CE";
            } else if ("5".equals(notice_relation)) {
              type_ca_ce = "CE";
            }
          }
        } else if ("FS20".equals(form.toUpperCase())) {
          if (legal_basis != null) {
            if ("32014L0024".equals(legal_basis)) {
              type_ca_ce = "CA";
            } else if ("32014L0025".equals(legal_basis)) {
              type_ca_ce = "CE";
            }
          }
        } else {
          type_ca_ce = "CA";
        }

        // Amministrazione aggiudicatrice, controllo del collegamento e dei dati
        // dell'archivio W3AMMI e UFFINT
        pagina = UtilityTags.getResource("label.simap." + form + ".I.1", null, false);
        String codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 1).getValue();
        if (codamm == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "CODAMM", pagina);
        } else {
          validazioneW3AMMI(sqlManager, form, codamm, listaControlli, type_ca_ce);
        }

        // Lista delle ulteriori amministrazioni aggiudicatrici collegate
        String selectW3SIMAP_ADDR = "select num, codamm from w3simap_addr where id = ?";
        String joint_procurement = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 11).getValue();
        List<?> datiW3SIMAP_ADDR = sqlManager.getListVector(selectW3SIMAP_ADDR, new Object[] { id });

        if (joint_procurement != null) {
          if ("1".equals(joint_procurement)) {
            if (datiW3SIMAP_ADDR == null || (datiW3SIMAP_ADDR != null && datiW3SIMAP_ADDR.size() == 0)) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3SIMAP", "JOINT_PROCUREMENT");
              String messaggio = "Inserire almeno una amministrazione aggiudicatrice congiunta";
              listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }
            if (datiW3SIMAP_ADDR != null && datiW3SIMAP_ADDR.size() > 0) {
              for (int iAddr = 0; iAddr < datiW3SIMAP_ADDR.size(); iAddr++) {
                Long numAddr = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAP_ADDR.get(iAddr), 0).getValue();
                String codammAddr = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP_ADDR.get(iAddr), 1).getValue();
                pagina = UtilityTags.getResource("label.simap." + form + ".I.2", null, false)
                    + " - Amministrazione aggiudicatrice congiunta n. "
                    + numAddr.toString();
                if (codammAddr == null) {
                  ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "CODAMM", pagina);
                } else {
                  validazioneW3AMMI_Addr(sqlManager, form, codammAddr, listaControlli, pagina);
                }
              }
            }
          } else if ("2".equals(joint_procurement)) {
            if (datiW3SIMAP_ADDR != null && datiW3SIMAP_ADDR.size() > 0) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3SIMAP", "JOINT_PROCUREMENT");
              String messaggio = "Non deve essere indicata alcuna amministrazione aggiudicatrice congiunta";
              listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }
          }
        }

        // Nel caso di appalto congiunto che coinvolge diversi paesi,
        // specificare le normative nazionali sugli appalti in vigore
        String procurement_law = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 12).getValue();
        if (procurement_law != null) {
          pagina = UtilityTags.getResource("label.simap." + form + ".I.2", null, false);
          ValidazioneUtility.validazioneS200(procurement_law, "W3SIMAP", "PROCUREMENT_LAW", pagina, listaControlli);
        }

        // Disponibilita' dei documenti di gara
        // Il controllo deve essere effettuato solamente per i formulari FS2 e
        // FS5,
        // per gli altri formulari si controlla solamente il formato
        // dell'eventuale indirizzo URL
        if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase()) || "FS7".equals(form.toUpperCase()) || "FS8".equals(form.toUpperCase())) {
          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase()) || "FS7".equals(form.toUpperCase())) {
            pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Disponibilita' dei documenti di gara";
            Long document_fu_re = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAP, 10).getValue();
            String document_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 8).getValue();
            if (document_fu_re == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "DOCUMENT_FU_RE", pagina);
            } else {
              if (document_url == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "DOCUMENT_URL", pagina);
              } else {
                ValidazioneUtility.validazioneURL(document_url, "W3SIMAP", "DOCUMENT_URL", pagina, listaControlli);
              }
            }
          } else {
            // Documentazione URL, controllo del formato URL
            pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Disponibilita' dei documenti di gara";
            String document_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 8).getValue();
            ValidazioneUtility.validazioneURL(document_url, "W3SIMAP", "DOCUMENT_URL", pagina, listaControlli);
          }

          // Ulteriori informazioni
          pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Ulteriori informazioni";
          String further_info = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 2).getValue();
          String further_info_codein = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 3).getValue();
          if (further_info == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "FURTHER_INFO", pagina);
          } else if ("2".equals(further_info)) {
            if (further_info_codein == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "FURTHER_INFO_CODEIN", pagina);
            } else {
              validazioneUFFINT_S1_S5_S6(sqlManager, further_info_codein, pagina + " - Ufficio/punto di contatto", listaControlli, "S1");
            }
          }

          // Offerte e domande di partecipazione
          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase()) || "FS7".equals(form.toUpperCase())) {
            pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Offerte e domande di partecipazione";
            String participation = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 4).getValue();
            String participation_codein = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 5).getValue();
            if (participation == null) {
              String descrizione = "Le offerte e le domande di partecipazione vanno inviate presso";
              String messaggio = "Il campo &egrave; obbligatorio";
              listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            } else if ("2".equals(participation)) {
              if (participation_codein == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "PARTICIPATION_CODEIN", pagina);
              } else {
                validazioneUFFINT_S1_S5_S6(sqlManager, participation_codein, pagina + " - Ufficio/punto di contatto", listaControlli, "S1");
              }
            }
          }

          // In versione elettronica ?
          String participation_el = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 6).getValue();
          String participation_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 7).getValue();
          if (participation_el != null && "1".equals(participation_el)) {
            if (participation_url == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "PARTICIPATION_URL", pagina);
            } else {
              ValidazioneUtility.validazioneURL(participation_url, "W3SIMAP", "PARTICIPATION_URL", pagina, listaControlli);
            }
          }

          // Strumenti... URL
          pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Comunicazione elettronica";
          String url_tool = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 9).getValue();
          ValidazioneUtility.validazioneURL(url_tool, "W3SIMAP", "URL_TOOL", pagina, listaControlli);

        }

      }

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazione_Sezione_I", e);
    }

  }

  /**
   * Validazione dell'amministrazione aggiudicatrice.
   * 
   * @param sqlManager
   * @param form
   * @param codamm
   * @param listaControlli
   * @param type_ca_ce
   *        (CA - 2014/24/EU, CE - 2014/25/EU)
   * @throws GestoreException
   */
  public void validazioneW3AMMI(SqlManager sqlManager, String form, String codamm, List<Object> listaControlli, String type_ca_ce)
      throws GestoreException {

    String pagina = UtilityTags.getResource("label.simap.w3ammi.I.1", null, false) + " - Amministrazione";

    try {
      String selectW3AMMI = "select codein, " // 0
          + " type_authority, " // 1
          + " type_authority_other, " // 2
          + " type_caacti, " // 3
          + " type_caacti_other, " // 4
          + " type_ceacti, " // 5
          + " type_ceacti_other," // 6
          + " customer_login, " // 7
          + " url_general, " // 8
          + " url_buyer " // 9
          + " from w3ammi where codamm = ? ";
      List<?> datiW3AMMI = sqlManager.getVector(selectW3AMMI, new Object[] { codamm });
      if (datiW3AMMI != null && datiW3AMMI.size() > 0) {
        String codein = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 0).getValue();
        if (codein == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "CODEIN", pagina);
        } else {
          validazioneUFFINT_S1_S5_S6(sqlManager, codein, pagina + " - Ufficio/punto di contatto", listaControlli, "S1");
        }

        // Indirizzo URL generale
        String url_general = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 8).getValue();
        if (url_general == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "URL_GENERAL", pagina);
        } else {
          ValidazioneUtility.validazioneURL(url_general, "W3AMMI", "URL_GENERAL", pagina, listaControlli);
        }

        // Indirizzo URL del profilo del committente
        String url_buyer = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 9).getValue();
        if (url_buyer == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "URL_BUYER", pagina);
        } else {
          ValidazioneUtility.validazioneURL(url_buyer, "W3AMMI", "URL_BUYER", pagina, listaControlli);
        }

        String type_authority = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 1).getValue();
        String type_authority_other = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 2).getValue();
        String type_caacti = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 3).getValue();
        String type_caacti_other = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 4).getValue();
        String type_ceacti = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 5).getValue();
        String type_ceacti_other = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 6).getValue();

        if ("CA".equals(type_ca_ce)) {
          // Tipo di amministrazione aggiudicatrice
          pagina = UtilityTags.getResource("label.simap.w3ammi.I.4", null, false) + " - Amministrazione";
          if ((type_authority == null && type_authority_other == null) || (type_authority != null && type_authority_other != null)) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3AMMI", "TYPE_AUTHORITY");
            descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3AMMI", "TYPE_AUTHORITY_OTHER");
            String messaggio = "Valorizzare uno dei due campi";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
          ValidazioneUtility.validazioneS200(type_authority_other, "W3AMMI", "TYPE_AUTHORITY_OTHER", pagina, listaControlli);

          // Principali settori di attivita' CA
          pagina = UtilityTags.getResource("label.simap.w3ammi.I.5", null, false) + " - Amministrazione";
          if ((type_caacti == null && type_caacti_other == null) || (type_caacti != null && type_caacti_other != null)) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3AMMI", "TYPE_CAACTI");
            descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3AMMI", "TYPE_CAACTI_OTHER");
            String messaggio = "Valorizzare uno dei due campi";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
          ValidazioneUtility.validazioneS200(type_caacti_other, "W3AMMI", "TYPE_CAACTI_OTHER", pagina, listaControlli);

        } else if ("CE".equals(type_ca_ce)) {

          // Principali settori di attivita' (caso CE)
          pagina = UtilityTags.getResource("label.simap.w3ammi.I.6", null, false) + " - Amministrazione";
          if ((type_ceacti == null && type_ceacti_other == null) || (type_ceacti != null && type_ceacti_other != null)) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3AMMI", "TYPE_CEACTI");
            descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3AMMI", "TYPE_CEACTI_OTHER");
            String messaggio = "Valorizzare uno dei due campi";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
          ValidazioneUtility.validazioneS200(type_ceacti_other, "W3AMMI", "TYPE_CEACTI_OTHER", pagina, listaControlli);
        }

        String customer_login = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 7).getValue();
        if (customer_login == null || (customer_login != null && customer_login.equals(""))) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "CUSTOMER_LOGIN", "Amministrazione");
        }

      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'amministrazione aggiudicatrice",
          "validazioneAmministrazione", e);
    }

  }

  /**
   * Controllo amministrazione aggiudicatrice congiunta (aggiuntiva).
   * 
   * @param sqlManager
   * @param form
   * @param codamm
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneW3AMMI_Addr(SqlManager sqlManager, String form, String codamm, List<Object> listaControlli, String pagina)
      throws GestoreException {

    try {
      String selectW3AMMI = "select codein, " // 0
          + " url_general, " // 1
          + " url_buyer " // 2
          + " from w3ammi where codamm = ? ";
      List<?> datiW3AMMI = sqlManager.getVector(selectW3AMMI, new Object[] { codamm });
      if (datiW3AMMI != null && datiW3AMMI.size() > 0) {
        String codein = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 0).getValue();
        if (codein == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "CODEIN", pagina);
        } else {
          validazioneUFFINT_S1_S5_S6(sqlManager, codein, pagina + " - Ufficio/punto di contatto", listaControlli, "S1");
        }

        // Indirizzo URL generale
        String url_general = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 1).getValue();
        if (url_general == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "URL_GENERAL", pagina);
        } else {
          ValidazioneUtility.validazioneURL(url_general, "W3AMMI", "URL_GENERAL", pagina, listaControlli);
        }

        // Indirizzo URL del profilo del committente
        String url_buyer = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 2).getValue();
        if (url_buyer == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "URL_BUYER", pagina);
        } else {
          ValidazioneUtility.validazioneURL(url_buyer, "W3AMMI", "URL_BUYER", pagina, listaControlli);
        }
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'amministrazione aggiudicatrice",
          "validazioneAmministrazione", e);
    }

  }

  /**
   * Validazione dati dell'ufficio/punto di contatto.
   * 
   * @param sqlManager
   * @param codein
   * @param pagina
   * @param listaControlli
   * @param tipo
   *        - Tipo di contratto (S1, S5 o S6)
   * @throws SQLException
   * @throws GestoreException
   */
  public void validazioneUFFINT_S1_S5_S6(SqlManager sqlManager, String codein, String pagina, List<Object> listaControlli, String tipo)
      throws SQLException, GestoreException {
    String selectUFFINT = "select uffint.nomein, " // 0
        + " uffint.viaein, " // 1
        + " uffint.nciein, " // 2
        + " uffint.indweb, " // 3
        + " uffint.telein, " // 4
        + " uffint.faxein, " // 5
        + " uffint.emaiin, " // 6
        + " uffint.codnaz, " // 7
        + " uffint.nomres " // 8
        + " from uffint "
        + " where uffint.codein = ? ";
    List<?> datiUFFINT = sqlManager.getVector(selectUFFINT, new Object[] { codein });
    if (datiUFFINT != null && datiUFFINT.size() > 0) {

      // Nome ufficiale
      String nomein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 0).getValue();
      if (nomein == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "UFFINT", "NOMEIN", pagina);
      } else {
        ValidazioneUtility.validazioneS300(nomein, "UFFINT", "NOMEIN", pagina, listaControlli);
      }

      // Lunghezza complessiva indirizzo, ammessi 400 caratteri
      String viaein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 1).getValue();
      String nciein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 2).getValue();
      if (viaein != null) {
        String indirizzo = "";
        if (viaein != null) indirizzo += viaein;
        if (nciein != null) indirizzo += ", " + nciein;
        ValidazioneUtility.validazioneS400(indirizzo, "UFFINT", "VIAEIN", pagina, listaControlli);
      }

      // Citta'
      String town = (String) sqlManager.getObject("select town from w3uffint where codein = ?", new Object[] { codein });
      if (town == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3UFFINT", "TOWN", pagina);
      } else {
        ValidazioneUtility.validazioneS100(town, "W3UFFINT", "TOWN", pagina, listaControlli);
      }

      // Codice NUTS
      String nuts = (String) sqlManager.getObject("select nuts from w3uffint where codein = ?", new Object[] { codein });
      if (nuts == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3UFFINT", "NUTS", pagina);
      } else {
        validazioneNUTS(sqlManager, nuts, "W3UFFINT", "NUTS", pagina, listaControlli);
      }

      // Nazione
      if (SqlManager.getValueFromVectorParam(datiUFFINT, 7).getValue() == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "UFFINT", "CODNAZ", pagina);
      }

      // Email e URL (controllo di obbligatorieta' per S1)
      if ("S1".equals(tipo)) {
        if (SqlManager.getValueFromVectorParam(datiUFFINT, 6).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "UFFINT", "EMAIIN", pagina);
        }
        if (SqlManager.getValueFromVectorParam(datiUFFINT, 3).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "UFFINT", "INDWEB", pagina);
        }
      }

      // Controllo del formato
      String indweb = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 3).getValue();
      ValidazioneUtility.validazioneURL(indweb, "UFFINT", "INDWEB", pagina, listaControlli);
      String telein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 4).getValue();
      ValidazioneUtility.validazionePhoneFax(telein, "UFFINT", "TELEIN", pagina, listaControlli);
      String faxein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 5).getValue();
      ValidazioneUtility.validazionePhoneFax(faxein, "UFFINT", "FAXEIN", pagina, listaControlli);
      String emaiin = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 6).getValue();
      ValidazioneUtility.validazioneEmail(emaiin, "UFFINT", "EMAIIN", pagina, listaControlli);

      // Responsabile, ammessi 300 caratteri
      String nomres = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 8).getValue();
      if (nomres != null && nomres.length() > 300) {
        String messaggio = "Non sono ammessi pi&ugrave; di 300 caratteri";
        String descrizione = "Persona di contatto";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }
    }

  }

  /**
   * Validazione codice NUTS 2016
   * 
   * @param sqlManager
   * @param pagina
   * @param listaControlli
   * @param nuts
   * @param entita
   * @param campo
   * @throws SQLException
   */
  public void validazioneNUTS(SqlManager sqlManager, String nuts, String entita, String campo, String pagina, List<Object> listaControlli)
      throws SQLException {
    Long cntNUTS = (Long) sqlManager.getObject("select count(*) from tabnuts where codice = ? and descrizione not like '%(NUTS 2016)%'", new Object[] { nuts });
    if (cntNUTS == null || (cntNUTS != null && cntNUTS.longValue() == 0)) {
      String messaggio = "Il codice NUTS indicato non e' coerente con la nuova codifica NUTS 2021";
      ValidazioneUtility.addAvviso(listaControlli, entita, campo, "E", pagina, messaggio);
    }
  }

  /**
   * Validazione impresa
   * 
   * @param sqlManager
   * @param codein
   * @param pagina
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneIMPR(SqlManager sqlManager, String codimp, String pagina, List<Object> listaControlli) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("validazioneIMPR: inizio metodo");

    String selectIMPR = "select nomest, " // 0
        + " proimp, " // 1
        + " nazimp, " // 2
        + " telimp, " // 3
        + " faximp, " // 4
        + " emaiip, " // 5
        + " indweb, " // 6
        + " indimp, " // 7
        + " nciimp " // 8
        + " from impr where codimp = ?";
    try {
      List<?> datiIMPR = sqlManager.getVector(selectIMPR, new Object[] { codimp });

      if (datiIMPR != null && datiIMPR.size() > 0) {
        String nomest = (String) SqlManager.getValueFromVectorParam(datiIMPR, 0).getValue();
        if (nomest == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "IMPR", "NOMEST", pagina);
        }
        ValidazioneUtility.validazioneS300(nomest, "IMPR", "NOMEST", pagina, listaControlli);

        // Lunghezza complessiva indirizzo, ammessi 400 caratteri
        String indimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 7).getValue();
        String nciimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 8).getValue();
        if (indimp != null) {
          String indirizzo = "";
          if (indimp != null) indirizzo += indimp;
          if (nciimp != null) indirizzo += ", " + nciimp;
          ValidazioneUtility.validazioneS400(indirizzo, "IMPR", "INDIMP", pagina, listaControlli);
        }

        String town = (String) sqlManager.getObject("select town from w3impr where codimp = ?", new Object[] { codimp });
        if (town == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3IMPR", "TOWN", pagina);
        }

        String nuts = (String) sqlManager.getObject("select nuts from w3impr where codimp = ?", new Object[] { codimp });
        if (nuts == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3IMPR", "NUTS", pagina);
        } else {
          validazioneNUTS(sqlManager, nuts, "W3IMPR", "NUTS", pagina, listaControlli);
        }

        if (SqlManager.getValueFromVectorParam(datiIMPR, 2).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "IMPR", "NAZIMP", pagina);
        }

        String telimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 3).getValue();
        ValidazioneUtility.validazionePhoneFax(telimp, "IMPR", "TELIMP", pagina, listaControlli);

        String faximp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 4).getValue();
        ValidazioneUtility.validazionePhoneFax(faximp, "IMPR", "FAXIMP", pagina, listaControlli);

        String emaiip = (String) SqlManager.getValueFromVectorParam(datiIMPR, 5).getValue();
        ValidazioneUtility.validazioneEmail(emaiip, "IMPR", "EMAIIP", pagina, listaControlli);

        String indweb = (String) SqlManager.getValueFromVectorParam(datiIMPR, 6).getValue();
        ValidazioneUtility.validazioneURL(indweb, "IMPR", "INDWEB", pagina, listaControlli);

      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'impresa", "validazioneIMPR", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneIMPR: fine metodo");

  }

  /**
   * Controllo numero massimo lotti.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneNumeroMassimoLotti(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    try {
      Long cnt = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ?", new Object[] { id });
      if (cnt != null && cnt.longValue() > 10000) {
        listaControlli.add(((Object) (new Object[] { "E", "Oggetto (II.2)", "Lotti",
            "Il numero totale di lotti supera il numero massimo consentito (10000)" })));
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazioneNumeroMassimoLotti", e);
    }
  }

  /**
   * Controllo numero massimo lotti aggiudicati.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneNumeroMassimoLottiAggiudicati(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    try {
      Long cnt = (Long) sqlManager.getObject("select count(*) from w3fs3award where id = ?", new Object[] { id });
      if (cnt != null && cnt.longValue() > 10000) {
        String descrizione = UtilityTags.getResource("label.simap.fs3.V", null, false);
        listaControlli.add(((Object) (new Object[] { "E", descrizione, "Lotti aggiudicati",
            "Il numero totale di lotti aggiudicati supera il numero massimo consentito (10000)" })));
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazioneNumeroMassimoLottiAggiudicati", e);
    }
  }

  /**
   * Controllo numero massimo lotti aggiudicati FS6.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneNumeroMassimoLottiAggiudicatiFS6(SqlManager sqlManager, Long id, List<Object> listaControlli)
      throws GestoreException {
    try {
      Long cnt = (Long) sqlManager.getObject("select count(*) from w3fs6award where id = ?", new Object[] { id });
      if (cnt != null && cnt.longValue() > 10000) {
        String descrizione = UtilityTags.getResource("label.simap.fs6.V", null, false);
        listaControlli.add(((Object) (new Object[] { "E", descrizione, "Lotti aggiudicati",
            "Il numero totale di lotti aggiudicati supera il numero massimo consentito (10000)" })));
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazioneNumeroMassimoLottiAggiudicati", e);
    }
  }

  /**
   * Validazione dei lotti associati alla comunicazione.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @param w3annexb_num
   *        Eventuale numero di lotto
   * @throws SQLException
   */
  public void validazioneW3ANNEXB(SqlManager sqlManager, Long id, Long w3annexb_num, List<Object> listaControlli) throws GestoreException {

    String selectW3ANNEXB = "select w3annexb.num, " // 0
        + " w3annexb.lotnum, " // 1
        + " w3annexb.site_nuts, " // 2
        + " w3annexb.description, " // 3
        + " w3annexb.ac_doc, " // 4
        + " w3annexb.work_month, " // 5
        + " w3annexb.work_days, " // 6
        + " w3annexb.work_start_date, " // 7
        + " w3annexb.work_end_date, " // 8
        + " w3annexb.renewal, " // 9
        + " w3annexb.renewal_descr, " // 10
        + " w3annexb.options, " // 11
        + " w3annexb.options_descr, " // 12
        + " w3annexb.eu_progr," // 13
        + " w3annexb.eu_progr_descr, " // 14
        + " w3annexb.acc_variants, " // 15
        + " w3annexb.title, " // 16
        + " w3annexb.nb_env_candidate, " // 17
        + " w3annexb.nb_min_candidate, " // 18
        + " w3annexb.nb_max_candidate, " // 19
        + " w3annexb.criteria_candidate, " // 20
        + " w3annexb.site_nuts_2, " // 21
        + " w3annexb.site_nuts_3, " // 22
        + " w3annexb.site_nuts_4, " // 23
        + " w3annexb.cost, " // 24
        + " w3annexb.site_label, " // 25
        + " w3annexb.additional_information, " // 26
        + " w3annexb.agree_to_publish " // 27
        + " from w3annexb where w3annexb.id = ?";

    if (w3annexb_num != null) {
      selectW3ANNEXB += " and w3annexb.num = " + w3annexb_num.toString();
    }

    String selectW3CPV = "select numcpv, cpv from w3cpv where ent = ? and id = ? and num = ?";
    String selectW3AWCRITERIA_CNT = "select count(*) from w3awcriteria where id = ? and num = ?";

    try {

      String form = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });
      form = form.toLowerCase();

      List<?> datiW3ANNEXB = sqlManager.getListVector(selectW3ANNEXB, new Object[] { id });
      if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

        for (int i = 0; i < datiW3ANNEXB.size(); i++) {

          Long num = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 0).getValue();
          Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 1).getValue();

          String pagina = UtilityTags.getResource("label.simap." + form + ".II.2.1", null, false);

          List<Object> listaControlliW3ANNEXB = new Vector<Object>();

          // Numero del lotto
          if (lotnum == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "LOTNUM", pagina);
          } else {
            // Ricerca univocita'
            Long cntlotnum = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ? and lotnum = ?", new Object[] {id, lotnum});
            if (cntlotnum != null && cntlotnum.longValue() > 1) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "LOTNUM");
              String messaggio = "Il numero del lotto deve essere univoco";
              listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }
          }

          // Titolo, denominazione
          String title = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 16).getValue();
          if (title == null) {
            String descrizione = "Denominazione";
            String messaggio = "Il campo &egrave; obbligatorio";
            listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          } else {
            ValidazioneUtility.validazioneS200(title, "W3ANNEXB", "TITLE", pagina, listaControlliW3ANNEXB);
          }

          // Controllo CPV aggiuntivi, se esiste l'occorrenza in W3CPV diventa
          // obbligatorio indicare il CPV del vocabolario principale
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.2", null, false);
          List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { "W3ANNEXB", id, num });
          String div_into_lots = null;
          if ("FS2".equals(form.toUpperCase())) {
            div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs2 where id = ?", new Object[] { id });
          } else if ("FS3".equals(form.toUpperCase())) {
            div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs3 where id = ?", new Object[] { id });
          } else if ("FS5".equals(form.toUpperCase())) {
            div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs5 where id = ?", new Object[] { id });
          } else if ("FS6".equals(form.toUpperCase())) {
            div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs6 where id = ?", new Object[] { id });
          }

          if (div_into_lots != null && "1".equals(div_into_lots)) {
            if (datiW3CPV == null || (datiW3CPV != null && datiW3CPV.size() == 0)) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3CPV", "CPV", pagina);
            }
          }

          if (datiW3CPV != null && datiW3CPV.size() > 0) {
            for (int iCPV = 0; iCPV < datiW3CPV.size(); iCPV++) {
              String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(iCPV), 1).getValue();
              if (cpv == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3CPV", "CPV", pagina);
              }
            }
          }

          // Codice NUTS
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.3", null, false);
          String site_nuts = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 2).getValue();
          if (site_nuts == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "SITE_NUTS", pagina);
          } else {
            validazioneNUTS(sqlManager, site_nuts, "W3ANNEXB", "SITE_NUTS", pagina, listaControlliW3ANNEXB);
          }

          String site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 21).getValue();
          String site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 22).getValue();
          String site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 23).getValue();
          if (site_nuts_2 != null) {
            validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_2", pagina, listaControlliW3ANNEXB);
          }
          if (site_nuts_3 != null) {
            validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_3", pagina, listaControlliW3ANNEXB);
          }
          if (site_nuts_4 != null) {
            validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_4", pagina, listaControlliW3ANNEXB);
          }

          // Sito o luogo principale
          String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 25).getValue();
          ValidazioneUtility.validazioneS200(site_label, "W3ANNEXB", "SITE_LABEL", pagina, listaControlliW3ANNEXB);

          // Descrizione dell'appalto
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.4", null, false);
          String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 3).getValue();
          if (description == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "DESCRIPTION", pagina);
          } else {
            ValidazioneUtility.validazioneS4000(description, "W3ANNEXB", "DESCRIPTION", pagina, listaControlliW3ANNEXB);
          }

          // Criteri di aggiudicazione W3AWCRITERIA Se i criteri sono indicati
          // nei documenti di gara non devono essere indicati i criteri in
          // questa sezione: non devono esistere occorrenze nella tabella
          // W3AWCRITERIA Altrimenti possono essere indicati criteri
          // "Qualita'"
          // (non sono obbligatori), ma se indicati e' necessario indicare
          // anche nome e
          // ponderazione. Deve esserci il criterio "Prezzo" oppure una lista
          // di
          // criteri "Costo"
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.5", null, false);
          if ("FS6".equals(form.toUpperCase())) {
            String agree_to_publish = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 27).getValue();
            if (agree_to_publish == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "AGREE_TO_PUBLISH", pagina);
            }
          }

          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase())) {
            String ac_doc = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 4).getValue();
            if (ac_doc == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "AC_DOC", pagina);
            } else if ("1".equals(ac_doc)) {
              Long cnt = (Long) sqlManager.getObject(selectW3AWCRITERIA_CNT, new Object[] { id, num, });
              if (cnt != null && cnt.longValue() > 0) {
                String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "AC_DOC");
                String messaggio = "Se i criteri di aggiudicazione sono indicati solo nei documenti di gara non deve essere valorizzata la tabella con i criteri";
                listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
              }
            } else if ("2".equals(ac_doc)) {
              this.validazioneAWCRITERIA(sqlManager, id, num, pagina, listaControlliW3ANNEXB);
            }
          } else if ("FS3".equals(form.toUpperCase()) || "FS6".equals(form.toUpperCase())) {
            this.validazioneAWCRITERIA(sqlManager, id, num, pagina, listaControlliW3ANNEXB);
          }

          // Valore stimato
          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase())) {
            pagina = UtilityTags.getResource("label.simap." + form + ".II.2.6", null, false);
            Double w3annexb_cost = (Double) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 24).getValue();
            ValidazioneUtility.validazioneCost(w3annexb_cost, "W3ANNEXB", "COST", pagina, listaControlliW3ANNEXB);
          }

          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase())) {
            pagina = UtilityTags.getResource("label.simap." + form + ".II.2.7", null, false);
            // Durata del contratto
            // Indicare la durata con una delle seguenti informazioni (mesi,
            // giorni, oppure da a)
            Long work_month = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 5).getValue();
            Long work_days = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 6).getValue();
            Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 7).getValue();
            Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 8).getValue();
            if (work_month == null && work_days == null && work_start_date == null && work_end_date == null) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_MONTH");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_DAYS");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_START_DATE");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_END_DATE");
              String messaggio = "Valorizzare il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_MONTH");
              messaggio += "\" oppure il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_DAYS");
              messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_START_DATE");
              messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_END_DATE") + "\"";
              listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

            if ((work_month != null && work_days != null)
                || ((work_month != null || work_days != null) && (work_start_date != null || work_end_date != null))) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_MONTH");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_DAYS");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_START_DATE");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_END_DATE");
              String messaggio = "Valorizzare solamente il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_MONTH");
              messaggio += "\" oppure il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_DAYS");
              messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_START_DATE");
              messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_END_DATE") + "\"";
              listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

            if (work_end_date != null && work_start_date != null) {
              if (work_end_date.getTime() <= work_start_date.getTime()) {
                String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(work_end_date, ">", work_start_date, "W3ANNEXB",
                    "WORK_START_DATE");
                ValidazioneUtility.addAvviso(listaControlliW3ANNEXB, "W3ANNEXB", "WORK_END_DATE", "E", pagina, messaggio);
              }
            }

            // Il contratto d'appalto e' oggetto di rinnovo, verificare la
            // descrizione dei rinnovi
            String renewal = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 9).getValue();
            String renewal_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 10).getValue();
            if (renewal == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "RENEWAL", pagina);
            } else if ("1".equals(renewal)) {
              if (renewal_descr == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "RENEWAL_DESCR", pagina);
              } else {
                ValidazioneUtility.validazioneS400(renewal_descr, "W3ANNEXB", "RENEWAL_DESCR", pagina, listaControlliW3ANNEXB);
              }
            }
          }

          // Limiti ai candidati (solo per FS2 e FS5)
          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase())) {
            pagina = UtilityTags.getResource("label.simap." + form + ".II.2.9", null, false);
            Long nb_env_candidate = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 17).getValue();
            Long nb_min_candidate = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 18).getValue();
            Long nb_max_candidate = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 19).getValue();
            String criteria_candidate = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 20).getValue();

            String type_procedure = (String) sqlManager.getObject("select type_procedure from w3fs2 where id = ?", new Object[] { id });
            if (type_procedure != null) {

              if (F02_PT_OPEN.equals(type_procedure) || F02_PT_OPEN_ACC.equals(type_procedure)) {
                if (nb_env_candidate != null)
                  ValidazioneUtility.addNotForeseenF02(listaControlliW3ANNEXB, "W3ANNEXB", "NB_ENV_CANDIDATE", pagina);
                if (nb_min_candidate != null)
                  ValidazioneUtility.addNotForeseenF02(listaControlliW3ANNEXB, "W3ANNEXB", "NB_MIN_CANDIDATE", pagina);
                if (nb_max_candidate != null)
                  ValidazioneUtility.addNotForeseenF02(listaControlliW3ANNEXB, "W3ANNEXB", "NB_MAX_CANDIDATE", pagina);
                if (criteria_candidate != null)
                  ValidazioneUtility.addNotForeseenF02(listaControlliW3ANNEXB, "W3ANNEXB", "CRITERIA_CANDIDATE", pagina);
              } else {
                if (nb_env_candidate != null && (nb_min_candidate != null || nb_max_candidate != null)) {
                  String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_ENV_CANDIDATE");
                  descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_MIN_CANDIDATE");
                  descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_MAX_CANDIDATE");
                  String messaggio = "Valorizzare solamente il campo \""
                      + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_ENV_CANDIDATE");
                  messaggio += "\" oppure l'intervallo di valori \""
                      + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_MIN_CANDIDATE");
                  messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_MAX_CANDIDATE") + "\"";
                  listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
                }

                if (nb_min_candidate != null && nb_max_candidate != null) {
                  if (nb_min_candidate.longValue() >= nb_max_candidate.longValue()) {
                    String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "NB_MIN_CANDIDATE");
                    String messaggio = ValidazioneUtility.getMessaggioConfrontoNumeri(nb_min_candidate, "<", nb_max_candidate, "W3ANNEXB",
                        "NB_MAX_CANDIDATE");
                    listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
                  }
                }

                if (nb_env_candidate != null || nb_min_candidate != null || nb_max_candidate != null) {
                  if (criteria_candidate == null) {
                    ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "CRITERIA_CANDIDATE", pagina);
                  }
                }
              }
            }
          }

          // Varianti ?
          if ("FS2".equals(form.toUpperCase()) || "FS5".equals(form.toUpperCase())) {
            pagina = UtilityTags.getResource("label.simap." + form + ".II.2.10", null, false);
            if (SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 15).getValue() == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "ACC_VARIANTS", pagina);
            }
          }

          // Il contratto prevede opzioni, verificare la descrizione delle
          // opzioni
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.11", null, false);
          String options = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 11).getValue();
          String options_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 12).getValue();
          if (options == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "OPTIONS", pagina);
          } else if ("1".equals(options)) {
            if (options_descr == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "OPTIONS_DESCR", pagina);
            } else {
              ValidazioneUtility.validazioneS4000(options_descr, "W3ANNEXB", "OPTIONS_DESCR", pagina, listaControlliW3ANNEXB);
            }
          }

          // Fondi europei, verificare numero di riferimento del progetto
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.13", null, false);
          String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 13).getValue();
          String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 14).getValue();
          if (eu_progr == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "EU_PROGR", pagina);
          } else if ("1".equals(eu_progr)) {
            if (eu_progr_descr == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "EU_PROGR_DESCR", pagina);
            } else {
              ValidazioneUtility.validazioneS400(eu_progr_descr, "W3ANNEXB", "EU_PROGR_DESCR", pagina, listaControlliW3ANNEXB);
            }
          }

          // Informazioni aggiuntive
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.14", null, false);
          String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 26).getValue();
          if (additional_information != null) {
            ValidazioneUtility.validazioneS400(additional_information, "W3ANNEXB", "ADDITIONAL_INFORMATION", pagina, listaControlliW3ANNEXB);
          }

          if (!listaControlliW3ANNEXB.isEmpty()) {
            if (datiW3ANNEXB.size() > 1) {
              if (lotnum == null) {
                ValidazioneUtility.setTitolo(listaControlli, "Lotto n. ...");
              } else {
                ValidazioneUtility.setTitolo(listaControlli, "Lotto n. " + lotnum.toString());
              }
            }
            for (int l = 0; l < listaControlliW3ANNEXB.size(); l++) {
              listaControlli.add(listaControlliW3ANNEXB.get(l));
            }
          }

        }
      } else {
        listaControlli.add(((Object) (new Object[] { "E", "Oggetto (II.2)", "Intera sezione", "Valorizzare i dati della sezione" })));
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazioneW3ANNEXB", e);
    }
  }

  /**
   * Gestione dei criteri.
   * 
   * @param sqlManager
   * @param id
   * @param num
   * @param pagina
   * @param listaControlliW3ANNEXB
   * @throws SQLException
   */
  public void validazioneAWCRITERIA(SqlManager sqlManager, Long id, Long num, String pagina, List<Object> listaControlliW3ANNEXB)
      throws SQLException {
    String selectW3AWCRITERIA_CNT_T = "select count(*) from w3awcriteria where id = ? and num = ? and criteria_type = ?";
    Long cnt_Q = (Long) sqlManager.getObject(selectW3AWCRITERIA_CNT_T, new Object[] { id, num, "Q" });
    Long cnt_P = (Long) sqlManager.getObject(selectW3AWCRITERIA_CNT_T, new Object[] { id, num, "P" });
    Long cnt_C = (Long) sqlManager.getObject(selectW3AWCRITERIA_CNT_T, new Object[] { id, num, "C" });
    if (cnt_Q == null) cnt_Q = new Long(0);
    if (cnt_P == null) cnt_P = new Long(0);
    if (cnt_C == null) cnt_C = new Long(0);

    if (cnt_P.longValue() > 1) {
      String descrizione = "Criteri di aggiudicazione";
      String messaggio = "E' possibile indicare un solo criterio di tipo \"Prezzo\"";
      listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }

    if (cnt_P.longValue() > 0 && cnt_C.longValue() > 0) {
      String descrizione = "Criteri di aggiudicazione";
      String messaggio = "E' possibile indicare il solo criterio di tipo \"Prezzo\" oppure una lista di criteri di tipo \"Costo\"";
      listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }

    if (cnt_P.longValue() == 0 && cnt_C.longValue() == 0) {
      String descrizione = "Criteri di aggiudicazione";
      String messaggio = "Deve essere indicato un criterio di tipo \"Prezzo\" oppure una lista di criteri di tipo \"Costo\"";
      listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }

    /** Nuovi controlli del 31/05/2019 **/
    if (cnt_C.longValue() > 20) {
      String descrizione = "Criteri di aggiudicazione";
      String messaggio = "E' possibile indicare al massimo venti (20) criteri di tipo \"Costo\"";
      listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }
    
    if (cnt_Q.longValue() > 20) {
      String descrizione = "Criteri di aggiudicazione";
      String messaggio = "E' possibile indicare al massimo venti (20) criteri di tipo \"Qualit&agrave;\"";
      listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }
    
    String selectW3AWCRITERIA = "select acnum, " // 0
        + " criteria_type, " // 1
        + " criteria, " // 2
        + " weighting " // 3
        + " from w3awcriteria where id = ? and num = ?";
    List<?> datiW3AWCRITERIA = sqlManager.getListVector(selectW3AWCRITERIA, new Object[] { id, num });
    if (datiW3AWCRITERIA != null && datiW3AWCRITERIA.size() > 0) {
      for (int iAWC = 0; iAWC < datiW3AWCRITERIA.size(); iAWC++) {
        String criteria_type = (String) SqlManager.getValueFromVectorParam(datiW3AWCRITERIA.get(iAWC), 1).getValue();
        String criteria = (String) SqlManager.getValueFromVectorParam(datiW3AWCRITERIA.get(iAWC), 2).getValue();
        Long weighting = (Long) SqlManager.getValueFromVectorParam(datiW3AWCRITERIA.get(iAWC), 3).getValue();

        if (criteria_type == null)
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3AWCRITERIA", "CRITERIA_TYPE", pagina);

        if (criteria_type != null && ("Q".equals(criteria_type) || "C".equals(criteria_type))) {
          if (criteria == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3AWCRITERIA", "CRITERIA", pagina);
          } else {
            ValidazioneUtility.validazioneS500(criteria, "W3AWCRITERIA", "CRITERIA", pagina, listaControlliW3ANNEXB);
          }
          if (weighting == null) ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3AWCRITERIA", "WEIGHTING", pagina);
        }

        if (criteria_type != null && "P".equals(criteria_type)) {
          if (cnt_Q == 0) {
            if (weighting != null) {
              String descrizione = "Criteri di aggiudicazione";
              String messaggio = "Se non &egrave; indicato alcun criterio di tipo \"Qualit&agrave;\" non &egrave; possibile indicare la \"Ponderazione\" sul criterio di tipo \"Prezzo\"";
              listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }
          } else {
            if (weighting == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3AWCRITERIA", "WEIGHTING", pagina);
            }
          }
        }
      }
    }

  }

}