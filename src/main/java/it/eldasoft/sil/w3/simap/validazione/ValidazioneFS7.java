package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ValidazioneFS7 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS7.class);

  /**
   * Controllo dati formulario standard 5: bando di gara.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS7(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS7: inizio metodo");

    try {

      // Tipo di preinformazione
      String qsu_call_competition = (String) sqlManager.getObject("select qsu_call_competition from w3fs7 where id = ?",
          new Object[] { id });
      if (qsu_call_competition == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS7", "QSU_CALL_COMPETITION", "");
      }

      this.validazioneFS7_Sezione_I(sqlManager, id, listaControlli);
      this.validazioneFS7_Sezione_II(sqlManager, id, listaControlli);
      this.validazioneW3ANNEXB_FS7(sqlManager, id, listaControlli);
      this.validazioneFS7_Sezione_III(sqlManager, id, listaControlli);
      this.validazioneFS7_Sezione_IV(sqlManager, id, listaControlli);
      this.validazioneFS7_Sezione_VI(sqlManager, id, listaControlli);
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative al sistema di qualificazione", "validazioneFS7", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS7: fine metodo");
  }

  /**
   * Sezione I: amministrazione
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  public void validazioneFS7_Sezione_I(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

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
        if (legal_basis == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "LEGAL_BASIS", "");
        }

        // Amministrazione aggiudicatrice, controllo del collegamento e dei dati
        // dell'archivio W3AMMI e UFFINT
        pagina = UtilityTags.getResource("label.simap." + form + ".I.1", null, false);
        String codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 1).getValue();
        if (codamm == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "CODAMM", pagina);
        } else {
          validazioneW3AMMI(sqlManager, form, codamm, listaControlli, "CE");
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
        pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Disponibilita' dei documenti di gara";
        Long document_fu_re = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAP, 10).getValue();
        String document_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 8).getValue();

        String qsu_call_competition = (String) sqlManager.getObject("select qsu_call_competition from w3fs7 where id = ?",
            new Object[] { id });
        if (qsu_call_competition != null) {
          if ("1".equals(qsu_call_competition)) {
            if (document_fu_re == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "DOCUMENT_FU_RE", pagina);
            } else {
              if (document_url == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "DOCUMENT_URL", pagina);
              } else {
                ValidazioneUtility.validazioneURL(document_url, "W3SIMAP", "DOCUMENT_URL", pagina, listaControlli);
              }
            }
          } else if (("2".equals(qsu_call_competition))) {
            if (document_fu_re != null) {
              ValidazioneUtility.addNotForeseenF07(listaControlli, "W3SIMAP", "DOCUMENT_FU_RE", pagina);
            }
            if (document_url != null) {
              ValidazioneUtility.addNotForeseenF07(listaControlli, "W3SIMAP", "DOCUMENT_URL", pagina);
            }
          }
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

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazione_Sezione_I", e);
    }

  }

  /**
   * Sezione II - Oggetto.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS7_Sezione_II(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS7 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " type_contract " // 3
        + " from w3fs7 "
        + " where w3fs7.id = ?";

    List<?> datiFS7 = sqlManager.getVector(selectFS7, new Object[] { id });

    String pagina = "";

    // Denominazione
    pagina = UtilityTags.getResource("label.simap.fs7.II.1.1", null, false);
    String title_contract = (String) SqlManager.getValueFromVectorParam(datiFS7, 0).getValue();
    if (title_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS7", "TITLE_CONTRACT", pagina);
    } else {
      ValidazioneUtility.validazioneS200(title_contract, "W3FS7", "TITLE_CONTRACT", pagina, listaControlli);
    }

    // Numero di riferimento
    String reference = (String) SqlManager.getValueFromVectorParam(datiFS7, 1).getValue();
    if (reference != null) {
      ValidazioneUtility.validazioneS100(reference, "W3FS7", "REFERENCE", pagina, listaControlli);
    }

    String type_contract = (String) SqlManager.getValueFromVectorParam(datiFS7, 3).getValue();

    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs7.II.1.2", null, false);
    String cpv = (String) SqlManager.getValueFromVectorParam(datiFS7, 2).getValue();
    if (cpv == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS7", "CPV", pagina);
    }

    // Controllo corrispondenza CPV con il tipo appalto
    ValidazioneUtility.validazioneCorrispondenzaCPV(listaControlli, pagina, cpv, type_contract, "W3FS7", "CPV");

    // Tipo di appalto
    pagina = UtilityTags.getResource("label.simap.fs7.II.1.3", null, false);
    if (type_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS7", "TYPE_CONTRACT", pagina);
    }

  }

  /**
   * Sezione III - Informazioni giuridiche...
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS7_Sezione_III(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS7 = "select w3fs7.service_res_desc, " // 0
        + " w3fs7.performance_conditions " // 1
        + " from w3fs7 "
        + " where w3fs7.id = ?";
    List<?> datiFS7 = sqlManager.getVector(selectFS7, new Object[] { id });

    String pagina = "";

    pagina = UtilityTags.getResource("label.simap.fs7.III.2.1", null, false);
    String service_res_desc = (String) SqlManager.getValueFromVectorParam(datiFS7, 1).getValue();
    ValidazioneUtility.validazioneS1500(service_res_desc, "W3FS7", "SERVICE_RES_DESC", pagina, listaControlli);

    pagina = UtilityTags.getResource("label.simap.fs7.III.2.2", null, false);
    String performance_conditions = (String) SqlManager.getValueFromVectorParam(datiFS7, 1).getValue();
    ValidazioneUtility.validazioneS1000(performance_conditions, "W3FS7", "PERFORMANCE_CONDITIONS", pagina, listaControlli);

  }

  /**
   * Sezione IV - Procedura.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS7_Sezione_IV(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS7 = "select use_electronic, " // 0
        + " notice_number_oj " // 1
        + " from w3fs7 "
        + " where id = ? ";

    List<?> datiFS7 = sqlManager.getVector(selectFS7, new Object[] { id });

    String pagina = "";

    // Tipo di procedura
    pagina = UtilityTags.getResource("label.simap.fs7.IV.1.6", null, false);
    String use_electronic = (String) SqlManager.getValueFromVectorParam(datiFS7, 0).getValue();
    if (use_electronic == null) {
      ValidazioneUtility.validazioneS400(use_electronic, "W3FS7", "USE_ELECTRONIC", pagina, listaControlli);
    }

    // Pubblicazione precedente
    pagina = UtilityTags.getResource("label.simap.fs7.IV.2.1", null, false);
    String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiFS7, 1).getValue();
    if (notice_number_oj != null) {
      ValidazioneUtility.validazioneNumberOJ(notice_number_oj, "W3FS7", "NOTICE_NUMBER_OJ", pagina, listaControlli);
    }

    // Lingue
    pagina = UtilityTags.getResource("label.simap.fs7.IV.2.4", null, false);
    String selectW3LANGUAGE = "select count(*) from w3language where id = ?";
    Long cnt_W3LANGUAGE = (Long) sqlManager.getObject(selectW3LANGUAGE, new Object[] { id });
    if (cnt_W3LANGUAGE == null || (cnt_W3LANGUAGE != null && cnt_W3LANGUAGE.longValue() == 0)) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3LANGUAGE", "LANGUAGE_EC", pagina);
    }

  }

  /**
   * Sezione VI - Altre informazioni
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS7_Sezione_VI(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS7 = "select review_body_codein, " // 0
        + " mediation_body_codein, " // 1
        + " review_procedure, " // 2
        + " review_info_codein, " // 3
        + " notice_date " // 4
        + " from w3fs7 where w3fs7.id = ?";

    List<?> datiFS7 = sqlManager.getVector(selectFS7, new Object[] { id });

    String pagina = "";

    // Organismo responsabile delle procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs7.VI.4.1", null, false);
    String review_body_codein = (String) SqlManager.getValueFromVectorParam(datiFS7, 0).getValue();
    if (review_body_codein == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS7", "REVIEW_BODY_CODEIN", pagina);
    } else {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, review_body_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

    // Organismo responsabile delle procedure di mediazione
    pagina = UtilityTags.getResource("label.simap.fs7.VI.4.2", null, false);
    String mediation_body_codein = (String) SqlManager.getValueFromVectorParam(datiFS7, 1).getValue();
    if (mediation_body_codein != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, mediation_body_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

    // Informazioni dettagliate sui termini di presentazione dei ricorsi
    pagina = UtilityTags.getResource("label.simap.fs7.VI.4.3", null, false);
    String review_procedure = (String) SqlManager.getValueFromVectorParam(datiFS7, 2).getValue();
    ValidazioneUtility.validazioneS4000(review_procedure, "W3FS7", "REVIEW_PROCEDURE", pagina, listaControlli);

    // Servizio presso il quale sono disponibili...
    pagina = UtilityTags.getResource("label.simap.fs7.VI.4.4", null, false);
    String review_info_codein = (String) SqlManager.getValueFromVectorParam(datiFS7, 3).getValue();
    if (review_info_codein != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, review_info_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
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
  private void validazioneW3ANNEXB_FS7(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {

    String selectW3ANNEXB = "select w3annexb.num, " // 0
        + " w3annexb.lotnum, " // 1
        + " w3annexb.site_nuts, " // 2
        + " w3annexb.description, " // 3
        + " w3annexb.ac_doc, " // 4
        + " w3annexb.q_date_start, " // 5
        + " w3annexb.q_date_stop, " // 6
        + " w3annexb.q_indefinite, " // 7
        + " w3annexb.q_renewal, " // 8
        + " w3annexb.q_renewal_descr, " // 9
        + " w3annexb.eu_progr, " // 10
        + " w3annexb.eu_progr_descr, " // 11
        + " w3annexb.site_nuts_2, " // 12
        + " w3annexb.site_nuts_3, " // 13
        + " w3annexb.site_nuts_4, " // 14
        + " w3annexb.site_label, " // 15
        + " w3annexb.additional_information " // 15
        + " from w3annexb where w3annexb.id = ? "
        + " and w3annexb.num = 1";

    String selectW3CPV = "select numcpv, cpv from w3cpv where ent = ? and id = ? and num = ?";
    String selectW3AWCRITERIA_CNT = "select count(*) from w3awcriteria where id = ? and num = ?";

    try {

      String form = "fs7";

      List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id });
      if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

        Long num = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
        Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();

        String pagina = UtilityTags.getResource("label.simap." + form + ".II.2.1", null, false);

        // Numero del lotto
        if (lotnum == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "LOTNUM", pagina);
        }

        // Controllo CPV aggiuntivi, se esiste l'occorrenza in W3CPV diventa
        // obbligatorio indicare il CPV del vocabolario principale
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.2", null, false);
        List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { "W3ANNEXB", id, num });
        if (datiW3CPV != null && datiW3CPV.size() > 0) {
          for (int iCPV = 0; iCPV < datiW3CPV.size(); iCPV++) {
            String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(iCPV), 1).getValue();
            if (cpv == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3CPV", "CPV", pagina);
            }
          }
        }

        // Codice NUTS
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.3", null, false);
        String site_nuts = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 2).getValue();
        if (site_nuts == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "SITE_NUTS", pagina);
        } else {
          validazioneNUTS(sqlManager, site_nuts, "W3ANNEXB", "SITE_NUTS", pagina, listaControlli);
        }

        String site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 12).getValue();
        String site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
        String site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 14).getValue();
        if (site_nuts_2 != null) {
          validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_2", pagina, listaControlli);
        }
        if (site_nuts_3 != null) {
          validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_3", pagina, listaControlli);
        }
        if (site_nuts_4 != null) {
          validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_4", pagina, listaControlli);
        }

        // Sito o luogo principale
        String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 15).getValue();
        ValidazioneUtility.validazioneS200(site_label, "W3ANNEXB", "SITE_LABEL", pagina, listaControlli);

        // Descrizione dell'appalto
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.4", null, false);
        String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 3).getValue();
        if (description == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "DESCRIPTION", pagina);
        } else {
          ValidazioneUtility.validazioneS4000(description, "W3ANNEXB", "DESCRIPTION", pagina, listaControlli);
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
        String ac_doc = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 4).getValue();
        if (ac_doc == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "AC_DOC", pagina);
        } else if ("1".equals(ac_doc)) {
          Long cnt = (Long) sqlManager.getObject(selectW3AWCRITERIA_CNT, new Object[] { id, num, });
          if (cnt != null && cnt.longValue() > 0) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "AC_DOC");
            String messaggio = "Se i criteri di aggiudicazione sono indicati solo nei documenti di gara non deve essere valorizzata la tabella con i criteri";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
        } else if ("2".equals(ac_doc)) {
          this.validazioneAWCRITERIA(sqlManager, id, num, pagina, listaControlli);
        }

        // Durata del sistema di qualificazione
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.8", null, false);
        // Durata del contratto
        // Indicare la durata con una delle seguenti informazioni (mesi,
        // giorni, oppure da a)
        Date q_date_start = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 5).getValue();
        Date q_date_stop = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
        String q_indefinite = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
        if (q_date_start == null && q_date_stop == null && q_indefinite == null) {
          String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_START");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_STOP");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_INDEFINITE");
          String messaggio = "Valorizzare il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_INDEFINITE");
          messaggio += "\" oppure l'intervallo di date \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_START");
          messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_STOP") + "\"";
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
        }

        if ((q_date_start != null || q_date_stop != null) && (q_indefinite != null && "1".equals(q_indefinite))) {
          String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_START");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_STOP");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_INDEFINITE");
          String messaggio = "E' possibile impostare il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_INDEFINITE");
          messaggio += "\" a \"Si\" oppure l'intervallo di date \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_START");
          messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "Q_DATE_STOP") + "\"";
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
        }

        if (q_date_start != null && q_date_stop != null) {
          if (q_date_stop.getTime() <= q_date_start.getTime()) {
            String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(q_date_stop, ">", q_date_start, "W3ANNEXB", "Q_DATE_START");
            ValidazioneUtility.addAvviso(listaControlli, "W3ANNEXB", "Q_DATE_STOP", "E", pagina, messaggio);
          }
        }

        // Il contratto d'appalto e' oggetto di rinnovo, verificare la
        // descrizione dei rinnovi
        String q_renewal = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
        String q_renewal_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();
        if (q_renewal == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "Q_RENEWAL", pagina);
        } else if ("1".equals(q_renewal)) {
          if (q_renewal_descr == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "Q_RENEWAL_DESCR", pagina);
          } else {
            ValidazioneUtility.validazioneS400(q_renewal_descr, "W3ANNEXB", "Q_RENEWAL_DESCR", pagina, listaControlli);
          }
        }

        // Fondi europei, verificare numero di riferimento del progetto
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.13", null, false);
        String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
        String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
        if (eu_progr == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "EU_PROGR", pagina);
        } else if ("1".equals(eu_progr)) {
          if (eu_progr_descr == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "EU_PROGR_DESCR", pagina);
          } else {
            ValidazioneUtility.validazioneS400(eu_progr_descr, "W3ANNEXB", "EU_PROGR_DESCR", pagina, listaControlli);
          }
        }

      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative ai lotti", "validazioneW3ANNEXB", e);
    }
  }

}