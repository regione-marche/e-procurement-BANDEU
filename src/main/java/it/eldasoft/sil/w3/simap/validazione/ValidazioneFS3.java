package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.utility.UtilityDate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ValidazioneFS3 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS3.class);

  /**
   * Controllo dati formulario standard 3: avviso di aggiudicazione di appalto
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS3(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS3: inizio metodo");

    try {
      this.validazione_Sezione_I(sqlManager, id, listaControlli);
      this.validazioneFS3_Sezione_II(sqlManager, id, listaControlli);
      this.validazioneNumeroMassimoLotti(sqlManager, id, listaControlli);
      this.validazioneW3ANNEXB(sqlManager, id, null, listaControlli);
      this.validazioneFS3_Sezione_IV(sqlManager, id, listaControlli);
      this.validazioneNumeroMassimoLottiAggiudicati(sqlManager, id, listaControlli);
      this.validazioneFS3_Sezione_V(sqlManager, id, listaControlli);
      this.validazioneFS3_Sezione_VI(sqlManager, id, listaControlli);
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'avviso di aggiudicazione", "validazioneFS3", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS3: fine metodo");
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
  private void validazioneFS3_Sezione_II(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS3 = "select title_contract, " // 0
        + " cpv, " // 1
        + " type_contract, " // 2
        + " short_contract_description, " // 3
        + " div_into_lots, " // 4
        + " scope_cost, " // 5
        + " scope_low, " // 6
        + " scope_high, " // 7
        + " reference " // 8
        + " from w3fs3 "
        + " where w3fs3.id = ?";

    List<?> datiFS3 = sqlManager.getVector(selectFS3, new Object[] { id });

    String pagina = "";

    // Denominazione
    pagina = UtilityTags.getResource("label.simap.fs3.II.1.1", null, false);
    String title_contract = (String) SqlManager.getValueFromVectorParam(datiFS3, 0).getValue();
    if (title_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "TITLE_CONTRACT", pagina);
    } else {
      ValidazioneUtility.validazioneS200(title_contract, "W3FS3", "TITLE_CONTRACT", pagina, listaControlli);
    }

    // Numero di riferimento
    String reference = (String) SqlManager.getValueFromVectorParam(datiFS3, 8).getValue();
    if (reference != null) {
      ValidazioneUtility.validazioneS100(reference, "W3FS3", "REFERENCE", pagina, listaControlli);
    }

    String type_contract = (String) SqlManager.getValueFromVectorParam(datiFS3, 2).getValue();

    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs3.II.1.2", null, false);
    String cpv = (String) SqlManager.getValueFromVectorParam(datiFS3, 1).getValue();
    if (cpv == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "CPV", pagina);
    }

    // Controllo corrispondenza CPV con il tipo appalto
    ValidazioneUtility.validazioneCorrispondenzaCPV(listaControlli, pagina, cpv, type_contract, "W3FS3", "CPV");

    // Tipo di appalto
    pagina = UtilityTags.getResource("label.simap.fs3.II.1.3", null, false);
    if (type_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "TYPE_CONTRACT", pagina);
    }

    // Breve descrizione
    pagina = UtilityTags.getResource("label.simap.fs3.II.1.4", null, false);
    String short_contract_description = (String) SqlManager.getValueFromVectorParam(datiFS3, 3).getValue();
    if (short_contract_description == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "SHORT_CONTRACT_DESCRIPTION", pagina);
    } else {
      ValidazioneUtility.validazioneS1000(short_contract_description, "W3FS3", "SHORT_CONTRACT_DESCRIPTION", pagina, listaControlli);
    }

    // Appalto suddiviso in lotti ?
    pagina = UtilityTags.getResource("label.simap.fs3.II.1.6", null, false);
    if (SqlManager.getValueFromVectorParam(datiFS3, 4).getValue() == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "DIV_INTO_LOTS", pagina);
    }

    // Valore totale dell'appalto
    // Il valore finale deve essere indicato se almeno un contratto e' stato
    // aggiudicato
    Long cnt_awarded = (Long) sqlManager.getObject("select count(*) from w3fs3award where id = ? and awarded = ?", new Object[] { id, "1" });
    if (cnt_awarded != null && cnt_awarded.longValue() > 0) {

      pagina = UtilityTags.getResource("label.simap.fs3.II.1.7", null, false);
      Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiFS3, 5).getValue();
      Double scope_low = (Double) SqlManager.getValueFromVectorParam(datiFS3, 6).getValue();
      Double scope_high = (Double) SqlManager.getValueFromVectorParam(datiFS3, 7).getValue();

      if (scope_cost == null && scope_low == null && scope_high == null) {
        String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_COST");
        descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_LOW");
        descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_HIGH");
        String messaggio = "Valorizzare il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_COST");
        messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_LOW");
        messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_HIGH") + "\"";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

      if (scope_cost != null && (scope_low != null || scope_high != null)) {
        String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_COST");
        descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_LOW");
        descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_HIGH");
        String messaggio = "Valorizzare solamente il campo \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_COST");
        messaggio += "\" oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_LOW");
        messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_HIGH") + "\"";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

      if (scope_low == null && scope_high != null) {
        String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_LOW");
        descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_HIGH");
        String messaggio = "Valorizzare anche \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3", "SCOPE_LOW") + "\"";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

      ValidazioneUtility.validazioneCost(scope_cost, "W3FS3", "SCOPE_COST", pagina, listaControlli);
      ValidazioneUtility.validazioneCost(scope_low, "W3FS3", "SCOPE_LOW", pagina, listaControlli);
      ValidazioneUtility.validazioneCost(scope_high, "W3FS3", "SCOPE_HIGH", pagina, listaControlli);

      if (scope_low != null && scope_high != null) {
        if (scope_low.doubleValue() > scope_high.doubleValue()) {
          String messaggio = ValidazioneUtility.getMessaggioConfrontoImporti(scope_low, "<=", scope_high, "W3FS3", "SCOPE_HIGH");
          ValidazioneUtility.addAvviso(listaControlli, "W3FS3", "SCOPE_LOW", "E", pagina, messaggio);
        }
      }
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
  private void validazioneFS3_Sezione_IV(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS3 = "select type_procedure, " // 0
        + " accelerated, " // 1
        + " contract_covered_gpa, " // 2
        + " notice_number_oj " // 3
        + " from w3fs3 "
        + " where id = ? ";

    List<?> datiFS3 = sqlManager.getVector(selectFS3, new Object[] { id });

    String pagina = "";

    // Tipo di procedura
    pagina = UtilityTags.getResource("label.simap.fs3.IV.1.1", null, false);
    String type_procedure = (String) SqlManager.getValueFromVectorParam(datiFS3, 0).getValue();
    if (type_procedure == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "TYPE_PROCEDURE", pagina);
    } else if ("9".equals(type_procedure) || "5".equals(type_procedure) || "7".equals(type_procedure)) {
      if (SqlManager.getValueFromVectorParam(datiFS3, 1).getValue() == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "ACCELERATED", pagina);
      }
    } else if ("8".equals(type_procedure)) {
      // Controllo allegato D1
      this.validazioneFS3_Sezione_IV_AnnexD(sqlManager, id, listaControlli, pagina);
    }

    // Motivazioni per la procedura accelerata
    String accelerated = (String) SqlManager.getValueFromVectorParam(datiFS3, 1).getValue();
    ValidazioneUtility.validazioneS1000(accelerated, "W3FS3", "ACCELERATED", pagina, listaControlli);

    // Accordo sugli appalti pubblici (AAP)
    pagina = UtilityTags.getResource("label.simap.fs3.IV.1.8", null, false);
    if (SqlManager.getValueFromVectorParam(datiFS3, 2).getValue() == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "CONTRACT_COVERED_GPA", pagina);
    }

    // Pubblicazioni precedente
    pagina = UtilityTags.getResource("label.simap.fs3.IV.2.1", null, false);
    String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiFS3, 3).getValue();

    if (notice_number_oj == null && !"8".equals(type_procedure)) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS3", "NOTICE_NUMBER_OJ", pagina);
    }

    if (notice_number_oj != null) {
      ValidazioneUtility.validazioneNumberOJ(notice_number_oj, "W3FS3", "NOTICE_NUMBER_OJ", pagina, listaControlli);
    }

  }

  /**
   * Controllo dati della sezione ANNEXD - Allegato D1
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @param pagina
   * @throws SQLException
   */
  private void validazioneFS3_Sezione_IV_AnnexD(SqlManager sqlManager, Long id, List<Object> listaControlli, String pagina)
      throws SQLException {
    String selectW3ANNEXD = "select no_open_restricted, " // 0
        + " manufactured, " // 1
        + " particular_tendered, " // 2
        + " extreme_urgency, " // 3
        + " additional_works, " // 4
        + " works_repetition, " // 5
        + " service_contract, " // 6
        + " supplies_quoted, " // 7
        + " purchase_supplies, " // 8
        + " outside_scope, " // 9
        + " reason_contract_lawful " // 10
        + " from w3annexd where id = ?";

    List<?> datiW3ANNEXD = sqlManager.getVector(selectW3ANNEXD, new Object[] { id });
    if (datiW3ANNEXD == null) {
      String descrizione = "Allegato D1";
      String messaggio = "Valorizzare l'allegato D1";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    } else {

      pagina = UtilityTags.getResource("label.simap.annexd1.short", null, false);

      boolean validD1_1 = false;
      boolean validD1_2 = false;

      String no_open_restricted = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 0).getValue();
      if (no_open_restricted != null) validD1_1 = true;

      String manufactured = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 1).getValue();
      if (manufactured != null && "1".equals(manufactured)) validD1_1 = true;

      String particular_tendered = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 2).getValue();
      if (particular_tendered != null) validD1_1 = true;

      String extreme_urgency = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 3).getValue();
      if (extreme_urgency != null && "1".equals(extreme_urgency)) validD1_1 = true;

      String additional_works = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 4).getValue();
      if (additional_works != null && "1".equals(additional_works)) validD1_1 = true;

      String works_repetition = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 5).getValue();
      if (works_repetition != null && "1".equals(works_repetition)) validD1_1 = true;

      String service_contract = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 6).getValue();
      if (service_contract != null && "1".equals(service_contract)) validD1_1 = true;

      String supplies_quoted = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 7).getValue();
      if (supplies_quoted != null && "1".equals(supplies_quoted)) validD1_1 = true;

      String purchase_supplies = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 8).getValue();
      if (purchase_supplies != null) validD1_1 = true;

      String outside_scope = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 9).getValue();
      if (outside_scope != null && "1".equals(outside_scope)) validD1_2 = true;

      if ((validD1_1 == false && validD1_2 == false) || (validD1_1 == true && validD1_2 == true)) {
        String descrizione = "1. Motivazioni della scelta... e 2. Altre motivazioni...";
        String messaggio = "Scegliere almeno una delle motivazioni della sezione \"1. Motivazioni della scelta...\" oppure la sola motivazione della sezione \"2. Altre motivazioni...\"";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }

      String reason_contract_lawful = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 10).getValue();
      if (reason_contract_lawful == null) {
        String descrizione = "3. Spiegazione";
        String messaggio = "Il campo &egrave; obbligatorio";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      } else {
        ValidazioneUtility.validazioneS2500(reason_contract_lawful, "W3ANNEXD", "REASON_CONTRACT_LAWFUL", pagina, listaControlli);
      }

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
  private void validazioneFS3_Sezione_V(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    try {
      Long conteggioW3FS3AWARD = (Long) sqlManager.getObject("select count(*) from w3fs3award where id = ?", new Object[] { id });
      if (conteggioW3FS3AWARD == null || (conteggioW3FS3AWARD != null && conteggioW3FS3AWARD.longValue() == 0)) {
        String messaggio = "Inserire almeno una aggiudicazione dell\'appalto";
        String descrizione = UtilityTags.getResource("label.simap.fs3.V", null, false);
        listaControlli.add(((Object) (new Object[] { "E", descrizione, descrizione, messaggio })));
      } else {
        this.validazioneW3FS3AWARD(sqlManager, id, null, listaControlli);
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni delle aggiudicazioni dell'appalto", "validazioneFS3", e);
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
  private void validazioneFS3_Sezione_VI(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS3 = "select appeal_procedure_codein, " // 0
        + " mediation_procedure_codein, " // 1
        + " appeal_service_codein, " // 2
        + " additional_information, " // 3
        + " appeal_precision " // 4
        + " from w3fs3 where w3fs3.id = ?";

    List<?> datiFS3 = sqlManager.getVector(selectFS3, new Object[] { id });

    String pagina = "";

    // Informazioni complementari
    pagina = UtilityTags.getResource("label.simap.fs3.VI.3", null, false);
    String additional_information = (String) SqlManager.getValueFromVectorParam(datiFS3, 3).getValue();
    ValidazioneUtility.validazioneS4000(additional_information, "W3FS3", "ADDITIONAL_INFORMATION", pagina, listaControlli);

    // Organismo responsabile delle procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs3.VI.4.1", null, false);
    String appeal_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiFS3, 0).getValue();
    if (appeal_procedure_codein == null) {
      String descrizione = "Organismo/ufficio";
      String messaggio = "Il campo &egrave; obbligatorio";
      listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
    } else {
      pagina = pagina + " - Ufficio o punto di contatto";
      this.validazioneUFFINT_S1_S5_S6(sqlManager, appeal_procedure_codein, pagina, listaControlli, "S6");
    }

    // Organismo responsabile delle procedure di mediazione
    pagina = UtilityTags.getResource("label.simap.fs3.VI.4.2", null, false);
    String mediation_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiFS3, 1).getValue();
    if (mediation_procedure_codein != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, mediation_procedure_codein, pagina + " - Ufficio o punto di contatto", listaControlli,
          "S6");
    }

    // Procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs3.VI.4.3", null, false);
    String appeal_precision = (String) SqlManager.getValueFromVectorParam(datiFS3, 3).getValue();
    ValidazioneUtility.validazioneS4000(appeal_precision, "W3FS3", "APPEAL_PRECISION", pagina, listaControlli);

    // Servizio presso il quale sono disponibili...
    pagina = UtilityTags.getResource("label.simap.fs3.VI.4.4", null, false);
    String appeal_service_codein = (String) SqlManager.getValueFromVectorParam(datiFS3, 2).getValue();
    if (appeal_service_codein != null) {
      this.validazioneUFFINT_S1_S5_S6(sqlManager, appeal_service_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
    }

  }

  /**
   * Sezione V: aggiudicazione dell'appalto.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @param w3fs3award_item
   *        Eventuale numero di aggiudicazione
   * @throws SQLException
   */
  public void validazioneW3FS3AWARD(SqlManager sqlManager, Long id, Long w3fs3award_item, List<Object> listaControlli)
      throws GestoreException {

    String pagina = "";

    String selectW3FS3AWARD = "select item, " // 0
        + " contract_title, " // 1
        + " awarded, " // 2
        + " no_awarded_type, " // 3
        + " original, " // 4
        + " date_original, " // 5
        + " contract_award_date, " // 6
        + " offers_received, " // 7
        + " awarded_group, " // 8
        + " final_cost, " // 9
        + " final_low, " // 10
        + " final_high, " // 11
        + " initial_cost, " // 12
        + " sub_contracted, " // 13
        + " sub_value, " // 14
        + " sub_prct, " // 15
        + " contract_number, " // 16
        + " additional_information, " // 17
        + " lot_number, " // 18
        + " nb_sme, " // 19
        + " nb_other_eu, " // 20
        + " nb_other_non_eu, " // 21
        + " offers_received_meaning " // 22
        + " from w3fs3award where id = ?";

    if (w3fs3award_item != null) {
      selectW3FS3AWARD += " and w3fs3award.item = " + w3fs3award_item.toString();
    }

    try {
      List<?> datiW3FS3AWARD = sqlManager.getListVector(selectW3FS3AWARD, new Object[] { id });

      if (datiW3FS3AWARD != null && datiW3FS3AWARD.size() > 0) {

        for (int i = 0; i < datiW3FS3AWARD.size(); i++) {
          Long item = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 0).getValue();

          pagina = "SEZIONE V: AGGIUDICAZIONE DELL'APPALTO";

          List<Object> listaControlliW3FS3AWARD = new Vector<Object>();

          // Numero contratto appalto
          String contract_number = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 16).getValue();
          ValidazioneUtility.validazioneS100(contract_number, "W3FS3AWARD", "CONTRACT_NUMBER", pagina, listaControlliW3FS3AWARD);

          // Numero del lotto
          Long lot_number = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 18).getValue();
          if (lot_number == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "LOT_NUMBER", pagina);
          } else {
            // Controllo che il numero del lotto aggiudicato corrisponda ad un
            // lotto della sezione di bando (W3ANNEXB_LOTNUM)
            Long cnt_w3annexb_lotnum = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ? and lotnum = ?",
                new Object[] { id, lot_number });
            if (cnt_w3annexb_lotnum == null || (cnt_w3annexb_lotnum != null && cnt_w3annexb_lotnum.longValue() == 0)) {
              String messaggio = "Il numero indicato non corrisponde ad alcun lotto della sezione II.2";
              ValidazioneUtility.addAvviso(listaControlliW3FS3AWARD, "W3FS3AWARD", "LOT_NUMBER", "E", pagina, messaggio);
            }
          }

          // Denominazione
          String contract_title = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 1).getValue();
          if (contract_title == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "CONTRACT_TITLE", pagina);
          } else {
            ValidazioneUtility.validazioneS200(contract_title, "W3FS3AWARD", "CONTRACT_TITLE", pagina, listaControlliW3FS3AWARD);
          }

          String awarded = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 2).getValue();
          if (awarded == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "AWARDED", pagina);
          }

          // Appalto non aggiudicato
          pagina = UtilityTags.getResource("label.simap.fs3.V.1", null, false);
          if (awarded != null && "2".equals(awarded)) {
            String no_awarded_type = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 3).getValue();
            if (no_awarded_type == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "NO_AWARDED_TYPE", pagina);
            } else if ("1".equals(no_awarded_type)) {
              if (SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 4).getValue() == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "ORIGINAL", pagina);
              }
              if (SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 5).getValue() == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "DATE_ORIGINAL", pagina);
              }
            }
          }

          // Appalto aggiudicato
          if (awarded != null && "1".equals(awarded)) {

            // Data di conclusione del contratto d'appalto
            pagina = UtilityTags.getResource("label.simap.fs3.V.2.1", null, false);
            Date contract_award_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 6).getValue();
            if (contract_award_date == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "CONTRACT_AWARD_DATE", pagina);
            } else {
              // Controllo che la data di conclusione di inferiore o uguale alla
              // data odierna
              Date dataOdierna = new Date();
              if (contract_award_date.getTime() > dataOdierna.getTime()) {
                String messaggio = "La data indicata ("
                    + UtilityDate.convertiData(contract_award_date, UtilityDate.FORMATO_GG_MM_AAAA)
                    + ") deve essere precedente o uguale alla data odierna";
                ValidazioneUtility.addAvviso(listaControlliW3FS3AWARD, "W3FS3AWARD", "CONTRACT_AWARD_DATE", "E", pagina, messaggio);
              }
            }

            // Numero offerte pervenute
            pagina = UtilityTags.getResource("label.simap.fs3.V.2.2", null, false);
            Long offers_received = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 7).getValue();
            if (offers_received == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "OFFERS_RECEIVED", pagina);
            } else {
              if (offers_received != null && offers_received.longValue() <= 0) {
                String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "OFFERS_RECEIVED");
                String messaggio = "Il numero deve essere maggiore di zero";
                listaControlliW3FS3AWARD.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
              }
            }

            // Numero offerte ricevute da PMI (NB_SME)
            Long nb_sme = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 19).getValue();
            if (nb_sme == null) {
              ValidazioneUtility.addCampoConsigliato(listaControlliW3FS3AWARD, "W3FS3AWARD", "NB_SME", pagina);
            } 

            // Numero offerte da altri UE (NB_OTHER_EU)
            Long nb_other_ue = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 20).getValue();
            if (nb_other_ue == null) {
              ValidazioneUtility.addCampoConsigliato(listaControlliW3FS3AWARD, "W3FS3AWARD", "NB_OTHER_EU", pagina);
            } 

            // Numero offerte da altri non UE (NB_OTHER_NON_EU)
            Long nb_other_non_eu = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 21).getValue();
            if (nb_other_non_eu == null) {
              ValidazioneUtility.addCampoConsigliato(listaControlliW3FS3AWARD, "W3FS3AWARD", "NB_OTHER_NON_EU", pagina);
            } 

            // Numero offerte per via telematica (OFFERS_RECEIVED_MEANING)
            Long offers_received_meaning = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 22).getValue();
            if (offers_received_meaning == null) {
              ValidazioneUtility.addCampoConsigliato(listaControlliW3FS3AWARD, "W3FS3AWARD", "OFFERS_RECEIVED_MEANING", pagina);
            } 

            // L'appalto e' stato aggiudicato ad un raggruppamento di operatori
            // economici ?
            String selectW3FS3AWARD_C_CNT = "select count(*) from w3fs3award_c where id = ? and item = ?";
            String awarded_group = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 8).getValue();

            if (awarded_group == null) {
              pagina = UtilityTags.getResource("label.simap.fs3.V.2.2", null, false);
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD", "AWARDED_GROUP", pagina);
            } else if ("1".equals(awarded_group)) {
              // Raggruppamento di operatori economici
              pagina = UtilityTags.getResource("label.simap.fs3.V.2.3", null, false);
              Long op_cnt = (Long) sqlManager.getObject(selectW3FS3AWARD_C_CNT, new Object[] { id, item });
              if (op_cnt != null && op_cnt.longValue() > 1) {
                this.validazioneW3FS3AWARD_C(sqlManager, id, item, pagina, listaControlliW3FS3AWARD);
              } else {
                String descrizione = "Contraenti";
                String messaggio = "Inserire almeno due contraenti";
                listaControlliW3FS3AWARD.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
              }

            } else if ("2".equals(awarded_group)) {
              // Singolo operatore economico
              pagina = UtilityTags.getResource("label.simap.fs3.V.2.3", null, false);
              Long op_cnt = (Long) sqlManager.getObject(selectW3FS3AWARD_C_CNT, new Object[] { id, item });
              if (op_cnt != null && op_cnt.longValue() == 1) {
                this.validazioneW3FS3AWARD_C(sqlManager, id, item, pagina, listaControlliW3FS3AWARD);
              } else {
                String descrizione = "Contraente";
                String messaggio = "Inserire uno ed un solo contraente";
                listaControlliW3FS3AWARD.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
              }

            }

            // Informazioni sul valore del contratto
            pagina = UtilityTags.getResource("label.simap.fs3.V.2.4", null, false);
            Double initial_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 12).getValue();
            ValidazioneUtility.validazioneCost(initial_cost, "W3FS3AWARD", "INITIAL_COST", pagina, listaControlliW3FS3AWARD);

            Double final_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 9).getValue();
            Double final_low = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 10).getValue();
            Double final_high = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 11).getValue();

            if (final_cost == null && final_low == null && final_high == null) {
              String descrizione = "Valore totale del contratto d'appalto";
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_LOW");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_HIGH");
              String messaggio = "Valorizzare il campo \"Valore totale del contratto d'appalto\"";
              messaggio += " oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_LOW");
              messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_HIGH") + "\"";
              listaControlliW3FS3AWARD.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

            if (final_cost != null && (final_low != null || final_high != null)) {
              String descrizione = "Valore totale del contratto d'appalto";
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_LOW");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_HIGH");
              String messaggio = "Valorizzare solamente il campo \"Valore totale del contratto d'appalto\"";
              messaggio += " oppure l'intervallo di valori \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_LOW");
              messaggio += " e " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_HIGH") + "\"";
              listaControlliW3FS3AWARD.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

            // Controllo che il valore finale (FINAL_COST) non sia superiore al
            // totale dell'appalto (W3FS3.SCOPE_COST)
            if (final_cost != null) {
              Double w3fs3_scope_cost = (Double) sqlManager.getObject("select scope_cost from w3fs3 where id = ?", new Object[] { id });
              if (w3fs3_scope_cost != null && final_cost.doubleValue() > w3fs3_scope_cost.doubleValue()) {
                String messaggio = ValidazioneUtility.getMessaggioConfrontoImporti(final_cost, "<=", w3fs3_scope_cost, "W3FS3",
                    "SCOPE_COST");
                messaggio += " indicato nella sezione II.1.7";
                ValidazioneUtility.addAvviso(listaControlliW3FS3AWARD, "W3FS3AWARD", "FINAL_COST", "E", pagina, messaggio);
              }
            }

            if (final_low == null && final_high != null) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_LOW");
              descrizione += ", " + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_HIGH");
              String messaggio = "Valorizzare anche \"" + ValidazioneUtility.getDescrizioneCampo("W3FS3AWARD", "FINAL_LOW") + "\"";
              listaControlliW3FS3AWARD.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

            if (final_low != null && final_high != null) {
              if (final_low.doubleValue() >= final_high.doubleValue()) {
                String messaggio = ValidazioneUtility.getMessaggioConfrontoImporti(final_low, "<", final_high, "W3FS3AWARD", "FINAL_HIGH");
                ValidazioneUtility.addAvviso(listaControlliW3FS3AWARD, "W3FS3AWARD", "FINAL_LOW", "E", pagina, messaggio);
              }
            }

            ValidazioneUtility.validazioneCost(final_cost, "W3FS3AWARD", "FINAL_COST", pagina, listaControlliW3FS3AWARD);
            ValidazioneUtility.validazioneCost(final_low, "W3FS3AWARD", "FINAL_LOW", pagina, listaControlliW3FS3AWARD);
            ValidazioneUtility.validazioneCost(final_high, "W3FS3AWARD", "FINAL_HIGH", pagina, listaControlliW3FS3AWARD);

            // Informazioni relative al subappalto
            pagina = UtilityTags.getResource("label.simap.fs3.V.2.5", null, false);
            String sub_contracted = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 13).getValue();
            Double sub_value = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 14).getValue();
            Double sub_prct = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 15).getValue();
            String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(i), 17).getValue();
            if (sub_contracted != null && "1".equals(sub_contracted)) {
              ValidazioneUtility.validazioneCost(sub_value, "W3FS3AWARD", "SUB_VALUE", pagina, listaControlliW3FS3AWARD);
              if (sub_prct != null && (sub_prct.doubleValue() <= 0 || sub_prct.doubleValue() > 100)) {
                String messaggio = "La percentuale indicata deve essere > 0 e <= 100";
                ValidazioneUtility.addAvviso(listaControlliW3FS3AWARD, "W3FS3AWARD", "SUB_PRCT", "E", pagina, messaggio);
              }
              ValidazioneUtility.validazioneS400(additional_information, "W3FS3AWARD", "ADDITIONAL_INFORMATION", pagina,
                  listaControlliW3FS3AWARD);
            }

          }

          if (!listaControlliW3FS3AWARD.isEmpty()) {
            ValidazioneUtility.setTitolo(listaControlli, "Aggiudicazione n. " + item.toString());
            for (int a = 0; a < listaControlliW3FS3AWARD.size(); a++) {
              listaControlli.add(listaControlliW3FS3AWARD.get(a));
            }
          }

        }
      }

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'aggiudicazione dell'appalto", "validazioneFS3AWARD",
          e);
    }
  }

  /**
   * Controllo dei contraenti.
   * 
   * @param sqlManager
   * @param id
   * @param item
   * @param pagina
   * @param listaControlliW3FS3AWARD
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneW3FS3AWARD_C(SqlManager sqlManager, Long id, Long item, String pagina, List<Object> listaControlliW3FS3AWARD)
      throws SQLException, GestoreException {
    String selectW3FS3AWARD_C = "select num, contractor_codimp, contractor_sme from w3fs3award_c where id = ? and item = ?";
    List<?> datiW3FS3AWARD_C = sqlManager.getListVector(selectW3FS3AWARD_C, new Object[] { id, item });
    if (datiW3FS3AWARD_C != null && datiW3FS3AWARD_C.size() > 0) {
      for (int icont = 0; icont < datiW3FS3AWARD_C.size(); icont++) {
        Long num = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD_C.get(icont), 0).getValue();
        String pagina_c = pagina + " - Contraente n. " + num.toString();
        String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD_C.get(icont), 1).getValue();
        String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD_C.get(icont), 2).getValue();
        if (contractor_codimp == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD_C", "CONTRACTOR_CODIMP", pagina_c);
        } else {
          this.validazioneIMPR(sqlManager, contractor_codimp, pagina_c, listaControlliW3FS3AWARD);
        }
        if (contractor_sme == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlliW3FS3AWARD, "W3FS3AWARD_C", "CONTRACTOR_SME", pagina_c);
        }
      }
    }

  }

}