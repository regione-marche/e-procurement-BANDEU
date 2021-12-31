package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.properties.ConfigManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import eu.europa.publications.resource.schema.ted.r209.reception.BodyF14;
import eu.europa.publications.resource.schema.ted.r209.reception.ChangesF14;
import eu.europa.publications.resource.schema.ted.r209.reception.ChangesF14.CHANGE;
import eu.europa.publications.resource.schema.ted.r209.reception.ChangesF14.CHANGE.NEWVALUE;
import eu.europa.publications.resource.schema.ted.r209.reception.ChangesF14.CHANGE.OLDVALUE;
import eu.europa.publications.resource.schema.ted.r209.reception.CiF14;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactAddContractingBodyF14;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactContractingBody;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactContractingBodyF14;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSet;
import eu.europa.publications.resource.schema.ted.r209.reception.F142014Document.F142014;
import eu.europa.publications.resource.schema.ted.r209.reception.ObjectContractF14;
import eu.europa.publications.resource.schema.ted.r209.reception.OriginalTranslation;
import eu.europa.publications.resource.schema.ted.r209.reception.TCeLanguageList;
import eu.europa.publications.resource.schema.ted.r209.reception.TypeContract.CTYPE;

public class ExportFS14 extends ExportFormulariStandard {

  protected static final String PROP_SIMAP_ESENDER_LOGIN = "it.eldasoft.simap.esenderlogin";

  public F142014 exportF14(SqlManager sqlManager, Long id) throws GestoreException {

    F142014 f14 = F142014.Factory.newInstance();
    f14.setLG(TCeLanguageList.IT);
    f14.setCATEGORY(OriginalTranslation.ORIGINAL);
    f14.addNewFORM().setStringValue("F14");

    try {
      Long id_rif = (Long) sqlManager.getObject("select id_rif from w3fs14 where id = ?", new Object[] { id });
      String form_rif = (String) sqlManager.getObject("select form_rif from w3fs14 where id = ?", new Object[] { id });

      // Base legale
      if ("fs8".equals(form_rif)) {
        String notice_relation = (String) sqlManager.getObject("select notice_relation from w3fs8 where id = ?", new Object[] { id_rif });
        if (notice_relation != null) {
          if ("3".equals(notice_relation)) {
            f14.addNewLEGALBASIS().setVALUE(
                eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString("32014L0024"));
          } else if ("4".equals(notice_relation)) {
            f14.addNewLEGALBASIS().setVALUE(
                eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString("32014L0025"));
          }
        }
      } else {
        String legal_basis = (String) sqlManager.getObject("select legal_basis from w3simap where id = ?", new Object[] { id_rif });
        if (legal_basis != null) {
          f14.addNewLEGALBASIS().setVALUE(
              eu.europa.publications.resource.authority.legalBasis.TLegalBasisTedschema.Enum.forString(legal_basis));
        }
      }

      f14.setCONTRACTINGBODY(_getBodyF14(sqlManager, id_rif));
      f14.setOBJECTCONTRACT(_getObjectContractF14(sqlManager, form_rif, id_rif));
      f14.setCOMPLEMENTARYINFO(_getciF14(sqlManager, id, form_rif, id_rif));
      f14.setCHANGES(_getChangesF14(sqlManager, id));

    } catch (SQLException e) {
      throw new GestoreException("Errore nell'esportazione dei dati per il formulario standard 14", "exportXML.sqlerror", e);
    }
    return f14;
  }

  /**
   * Sezione I.
   * 
   * @param sqlManager
   * @param id_rif
   * @return
   * @throws SQLException
   */
  private BodyF14 _getBodyF14(SqlManager sqlManager, Long id_rif) throws SQLException {
    BodyF14 bodyF14 = BodyF14.Factory.newInstance();

    // Denominazione ed indirizzo amministrazione aggiudicatrice
    String w3simap_codamm = (String) sqlManager.getObject("select codamm from w3simap where id = ?", new Object[] { id_rif });

    ContactContractingBody contactBody = this.getContactContractingBodyW3AMMI(sqlManager, w3simap_codamm);
    ContactContractingBodyF14 contactBodyF14 = (ContactContractingBodyF14) contactBody.changeType(ContactContractingBodyF14.type);
    bodyF14.setADDRESSCONTRACTINGBODY(contactBodyF14);

    // Denominazione ed indirizzo delle altre amministrazione aggiudicatrici
    // congiunte
    List<?> datiW3SIMAP_ADDR = sqlManager.getListVector("select codamm from w3simap_addr where id = ? order by num",
        new Object[] { id_rif });
    if (datiW3SIMAP_ADDR != null && datiW3SIMAP_ADDR.size() > 0) {
      for (int isa = 0; isa < datiW3SIMAP_ADDR.size(); isa++) {
        String w3simap_addr_codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP_ADDR.get(isa), 0).getValue();
        bodyF14.addNewADDRESSCONTRACTINGBODYADDITIONAL();
        ContactContractingBody contactBodyAdditional = this.getContactContractingBodyW3AMMI(sqlManager, w3simap_addr_codamm);
        ContactAddContractingBodyF14 contactAddBodyF14 = (ContactAddContractingBodyF14) contactBodyAdditional.changeType(ContactAddContractingBodyF14.type);
        bodyF14.setADDRESSCONTRACTINGBODYADDITIONALArray(isa, contactAddBodyF14);
      }
    }

