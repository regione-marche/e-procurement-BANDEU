package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlAnySimpleType;

import eu.europa.publications.resource.schema.ted.r209.reception.AcDefinition;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF01;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF05;
import eu.europa.publications.resource.schema.ted.r209.reception.CeActivity;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF05;
import eu.europa.publications.resource.schema.ted.r209.reception.CondForOpeningTenders;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.DurationUnitMD;
import eu.europa.publications.resource.schema.ted.r209.reception.F052014Document.F052014;
import eu.europa.publications.resource.schema.ted.r209.reception.FrameworkInfo;
import eu.europa.publications.resource.schema.ted.r209.reception.LeftiF05;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF05;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF05;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.ProcedureF05;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFS5 extends ExportFormulariStandard {

  public F052014 exportF05(SqlManager sqlManager, Long id) throws GestoreException {

    F052014 f05 = F052014.Factory.newInstance();

    f05.setLG(TCeLanguageList.IT);
    f05.setCATEGORY(OriginalTranslation.ORIGINAL);
    f05.addNewFORM().setStringValue("F05");

    try {

      // Base legale
      String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id });
      if (legal_basis != null) {
        f05.addNewLEGALBASIS().setVALUE(
            eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
      }

      f05.setCONTRACTINGBODY(getBodyF05CEGeneric(sqlManager, id));
      f05.setOBJECTCONTRACT(_getObjectContractF05(sqlManager, id));
      f05.setLEFTI(_getLeftiF05(sqlManager, id));
      f05.setPROCEDURE(_getProcedureF05(sqlManager, id));
      f05.setCOMPLEMENTARYINFO(_getCiF05(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 5", "exportXML.sqlerror", e);
    }

    return f05;

  }


  /**
   * Sezione II.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF05 _getObjectContractF05(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF05 objectContractF05 = ObjectContractF05.Factory.newInstance();

    String selectW3FS5 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " cpvsupp1, " // 3
        + " cpvsupp2, " // 4
        + " type_contract, " // 5
        + " scope_total, " // 6
        + " scope_cost, " // 7
        + " div_into_lots, " // 8
        + " div_lots_value, " // 9
        + " div_lots_max, " // 10
        + " lots_max_tenderer, " // 11
        + " lots_combining " // 12
        + " from w3fs5 "
        + " where w3fs5.id = ?";

    List<?> datiW3FS5 = sqlManager.getVector(selectW3FS5, new Object[] { id });
    if (datiW3FS5 != null && datiW3FS5.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 0).getValue();
      if (title_contract != null) {
        objectContractF05.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 1).getValue();
      if (reference != null) {
        objectContractF05.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 4).getValue();
      if (cpv != null) {
        objectContractF05.addNewCPVMAIN();
        objectContractF05.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF05.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF05.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF05.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF05.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF05.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      // Descrizione
      String scope_total = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 6).getValue();
      if (scope_total != null) {
        objectContractF05.setSHORTDESCR(getTextFtMultiLines(scope_total));
      }

      // Valore
      Double scope_cost = (Double) SqlManager.getValueFromVectorParam(datiW3FS5, 7).getValue();
      if (scope_cost != null && scope_cost.doubleValue() > 0) {
        objectContractF05.addNewVALESTIMATEDTOTAL().setBigDecimalValue(getCost(scope_cost));
        objectContractF05.getVALESTIMATEDTOTAL().setCURRENCY(
            eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
      }

      // Suddivisione in lotti ?
      String div_into_lots = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 8).getValue();
      String div_lots = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 9).getValue();
      Long div_lots_max = (Long) SqlManager.getValueFromVectorParam(datiW3FS5, 10).getValue();
      Long lots_max_tenderer = (Long) SqlManager.getValueFromVectorParam(datiW3FS5, 11).getValue();
      String lots_combining = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 12).getValue();
      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Esiste suddivisione in lotti.
        objectContractF05.addNewLOTDIVISION();
        if (div_lots != null) {
          if ("1".equals(div_lots)) {
            // Un solo lotto
            objectContractF05.getLOTDIVISION().addNewLOTONEONLY();
          } else if ("2".equals(div_lots)) {
            // Uno o piu' lotti
            if (div_lots_max != null) {
              objectContractF05.getLOTDIVISION().setLOTMAXNUMBER(div_lots_max.intValue());
            }
          } else if ("3".equals(div_lots)) {
            // Tutti i lotti
            objectContractF05.getLOTDIVISION().addNewLOTALL();
          }
        }
        if (lots_max_tenderer != null) {
          objectContractF05.getLOTDIVISION().setLOTMAXONETENDERER(lots_max_tenderer.intValue());
        }
        if (lots_combining != null) {
          objectContractF05.getLOTDIVISION().setLOTCOMBININGCONTRACTRIGHT(getTextFtMultiLines(lots_combining));
        }

        // Dati sezione II.2 (multipla)
        List<?> datiW3ANNEXB = sqlManager.getListVector("select num from w3annexb where id = ? order by lotnum", new Object[] { id });
        if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {
          for (int l = 0; l < datiW3ANNEXB.size(); l++) {
            Long w3annexb_num = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB.get(l), 0).getValue();
            objectContractF05.addNewOBJECTDESCR();
            objectContractF05.setOBJECTDESCRArray(l, _getObjectF05(sqlManager, id, w3annexb_num, new Long(l + 1)));
          }
        }

      } else {
        // Non c'e' suddivisione in lotto. Esiste un solo lotto.
        objectContractF05.addNewNOLOTDIVISION();

        // Dati sezione II.2
        objectContractF05.addNewOBJECTDESCR();
        objectContractF05.setOBJECTDESCRArray(0, _getObjectF05(sqlManager, id, new Long(1), new Long(1)));
      }

    }

    return objectContractF05;
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
  private ObjectF05 _getObjectF05(SqlManager sqlManager, Long id, Long num, Long item_simap) throws SQLException {
    ObjectF05 objectF05 = ObjectF05.Factory.newInstance();

    String div_into_lots = (String) sqlManager.getObject("select div_into_lots from w3fs5 where id = ?", new Object[] { id });

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
        + " additional_information, " // 21
        + " nb_env_candidate, " // 22
        + " nb_min_candidate, " // 23
        + " nb_max_candidate, " // 24
        + " criteria_candidate, " // 25
        + " ecatalogue_required " // 26
        + " from w3annexb "
        + " where id = ? and num = ?";

    List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id, num });
    if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

      // Item
      objectF05.setITEM(item_simap.intValue());

      if (div_into_lots != null && "1".equals(div_into_lots)) {
        // Titolo
        String title = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
        if (title != null) {
          objectF05.setTITLE(getTextFtSingleLine(title));
        }

        // Numero del lotto
        Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();
        if (lotnum != null) {
          objectF05.setLOTNO(lotnum.toString());
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
            CpvSet cpvSet = objectF05.addNewCPVADDITIONAL();
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
        objectF05.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF05.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF05.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF05.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
      if (site_label != null) {
        objectF05.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
      if (description != null) {
        objectF05.setSHORTDESCR(getTextFtMultiLines(description));
      }

      // Criteri di aggiudicazione
      String ac_doc = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
      if (ac_doc != null) {

        eu.europa.publications.resource.schema.ted.r209.reception.ObjectF05.AC ac = objectF05.addNewAC();

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

      // Valore stimato
      Double cost = (Double) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();
      if (cost != null && cost.doubleValue() > 0) {
        objectF05.addNewVALOBJECT().setBigDecimalValue(getCost(cost));
        objectF05.getVALOBJECT().setCURRENCY(eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
      }

      // Durata del contratto
      Long work_month = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
      Long work_days = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
      Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 12).getValue();
      Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
      if (work_month != null || work_days != null) {
        if (work_month != null) {
          objectF05.addNewDURATION().setTYPE(DurationUnitMD.MONTH);
          objectF05.getDURATION().setStringValue(work_month.toString());
        } else if (work_days != null) {
          objectF05.addNewDURATION().setTYPE(DurationUnitMD.DAY);
          objectF05.getDURATION().setStringValue(work_days.toString());
        }
      }
      if (work_start_date != null) {
        objectF05.setDATESTART(getDate(work_start_date));
      }
      if (work_end_date != null) {
        objectF05.setDATEEND(getDate(work_end_date));
      }

      // Rinnovi
      String renewal = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 14).getValue();
      String renewal_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 15).getValue();
      if (renewal != null && "1".equals(renewal)) {
        objectF05.addNewRENEWAL();
        if (renewal_descr != null) {
          objectF05.setRENEWALDESCR(getTextFtMultiLines(renewal_descr));
        }
      } else {
        objectF05.addNewNORENEWAL();
      }

      // Limiti al numero di candidati e criteri
      Long nb_env_candidate = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 22).getValue();
      Long nb_min_candidate = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 23).getValue();
      Long nb_max_candidate = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 24).getValue();
      String criteria_candidate = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 25).getValue();
      if (nb_env_candidate != null) {
        objectF05.setNBENVISAGEDCANDIDATE(nb_env_candidate.intValue());
      } else {
        if (nb_min_candidate != null) {
          objectF05.setNBMINLIMITCANDIDATE(nb_min_candidate.intValue());
        }
        if (nb_max_candidate != null) {
          objectF05.setNBMAXLIMITCANDIDATE(nb_max_candidate.intValue());
        }
      }
      if (criteria_candidate != null) {
        objectF05.setCRITERIACANDIDATE(getTextFtMultiLines(criteria_candidate));
      }

      // Varianti
      String acc_variants = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 16).getValue();
      if (acc_variants != null && "1".equals(acc_variants)) {
        objectF05.addNewACCEPTEDVARIANTS();
      } else {
        objectF05.addNewNOACCEPTEDVARIANTS();
      }

      // Opzioni
      String options = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 17).getValue();
      String options_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 18).getValue();
      if (options != null && "1".equals(options)) {
        objectF05.addNewOPTIONS();
        if (options_descr != null) {
          objectF05.setOPTIONSDESCR(getTextFtMultiLines(options_descr));
        }
      } else {
        objectF05.addNewNOOPTIONS();
      }

      // Catalogo elettronico ?
      String ecatalogue_required = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 26).getValue();
      if (ecatalogue_required != null && "1".equals(ecatalogue_required)) {
        objectF05.addNewECATALOGUEREQUIRED();
      }

      // Fondi europei
      String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 19).getValue();
      String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 20).getValue();
      if (eu_progr != null) {
        if ("1".equals(eu_progr) && eu_progr_descr != null) {
          eu_progr_descr = conversioneCaratteriXML(eu_progr_descr);
          objectF05.setEUPROGRRELATED(getTextFtMultiLines(eu_progr_descr));
        } else {
          objectF05.addNewNOEUPROGRRELATED();
        }
      }

      // Informazioni complementari
      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 21).getValue();
      if (additional_information != null) {
        objectF05.setINFOADD(getTextFtMultiLines(additional_information));
      }

    }

    return objectF05;
  }

  /**
   * Sezione III.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private LeftiF05 _getLeftiF05(SqlManager sqlManager, Long id) throws SQLException {
    LeftiF05 leftiF05 = LeftiF05.Factory.newInstance();

    String selectW3FS5S36 = "select operators_personal_situation, " // 0
        + " eco_criteria_doc, " // 1
        + " eaf_capacity_information, " // 2
        + " eaf_capacity_min_level, " // 3
        + " tech_crit_doc, " // 4
        + " t_capacity_information, " // 5
        + " t_capacity_min_level, " // 6
        + " restricted_sheltered, " // 7
        + " restricted_frame, " // 8
        + " service_reserved, " // 9
        + " service_res_desc, " // 10
        + " performance_conditions, " // 11
        + " request_names " // 12
        + " from w3fs5s36 "
        + " where id = ?";

    List<?> datiW3FS5S36 = sqlManager.getVector(selectW3FS5S36, new Object[] { id });
    if (datiW3FS5S36 != null && datiW3FS5S36.size() > 0) {
      String suitability = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 0).getValue();
      if (suitability != null) {
        leftiF05.setSUITABILITY(getTextFtMultiLines(suitability));
      }

      String eco_criteria_doc = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 1).getValue();
      String eco_fin_info = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 2).getValue();
      String eco_fin_min_level = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 3).getValue();
      if (eco_criteria_doc != null && "1".equals(eco_criteria_doc)) {
        leftiF05.addNewECONOMICCRITERIADOC();
      } else {
        if (eco_fin_info != null) leftiF05.setECONOMICFINANCIALINFO(getTextFtMultiLines(eco_fin_info));
        if (eco_fin_min_level != null) leftiF05.setECONOMICFINANCIALMINLEVEL(getTextFtMultiLines(eco_fin_min_level));
      }

      String tech_crit_doc = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 4).getValue();
      String tech_prof_info = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 5).getValue();
      String tech_prof_min_level = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 6).getValue();
      if (tech_crit_doc != null && "1".equals(tech_crit_doc)) {
        leftiF05.addNewTECHNICALCRITERIADOC();
      } else {
        if (tech_prof_info != null) leftiF05.setTECHNICALPROFESSIONALINFO(getTextFtMultiLines(tech_prof_info));
        if (tech_prof_min_level != null) leftiF05.setTECHNICALPROFESSIONALMINLEVEL(getTextFtMultiLines(tech_prof_min_level));
      }

      String restricted_sheltered = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 7).getValue();
      if (restricted_sheltered != null && "1".equals(restricted_sheltered)) {
        leftiF05.addNewRESTRICTEDSHELTEREDWORKSHOP();
      }

      String restricted_frame = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 8).getValue();
      if (restricted_frame != null && "1".equals(restricted_frame)) {
        leftiF05.addNewRESTRICTEDSHELTEREDPROGRAM();
      }

      String particular_profession = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 9).getValue();
      String reference_to_law = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 10).getValue();
      if (particular_profession != null && "1".equals(particular_profession)) {
        leftiF05.addNewPARTICULARPROFESSION().setCTYPE(eu.europa.publications.resource.schema.ted.r209.reception.Services.CTYPE.SERVICES);
        if (reference_to_law != null) leftiF05.setREFERENCETOLAW(getTextFtMultiLines(reference_to_law));
      }

      String performance_conditions = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 11).getValue();
      if (performance_conditions != null) {
        leftiF05.setPERFORMANCECONDITIONS(getTextFtMultiLines(performance_conditions));
      }

      String performance_staff = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 12).getValue();
      if (performance_staff != null && "1".equals(performance_staff)) {
        leftiF05.addNewPERFORMANCESTAFFQUALIFICATION();
      }
    }

    return leftiF05;

  }

  /**
   * Sezione IV.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ProcedureF05 _getProcedureF05(SqlManager sqlManager, Long id) throws SQLException {
    ProcedureF05 procedureF05 = ProcedureF05.Factory.newInstance();

    String selectW3FS5 = "select type_procedure, " // 0
        + " accelerated, " // 1
        + " framework, " // 2
        + " fr_several_op, " // 3
        + " frame_operators_number, " // 4
        + " dps, " // 5
        + " dps_add, " // 6
        + " frame_justification, " // 7
        + " reduction, " // 8
        + " right_contract, " // 9
        + " is_electronic, " // 10
        + " use_electronic, " // 11
        + " gpa, " // 12
        + " notice_number_oj, " // 13
        + " receipt_limit_date, " // 14
        + " receipt_limit_time, " // 15
        + " dispatch_invitations_date, " // 16
        + " until_date, " // 17
        + " period_month, " // 18
        + " opening_tenders_date, " // 19
        + " opening_tenders_time, " // 20
        + " opening_tenders_place, " // 21
        + " authorised_persons_desc" // 22
        + " from w3fs5 "
        + " where id = ?";

    List<?> datiW3FS5 = sqlManager.getVector(selectW3FS5, new Object[] { id });
    if (datiW3FS5 != null && datiW3FS5.size() > 0) {

      // Tipo procedura
      String type_procedure = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 0).getValue();
      String accelerated = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 1).getValue();

      // TODO: rivedere valori per TYPE_PROCEDURE

      // 1|PT_OPEN|Aperta
      // 2|PT_RESTRICTED|Ristretta
      // 6|PT_COMPETITIVE|Dialogo competitivo
      // 8|PT_INNOVATION|Parternariato per l'innovazione
      // 11|PT_NEGOTIATED_WITH_PRIOR_CALL|Negoziata con previo avviso di
      // indizione di gara

      if (type_procedure != null) {
        if ("1".equals(type_procedure)) {
          procedureF05.addNewPTOPEN();
        } else if ("2".equals(type_procedure)) {
          procedureF05.addNewPTRESTRICTED();
        } else if ("6".equals(type_procedure)) {
          procedureF05.addNewPTCOMPETITIVEDIALOGUE();
        } else if ("8".equals(type_procedure)) {
          procedureF05.addNewPTINNOVATIONPARTNERSHIP();
        } else if ("11".equals(type_procedure)) {
          procedureF05.addNewPTNEGOTIATEDWITHPRIORCALL();
        }
      }

      // Informazioni su accordo quadro e sistema dinamico di acquisizione
      String framework = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 2).getValue();
      String fr_several_op = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 3).getValue();
      Long frame_operators_number = (Long) SqlManager.getValueFromVectorParam(datiW3FS5, 4).getValue();
      String frame_justification = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 7).getValue();
      if (framework != null && "1".equals(framework)) {
        FrameworkInfo frameworkInfo = procedureF05.addNewFRAMEWORK();
        if (fr_several_op != null && "1".equals(fr_several_op)) {
          // Accordo quadro con piu' operatori
          frameworkInfo.addNewSEVERALOPERATORS();
          if (frame_operators_number != null) {
            frameworkInfo.setNBPARTICIPANTS(frame_operators_number.intValue());
          }
        } else {
          // Singolo operatore
          frameworkInfo.addNewSINGLEOPERATOR();
        }
        if (frame_justification != null) {
          frame_justification = conversioneCaratteriXML(frame_justification);
          frameworkInfo.setJUSTIFICATION(getTextFtMultiLines(frame_justification));
        }
      }

      // Sistema dinamico
      String dps = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 5).getValue();
      String dps_add = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 6).getValue();
      if (dps != null && "1".equals(dps)) {
        procedureF05.addNewDPS();
        if (dps_add != null && "1".equals(dps_add)) procedureF05.addNewDPSADDITIONALPURCHASERS();
      }

      // Riduzione soluzioni
      String reduction = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 8).getValue();
      if (reduction != null && "1".equals(reduction)) {
        procedureF05.addNewREDUCTIONRECOURSE();
      }

      // Asta elettronica ?
      String is_electronic = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 10).getValue();
      String use_electronic = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 11).getValue();
      if (is_electronic != null && "1".equals(is_electronic)) {
        procedureF05.addNewEAUCTIONUSED();
        if (use_electronic != null) procedureF05.setINFOADDEAUCTION(getTextFtMultiLines(use_electronic));
      }

      // Accordo sugli appalti pubblici ?
      String gpa = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 12).getValue();
      if (gpa != null && "1".equals(gpa)) {
        procedureF05.addNewCONTRACTCOVEREDGPA();
      } else {
        procedureF05.addNewNOCONTRACTCOVEREDGPA();
      }

      // Pubblicazione precedente
      String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 13).getValue();
      if (notice_number_oj != null) {
        procedureF05.setNOTICENUMBEROJ(notice_number_oj);
      }

      // Termini per la ricezione delle offerte
      Date receipt_limit_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS5, 14).getValue();
      String receipt_limit_time = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 15).getValue();
      if (receipt_limit_date != null) {
        procedureF05.setDATERECEIPTTENDERS(getDate(receipt_limit_date));
        if (receipt_limit_time != null) {
          procedureF05.setTIMERECEIPTTENDERS(receipt_limit_time);
        }
      }

      // Data stimata spedizione
      Date dispatch_invitations_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS5, 16).getValue();
      if (dispatch_invitations_date != null) {
        procedureF05.setDATEDISPATCHINVITATIONS(getDate(dispatch_invitations_date));
      }

      // Lingue utilizzate
      String selectW3LANGUAGE = "select language_ec from w3language where id = ?";
      List<?> datiW3LANGUAGE = sqlManager.getListVector(selectW3LANGUAGE, new Object[] { id });
      if (datiW3LANGUAGE != null && datiW3LANGUAGE.size() > 0) {
        procedureF05.addNewLANGUAGES();
        for (int lec = 0; lec < datiW3LANGUAGE.size(); lec++) {
          String language_ec = (String) SqlManager.getValueFromVectorParam(datiW3LANGUAGE.get(lec), 0).getValue();
          procedureF05.getLANGUAGES().addNewLANGUAGE().setVALUE(
              eu.europa.publications.resource.schema.ted.r209.reception.TLanguageList.Enum.forString(language_ec));
        }
      }

      // Offerta valida
      Date until_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS5, 17).getValue();
      Long period_month = (Long) SqlManager.getValueFromVectorParam(datiW3FS5, 18).getValue();
      if (until_date != null) {
        procedureF05.setDATETENDERVALID(getDate(until_date));
      } else {
        if (period_month != null) {
          procedureF05.addNewDURATIONTENDERVALID();
          XmlAnySimpleType x = XmlAnySimpleType.Factory.newInstance();
          x.setStringValue("MONTH");
          procedureF05.getDURATIONTENDERVALID().setTYPE(x);
          procedureF05.getDURATIONTENDERVALID().setStringValue(period_month.toString());
        }
      }

      // Modalita' apertura offerte
      Date opening_tenders_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS5, 19).getValue();
      String opening_tenders_time = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 20).getValue();
      String opening_tenders_place = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 21).getValue();
      String authorised_persons_desc = (String) SqlManager.getValueFromVectorParam(datiW3FS5, 22).getValue();
      if (opening_tenders_date != null || opening_tenders_time != null || opening_tenders_place != null || authorised_persons_desc != null) {
        CondForOpeningTenders cond = procedureF05.addNewOPENINGCONDITION();
        if (opening_tenders_date != null) cond.setDATEOPENINGTENDERS(getDate(opening_tenders_date));
        if (opening_tenders_time != null) cond.setTIMEOPENINGTENDERS(opening_tenders_time);
        if (opening_tenders_place != null) cond.setPLACE(getTextFtMultiLines(opening_tenders_place));
        if (authorised_persons_desc != null) cond.setINFOADD(getTextFtMultiLines(authorised_persons_desc));
      }

    }

    return procedureF05;
  }

  /**
   * Sezione VI.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF05 _getCiF05(SqlManager sqlManager, Long id) throws SQLException {
    CiF05 ciF05 = CiF05.Factory.newInstance();

    String selectW3FS5S36 = "select w3fs5s36.recurrent_proc, " // 0
        + " w3fs5s36.recurrent_proc_desc, " // 1
        + " w3fs5s36.eordering, " // 2
        + " w3fs5s36.einvoicing, " // 3
        + " w3fs5s36.epayment, " // 4
        + " w3fs5s36.appeal_procedure_codein, " // 5
        + " w3fs5s36.mediation_procedure_codein, " // 6
        + " w3fs5s36.appeal_precision, " // 7
        + " w3fs5s36.appeal_service_codein, " // 8
        + " w3fs5.notice_date, " // 9
        + " w3fs5s36.additional_information " // 10
        + " from w3fs5, w3fs5s36 "
        + " where w3fs5.id = ? and w3fs5.id = w3fs5s36.id";

    List<?> datiW3FS5S36 = sqlManager.getVector(selectW3FS5S36, new Object[] { id });
    if (datiW3FS5S36 != null && datiW3FS5S36.size() > 0) {
      String recurrent_proc = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 0).getValue();
      String recurrent_proc_desc = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 1).getValue();
      if (recurrent_proc != null) {
        if ("1".equals(recurrent_proc)) {
          ciF05.addNewRECURRENTPROCUREMENT();
          if (recurrent_proc_desc != null) {
            ciF05.setESTIMATEDTIMING(getTextFtMultiLines(recurrent_proc_desc));
          }
        } else if ("2".equals(recurrent_proc)) {
          ciF05.addNewNORECURRENTPROCUREMENT();
        }
      }

      String eordering = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 2).getValue();
      if (eordering != null && "1".equals(eordering)) {
        ciF05.addNewEORDERING();
      }

      String einvoicing = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 3).getValue();
      if (einvoicing != null && "1".equals(einvoicing)) {
        ciF05.addNewEINVOICING();
      }

      String epayment = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 4).getValue();
      if (epayment != null && "1".equals(epayment)) {
        ciF05.addNewEPAYMENT();
      }

      String appeal_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 5).getValue();
      if (appeal_procedure_codein != null) {
        ciF05.setADDRESSREVIEWBODY(getContactReview(sqlManager, appeal_procedure_codein));
      }

      String mediation_procedure_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 6).getValue();
      if (mediation_procedure_codein != null) {
        ciF05.setADDRESSMEDIATIONBODY(getContactReview(sqlManager, mediation_procedure_codein));
      }

      String appeal_precision = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 7).getValue();
      if (appeal_precision != null) {
        ciF05.setREVIEWPROCEDURE(getTextFtMultiLines(appeal_precision));
      }

      String appeal_service_codein = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 8).getValue();
      if (appeal_service_codein != null) {
        ciF05.setADDRESSREVIEWINFO(getContactReview(sqlManager, appeal_service_codein));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS5S36, 9).getValue();
      if (notice_date != null) {
        ciF05.setDATEDISPATCHNOTICE(getDate(notice_date));
      }

      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS5S36, 10).getValue();
      if (additional_information != null) {
        ciF05.setINFOADD(getTextFtMultiLines(additional_information));
      }

    }

    return ciF05;

  }

}
