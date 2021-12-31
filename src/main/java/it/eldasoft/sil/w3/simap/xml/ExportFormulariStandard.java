package it.eldasoft.sil.w3.simap.xml;

import it.eldasoft.gene.bl.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;

import eu.europa.publications.resource.schema.ted.r209.reception.BodyF01;
import eu.europa.publications.resource.schema.ted.r209.reception.BodyF05;
import eu.europa.publications.resource.schema.ted.r209.reception.CaActivity;
import eu.europa.publications.resource.schema.ted.r209.reception.CaType;
import eu.europa.publications.resource.schema.ted.r209.reception.CeActivity;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactContractingBody;
import eu.europa.publications.resource.schema.ted.r209.reception.ContactReview;
import eu.europa.publications.resource.schema.ted.r209.reception.Country;
import eu.europa.publications.resource.schema.ted.r209.reception.CpvSupplementaryCodes;
import eu.europa.publications.resource.schema.ted.r209.reception.P;
import eu.europa.publications.resource.schema.ted.r209.reception.TCountryList;
import eu.europa.publications.resource.schema.ted.r209.reception.TextFtMultiLines;
import eu.europa.publications.resource.schema.ted.r209.reception.TextFtSingleLine;
import eu.europa.publications.resource.schema.ted.x2021.nuts.Nuts;
import eu.europa.publications.resource.schema.ted.x2021.nuts.TNutsCodeList;


public class ExportFormulariStandard {

  public ExportFormulariStandard() {

  }

  static Logger                 logger                       = Logger.getLogger(ExportFormulariStandard.class);

  protected static final String PROP_SIMAP_TED_ESENDER_CLASS = "it.eldasoft.simap.tedesenderclass";
  protected static final String PROP_SIMAP_ESENDER_LOGIN     = "it.eldasoft.simap.esenderlogin";
  protected static final String PROP_SIMAP_EMAIL_TECHNICAL   = "it.eldasoft.simap.email.technical";

  /**
   * Conversione caratteri per XML
   * 
   * @param testo
   * @return
   */
  protected static String conversioneCaratteriXML(String testo) {
    String result = null;

    StringTokenizer tokenizer = new StringTokenizer(testo, "&<>\"%", true);
    int tokenCount = tokenizer.countTokens();

    if (tokenCount == 1)
      result = testo.trim();
    else {
      /*
       * text.length + (tokenCount * 6) Creo il buffer grande in modo da non
       * richiedere un'espansione del buffer che è molto costosa in termini
       * d'utilizzo del processore
       */
      StringBuffer buffer = new StringBuffer(testo.length() + tokenCount * 6);
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        if (token.length() == 1) {
          switch (token.charAt(0)) {
          case '&':
            buffer.append("&amp;");
            break;
          case '<':
            buffer.append("&lt;");
            break;
          case '>':
            buffer.append("&gt;");
            break;
          case '"':
            buffer.append("&quot;");
            break;
          default:
            buffer.append(token);
          }
        } else {
          buffer.append(token);
        }
      }

      result = buffer.toString().trim();
    }

