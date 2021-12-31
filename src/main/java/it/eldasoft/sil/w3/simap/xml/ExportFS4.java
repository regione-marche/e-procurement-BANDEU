package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.AcDefinition;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF04;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF01;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF04;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.DurationUnitMD;
import eu.europa.publications.resource.schema.ted.r209.reception.F042014Document.F042014;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF05;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF06;
import eu.europa.publications.resource.schema.ted.r209.reception.FrameworkInfo;
import eu.europa.publications.resource.schema.ted.r209.reception.LeftiF01;
import eu.europa.publications.resource.schema.ted.r209.reception.LeftiF04;
import eu.europa.publications.resource.schema.ted.r209.reception.NoticeF04;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF01;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF04;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF01;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF04;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF04.AC;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.ProcedureF01;
import eu.europa.publications.resource.schema.ted.r209.reception.ProcedureF04;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFS4 extends ExportFormulariStandard {

  public F042014 exportF04(SqlManager sqlManager, Long id) throws GestoreException {

    F042014 f04 = F042014.Factory.newInstance();

    f04.setLG(TCeLanguageList.IT);
    f04.setCATEGORY(OriginalTranslation.ORIGINAL);
    f04.addNewFORM().setStringValue("F04");

    try {
      // Notice_f01
      // 1 - Il presente avviso è solo un avviso periodico indicativo
      // 2 - Lo scopo del presente avviso è ridurre i termini per la ricezione
      // delle offerte
      // 3 - Il presente avviso è un avviso di indizione di gara
      NoticeF04 noticeF04 = f04.addNewNOTICE();
      Long w3fs4_notice_f04 = (Long) sqlManager.getObject("select notice_f04 from w3fs4 where id = ?", new Object[] { id });
      if (w3fs4_notice_f04 != null) {
        switch (w3fs4_notice_f04.intValue()) {
        case 1:
          noticeF04.setTYPE(NoticeF04.TYPE.PER_ONLY);
          break;
        case 2:
          noticeF04.setTYPE(NoticeF04.TYPE.PER_REDUCING_TIME_LIMITS);
          break;
        case 3:
          noticeF04.setTYPE(NoticeF04.TYPE.PER_CALL_COMPETITION);
          break;
        default:
          break;
        }
      }

      // Base legale
      String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id });
      if (legal_basis != null) {
        f04.addNewLEGALBASIS().setVALUE(
            eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
      }

      f04.setCONTRACTINGBODY(_getBodyF04(sqlManager, id));
      f04.addNewOBJECTCONTRACT();
      f04.setOBJECTCONTRACTArray(0, _getObjectContractF04(sqlManager, id));
      f04.setLEFTI(_getLeftiF04(sqlManager, id));
      f04.setPROCEDURE(_getProcedureF04(sqlManager, id));
      f04.setCOMPLEMENTARYINFO(_getCiF04(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 1", "exportXML.sqlerror", e);
    }

    return f04;

  }

  /**
   * Sezione I
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private BodyF04 _getBodyF04(SqlManager sqlManager, Long id) throws SQLException {
    BodyF05 bodyF05 = getBodyF05CEGeneric(sqlManager, id);
    BodyF04 bodyF04 = (BodyF04) bodyF05.changeType(BodyF04.type);

    return bodyF04;
  }
  
  /**
   * Sezione II
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF04 _getObjectContractF04(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF04 objectContractF04 = ObjectContractF04.Factory.newInstance();

    String selectW3FS4 = "select w3fs4s2.title_contract, " // 0
        + " w3fs4s2.reference, " // 1
        + " w3fs4s2.cpv, " // 2
        + " w3fs4s2.cpvsupp1, " // 3
        + " w3fs4s2.cpvsupp2, " // 4
        + " w3fs4s2.type_contract, " // 5
        + " w3fs4s2.scope_total, " // 6
        + " w3fs4s2.scope_cost, " // 7
        + " w3fs4s2.date_publi, " // 8
        + " w3fs4s2.div_into_lots, " // 9
        + " w3fs4s2.div_lots, " // 10
        + " w3fs4s2.div_lots_max, " // 11
        + " w3fs4s2.lots_max_tenderer, " // 12
        + " w3fs4s2.lots_combining " // 13
        + " from w3fs4, w3fs4s2 "
        + " where w3fs4.id = w3fs4s2.id and w3fs4.id = ?";

    List<?> datiW3FS4 = sqlManager.getVector(selectW3FS4, new Object[] { id });
    if (datiW3FS4 != null && datiW3FS4.size() > 0) {

      objectContractF04.setITEM(1);

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 0).getValue();
      if (title_contract != null) {
        objectContractF04.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 1).getValue();
      if (reference != null) {
        reference = conversioneCaratteriXML(reference);
        objectContractF04.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 4).getValue();
      if (cpv != null) {
        objectContractF04.addNewCPVMAIN();
        objectContractF04.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF04.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF04.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF04.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF04.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF04.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      // Descrizione
      String scope_total = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 6).getValue();
      if (scope_total != null) {
        objectContractF04.setSHORTDESCR(getTextFtMultiLines(scope_total));
      }

      // Valore
      Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS4, 7).getValue();
      if (scope_cost != null && scope_cost.doubleValue() > 0) {
        objectContractF04.addNewVALESTIMATEDTOTAL().setBigDecimalValue(getCost(scope_cost));
        objectContractF04.getVALESTIMATEDTOTAL().setCURRENCY(
            eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
      }

      // Data prevista pubblicazione
      Date date_publi = (Date) SqlManager.getValueFromVectorParam(datiW3FS4, 8).getValue();
      if (date_publi != null) {
        objectContractF04.setDATEPUBLICATIONNOTICE(getDate(date_publi));
      }

      // Suddivisione in lotti ?
      String div_into_lots = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 9).getValue();
      String div_lots = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 10).getValue();
      Long div_lots_max = (Long) SqlManager.getValueFromVectorParam(datiW3FS4, 11).getValue();
      Long lots_max_tenderer = (Long) SqlManager.getValueFromVectorParam(datiW3FS4, 12).getValue();
      String lots_combining = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 13).getValue();
      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Esiste suddivisione in lotti.
        objectContractF04.addNewLOTDIVISION();
        if (div_lots != null) {
          if ("1".equals(div_lots)) {
            // Un solo lotto
            objectContractF04.getLOTDIVISION().addNewLOTONEONLY();
          } else if ("2".equals(div_lots)) {
            // Uno o piu' lotti
            if (div_lots_max != null) {
              objectContractF04.getLOTDIVISION().setLOTMAXNUMBER(div_lots_max.intValue());
            }
          } else if ("3".equals(div_lots)) {
            // Tutti i lotti
            objectContractF04.getLOTDIVISION().addNewLOTALL();
          }
        }
        if (lots_max_tenderer != null) {
          objectContractF04.getLOTDIVISION().setLOTMAXONETENDERER(lots_max_tenderer.intValue());
        }
        if (lots_combining != null) {
          objectContractF04.getLOTDIVISION().setLOTCOMBININGCONTRACTRIGHT(getTextFtMultiLines(lots_combining));
        }

        // Dati sezione II.2 (multipla)
        List<?> datiW3ANNEXB = sqlManager.getListVector("select num from w3annexb where id = ? order by lotnum", new Object[] { id });
        if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {
          for (int l = 0; l < datiW3ANNEXB.size(); l++) {
            Long w3annexb_num = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(l), 0).getValue();
            objectContractF04.addNewOBJECTDESCR();
            objectContractF04.setOBJECTDESCRArray(l, _getObjectF04(sqlManager, id, w3annexb_num, new Long(l + 1)));
          }
        }

      } else {
        // Non c'e' suddivisione in lotto. Esiste un solo lotto.
        objectContractF04.addNewNOLOTDIVISION();

        // Dati sezione II.2
        objectContractF04.addNewOBJECTDESCR();
        objectContractF04.setOBJECTDESCRArray(0, _getObjectF04(sqlManager, id, new Long(1), new Long(1)));
      }

    }
    return objectContractF04;
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
  private ObjectF04 _getObjectF04(SqlManager sqlManager, Long id, Long num, Long item_simap) throws SQLException {
    ObjectF04 objectF04 = ObjectF04.Factory.newInstance();

    String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs4s2 where id = ?", new Object[] { id });

    String selectW3ANNEXB = "select title, " // 0
        + " lotnum, " // 1
        + " site_nuts, " // 2
        + " site_nuts_2, " // 3
        + " site_nuts_3, " // 4
        + " site_nuts_4, " // 5
        + " site_label, " // 6
        + " description, " // 7
        + " ac_doc, " // 8
        + " cost, " // 9
        + " work_month, " // 10
        + " work_days, " // 11
        + " work_start_date, " // 12
        + " work_end_date, " // 13
        + " renewal, " // 14
        + " renewal_descr, " // 15
        + " acc_variants, " // 16
        + " options, " // 17
        + " options_descr, " // 18
        + " eu_progr, " // 19
        + " eu_progr_descr, " // 20
        + " additional_information " // 21
        + " from w3annexb "
        + " where id = ? and num = ?";

    List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id, num });
    if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

      // Item
      objectF04.setITEM(item_simap.intValue());

      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Titolo
        String title = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
        if (title != null) {
          objectF04.setTITLE(getTextFtSingleLine(title));
        }

        // Numero del lotto
        Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();
        if (lotnum != null) {
          objectF04.setLOTNO(lotnum.toString());
        }
      }

      // CPV aggiuntivi
      String selectW3CPV = "select cpv, cpvsupp1, cpvsupp2 from w3cpv where id = ? and num = ?";
      List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { id, num });
      if (datiW3CPV != null && datiW3CPV.size() > 0) {
        for (int c = 0; c < datiW3CPV.size(); c++) {
          String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 0).getValue();
          String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 1).getValue();
          String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 2).getValue();
          if (cpv != null) {
            CpvSet cpvSet = objectF04.addNewCPVADDITIONAL();
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
      String site_nuts = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 2).getValue();
      String site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 3).getValue();
      String site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 4).getValue();
      String site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 5).getValue();
      if (site_nuts != null) {
        objectF04.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF04.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF04.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF04.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
      if (site_label != null) {
        objectF04.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
      if (description != null) {
        objectF04.setSHORTDESCR(getTextFtMultiLines(description));
      }

      // Criteri di aggiudicazione, e criteri
      String ac_doc = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
      String selectAWCRITERIA = "select criteria, weighting from w3awcriteria where id = ? and num = ? and criteria_type = ?";
      List<?> datiAWCRITERIA_Q = sqlManager.getListVector(selectAWCRITERIA, new Object[] { id, num, "Q" });
      List<?> datiAWCRITERIA_C = sqlManager.getListVector(selectAWCRITERIA, new Object[] { id, num, "C" });
      Long cntP = (Long) sqlManager.getObject("select count(*) from w3awcriteria where id = ? and num = ? and criteria_type = ?",
          new Object[] { id, num, "P" });

      if (ac_doc != null
          || (datiAWCRITERIA_Q != null && datiAWCRITERIA_Q.size() > 0)
          || (datiAWCRITERIA_C != null && datiAWCRITERIA_C.size() > 0)
          || (cntP != null && cntP.longValue() == 1)) {

        AC ac = objectF04.addNewAC();

        if ("1".equals(ac_doc)) {
          ac.addNewACPROCUREMENTDOC();
        } else {

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

      // Valore stimato
      Double cost = (Double) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();
      if (cost != null && cost.doubleValue() > 0) {
        objectF04.addNewVALOBJECT().setBigDecimalValue(getCost(cost));
        objectF04.getVALOBJECT().setCURRENCY(eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
      }

      // Durata del contratto
      Long work_month = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
      Long work_days = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
      Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 12).getValue();
      Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
      if (work_month != null || work_days != null) {
        if (work_month != null) {
          objectF04.addNewDURATION().setTYPE(DurationUnitMD.MONTH);
          objectF04.getDURATION().setStringValue(work_month.toString());
        } else if (work_days != null) {
          objectF04.addNewDURATION().setTYPE(DurationUnitMD.DAY);
          objectF04.getDURATION().setStringValue(work_days.toString());
        }
      }
      if (work_start_date != null) {
        objectF04.setDATESTART(getDate(work_start_date));
      }
      if (work_end_date != null) {
        objectF04.setDATEEND(getDate(work_end_date));
      }

      // Rinnovi
      String renewal = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 14).getValue();
      String renewal_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 15).getValue();
      if (renewal != null && "1".equals(renewal)) {
        objectF04.addNewRENEWAL();
        if (renewal_descr != null) {
          objectF04.setRENEWALDESCR(getTextFtMultiLines(renewal_descr));
        }
      }

      // Varianti
      String acc_variants = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 16).getValue();
      if (acc_variants != null && "1".equals(acc_variants)) {
        objectF04.addNewACCEPTEDVARIANTS();
      }

      // Opzioni
      String options = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 17).getValue();
      String options_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 18).getValue();
      if (options != null && "1".equals(options)) {
        objectF04.addNewOPTIONS();
        if (options_descr != null) {
          objectF04.setOPTIONSDESCR(getTextFtMultiLines(options_descr));
        }
      }

      // Fondi europei
      String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 19).getValue();
      String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 20).getValue();
      if (eu_progr != null) {
        if ("1".equals(eu_progr) && eu_progr_descr != null) {
          eu_progr_descr = conversioneCaratteriXML(eu_progr_descr);
          objectF04.setEUPROGRRELATED(getTextFtMultiLines(eu_progr_descr));
        } else {
          objectF04.addNewNOEUPROGRRELATED();
        }
      }

      // Informazioni complementari
      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 21).getValue();
      if (additional_information != null) {
        objectF04.setINFOADD(getTextFtMultiLines(additional_information));
      }

    }

    return objectF04;
  }

  /**
   * Sezione III
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private LeftiF04 _getLeftiF04(SqlManager sqlManager, Long id) throws SQLException {
    LeftiF04 leftiF04 = LeftiF04.Factory.newInstance();

    String selectW3FS4 = "select suitability, " // 0
        + " eco_criteria_doc, " // 1
        + " eco_fin_info, " // 2
        + " eco_fin_min_level, " // 3
        + " tech_crit_doc, " // 4
        + " tech_prof_info, " // 5
        + " tech_prof_min_level, " // 6
        + " restricted_sheltered, " // 7
        + " restricted_frame, " // 8
        + " particular_profession, " // 9
        + " reference_to_law, " // 10
        + " performance_conditions, " // 11
        + " performance_staff " // 12
        + " from w3fs4 "
        + " where id = ?";

    List<?> datiW3FS4 = sqlManager.getVector(selectW3FS4, new Object[] { id });
    if (datiW3FS4 != null && datiW3FS4.size() > 0) {
      String suitability = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 0).getValue();
      if (suitability != null) {
        leftiF04.setSUITABILITY(getTextFtMultiLines(suitability));
      }

      String eco_criteria_doc = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 1).getValue();
      String eco_fin_info = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 2).getValue();
      String eco_fin_min_level = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 3).getValue();
      if (eco_criteria_doc != null && "1".equals(eco_criteria_doc)) {
        leftiF04.addNewECONOMICCRITERIADOC();
      } else {
        if (eco_fin_info != null) leftiF04.setECONOMICFINANCIALINFO(getTextFtMultiLines(eco_fin_info));
        if (eco_fin_min_level != null) leftiF04.setECONOMICFINANCIALMINLEVEL(getTextFtMultiLines(eco_fin_min_level));
      }

      String tech_crit_doc = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 4).getValue();
      String tech_prof_info = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 5).getValue();
      String tech_prof_min_level = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 6).getValue();
      if (tech_crit_doc != null && "1".equals(tech_crit_doc)) {
        leftiF04.addNewTECHNICALCRITERIADOC();
      } else {
        if (tech_prof_info != null) leftiF04.setTECHNICALPROFESSIONALINFO(getTextFtMultiLines(tech_prof_info));
        if (tech_prof_min_level != null) leftiF04.setTECHNICALPROFESSIONALMINLEVEL(getTextFtMultiLines(tech_prof_min_level));
      }

      String restricted_sheltered = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 7).getValue();
      if (restricted_sheltered != null && "1".equals(restricted_sheltered)) {
        leftiF04.addNewRESTRICTEDSHELTEREDWORKSHOP();
      }

      String restricted_frame = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 8).getValue();
      if (restricted_frame != null && "1".equals(restricted_frame)) {
        leftiF04.addNewRESTRICTEDSHELTEREDPROGRAM();
      }

      String particular_profession = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 9).getValue();
      String reference_to_law = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 10).getValue();
      if (particular_profession != null && "1".equals(particular_profession)) {
        leftiF04.addNewPARTICULARPROFESSION().setCTYPE(eu.europa.publications.resource.schema.ted.r209.reception.Services.CTYPE.SERVICES);
        if (reference_to_law != null) leftiF04.setREFERENCETOLAW(getTextFtMultiLines(reference_to_law));
      }

      String performance_conditions = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 11).getValue();
      if (performance_conditions != null) {
        leftiF04.setPERFORMANCECONDITIONS(getTextFtMultiLines(performance_conditions));
      }

      String performance_staff = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 12).getValue();
      if (performance_staff != null && "1".equals(performance_staff)) {
        leftiF04.addNewPERFORMANCESTAFFQUALIFICATION();
      }

    }

    return leftiF04;
  }

  /**
   * Sezione IV
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ProcedureF04 _getProcedureF04(SqlManager sqlManager, Long id) throws SQLException {
    ProcedureF04 procedureF04 = ProcedureF04.Factory.newInstance();

    String selectW3FS4 = "select type_procedure, " // 0
        + " framework, " // 1
        + " fr_several_op, " // 2
        + " fr_nb_partecipants, " // 3
        + " dps, " // 4
        + " dps_add, " // 5
        + " fr_justification, " // 6
        + " eauction, " // 7
        + " eauction_info, " // 8
        + " gpa, " // 9
        + " date_receipt, " // 10
        + " time_receipt, " // 11
        + " date_award " // 12
        + " from w3fs4 "
        + " where id = ?";

    List<?> datiW3FS4 = sqlManager.getVector(selectW3FS4, new Object[] { id });
    if (datiW3FS4 != null && datiW3FS4.size() > 0) {

      // Tipo procedura
      Long type_procedure = (Long) SqlManager.getValueFromVectorParam(datiW3FS4, 0).getValue();
      if (type_procedure != null) {
        if (new Long(1).equals(type_procedure)) {
          procedureF04.addNewPTRESTRICTED();
        } else if (new Long(2).equals(type_procedure)) {
          procedureF04.addNewPTNEGOTIATEDWITHPRIORCALL();
        }
      }

      // Sistema dinamico di acquisizione
      String framework = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 1).getValue();
      String fr_several_op = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 2).getValue();
      Long fr_nb_partecipants = (Long) SqlManager.getValueFromVectorParam(datiW3FS4, 3).getValue();
      String fr_justification = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 6).getValue();
      if (framework != null && "1".equals(framework)) {
        FrameworkInfo frameworkInfo = procedureF04.addNewFRAMEWORK();
        if (fr_several_op != null && "1".equals(fr_several_op)) {
          // Accordo quadro con piu' operatori
          frameworkInfo.addNewSEVERALOPERATORS();
          if (fr_nb_partecipants != null) {
            frameworkInfo.setNBPARTICIPANTS(fr_nb_partecipants.intValue());
          }
        } else {
          // Singolo operatore
          frameworkInfo.addNewSINGLEOPERATOR();
        }
        if (fr_justification != null) {
          fr_justification = conversioneCaratteriXML(fr_justification);
          frameworkInfo.setJUSTIFICATION(getTextFtMultiLines(fr_justification));
        }
      }

      // Sistema dinamico
      String dps = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 4).getValue();
      String dps_add = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 5).getValue();
      if (dps != null && "1".equals(dps)) {
        procedureF04.addNewDPS();
        if (dps_add != null && "1".equals(dps_add)) procedureF04.addNewDPSADDITIONALPURCHASERS();
      }

      // Asta elettronica ?
      String eauction = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 7).getValue();
      String eauction_info = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 8).getValue();
      if (eauction != null && "1".equals(eauction)) {
        procedureF04.addNewEAUCTIONUSED();
        if (eauction_info != null) procedureF04.setINFOADDEAUCTION(getTextFtMultiLines(eauction_info));
      }

      // Accordo sugli appalti pubblici ?
      String gpa = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 9).getValue();
      if (gpa != null && "1".equals(gpa)) {
        procedureF04.addNewCONTRACTCOVEREDGPA();
      } else {
        procedureF04.addNewNOCONTRACTCOVEREDGPA();
      }

      // Termini per la ricezione delle offerte
      Date date_receipt = (Date) SqlManager.getValueFromVectorParam(datiW3FS4, 10).getValue();
      String time_receipt = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 11).getValue();
      if (date_receipt != null) {
        procedureF04.setDATERECEIPTTENDERS(getDate(date_receipt));
        if (time_receipt != null) {
          procedureF04.setTIMERECEIPTTENDERS(time_receipt);
        }
      }

      // Lingue utilizzate
      String selectW3LANGUAGE = "select language_ec from w3language where id = ?";
      List<?> datiW3LANGUAGE = sqlManager.getListVector(selectW3LANGUAGE, new Object[] { id });
      if (datiW3LANGUAGE != null && datiW3LANGUAGE.size() > 0) {
        procedureF04.addNewLANGUAGES();
        for (int lec = 0; lec < datiW3LANGUAGE.size(); lec++) {
          String language_ec = (String) SqlManager.getValueFromVectorParam(datiW3LANGUAGE.get(lec), 0).getValue();
          procedureF04.getLANGUAGES().addNewLANGUAGE().setVALUE(
              eu.europa.publications.resource.schema.ted.r209.reception.TLanguageList.Enum.forString(language_ec));
        }
      }

      // Data prevista partecipazione
      Date date_award = (Date) SqlManager.getValueFromVectorParam(datiW3FS4, 12).getValue();
      if (date_award != null) {
        procedureF04.setDATEAWARDSCHEDULED(getDate(date_award));
      }

    }

    return procedureF04;
  }

  /**
   * Sezione VI
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF04 _getCiF04(SqlManager sqlManager, Long id) throws SQLException {
    CiF04 ciF04 = CiF04.Factory.newInstance();

    String selectW3FS4 = "select eordering, " // 0
        + " einvoicing, " // 1
        + " epayment, " // 2
        + " review_body_codein, " // 3
        + " mediation_body_codein, " // 4
        + " review_procedure, " // 5
        + " review_info_codein, " // 6
        + " notice_date, " // 7
        + " additional_information " // 8
        + " from w3fs4 "
        + " where id = ?";

    List<?> datiW3FS4 = sqlManager.getVector(selectW3FS4, new Object[] { id });
    if (datiW3FS4 != null && datiW3FS4.size() > 0) {
      String eordering = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 0).getValue();
      if (eordering != null && "1".equals(eordering)) {
        ciF04.addNewEORDERING();
      }

      String einvoicing = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 1).getValue();
      if (einvoicing != null && "1".equals(einvoicing)) {
        ciF04.addNewEINVOICING();
      }

      String epayment = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 2).getValue();
      if (epayment != null && "1".equals(epayment)) {
        ciF04.addNewEPAYMENT();
      }

      String review_body_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 3).getValue();
      if (review_body_codein != null) {
        ciF04.setADDRESSREVIEWBODY(getContactReview(sqlManager, review_body_codein));
      }

      String mediation_body_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 4).getValue();
      if (mediation_body_codein != null) {
        ciF04.setADDRESSMEDIATIONBODY(getContactReview(sqlManager, mediation_body_codein));
      }

      String review_procedure = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 5).getValue();
      if (review_procedure != null) {
        ciF04.setREVIEWPROCEDURE(getTextFtMultiLines(review_procedure));
      }

      String review_info_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 6).getValue();
      if (review_info_codein != null) {
        ciF04.setADDRESSREVIEWINFO(getContactReview(sqlManager, review_info_codein));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS4, 7).getValue();
      if (notice_date != null) {
        ciF04.setDATEDISPATCHNOTICE(getDate(notice_date));
      }

      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS4, 8).getValue();
      if (additional_information != null) {
        ciF04.setINFOADD(getTextFtMultiLines(additional_information));
      }

    }

    return ciF04;
  }

}
