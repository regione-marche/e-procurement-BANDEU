package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.AcDefinition;
import eu.europa.publications.resource.schema.ted.r209.reception.AnnexD1;
import eu.europa.publications.resource.schema.ted.r209.reception.AnnexD1.DACCORDANCEARTICLE;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF03;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF03.AWARDEDCONTRACT;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF03.AWARDEDCONTRACT.CONTRACTORS;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF03.AWARDEDCONTRACT.CONTRACTORS.CONTRACTOR;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF03.AWARDEDCONTRACT.TENDERS;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF03.AWARDEDCONTRACT.VALUES;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF01;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF03;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF03;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactContractor;
import eu.europa.publications.resource.schema.ted.r209.reception.Country;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.F032014Document.F032014;
import eu.europa.publications.resource.schema.ted.r209.reception.NoSupplies;
import eu.europa.publications.resource.schema.ted.r209.reception.NoWorks;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF03;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF03;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF03.AC;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.PROCUREMENTDISCONTINUEDDocument.PROCUREMENTDISCONTINUED;
import eu.europa.publications.resource.schema.ted.r209.reception.ProcedureF03;
import eu.europa.publications.resource.schema.ted.r209.reception.Services;
import eu.europa.publications.resource.schema.ted.r209.reception.Supplies;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TCountryList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.r209.reception.ValRange;
import eu.europa.publications.resource.schema.ted.x2021.nuts.Nuts;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;

public class ExportFS3 extends ExportFormulariStandard {

  public F032014 exportF03(SqlManager sqlManager, Long id) throws GestoreException {

    F032014 f03 = F032014.Factory.newInstance();

    f03.setLG(TCeLanguageList.IT);
    f03.setCATEGORY(OriginalTranslation.ORIGINAL);
    f03.addNewFORM().setStringValue("F03");

    try {

      // Base legale
      String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id });
      if (legal_basis != null) {
        f03.addNewLEGALBASIS().setVALUE(
            eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
      }

      f03.setCONTRACTINGBODY(_getBodyF03(sqlManager, id));
      f03.setOBJECTCONTRACT(_getObjectContractF03(sqlManager, id));
      f03.setPROCEDURE(_getProcedureF03(sqlManager, id));

      String selectW3FS3AWARD = "select item from w3fs3award where id = ? order by item";
      List<?> datiW3FS3AWARD = sqlManager.getListVector(selectW3FS3AWARD, new Object[] { id });
      if (datiW3FS3AWARD != null && datiW3FS3AWARD.size() > 0) {
        for (int a = 0; a < datiW3FS3AWARD.size(); a++) {
          Long item = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD.get(a), 0).getValue();
          f03.addNewAWARDCONTRACT();
          f03.setAWARDCONTRACTArray(a, _getAwardContractF03(sqlManager, id, item, new Long(a + 1)));
        }
      }

      f03.setCOMPLEMENTARYINFO(_getCiF03(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 3", "exportXML.sqlerror", e);
    }

    return f03;

  }

