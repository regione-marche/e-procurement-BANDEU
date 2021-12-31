package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.AcDefinition;
import eu.europa.publications.resource.schema.ted.r209.reception.AnnexD2;
import eu.europa.publications.resource.schema.ted.r209.reception.AnnexD2.DACCORDANCEARTICLE;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT.CONTRACTORS;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT.CONTRACTORS.CONTRACTOR;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT.COUNTRYORIGIN;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT.NBCONTRACTAWARDED;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT.TENDERS;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF06.AWARDEDCONTRACT.VALUES;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF05;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF06;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF06;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactContractor;
import eu.europa.publications.resource.schema.ted.r209.reception.Country;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.F062014Document.F062014;
import eu.europa.publications.resource.schema.ted.r209.reception.NoSupplies;
import eu.europa.publications.resource.schema.ted.r209.reception.NoWorks;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF06;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF06;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF06.AC;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.PROCUREMENTDISCONTINUEDDocument.PROCUREMENTDISCONTINUED;
import eu.europa.publications.resource.schema.ted.r209.reception.ProcedureF06;
import eu.europa.publications.resource.schema.ted.r209.reception.Publication;
import eu.europa.publications.resource.schema.ted.r209.reception.Services;
import eu.europa.publications.resource.schema.ted.r209.reception.Supplies;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TCountryList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.r209.reception.ValRange;
import eu.europa.publications.resource.schema.ted.x2021.nuts.Nuts;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFS6 extends ExportFormulariStandard {

  public F062014 exportF06(SqlManager sqlManager, Long id) throws GestoreException {

    F062014 f06 = F062014.Factory.newInstance();

    f06.setLG(TCeLanguageList.IT);
    f06.setCATEGORY(OriginalTranslation.ORIGINAL);
    f06.addNewFORM().setStringValue("F06");

    try {

      // Base legale
      String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id });
      if (legal_basis != null) {
        f06.addNewLEGALBASIS().setVALUE(
            eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
      }

      f06.setCONTRACTINGBODY(_getBodyF06(sqlManager, id));
      f06.setOBJECTCONTRACT(_getObjectContractF06(sqlManager, id));
      f06.setPROCEDURE(_getProcedureF06(sqlManager, id));

      String selectW3FS6AWARD = "select item from w3fs6award where id = ? order by item";
      List<?> datiW3FS6AWARD = sqlManager.getListVector(selectW3FS6AWARD, new Object[] { id });
      if (datiW3FS6AWARD != null && datiW3FS6AWARD.size() > 0) {
        for (int a = 0; a < datiW3FS6AWARD.size(); a++) {
          Long item = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD.get(a), 0).getValue();
          f06.addNewAWARDCONTRACT();
          f06.setAWARDCONTRACTArray(a, _getAwardContractF06(sqlManager, id, item, new Long(a + 1)));
        }
      }

      f06.setCOMPLEMENTARYINFO(_getCiF06(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 6", "exportXML.sqlerror", e);
    }

    return f06;

  }

  /**
   * Sezione I
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private BodyF06 _getBodyF06(SqlManager sqlManager, Long id) throws SQLException {
    BodyF05 bodyF05 = getBodyF05CEGeneric(sqlManager, id);
    BodyF06 bodyF06 = (BodyF06) bodyF05.changeType(BodyF06.type);

    return bodyF06;
  }

  /**
   * Sezione II
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF06 _getObjectContractF06(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF06 objectContractF06 = ObjectContractF06.Factory.newInstance();

    String selectW3FS6 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " cpvsupp1, " // 3
        + " cpvsupp2, " // 4
        + " type_contract, " // 5
        + " short_contract_description, " // 6
        + " div_into_lots, " // 7
        + " scope_cost, " // 8
        + " scope_low, " // 9
        + " scope_high, " // 10
        + " agree_to_publish " // 11
        + " from w3fs6 "
        + " where w3fs6.id = ?";

    List<?> datiW3FS6 = sqlManager.getVector(selectW3FS6, new Object[] { id });
    if (datiW3FS6 != null && datiW3FS6.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 0).getValue();
      if (title_contract != null) {
        objectContractF06.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 1).getValue();
      if (reference != null) {
        objectContractF06.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 4).getValue();
      if (cpv != null) {
        objectContractF06.addNewCPVMAIN();
        objectContractF06.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF06.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF06.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF06.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF06.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF06.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      // Descrizione
      String short_contract_description = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 6).getValue();
      if (short_contract_description != null) {
        objectContractF06.setSHORTDESCR(getTextFtMultiLines(short_contract_description));
      }

      // Valore
      // Il valore finale deve essere indicato se almeno un contratto e' stato
      // aggiudicato
      Long cnt_awarded = (Long) sqlManager.getObject("select count(*) from w3fs6award where id = ? and awarded = ?",
          new Object[] { id, "1" });

      String agree_to_publish = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 11).getValue();

      if (cnt_awarded != null && cnt_awarded.longValue() > 0) {
        Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS6, 8).getValue();
        Double scope_low = (Double) SqlManager.getValueFromVectorParam(datiW3FS6, 9).getValue();
        Double scope_high = (Double) SqlManager.getValueFromVectorParam(datiW3FS6, 10).getValue();
        if (scope_cost != null && scope_cost.doubleValue() > 0) {
          objectContractF06.addNewVALTOTAL().setBigDecimalValue(getCost(scope_cost));

          // Consentirne la pubblicazione ?
          if ("1".equals(agree_to_publish)) {
            objectContractF06.getVALTOTAL().setPUBLICATION(Publication.YES);
          } else if ("2".equals(agree_to_publish)) {
            objectContractF06.getVALTOTAL().setPUBLICATION(Publication.NO);
          }

          objectContractF06.getVALTOTAL().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));

        } else if (scope_low != null && scope_high != null && scope_low.doubleValue() > 0 && scope_high.doubleValue() > 0) {
          objectContractF06.addNewVALRANGETOTAL();

          // Consentirne la pubblicazione ?
          if ("1".equals(agree_to_publish)) {
            objectContractF06.getVALRANGETOTAL().setPUBLICATION(Publication.YES);
          } else if ("2".equals(agree_to_publish)) {
            objectContractF06.getVALRANGETOTAL().setPUBLICATION(Publication.NO);
          }

          objectContractF06.getVALRANGETOTAL().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          objectContractF06.getVALRANGETOTAL().setLOW(getCost(scope_low));
          objectContractF06.getVALRANGETOTAL().setHIGH(getCost(scope_high));

        }
      }

      // Suddivisione in lotti ?
      String div_into_lots = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 7).getValue();
      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Esiste suddivisione in lotti.
        objectContractF06.addNewLOTDIVISION();

        // Dati sezione II.2 (multipla)
        List<?> datiW3ANNEXB = sqlManager.getListVector("select num from w3annexb where id = ? order by lotnum", new Object[] { id });
        if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {
          for (int l = 0; l < datiW3ANNEXB.size(); l++) {
            Long w3annexb_num = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(l), 0).getValue();
            objectContractF06.addNewOBJECTDESCR();
            objectContractF06.setOBJECTDESCRArray(l, _getObjectF06(sqlManager, id, w3annexb_num, new Long(l + 1)));
          }
        }

      } else {
        // Non c'e' suddivisione in lotto. Esiste un solo lotto.
        objectContractF06.addNewNOLOTDIVISION();

        // Dati sezione II.2
        objectContractF06.addNewOBJECTDESCR();
        objectContractF06.setOBJECTDESCRArray(0, _getObjectF06(sqlManager, id, new Long(1), new Long(1)));
      }

    }

    return objectContractF06;
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
  private ObjectF06 _getObjectF06(SqlManager sqlManager, Long id, Long num, Long item_simap) throws SQLException {
    ObjectF06 objectF06 = ObjectF06.Factory.newInstance();

    String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs6 where id = ?", new Object[] { id });

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
        + " additional_information, " // 12
        + " agree_to_publish " // 13
        + " from w3annexb "
        + " where id = ? and num = ?";

    List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id, num });
    if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

      // Item
      objectF06.setITEM(item_simap.intValue());

      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Titolo
        String title = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
        if (title != null) {
          objectF06.setTITLE(getTextFtSingleLine(title));
        }

        // Numero del lotto
        Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();
        if (lotnum != null) {
          objectF06.setLOTNO(lotnum.toString());
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
            CpvSet cpvSet = objectF06.addNewCPVADDITIONAL();
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
        objectF06.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF06.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF06.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF06.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
      if (site_label != null) {
        objectF06.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
      if (description != null) {
        objectF06.setSHORTDESCR(getTextFtMultiLines(description));
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

        AC ac = objectF06.addNewAC();

        // Consentirne la pubblicazione ?
        String agree_to_publish = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
        if ("1".equals(agree_to_publish)) {
          ac.setPUBLICATION(Publication.YES);
        } else if ("2".equals(agree_to_publish)) {
          ac.setPUBLICATION(Publication.NO);
        }

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
        objectF06.addNewOPTIONS();
        if (options_descr != null) {
          objectF06.setOPTIONSDESCR(getTextFtMultiLines(options_descr));
        }
      } else {
        objectF06.addNewNOOPTIONS();
      }

      // Fondi europei
      String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
      String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
      if (eu_progr != null) {
        if ("1".equals(eu_progr) && eu_progr_descr != null) {
          eu_progr_descr = conversioneCaratteriXML(eu_progr_descr);
          objectF06.setEUPROGRRELATED(getTextFtMultiLines(eu_progr_descr));
        } else {
          objectF06.addNewNOEUPROGRRELATED();
        }
      }

    }

    return objectF06;
  }

  /**
   * Sezione IV
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ProcedureF06 _getProcedureF06(SqlManager sqlManager, Long id) throws SQLException {
    ProcedureF06 procedureF06 = ProcedureF06.Factory.newInstance();

    String selectW3FS6 = "select type_procedure, " // 0
        + " accelerated, " // 1
        + " framework, " // 2
        + " dps, " // 3
        + " is_electronic, " // 4
        + " contract_covered_gpa, " // 5
        + " notice_number_oj, " // 6
        + " termination_dps, " // 7
        + " termination_pin " // 8
        + " from w3fs6 "
        + " where id = ?";

    List<?> datiW3FS6 = sqlManager.getVector(selectW3FS6, new Object[] { id });
    if (datiW3FS6 != null && datiW3FS6.size() > 0) {

      // Tipo procedura
      String type_procedure = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 0).getValue();

      if (type_procedure != null) {
        if ("1".equals(type_procedure)) {
          procedureF06.addNewPTOPEN();
        } else if ("2".equals(type_procedure)) {
          procedureF06.addNewPTRESTRICTED();
        } else if ("6".equals(type_procedure)) {
          procedureF06.addNewPTCOMPETITIVEDIALOGUE();
        } else if ("8".equals(type_procedure)) {
          procedureF06.setPTAWARDCONTRACTWITHOUTCALL(_getAnnexD2(sqlManager, id));
        } else if ("10".equals(type_procedure)) {
          procedureF06.addNewPTINNOVATIONPARTNERSHIP();
        } else if ("11".equals(type_procedure)) {
          procedureF06.addNewPTNEGOTIATEDWITHPRIORCALL();
        }
      }

      // Informazioni su accordo quadro e sistema dinamico di acquisizione
      String framework = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 2).getValue();
      if (framework != null && "1".equals(framework)) {
        procedureF06.addNewFRAMEWORK();
      }

      // Sistema dinamico
      String dps = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 3).getValue();
      if (dps != null && "1".equals(dps)) {
        procedureF06.addNewDPS();
      }

      // Asta elettronica
      String is_electronic = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 4).getValue();
      if (is_electronic != null && "1".equals(is_electronic)) {
        procedureF06.addNewEAUCTIONUSED();
      }

      // Accordo sugli appalti pubblici ?
      String contract_covered_gpa = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 5).getValue();
      if (contract_covered_gpa != null && "1".equals(contract_covered_gpa)) {
        procedureF06.addNewCONTRACTCOVEREDGPA();
      } else {
        procedureF06.addNewNOCONTRACTCOVEREDGPA();
      }

      // Pubblicazione precedente
      String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 6).getValue();
      if (notice_number_oj != null) {
        procedureF06.setNOTICENUMBEROJ(notice_number_oj);
      }

      // Chiusura sistema dinamico di acquisizione
      String termination_dps = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 7).getValue();
      if (termination_dps != null && "1".equals(termination_dps)) {
        procedureF06.addNewTERMINATIONDPS();
      }

      // Fine validita'
      String termination_pin = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 8).getValue();
      if (termination_pin != null && "1".equals(termination_pin)) {
        procedureF06.addNewTERMINATIONPIN();
      }

    }

    return procedureF06;
  }

  /**
   * Allegato D2
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private AnnexD2 _getAnnexD2(SqlManager sqlManager, Long id) throws SQLException {

    AnnexD2 annexD2 = AnnexD2.Factory.newInstance();

    String selectW3ANNEXD = "select no_tenders_requests, " // 0
        + " manufactured, " // 1
        + " particular_tendered, " // 2
        + " extreme_urgency, " // 3
        + " additional_works, " // 4
        + " works_repetition, " // 5
        + " service_contract, " // 6
        + " supplies_quoted, " // 7
        + " purchase_supplies, " // 8
        + " outside_scope, " // 9
        + " reason_contract_lawful, " // 10
        + " bargain_purchase " // 11
        + " from w3annexd where id = ?";

    List<?> datiW3ANNEXD = sqlManager.getVector(selectW3ANNEXD, new Object[] { id });
    if (datiW3ANNEXD != null && datiW3ANNEXD.size() > 0) {

      String outside_scope = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 9).getValue();
      if (outside_scope != null && "1".equals(outside_scope)) {
        annexD2.addNewDOUTSIDESCOPE();
      } else {
        DACCORDANCEARTICLE d2 = annexD2.addNewDACCORDANCEARTICLE();

        String type_contract = (String) sqlManager.getObject("select type_contract from w3fs6 where id = ?", new Object[] { id });

        // 1 OPEN_PROCEDURE una procedura aperta
        // 2 RESTRICTED_PROCEDURE una procedura ristretta
        String no_tenders_request = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 0).getValue();
        if (no_tenders_request != null && "1".equals(no_tenders_request)) {
          d2.addNewDNOTENDERSREQUESTS();
        }

        String manufactured = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 1).getValue();
        if (manufactured != null && "1".equals(manufactured)) {
          d2.addNewDPURERESEARCH();
        }

        // 1 - TECHNICAL di carattere tecnico
        // 2 - ARTISTIC di carattere artistico
        // 3 - EXCLUSIVE_RIGHTS connesso alla tutela di diritti esclusivi
        String particular_tendered = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 2).getValue();
        if (particular_tendered != null) {
          if ("1".equals(particular_tendered)) d2.addNewDTECHNICAL();
          if ("2".equals(particular_tendered)) d2.addNewDARTISTIC();
          if ("3".equals(particular_tendered)) d2.addNewDPROTECTRIGHTS();
        }

        String extreme_urgency = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 3).getValue();
        if (extreme_urgency != null && "1".equals(extreme_urgency)) {
          d2.addNewDEXTREMEURGENCY();
        }

        String additional_works = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 4).getValue();
        if (additional_works != null && "1".equals(additional_works)) {
          d2.addNewDADDDELIVERIESORDERED();
        }

        String works_repetition = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 5).getValue();
        if (works_repetition != null && "1".equals(works_repetition)) {
          if (type_contract != null) {
            if ("SERV".equals(type_contract)) {
              d2.addNewDREPETITIONEXISTING().setCTYPE(NoSupplies.CTYPE.SERVICES);
            } else if ("WORK".equals(type_contract)) {
              d2.addNewDREPETITIONEXISTING().setCTYPE(NoSupplies.CTYPE.WORKS);
            }
          }
        }

        String service_contract = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 6).getValue();
        if (service_contract != null && "1".equals(service_contract)) {
          d2.addNewDCONTRACTAWARDEDDESIGNCONTEST().setCTYPE(Services.CTYPE.SERVICES);
        }

        String supplies_quoted = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 7).getValue();
        if (supplies_quoted != null && "1".equals(supplies_quoted)) {
          d2.addNewDCOMMODITYMARKET().setCTYPE(Supplies.CTYPE.SUPPLIES);
        }

        // 1 presso il curatore o il liquidatore di un fallimento o di un
        // concordato giudiziario
        // 2 presso un fornitore che cessa definitivamente l'attività
        String purchase_supplies = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 8).getValue();
        if (purchase_supplies != null) {
          if ("1".equals(purchase_supplies)) {
            if (type_contract != null) {
              if ("SERV".equals(type_contract)) {
                d2.addNewDFROMLIQUIDATORCREDITOR().setCTYPE(NoWorks.CTYPE.SERVICES);
              } else if ("SUPP".equals(type_contract)) {
                d2.addNewDFROMLIQUIDATORCREDITOR().setCTYPE(NoWorks.CTYPE.SUPPLIES);
              }
            }
          } else if ("2".equals(purchase_supplies)) {
            if (type_contract != null) {
              if ("SERV".equals(type_contract)) {
                d2.addNewDFROMWINDINGPROVIDER().setCTYPE(NoWorks.CTYPE.SERVICES);
              } else if ("SUPP".equals(type_contract)) {
                d2.addNewDFROMWINDINGPROVIDER().setCTYPE(NoWorks.CTYPE.SUPPLIES);
              }
            }

          }
        }

        // Acquisto di opportunita'
        String bargain_purchase = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 11).getValue();
        if (bargain_purchase != null && "1".equals(bargain_purchase)) {
          d2.addNewDBARGAINPURCHASE();
        }

      }

      String reason_contract_lawful = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXD, 10).getValue();
      if (reason_contract_lawful != null) {
        annexD2.setDJUSTIFICATION(getTextFtMultiLines(reason_contract_lawful));
      }
    }

    return annexD2;

  }

  /**
   * Sezione V
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private AwardContractF06 _getAwardContractF06(SqlManager sqlManager, Long id, Long item, Long item_simap) throws SQLException {
    AwardContractF06 awardContractF06 = AwardContractF06.Factory.newInstance();

    String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs6 where id = ?", new Object[] { id });

    String selectW3FS6AWARD = "select contract_number, " // 0
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
        + " additional_information, " // 21
        + " agree_to_publish_tenders, " // 22
        + " agree_to_publish_contractor, " // 23
        + " agree_to_publish_value, " // 24
        + " val_bargain_purchase, " // 25
        + " nb_contract_awarded, " // 26
        + " community_origin, " // 27
        + " non_community_origin, " // 28
        + " nco_country_1, " // 29
        + " nco_country_2, " // 30
        + " nco_country_3, " // 31
        + " nco_country_4, " // 32
        + " nco_country_5, " // 33
        + " nco_country_6, " // 34
        + " nco_country_7, " // 35
        + " nco_country_8, " // 36
        + " nco_country_9, " // 37
        + " nco_country_10, " // 38
        + " awarded_tenderer_variant, " // 39
        + " tenders_excluded " // 40
        + " from w3fs6award "
        + " where id = ? and item = ?";

    List<?> datiW3FS6AWARD = sqlManager.getVector(selectW3FS6AWARD, new Object[] { id, item });
    if (datiW3FS6AWARD != null && datiW3FS6AWARD.size() > 0) {
      awardContractF06.setITEM(item_simap.intValue());

      // Numero contratto
      String contract_number = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 0).getValue();
      if (contract_number != null) {
        contract_number = conversioneCaratteriXML(contract_number);
        awardContractF06.setCONTRACTNO(contract_number);
      }

      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Numero lotto
        Long lot_number = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 1).getValue();
        if (lot_number != null) {
          awardContractF06.setLOTNO(lot_number.toString());
        }
      }

      // Titolo
      String contract_title = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 2).getValue();
      if (contract_title != null) {
        awardContractF06.setTITLE(getTextFtSingleLine(contract_title));
      }

      String awarded = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 3).getValue();
      if (awarded != null) {
        if ("2".equals(awarded)) {
          // Appalto o lotto non aggiudicato
          String no_awarded_type = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 4).getValue();
          String original = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 5).getValue();
          Date date_original = (Date) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 6).getValue();
          if (no_awarded_type != null) {
            if ("1".equals(no_awarded_type)) {
              // Altri motivi (interruzione della procedura)
              PROCUREMENTDISCONTINUED pd = awardContractF06.addNewNOAWARDEDCONTRACT().addNewPROCUREMENTDISCONTINUED();
              pd.addNewORIGINALOTHERMEANS().setStringValue(original);
              pd.addNewDATEDISPATCHORIGINAL().setCalendarValue(getDate(date_original));

              pd.getORIGINALOTHERMEANS().addNewPUBLICATION().setStringValue("NO");
              pd.getDATEDISPATCHORIGINAL().addNewPUBLICATION().setStringValue("NO");

              // pd.addNewORIGINALOTHERMEANS().addNewPUBLICATION().setStringValue(original);
              // pd.addNewDATEDISPATCHORIGINAL().addNewPUBLICATION().setStringValue(getDate(date_original).toString());
            } else if ("2".equals(no_awarded_type)) {
              // Non sono pervenute o sono state tutte respinte le offerte o
              // domande di partecipazione
              awardContractF06.addNewNOAWARDEDCONTRACT().addNewPROCUREMENTUNSUCCESSFUL();
            }
          }

        } else if ("1".equals(awarded)) {
          // Appalto o lotto aggiudicato
          AWARDEDCONTRACT aw = awardContractF06.addNewAWARDEDCONTRACT();

          // Data di conclusione
          Date date_original = (Date) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 7).getValue();
          if (date_original != null) {
            aw.setDATECONCLUSIONCONTRACT(getDate(date_original));
          }

          // Informazioni sulle offerte
          Long offers_received = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 8).getValue();
          Long nb_sme = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 9).getValue();
          Long nb_other_eu = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 10).getValue();
          Long nb_other_non_eu = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 11).getValue();
          Long offers_received_meaning = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 12).getValue();

          if (offers_received != null
              || nb_sme != null
              || nb_other_eu != null
              || nb_other_non_eu != null
              || offers_received_meaning != null) {

            TENDERS tendersF06 = aw.addNewTENDERS();

            // Consentirne la pubblicazione ?
            String agree_to_publish_tenders = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 22).getValue();
            if ("1".equals(agree_to_publish_tenders)) {
              tendersF06.setPUBLICATION(Publication.YES);
            } else if ("2".equals(agree_to_publish_tenders)) {
              tendersF06.setPUBLICATION(Publication.NO);
            }

            if (offers_received != null) {
              tendersF06.setNBTENDERSRECEIVED(offers_received.intValue());
            }
            if (nb_sme != null) {
              tendersF06.setNBTENDERSRECEIVEDSME(nb_sme.intValue());
            }
            if (nb_other_eu != null) {
              tendersF06.setNBTENDERSRECEIVEDOTHEREU(nb_other_eu.intValue());
            }
            if (nb_other_non_eu != null) {
              tendersF06.setNBTENDERSRECEIVEDNONEU(nb_other_non_eu.intValue());
            }
            if (offers_received_meaning != null) {
              tendersF06.setNBTENDERSRECEIVEDEMEANS(offers_received_meaning.intValue());
            }

          }

          // Appalto aggiudicato ad un raggruppamento economico
          CONTRACTORS contractorsF06 = aw.addNewCONTRACTORS();

          // Consentirne la pubblicazione ?
          String agree_to_publish_contractor = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 23).getValue();
          if ("1".equals(agree_to_publish_contractor)) {
            contractorsF06.setPUBLICATION(Publication.YES);
          } else if ("2".equals(agree_to_publish_contractor)) {
            contractorsF06.setPUBLICATION(Publication.NO);
          }

          String awarded_group = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 13).getValue();
          if (awarded_group != null) {
            if ("1".equals(awarded_group)) {
              contractorsF06.addNewAWARDEDTOGROUP();
            } else if ("2".equals(awarded_group)) {
              contractorsF06.addNewNOAWARDEDTOGROUP();
            }
          }

          // Contraenti
          String selectW3FS6AWARD_C = "select contractor_codimp, contractor_sme from w3fs6award_c where id = ? and item = ? order by num";
          List<?> datiW3FS6AWARD_C = sqlManager.getListVector(selectW3FS6AWARD_C, new Object[] { id, item });
          if (datiW3FS6AWARD_C != null && datiW3FS6AWARD_C.size() > 0) {
            for (int c = 0; c < datiW3FS6AWARD_C.size(); c++) {
              CONTRACTOR contractor = contractorsF06.addNewCONTRACTOR();
              String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD_C.get(c), 0).getValue();
              String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD_C.get(c), 1).getValue();

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
          Double initial_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 14).getValue();
          Double final_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 15).getValue();
          Double final_low = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 16).getValue();
          Double final_high = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 17).getValue();

          VALUES valuesF06 = aw.addNewVALUES();

          // Consentirne la pubblicazione ?
          String agree_to_publish_value = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 24).getValue();
          if ("1".equals(agree_to_publish_value)) {
            valuesF06.setPUBLICATION(Publication.YES);
          } else if ("2".equals(agree_to_publish_value)) {
            valuesF06.setPUBLICATION(Publication.NO);
          }

          if (initial_cost != null && initial_cost.doubleValue() > 0) {
            valuesF06.addNewVALESTIMATEDTOTAL().setBigDecimalValue(getCost(initial_cost));
            valuesF06.getVALESTIMATEDTOTAL().setCURRENCY(
                eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          }
          if (final_cost != null && final_cost.doubleValue() > 0) {
            valuesF06.addNewVALTOTAL().setBigDecimalValue(getCost(final_cost));
            valuesF06.getVALTOTAL().setCURRENCY(eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          } else if ((final_low != null && final_low.doubleValue() > 0) || (final_high != null && final_high.doubleValue() > 0)) {
            ValRange vr = valuesF06.addNewVALRANGETOTAL();
            vr.setCURRENCY(eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
            if (final_low != null && final_low.doubleValue() > 0) vr.setLOW(getCost(final_low));
            if (final_high != null && final_high.doubleValue() > 0) vr.setHIGH(getCost(final_high));
          }

          // Informazioni sui subappalti
          String sub_contracted = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 18).getValue();
          Double sub_value = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 19).getValue();
          Double sub_prct = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 20).getValue();
          String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 21).getValue();
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

          // Prezzo pagato per gli acquisti di opportunita'
          Double val_bargain_purchase = (Double) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 25).getValue();
          if (val_bargain_purchase != null && val_bargain_purchase.doubleValue() > 0) {
            aw.addNewVALBARGAINPURCHASE().setBigDecimalValue(getCost(val_bargain_purchase));
            aw.getVALBARGAINPURCHASE().setCURRENCY(
                eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
          }

          // Numero di contratti d'appalto aggiudicati
          Long nb_contract_awarded = (Long) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 26).getValue();
          if (nb_contract_awarded != null && nb_contract_awarded.longValue() > 0) {
            NBCONTRACTAWARDED nb = aw.addNewNBCONTRACTAWARDED();
            nb.setIntValue(nb_contract_awarded.intValue());
            nb.addNewPUBLICATION().setStringValue("NO");
          }

          // Paese di origine del prodotto o del servizio
          String community_origin = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 27).getValue();
          String non_community_origin = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 28).getValue();
          String nco_country_1 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 29).getValue();
          String nco_country_2 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 30).getValue();
          String nco_country_3 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 31).getValue();
          String nco_country_4 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 32).getValue();
          String nco_country_5 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 33).getValue();
          String nco_country_6 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 34).getValue();
          String nco_country_7 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 35).getValue();
          String nco_country_8 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 36).getValue();
          String nco_country_9 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 37).getValue();
          String nco_country_10 = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 38).getValue();

          if ((community_origin != null && "1".equals(community_origin))
              || (non_community_origin != null && "1".equals(non_community_origin))) {

            COUNTRYORIGIN co = aw.addNewCOUNTRYORIGIN();
            co.addNewPUBLICATION().setStringValue("NO");

            if (community_origin != null && "1".equals(community_origin)) {
              co.addNewCOMMUNITYORIGIN();
            }

            if (non_community_origin != null && "1".equals(non_community_origin)) {
              if (nco_country_1 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_1));
              if (nco_country_2 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_2));
              if (nco_country_3 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_3));
              if (nco_country_4 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_4));
              if (nco_country_5 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_5));
              if (nco_country_6 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_6));
              if (nco_country_7 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_7));
              if (nco_country_8 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_8));
              if (nco_country_9 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_9));
              if (nco_country_10 != null) co.addNewNONCOMMUNITYORIGIN().setVALUE(TCountryList.Enum.forString(nco_country_10));
            }

          }

          // Il contratto d'appalto e' stato aggiudicato a un offertente che ha
          // proposto una variante
          String awarded_tenderer_variant = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 39).getValue();
          if (awarded_tenderer_variant != null) {
            if ("1".equals(awarded_tenderer_variant)) {
              aw.addNewAWARDEDTENDERERVARIANT();
              aw.getAWARDEDTENDERERVARIANT().addNewPUBLICATION().setStringValue("NO");
            } else if ("2".equals(awarded_tenderer_variant)) {
              aw.addNewNOAWARDEDTENDERERVARIANT();
              aw.getNOAWARDEDTENDERERVARIANT().addNewPUBLICATION().setStringValue("NO");
            }
          }

          // Sono state escluse offerte in quanto anormalmente basse
          String tenders_excluded = (String) SqlManager.getValueFromVectorParam(datiW3FS6AWARD, 40).getValue();
          if (tenders_excluded != null) {
            if ("1".equals(tenders_excluded)) {
              aw.addNewTENDERSEXCLUDED();
              aw.getTENDERSEXCLUDED().addNewPUBLICATION().setStringValue("NO");
            } else if ("2".equals(tenders_excluded)) {
              aw.addNewNOTENDERSEXCLUDED();
              aw.getNOTENDERSEXCLUDED().addNewPUBLICATION().setStringValue("NO");
            }
          }

        }
      }

    }

    return awardContractF06;
  }

  /**
   * Sezione VI
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF06 _getCiF06(SqlManager sqlManager, Long id) throws SQLException {
    CiF06 ciF06 = CiF06.Factory.newInstance();

    String selectW3FS6 = "select additional_information, " // 0
        + " appeal_procedure_codein, " // 1
        + " mediation_procedure_codein, " // 2
        + " appeal_precision, " // 3
        + " appeal_service_codein, " // 4
        + " notice_date " // 5
        + " from w3fs6 "
        + " where w3fs6.id = ?";

    List<?> datiW3FS6 = sqlManager.getVector(selectW3FS6, new Object[] { id });
    if (datiW3FS6 != null && datiW3FS6.size() > 0) {
      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 0).getValue();
      if (additional_information != null) {
        ciF06.setINFOADD(getTextFtMultiLines(additional_information));
      }

      String appeal_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 1).getValue();
      if (datiW3FS6 != null) {
        ciF06.setADDRESSREVIEWBODY(getContactReview(sqlManager, appeal_procedure_codein));
      }

      String mediation_body_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 2).getValue();
      if (mediation_body_codein != null) {
        ciF06.setADDRESSMEDIATIONBODY(getContactReview(sqlManager, mediation_body_codein));
      }

      String appeal_precision = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 3).getValue();
      if (appeal_precision != null) {
        ciF06.setREVIEWPROCEDURE(getTextFtMultiLines(appeal_precision));
      }

      String appeal_service_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS6, 4).getValue();
      if (appeal_service_codein != null) {
        ciF06.setADDRESSREVIEWINFO(getContactReview(sqlManager, appeal_service_codein));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS6, 5).getValue();
      if (notice_date != null) {
        ciF06.setDATEDISPATCHNOTICE(getDate(notice_date));
      }
    }

    return ciF06;
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
