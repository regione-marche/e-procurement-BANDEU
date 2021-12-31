package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ValidazioneFS20 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS20.class);

  /**
   * Controllo dati formulario standard 3: avviso di aggiudicazione di appalto
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS20(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS20: inizio metodo");

    try {
      this.validazione_Sezione_I(sqlManager, id, listaControlli);
      this.validazioneFS20_Sezione_II(sqlManager, id, listaControlli);
      this.validazioneW3ANNEXB_FS20(sqlManager, id, listaControlli);
      this.validazioneFS20_Sezione_IV(sqlManager, id, listaControlli);
      this.validazioneFS20_Sezione_V(sqlManager, id, listaControlli);
      this.validazioneFS20_Sezione_VI(sqlManager, id, listaControlli);
      this.validazioneFS20_Sezione_VII(sqlManager, id, listaControlli);
      
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'avviso di aggiudicazione", "validazioneFS20", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS20: fine metodo");
  }

  /**
   * Sezione II: oggetto.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS20_Sezione_II(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS20 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " type_contract " // 3
        + " from w3fs20 "
        + " where w3fs20.id = ?";

    List<?> datiFS20 = sqlManager.getVector(selectFS20, new Object[] { id });

    String pagina = "";

    // Denominazione
    pagina = UtilityTags.getResource("label.simap.fs20.II.1.1", null, false);
    String title_contract = (String) SqlManager.getValueFromVectorParam(datiFS20, 0).getValue();
    if (title_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "TITLE_CONTRACT", pagina);
    } else {
      ValidazioneUtility.validazioneS200(title_contract, "W3FS20", "TITLE_CONTRACT", pagina, listaControlli);
    }

    // Numero di riferimento
    String reference = (String) SqlManager.getValueFromVectorParam(datiFS20, 1).getValue();
    if (reference != null) {
      ValidazioneUtility.validazioneS100(reference, "W3FS20", "REFERENCE", pagina, listaControlli);
    }

    String type_contract = (String) SqlManager.getValueFromVectorParam(datiFS20, 3).getValue();
    
    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs20.II.1.2", null, false);
    String cpv = (String) SqlManager.getValueFromVectorParam(datiFS20, 2).getValue();
    if (cpv == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "CPV", pagina);
    }
    
    // Controllo corrispondenza CPV con il tipo appalto
    ValidazioneUtility.validazioneCorrispondenzaCPV(listaControlli, pagina, cpv, type_contract, "W3FS20", "CPV");

    // Tipo di appalto
    pagina = UtilityTags.getResource("label.simap.fs20.II.1.3", null, false);
    if (type_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "TYPE_CONTRACT", pagina);
    }

  }

  /**
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  private void validazioneW3ANNEXB_FS20(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {

    String selectW3ANNEXB = "select w3annexb.num, " // 0
        + " w3annexb.lotnum, " // 1
        + " w3annexb.site_nuts, " // 2
        + " w3annexb.site_nuts_2, " // 3
        + " w3annexb.site_nuts_3, " // 4
        + " w3annexb.site_nuts_4, " // 5
        + " w3annexb.site_label, " // 6
        + " w3annexb.description, " // 7
        + " w3annexb.work_month, " // 8
        + " w3annexb.work_days, " // 9
        + " w3annexb.work_start_date, " // 10
        + " w3annexb.work_end_date, " // 11
        + " w3annexb.justification, " // 12
        + " w3annexb.eu_progr, " // 13
        + " w3annexb.eu_progr_descr " // 14
        + " from w3annexb where w3annexb.id = ? "
        + " and w3annexb.num = 1";

    String selectW3CPV = "select numcpv, cpv from w3cpv where ent = ? and id = ? and num = ?";

    try {

      String form = "fs20";

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

        String site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 3).getValue();
        String site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 4).getValue();
        String site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 5).getValue();
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
        String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
        ValidazioneUtility.validazioneS200(site_label, "W3ANNEXB", "SITE_LABEL", pagina, listaControlli);

        // Descrizione dell'appalto
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.4", null, false);
        String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
        if (description == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "DESCRIPTION", pagina);
        } else {
          ValidazioneUtility.validazioneS4000(description, "W3ANNEXB", "DESCRIPTION", pagina, listaControlli);
        }

        // Durata
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.7", null, false);
        Long work_month = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
        Long work_days = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();
        Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
        Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
        if (work_month == null && work_days == null && work_start_date == null && work_end_date == null) {
          String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_MONTH");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_DAYS");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_START_DATE");
          descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_END_DATE");
          String messaggio = "Valorizzare il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_MONTH");
          messaggio += "\" oppure il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_DAYS");
          messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_START_DATE");
          messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "WORK_END_DATE") + "\"";
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
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
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
        }

        if (work_end_date != null && work_start_date != null) {
          if (work_end_date.getTime() <= work_start_date.getTime()) {
            String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(work_end_date, ">", work_start_date, "W3ANNEXB",
                "WORK_START_DATE");
            ValidazioneUtility.addAvviso(listaControlli, "W3ANNEXB", "WORK_END_DATE", "E", pagina, messaggio);
          }
        }

        // Giustificazione
        String justification = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 12).getValue();
        ValidazioneUtility.validazioneS400(justification, "W3ANNEXB", "JUSTIFICATION", pagina, listaControlli);

        // Fondi europei, verificare numero di riferimento del progetto
        pagina = UtilityTags.getResource("label.simap." + form + ".II.2.13", null, false);
        String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
        String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 14).getValue();
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

  /**
   * Sezione IV - Procedura
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS20_Sezione_IV(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS20 = "select notice_number_oj " // 0
        + " from w3fs20 "
        + " where id = ? ";

    List<?> datiFS20 = sqlManager.getVector(selectFS20, new Object[] { id });

    // Pubblicazioni precedente
    String pagina = UtilityTags.getResource("label.simap.fs20.IV.2.1", null, false);
    String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiFS20, 0).getValue();
    if (notice_number_oj != null) {
      ValidazioneUtility.validazioneNumberOJ(notice_number_oj, "W3FS20", "NOTICE_NUMBER_OJ", pagina, listaControlli);
    }

  }

  /**
   * Sezione V: aggiudicazione dell'appalto.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  private void validazioneFS20_Sezione_V(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS20 = "select contract_number, " // 0
        + " contract_title, " // 1
        + " contract_award_date, " // 2
        + " awarded_group, " // 3
        + " val_total " // 4
        + " from w3fs20 "
        + " where id = ? ";

    List<?> datiFS20 = sqlManager.getVector(selectFS20, new Object[] { id });

    String pagina = UtilityTags.getResource("label.simap.fs20.V", null, false);

    // Numero contratto appalto
    String contract_number = (String) SqlManager.getValueFromVectorParam(datiFS20, 0).getValue();
    if (contract_number == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "CONTRACT_NUMBER", pagina);
    } else {
      ValidazioneUtility.validazioneS100(contract_number, "W3FS20", "CONTRACT_NUMBER", pagina, listaControlli);
    }

    // Denominazione
    String contract_title = (String) SqlManager.getValueFromVectorParam(datiFS20, 1).getValue();
    if (contract_title == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "CONTRACT_TITLE", pagina);
    } else {
      ValidazioneUtility.validazioneS200(contract_title, "W3FS20", "CONTRACT_TITLE", pagina, listaControlli);
    }

    // Data conclusione/aggiudicazione
    pagina = UtilityTags.getResource("label.simap.fs20.V.2.1", null, false);
    Date contract_award_date = (Date) SqlManager.getValueFromVectorParam(datiFS20, 2).getValue();
    if (contract_award_date == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "CONTRACT_AWARD_DATE", pagina);
    }

    // L'appalto e' stato aggiudicato ad un raggruppamento di operatori
    // economici ?
    String selectW3FS20AWARD_C_CNT = "select count(*) from w3fs20award_c where id = ? and item = ?";
    String awarded_group = (String) SqlManager.getValueFromVectorParam(datiFS20, 3).getValue();

    if (awarded_group == null) {
      pagina = UtilityTags.getResource("label.simap.fs20.V.2.2", null, false);
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "AWARDED_GROUP", pagina);
    } else if ("1".equals(awarded_group)) {
      // Raggruppamento di operatori economici
      pagina = UtilityTags.getResource("label.simap.fs20.V.2.3", null, false);
      Long op_cnt = (Long) sqlManager.getObject(selectW3FS20AWARD_C_CNT, new Object[] { id, new Long(1) });
      if (op_cnt != null && op_cnt.longValue() > 1) {
        this.validazioneW3FS20AWARD_C(sqlManager, id, new Long(1), pagina, listaControlli);
      } else {
        String descrizione = "Contraenti";
        String messaggio = "Inserire almeno due contraenti";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

    } else if ("2".equals(awarded_group)) {
      // Singolo operatore economico
      pagina = UtilityTags.getResource("label.simap.fs20.V.2.3", null, false);
      Long op_cnt = (Long) sqlManager.getObject(selectW3FS20AWARD_C_CNT, new Object[] { id, new Long(1) });
      if (op_cnt != null && op_cnt.longValue() == 1) {
        this.validazioneW3FS20AWARD_C(sqlManager, id, new Long(1), pagina, listaControlli);
      } else {
        String descrizione = "Contraente";
        String messaggio = "Inserire uno ed un solo contraente";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

    }

    // Valore totale
    pagina = UtilityTags.getResource("label.simap.fs20.V.2.4", null, false);
    Double val_total = (Double) SqlManager.getValueFromVectorParam(datiFS20, 4).getValue();
    if (val_total == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "VAL_TOTAL", pagina);
    } else {
      ValidazioneUtility.validazioneCost(val_total, "W3FS20", "VAL_TOTAL", pagina, listaControlli);
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
  private void validazioneFS20_Sezione_VI(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS20 = "select address_review_body, " // 0
        + " address_mediation_body, " // 1
        + " address_review_info, " // 2
        + " additional_information, " // 3
        + " review_procedure " // 4
        + " from w3fs20 where w3fs20.id = ?";

    List<?> datiFS20 = sqlManager.getVector(selectFS20, new Object[] { id });

    String pagina = "";

    // Informazioni complementari
    pagina = UtilityTags.getResource("label.simap.fs20.VI.3", null, false);
    String additional_information = (String) SqlManager.getValueFromVectorParam(datiFS20, 3).getValue();
    ValidazioneUtility.validazioneS4000(additional_information, "W3FS20", "ADDITIONAL_INFORMATION", pagina, listaControlli);

    // Organismo responsabile delle procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs20.VI.4.1", null, false);
    String address_review_body = (String) SqlManager.getValueFromVectorParam(datiFS20, 0).getValue();
    if (address_review_body == null) {
      String descrizione = "Organismo/ufficio";
      String messaggio = "Il campo &egrave; obbligatorio";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    } else {
      pagina = pagina + " - Ufficio o punto di contatto";
      this.validazioneUFFINT_S1_S5_S6(sqlManager, address_review_body, pagina, listaControlli, "S6");
    }

    // Organismo responsabile delle procedure di mediazione
    pagina = UtilityTags.getResource("label.simap.fs20.VI.4.2", null, false);
    String address_mediation_body = (String) SqlManager.getValueFromVectorParam(datiFS20, 1).getValue();
    if (address_mediation_body != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, address_mediation_body, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

    // Procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs20.VI.4.3", null, false);
    String appeal_precision = (String) SqlManager.getValueFromVectorParam(datiFS20, 4).getValue();
    ValidazioneUtility.validazioneS4000(appeal_precision, "W3FS20", "REVIEW_PROCEDURE", pagina, listaControlli);

    // Servizio presso il quale sono disponibili...
    pagina = UtilityTags.getResource("label.simap.fs20.VI.4.4", null, false);
    String address_review_info = (String) SqlManager.getValueFromVectorParam(datiFS20, 2).getValue();
    if (address_review_info != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, address_review_info, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

  }

  /**
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS20_Sezione_VII(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS20 = "select m_cpv, " // 0
        + " m_site_nuts, " // 1
        + " m_site_nuts_2, " // 2
        + " m_site_nuts_3, " // 3
        + " m_site_nuts_4, " // 4
        + " m_site_label, " // 5
        + " m_description, " // 6
        + " m_duration_month, " // 7
        + " m_duration_day, " // 8
        + " m_date_start, " // 9
        + " m_date_stop, " // 10
        + " m_justification, " // 11
        + " m_val_total, " // 12
        + " m_awarded_group, " // 13
        + " m_short_descr, " // 14
        + " m_additional_need, " // 15
        + " m_unforessen, " // 16
        + " m_val_total_before, " // 17
        + " m_val_total_after " // 18
        + " from w3fs20 where w3fs20.id = ?";

    List<?> datiFS20 = sqlManager.getVector(selectFS20, new Object[] { id });

    String pagina = "";

    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.1", null, false);
    if (SqlManager.getValueFromVectorParam(datiFS20, 0).getValue() == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "M_CPV", pagina);
    }

    String selectW3CPV = "select numcpv, cpv from w3cpv where ent = ? and id = ? and num = ?";

    // Controllo CPV aggiuntivi, se esiste l'occorrenza in W3CPV diventa
    // obbligatorio indicare il CPV del vocabolario principale
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.2", null, false);
    List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { "W3FS20", id, new Long(1) });
    if (datiW3CPV != null && datiW3CPV.size() > 0) {
      for (int iCPV = 0; iCPV < datiW3CPV.size(); iCPV++) {
        String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(iCPV), 1).getValue();
        if (cpv == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3CPV", "CPV", pagina);
        }
      }
    }

    // Codice NUTS
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.3", null, false);
    String site_nuts = (String) SqlManager.getValueFromVectorParam(datiFS20, 1).getValue();
    if (site_nuts == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "M_SITE_NUTS", pagina);
    } else {
      validazioneNUTS(sqlManager, site_nuts, "W3FS20", "M_SITE_NUTS", pagina, listaControlli);
    }

    String site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiFS20, 2).getValue();
    String site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiFS20, 3).getValue();
    String site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiFS20, 4).getValue();
    if (site_nuts_2 != null) {
      validazioneNUTS(sqlManager, site_nuts_2, "W3FS20", "M_SITE_NUTS_2", pagina, listaControlli);
    }
    if (site_nuts_3 != null) {
      validazioneNUTS(sqlManager, site_nuts_2, "W3FS20", "M_SITE_NUTS_3", pagina, listaControlli);
    }
    if (site_nuts_4 != null) {
      validazioneNUTS(sqlManager, site_nuts_2, "W3FS20", "M_SITE_NUTS_4", pagina, listaControlli);
    }

    // Sito o luogo principale
    String site_label = (String) SqlManager.getValueFromVectorParam(datiFS20, 5).getValue();
    ValidazioneUtility.validazioneS200(site_label, "W3FS20", "M_SITE_LABEL", pagina, listaControlli);

    // Descrizione dell'appalto
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.4", null, false);
    String description = (String) SqlManager.getValueFromVectorParam(datiFS20, 6).getValue();
    if (description == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "M_DESCRIPTION", pagina);
    } else {
      ValidazioneUtility.validazioneS4000(description, "W3FS20", "M_DESCRIPTION", pagina, listaControlli);
    }

    // Durata
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.5", null, false);
    Long work_month = (Long) SqlManager.getValueFromVectorParam(datiFS20, 7).getValue();
    Long work_days = (Long) SqlManager.getValueFromVectorParam(datiFS20, 8).getValue();
    Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiFS20, 9).getValue();
    Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiFS20, 10).getValue();
    if (work_month == null && work_days == null && work_start_date == null && work_end_date == null) {
      String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_MONTH");
      descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_DAY");
      descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_START");
      descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_STOP");
      String messaggio = "Valorizzare il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_MONTH");
      messaggio += "\" oppure il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_DAY");
      messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_START");
      messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_STOP") + "\"";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }

    if ((work_month != null && work_days != null)
        || ((work_month != null || work_days != null) && (work_start_date != null || work_end_date != null))) {
      String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_MONTH");
      descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_DAY");
      descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_START");
      descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_STOP");
      String messaggio = "Valorizzare solamente il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_MONTH");
      messaggio += "\" oppure il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DURATION_DAY");
      messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_START");
      messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3FS20", "M_DATE_STOP") + "\"";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }

    if (work_end_date != null && work_start_date != null) {
      if (work_end_date.getTime() <= work_start_date.getTime()) {
        String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(work_end_date, ">", work_start_date, "W3FS20", "M_DATE_START");
        ValidazioneUtility.addAvviso(listaControlli, "W3FS20", "M_DATE_STOP", "E", pagina, messaggio);
      }
    }

    // Giustificazione
    String justification = (String) SqlManager.getValueFromVectorParam(datiFS20, 11).getValue();
    ValidazioneUtility.validazioneS400(justification, "W3FS20", "JUSTIFICATION", pagina, listaControlli);

    // Valore totale
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.6", null, false);
    Double val_total = (Double) SqlManager.getValueFromVectorParam(datiFS20, 12).getValue();
    if (val_total == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "VAL_TOTAL", pagina);
    } else {
      ValidazioneUtility.validazioneCost(val_total, "W3FS20", "VAL_TOTAL", pagina, listaControlli);
    }

    // L'appalto e' stato aggiudicato ad un raggruppamento di operatori
    // economici ?
    pagina = UtilityTags.getResource("label.simap.fs20.VII.1.7", null, false);
    String selectW3FS20AWARD_M_CNT = "select count(*) from w3fs20award_M where id = ? and item = ?";
    String awarded_group = (String) SqlManager.getValueFromVectorParam(datiFS20, 13).getValue();

    if (awarded_group == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "AWARDED_GROUP", pagina);
    } else if ("1".equals(awarded_group)) {
      // Raggruppamento di operatori economici
      Long op_cnt = (Long) sqlManager.getObject(selectW3FS20AWARD_M_CNT, new Object[] { id, new Long(1) });
      if (op_cnt != null && op_cnt.longValue() > 1) {
        this.validazioneW3FS20AWARD_M(sqlManager, id, new Long(1), pagina, listaControlli);
      } else {
        String descrizione = "Contraenti";
        String messaggio = "Inserire almeno due contraenti";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

    } else if ("2".equals(awarded_group)) {
      // Singolo operatore economico
      Long op_cnt = (Long) sqlManager.getObject(selectW3FS20AWARD_M_CNT, new Object[] { id, new Long(1) });
      if (op_cnt != null && op_cnt.longValue() == 1) {
        this.validazioneW3FS20AWARD_M(sqlManager, id, new Long(1), pagina, listaControlli);
      } else {
        String descrizione = "Contraente";
        String messaggio = "Inserire uno ed un solo contraente";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

    }

    // Descrizione delle modifiche
    pagina = UtilityTags.getResource("label.simap.fs20.VII.2.1", null, false);
    String m_short_descr = (String) SqlManager.getValueFromVectorParam(datiFS20, 14).getValue();
    if (m_short_descr == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "M_SHORT_DESCR", pagina);
    } else {
      ValidazioneUtility.validazioneS4000(m_short_descr, "W3FS20", "M_SHORT_DESCR", pagina, listaControlli);
    }

    // Motivi della modifica
    pagina = UtilityTags.getResource("label.simap.fs20.VII.2.2", null, false);
    String m_additional_need = (String) SqlManager.getValueFromVectorParam(datiFS20, 15).getValue();
    String m_unforeseen = (String) SqlManager.getValueFromVectorParam(datiFS20, 16).getValue();
    if (m_additional_need == null && m_unforeseen == null) {
      String descrizione = "Descrizione dei motivi economici o tecnici ...";
      descrizione += ", Descrizione delle circostanze ...";
      String messaggio = "Valorizzare il campo \"Descrizione dei motivi economici o tecnici ...\"";
      messaggio += "\" oppure il campo \"Descrizione delle circostanze ...\"";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    } else if (m_additional_need != null && m_unforeseen != null) {
      String descrizione = "Descrizione dei motivi economici o tecnici ...";
      descrizione += ", Descrizione delle circostanze ...";
      String messaggio = "Valorizzare solamente il campo \"Descrizione dei motivi economici o tecnici ...\"";
      messaggio += "\" oppure il campo \"Descrizione delle circostanze ...\"";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    }

    ValidazioneUtility.validazioneS1000(m_additional_need, "W3FS20", "M_ADDITIONAL_NEED", pagina, listaControlli);
    ValidazioneUtility.validazioneS1000(m_unforeseen, "W3FS20", "M_UNFORESSEN", pagina, listaControlli);

    // Aumento del prezzo
    // Valore totale
    pagina = UtilityTags.getResource("label.simap.fs20.VII.2.3", null, false);
    Double m_val_total_before = (Double) SqlManager.getValueFromVectorParam(datiFS20, 17).getValue();
    if (m_val_total_before == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "M_VAL_TOTAL_BEFORE", pagina);
    } else {
      ValidazioneUtility.validazioneCost(val_total, "W3FS20", "M_VAL_TOTAL_BEFORE", pagina, listaControlli);
    }

    Double m_val_total_after = (Double) SqlManager.getValueFromVectorParam(datiFS20, 18).getValue();
    if (m_val_total_after == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS20", "M_VAL_TOTAL_AFTER", pagina);
    } else {
      ValidazioneUtility.validazioneCost(val_total, "W3FS20", "M_VAL_TOTAL_AFTER", pagina, listaControlli);
    }

  }

  /**
   * Controllo dei contraenti.
   * 
   * @param sqlManager
   * @param id
   * @param item
   * @param pagina
   * @param listaControlliW3FS20AWARD
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneW3FS20AWARD_C(SqlManager sqlManager, Long id, Long item, String pagina, List<Object> listaControlliW3FS20AWARD)
      throws SQLException, GestoreException {
    String selectW3FS20AWARD_C = "select num, contractor_codimp, contractor_sme from w3fs20award_c where id = ? and item = ?";
    List<?> datiW3FS20AWARD_C = sqlManager.getListVector(selectW3FS20AWARD_C, new Object[] { id, item });
    if (datiW3FS20AWARD_C != null && datiW3FS20AWARD_C.size() > 0) {
      for (int icont = 0; icont < datiW3FS20AWARD_C.size(); icont++) {
        Long num = (Long) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_C.get(icont), 0).getValue();
        String pagina_c = pagina + " - Contraente n. " + num.toString();
        String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_C.get(icont), 1).getValue();
        String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_C.get(icont), 2).getValue();
        if (contractor_codimp == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS20AWARD, "W3FS20AWARD_C", "CONTRACTOR_CODIMP", pagina_c);
        } else {
          this.validazioneIMPR(sqlManager, contractor_codimp, pagina_c, listaControlliW3FS20AWARD);
        }
        if (contractor_sme == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS20AWARD, "W3FS20AWARD_C", "CONTRACTOR_SME", pagina_c);
        }
      }
    }
  }

  /**
   * 
   * @param sqlManager
   * @param id
   * @param item
   * @param pagina
   * @param listaControlliW3FS20AWARD
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneW3FS20AWARD_M(SqlManager sqlManager, Long id, Long item, String pagina, List<Object> listaControlliW3FS20AWARD)
      throws SQLException, GestoreException {
    String selectW3FS20AWARD_M = "select num, contractor_codimp, contractor_sme from w3fs20award_m where id = ? and item = ?";
    List<?> datiW3FS20AWARD_M = sqlManager.getListVector(selectW3FS20AWARD_M, new Object[] { id, item });
    if (datiW3FS20AWARD_M != null && datiW3FS20AWARD_M.size() > 0) {
      for (int icont = 0; icont < datiW3FS20AWARD_M.size(); icont++) {
        Long num = (Long) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_M.get(icont), 0).getValue();
        String pagina_c = pagina + " - Contraente n. " + num.toString();
        String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_M.get(icont), 1).getValue();
        String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_M.get(icont), 2).getValue();
        if (contractor_codimp == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS20AWARD, "W3FS20AWARD_M", "CONTRACTOR_CODIMP", pagina_c);
        } else {
          this.validazioneIMPR(sqlManager, contractor_codimp, pagina_c, listaControlliW3FS20AWARD);
        }
        if (contractor_sme == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS20AWARD, "W3FS20AWARD_M", "CONTRACTOR_SME", pagina_c);
        }
      }
    }
  }

}