  /**
   * Sezione I
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private BodyF03 _getBodyF03(SqlManager sqlManager, Long id) throws SQLException {
    BodyF01 bodyF01 = getBodyF01CAGeneric(sqlManager, id);
    BodyF03 bodyF03 = (BodyF03) bodyF01.changeType(BodyF03.type);
    return bodyF03;
  }

  /**
   * Sezione II
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF03 _getObjectContractF03(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF03 objectContractF03 = ObjectContractF03.Factory.newInstance();

    String selectW3FS3 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " cpvsupp1, " // 3
        + " cpvsupp2, " // 4
        + " type_contract, " // 5
        + " short_contract_description, " // 6
        + " div_into_lots, " // 7
        + " scope_cost, " // 8
        + " scope_low, " // 9
        + " scope_high " // 10
        + " from w3fs3 "
        + " where w3fs3.id = ?";

    List<?> datiW3FS3 = sqlManager.getVector(selectW3FS3, new Object[] { id });
    if (datiW3FS3 != null && datiW3FS3.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 0).getValue();
      if (title_contract != null) {
        objectContractF03.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 1).getValue();
      if (reference != null) {
        objectContractF03.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 4).getValue();
      if (cpv != null) {
        objectContractF03.addNewCPVMAIN();
        objectContractF03.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF03.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF03.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF03.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF03.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF03.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      // Descrizione
      String short_contract_description = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 6).getValue();
      if (short_contract_description != null) {
        objectContractF03.setSHORTDESCR(getTextFtMultiLines(short_contract_description));
      }

      // Valore
      // Il valore finale deve essere indicato se almeno un contratto e' stato
      // aggiudicato
      Long cnt_awarded = (Long) sqlManager.getObject("select count(*) from w3fs3award where id = ? and awarded = ?",
          new Object[] { id, "1" });
      if (cnt_awarded != null && cnt_awarded.longValue() > 0) {
        Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS3, 8).getValue();
        Double scope_low = (Double) SqlManager.getValueFromVectorParam(datiW3FS3, 9).getValue();
        Double scope_high = (Double) SqlManager.getValueFromVectorParam(datiW3FS3, 10).getValue();
        if (scope_cost != null && scope_cost.doubleValue() > 0) {
          objectContractF03.addNewVALTOTAL().setBigDecimalValue(getCost(scope_cost));
          objectContractF03.getVALTOTAL().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
        } else if (scope_low != null && scope_high != null && scope_low.doubleValue() > 0 && scope_high.doubleValue() > 0) {
          objectContractF03.addNewVALRANGETOTAL();
          objectContractF03.getVALRANGETOTAL().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          objectContractF03.getVALRANGETOTAL().setLOW(getCost(scope_low));
          objectContractF03.getVALRANGETOTAL().setHIGH(getCost(scope_high));

        }
      }

      // Suddivisione in lotti ?
      String div_into_lots = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 7).getValue();
      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Esiste suddivisione in lotti.
        objectContractF03.addNewLOTDIVISION();

        // Dati sezione II.2 (multipla)
        List<?> datiW3ANNEXB = sqlManager.getListVector("select num from w3annexb where id = ? order by lotnum", new Object[] { id });
        if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {
          for (int l = 0; l < datiW3ANNEXB.size(); l++) {
            Long w3annexb_num = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(l), 0).getValue();
            objectContractF03.addNewOBJECTDESCR();
            objectContractF03.setOBJECTDESCRArray(l, _getObjectF03(sqlManager, id, w3annexb_num, new Long(l + 1)));
          }
        }

      } else {
        // Non c'e' suddivisione in lotto. Esiste un solo lotto.
        objectContractF03.addNewNOLOTDIVISION();

        // Dati sezione II.2
        objectContractF03.addNewOBJECTDESCR();
        objectContractF03.setOBJECTDESCRArray(0, _getObjectF03(sqlManager, id, new Long(1), new Long(1)));
      }

    }

    return objectContractF03;
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
  private ObjectF03 _getObjectF03(SqlManager sqlManager, Long id, Long num, Long item_simap) throws SQLException {
    ObjectF03 objectF03 = ObjectF03.Factory.newInstance();

    String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs3 where id = ?", new Object[] { id });

    String selectW3ANNEXB = "select title, " // 0
        + " lotnum, " // 1
        + " site_nuts, " // 2
        + " site_nuts_2, " // 3
        + " site_nuts_3, " // 4
        + " site_nuts_4, " // 5
        + " site_label, " // 6
        + " description, " // 7
        + " options, " // 8
        + " options_descr, " // 9
        + " eu_progr, " // 10
        + " eu_progr_descr, " // 11
        + " additional_information " // 12
        + " from w3annexb "
        + " where id = ? and num = ?";

    List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id, num });
    if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

      // Item
      objectF03.setITEM(item_simap.intValue());

      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Titolo
        String title = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
        if (title != null) {
          objectF03.setTITLE(getTextFtSingleLine(title));
        }

        // Numero del lotto
        Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();
        if (lotnum != null) {
          objectF03.setLOTNO(lotnum.toString());
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
            CpvSet cpvSet = objectF03.addNewCPVADDITIONAL();
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
        objectF03.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF03.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF03.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF03.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
      if (site_label != null) {
        objectF03.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
      if (description != null) {
        objectF03.setSHORTDESCR(getTextFtMultiLines(description));
      }

      // Criteri di aggiudicazione
      String selectAWCRITERIA = "select criteria, weighting from w3awcriteria where id = ? and num = ? and criteria_type = ?";
      // Criteri qualita' e criteri di costo
      List<?> datiAWCRITERIA_Q = sqlManager.getListVector(selectAWCRITERIA, new Object[] { id, num, "Q" });
      List<?> datiAWCRITERIA_C = sqlManager.getListVector(selectAWCRITERIA, new Object[] { id, num, "C" });
      Long cntP = (Long) sqlManager.getObject("select count(*) from w3awcriteria where id = ? and num = ? and criteria_type = ?",
          new Object[] { id, num, "P" });

      if ((datiAWCRITERIA_Q != null && datiAWCRITERIA_Q.size() > 0)
          || (datiAWCRITERIA_C != null && datiAWCRITERIA_C.size() > 0)
          || (cntP != null && cntP.longValue() == 1)) {

        AC ac = objectF03.addNewAC();

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

      // Opzioni
      String options = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
      String options_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();
      if (options != null && "1".equals(options)) {
        objectF03.addNewOPTIONS();
        if (options_descr != null) {
          objectF03.setOPTIONSDESCR(getTextFtMultiLines(options_descr));
        }
      } else {
        objectF03.addNewNOOPTIONS();
      }

      // Fondi europei
      String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
      String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
      if (eu_progr != null) {
        if ("1".equals(eu_progr) && eu_progr_descr != null) {
          eu_progr_descr = conversioneCaratteriXML(eu_progr_descr);
          objectF03.setEUPROGRRELATED(getTextFtMultiLines(eu_progr_descr));
        } else {
          objectF03.addNewNOEUPROGRRELATED();
        }
      }

    }

    return objectF03;
  }

  /**
   * Sezione IV
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ProcedureF03 _getProcedureF03(SqlManager sqlManager, Long id) throws SQLException {
    ProcedureF03 procedureF03 = ProcedureF03.Factory.newInstance();

    String selectW3FS3 = "select type_procedure, " // 0
        + " accelerated, " // 1
        + " framework, " // 2
        + " dps, " // 3
        + " is_electronic, " // 4
        + " contract_covered_gpa, " // 5
        + " notice_number_oj, " // 6
        + " termination_dps, " // 7
        + " termination_pin " // 8
        + " from w3fs3 "
        + " where id = ?";

    List<?> datiW3FS3 = sqlManager.getVector(selectW3FS3, new Object[] { id });
    if (datiW3FS3 != null && datiW3FS3.size() > 0) {

      // Tipo procedura
      String type_procedure = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 0).getValue();
      String accelerated = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 1).getValue();

      // 1 |PT_OPEN |Procedura aperta |
      // 10|PT_INNOVATION |Partenariato per l'innovazione |
      // 2 |PT_RESTRICTED |Procedura ristretta |
      // 3 |PT_RESTRICTED_ACC |Procedura ristretta accelerata |
      // 4 |PT_NEGOTIATION |Procedura competitiva con negoziazione |
      // 5 |PT_NEGOTIATION_ACC |Procedura competitiva accelerata con
      // negoziazione |
      // 6 |PT_COMPETITIVE |Dialogo competitivo |
      // 8 |PT_AWARD_WC |Aggiudicazione senza previa pubblicazione (completare
      // l'allegato D1) |
      // 9 |PT_OPEN_ACC |Procedura aperta accelerata |
      if (type_procedure != null) {
        if ("1".equals(type_procedure)) {
          procedureF03.addNewPTOPEN();
        } else if ("2".equals(type_procedure)) {
          procedureF03.addNewPTRESTRICTED();
        } else if ("3".equals(type_procedure)) {
          procedureF03.addNewPTRESTRICTED();
          procedureF03.setACCELERATEDPROC(getTextFtMultiLines(accelerated));
        } else if ("4".equals(type_procedure)) {
          procedureF03.addNewPTCOMPETITIVENEGOTIATION();
        } else if ("5".equals(type_procedure)) {
          procedureF03.addNewPTCOMPETITIVENEGOTIATION();
          procedureF03.setACCELERATEDPROC(getTextFtMultiLines(accelerated));
        } else if ("6".equals(type_procedure)) {
          procedureF03.addNewPTCOMPETITIVEDIALOGUE();
        } else if ("8".equals(type_procedure)) {
          procedureF03.setPTAWARDCONTRACTWITHOUTCALL(_getAnnexD1(sqlManager, id));
        } else if ("9".equals(type_procedure)) {
          procedureF03.addNewPTOPEN();
          procedureF03.setACCELERATEDPROC(getTextFtMultiLines(accelerated));
        } else if ("10".equals(type_procedure)) {
          procedureF03.addNewPTINNOVATIONPARTNERSHIP();
        }
      }

      // Informazioni su accordo quadro e sistema dinamico di acquisizione
      String framework = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 2).getValue();
      if (framework != null && "1".equals(framework)) {
        procedureF03.addNewFRAMEWORK();
      }

      // Sistema dinamico
      String dps = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 3).getValue();
      if (dps != null && "1".equals(dps)) {
        procedureF03.addNewDPS();
      }

      // Asta elettronica
      String is_electronic = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 4).getValue();
      if (is_electronic != null && "1".equals(is_electronic)) {
        procedureF03.addNewEAUCTIONUSED();
      }

      // Accordo sugli appalti pubblici ?
      String contract_covered_gpa = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 5).getValue();
      if (contract_covered_gpa != null && "1".equals(contract_covered_gpa)) {
        procedureF03.addNewCONTRACTCOVEREDGPA();
      } else {
        procedureF03.addNewNOCONTRACTCOVEREDGPA();
      }

      // Pubblicazione precedente
      String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 6).getValue();
      if (notice_number_oj != null) {
        procedureF03.setNOTICENUMBEROJ(notice_number_oj);
      }

      // Chiusura sistema dinamico di acquisizione
      String termination_dps = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 7).getValue();
      if (termination_dps != null && "1".equals(termination_dps)) {
        procedureF03.addNewTERMINATIONDPS();
      }

      // Fine validita'
      String termination_pin = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 8).getValue();
      if (termination_pin != null && "1".equals(termination_pin)) {
        procedureF03.addNewTERMINATIONPIN();
      }

    }

    return procedureF03;
  }

  /**
   * Allegato D1
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private AnnexD1 _getAnnexD1(SqlManager sqlManager, Long id) throws SQLException {

    AnnexD1 annexD1 = AnnexD1.Factory.newInstance();

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
    if (datiW3ANNEXD != null && datiW3ANNEXD.size() > 0) {

      String outside_scope = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 9).getValue();
      if (outside_scope != null && "1".equals(outside_scope)) {
        annexD1.addNewDOUTSIDESCOPE();
      } else {
        DACCORDANCEARTICLE d1 = annexD1.addNewDACCORDANCEARTICLE();

        String type_contract = (String) sqlManager.getObject("select type_contract from w3fs3 where id = ?", new Object[] { id });

        // 1 OPEN_PROCEDURE una procedura aperta
        // 2 RESTRICTED_PROCEDURE una procedura ristretta
        String no_open_restricted = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 0).getValue();
        if (no_open_restricted != null) {
          if ("1".equals(no_open_restricted)) d1.addNewDPROCOPEN();
          if ("2".equals(no_open_restricted)) d1.addNewDPROCRESTRICTED();
        }

        String manufactured = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 1).getValue();
        if (manufactured != null && "1".equals(manufactured)) {
          d1.addNewDMANUFFORRESEARCH().setCTYPE(Supplies.CTYPE.SUPPLIES);
        }

        // 1 - TECHNICAL di carattere tecnico
        // 2 - ARTISTIC di carattere artistico
        // 3 - EXCLUSIVE_RIGHTS connesso alla tutela di diritti esclusivi
        String particular_tendered = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 2).getValue();
        if (particular_tendered != null) {
          if ("1".equals(particular_tendered)) d1.addNewDTECHNICAL();
          if ("2".equals(particular_tendered)) d1.addNewDARTISTIC();
          if ("3".equals(particular_tendered)) d1.addNewDPROTECTRIGHTS();
        }

        String extreme_urgency = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 3).getValue();
        if (extreme_urgency != null && "1".equals(extreme_urgency)) {
          d1.addNewDEXTREMEURGENCY();
        }

        String additional_works = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 4).getValue();
        if (additional_works != null && "1".equals(additional_works)) {
          d1.addNewDADDDELIVERIESORDERED();
        }

        String works_repetition = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 5).getValue();
        if (works_repetition != null && "1".equals(works_repetition)) {
          if (type_contract != null) {
            if ("SERV".equals(type_contract)) {
              d1.addNewDREPETITIONEXISTING().setCTYPE(NoSupplies.CTYPE.SERVICES);
            } else if ("WORK".equals(type_contract)) {
              d1.addNewDREPETITIONEXISTING().setCTYPE(NoSupplies.CTYPE.WORKS);
            }
          }
        }

        String service_contract = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 6).getValue();
        if (service_contract != null && "1".equals(service_contract)) {
          d1.addNewDCONTRACTAWARDEDDESIGNCONTEST().setCTYPE(Services.CTYPE.SERVICES);
        }

        String supplies_quoted = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 7).getValue();
        if (supplies_quoted != null && "1".equals(supplies_quoted)) {
          d1.addNewDCOMMODITYMARKET().setCTYPE(Supplies.CTYPE.SUPPLIES);
        }

        // 1 presso il curatore o il liquidatore di un fallimento o di un
        // concordato giudiziario
        // 2 presso un fornitore che cessa definitivamente l'attività
        String purchase_supplies = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 8).getValue();
        if (purchase_supplies != null) {
          if ("1".equals(purchase_supplies)) {
            if (type_contract != null) {
              if ("SERV".equals(type_contract)) {
                d1.addNewDFROMLIQUIDATORCREDITOR().setCTYPE(NoWorks.CTYPE.SERVICES);
              } else if ("SUPP".equals(type_contract)) {
                d1.addNewDFROMLIQUIDATORCREDITOR().setCTYPE(NoWorks.CTYPE.SUPPLIES);
              }
            }
          } else if ("2".equals(purchase_supplies)) {
            if (type_contract != null) {
              if ("SERV".equals(type_contract)) {
                d1.addNewDFROMWINDINGPROVIDER().setCTYPE(NoWorks.CTYPE.SERVICES);
              } else if ("SUPP".equals(type_contract)) {
                d1.addNewDFROMWINDINGPROVIDER().setCTYPE(NoWorks.CTYPE.SUPPLIES);
              }
            }

          }
        }
      }

      String reason_contract_lawful = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 10).getValue();
      if (reason_contract_lawful != null) {
        annexD1.setDJUSTIFICATION(getTextFtMultiLines(reason_contract_lawful));
      }
    }

    return annexD1;

  }

  /**
   * Sezione V
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private AwardContractF03 _getAwardContractF03(SqlManager sqlManager, Long id, Long item, Long item_simap) throws SQLException {
    AwardContractF03 awardContractF03 = AwardContractF03.Factory.newInstance();

    String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs3 where id = ?", new Object[] { id });

    String selectW3FS3AWARD = "select contract_number, " // 0
        + " lot_number, " // 1
        + " contract_title, " // 2
        + " awarded, " // 3
        + " no_awarded_type, " // 4
        + " original, " // 5
        + " date_original, " // 6
        + " contract_award_date, " // 7
        + " offers_received, " // 8
        + " nb_sme, " // 9
        + " nb_other_eu, " // 10
        + " nb_other_non_eu, " // 11
        + " offers_received_meaning, " // 12
        + " awarded_group, " // 13
        + " initial_cost, " // 14
        + " final_cost, " // 15
        + " final_low, " // 16
        + " final_high, " // 17
        + " sub_contracted, " // 18
        + " sub_value, " // 19
        + " sub_prct, " // 20
        + " additional_information " // 21
        + " from w3fs3award "
        + " where id = ? and item = ?";

    List<?> datiW3FS3AWARD = sqlManager.getVector(selectW3FS3AWARD, new Object[] { id, item });
    if (datiW3FS3AWARD != null && datiW3FS3AWARD.size() > 0) {
      awardContractF03.setITEM(item_simap.intValue());

      // Numero contratto
      String contract_number = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 0).getValue();
      if (contract_number != null) {
        contract_number = conversioneCaratteriXML(contract_number);
        awardContractF03.setCONTRACTNO(contract_number);
      }

      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Numero lotto
        Long lot_number = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 1).getValue();
        if (lot_number != null) {
          awardContractF03.setLOTNO(lot_number.toString());
        }
      }

      // Titolo
      String contract_title = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 2).getValue();
      if (contract_title != null) {
        awardContractF03.setTITLE(getTextFtSingleLine(contract_title));
      }

      String awarded = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 3).getValue();
      if (awarded != null) {
        if ("2".equals(awarded)) {
          // Appalto o lotto non aggiudicato
          String no_awarded_type = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 4).getValue();
          String original = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 5).getValue();
          Date date_original = (Date) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 6).getValue();
          if (no_awarded_type != null) {
            if ("1".equals(no_awarded_type)) {
              // Altri motivi (interruzione della procedura)
              PROCUREMENTDISCONTINUED pd = awardContractF03.addNewNOAWARDEDCONTRACT().addNewPROCUREMENTDISCONTINUED();
              pd.addNewORIGINALOTHERMEANS().setStringValue(original);
              pd.addNewDATEDISPATCHORIGINAL().setCalendarValue(getDate(date_original));

              pd.getORIGINALOTHERMEANS().addNewPUBLICATION().setStringValue("NO");
              pd.getDATEDISPATCHORIGINAL().addNewPUBLICATION().setStringValue("NO");

              // pd.addNewORIGINALOTHERMEANS().addNewPUBLICATION().setStringValue(original);
              // pd.addNewDATEDISPATCHORIGINAL().addNewPUBLICATION().setStringValue(getDate(date_original).toString());
            } else if ("2".equals(no_awarded_type)) {
              // Non sono pervenute o sono state tutte respinte le offerte o
              // domande di partecipazione
              awardContractF03.addNewNOAWARDEDCONTRACT().addNewPROCUREMENTUNSUCCESSFUL();
            }
          }

        } else if ("1".equals(awarded)) {
          // Appalto o lotto aggiudicato
          AWARDEDCONTRACT aw = awardContractF03.addNewAWARDEDCONTRACT();

          // Data di conclusione
          Date date_original = (Date) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 7).getValue();
          if (date_original != null) {
            aw.setDATECONCLUSIONCONTRACT(getDate(date_original));
          }

          // Informazioni sulle offerte
          Long offers_received = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 8).getValue();
          Long nb_sme = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 9).getValue();
          Long nb_other_eu = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 10).getValue();
          Long nb_other_non_eu = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 11).getValue();
          Long offers_received_meaning = (Long) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 12).getValue();

          if (offers_received != null
              || nb_sme != null
              || nb_other_eu != null
              || nb_other_non_eu != null
              || offers_received_meaning != null) {

            TENDERS tendersF03 = aw.addNewTENDERS();

            if (offers_received != null) {
              tendersF03.setNBTENDERSRECEIVED(offers_received.intValue());
            }
            if (nb_sme != null) {
              tendersF03.setNBTENDERSRECEIVEDSME(nb_sme.intValue());
            }
            if (nb_other_eu != null) {
              tendersF03.setNBTENDERSRECEIVEDOTHEREU(nb_other_eu.intValue());
            }
            if (nb_other_non_eu != null) {
              tendersF03.setNBTENDERSRECEIVEDNONEU(nb_other_non_eu.intValue());
            }
            if (offers_received_meaning != null) {
              tendersF03.setNBTENDERSRECEIVEDEMEANS(offers_received_meaning.intValue());
            }

          }

          // Appalto aggiudicato ad un raggruppamento economico
          CONTRACTORS contractorsF03 = aw.addNewCONTRACTORS();

          String awarded_group = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 13).getValue();
          if (awarded_group != null) {
            if ("1".equals(awarded_group)) {
              contractorsF03.addNewAWARDEDTOGROUP();
            } else if ("2".equals(awarded_group)) {
              contractorsF03.addNewNOAWARDEDTOGROUP();
            }
          }

          // Contraenti
          String selectW3FS3AWARD_C = "select contractor_codimp, contractor_sme from w3fs3award_c where id = ? and item = ? order by num";
          List<?> datiW3FS3AWARD_C = sqlManager.getListVector(selectW3FS3AWARD_C, new Object[] { id, item });
          if (datiW3FS3AWARD_C != null && datiW3FS3AWARD_C.size() > 0) {
            for (int c = 0; c < datiW3FS3AWARD_C.size(); c++) {
              CONTRACTOR contractor = contractorsF03.addNewCONTRACTOR();
              String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD_C.get(c), 0).getValue();
              String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD_C.get(c), 1).getValue();

              if (contractor_codimp != null) {
                contractor.setADDRESSCONTRACTOR(getContactContractor(sqlManager, contractor_codimp));
              }

              if (contractor_sme != null) {
                if ("1".equals(contractor_sme)) {
                  contractor.addNewSME();
                } else if ("2".equals(contractor_sme)) {
                  contractor.addNewNOSME();
                }
              }

            }
          }

          // Informazioni sul valore del lotto
          Double initial_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 14).getValue();
          Double final_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 15).getValue();
          Double final_low = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 16).getValue();
          Double final_high = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 17).getValue();

          VALUES valuesF03 = aw.addNewVALUES();

          if (initial_cost != null && initial_cost.doubleValue() > 0) {
            valuesF03.addNewVALESTIMATEDTOTAL().setBigDecimalValue(getCost(initial_cost));
            valuesF03.getVALESTIMATEDTOTAL().setCURRENCY(
                eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          }
          if (final_cost != null && final_cost.doubleValue() > 0) {
            valuesF03.addNewVALTOTAL().setBigDecimalValue(getCost(final_cost));
            valuesF03.getVALTOTAL().setCURRENCY(eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          } else if ((final_low != null && final_low.doubleValue() > 0) || (final_high != null && final_high.doubleValue() > 0)) {
            ValRange vr = valuesF03.addNewVALRANGETOTAL();
            vr.setCURRENCY(eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
            if (final_low != null && final_low.doubleValue() > 0) vr.setLOW(getCost(final_low));
            if (final_high != null && final_high.doubleValue() > 0) vr.setHIGH(getCost(final_high));
          }

          // Informazioni sui subappalti
          String sub_contracted = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 18).getValue();
          Double sub_value = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 19).getValue();
          Double sub_prct = (Double) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 20).getValue();
          String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS3AWARD, 21).getValue();
          if (sub_contracted != null && "1".equals(sub_contracted)) {
            aw.addNewLIKELYSUBCONTRACTED();
          }
          if (sub_value != null && sub_value.doubleValue() > 0) {
            aw.addNewVALSUBCONTRACTING().setBigDecimalValue(getCost(sub_value));
            aw.getVALSUBCONTRACTING().setCURRENCY(
                eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          }
          if (sub_prct != null) {
            aw.setPCTSUBCONTRACTING(sub_prct.intValue());
          }
          if (additional_information != null) {
            aw.setINFOADDSUBCONTRACTING(getTextFtMultiLines(additional_information));
          }

        }
      }

    }

    return awardContractF03;
  }

  /**
   * Sezione VI
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF03 _getCiF03(SqlManager sqlManager, Long id) throws SQLException {
    CiF03 ciF03 = CiF03.Factory.newInstance();

    String selectW3FS3 = "select additional_information, " // 0
        + " appeal_procedure_codein, " // 1
        + " mediation_procedure_codein, " // 2
        + " appeal_precision, " // 3
        + " appeal_service_codein, " // 4
        + " notice_date " // 5
        + " from w3fs3 "
        + " where w3fs3.id = ?";

    List<?> datiW3FS3 = sqlManager.getVector(selectW3FS3, new Object[] { id });
    if (datiW3FS3 != null && datiW3FS3.size() > 0) {
      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 0).getValue();
      if (additional_information != null) {
        ciF03.setINFOADD(getTextFtMultiLines(additional_information));
      }

      String appeal_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 1).getValue();
      if (datiW3FS3 != null) {
        ciF03.setADDRESSREVIEWBODY(getContactReview(sqlManager, appeal_procedure_codein));
      }

      String mediation_body_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 2).getValue();
      if (mediation_body_codein != null) {
        ciF03.setADDRESSMEDIATIONBODY(getContactReview(sqlManager, mediation_body_codein));
      }

      String appeal_precision = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 3).getValue();
      if (appeal_precision != null) {
        ciF03.setREVIEWPROCEDURE(getTextFtMultiLines(appeal_precision));
      }

      String appeal_service_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS3, 4).getValue();
      if (appeal_service_codein != null) {
        ciF03.setADDRESSREVIEWINFO(getContactReview(sqlManager, appeal_service_codein));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS3, 5).getValue();
      if (notice_date != null) {
        ciF03.setDATEDISPATCHNOTICE(getDate(notice_date));
      }
    }

    return ciF03;
  }

  /**
   * Contraente
   * 
   * @param sqlManager
   * @param codimp
   * @return
   * @throws SQLException
   */
  private ContactContractor getContactContractor(SqlManager sqlManager, String codimp) throws SQLException {
    ContactContractor cc = ContactContractor.Factory.newInstance();

    String selectIMPR = "select nomest, " // 0
        + " indimp, " // 1
        + " nciimp, " // 2
        + " locimp, " // 3
        + " proimp, " // 4
        + " capimp, " // 5
        + " telimp, " // 6
        + " faximp, " // 7
        + " emaiip, " // 8
        + " indweb, " // 9
        + " nazimp " // 10
        + " from impr "
        + " where codimp = ?";

    List<?> datiIMPR = sqlManager.getVector(selectIMPR, new Object[] { codimp });

    if (datiIMPR != null && datiIMPR.size() > 0) {

      String nomest = (String) SqlManager.getValueFromVectorParam(datiIMPR, 0).getValue();

      if (nomest != null) {
        nomest = conversioneCaratteriXML(nomest);
        cc.setOFFICIALNAME(nomest);
      }

      String indimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 1).getValue();
      String nciimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 2).getValue();
      if (indimp != null) {
        String indirizzo = "";
        if (indimp != null) indirizzo += indimp;
        if (nciimp != null) indirizzo += ", " + nciimp;
        indirizzo = conversioneCaratteriXML(indirizzo);
        cc.setADDRESS(indirizzo);
      }