    return result;
  }

  /**
   * Trasformazione stringhe estese nel formato TextFtMultiLines. Converte
   * ritorno a capo con elementi annidati di tipo P.
   * 
   * @param in
   * @return
   */
  protected static TextFtMultiLines getTextFtMultiLines(String in) {
    in = conversioneCaratteriXML(in);
    TextFtMultiLines textOutput = TextFtMultiLines.Factory.newInstance();
    if (in != null && !"".equals(in)) {
      String[] inArray = in.split("\r\n|\r|\n");
      if (inArray != null && inArray.length > 0) {
        P[] p = new P[inArray.length];
        for (int i = 0; i < inArray.length; i++) {
          p[i] = P.Factory.newInstance();
          XmlCursor c = p[i].newCursor();
          c.toFirstContentToken();
          c.toLastAttribute();
          c.insertChars(inArray[i]);
        }
        textOutput.setPArray(p);
      }
    }
    return textOutput;
  }

  /**
   * Gestione oggetto Text su singola riga.
   * 
   * @param in
   * @return
   */
  protected static TextFtSingleLine getTextFtSingleLine(String in) {
    in = conversioneCaratteriXML(in);
    TextFtSingleLine textOutput = TextFtSingleLine.Factory.newInstance();
    P p = P.Factory.newInstance();
    XmlCursor c = p.newCursor();
    c.toFirstContentToken();
    c.toLastAttribute();
    c.insertChars(in);
    textOutput.setP(p);
    return textOutput;
  }

  /**
   * Gestione codice CPV del vocabolario principale: bisogna considerare solo i
   * primi 8 caratteri. I rimanenti caratteri sono di controllo.
   */
  protected static String getCPV(String cpv) {
    return cpv.substring(0, 8);
  }

  /**
   * Gestione codice CPV del vocabolario supplementare: bisogna considerare solo
   * i primi 4 caratteri. I rimanenti caratteri sono di controllo.
   */
  protected static CpvSupplementaryCodes.CODE.Enum getCPVSUPP(String cpvsupp) {
    return CpvSupplementaryCodes.CODE.Enum.forString(cpvsupp.substring(0, 4));
  }

  /**
   * Trasformazione della data in calendar
   */
  protected static Calendar getDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.clear(Calendar.ZONE_OFFSET);
    return cal;
  }

  /**
   * Trasformazione dell'importo in BigDecimal.
   * 
   * @param originalCost
   * @return
   */
  protected static BigDecimal getCost(Double originalCost) {
    BigDecimal bg = null;
    if (originalCost != null) {
      bg = new BigDecimal(Double.toString(originalCost.doubleValue()));
    }
    return bg;
  }

  /**
   * Sezione I
   * 
   * @param sqlManager
   * @param id
   * @return
   * @throws SQLException
   */
  private BodyF01 getBodyF01Generic(SqlManager sqlManager, Long id) throws SQLException {

    BodyF01 bodyF01 = BodyF01.Factory.newInstance();

    String selectW3SIMAP = "select codamm, " // 0
        + " joint_procurement, " // 1
        + " procurement_law, " // 2
        + " central_purchasing, " // 3
        + " document_fu_re, " // 4
        + " document_url, " // 5
        + " further_info, " // 6
        + " further_info_codein, " // 7
        + " participation, " // 8
        + " participation_codein, " // 9
        + " participation_el, " // 10
        + " participation_url, " // 11
        + " url_tool " // 12
        + " from w3simap "
        + " where id = ?";

    List<?> datiW3SIMAP = sqlManager.getVector(selectW3SIMAP, new Object[] { id });
    if (datiW3SIMAP != null && datiW3SIMAP.size() > 0) {

      // Denominazione ed indirizzo amministrazione aggiudicatrice
      String w3simap_codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 0).getValue();
      bodyF01.setADDRESSCONTRACTINGBODY(this.getContactContractingBodyW3AMMI(sqlManager, w3simap_codamm));

      // Denominazione ed indirizzo delle altre amministrazione aggiudicatrici
      // congiunte
      List<?> datiW3SIMAP_ADDR = sqlManager.getListVector("select codamm from w3simap_addr where id = ? order by num", new Object[] { id });
      if (datiW3SIMAP_ADDR != null && datiW3SIMAP_ADDR.size() > 0) {
        for (int isa = 0; isa < datiW3SIMAP_ADDR.size(); isa++) {
          String w3simap_addr_codamm = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP_ADDR.get(isa), 0).getValue();
          bodyF01.addNewADDRESSCONTRACTINGBODYADDITIONAL();
          bodyF01.setADDRESSCONTRACTINGBODYADDITIONALArray(isa, this.getContactContractingBodyW3AMMI(sqlManager, w3simap_addr_codamm));
        }
      }

      // Appalto congiunto
      String joint_procurement = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 1).getValue();
      if (joint_procurement != null && "1".equals(joint_procurement)) {
        bodyF01.addNewJOINTPROCUREMENTINVOLVED();
      }

      // Normative
      String procurement_law = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 2).getValue();
      if (procurement_law != null) {
        bodyF01.addNewPROCUREMENTLAW();
        bodyF01.setPROCUREMENTLAW(getTextFtMultiLines(procurement_law));
      }

      // Centrale di committenza
      String central_purchasing = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 3).getValue();
      if (central_purchasing != null && "1".equals(central_purchasing)) {
        bodyF01.addNewCENTRALPURCHASING();
      }

      // Documenti di gara
      Long document_fu_re = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAP, 4).getValue();
      if (document_fu_re != null) {
        if (new Long(1).equals(document_fu_re)) {
          bodyF01.addNewDOCUMENTFULL();
        } else if (new Long(2).equals(document_fu_re)) {
          bodyF01.addNewDOCUMENTRESTRICTED();
        }
      }

      // URL dei documenti di gara
      String document_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 5).getValue();
      if (document_url != null) {
        bodyF01.setURLDOCUMENT(document_url);
      }

      // Ulteriori informazioni
      String further_info = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 6).getValue();
      String further_info_codein = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 7).getValue();
      if (further_info != null) {
        if ("1".equals(further_info)) {
          bodyF01.addNewADDRESSFURTHERINFOIDEM();
        } else if ("2".equals(further_info)) {
          bodyF01.addNewADDRESSFURTHERINFO();
          bodyF01.setADDRESSFURTHERINFO(this.getContactContractingBodyUFFINT(sqlManager, further_info_codein));
        }
      }

      // Partecipazione
      String participation = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 8).getValue();
      String participation_codein = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 9).getValue();
      String participation_el = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 10).getValue();
      String participation_url = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 11).getValue();
      if (participation != null) {
        if ("1".equals(participation)) {
          bodyF01.addNewADDRESSPARTICIPATIONIDEM();
        } else {
          bodyF01.addNewADDRESSPARTICIPATION();
          bodyF01.setADDRESSPARTICIPATION(this.getContactContractingBodyUFFINT(sqlManager, participation_codein));
        }
      }
      if (participation_el != null && "1".equals(participation_el)) {
        bodyF01.setURLPARTICIPATION(participation_url);
      }

      // URL tool
      String url_tool = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP, 12).getValue();
      if (url_tool != null) {
        bodyF01.setURLTOOL(url_tool);
      }
    }

    return bodyF01;

  }

  protected BodyF01 getBodyF01CAGeneric(SqlManager sqlManager, Long id) throws SQLException {
    BodyF01 bodyF01 = getBodyF01Generic(sqlManager, id);
    addCAToAuthority(sqlManager, bodyF01, id);
    return bodyF01;
  }

  protected BodyF05 getBodyF05CEGeneric(SqlManager sqlManager, Long id) throws SQLException {
    BodyF01 bodyF01 = getBodyF01Generic(sqlManager, id);
    BodyF05 bodyF05 = (BodyF05) bodyF01.changeType(BodyF05.type);
    addCEToAuthority(sqlManager, id, bodyF05);
    return bodyF05;
  }

  /**
   * @param sqlManager
   * @param bodyF01
   * @param id
   * @throws SQLException
   */
  private void addCAToAuthority(SqlManager sqlManager, BodyF01 bodyF01, Long id) throws SQLException {
    // Tipologia di amministrazione e principale settore di attività
    String selectW3AMMI = "select type_authority, " // 0
        + " type_authority_other, " // 1
        + " type_caacti, " // 2
        + " type_caacti_other " // 3
        + " from w3ammi, w3simap "
        + " where w3ammi.codamm = w3simap.codamm and w3simap.id = ?";
    List<?> datiW3AMMI = sqlManager.getVector(selectW3AMMI, new Object[] { id });
    if (datiW3AMMI != null && datiW3AMMI.size() > 0) {

      // 1 |REGIONAL_AUTHORITY |Autorità regionale o locale |
      // 2 |MINISTRY |Ministero o qualsiasi altra autorità nazionale o
      // federale |
      // 3 |BODY_PUBLIC |Organismo di diritto pubblico |
      // 4 |EU_INSTITUTION |Istituzione/agenzia europea o organizzazione
      // internazionale |
      // 5 |NATIONAL_AGENCY |Agenzia/ufficio nazionale o federale |
      // 6 |REGIONAL_AGENCY |Agenzia/ufficio regionale o locale |
      String type_authority = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 0).getValue();
      if (type_authority != null) {
        if ("1".equals(type_authority)) {
          bodyF01.addNewCATYPE().setVALUE(CaType.VALUE.REGIONAL_AUTHORITY);
        } else if ("2".equals(type_authority)) {
          bodyF01.addNewCATYPE().setVALUE(CaType.VALUE.MINISTRY);
        } else if ("3".equals(type_authority)) {
          bodyF01.addNewCATYPE().setVALUE(CaType.VALUE.BODY_PUBLIC);
        } else if ("4".equals(type_authority)) {
          bodyF01.addNewCATYPE().setVALUE(CaType.VALUE.EU_INSTITUTION);
        } else if ("5".equals(type_authority)) {
          bodyF01.addNewCATYPE().setVALUE(CaType.VALUE.NATIONAL_AGENCY);
        } else if ("6".equals(type_authority)) {
          bodyF01.addNewCATYPE().setVALUE(CaType.VALUE.REGIONAL_AGENCY);
        }
      }

      String type_authority_other = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 1).getValue();
      if (type_authority_other != null) {
        bodyF01.setCATYPEOTHER(type_authority_other);
      }

      // 1 |1 |Servizi generali delle amministrazioni pubbliche |
      // 2 |2 |Edilizia abitativa e strutture per le collettivit… |
      // 3 |3 |Difesa |
      // 4 |4 |Protezione sociale |
      // 5 |5 |Ordine pubblico e sicurezza |
      // 6 |6 |Servizi ricreativi, cultura e religione |
      // 7 |7 |Ambiente |
      // 8 |8 |Istruzione |
      // 9 |9 |Affari economici e finanziari |
      // 10 |10 |Salute |
      String type_caacti = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 2).getValue();
      if (type_caacti != null) {
        if ("1".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.GENERAL_PUBLIC_SERVICES);
        } else if ("2".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.HOUSING_AND_COMMUNITY_AMENITIES);
        } else if ("3".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.DEFENCE);
        } else if ("4".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.SOCIAL_PROTECTION);
        } else if ("5".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.PUBLIC_ORDER_AND_SAFETY);
        } else if ("6".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.RECREATION_CULTURE_AND_RELIGION);
        } else if ("7".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.ENVIRONMENT);
        } else if ("8".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.EDUCATION);
        } else if ("9".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.ECONOMIC_AND_FINANCIAL_AFFAIRS);
        } else if ("10".equals(type_caacti)) {
          bodyF01.addNewCAACTIVITY().setVALUE(CaActivity.VALUE.HEALTH);
        }
      }

      String type_caacti_other = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 3).getValue();
      if (type_caacti_other != null) {
        bodyF01.setCAACTIVITYOTHER(type_caacti_other);
      }

    }
  }

  /**
   * 
   * @param sqlManager
   * @param id
   * @param bodyF05
   * @throws SQLException
   */
  private void addCEToAuthority(SqlManager sqlManager, Long id, BodyF05 bodyF05) throws SQLException {
    String selectW3AMMI = "select type_ceacti, " // 0
        + " type_ceacti_other " // 1
        + " from w3ammi, w3simap "
        + " where w3ammi.codamm = w3simap.codamm and w3simap.id = ?";
    List<?> datiW3AMMI = sqlManager.getVector(selectW3AMMI, new Object[] { id });
    if (datiW3AMMI != null && datiW3AMMI.size() > 0) {
      String type_ceacti = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 0).getValue();

      // |1|Produzione, trasporto e distribuzione di gas e calore
      // |2|Servizi ferroviari
      // |3|Elettricita'
      // |4|Servizi di ferrovia urbana, tram, filobus o bus
      // |5|Esplorazione ed estrazione di gas e petrolio
      // |6|Attivit… connesse ai porti
      // |7|Esplorazione ed estrazione di carbone e altri combustibili solidi
      // |8|Attivita' connesse agli aeroporti
      // |9|Acqua
      // |10|Servizi postali
      if (type_ceacti != null) {
        if ("1".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT);
        } else if ("2".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.RAILWAY_SERVICES);
        } else if ("3".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.ELECTRICITY);
        } else if ("4".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES);
        } else if ("5".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.EXPLORATION_EXTRACTION_GAS_OIL);
        } else if ("6".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.PORT_RELATED_ACTIVITIES);
        } else if ("7".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL);
        } else if ("8".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.AIRPORT_RELATED_ACTIVITIES);
        } else if ("9".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.WATER);
        } else if ("10".equals(type_ceacti)) {
          bodyF05.addNewCEACTIVITY().setVALUE(CeActivity.VALUE.POSTAL_SERVICES);
        }

      }

      String type_ceacti_other = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 1).getValue();
      if (type_ceacti_other != null) {
        bodyF05.setCEACTIVITYOTHER(type_ceacti_other);
      }
    }
  }

  /**
   * Oggetto ContactContractingBody relativo a UFFINT.
   * 
   * @param sqlManager
   * @param codein
   * @return
   * @throws SQLException
   */
  protected ContactContractingBody getContactContractingBodyUFFINT(SqlManager sqlManager, String codein) throws SQLException {
    ContactContractingBody contactContractingBody = ContactContractingBody.Factory.newInstance();
    String selectUFFINT = "select nomein, " // 0
        + " viaein, " // 1
        + " nciein, " // 2
        + " citein, " // 3
        + " proein, " // 4
        + " capein, " // 5
        + " codnaz, " // 6
        + " nomres, " // 7
        + " telein, " // 8
        + " emaiin, " // 9
        + " faxein, " // 10
        + " indweb " // 11
        + " from uffint where codein = ?";

    List<?> datiUFFINT = sqlManager.getVector(selectUFFINT, new Object[] { codein });
    if (datiUFFINT != null && datiUFFINT.size() > 0) {
      String nomein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 0).getValue();
      String viaein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 1).getValue();
      String nciein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 2).getValue();
      String capein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 5).getValue();
      Long codnaz = (Long) SqlManager.getValueFromVectorParam(datiUFFINT, 6).getValue();
      String nomres = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 7).getValue();
      String telein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 8).getValue();
      String emaiin = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 9).getValue();
      String faxein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 10).getValue();
      String indweb = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 11).getValue();

      // Nome ufficiale
      if (nomein != null) {
        nomein = conversioneCaratteriXML(nomein);
        contactContractingBody.setOFFICIALNAME(nomein);
      }
      // Indirizzo
      if (viaein != null) {
        viaein = conversioneCaratteriXML(viaein);
        String indirizzo = "";
        if (viaein != null) indirizzo += viaein;
        if (nciein != null) indirizzo += ", " + nciein;
        contactContractingBody.setADDRESS(indirizzo);
      }

      // Citta'
      String town = (String) sqlManager.getObject("select town from w3uffint where codein = ?", new Object[] { codein });
      if (town != null) contactContractingBody.setTOWN(town);

      // NUTS
      String nuts = (String) sqlManager.getObject("select nuts from w3uffint where codein = ?", new Object[] { codein });
      if (nuts != null) {
        Nuts r209nuts = Nuts.Factory.newInstance();
        r209nuts.setCODE(TNutsCodeList.Enum.forString(nuts));
        contactContractingBody.setNUTS(r209nuts);
      }

      // CAP
      if (capein != null) {
        contactContractingBody.setPOSTALCODE(capein);
      }
      // Paese
      if (codnaz != null) {
        String tab2tip = (String) sqlManager.getObject("select tab2tip from tab2 where tab2cod = ? and tab2d1 = ?", new Object[] { "W3z12",
            codnaz.toString() });
        Country r209country = Country.Factory.newInstance();
        r209country.setVALUE(TCountryList.Enum.forString(tab2tip));
        contactContractingBody.setCOUNTRY(r209country);
      }
      // Punto di contatto
      if (nomres != null) {
        nomres = conversioneCaratteriXML(nomres);
        contactContractingBody.setCONTACTPOINT(nomres);
      }
      // Telefono
      if (telein != null) {
        contactContractingBody.setPHONE(telein);
      }
      // Email
      if (emaiin != null) {
        contactContractingBody.setEMAIL(emaiin);
      }
      // Fax
      if (faxein != null) {
        contactContractingBody.setFAX(faxein);
      }
      // Indirizzo generale
      if (indweb != null) {
        contactContractingBody.setURLGENERAL(indweb);
      }

    }

    return contactContractingBody;
  }

  /**
   * Oggetto ContactContractingBody relativo a W3AMMI.
   * 
   * @param sqlManager
   * @param codamm
   * @return
   * @throws SQLException
   */
  protected ContactContractingBody getContactContractingBodyW3AMMI(SqlManager sqlManager, String codamm) throws SQLException {
    ContactContractingBody contactContractingBody = ContactContractingBody.Factory.newInstance();
    String selectW3AMMI = "select codein, url_general, url_buyer from w3ammi where codamm = ?";
    List<?> datiW3AMMI = sqlManager.getVector(selectW3AMMI, new Object[] { codamm });
    if (datiW3AMMI != null && datiW3AMMI.size() > 0) {
      String codein = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 0).getValue();
      contactContractingBody = this.getContactContractingBodyUFFINT(sqlManager, codein);
      // Indirizzo del profilo del committente
      String url_buyer = (String) SqlManager.getValueFromVectorParam(datiW3AMMI, 2).getValue();
      if (url_buyer != null) contactContractingBody.setURLBUYER(url_buyer);
    }
    return contactContractingBody;
  }

  /**
   * Oggetto ContactReview utilizzato nella sezione VI.
   * 
   * @param sqlManager
   * @param codein
   * @return
   * @throws SQLException
   */
  protected ContactReview getContactReview(SqlManager sqlManager, String codein) throws SQLException {
    ContactReview contactReview = ContactReview.Factory.newInstance();

    String selectUFFINT = "select nomein, " // 0
        + " viaein, " // 1
        + " nciein, " // 2
        + " citein, " // 3
        + " proein, " // 4
        + " capein, " // 5
        + " codnaz, " // 6
        + " nomres, " // 7
        + " telein, " // 8
        + " emaiin, " // 9
        + " faxein, " // 10
        + " indweb " // 11
        + " from uffint where codein = ?";

    List<?> datiUFFINT = sqlManager.getVector(selectUFFINT, new Object[] { codein });
    if (datiUFFINT != null && datiUFFINT.size() > 0) {
      String nomein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 0).getValue();
      String viaein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 1).getValue();
      String nciein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 2).getValue();
      String capein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 5).getValue();
      Long codnaz = (Long) SqlManager.getValueFromVectorParam(datiUFFINT, 6).getValue();
      String telein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 8).getValue();
      String emaiin = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 9).getValue();
      String faxein = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 10).getValue();
      String indweb = (String) SqlManager.getValueFromVectorParam(datiUFFINT, 11).getValue();

      // Nome ufficiale
      if (nomein != null) {
        nomein = conversioneCaratteriXML(nomein);
        contactReview.setOFFICIALNAME(nomein);
      }
      // Indirizzo
      if (viaein != null) {
        viaein = conversioneCaratteriXML(viaein);
        String indirizzo = "";
        if (viaein != null) indirizzo += viaein;
        if (nciein != null) indirizzo += ", " + nciein;
        contactReview.setADDRESS(indirizzo);
      }

      // Citta'
      String town = (String) sqlManager.getObject("select town from w3uffint where codein = ?", new Object[] { codein });
      if (town != null) contactReview.setTOWN(town);

      // CAP
      if (capein != null) {
        contactReview.setPOSTALCODE(capein);
      }
      // Paese
      if (codnaz != null) {
        String tab2tip = (String) sqlManager.getObject("select tab2tip from tab2 where tab2cod = ? and tab2d1 = ?", new Object[] { "W3z12",
            codnaz.toString() });
        Country r209country = Country.Factory.newInstance();
        r209country.setVALUE(TCountryList.Enum.forString(tab2tip));
        contactReview.setCOUNTRY(r209country);
      }
      // Telefono
      if (telein != null) {
        contactReview.setPHONE(telein);
      }
      // Email
      if (emaiin != null) {
        contactReview.setEMAIL(emaiin);
      }
      // Fax
      if (faxein != null) {
        contactReview.setFAX(faxein);
      }
      // Indirizzo generale
      if (indweb != null) {
        contactReview.setURL(indweb);
      }

    }

    return contactReview;
  }

}
