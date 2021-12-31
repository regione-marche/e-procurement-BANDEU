package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.BodyF01;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF05;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF08;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF08;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.F082014Document.F082014;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF08;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectF08;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFS8 extends ExportFormulariStandard {

  public F082014 exportF08(SqlManager sqlManager, Long id) throws GestoreException {

    F082014 f08 = F082014.Factory.newInstance();
    f08.setLG(TCeLanguageList.IT);
    f08.setCATEGORY(OriginalTranslation.ORIGINAL);
    f08.addNewFORM().setStringValue("F08");

    try {
      // Direttiva
      // 3 - Avviso di preinformazione (Direttiva 2014/24/UE)
      // 4 - Avviso periodico indicativo (Direttiva 2014/25/UE)
      String notice_relation = (String) sqlManager.getObject("select notice_relation from w3fs8 where id = ?", new Object[] { id });
      if (notice_relation != null) {
        if ("3".equals(notice_relation)) {
          f08.addNewLEGALBASIS().setVALUE(
              eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString("32014L0024"));
          f08.setCONTRACTINGBODY(this._getBodyF08CA(sqlManager, id));

        } else if ("4".equals(notice_relation)) {
          f08.addNewLEGALBASIS().setVALUE(
              eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString("32014L0025"));
          f08.setCONTRACTINGBODY(this._getBodyF08CE(sqlManager, id));
        }
      }

      f08.setOBJECTCONTRACT(_getObjectContractF08(sqlManager, id));
      f08.setCOMPLEMENTARYINFO(_getCiF08(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 8", "exportXML.sqlerror", e);
    }

    return f08;
  }

  private BodyF08 _getBodyF08CA(SqlManager sqlManager, Long id) throws SQLException {
    BodyF01 bodyF01 = getBodyF01CAGeneric(sqlManager, id);
    BodyF08 bodyF08 = (BodyF08) bodyF01.changeType(BodyF08.type);
    return bodyF08;
  }

  private BodyF08 _getBodyF08CE(SqlManager sqlManager, Long id) throws SQLException {
    BodyF05 bodyF05 = getBodyF05CEGeneric(sqlManager, id);
    BodyF08 bodyF08 = (BodyF08) bodyF05.changeType(BodyF08.type);
    return bodyF08;
  }

  /**
   * Sezione II
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF08 _getObjectContractF08(SqlManager sqlManager, Long id) throws SQLException {
    ObjectContractF08 objectContractF08 = ObjectContractF08.Factory.newInstance();

    String selectW3FS8S2 = "select title_contract, " // 0
        + " reference, " // 1
        + " cpv, " // 2
        + " cpvsupp1, " // 3
        + " cpvsupp2, " // 4
        + " type_contract " // 5
        + " from w3fs8s2 "
        + " where id = ? and num = 1";

    List<?> datiW3FS2S8 = sqlManager.getVector(selectW3FS8S2, new Object[] { id });
    if (datiW3FS2S8 != null && datiW3FS2S8.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS2S8, 0).getValue();
      if (title_contract != null) {
        objectContractF08.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS2S8, 1).getValue();
      if (reference != null) {
        objectContractF08.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS2S8, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS2S8, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS2S8, 4).getValue();
      if (cpv != null) {
        objectContractF08.addNewCPVMAIN();
        objectContractF08.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF08.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF08.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS2S8, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF08.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF08.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF08.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      objectContractF08.setOBJECTDESCR(_getObjectF08(sqlManager, id, new Long(1)));

    }

    return objectContractF08;
  }

  /**
   * Sezione II.2
   * 
   * @param sqlManager
   * @param id
   * @param num
   * @return
   * @throws SQLException
   */
  private ObjectF08 _getObjectF08(SqlManager sqlManager, Long id, Long num) throws SQLException {

    ObjectF08 objectF08 = ObjectF08.Factory.newInstance();

    String selectW3ANNEXB = "select site_nuts, " // 0
        + " site_nuts_2, " // 1
        + " site_nuts_3, " // 2
        + " site_nuts_4, " // 3
        + " site_label, " // 4
        + " description " // 5
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
            CpvSet cpvSet = objectF08.addNewCPVADDITIONAL();
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
        objectF08.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts));
      }
      if (site_nuts_2 != null) {
        objectF08.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_2));
      }
      if (site_nuts_3 != null) {
        objectF08.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_3));
      }
      if (site_nuts_4 != null) {
        objectF08.addNewNUTS().setCODE(TNutsCodeList.Enum.forString(site_nuts_4));
      }

      // Luogo principale
      String site_label = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 4).getValue();
      if (site_label != null) {
        objectF08.setMAINSITE(getTextFtMultiLines(site_label));
      }

      // Descrizione
      String description = (String) SqlManager.getValueFromVectorParam(datiW3ANNEXB, 5).getValue();
      if (description != null) {
        objectF08.setSHORTDESCR(getTextFtMultiLines(description));
      }
    }

    return objectF08;
  }

  /**
   * Sezione VI
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF08 _getCiF08(SqlManager sqlManager, Long id) throws SQLException {
    CiF08 ciF08 = CiF08.Factory.newInstance();

    String selectW3FS8 = "select additional_information, " // 0
        + " notice_date " // 1
        + " from w3fs8 "
        + " where w3fs8.id = ?";

    List<?> datiW3FS8 = sqlManager.getVector(selectW3FS8, new Object[] { id });
    if (datiW3FS8 != null && datiW3FS8.size() > 0) {
      String additional_information = (String) SqlManager.getValueFromVectorParam(datiW3FS8, 0).getValue();
      if (additional_information != null) {
        ciF08.setINFOADD(getTextFtMultiLines(additional_information));
      }

      Date notice_date = (Date) SqlManager.getValueFromVectorParam(datiW3FS8, 1).getValue();
      if (notice_date != null) {
        ciF08.setDATEDISPATCHNOTICE(getDate(notice_date));
      }
    }

    return ciF08;
  }

}