      String town = (String) sqlManager.getObject("select town from w3impr where codimp = ?", new Object[] { codimp });
      if (town != null) cc.setTOWN(town);

      String nuts = (String) sqlManager.getObject("select nuts from w3impr where codimp = ?", new Object[] { codimp });
      if (nuts != null) {
        Nuts r209nuts = Nuts.Factory.newInstance();
        r209nuts.setCODE(TNutsCodeList.Enum.forString(nuts));
        cc.setNUTS(r209nuts);
      }

      String capimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 5).getValue();
      if (capimp != null) cc.setPOSTALCODE(capimp);

      String telimp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 6).getValue();
      if (telimp != null) cc.setPHONE(telimp);

      String faximp = (String) SqlManager.getValueFromVectorParam(datiIMPR, 7).getValue();
      if (faximp != null) cc.setFAX(faximp);

      String emaiip = (String) SqlManager.getValueFromVectorParam(datiIMPR, 8).getValue();
      if (emaiip != null) cc.setEMAIL(emaiip);

      String indweb = (String) SqlManager.getValueFromVectorParam(datiIMPR, 9).getValue();
      if (indweb != null) cc.setURL(indweb);

      Long nazimp = (Long) SqlManager.getValueFromVectorParam(datiIMPR, 10).getValue();
      if (nazimp != null) {
        String tab2tip = (String) sqlManager.getObject("select tab2tip from tab2 where tab2cod = ? and tab2d1 = ?", new Object[] { "W3z12",
            nazimp.toString() });
        Country r209country = Country.Factory.newInstance();
        r209country.setVALUE(TCountryList.Enum.forString(tab2tip));
        cc.setCOUNTRY(r209country);
      }

    }

    return cc;
  }

}
