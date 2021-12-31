package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.utility.UtilityDate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ValidazioneFS2 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS2.class);

  /**
   * Controllo dati formulario standard 2: bando di gara.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS2(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS2: inizio metodo");

    try {
      this.validazione_Sezione_I(sqlManager, id, listaControlli);
      this.validazioneFS2_Sezione_II(sqlManager, id, listaControlli);
      this.validazioneNumeroMassimoLotti(sqlManager, id, listaControlli);
      this.validazioneW3ANNEXB(sqlManager, id, null, listaControlli);
      this.validazioneFS2_Sezione_III(sqlManager, id, listaControlli);
      this.validazioneFS2_Sezione_IV(sqlManager, id, listaControlli);
      this.validazioneFS2_Sezione_VI(sqlManager, id, listaControlli);
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative al bando di gara", "validazioneFS2", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS2: fine metodo");
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
  private void validazioneFS2_Sezione_II(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS2 = "select title_contract, " // 0
        + " cpv, " // 1
        + " type_contract, " // 2
        + " scope_total, " // 3
        + " div_into_lots, " // 4
        + " div_lots_value, " // 5
        + " div_lots_max, " // 6
        + " lots_max_tenderer, " // 7
        + " scope_cost, " // 8
        + " reference, " // 9
        + " lots_combining" // 10
        + " from w3fs2 "
        + " where w3fs2.id = ?";

    List<?> datiFS2 = sqlManager.getVector(selectFS2, new Object[] { id });

    String pagina = "";

    // Denominazione
    pagina = UtilityTags.getResource("label.simap.fs2.II.1.1", null, false);
    String title_contract = (String) SqlManager.getValueFromVectorParam(datiFS2, 0).getValue();
    if (title_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "TITLE_CONTRACT", pagina);
    } else {
      ValidazioneUtility.validazioneS200(title_contract, "W3FS2", "TITLE_CONTRACT", pagina, listaControlli);
    }

    // Numero di riferimento
    String reference = (String) SqlManager.getValueFromVectorParam(datiFS2, 9).getValue();
    if (reference != null) {
      ValidazioneUtility.validazioneS100(reference, "W3FS2", "REFERENCE", pagina, listaControlli);
    }

    String type_contract = (String) SqlManager.getValueFromVectorParam(datiFS2, 2).getValue();

    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs2.II.1.2", null, false);
    String cpv = (String) SqlManager.getValueFromVectorParam(datiFS2, 1).getValue();
    if (cpv == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "CPV", pagina);
    }

    // Controllo corrispondenza CPV con il tipo appalto
    ValidazioneUtility.validazioneCorrispondenzaCPV(listaControlli, pagina, cpv, type_contract, "W3FS2", "CPV");

    // Tipo di appalto
    pagina = UtilityTags.getResource("label.simap.fs2.II.1.3", null, false);
    if (type_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "TYPE_CONTRACT", pagina);
    }

    // Breve descrizione
    pagina = UtilityTags.getResource("label.simap.fs2.II.1.4", null, false);
    String scope_total = (String) SqlManager.getValueFromVectorParam(datiFS2, 3).getValue();
    if (scope_total == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "SCOPE_TOTAL", pagina);
    } else {
      ValidazioneUtility.validazioneS1000(reference, "W3FS2", "SCOPE_TOTAL", pagina, listaControlli);
    }

    // Valore totale stimato
    pagina = UtilityTags.getResource("label.simap.fs2.II.1.5", null, false);
    Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiFS2, 8).getValue();
    ValidazioneUtility.validazioneCost(scope_cost, "W3FS2", "SCOPE_COST", pagina, listaControlli);

    // Appalto suddiviso in lotti ?
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.6", null, false);
    String div_into_lots = (String) SqlManager.getValueFromVectorParam(datiFS2, 4).getValue();
    if (div_into_lots == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "DIV_INTO_LOTS", pagina);
    } else if ("1".equals(div_into_lots)) {
      // Le offerte vanno presentare per tutti i lotti, solo ...
      String div_lots_value = (String) SqlManager.getValueFromVectorParam(datiFS2, 5).getValue();
      if (div_lots_value == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "DIV_LOTS_VALUE", pagina);
      }

      // Numero massimo di lotti su cui presentare le offerte
      if (div_lots_value != null && "2".equals(div_lots_value)) {
        Long div_lots_max = (Long) SqlManager.getValueFromVectorParam(datiFS2, 6).getValue();
        if (div_lots_max == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "DIV_LOTS_MAX", pagina);
        } else {
          Long cntW3ANNEXB = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ?", new Object[] { id });
          if (cntW3ANNEXB != null && div_lots_max.longValue() > cntW3ANNEXB.longValue()) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS2", "DIV_LOTS_MAX");
            String messaggio = "Il numero di lotti indicato deve essere inferiore o uguale al numero totale dei lotti";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
          
          if (div_lots_max.longValue() <= 0) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS2", "DIV_LOTS_MAX");
            String messaggio = "Il numero di lotti indicato deve essere maggiore di zero";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
          
        }
      }

      // Numero massimo di lotti aggiudicabili ad un singolo offerente
      Long lots_max_tenderer = (Long) SqlManager.getValueFromVectorParam(datiFS2, 7).getValue();
      if (lots_max_tenderer == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "LOTS_MAX_TENDERER", pagina);
      } else {
        Long cntW3ANNEXB = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ?", new Object[] { id });
        if (cntW3ANNEXB != null && lots_max_tenderer.longValue() > cntW3ANNEXB.longValue()) {
          String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS2", "LOTS_MAX_TENDERER");
          String messaggio = "Il numero di lotti indicato deve essere inferiore o uguale al numero totale dei lotti";
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
        }
        
        if (lots_max_tenderer.longValue() <= 0) {
          String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS2", "LOTS_MAX_TENDERER");
          String messaggio = "Il numero di lotti indicato deve essere maggiore di zero";
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
        }
        
      }

      // L'amministrazione aggiudicatrice si riserva la facoltà di aggiudicare i
      // contratti d'appalto combinando i seguenti lotti o gruppi di lotti
      String lots_combining = (String) SqlManager.getValueFromVectorParam(datiFS2, 10).getValue();
      if (lots_combining != null) {
        ValidazioneUtility.validazioneS400(lots_combining, "W3FS2", "LOTS_COMBINING", pagina, listaControlli);
      }

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
  private void validazioneFS2_Sezione_III(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS2 = "select w3fs2s36.eco_criteria_doc, " // 0
        + " w3fs2s36.eaf_capacity_information, " // 1
        + " w3fs2s36.eaf_capacity_min_level, " // 2
        + " w3fs2s36.tech_crit_doc, " // 3
        + " w3fs2s36.t_capacity_information, " // 4
        + " w3fs2s36.t_capacity_min_level, " // 5
        + " w3fs2s36.operators_personal_situation, " // 6
        + " w3fs2s36.service_res_desc, " // 7
        + " w3fs2s36.performance_conditions " // 8
        + " from w3fs2, w3fs2s36 "
        + " where w3fs2.id = ? and w3fs2.id = w3fs2s36.id";
    List<?> datiFS2 = sqlManager.getVector(selectFS2, new Object[] { id });

    String pagina = "";

    // Controllo dimensione massime
    String operators_personal_situation = (String) SqlManager.getValueFromVectorParam(datiFS2, 6).getValue();
    pagina = UtilityTags.getResource("label.simap.fs2.III.1.1", null, false);
    ValidazioneUtility.validazioneS4000(operators_personal_situation, "W3FS2S36", "OPERATORS_PERSONAL_SITUATION", pagina, listaControlli);

    String eaf_capacity_information = (String) SqlManager.getValueFromVectorParam(datiFS2, 1).getValue();
    String eaf_capacity_min_level = (String) SqlManager.getValueFromVectorParam(datiFS2, 2).getValue();
    pagina = UtilityTags.getResource("label.simap.fs2.III.1.2", null, false);
    ValidazioneUtility.validazioneS4000(eaf_capacity_information, "W3FS2S36", "EAF_CAPACITY_INFORMATION", pagina, listaControlli);
    ValidazioneUtility.validazioneS4000(eaf_capacity_min_level, "W3FS2S36", "EAF_CAPACITY_MIN_LEVEL", pagina, listaControlli);

    String t_capacity_information = (String) SqlManager.getValueFromVectorParam(datiFS2, 4).getValue();
    String t_capacity_min_level = (String) SqlManager.getValueFromVectorParam(datiFS2, 5).getValue();
    pagina = UtilityTags.getResource("label.simap.fs2.III.1.3", null, false);
    ValidazioneUtility.validazioneS4000(t_capacity_information, "W3FS2S36", "T_CAPACITY_INFORMATION", pagina, listaControlli);
    ValidazioneUtility.validazioneS4000(t_capacity_min_level, "W3FS2S36", "T_CAPACITY_MIN_LEVEL", pagina, listaControlli);

    pagina = UtilityTags.getResource("label.simap.fs2.III.2.1", null, false);
    String service_res_desc = (String) SqlManager.getValueFromVectorParam(datiFS2, 7).getValue();
    ValidazioneUtility.validazioneS1500(service_res_desc, "W3FS2S36", "SERVICE_RES_DESC", pagina, listaControlli);

    pagina = UtilityTags.getResource("label.simap.fs2.III.2.2", null, false);
    String performance_conditions = (String) SqlManager.getValueFromVectorParam(datiFS2, 8).getValue();
    ValidazioneUtility.validazioneS1000(performance_conditions, "W3FS2S36", "PERFORMANCE_CONDITIONS", pagina, listaControlli);

    // Capacita' economica e finanziaria
    pagina = UtilityTags.getResource("label.simap.fs2.III.1.2", null, false);
    String eco_criteria_doc = (String) SqlManager.getValueFromVectorParam(datiFS2, 0).getValue();
    if (eco_criteria_doc != null && "2".equals(eco_criteria_doc)) {
      if (SqlManager.getValueFromVectorParam(datiFS2, 1).getValue() == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "EAF_CAPACITY_INFORMATION", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS2, 2).getValue() == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "EAF_CAPACITY_MIN_LEVEL", pagina);
      }
    }

    // Capacita' professionale e tecnica
    pagina = UtilityTags.getResource("label.simap.fs2.III.1.3", null, false);
    String tech_crit_doc = (String) SqlManager.getValueFromVectorParam(datiFS2, 3).getValue();
    if (tech_crit_doc != null && "2".equals(tech_crit_doc)) {
      if (SqlManager.getValueFromVectorParam(datiFS2, 4).getValue() == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "T_CAPACITY_INFORMATION", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS2, 5).getValue() == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "T_CAPACITY_MIN_LEVEL", pagina);
      }
    }
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
  private void validazioneFS2_Sezione_IV(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS2 = "select type_procedure, " // 0
        + " accelerated, " // 1
        + " framework, " // 2
        + " fr_several_op, " // 3
        + " gpa, " // 4
        + " receipt_limit_date, " // 5
        + " opening_tenders_date, " // 6
        + " opening_tenders_time, " // 7
        + " notice_number_oj, " // 8
        + " frame_justification, " // 9
        + " dps, " // 10
        + " reduction, " // 11
        + " right_contract, " // 12
        + " dispatch_invitations_date, " // 13
        + " receipt_limit_time, " // 14
        + " notice_date, " // 15
        + " use_electronic, " // 16
        + " opening_tenders_place, " // 17
        + " authorised_persons_desc " // 18
        + " from w3fs2 "
        + " where id = ? ";

    List<?> datiFS2 = sqlManager.getVector(selectFS2, new Object[] { id });

    String pagina = "";

    // Tipo di procedura
    pagina = UtilityTags.getResource("label.simap.fs2.IV.1.1", null, false);
    String type_procedure = (String) SqlManager.getValueFromVectorParam(datiFS2, 0).getValue();
    if (type_procedure == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "TYPE_PROCEDURE", pagina);
    } else if ("3".equals(type_procedure) || "5".equals(type_procedure) || "7".equals(type_procedure)) {
      String accelerated = (String) SqlManager.getValueFromVectorParam(datiFS2, 1).getValue();
      if (accelerated == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "ACCELERATED", pagina);
      } else {
        ValidazioneUtility.validazioneS1000(accelerated, "W3FS2", "ACCELERATED", pagina, listaControlli);
      }
    }

    // Numero di operatori nel caso di accordo quadro, sistema dinamico di
    // acquisizione
    pagina = UtilityTags.getResource("label.simap.fs2.IV.1.3", null, false);
    String framework = (String) SqlManager.getValueFromVectorParam(datiFS2, 2).getValue();
    String fr_several_op = (String) SqlManager.getValueFromVectorParam(datiFS2, 3).getValue();
    String frame_justification = (String) SqlManager.getValueFromVectorParam(datiFS2, 9).getValue();
    String dps = (String) SqlManager.getValueFromVectorParam(datiFS2, 10).getValue();
    if (F02_PT_RESTRICTED.equals(type_procedure) || F02_PT_RESTRICTED_ACC.equals(type_procedure)) {
      if (framework != null && "1".equals(framework)) {
        if (fr_several_op == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "FR_SEVERAL_OP", pagina);
        }
      }
    } else {
      if (dps != null) ValidazioneUtility.addNotForeseenF02(listaControlli, "W3FS2", "DPS", pagina);
    }
    ValidazioneUtility.validazioneS400(frame_justification, "W3FS2", "FRAME_JUSTIFICATION", pagina, listaControlli);

    // Informazioni sulla riduzione delle offerte
    pagina = UtilityTags.getResource("label.simap.fs2.IV.1.4", null, false);
    String reduction = (String) SqlManager.getValueFromVectorParam(datiFS2, 11).getValue();
    if (F02_PT_OPEN.equals(type_procedure)
        || F02_PT_OPEN_ACC.equals(type_procedure)
        || F02_PT_RESTRICTED.equals(type_procedure)
        || F02_PT_RESTRICTED_ACC.equals(type_procedure)) {
      if (reduction != null) ValidazioneUtility.addNotForeseenF02(listaControlli, "W3FS2", "REDUCTION", pagina);
    }

    // Informazioni sulla negoziazione
    pagina = UtilityTags.getResource("label.simap.fs2.IV.1.5", null, false);
    String right_contract = (String) SqlManager.getValueFromVectorParam(datiFS2, 12).getValue();
    if (F02_PT_NEGOTIATION.equals(type_procedure) || F02_PT_NEGOTIATION_ACC.equals(type_procedure)) {

    } else {
      if (right_contract != null) ValidazioneUtility.addNotForeseenF02(listaControlli, "W3FS2", "RIGHT_CONTRACT", pagina);
    }

    // Ulteriori informazioni sull'asta elettronica
    pagina = UtilityTags.getResource("label.simap.fs2.IV.1.6", null, false);
    String use_electronic = (String) SqlManager.getValueFromVectorParam(datiFS2, 16).getValue();
    ValidazioneUtility.validazioneS400(use_electronic, "W3FS2", "USE_ELECTRONIC", pagina, listaControlli);

    // Accordo sugli appalti pubblici (AAP)
    pagina = UtilityTags.getResource("label.simap.fs2.IV.1.8", null, false);
    if (SqlManager.getValueFromVectorParam(datiFS2, 4).getValue() == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "GPA", pagina);
    }

    // Pubblicazione precedente
    pagina = UtilityTags.getResource("label.simap.fs2.IV.2.1", null, false);
    String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiFS2, 8).getValue();
    if (notice_number_oj != null) {
      ValidazioneUtility.validazioneNumberOJ(notice_number_oj, "W3FS2", "NOTICE_NUMBER_OJ", pagina, listaControlli);
    }

    // Termine ultimo per la ricezione delle offerte
    pagina = UtilityTags.getResource("label.simap.fs2.IV.2.2", null, false);
    Date receipt_limit_date = (Date) SqlManager.getValueFromVectorParam(datiFS2, 5).getValue();
    if (receipt_limit_date == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "RECEIPT_LIMIT_DATE", pagina);
    } else {
      // Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiFS2,
      // 15).getValue();
      Date notice_date = UtilityDate.getDataOdiernaAsDate();
      if (notice_date != null) {
        if (receipt_limit_date.getTime() <= notice_date.getTime()) {
          String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(receipt_limit_date, ">", notice_date, "W3FS2", "NOTICE_DATE");
          ValidazioneUtility.addAvviso(listaControlli, "W3FS2", "RECEIPT_LIMIT_DATE", "E", pagina, messaggio);
        }
      } else {
        Date notice_date_today = new Date();
        if (receipt_limit_date.getTime() <= notice_date_today.getTime()) {
          String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(receipt_limit_date, ">", notice_date_today, "W3FS2",
              "NOTICE_DATE");
          ValidazioneUtility.addAvviso(listaControlli, "W3FS2", "RECEIPT_LIMIT_DATE", "E", pagina, messaggio);
        }
      }
    }

    if (SqlManager.getValueFromVectorParam(datiFS2, 14).getValue() == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "RECEIPT_LIMIT_TIME", pagina);
    }

    // Data stimata di spedizione ai candidati
    pagina = UtilityTags.getResource("label.simap.fs2.IV.2.3", null, false);
    Date dispatch_invitations_date = (Date) SqlManager.getValueFromVectorParam(datiFS2, 13).getValue();
    if (F02_PT_OPEN.equals(type_procedure) || F02_PT_OPEN_ACC.equals(type_procedure)) {
      if (dispatch_invitations_date != null)
        ValidazioneUtility.addNotForeseenF02(listaControlli, "W3FS2", "DISPATCH_INVITATIOS_DATE", pagina);
    }

    // Lingue
    pagina = UtilityTags.getResource("label.simap.fs2.IV.2.4", null, false);
    String selectW3LANGUAGE = "select count(*) from w3language where id = ?";
    Long cnt_W3LANGUAGE = (Long) sqlManager.getObject(selectW3LANGUAGE, new Object[] { id });
    if (cnt_W3LANGUAGE == null || (cnt_W3LANGUAGE != null && cnt_W3LANGUAGE.longValue() == 0)) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3LANGUAGE", "LANGUAGE_EC", pagina);
    }

    // Termine ultimo per la ricezione delle offerte
    pagina = UtilityTags.getResource("label.simap.fs2.IV.2.7", null, false);
    Date opening_tenders_date = (Date) SqlManager.getValueFromVectorParam(datiFS2, 6).getValue();
    String opening_tenders_time = (String) SqlManager.getValueFromVectorParam(datiFS2, 7).getValue();

    if (F02_PT_OPEN.equals(type_procedure) || F02_PT_OPEN_ACC.equals(type_procedure)) {
      if (opening_tenders_date == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "OPENING_TENDERS_DATE", pagina);
      }
      if (opening_tenders_time == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2", "OPENING_TENDERS_TIME", pagina);
      }
    } else {
      if (opening_tenders_date != null) ValidazioneUtility.addNotForeseenF02(listaControlli, "W3FS2", "OPENING_TENDERS_DATE", pagina);
      if (opening_tenders_time != null) ValidazioneUtility.addNotForeseenF02(listaControlli, "W3FS2", "OPENING_TENDERS_TIME", pagina);
    }

    // Modalita' di apertura offerte
    pagina = UtilityTags.getResource("label.simap.fs2.IV.2.7", null, false);
    String opening_tenders_place = (String) SqlManager.getValueFromVectorParam(datiFS2, 17).getValue();
    String authorised_persons_desc = (String) SqlManager.getValueFromVectorParam(datiFS2, 18).getValue();
    ValidazioneUtility.validazioneS400(opening_tenders_place, "W3FS2", "OPENING_TENDERS_PLACE", pagina, listaControlli);
    ValidazioneUtility.validazioneS400(authorised_persons_desc, "W3FS2", "AUTHORISED_PERSONS_DESC", pagina, listaControlli);

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
  private void validazioneFS2_Sezione_VI(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS2 = "select w3fs2s36.recurrent_proc, " // 0
        + " w3fs2s36.recurrent_proc_desc, " // 1
        + " w3fs2s36.appeal_procedure_codein, " // 2
        + " w3fs2s36.mediation_procedure_codein, " // 3
        + " w3fs2s36.appeal_service_codein, " // 4
        + " w3fs2.notice_date, " // 5
        + " w3fs2s36.additional_information, " // 6
        + " w3fs2s36.appeal_precision " // 7
        + " from w3fs2, w3fs2s36 where w3fs2.id = ? and w3fs2s36.id = w3fs2.id";

    List<?> datiFS2 = sqlManager.getVector(selectFS2, new Object[] { id });

    String pagina = "";

    // Informazioni relative alla rinovabilita'
    pagina = UtilityTags.getResource("label.simap.fs2.VI.1", null, false);
    String recurrent_proc = (String) SqlManager.getValueFromVectorParam(datiFS2, 0).getValue();
    String recurrent_proc_desc = (String) SqlManager.getValueFromVectorParam(datiFS2, 1).getValue();
    if (recurrent_proc == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "RECURRENT_PROC", pagina);
    } else if ("1".equals(recurrent_proc)) {
      if (recurrent_proc_desc == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "RECURRENT_PROC_DESC", pagina);
      } else {
        ValidazioneUtility.validazioneS400(recurrent_proc_desc, "W3FS2S36", "RECURRENT_PROC_DESC", pagina, listaControlli);
      }
    }

    // Informazioni complementari
    pagina = UtilityTags.getResource("label.simap.fs2.VI.3", null, false);
    String additional_information = (String) SqlManager.getValueFromVectorParam(datiFS2, 6).getValue();
    ValidazioneUtility.validazioneS4000(additional_information, "W3FS2S36", "ADDITIONAL_INFORMATION", pagina, listaControlli);

    // Organismo responsabile delle procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs2.VI.4.1", null, false);
    String appeal_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiFS2, 2).getValue();
    if (appeal_procedure_codein == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS2S36", "APPEAL_PROCEDURE_CODEIN", pagina);
    } else {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, appeal_procedure_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

    // Organismo responsabile delle procedure di mediazione
    pagina = UtilityTags.getResource("label.simap.fs2.VI.4.2", null, false);
    String mediation_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiFS2, 3).getValue();
    if (mediation_procedure_codein != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, mediation_procedure_codein, pagina + " - Ufficio o punto di contatto", listaControlli,
          "S6");
    }

    // Informazioni dettagliate sui termini di presentazione dei ricorsi
    pagina = UtilityTags.getResource("label.simap.fs2.VI.4.3", null, false);
    String appeal_precision = (String) SqlManager.getValueFromVectorParam(datiFS2, 7).getValue();
    ValidazioneUtility.validazioneS4000(appeal_precision, "W3FS2S36", "APPEAL_PRECISION", pagina, listaControlli);

    // Servizio presso il quale sono disponibili...
    pagina = UtilityTags.getResource("label.simap.fs2.VI.4.4", null, false);
    String appeal_service_codein = (String) SqlManager.getValueFromVectorParam(datiFS2, 4).getValue();
    if (appeal_service_codein != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, appeal_service_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

  }

}