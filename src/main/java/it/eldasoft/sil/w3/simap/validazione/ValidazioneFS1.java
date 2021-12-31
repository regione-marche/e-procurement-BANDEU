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

public class ValidazioneFS1 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS1.class);

  /**
   * Controllo dati formulario standard 1: avviso di preinformazione.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS1(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS1: inizio metodo");
    try {

      // Tipo di preinformazione
      Long notice_f01 = (Long) sqlManager.getObject("select notice_f01 from w3fs1 where id = ?", new Object[] { id });
      if (notice_f01 == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "NOTICE_F01", "");
      }
      this.validazione_FS1_Sezione_I(sqlManager, id, listaControlli);
      this.validazioneFS1_Sezione_II(sqlManager, id, listaControlli);
      this.validazioneNumeroMassimoLotti(sqlManager, id, listaControlli);
      this.validazione_FS1_W3ANNEXB(sqlManager, id, null, listaControlli);
      this.validazioneFS1_Sezione_III(sqlManager, id, listaControlli);
      this.validazioneFS1_Sezione_IV(sqlManager, id, listaControlli);
      this.validazioneFS1_Sezione_VI(sqlManager, id, listaControlli);

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'avviso di preinformazione", "validazioneFS1", e);
    }
    if (logger.isDebugEnabled()) logger.debug("validazioneFS1: fine metodo");
  }

  /**
   * Validazione della sezione I: amministrazione aggiudicatrice. Versione
   * specializzata per FS1.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  public void validazione_FS1_Sezione_I(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

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

        // Tipo avviso di preinformazione
        Long notice_f01 = (Long) sqlManager.getObject("select notice_f01 from w3fs1 where id = ?", new Object[] { id });

        // Base legale
        String legal_basis = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 13).getValue();
        if (legal_basis == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "LEGAL_BASIS", "");
        }

        String form = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 0).getValue();
        form = form.toLowerCase();

        // Amministrazione aggiudicatrice, controllo del collegamento e dei dati
        // dell'archivio W3AMMI e UFFINT
        pagina = UtilityTags.getResource("label.simap." + form + ".I.1", null, false);
        String codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 1).getValue();
        if (codamm == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "CODAMM", pagina);
        } else {
          validazioneW3AMMI(sqlManager, form, codamm, listaControlli, "CA");
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
                  String codeinAddr = (String) sqlManager.getObject("select codein from w3ammi where codamm = ?",
                      new Object[] { codammAddr });
                  if (codeinAddr == null) {
                    pagina = pagina + " - Amministrazione";
                    ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3AMMI", "CODEIN", pagina);
                  } else {
                    pagina = pagina + " - Amministrazione - Ufficio/punto di contatto";
                    validazioneUFFINT_S1_S5_S6(sqlManager, codeinAddr, pagina, listaControlli, "S1");
                  }
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
        // Il controllo deve essere effettuata in funzione dell'utilizzo del
        // formulario FS1
        pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Disponibilita' dei documenti di gara";
        Long document_fu_re = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAP, 10).getValue();
        String document_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 8).getValue();
        if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
          if (document_fu_re != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3SIMAP", "DOCUMENT_FU_RE", pagina);
          if (document_url != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3SIMAP", "DOCUMENT_URL", pagina);
        } else {
          if (document_fu_re != null) {
            if (document_url == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "DOCUMENT_URL", pagina);
            } else {
              ValidazioneUtility.validazioneURL(document_url, "W3SIMAP", "DOCUMENT_URL", pagina, listaControlli);
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
            validazioneUFFINT_S1_S5_S6(sqlManager, further_info_codein, pagina, listaControlli, "S1");
          }
        }

        // Offerte e domande di partecipazione
        pagina = UtilityTags.getResource("label.simap." + form + ".I.3", null, false) + " - Offerte e domande di partecipazione";
        String participation = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 4).getValue();
        String participation_codein = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 5).getValue();

        if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
          if (participation != null) {
            String descrizione = "Le offerte e le domande di partecipazione vanno inviate presso";
            String messaggio = "Il campo non deve essere indicato";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }
          if (participation_codein != null)
            ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3SIMAP", "PARTICIPATION_CODEIN", pagina);
        } else {
          if (participation == null) {
            String descrizione = "Le offerte e le domande di partecipazione vanno inviate presso";
            String messaggio = "Il campo &egrave; obbligatorio";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          } else if ("2".equals(participation)) {
            if (participation_codein == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "PARTICIPATION_CODEIN", pagina);
            } else {
              validazioneUFFINT_S1_S5_S6(sqlManager, participation_codein, pagina, listaControlli, "S1");
            }
          }
        }

        // In versione elettronica ?
        String participation_el = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 6).getValue();
        String participation_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 7).getValue();
        if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
          if (participation_el != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3SIMAP", "PARTICIPATION_EL", pagina);
          if (participation_url != null)
            ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3SIMAP", "PARTICIPATION_URL", pagina);
        } else {
          if (participation_el != null && "1".equals(participation_el)) {
            if (participation_url == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "PARTICIPATION_URL", pagina);
            } else {
              ValidazioneUtility.validazioneURL(participation_url, "W3SIMAP", "PARTICIPATION_URL", pagina, listaControlli);
            }
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
   * Sezione II - Oggetto
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS1_Sezione_II(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS1 = "select w3fs1.notice_f01, " // 0
        + " w3fs1s2.title_contract, " // 1
        + " w3fs1s2.cpv, " // 2
        + " w3fs1s2.type_contract, " // 3
        + " w3fs1s2.scope_total, " // 4
        + " w3fs1s2.div_into_lots, " // 5
        + " w3fs1s2.div_lots, " // 6
        + " w3fs1s2.div_lots_max, " // 7
        + " w3fs1s2.lots_max_tenderer, " // 8
        + " w3fs1s2.date_publi, " // 9
        + " w3fs1s2.scope_cost, " // 10
        + " w3fs1s2.reference, " // 11
        + " w3fs1s2.lots_combining " // 12
        + " from w3fs1, w3fs1s2 "
        + " where w3fs1.id = w3fs1s2.id and w3fs1.id = ? and w3fs1s2.num = 1";

    List<?> datiFS1 = sqlManager.getVector(selectFS1, new Object[] { id });

    String pagina = "";

    Long notice_f01 = (Long) SqlManager.getValueFromVectorParam(datiFS1, 0).getValue();

    // Denominazione dell'appalto
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.1", null, false);
    String title_contract = (String) SqlManager.getValueFromVectorParam(datiFS1, 1).getValue();
    if (title_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "TITLE_CONTRACT", pagina);
    } else {
      ValidazioneUtility.validazioneS200(title_contract, "W3FS1S2", "TITLE_CONTRACT", pagina, listaControlli);
    }

    // Numero di riferimento
    String reference = (String) SqlManager.getValueFromVectorParam(datiFS1, 11).getValue();
    if (reference != null) {
      ValidazioneUtility.validazioneS100(reference, "W3FS1S2", "REFERENCE", pagina, listaControlli);
    }

    String type_contract = (String) SqlManager.getValueFromVectorParam(datiFS1, 3).getValue();

    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.2", null, false);
    String cpv = (String) SqlManager.getValueFromVectorParam(datiFS1, 2).getValue();
    if (cpv == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "CPV", pagina);
    }

    // Controllo corrispondenza CPV con il tipo appalto
    ValidazioneUtility.validazioneCorrispondenzaCPV(listaControlli, pagina, cpv, type_contract, "W3FS1S2", "CPV");

    // Tipo di appalto
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.3", null, false);
    if (type_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "TYPE_CONTRACT", pagina);
    }

    // Breve descrizione
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.4", null, false);
    String scope_total = (String) SqlManager.getValueFromVectorParam(datiFS1, 4).getValue();
    if (scope_total == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "SCOPE_TOTAL", pagina);
    } else {
      ValidazioneUtility.validazioneS1000(scope_total, "W3FS1S2", "SCOPE_TOTAL", pagina, listaControlli);
    }

    // Valore stimato
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.5", null, false);
    Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiFS1, 10).getValue();
    ValidazioneUtility.validazioneCost(scope_cost, "W3FS1S2", "SCOPE_COST", pagina, listaControlli);

    // Appalto suddiviso in lotti ?
    pagina = UtilityTags.getResource("label.simap.fs1.II.1.6", null, false);
    String div_into_lots = (String) SqlManager.getValueFromVectorParam(datiFS1, 5).getValue();
    if (div_into_lots == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "DIV_INTO_LOTS", pagina);
    } else if ("1".equals(div_into_lots)) {
      String div_lots = (String) SqlManager.getValueFromVectorParam(datiFS1, 6).getValue();
      Long div_lots_max = (Long) SqlManager.getValueFromVectorParam(datiFS1, 7).getValue();
      Long lots_max_tenderer = (Long) SqlManager.getValueFromVectorParam(datiFS1, 8).getValue();

      if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
        if (div_lots != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1S2", "DIV_LOTS", pagina);
        if (div_lots_max != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1S2", "DIV_LOTS_MAX", pagina);
        if (lots_max_tenderer != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1S2", "LOTS_MAX_TENDERER", pagina);

      } else {
        // Le offerte vanno presentare per tutti i lotti, solo ...
        if (div_lots == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "DIV_LOTS", pagina);
        }

        // Numero massimo di lotti su cui presentare le offerte
        if (div_lots != null && "2".equals(div_lots)) {
          if (div_lots_max == null) {
            ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "DIV_LOTS_MAX", pagina);
          } else {
            Long cntW3ANNEXB = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ?", new Object[] { id });
            if (cntW3ANNEXB != null && div_lots_max.longValue() > cntW3ANNEXB.longValue()) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS1S2", "DIV_LOTS_MAX");
              String messaggio = "Il numero di lotti indicato deve essere inferiore o uguale al numero totale dei lotti";
              listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

            if (div_lots_max.longValue() <= 0) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS1S2", "DIV_LOTS_MAX");
              String messaggio = "Il numero di lotti indicato deve maggiore di zero";
              listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }

          }
        }

        // Numero massimo di lotti aggiudicabili ad un singolo offerente
        if (lots_max_tenderer == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "LOTS_MAX_TENDERER", pagina);
        } else {
          Long cntW3ANNEXB = (Long) sqlManager.getObject("select count(*) from w3annexb where id = ?", new Object[] { id });
          if (cntW3ANNEXB != null && lots_max_tenderer.longValue() > cntW3ANNEXB.longValue()) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS1S2", "LOTS_MAX_TENDERER");
            String messaggio = "Il numero di lotti indicato deve essere inferiore o uguale al numero totale dei lotti";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }

          if (lots_max_tenderer.longValue() <= 0) {
            String descrizione = ValidazioneUtility.getDescrizioneCampo("W3FS1S2", "LOTS_MAX_TENDERER");
            String messaggio = "Il numero di lotti indicato deve essere maggiore di zero";
            listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
          }

        }
      }

      // L'amministrazione aggiudicatrice si riserva la facoltà di aggiudicare i
      // contratti d'appalto combinando i seguenti lotti o gruppi di lotti
      String lots_combining = (String) SqlManager.getValueFromVectorParam(datiFS1, 12).getValue();
      if (lots_combining != null) {
        ValidazioneUtility.validazioneS400(lots_combining, "W3FS1S2", "LOTS_COMBINING", pagina, listaControlli);
      }

    }

    // Data prevista pubblicazione
    pagina = UtilityTags.getResource("label.simap.fs1.II.3", null, false);
    Date date_publi = (Date) SqlManager.getValueFromVectorParam(datiFS1, 9).getValue();
    if (NOTICE_F01_PRI_CALL_COMPETITION.equals(notice_f01)) {
      if (date_publi != null) {
        ValidazioneUtility.addNotForeseenF01PriCallCompetition(listaControlli, "W3FS1S2", "DATE_PUBLI", pagina);
      }
    } else {
      if (date_publi == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1S2", "DATE_PUBLI", pagina);
      }
    }

  }

  /**
   * Validazione dei lotti associati alla comunicazione. Versione specializzata
   * per il formulario FS1.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @param w3annexb_num
   *        Eventuale numero di lotto
   * @throws GestoreException
   */
  public void validazione_FS1_W3ANNEXB(SqlManager sqlManager, Long id, Long w3annexb_num, List<Object> listaControlli)
      throws GestoreException {

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
        + " w3annexb.additional_information " // 26
        + " from w3annexb where w3annexb.id = ?";

    if (w3annexb_num != null) {
      selectW3ANNEXB += " and w3annexb.num = " + w3annexb_num.toString();
    }

    String selectW3CPV = "select numcpv, cpv from w3cpv where ent = ? and id = ? and num = ?";
    String selectW3AWCRITERIA_CNT = "select count(*) from w3awcriteria where id = ? and num = ?";

    try {

      Long notice_f01 = (Long) sqlManager.getObject("select notice_f01 from w3fs1 where id = ?", new Object[] { id });

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
            ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "LOTNUM", pagina);
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
          String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs1s2 where id = ?", new Object[] { id });
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
          String ac_doc = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 4).getValue();
          Long cntW3AWCRITERIA = (Long) sqlManager.getObject(selectW3AWCRITERIA_CNT, new Object[] { id, num });

          if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
            if (ac_doc != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "AC_DOC", pagina);
            }
            if (cntW3AWCRITERIA != null && cntW3AWCRITERIA.longValue() > 0) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "AC_DOC");
              String messaggio = "Non deve essere valorizzata la tabella con i criteri";
              listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }
          } else {
            if (ac_doc != null) {
              if ("1".equals(ac_doc)) {
                if (cntW3AWCRITERIA != null && cntW3AWCRITERIA.longValue() > 0) {
                  String descrizione = ValidazioneUtility.getDescrizioneCampo("W3ANNEXB", "AC_DOC");
                  String messaggio = "Se i criteri di aggiudicazione sono indicati solo nei documenti di gara non deve essere valorizzata la tabella con i criteri";
                  listaControlliW3ANNEXB.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
                }
              } else if ("2".equals(ac_doc)) {
                validazioneAWCRITERIA(sqlManager, id, num, pagina, listaControlliW3ANNEXB);
              }
            }
          }

          // Valore stimato
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.6", null, false);
          Double w3annexb_cost = (Double) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 24).getValue();
          ValidazioneUtility.validazioneCost(w3annexb_cost, "W3ANNEXB", "COST", pagina, listaControlliW3ANNEXB);

          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.7", null, false);
          Long work_month = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 5).getValue();
          Long work_days = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 6).getValue();
          Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 7).getValue();
          Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 8).getValue();
          String renewal = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 9).getValue();
          String renewal_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 10).getValue();

          if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
            if (work_month != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "WORK_MONTH", pagina);
            }
            if (work_days != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "WORK_DAYS", pagina);
            }
            if (work_start_date != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "WORK_START_DATE", pagina);
            }
            if (work_end_date != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "WORK_END_DATE", pagina);
            }
            if (renewal != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "RENEWAL", pagina);
            }
            if (renewal_descr != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "RENEWAL_DESCR", pagina);
            }

          } else {
            // Durata del contratto
            // Indicare la durata con una delle seguenti informazioni (mesi,
            // giorni, oppure da a)
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

          // Varianti ?
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.10", null, false);
          String acc_variants = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 15).getValue();
          if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
            if (acc_variants != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "ACC_VARIANTS", pagina);
            }
          } else {
            if (acc_variants == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "ACC_VARIANTS", pagina);
            }
          }

          // Il contratto prevede opzioni, verificare la descrizione delle
          // opzioni
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.11", null, false);
          String options = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 11).getValue();
          String options_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 12).getValue();
          if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
            if (options != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "OPTIONS", pagina);
            }
            if (options_descr != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "OPTIONS_DESCR", pagina);
            }
          } else {
            if ("1".equals(options)) {
              if (options_descr == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "OPTIONS_DESCR", pagina);
              } else {
                ValidazioneUtility.validazioneS4000(options_descr, "W3ANNEXB", "OPTIONS_DESCR", pagina, listaControlliW3ANNEXB);
              }
            }
          }

          // Fondi europei, verificare numero di riferimento del progetto
          pagina = UtilityTags.getResource("label.simap." + form + ".II.2.13", null, false);
          String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 13).getValue();
          String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(i), 14).getValue();
          if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
            if (eu_progr != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "EU_PROGR", pagina);
            }
            if (eu_progr_descr != null) {
              ValidazioneUtility.addNotForeseenF01PriOnly(listaControlliW3ANNEXB, "W3ANNEXB", "EU_PROGR_DESCR", pagina);
            }
          } else {
            if (eu_progr == null) {
              ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "EU_PROGR", pagina);
            } else if ("1".equals(eu_progr)) {
              if (eu_progr_descr == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlliW3ANNEXB, "W3ANNEXB", "EU_PROGR_DESCR", pagina);
              } else {
                ValidazioneUtility.validazioneS400(eu_progr_descr, "W3ANNEXB", "EU_PROGR_DESCR", pagina, listaControlliW3ANNEXB);
              }
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
   * Sezione III - Informazioni giuridiche...
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS1_Sezione_III(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS1 = "select eco_criteria_doc, " // 0
        + " eco_fin_info, " // 1
        + " eco_fin_min_level, " // 2
        + " tech_crit_doc, " // 3
        + " tech_prof_info, " // 4
        + " tech_prof_min_level, " // 5
        + " suitability, " // 6
        + " restricted_sheltered, " // 7
        + " restricted_frame, " // 8
        + " particular_profession, " // 9
        + " reference_to_law, " // 10
        + " performance_conditions, " // 11
        + " performance_staff " // 12
        + " from w3fs1 "
        + " where id = ?";

    List<?> datiFS1 = sqlManager.getVector(selectFS1, new Object[] { id });

    String pagina = "";

    // Controllo dimensione massime
    String suitability = (String) SqlManager.getValueFromVectorParam(datiFS1, 6).getValue();
    pagina = UtilityTags.getResource("label.simap.fs1.III.1.1", null, false);
    ValidazioneUtility.validazioneS4000(suitability, "W3FS1", "SUITABILITY", pagina, listaControlli);

    String eco_fin_info = (String) SqlManager.getValueFromVectorParam(datiFS1, 1).getValue();
    String eco_fin_min_level = (String) SqlManager.getValueFromVectorParam(datiFS1, 2).getValue();
    pagina = UtilityTags.getResource("label.simap.fs1.III.1.2", null, false);
    ValidazioneUtility.validazioneS4000(eco_fin_info, "W3FS1", "ECO_FIN_INFO", pagina, listaControlli);
    ValidazioneUtility.validazioneS4000(eco_fin_min_level, "W3FS1", "ECO_FIN_MIN_LEVEL", pagina, listaControlli);

    String tech_prof_info = (String) SqlManager.getValueFromVectorParam(datiFS1, 4).getValue();
    String tech_prof_min_level = (String) SqlManager.getValueFromVectorParam(datiFS1, 5).getValue();
    pagina = UtilityTags.getResource("label.simap.fs1.III.1.3", null, false);
    ValidazioneUtility.validazioneS4000(tech_prof_info, "W3FS1", "TECH_PROF_INFO", pagina, listaControlli);
    ValidazioneUtility.validazioneS4000(tech_prof_min_level, "W3FS1", "TECH_PROF_MIN_LEVEL", pagina, listaControlli);

    pagina = UtilityTags.getResource("label.simap.fs1.III.2.1", null, false);
    String reference_to_law = (String) SqlManager.getValueFromVectorParam(datiFS1, 10).getValue();
    ValidazioneUtility.validazioneS1500(reference_to_law, "W3FS1", "REFERENCE_TO_LAW", pagina, listaControlli);

    pagina = UtilityTags.getResource("label.simap.fs1.III.2.2", null, false);
    String performance_conditions = (String) SqlManager.getValueFromVectorParam(datiFS1, 11).getValue();
    ValidazioneUtility.validazioneS1000(performance_conditions, "W3FS1", "PERFORMANCE_CONDITIONS", pagina, listaControlli);

    Long notice_f01 = (Long) sqlManager.getObject("select notice_f01 from w3fs1 where id = ?", new Object[] { id });
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {

      pagina = UtilityTags.getResource("label.simap.fs1.III.1.1", null, false);
      if (SqlManager.getValueFromVectorParam(datiFS1, 6).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "SUITABILITY", pagina);
      }

      pagina = UtilityTags.getResource("label.simap.fs1.III.1.2", null, false);
      if (SqlManager.getValueFromVectorParam(datiFS1, 0).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "ECO_CRITERIA_DOC", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 1).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "ECO_FIN_INFO", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 2).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "ECO_FIN_MIN_LEVEL", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 3).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "TECH_CRIT_DOC", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 4).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "TECH_PROF_INFO", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 5).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "TECH_PROF_MIN_LEVEL", pagina);
      }

      pagina = UtilityTags.getResource("label.simap.fs1.III.1.5", null, false);
      if (SqlManager.getValueFromVectorParam(datiFS1, 7).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "RESTRICTED_SHELTERED", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 8).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "RESTRICTED_FRAME", pagina);
      }

      pagina = UtilityTags.getResource("label.simap.fs1.III.2.1", null, false);
      if (SqlManager.getValueFromVectorParam(datiFS1, 9).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "PARTICULAR_PROFESSION", pagina);
      }
      if (SqlManager.getValueFromVectorParam(datiFS1, 10).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "REFERENCE_TO_LAW", pagina);
      }

      pagina = UtilityTags.getResource("label.simap.fs1.III.2.2", null, false);
      if (SqlManager.getValueFromVectorParam(datiFS1, 11).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "PERFORMANCE_CONDITIONS", pagina);
      }

      pagina = UtilityTags.getResource("label.simap.fs1.III.2.3", null, false);
      if (SqlManager.getValueFromVectorParam(datiFS1, 12).getValue() != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "PERFORMANCE_STAFF", pagina);
      }

    } else {

      // Capacita' economica e finanziaria
      pagina = UtilityTags.getResource("label.simap.fs1.III.1.2", null, false);
      String eco_criteria_doc = (String) SqlManager.getValueFromVectorParam(datiFS1, 0).getValue();
      if (eco_criteria_doc != null && "2".equals(eco_criteria_doc)) {
        if (SqlManager.getValueFromVectorParam(datiFS1, 1).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "ECO_FIN_INFO", pagina);
        }
        if (SqlManager.getValueFromVectorParam(datiFS1, 2).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "ECO_FIN_MIN_LEVEL", pagina);
        }
      }

      // Capacita' professionale e tecnica
      pagina = UtilityTags.getResource("label.simap.fs1.III.1.3", null, false);
      String tech_crit_doc = (String) SqlManager.getValueFromVectorParam(datiFS1, 3).getValue();
      if (tech_crit_doc != null && "2".equals(tech_crit_doc)) {
        if (SqlManager.getValueFromVectorParam(datiFS1, 4).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "TECH_PROF_INFO", pagina);
        }
        if (SqlManager.getValueFromVectorParam(datiFS1, 5).getValue() == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "TECH_PROF_MIN_LEVEL", pagina);
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
  private void validazioneFS1_Sezione_IV(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {

    String selectFS1 = "select type_procedure, " // 0
        + " framework, " // 1
        + " fr_several_op, " // 2
        + " gpa, " // 3
        + " date_receipt, " // 4
        + " review_body_codein, " // 5
        + " fr_justification, " // 6
        + " dps, " // 7
        + " eauction, " // 8
        + " time_receipt, " // 9
        + " date_award, " // 10
        + " notice_date, " // 11
        + " eauction_info " // 12
        + " from w3fs1 "
        + " where id = ? ";

    List<?> datiFS1 = sqlManager.getVector(selectFS1, new Object[] { id });

    Long notice_f01 = (Long) sqlManager.getObject("select notice_f01 from w3fs1 where id = ?", new Object[] { id });

    String pagina = "";

    // Tipo di procedura
    Long type_procedure = (Long) SqlManager.getValueFromVectorParam(datiFS1, 0).getValue();
    pagina = UtilityTags.getResource("label.simap.fs1.IV.1.1", null, false);

    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (type_procedure != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "TYPE_PROCEDURE", pagina);
    } else if (NOTICE_F01_PRI_REDUCING_TIME_LIMITS.equals(notice_f01)) {
      if (type_procedure != null)
        ValidazioneUtility.addNotForeseenF01PriReducingTimeLimits(listaControlli, "W3FS1", "TYPE_PROCEDURE", pagina);
    } else {
      if (type_procedure == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "TYPE_PROCEDURE", pagina);
      }
    }

    // Numero di operatori nel caso di accordo quadro
    pagina = UtilityTags.getResource("label.simap.fs1.IV.1.3", null, false);
    String framework = (String) SqlManager.getValueFromVectorParam(datiFS1, 1).getValue();
    String fr_several_op = (String) SqlManager.getValueFromVectorParam(datiFS1, 2).getValue();
    String fr_justification = (String) SqlManager.getValueFromVectorParam(datiFS1, 2).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (framework != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "FRAMEWORK", pagina);
    } else {
      if (framework != null && "1".equals(framework)) {
        if (fr_several_op == null) {
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "FR_SEVERAL_OP", pagina);
        }
        ValidazioneUtility.validazioneS400(fr_justification, "W3FS1", "FR_JUSTIFICATION", pagina, listaControlli);
      }
    }

    // Sistema dinamico di acquisizione
    String dps = (String) SqlManager.getValueFromVectorParam(datiFS1, 7).getValue();
    if (dps != null && NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "DPS", pagina);
    }

    // Asta elettronica ?
    pagina = UtilityTags.getResource("label.simap.fs1.IV.1.6", null, false);
    String eauction = (String) SqlManager.getValueFromVectorParam(datiFS1, 8).getValue();
    if (eauction != null && NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "EAUCTION", pagina);
    }
    String eauction_info = (String) SqlManager.getValueFromVectorParam(datiFS1, 12).getValue();
    ValidazioneUtility.validazioneS400(eauction_info, "W3FS1", "EAUCTION", pagina, listaControlli);

    // Accordo sugli appalti pubblici (AAP)
    pagina = UtilityTags.getResource("label.simap.fs1.IV.1.8", null, false);
    if (SqlManager.getValueFromVectorParam(datiFS1, 3).getValue() == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "GPA", pagina);
    }

    // Termine ricezione manifestazione di interesse
    pagina = UtilityTags.getResource("label.simap.fs1.IV.2.2", null, false);
    Date date_receipt = (Date) SqlManager.getValueFromVectorParam(datiFS1, 4).getValue();
    String time_receipt = (String) SqlManager.getValueFromVectorParam(datiFS1, 9).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (date_receipt != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "DATE_RECEIPT", pagina);
      if (time_receipt != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "TIME_RECEIPT", pagina);
    } else if (NOTICE_F01_PRI_REDUCING_TIME_LIMITS.equals(notice_f01)) {
      if (date_receipt != null) ValidazioneUtility.addNotForeseenF01PriReducingTimeLimits(listaControlli, "W3FS1", "DATE_RECEIPT", pagina);
      if (time_receipt != null) ValidazioneUtility.addNotForeseenF01PriReducingTimeLimits(listaControlli, "W3FS1", "TIME_RECEIPT", pagina);
    } else {
      if (date_receipt == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "DATE_RECEIPT", pagina);
      if (time_receipt == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "TIME_RECEIPT", pagina);
    }

    if (date_receipt != null) {
      // Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiFS1,
      // 11).getValue();
      Date notice_date = UtilityDate.getDataOdiernaAsDate();
      if (notice_date != null) {
        if (date_receipt.getTime() <= notice_date.getTime()) {
          String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(date_receipt, ">", notice_date, "W3FS1", "NOTICE_DATE");
          ValidazioneUtility.addAvviso(listaControlli, "W3FS1", "DATE_RECEIPT", "E", pagina, messaggio);
        }
      } else {
        Date notice_date_today = new Date();
        if (date_receipt.getTime() <= notice_date_today.getTime()) {
          String messaggio = ValidazioneUtility.getMessaggioConfrontoDate(date_receipt, ">", notice_date_today, "W3FS1", "NOTICE_DATE");
          ValidazioneUtility.addAvviso(listaControlli, "W3FS1", "DATE_RECEIPT", "E", pagina, messaggio);
        }
      }
    }

    // Lingue
    pagina = UtilityTags.getResource("label.simap.fs1.IV.2.4", null, false);
    String selectW3LANGUAGE = "select count(*) from w3language where id = ?";
    Long cnt_W3LANGUAGE = (Long) sqlManager.getObject(selectW3LANGUAGE, new Object[] { id });
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (cnt_W3LANGUAGE != null && cnt_W3LANGUAGE.longValue() > 0) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3LANGUAGE", "LANGUAGE_EC", pagina);
      }
    } else if (NOTICE_F01_PRI_REDUCING_TIME_LIMITS.equals(notice_f01)) {
      if (cnt_W3LANGUAGE != null && cnt_W3LANGUAGE.longValue() > 0) {
        ValidazioneUtility.addNotForeseenF01PriReducingTimeLimits(listaControlli, "W3LANGUAGE", "LANGUAGE_EC", pagina);
      }
    } else {
      if (cnt_W3LANGUAGE == null || (cnt_W3LANGUAGE != null && cnt_W3LANGUAGE.longValue() == 0)) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3LANGUAGE", "LANGUAGE_EC", pagina);
      }
    }

    // Data prevista avvio procedura di aggiudicazione
    Date date_award = (Date) SqlManager.getValueFromVectorParam(datiFS1, 10).getValue();
    if (date_award != null && NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "DATE_AWARD", pagina);
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
  private void validazioneFS1_Sezione_VI(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException, GestoreException {
    String selectFS1 = "select review_body_codein, " // 0
        + " mediation_body_codein, " // 1
        + " review_info_codein, " // 2
        + " notice_date, " // 3
        + " eordering, " // 4
        + " einvoicing, " // 5
        + " epayment, " // 6
        + " review_procedure, " // 7
        + " additional_information " // 8
        + " from w3fs1 where id = ? ";
    List<?> datiFS1 = sqlManager.getVector(selectFS1, new Object[] { id });

    Long notice_f01 = (Long) sqlManager.getObject("select notice_f01 from w3fs1 where id = ?", new Object[] { id });

    String pagina = "";

    // Informazioni relativi ai flussi elettronici
    pagina = UtilityTags.getResource("label.simap.fs1.VI.2", null, false);
    String eordering = (String) SqlManager.getValueFromVectorParam(datiFS1, 4).getValue();
    String einvoicing = (String) SqlManager.getValueFromVectorParam(datiFS1, 5).getValue();
    String epayment = (String) SqlManager.getValueFromVectorParam(datiFS1, 6).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (eordering != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "EORDERING", pagina);
      if (einvoicing != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "EINVOICING", pagina);
      if (epayment != null) ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "EPAYMENT", pagina);
    }

    // Informazioni complementari
    String additional_information = (String) SqlManager.getValueFromVectorParam(datiFS1, 8).getValue();
    ValidazioneUtility.validazioneS4000(additional_information, "W3FS1", "ADDITIONAL_INFORMATION", pagina, listaControlli);

    // Organismo responsabile delle procedure di ricorso
    pagina = UtilityTags.getResource("label.simap.fs1.VI.4.1", null, false);
    String review_body_codein = (String) SqlManager.getValueFromVectorParam(datiFS1, 0).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (review_body_codein != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "REVIEW_BODY_CODEIN", pagina);
      }
    } else {
      if (review_body_codein == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS1", "REVIEW_BODY_CODEIN", pagina);
      } else {
        this.validazioneUFFINT_S1_S5_S6(sqlManager, review_body_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
      }
    }

    // Organismo responsabile delle procedure di mediazione
    pagina = UtilityTags.getResource("label.simap.fs1.VI.4.2", null, false);
    String mediation_body_codein = (String) SqlManager.getValueFromVectorParam(datiFS1, 1).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (mediation_body_codein != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "MEDIATION_BODY_CODEIN", pagina);
      }
    } else {
      if (mediation_body_codein != null) {
        this.validazioneUFFINT_S1_S5_S6(sqlManager, mediation_body_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
      }
    }

    // Procedura di ricorso
    pagina = UtilityTags.getResource("label.simap.fs1.VI.4.3", null, false);
    String review_procedure = (String) SqlManager.getValueFromVectorParam(datiFS1, 7).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (review_procedure != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "REVIEW_PROCEDURE", pagina);
      }
    }
    ValidazioneUtility.validazioneS4000(review_procedure, "W3FS1", "REVIEW_PROCEDURE", pagina, listaControlli);

    // Servizio presso il quale sono disponibili...
    pagina = UtilityTags.getResource("label.simap.fs1.VI.4.4", null, false);
    String review_info_codein = (String) SqlManager.getValueFromVectorParam(datiFS1, 2).getValue();
    if (NOTICE_F01_PRI_ONLY.equals(notice_f01)) {
      if (review_info_codein != null) {
        ValidazioneUtility.addNotForeseenF01PriOnly(listaControlli, "W3FS1", "REVIEW_INFO_CODEIN", pagina);
      }
    } else {
      if (review_info_codein != null) {
        this.validazioneUFFINT_S1_S5_S6(sqlManager, review_info_codein, pagina + " - Ufficio o punto di contatto", listaControlli, "S6");
      }
    }

  }

}