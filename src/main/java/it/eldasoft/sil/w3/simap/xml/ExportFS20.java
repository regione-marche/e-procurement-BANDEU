package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF20;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF20.AWARDEDCONTRACT;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF20.AWARDEDCONTRACT.CONTRACTORS;
import eu.europa.publications.resource.schema.ted.r209.reception.AwardContractF20.AWARDEDCONTRACT.CONTRACTORS.CONTRACTOR;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF20;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF20;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactContractor;
import eu.europa.publications.resource.schema.ted.r209.reception.Country;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.DurationUnitMD;
import eu.europa.publications.resource.schema.ted.r209.reception.F202014Document.F202014;
import eu.europa.publications.resource.schema.ted.r209.reception.ModificationsF20;
import eu.europa.publications.resource.schema.ted.r209.reception.ModificationsF20.DESCRIPTIONPROCUREMENT;
import eu.europa.publications.resource.schema.ted.r209.reception.ModificationsF20.INFOMODIFICATIONS;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF20;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF20;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TCountryList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.x2021.nuts.Nuts;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFS20 extends ExportFormulariStandard {

  public F202014 exportF20(SqlManager sqlManager, Long id) throws GestoreException {

    F202014 f20 = F202014.Factory.newInstance();

    f20.setLG(TCeLanguageList.IT);
    f20.setCATEGORY(OriginalTranslation.ORIGINAL);
    f20.addNewFORM().setStringValue("F20");

    try {

      // Base legale
      String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id });
      if (legal_basis != null) {
        f20.addNewLEGALBASIS().setVALUE(
            eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
      }

      f20.setCONTRACTINGBODY(_getBodyF20(sqlManager, id));
      f20.setOBJECTCONTRACT(_getObjectContractF20(sqlManager, id));

      // Sezione IV: procedura e numero pubblicazione precedente
      List<?> datiW3FS20 = sqlManager.getVector("select notice_number_oj from w3fs20 where id = ?", new Object[] { id });
      if (datiW3FS20 != null && datiW3FS20.size() > 0) {
        String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 0).getValue();
        if (notice_number_oj != null) {
          f20.addNewPROCEDURE().setNOTICENUMBEROJ(notice_number_oj);
        }
      }

      f20.setAWARDCONTRACT(_getAwardContractF20(sqlManager, id));
      f20.setCOMPLEMENTARYINFO(_getCiF20(sqlManager, id));
      f20.setMODIFICATIONSCONTRACT(_getModificationF20(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 20", "exportXML.sqlerror", e);
    }

    return f20;

  }

  /**
   * Sezione I.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private BodyF20 _getBodyF20(SqlManager sqlManager, Long id) throws SQLException {

    BodyF20 bodyF20 = BodyF20.Factory.newInstance();

    String selectW3SIMAP = "select codamm from w3simap where id = ?";

    List<?> datiW3SIMAP = sqlManager.getVector(selectW3SIMAP, new Object[] { id });
    if (datiW3SIMAP != null && datiW3SIMAP.size() > 0) {

      String codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 0).getValue();
      bodyF20.setADDRESSCONTRACTINGBODY(this.getContactContractingBodyW3AMMI(sqlManager, codamm));

    }

    return bodyF20;

  }

  /**
   * Sezione II.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF20 _getObjectContractF20(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF20 objectContractF20 = ObjectContractF20.Factory.newInstance();

    String selectW3FS20 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " cpvsupp1, " // 3
        + " cpvsupp2, " // 4
        + " type_contract " // 5
        + " from w3fs20 "
        + " where w3fs20.id = ?";

    List<?> datiW3FS20 = sqlManager.getVector(selectW3FS20, new Object[] { id });
    if (datiW3FS20 != null && datiW3FS20.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 0).getValue();
      if (title_contract != null) {
        objectContractF20.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 1).getValue();
      if (reference != null) {
        objectContractF20.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 4).getValue();
      if (cpv != null) {
        objectContractF20.addNewCPVMAIN();
        objectContractF20.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF20.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF20.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF20.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF20.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF20.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      objectContractF20.setOBJECTDESCR(_getObjectF20(sqlManager, id, new Long(1)));

    }

    return objectContractF20;
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
  private ObjectF20 _getObjectF20(SqlManager sqlManager, Long id, Long num) throws SQLException {
    ObjectF20 objectF20 = ObjectF20.Factory.newInstance();

    String selectW3ANNEXB = "select title, " // 0
        + " lotnum, " // 1
        + " site_nuts, " // 2
        + " site_nuts_2, " // 3
        + " site_nuts_3, " // 4
        + " site_nuts_4, " // 5
        + " site_label, " // 6
        + " description, " // 7
        + " work_month, " // 8
        + " work_days, " // 9
        + " work_start_date, " // 10
        + " work_end_date, " // 11
        + " justification, " // 12
        + " eu_progr, " // 13
        + " eu_progr_descr " // 14
        + " from w3annexb "
        + " where id = ? and num = ?";

    List<?> datiW3ANNEXB = sqlManager.getVector(selectW3ANNEXB, new Object[] { id, num });
    if (datiW3ANNEXB != null && datiW3ANNEXB.size() > 0) {

      // Titolo
      String title = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 0).getValue();
      if (title != null) {
        objectF20.setTITLE(getTextFtSingleLine(title));
      }

      // Numero del lotto
      Long lotnum = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 1).getValue();
      if (lotnum != null) {
        objectF20.setLOTNO(lotnum.toString());
      }

      // CPV aggiuntivi
      String selectW3CPV = "select cpv, cpvsupp1, cpvsupp2 from w3cpv where id = ? and num = ? and ent = 'W3ANNEXB'";
      List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { id, num });
      if (datiW3CPV != null && datiW3CPV.size() > 0) {
        for (int c = 0; c < datiW3CPV.size(); c++) {
          String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 0).getValue();
          String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 1).getValue();
          String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 2).getValue();
          if (cpv != null) {
            CpvSet cpvSet = objectF20.addNewCPVADDITIONAL();
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
        objectF20.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF20.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF20.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF20.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 6).getValue();
      if (site_label != null) {
        objectF20.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 7).getValue();
      if (description != null) {
        objectF20.setSHORTDESCR(getTextFtMultiLines(description));
      }

      // Durata del contratto
      Long work_month = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 8).getValue();
      Long work_days = (Long) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 9).getValue();
      Date work_start_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 10).getValue();
      Date work_end_date = (Date) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 11).getValue();
      if (work_month != null || work_days != null) {
        if (work_month != null) {
          objectF20.addNewDURATION().setTYPE(DurationUnitMD.MONTH);
          objectF20.getDURATION().setStringValue(work_month.toString());
        } else if (work_days != null) {
          objectF20.addNewDURATION().setTYPE(DurationUnitMD.DAY);
          objectF20.getDURATION().setStringValue(work_days.toString());
        }
      }
      if (work_start_date != null) {
        objectF20.setDATESTART(getDate(work_start_date));
      }
      if (work_end_date != null) {
        objectF20.setDATEEND(getDate(work_end_date));
      }

      // Giustificazione
      String justification = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 12).getValue();
      if (justification != null) {
        objectF20.setJUSTIFICATION(getTextFtMultiLines(justification));
      }

      // Fondi europei
      String eu_progr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 13).getValue();
      String eu_progr_descr = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 14).getValue();
      if (eu_progr != null) {
        if ("1".equals(eu_progr) && eu_progr_descr != null) {
          eu_progr_descr = conversioneCaratteriXML(eu_progr_descr);
          objectF20.setEUPROGRRELATED(getTextFtMultiLines(eu_progr_descr));
        } else {
          objectF20.addNewNOEUPROGRRELATED();
        }
      }

    }

    return objectF20;
  }

  /**
   * Aggiundicazione dell'appalto.
   * 
   * @param sqlManager
   * @param id
   * @param item
   * @param item_simap
   * @return
   * @throws SQLException
   */
  private AwardContractF20 _getAwardContractF20(SqlManager sqlManager, Long id) throws SQLException {
    AwardContractF20 awardContractF20 = AwardContractF20.Factory.newInstance();

    String selectW3FS20 = "select contract_number, " // 0
        + " lot_number, " // 1
        + " contract_title, " // 2
        + " contract_award_date, " // 3
        + " awarded_group, " // 4
        + " val_total " // 5
        + " from w3fs20 "
        + " where id = ?";

    List<?> datiW3FS20 = sqlManager.getVector(selectW3FS20, new Object[] { id });
    if (datiW3FS20 != null && datiW3FS20.size() > 0) {

      // Numero contratto
      String contract_number = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 0).getValue();
      if (contract_number != null) {
        contract_number = conversioneCaratteriXML(contract_number);
        awardContractF20.setCONTRACTNO(contract_number);
      }

      // Numero lotto
      Long lot_number = (Long) SqlManager.getValueFromVectorParam(datiW3FS20, 1).getValue();
      if (lot_number != null) {
        awardContractF20.setLOTNO(lot_number.toString());
      }

      // Titolo
      String contract_title = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 2).getValue();
      if (contract_title != null) {
        awardContractF20.setTITLE(getTextFtSingleLine(contract_title));
      }

      // Data di conclusione del contratto d'appalto
      Date date_conclusion_contract = (Date) SqlManager.getValueFromVectorParam(datiW3FS20, 3).getValue();
      String awarded_group = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 4).getValue();
      Double val_total = (Double) SqlManager.getValueFromVectorParam(datiW3FS20, 5).getValue();
      if (date_conclusion_contract != null || awarded_group != null || val_total != null) {
        AWARDEDCONTRACT aw = awardContractF20.addNewAWARDEDCONTRACT();

        if (date_conclusion_contract != null) {
          aw.setDATECONCLUSIONCONTRACT(getDate(date_conclusion_contract));
        }

        if (val_total != null) {
          aw.addNewVALUES().addNewVALTOTAL().setBigDecimalValue(getCost(val_total));
          aw.getVALUES().getVALTOTAL().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
        }

        if (awarded_group != null) {

          CONTRACTORS contractors = aw.addNewCONTRACTORS();

          if ("1".equals(awarded_group)) {
            contractors.addNewAWARDEDTOGROUP();
          } else if ("2".equals(awarded_group)) {
            contractors.addNewNOAWARDEDTOGROUP();
          }

          // Contraenti
          String selectW3FS20AWARD_C = "select contractor_codimp, contractor_sme from w3fs20award_c where id = ? and item = ? order by num";
          List<?> datiW3FS20AWARD_C = sqlManager.getListVector(selectW3FS20AWARD_C, new Object[] { id, new Long(1) });
          if (datiW3FS20AWARD_C != null && datiW3FS20AWARD_C.size() > 0) {
            for (int c = 0; c < datiW3FS20AWARD_C.size(); c++) {

              CONTRACTOR contractor = contractors.addNewCONTRACTOR();
              String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_C.get(c), 0).getValue();
              String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_C.get(c), 1).getValue();

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
        }
      }
    }

    return awardContractF20;
  }

  /**
   * Sezione VI.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF20 _getCiF20(SqlManager sqlManager, Long id) throws SQLException {
    CiF20 ciF20 = CiF20.Factory.newInstance();

    String selectW3FS20 = "select additional_information, " // 0
        + " address_review_body, " // 1
        + " address_mediation_body, " // 2
        + " review_procedure, " // 3
        + " address_review_info, " // 4
        + " notice_date " // 5
        + " from w3fs20 "
        + " where w3fs20.id = ?";

    List<?> datiW3FS20 = sqlManager.getVector(selectW3FS20, new Object[] { id });
    if (datiW3FS20 != null && datiW3FS20.size() > 0) {

      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 0).getValue();
      if (additional_information != null) {
        ciF20.setINFOADD(getTextFtMultiLines(additional_information));
      }

      String address_review_body = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 1).getValue();
      if (address_review_body != null) {
        ciF20.setADDRESSREVIEWBODY(getContactReview(sqlManager, address_review_body));
      }

      String address_mediation_body = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 2).getValue();
      if (address_mediation_body != null) {
        ciF20.setADDRESSMEDIATIONBODY(getContactReview(sqlManager, address_mediation_body));
      }

      String review_procedure = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 3).getValue();
      if (review_procedure != null) {
        ciF20.setREVIEWPROCEDURE(getTextFtMultiLines(review_procedure));
      }

      String address_review_info = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 4).getValue();
      if (address_review_info != null) {
        ciF20.setADDRESSREVIEWBODY(getContactReview(sqlManager, address_review_info));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS20, 5).getValue();
      if (notice_date != null) {
        ciF20.setDATEDISPATCHNOTICE(getDate(notice_date));
      }

    }

    return ciF20;

  }

  /**
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

  /**
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ModificationsF20 _getModificationF20(SqlManager sqlManager, Long id) throws SQLException {

    ModificationsF20 modF20 = ModificationsF20.Factory.newInstance();

    String selectW3FS20 = "select m_cpv, " // 0
        + " m_cpvsupp1, " // 1
        + " m_cpvsupp2, " // 2
        + " m_site_nuts, " // 3
        + " m_site_nuts_2, " // 4
        + " m_site_nuts_3, " // 5
        + " m_site_nuts_4, " // 6
        + " m_site_label, " // 7
        + " m_description, " // 8
        + " m_duration_month, " // 9
        + " m_duration_day, " // 10
        + " m_date_start, " // 11
        + " m_date_stop, " // 12
        + " m_justification, " // 13
        + " m_val_total, " // 14
        + " m_awarded_group, " // 15
        + " m_short_descr, " // 16
        + " m_additional_need, " // 17
        + " m_unforessen, " // 18
        + " m_val_total_before, " // 19
        + " m_val_total_after " // 20
        + " from w3fs20 where id = ?";

    List<?> datiW3FS20 = sqlManager.getVector(selectW3FS20, new Object[] { id });
    if (datiW3FS20 != null && datiW3FS20.size() > 0) {

      DESCRIPTIONPROCUREMENT descrProc = modF20.addNewDESCRIPTIONPROCUREMENT();

      // CPV del vocabolario principale e di quello supplementare
      String m_cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 0).getValue();
      String m_cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 1).getValue();
      String m_cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 2).getValue();
      if (m_cpv != null) {
        descrProc.addNewCPVMAIN();
        descrProc.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(m_cpv));
        if (m_cpvsupp1 != null) {
          descrProc.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(m_cpvsupp1));
        }
        if (m_cpvsupp2 != null) {
          descrProc.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(m_cpvsupp2));
        }
      }

      // CPV aggiuntivi
      String selectW3CPV = "select cpv, cpvsupp1, cpvsupp2 from w3cpv where id = ? and num = ? and ent = 'W3FS20'";
      List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { id, new Long(1) });
      if (datiW3CPV != null && datiW3CPV.size() > 0) {
        for (int c = 0; c < datiW3CPV.size(); c++) {
          String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 0).getValue();
          String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 1).getValue();
          String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(c), 2).getValue();
          if (cpv != null) {
            CpvSet cpvSet = descrProc.addNewCPVADDITIONAL();
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
      String m_site_nuts = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 3).getValue();
      String m_site_nuts_2 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 4).getValue();
      String m_site_nuts_3 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 5).getValue();
      String m_site_nuts_4 = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 6).getValue();
      if (m_site_nuts != null) {
        descrProc.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(m_site_nuts));
      }
      if (m_site_nuts_2 != null) {
        descrProc.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(m_site_nuts_2));
      }
      if (m_site_nuts_3 != null) {
        descrProc.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(m_site_nuts_3));
      }
      if (m_site_nuts_4 != null) {
        descrProc.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(m_site_nuts_4));
      }

      // Luogo principale
      String m_site_label = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 7).getValue();
      if (m_site_label != null) {
        descrProc.setMAINSITE(getTextFtMultiLines(m_site_label));
      }

      // Descrizione
      String m_description = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 8).getValue();
      if (m_description != null) {
        descrProc.setSHORTDESCR(getTextFtMultiLines(m_description));
      }

      // Durata del contratto
      Long m_duration_month = (Long) SqlManager.getValueFromVectorParam(datiW3FS20, 9).getValue();
      Long m_duration_day = (Long) SqlManager.getValueFromVectorParam(datiW3FS20, 10).getValue();
      Date m_date_start = (Date) SqlManager.getValueFromVectorParam(datiW3FS20, 11).getValue();
      Date m_date_stop = (Date) SqlManager.getValueFromVectorParam(datiW3FS20, 12).getValue();
      if (m_duration_month != null || m_duration_day != null) {
        if (m_duration_month != null) {
          descrProc.addNewDURATION().setTYPE(DurationUnitMD.MONTH);
          descrProc.getDURATION().setStringValue(m_duration_month.toString());
        } else if (m_duration_day != null) {
          descrProc.addNewDURATION().setTYPE(DurationUnitMD.DAY);
          descrProc.getDURATION().setStringValue(m_duration_day.toString());
        }
      }
      if (m_date_start != null) {
        descrProc.setDATESTART(getDate(m_date_start));
      }
      if (m_date_stop != null) {
        descrProc.setDATEEND(getDate(m_date_stop));
      }

      // Giustificazione
      String m_justification = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 13).getValue();
      if (m_justification != null) {
        descrProc.setJUSTIFICATION(getTextFtMultiLines(m_justification));
      }

      // Valore totale
      Double m_val_total = (Double) SqlManager.getValueFromVectorParam(datiW3FS20, 14).getValue();
      if (m_val_total != null) {
        descrProc.addNewVALUES().addNewVALTOTAL().setBigDecimalValue(getCost(m_val_total));
        descrProc.getVALUES().getVALTOTAL().setCURRENCY(
            eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
      }

      // L'appalto e' stato aggiudicato ad un raggruppamento di operatori
      // economici
      String awarded_group = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 15).getValue();
      if (awarded_group != null) {

        eu.europa.publications.resource.schema.ted.r209.reception.ModificationsF20.DESCRIPTIONPROCUREMENT.CONTRACTORS contractors = descrProc.addNewCONTRACTORS();

        if ("1".equals(awarded_group)) {
          contractors.addNewAWARDEDTOGROUP();
        } else if ("2".equals(awarded_group)) {
          contractors.addNewNOAWARDEDTOGROUP();
        }

        // Contraenti
        String selectW3FS20AWARD_M = "select contractor_codimp, contractor_sme from w3fs20award_m where id = ? and item = ? order by num";
        List<?> datiW3FS20AWARD_M = sqlManager.getListVector(selectW3FS20AWARD_M, new Object[] { id, new Long(1) });
        if (datiW3FS20AWARD_M != null && datiW3FS20AWARD_M.size() > 0) {
          for (int c = 0; c < datiW3FS20AWARD_M.size(); c++) {

            eu.europa.publications.resource.schema.ted.r209.reception.ModificationsF20.DESCRIPTIONPROCUREMENT.CONTRACTORS.CONTRACTOR contractor = contractors.addNewCONTRACTOR();
            String contractor_codimp = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_M.get(c), 0).getValue();
            String contractor_sme = (String) SqlManager.getValueFromVectorParam(datiW3FS20AWARD_M.get(c), 1).getValue();

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

      }

      INFOMODIFICATIONS infoMod = modF20.addNewINFOMODIFICATIONS();
      String m_short_descr = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 16).getValue();
      String m_additional_need = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 17).getValue();
      String m_unforeseen = (String) SqlManager.getValueFromVectorParam(datiW3FS20, 18).getValue();
      Double m_val_total_before = (Double) SqlManager.getValueFromVectorParam(datiW3FS20, 19).getValue();
      Double m_val_total_after = (Double) SqlManager.getValueFromVectorParam(datiW3FS20, 20).getValue();

      if (m_short_descr != null) {
        infoMod.setSHORTDESCR(getTextFtMultiLines(m_short_descr));
      }

      if (m_additional_need != null) {
        infoMod.setADDITIONALNEED(getTextFtMultiLines(m_additional_need));
      }

      if (m_unforeseen != null) {
        infoMod.setUNFORESEENCIRCUMSTANCE(getTextFtMultiLines(m_unforeseen));
      }

      if (m_val_total_before != null || m_val_total_after != null) {
        infoMod.addNewVALUES();

        if (m_val_total_before != null) {
          infoMod.getVALUES().addNewVALTOTALBEFORE().setBigDecimalValue(getCost(m_val_total_before));
          infoMod.getVALUES().getVALTOTALBEFORE().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
        }

        if (m_val_total_after != null) {
          infoMod.getVALUES().addNewVALTOTALAFTER().setBigDecimalValue(getCost(m_val_total_after));
          infoMod.getVALUES().getVALTOTALAFTER().setCURRENCY(
              eu.europa.publications.resource.authority.currency.TCurrencyTedschema.Enum.forString("EUR"));
        }

      }

    }

    return modF20;

  }

}