    return bodyF14;
  }

  /**
   * Sezione II
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ObjectContractF14 _getObjectContractF14(SqlManager sqlManager, String form_rif, Long id_rif) throws SQLException {
    ObjectContractF14 objectContractF14 = ObjectContractF14.Factory.newInstance();

    String selectW3FS = "";
    if ("FS1".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " scope_total " // 6
          + " from w3fs1s2 "
          + " where id = ?";
    } else if ("FS2".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " scope_total " // 6
          + " from w3fs2 "
          + " where w3fs2.id = ?";
    } else if ("FS3".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " short_contract_description " // 6
          + " from w3fs3 "
          + " where w3fs3.id = ?";
    } else if ("FS4".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " title_contract " // 6
          + " from w3fs4s2 "
          + " where id = ?";
    } else if ("FS5".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " title_contract " // 6
          + " from w3fs5 "
          + " where w3fs5.id = ?";
    } else if ("FS6".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " title_contract " // 6
          + " from w3fs6 "
          + " where w3fs6.id = ?";
    } else if ("FS7".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " title_contract " // 6
          + " from w3fs7 "
          + " where w3fs7.id = ?";
    } else if ("FS20".equals(form_rif)) {
      selectW3FS = "select title_contract, " // 0
          + " reference, " // 1
          + " cpv, " // 2
          + " cpvsupp1, " // 3
          + " cpvsupp2, " // 4
          + " type_contract, " // 5
          + " title_contract " // 6
          + " from w3fs20 "
          + " where w3fs20.id = ?";
    } else if ("FS8".equals(form_rif)) {
      selectW3FS = "select w3fs8s2.title_contract, " // 0
          + " w3fs8s2.reference, " // 1
          + " w3fs8s2.cpv, " // 2
          + " w3fs8s2.cpvsupp1, " // 3
          + " w3fs8s2.cpvsupp2, " // 4
          + " w3fs8s2.type_contract, " // 5
          + " w3annexb.description " // 6
          + " from w3fs8s2, w3annexb "
          + " where w3fs8s2.id = ? and w3fs8s2.num = 1 and w3fs2s8.id = w3annexb.id and w3annexb.num = 1";
    }

    List<?> datiW3FS = sqlManager.getVector(selectW3FS, new Object[] { id_rif });
    if (datiW3FS != null && datiW3FS.size() > 0) {

      // Titolo del progetto
      String title_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS, 0).getValue();
      if (title_contract != null) {
        objectContractF14.setTITLE(getTextFtSingleLine(title_contract));
      }

      // Riferimento
      String reference = (String) SqlManager.getValueFromVectorParam(datiW3FS, 1).getValue();
      if (reference != null) {
        objectContractF14.setREFERENCENUMBER(reference);
      }

      // CPV del vocabolario principale e di quello supplementare
      String cpv = (String) SqlManager.getValueFromVectorParam(datiW3FS, 2).getValue();
      String cpvsupp1 = (String) SqlManager.getValueFromVectorParam(datiW3FS, 3).getValue();
      String cpvsupp2 = (String) SqlManager.getValueFromVectorParam(datiW3FS, 4).getValue();
      if (cpv != null) {
        objectContractF14.addNewCPVMAIN();
        objectContractF14.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv));
        if (cpvsupp1 != null) {
          objectContractF14.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1));
        }
        if (cpvsupp2 != null) {
          objectContractF14.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2));
        }
      }

      // Tipo di contratto/appalto
      String type_contract = (String) SqlManager.getValueFromVectorParam(datiW3FS, 5).getValue();
      if (type_contract != null) {
        if ("SERV".equals(type_contract)) {
          objectContractF14.addNewTYPECONTRACT().setCTYPE(CTYPE.SERVICES);
        } else if ("SUPP".equals(type_contract)) {
          objectContractF14.addNewTYPECONTRACT().setCTYPE(CTYPE.SUPPLIES);
        } else if ("WORK".equals(type_contract)) {
          objectContractF14.addNewTYPECONTRACT().setCTYPE(CTYPE.WORKS);
        }
      }

      // Descrizione
      String scope_total = (String) SqlManager.getValueFromVectorParam(datiW3FS, 6).getValue();
      if (scope_total != null) {
        objectContractF14.setSHORTDESCR(getTextFtMultiLines(scope_total));
      }

    }

    return objectContractF14;
  }

  /**
   * Sezione VI.
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private CiF14 _getciF14(SqlManager sqlManager, Long id, String form_rif, Long id_rif) throws SQLException {
    CiF14 ciF14 = CiF14.Factory.newInstance();

    // Invio effettuato da Ted Esender
    ciF14.addNewORIGINALTEDESENDER().addNewPUBLICATION().setStringValue("NO");

    // Esender Login
    ciF14.addNewESENDERLOGIN().setStringValue(ConfigManager.getValore(PROP_SIMAP_ESENDER_LOGIN));
    ciF14.getESENDERLOGIN().addNewPUBLICATION().setStringValue("NO");

    // Customer login
    String codamm_rif = (String) sqlManager.getObject("select codamm from w3simap where id = ?", new Object[] { id_rif });
    if (codamm_rif != null) {
      String customer_login = (String) sqlManager.getObject("select customer_login from w3ammi where codamm = ?",
          new Object[] { codamm_rif });
      if (customer_login != null) {
        ciF14.addNewCUSTOMERLOGIN().setStringValue(customer_login);
        ciF14.getCUSTOMERLOGIN().addNewPUBLICATION().setStringValue("NO");
      }
    }

    // No_Doc_Ext
    String no_doc_ext = (String) sqlManager.getObject("select no_doc_ext from w3simap where id = ?", new Object[] { id_rif });
    if (no_doc_ext != null) {
      ciF14.addNewNODOCEXT().setStringValue(no_doc_ext);
      ciF14.getNODOCEXT().addNewPUBLICATION().setStringValue("NO");
    }

    // Notice number OJ
    String notice_number_oj = (String) sqlManager.getObject("select notice_number_oj from w3simap where id = ?", new Object[] { id_rif });
    if (notice_number_oj != null) ciF14.setNOTICENUMBEROJ(notice_number_oj);

    // Date dispatch original
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
    } else if ("FS20".equals(form_rif)) {
      selectNoticeDate = "select notice_date from w3fs20 where id = ?";      
    } else if ("FS8".equals(form_rif)) {
      selectNoticeDate = "select notice_date from w3fs8 where id = ?";
    }
    Date notice_date_original = (Date) sqlManager.getObject(selectNoticeDate, new Object[] { id_rif });
    if (notice_date_original != null) {
      ciF14.addNewDATEDISPATCHORIGINAL().setCalendarValue(getDate(notice_date_original));
      ciF14.getDATEDISPATCHORIGINAL().addNewPUBLICATION().setStringValue("NO");
    }

    Date notice_date = (Date) sqlManager.getObject("select notice_date from w3fs14 where id = ?", new Object[] { id });
    if (notice_date != null) {
      ciF14.setDATEDISPATCHNOTICE(getDate(notice_date));
    }

    return ciF14;
  }

  /**
   * Sezion VII
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private ChangesF14 _getChangesF14(SqlManager sqlManager, Long id) throws SQLException {
    ChangesF14 changesF14 = ChangesF14.Factory.newInstance();

    String selectW3FS14 = "select correct_reason, " // 0
        + " other_information " // 1
        + " from w3fs14 "
        + " where id = ?";

    List<?> datiW3FS14 = sqlManager.getVector(selectW3FS14, new Object[] { id });
    if (datiW3FS14 != null && datiW3FS14.size() > 0) {
      // 1 - Modifica delle informazioni originali fornite dall'amministrazione
      // aggiudicatrice
      // 2 - Pubblicazione su TED non conforme alle informazioni fornite
      // dall'amministrazione
      String correct_reason = (String) SqlManager.getValueFromVectorParam(datiW3FS14, 0).getValue();
      if (correct_reason != null) {
        if ("1".equals(correct_reason)) {
          changesF14.addNewMODIFICATIONORIGINAL();
          changesF14.getMODIFICATIONORIGINAL().addNewPUBLICATION().setStringValue("NO");
        } else if ("2".equals(correct_reason)) {
          changesF14.addNewPUBLICATIONTED();
          changesF14.getPUBLICATIONTED().addNewPUBLICATION().setStringValue("NO");
        }
      }

      // Correzioni
      this._getChange(changesF14, sqlManager, id);

      // Altre informazioni
      String other_information = (String) SqlManager.getValueFromVectorParam(datiW3FS14, 1).getValue();
      if (other_information != null) {
        changesF14.setINFOADD(getTextFtMultiLines(other_information));
      }

    }

    return changesF14;
  }

  /**
   * Sezione VII.2.2
   * 
   * @param changesF14
   * @param sqlManager
   * @param id
   * @throws SQLException
   */
  private void _getChange(ChangesF14 changesF14, SqlManager sqlManager, Long id) throws SQLException {
    String selectW3FS14CORR = "select type, " // 0
        + " object, " // 1
        + " section, " // 2
        + " lot_no, " // 3
        + " label_text, " // 4
        + " text_old, " // 5
        + " text_new, " // 6
        + " date_old, " // 7
        + " date_new, " // 8
        + " time_old, " // 9
        + " time_new, " // 10
        + " nuts_old, " // 11
        + " nuts_new, " // 12
        + " cpv_old, " // 13
        + " cpv_new, " // 14
        + " cpvsupp1_old, " // 15
        + " cpvsupp1_new, " // 16
        + " cpvsupp2_old, " // 17
        + " cpvsupp2_new " // 18
        + " from w3fs14corr "
        + " where id = ? order by num";
    List<?> datiW3FS14CORR = sqlManager.getListVector(selectW3FS14CORR, new Object[] { id });
    if (datiW3FS14CORR != null && datiW3FS14CORR.size() > 0) {
      for (int c = 0; c < datiW3FS14CORR.size(); c++) {
        String type = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 0).getValue();
        String object = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 1).getValue();
        String section = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 2).getValue();
        String lot_no = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 3).getValue();
        String label_text = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 4).getValue();
        String text_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 5).getValue();
        String text_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 6).getValue();
        Date date_old = (Date) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 7).getValue();
        Date date_new = (Date) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 8).getValue();
        String time_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 9).getValue();
        String time_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 10).getValue();
        String cpv_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 13).getValue();
        String cpv_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 14).getValue();
        String cpvsupp1_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 15).getValue();
        String cpvsupp1_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 16).getValue();
        String cpvsupp2_old = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 17).getValue();
        String cpvsupp2_new = (String) SqlManager.getValueFromVectorParam(datiW3FS14CORR.get(c), 18).getValue();

        CHANGE change = changesF14.addNewCHANGE();

        change.addNewWHERE();

        if (section != null) change.getWHERE().setSECTION(section);
        if (lot_no != null) change.getWHERE().setLOTNO(lot_no);
        if (label_text != null) change.getWHERE().setLABEL(label_text);

        OLDVALUE o = change.addNewOLDVALUE();
        NEWVALUE n = change.addNewNEWVALUE();

        if ("CPV".equals(object)) {
          this._getChangeCPV(type, cpv_old, cpv_new, cpvsupp1_old, cpvsupp1_new, cpvsupp2_old, cpvsupp2_new, o, n);
        } else if ("CPVA".equals(object)) {
          this._getChangeCPVADDITIONAL(type, cpv_old, cpv_new, cpvsupp1_old, cpvsupp1_new, cpvsupp2_old, cpvsupp2_new, o, n);
        } else if ("TEXT".equals(object)) {
          this._getChangeText(type, text_old, text_new, o, n);
        } else if ("DATE".equals(object)) {
          this._getChangeDateTime(type, date_old, date_new, time_old, time_new, o, n);
        }

      }
    }
  }

  /**
   * 
   * @param type
   * @param date_old
   * @param date_new
   * @param time_old
   * @param time_new
   * @param o
   * @param n
   */
  private void _getChangeDateTime(String type, Date date_old, Date date_new, String time_old, String time_new, OLDVALUE o, NEWVALUE n) {
    if ("ADD".equals(type)) {
      o.addNewNOTHING();
      n.setDATE(getDate(date_new));
      if (time_new != null) n.setTIME(time_new);
    } else if ("REP".equals(type)) {
      o.setDATE(getDate(date_old));
      n.setDATE(getDate(date_new));
      if (time_old != null) o.setTIME(time_old);
      if (time_new != null) n.setTIME(time_new);
    } else if ("DEL".equals(type)) {
      o.setDATE(getDate(date_old));
      if (time_old != null) o.setTIME(time_old);
      n.addNewNOTHING();
    }
  }

  /**
   * 
   * @param type
   * @param text_old
   * @param text_new
   * @param o
   * @param n
   */
  private void _getChangeText(String type, String text_old, String text_new, OLDVALUE o, NEWVALUE n) {
    if ("ADD".equals(type)) {
      o.addNewNOTHING();
      n.setTEXT(getTextFtMultiLines(text_new));
    } else if ("REP".equals(type)) {
      o.setTEXT(getTextFtMultiLines(text_old));
      n.setTEXT(getTextFtMultiLines(text_new));
    } else if ("DEL".equals(type)) {
      o.setTEXT(getTextFtMultiLines(text_old));
      n.addNewNOTHING();
    }
  }

  /**
   * 
   * @param type
   * @param cpv_old
   * @param cpv_new
   * @param cpvsupp1_old
   * @param cpvsupp1_new
   * @param cpvsupp2_old
   * @param cpvsupp2_new
   * @param o
   * @param n
   */
  private void _getChangeCPV(String type, String cpv_old, String cpv_new, String cpvsupp1_old, String cpvsupp1_new, String cpvsupp2_old,
      String cpvsupp2_new, OLDVALUE o, NEWVALUE n) {
    if ("ADD".equals(type)) {
      o.addNewNOTHING();
      if (cpv_new != null) {
        n.addNewCPVMAIN();
        n.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv_new));
        if (cpvsupp1_new != null) {
          n.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_new));
        }
        if (cpvsupp2_new != null) {
          n.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_new));
        }
      }
    } else if ("REP".equals(type)) {
      if (cpv_old != null) {
        o.addNewCPVMAIN();
        o.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv_old));
        if (cpvsupp1_old != null) {
          o.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_old));
        }
        if (cpvsupp2_old != null) {
          o.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_old));
        }
      }
      if (cpv_new != null) {
        n.addNewCPVMAIN();
        n.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv_new));
        if (cpvsupp1_new != null) {
          n.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_new));
        }
        if (cpvsupp2_new != null) {
          n.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_new));
        }
      }

    } else if ("DEL".equals(type)) {
      if (cpv_old != null) {
        o.addNewCPVMAIN();
        o.getCPVMAIN().addNewCPVCODE().setCODE(getCPV(cpv_old));
        if (cpvsupp1_old != null) {
          o.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_old));
        }
        if (cpvsupp2_old != null) {
          o.getCPVMAIN().addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_old));
        }
      }
      n.addNewNOTHING();
    }
  }

  /**
   * 
   * @param type
   * @param cpv_old
   * @param cpv_new
   * @param cpvsupp1_old
   * @param cpvsupp1_new
   * @param cpvsupp2_old
   * @param cpvsupp2_new
   * @param o
   * @param n
   */
  private void _getChangeCPVADDITIONAL(String type, String cpv_old, String cpv_new, String cpvsupp1_old, String cpvsupp1_new,
      String cpvsupp2_old, String cpvsupp2_new, OLDVALUE o, NEWVALUE n) {
    if ("ADD".equals(type)) {
      o.addNewNOTHING();
      if (cpv_new != null) {
        CpvSet newCpvAdditional = n.addNewCPVADDITIONAL();
        newCpvAdditional.addNewCPVCODE().setCODE(getCPV(cpv_new));
        if (cpvsupp1_new != null) {
          newCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_new));
        }
        if (cpvsupp2_new != null) {
          newCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_new));
        }
      }
    } else if ("REP".equals(type)) {
      if (cpv_old != null) {
        CpvSet oldCpvAdditional = o.addNewCPVADDITIONAL();
        oldCpvAdditional.addNewCPVCODE().setCODE(getCPV(cpv_old));
        if (cpvsupp1_old != null) {
          oldCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_old));
        }
        if (cpvsupp2_old != null) {
          oldCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_old));
        }
      }
      if (cpv_new != null) {
        CpvSet newCpvAdditional = n.addNewCPVADDITIONAL();
        newCpvAdditional.addNewCPVCODE().setCODE(getCPV(cpv_new));
        if (cpvsupp1_new != null) {
          newCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_new));
        }
        if (cpvsupp2_new != null) {
          newCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_new));
        }
      }

    } else if ("DEL".equals(type)) {
      if (cpv_old != null) {
        CpvSet oldCpvAdditional = o.addNewCPVADDITIONAL();
        oldCpvAdditional.addNewCPVCODE().setCODE(getCPV(cpv_old));
        if (cpvsupp1_old != null) {
          oldCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp1_old));
        }
        if (cpvsupp2_old != null) {
          oldCpvAdditional.addNewCPVSUPPLEMENTARYCODE().setCODE(getCPVSUPP(cpvsupp2_old));
        }
      }
      n.addNewNOTHING();
    }
  }

}
