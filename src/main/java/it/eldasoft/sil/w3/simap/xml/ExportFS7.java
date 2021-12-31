package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.AcDefinition;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF05;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF07;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF07;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.F072014Document.F072014;
import eu.europa.publications.resource.schema.ted.r209.reception.LeftiF07;
import eu.europa.publications.resource.schema.ted.r209.reception.NoticeF07;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF07;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF07;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.ProcedureF07;
import eu.europa.publications.resource.schema.ted.r209.reception.QUALIFICATIONDocument.QUALIFICATION;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFS7 extends ExportFormulariStandard {

  public F072014 exportF07(SqlManager sqlManager, Long id) throws GestoreException {

    F072014 f07 = F072014.Factory.newInstance();

    f07.setLG(TCeLanguageList.IT);
    f07.setCATEGORY(OriginalTranslation.ORIGINAL);
    f07.addNewFORM().setStringValue("F07");

    try {

      // Base legale
      String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id });
      if (legal_basis != null) {
        f07.addNewLEGALBASIS().setVALUE(
            eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
      }

      // Qsu call competition
      String qsu_call_competition = (String) sqlManager.getObject("select qsu_call_competition from w3fs7 where id = ?", new Object[] { id });
      if (qsu_call_competition != null && "1".equals(qsu_call_competition)) {
        f07.addNewNOTICE().addNewTYPE().setStringValue("QSU_CALL_COMPETITION");
      }
      
      f07.setCONTRACTINGBODY(_getBodyF07(sqlManager, id));
      f07.setOBJECTCONTRACT(_getObjectContractF07(sqlManager, id));
      f07.setLEFTI(_getLeftiF07(sqlManager, id));
      f07.setPROCEDURE(_getProcedureF07(sqlManager, id));
      f07.setCOMPLEMENTARYINFO(_getCiF07(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 7", "exportXML.sqlerror", e);
    }

    return f07;

  }

  /**
   * Sezione I
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private BodyF07 _getBodyF07(SqlManager sqlManager, Long id) throws SQLException {
    BodyF05 bodyF05 = getBodyF05CEGeneric(sqlManager, id);
    BodyF07 bodyF07 = (BodyF07) bodyF05.changeType(BodyF07.type);

    return bodyF07;
  }

  /**
   * Sezione II.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF07 _getObjectContractF07(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF07 objectContractF07 = ObjectContractF07.Factory.newInstance();

    String selectW3FS7 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " cpvsupp1, " // 3
        + " cpvsupp2, " // 4
        + " type_contract " // 5
        + " from w3fs7 "
        + " where id = ?";

    List<?> datiW3FS7 = sqlManager.getVector(selectW3FS7, new Object[] { id });
    if (datiW3FS7 != null && datiW3FS7.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 0).getValue();
      if (title_contract != null) {
        objectContractF07.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 1).getValue();
      if (reference != null) {
        objectContractF07.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 4).getValue();
      if (cpv != null) {
        objectContractF07.addNewCPVMAIN();
        objectContractF07.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF07.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF07.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF07.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF07.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF07.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      // Lotto
      objectContractF07.setOBJECTDESCR(_getObjectF07(sqlManager, id, new Long(1), new Long(1)));

    }

    return objectContractF07;
  }

  /**
   * Dati del lotto.
   * 
   * @param sqlManager
   * @param id
   * @param num
   * @return
   * @throws SQLException
   */
  private ObjectF07 _getObjectF07(SqlManager sqlManager, Long id, Long num, Long item_simap) throws SQLException {
    ObjectF07 objectF07 = ObjectF07.Factory.newInstance();

    String selectW3ANNEXB = "select site_nuts, " // 0
        + " site_nuts_2, " // 1
        + " site_nuts_3, " // 2
        + " site_nuts_4, " // 3
        + " site_label, " // 4
        + " description, " // 5
        + " ac_doc, " // 6
        + " q_date_start, " // 7
        + " q_date_stop, " // 8
        + " q_indefinite, " // 9
        + " q_renewal, " // 10
        + " q_renewal_descr, " // 11
        + " eu_progr, " // 12
        + " eu_progr_descr " // 13
        + " from w3annexb "
        + " where id = ? and num = ?";

    List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id, num });
    if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

      // CPV aggiuntivi
      String selectW3CPV = "select cpv, cpvsupp1, cpvsupp2 from w3cpv where id = ? and num = ?";
      List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { id, num });
      if (datiW3CPV != null && datiW3CPV.size() > 0) {
        for (int c = 0; c < datiW3CPV.size(); c++) {
          String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 0).getValue();
          String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 1).getValue();
          String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 2).getValue();
          if (cpv != null) {
            CpvSet cpvSet = objectF07.addNewCPVADDITIONAL();
            cpvSet.addNewCPVCODE().setCODE(getCPV(cpv));
            if (cpvsupp1 != null) {
              cpvSet.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
            }
            if (cpvsupp2 != null) {
              cpvSet.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
            }
          }
        }
      }

      // NUTS
      String site_nuts = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
      String site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();
      String site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 2).getValue();
      String site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 3).getValue();
      if (site_nuts != null) {
        objectF07.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF07.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF07.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF07.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 4).getValue();
      if (site_label != null) {
        objectF07.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 5).getValue();
      if (description != null) {
        objectF07.setSHORTDESCR(getTextFtMultiLines(description));
      }

      // Criteri di aggiudicazione
      String ac_doc = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
      if (ac_doc != null) {

        eu.europa.publications.resource.schema.ted.r209.reception.ObjectF07.AC ac = objectF07.addNewAC();

        if ("1".equals(ac_doc)) {
          ac.addNewACPROCUREMENTDOC();
        } else {
          String selectAWCRITERIA = "select criteria, weighting from w3awcriteria where id = ? and num = ? and criteria_type = ?";
          // Criteri qualita'
          List<?> datiAWCRITERIA_Q = sqlManager.getListVector(selectAWCRITERIA, new Object[] { id, num, "Q" });
          if (datiAWCRITERIA_Q != null && datiAWCRITERIA_Q.size() > 0) {
            for (int q = 0; q < datiAWCRITERIA_Q.size(); q++) {
              String criteria = (String) SqlManager.getValueFromVectorParam(datiAWCRITERIA_Q.get(q), 0).getValue();
              Long weighting = (Long) SqlManager.getValueFromVectorParam(datiAWCRITERIA_Q.get(q), 1).getValue();
              if (criteria != null && weighting != null) {
                AcDefinition acq = ac.addNewACQUALITY();
                criteria = conversioneCaratteriXML(criteria);
                criteria = criteria.replaceAll("[\n\r]", " ");
                acq.setACCRITERION(criteria);
                acq.setACWEIGHTING(weighting.toString());
              }
            }
          }
          // Criteri costo
          List<?> datiAWCRITERIA_C = sqlManager.getListVector(selectAWCRITERIA, new Object[] { id, num, "C" });
          if (datiAWCRITERIA_C != null && datiAWCRITERIA_C.size() > 0) {
            for (int c = 0; c < datiAWCRITERIA_C.size(); c++) {
              String criteria = (String) SqlManager.getValueFromVectorParam(datiAWCRITERIA_C.get(c), 0).getValue();
              Long weighting = (Long) SqlManager.getValueFromVectorParam(datiAWCRITERIA_C.get(c), 1).getValue();
              if (criteria != null && weighting != null) {
                AcDefinition acc = ac.addNewACCOST();
                criteria = conversioneCaratteriXML(criteria);
                criteria = criteria.replaceAll("[\n\r]", " ");
                acc.setACCRITERION(criteria);
                acc.setACWEIGHTING(weighting.toString());
              }
            }
          }

          Long cntP = (Long) sqlManager.getObject("select count(*) from w3awcriteria where id = ? and num = ? and criteria_type = ?",
              new Object[] { id, num, "P" });
          if (cntP != null && cntP.longValue() == 1) {
            Long price_weighting = (Long) sqlManager.getObject(
                "select weighting from w3awcriteria where id = ? and num = ? and criteria_type = ?", new Object[] { id, num, "P" });
            if (price_weighting != null) {
              ac.addNewACPRICE().setACWEIGHTING(price_weighting.toString());
            } else {
              ac.addNewACPRICE();
            }
          }

        }
      }

      // Durata del sistema di qualificazione
      Date q_date_start = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
      Date q_date_stop = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
      String q_indefinite = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();

      if (q_indefinite != null && "1".equals(q_indefinite)) {
        objectF07.addNewINDEFINITEDURATION();
      } else {
        if (q_date_start != null) {
          objectF07.setDATESTART(getDate(q_date_start));
        }
        if (q_date_stop != null) {
          objectF07.setDATEEND(getDate(q_date_stop));
        }
      }

      // Rinnovi
      String q_renewal = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
      String q_renewal_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
      if (q_renewal != null && "1".equals(q_renewal)) {
        objectF07.addNewRENEWAL();
        if (q_renewal_descr != null) {
          objectF07.setRENEWALDESCR(getTextFtMultiLines(q_renewal_descr));
        }
      }

      // Fondi europei
      String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 12).getValue();
      String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
      if (eu_progr != null) {
        if ("1".equals(eu_progr) && eu_progr_descr != null) {
          eu_progr_descr = conversioneCaratteriXML(eu_progr_descr);
          objectF07.setEUPROGRRELATED(getTextFtMultiLines(eu_progr_descr));
        } else {
          objectF07.addNewNOEUPROGRRELATED();
        }
      }
    }

    return objectF07;
  }

  /**
   * Sezione III.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private LeftiF07 _getLeftiF07(SqlManager sqlManager, Long id) throws SQLException {
    LeftiF07 leftiF07 = LeftiF07.Factory.newInstance();

    String selectW3FS7 = "select restricted_sheltered, " // 0
        + " restricted_frame, " // 1
        + " service_reserved, " // 2
        + " service_res_desc, " // 3
        + " performance_conditions, " // 4
        + " request_names " // 5
        + " from w3fs7 "
        + " where id = ?";

    List<?> datiW3FS7 = sqlManager.getVector(selectW3FS7, new Object[] { id });
    if (datiW3FS7 != null && datiW3FS7.size() > 0) {

      String restricted_sheltered = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 0).getValue();
      if (restricted_sheltered != null && "1".equals(restricted_sheltered)) {
        leftiF07.addNewRESTRICTEDSHELTEREDWORKSHOP();
      }

      String restricted_frame = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 1).getValue();
      if (restricted_frame != null && "1".equals(restricted_frame)) {
        leftiF07.addNewRESTRICTEDSHELTEREDPROGRAM();
      }

      // Condizioni e metodi di qualificazione
      String selectW3FS7_Q = "select conditions, methods from w3fs7_q where id = ? order by num";
      List<?> datiW3FS7_Q = sqlManager.getListVector(selectW3FS7_Q, new Object[] { id });
      if (datiW3FS7_Q != null && datiW3FS7_Q.size() > 0) {
        for (int q = 0; q < datiW3FS7_Q.size(); q++) {
          String conditions = (String) SqlManager.getValueFromVectorParam(datiW3FS7_Q.get(q), 0).getValue();
          String methods = (String) SqlManager.getValueFromVectorParam(datiW3FS7_Q.get(q), 1).getValue();

          if (conditions != null) {
            QUALIFICATION qualification = leftiF07.addNewQUALIFICATION();
            qualification.setCONDITIONS(getTextFtMultiLines(conditions));
            if (methods != null) {
              qualification.setMETHODS(getTextFtMultiLines(methods));
            }
          }

        }
      }

      String service_reserved = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 2).getValue();
      String service_res_desc = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 3).getValue();
      if (service_reserved != null && "1".equals(service_reserved)) {
        leftiF07.addNewPARTICULARPROFESSION().setCTYPE(eu.europa.publications.resource.schema.ted.r209.reception.Services.CTYPE.SERVICES);
        if (service_res_desc != null) leftiF07.setREFERENCETOLAW(getTextFtMultiLines(service_res_desc));
      }

      String performance_conditions = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 4).getValue();
      if (performance_conditions != null) {
        leftiF07.setPERFORMANCECONDITIONS(getTextFtMultiLines(performance_conditions));
      }

      String performance_staff = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 5).getValue();
      if (performance_staff != null && "1".equals(performance_staff)) {
        leftiF07.addNewPERFORMANCESTAFFQUALIFICATION();
      }
    }

    return leftiF07;

  }

  /**
   * Sezione IV.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ProcedureF07 _getProcedureF07(SqlManager sqlManager, Long id) throws SQLException {
    ProcedureF07 procedureF07 = ProcedureF07.Factory.newInstance();

    String selectW3FS7 = "select is_electronic, " // 0
        + " use_electronic, " // 1
        + " notice_number_oj " // 2
        + " from w3fs7 "
        + " where id = ?";

    List<?> datiW3FS7 = sqlManager.getVector(selectW3FS7, new Object[] { id });
    if (datiW3FS7 != null && datiW3FS7.size() > 0) {

      // Asta elettronica ?
      String is_electronic = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 0).getValue();
      String use_electronic = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 1).getValue();
      if (is_electronic != null && "1".equals(is_electronic)) {
        procedureF07.addNewEAUCTIONUSED();
        if (use_electronic != null) procedureF07.setINFOADDEAUCTION(getTextFtMultiLines(use_electronic));
      }

      // Pubblicazione precedente
      String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 2).getValue();
      if (notice_number_oj != null) {
        procedureF07.setNOTICENUMBEROJ(notice_number_oj);
      }

      // Lingue utilizzate
      String selectW3LANGUAGE = "select language_ec from w3language where id = ?";
      List<?> datiW3LANGUAGE = sqlManager.getListVector(selectW3LANGUAGE, new Object[] { id });
      if (datiW3LANGUAGE != null && datiW3LANGUAGE.size() > 0) {
        procedureF07.addNewLANGUAGES();
        for (int lec = 0; lec < datiW3LANGUAGE.size(); lec++) {
          String language_ec = (String) SqlManager.getValueFromVectorParam(datiW3LANGUAGE.get(lec), 0).getValue();
          procedureF07.getLANGUAGES().addNewLANGUAGE().setVALUE(
              eu.europa.publications.resource.schema.ted.r209.reception.TLanguageList.Enum.forString(language_ec));
        }
      }
    }

    return procedureF07;
  }

  /**
   * Sezione VI.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF07 _getCiF07(SqlManager sqlManager, Long id) throws SQLException {
    CiF07 ciF07 = CiF07.Factory.newInstance();

    String selectW3FS7 = "select eordering, " // 0
        + " einvoicing, " // 1
        + " epayment, " // 2
        + " review_body_codein, " // 3
        + " mediation_body_codein, " // 4
        + " review_procedure, " // 5
        + " review_info_codein, " // 6
        + " notice_date, " // 7
        + " additional_information " // 8
        + " from w3fs7 "
        + " where w3fs7.id = ?";

    List<?> datiW3FS7 = sqlManager.getVector(selectW3FS7, new Object[] { id });
    if (datiW3FS7 != null && datiW3FS7.size() > 0) {

      String eordering = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 0).getValue();
      if (eordering != null && "1".equals(eordering)) {
        ciF07.addNewEORDERING();
      }

      String einvoicing = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 1).getValue();
      if (einvoicing != null && "1".equals(einvoicing)) {
        ciF07.addNewEINVOICING();
      }

      String epayment = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 2).getValue();
      if (epayment != null && "1".equals(epayment)) {
        ciF07.addNewEPAYMENT();
      }

      String review_body_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 3).getValue();
      if (review_body_codein != null) {
        ciF07.setADDRESSREVIEWBODY(getContactReview(sqlManager, review_body_codein));
      }

      String mediation_body_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 4).getValue();
      if (mediation_body_codein != null) {
        ciF07.setADDRESSMEDIATIONBODY(getContactReview(sqlManager, mediation_body_codein));
      }

      String review_procedure = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 5).getValue();
      if (review_procedure != null) {
        ciF07.setREVIEWPROCEDURE(getTextFtMultiLines(review_procedure));
      }

      String review_info_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS7, 6).getValue();
      if (review_info_codein != null) {
        ciF07.setADDRESSREVIEWINFO(getContactReview(sqlManager, review_info_codein));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS7, 7).getValue();
      if (notice_date != null) {
        ciF07.setDATEDISPATCHNOTICE(getDate(notice_date));
      }

    }

    return ciF07;

  }

}
