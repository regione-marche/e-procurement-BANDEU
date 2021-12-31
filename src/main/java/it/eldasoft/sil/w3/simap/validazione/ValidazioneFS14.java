package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ValidazioneFS14 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS14.class);

  /**
   * Controllo dati formulario standard 14: avviso relativo a informazioni
   * complementari, informazioni su procedure incomplete o rettifiche.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS14(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS14: inizio metodo");

    String pagina = "";

    try {

      // Controllo dei riferimenti originali
      pagina = "Dati del bando/avviso originale";
      Long id_rif = (Long) sqlManager.getObject("select id_rif from w3fs14 where id = ?", new Object[] { id });
      String form_rif = (String) sqlManager.getObject("select form_rif from w3fs14 where id = ?", new Object[] { id });
      String notice_number_oj = (String) sqlManager.getObject("select notice_number_oj from w3simap where id = ?", new Object[] { id_rif });
      if (notice_number_oj == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3SIMAP", "NOTICE_NUMBER_OJ", pagina);
      } else {
        ValidazioneUtility.validazioneNumberOJ(notice_number_oj, "W3SIMAP", "NOTICE_NUMBER_OJ", pagina, listaControlli);
      }
      String selectNoticeDate = "";
      if ("FS1".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs1 where id = ?";
      } else if ("FS2".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs2 where id = ?";
      } else if ("FS3".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs3 where id = ?";
      } else if ("FS4".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs4 where id = ?";
      } else if ("FS5".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs5 where id = ?";
      } else if ("FS6".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs6 where id = ?";
      } else if ("FS7".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs7 where id = ?";        
      } else if ("FS8".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs8 where id = ?";
      } else if ("FS20".equals(form_rif)) {
        selectNoticeDate = "select notice_date from w3fs20 where id = ?";
      }
      Date notice_date_original = (Date) sqlManager.getObject(selectNoticeDate, new Object[] { id_rif });
      if (notice_date_original == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3" + form_rif, "NOTICE_DATE", pagina);
      }

      // Motivo della modifica
      pagina = UtilityTags.getResource("label.simap.fs14.VII.1.1", null, false);
      String correct_reason = (String) sqlManager.getObject("select correct_reason from w3fs14 where id = ?", new Object[] { id });
      if (correct_reason == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS14", "CORRECT_REASON", pagina);
      }

      Long countW3FS14CORR = (Long) sqlManager.getObject("select count(*) from w3fs14corr where id = ?", new Object[] { id });
      String other_information = (String) sqlManager.getObject("select other_information from w3fs14 where id = ?", new Object[] { id });
      if (countW3FS14CORR == null || (countW3FS14CORR != null && countW3FS14CORR.longValue() == 0)) {
        if (other_information == null) {
          pagina = UtilityTags.getResource("label.simap.fs14.VII.1.2", null, false);
          pagina += " e " + UtilityTags.getResource("label.simap.fs14.VII.2", null, false);
          String descrizione = "Correzione o altre informazioni";
          String messaggio = "Valorizzare almeno una sezione tra quelle indicate";
          listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
        }
      }
      ValidazioneUtility.validazioneS4000(other_information, "W3FS14", "OTHER_INFORMATION", pagina, listaControlli);

      this.validazioneW3FS14CORR(sqlManager, id, listaControlli);

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative alla rettifica", "validazioneFS14", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS14: fine metodo");
  }

  /**
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  private void validazioneW3FS14CORR(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {

    if (logger.isDebugEnabled()) logger.debug("validazioneFS14CORR: inizio metodo");

    String entita = "W3FS14CORR";

    String selectW3FS14CORR = "select num, " // 0
        + " object, " // 1
        + " type, " // 2
        + " section, " // 3
        + " text_old, " // 4
        + " text_new, " // 5
        + " date_old, " // 6
        + " date_new, " // 7
        + " time_old, " // 8
        + " time_new, " // 9
        + " nuts_old, " // 10
        + " nuts_new, " // 11
        + " cpv_old, " // 12
        + " cpv_new, " // 13
        + " cpvsupp1_old, " // 14
        + " cpvsupp1_new, " // 15
        + " cpvsupp2_old, " // 16
        + " cpvsupp2_new, " // 17
        + " codein_new, " // 18
        + " lot_no, " // 19
        + " label_text" // 20
        + " from w3fs14corr where id = ?";

    try {
      List<?> datiW3FS14CORR = sqlManager.getListVector(selectW3FS14CORR, new Object[] { id });
      if (datiW3FS14CORR != null && datiW3FS14CORR.size() > 0) {
        for (int i = 0; i < datiW3FS14CORR.size(); i++) {
          Long num = (Long) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 0).getValue();

          String pagina = UtilityTags.getResource("label.simap.fs14.VII.1.2", null, false) + " - Correzione n. " + num.toString();

          String object = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 1).getValue();
          if (object == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "OBJECT", pagina);

          String type = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 2).getValue();
          if (type == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "TYPE", pagina);

          String section = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 3).getValue();
          if (section == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "SECTION", pagina);

          String lot_no = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 19).getValue();
          if (lot_no != null) {
            String numero_sezione = section.substring(0, section.indexOf("."));
            if (numero_sezione != null
                && (numero_sezione.equals("III") || numero_sezione.equals("IV") || numero_sezione.equals("VI") || numero_sezione.equals("VII"))) {
              String descrizione = ValidazioneUtility.getDescrizioneCampo(entita, "LOT_NO");
              String messaggio = "Per le sezioni III, IV, VI e VII il numero del lotto non puo' essere indicato";
              listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
            }
          }
          ValidazioneUtility.validazioneS100(lot_no, entita, "LOT_NO", pagina, listaControlli);
          
          String label_text = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 20).getValue();
          ValidazioneUtility.validazioneS400(label_text, entita, "LABEL_TEXT", pagina, listaControlli);
          
          if (type != null && object != null) {
            if ("TEXT".equals(object)) {
              String text_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 4).getValue();
              String text_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 5).getValue();
              ValidazioneUtility.validazioneS4000(text_old, entita, "TEXT_OLD", pagina, listaControlli);
              ValidazioneUtility.validazioneS4000(text_new, entita, "TEXT_NEW", pagina, listaControlli);

              if (("DEL".equals(type) || "REP".equals(type)) && text_old == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "TEXT_OLD", pagina);
              }
              if (("ADD".equals(type) || "REP".equals(type)) && text_new == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "TEXT_NEW", pagina);
              }

            } else if ("DATE".equals(object)) {
              Date date_old = (Date) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 6).getValue();
              Date date_new = (Date) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 7).getValue();
              String time_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 8).getValue();
              String time_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 9).getValue();

              if (("DEL".equals(type) || "REP".equals(type))) {
                if (date_old == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "DATE_OLD", pagina);
                if (time_old == null) ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "TIME_OLD", pagina);
              }

              if (("ADD".equals(type) || "REP".equals(type)) && date_new == null)
                ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "DATE_NEW", pagina);
              if (("ADD".equals(type) || "REP".equals(type)) && time_new == null)
                ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "TIME_NEW", pagina);

            } else if ("CPV".equals(object) || "CPVA".equals(object)) {
              String cpv_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 12).getValue();
              String cpv_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(i), 13).getValue();

              if (("DEL".equals(type) || "REP".equals(type)) && cpv_old == null) {
                ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "CPV_OLD", pagina);
              }
              if (("ADD".equals(type) || "REP".equals(type)) && cpv_new == null)
                ValidazioneUtility.addCampoObbligatorio(listaControlli, entita, "CPV_NEW", pagina);

            }
          }
        }
      }

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative alla lista delle informazioni da rettificare",
          "validazioneFS14CORR", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS14CORR: fine metodo");

  }

}