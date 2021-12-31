package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

public class ValidazioneFS8 extends ValidazioneFormulariStandard {

  static Logger logger = Logger.getLogger(ValidazioneFS8.class);

  /**
   * Controllo dati formulario standard 8: avviso relativo al profilo di
   * committente.
   * 
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws GestoreException
   */
  public void validazioneFS8(SqlManager sqlManager, Long id, List<Object> listaControlli) throws GestoreException {
    if (logger.isDebugEnabled()) logger.debug("validazioneFS8: inizio metodo");

    try {

      // L'avviso riguarda
      String notice_relation = (String) sqlManager.getObject("select notice_relation from w3fs8 where id = ?", new Object[] { id });
      if (notice_relation == null) {
        ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS8", "NOTICE_RELATION", "");
      }

      this.validazione_Sezione_I(sqlManager, id, listaControlli);
      this.validazioneFS8_Sezione_II_VI(sqlManager, id, listaControlli);

    } catch (SQLException e) {
      throw new GestoreException("Errore nella lettura delle informazioni relative all'avviso sul profilo di committente",
          "validazioneFS8", e);
    }

    if (logger.isDebugEnabled()) logger.debug("validazioneFS8: fine metodo");
  }

  /**
   * Sezione II: oggetto.
   * 
   * @param sqlManager
   * @param id
   * @param listaControlli
   * @throws SQLException
   * @throws GestoreException
   */
  private void validazioneFS8_Sezione_II_VI(SqlManager sqlManager, Long id, List<Object> listaControlli) throws SQLException,
      GestoreException {

    String selectFS8 = "select w3fs8s2.title_contract, " // 0
        + " w3fs8s2.cpv, " // 1
        + " w3fs8s2.type_contract, " // 2
        + " w3fs8s2.reference, " // 3
        + " w3fs8.additional_information " // 4
        + " from w3fs8, w3fs8s2 "
        + " where w3fs8.id = w3fs8s2.id and w3fs8.id = ? and w3fs8s2.num = 1";

    String selectW3CPV = "select numcpv, cpv from w3cpv where ent = ? and id = ? and num = ?";

    List<?> datiFS8 = sqlManager.getVector(selectFS8, new Object[] { id });

    String pagina = "";

    // Denominazione dell'appalto
    pagina = UtilityTags.getResource("label.simap.fs8.II.1.1", null, false);
    String title_contract = (String) SqlManager.getValueFromVectorParam(datiFS8, 0).getValue();
    if (title_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS8S2", "TITLE_CONTRACT", pagina);
    } else {
      ValidazioneUtility.validazioneS200(title_contract, "W3FS8S2", "TITLE_CONTRACT", pagina, listaControlli);
    }

    // Numero di riferimento
    String reference = (String) SqlManager.getValueFromVectorParam(datiFS8, 3).getValue();
    ValidazioneUtility.validazioneS100(reference, "W3FS8S2", "REFERENCE", pagina, listaControlli);

    String type_contract = (String) SqlManager.getValueFromVectorParam(datiFS8, 2).getValue();
    
    // CPV principale
    pagina = UtilityTags.getResource("label.simap.fs8.II.1.2", null, false);
    String cpvMain = (String) SqlManager.getValueFromVectorParam(datiFS8, 1).getValue();
    if (cpvMain == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS8S2", "CPV", pagina);
    }

    // Controllo corrispondenza CPV con il tipo appalto
    ValidazioneUtility.validazioneCorrispondenzaCPV(listaControlli, pagina, cpvMain, type_contract, "W3FS8S2", "CPV");
    
    // Tipo di appalto
    pagina = UtilityTags.getResource("label.simap.fs8.II.1.3", null, false);
    if (type_contract == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3FS8S2", "TYPE_CONTRACT", pagina);
    }

    // Controllo CPV aggiuntivi, se esiste l'occorrenza in W3CPV diventa
    // obbligatorio indicare il CPV del vocabolario principale
    List<?> datiW3CPV = sqlManager.getListVector(selectW3CPV, new Object[] { "W3ANNEXB", id, new Long(1) });
    if (datiW3CPV != null && datiW3CPV.size() > 0) {
      for (int iCPV = 0; iCPV < datiW3CPV.size(); iCPV++) {
        String cpv = (String) SqlManager.getValueFromVectorParam(datiW3CPV.get(iCPV), 1).getValue();
        if (cpv == null) {
          pagina = UtilityTags.getResource("label.simap.fs8.II.2.2", null, false);
          ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3CPV", "CPV", pagina);
        }
      }
    }

    // Codice NUTS
    pagina = UtilityTags.getResource("label.simap.fs8.II.2.3", null, false);
    String site_nuts = (String) sqlManager.getObject("select site_nuts from w3annexb where id = ? and num = ?", new Object[] { id,
        new Long(1) });
    if (site_nuts == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "SITE_NUTS", pagina);
    } else {
      validazioneNUTS(sqlManager, site_nuts, "W3ANNEXB", "SITE_NUTS", pagina, listaControlli);
    }

    String site_nuts_2 = (String) sqlManager.getObject("select site_nuts_2 from w3annexb where id = ? and num = ?", new Object[] { id,
        new Long(1) });
    String site_nuts_3 = (String) sqlManager.getObject("select site_nuts_3 from w3annexb where id = ? and num = ?", new Object[] { id,
        new Long(1) });
    String site_nuts_4 = (String) sqlManager.getObject("select site_nuts_4 from w3annexb where id = ? and num = ?", new Object[] { id,
        new Long(1) });
    if (site_nuts_2 != null) {
      validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_2", pagina, listaControlli);
    }
    if (site_nuts_3 != null) {
      validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_3", pagina, listaControlli);
    }
    if (site_nuts_4 != null) {
      validazioneNUTS(sqlManager, site_nuts_2, "W3ANNEXB", "SITE_NUTS_4", pagina, listaControlli);
    }

    String site_label = (String) sqlManager.getObject("select site_label from w3annexb where id = ? and num = ?", new Object[] { id,
        new Long(1) });
    ValidazioneUtility.validazioneS200(site_label, "W3ANNEXB", "SITE_LABEL", pagina, listaControlli);

    // Descrizione dell'appalto
    pagina = UtilityTags.getResource("label.simap.fs8.II.2.4", null, false);
    String description = (String) sqlManager.getObject("select description from w3annexb where id = ? and num = ?", new Object[] { id,
        new Long(1) });
    if (description == null) {
      ValidazioneUtility.addCampoObbligatorio(listaControlli, "W3ANNEXB", "DESCRIPTION", pagina);
    } else {
      ValidazioneUtility.validazioneS4000(site_label, "W3ANNEXB", "DESCRIPTION", pagina, listaControlli);
    }

    // Informazioni complementari
    pagina = UtilityTags.getResource("label.simap.fs8.VI.3", null, false);
    String additional_information = (String) SqlManager.getValueFromVectorParam(datiFS8, 4).getValue();
    ValidazioneUtility.validazioneS4000(additional_information, "W3FS8", "ADDITIONAL_INFORMATION", pagina, listaControlli);

  }

}