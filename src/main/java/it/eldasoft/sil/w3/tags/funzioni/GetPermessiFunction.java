package it.eldasoft.sil.w3.tags.funzioni;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.properties.ConfigManager;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class GetPermessiFunction extends AbstractFunzioneTag {

  private static final String PROP_PROTEZIONE_ARCHIVI = "it.eldasoft.protezionearchivi";
  private static final String PROP_SIMAP_RESEND       = "it.eldasoft.simap.resend";

  public GetPermessiFunction() {
    super(4, new Class[] { PageContext.class, String.class, String.class, Object.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);

    String tblname = (String) params[1];
    String clm1name = (String) params[2];
    String clm1value = (String) params[3];

    String visualizzazione = "false";
    String modifica = "false";
    String esecuzione = "false";
    String modificaDopoInvio = "true";

    boolean verificaPermessi = false;

    try {
      // Verifico se la tabella indicata e' una archivio e se deve essere
      // controllata dal sistema di protezione. Se l'archivio non e' controllato
      // significa che ha sempre i permessi di visualizzazione e modifica
      if ("W3AMMI".equals(tblname)
          || "UFFINT".equals(tblname)
          || "TECNI".equals(tblname)
          || "IMPR".equals(tblname)
          || "TEIM".equals(tblname)) {
        String protezioneArchivi = ConfigManager.getValore(PROP_PROTEZIONE_ARCHIVI);
        if (protezioneArchivi != null && "1".equals(protezioneArchivi)) {
          verificaPermessi = true;
        } else {
          verificaPermessi = false;
          visualizzazione = "true";
          modifica = "true";
          esecuzione = "false";
        }
      } else {
        verificaPermessi = true;
      }

      if (verificaPermessi) {
        Long rwx = UtilityPermessi.getRWXPermessi(profilo, tblname, clm1name, clm1value, sqlManager);
        if (rwx != null) {
          switch (rwx.intValue()) {
          case 4:
            visualizzazione = "true";
            modifica = "false";
            esecuzione = "false";
            break;

          case 6:
            visualizzazione = "true";
            modifica = "true";
            esecuzione = "false";
            break;

          case 7:
            visualizzazione = "true";
            modifica = "true";
            esecuzione = "true";
            break;
          }
        }
      }

    } catch (SQLException e) {
      throw new JspException("Errore nella lettura dei diritti di visualizzazione/modifica/esecuzione", e);
    }

    // Verifico se il bando/avviso e' stato inviato con successo.
    // Se l'invio e' pendente o e' stato inviato con successo si deve inibire
    if ("W3SIMAP".equals(tblname) && "ID".equals(clm1name)) {
      String selectW3SIMAPWS = "select status_code from w3simapws where id = ? and num = (select max(num) from w3simapws where id = ?) and phase = 'GAMMA'";
      Long id = new Long(clm1value);
      try {
        String status_code = (String) sqlManager.getObject(selectW3SIMAPWS, new Object[] { id, id });
        if (status_code != null) {
          String resend = ConfigManager.getValore(PROP_SIMAP_RESEND);
          if (resend == null || (resend != null && "".equals(resend.trim()))) {
            resend = "NOT_PUBLISHED, WAITING_FOR_INFORMATION, RECEPTION_ERROR, QUALIFICATION_ERROR";
          }
          java.util.List<String> originalResend = Arrays.asList(resend.split(","));
          java.util.List<String> re = new ArrayList<String>();
          for(String s : originalResend) {
            re.add(s.trim());
          }
          if (!re.contains(status_code.trim())) modificaDopoInvio = "false";
        }
      } catch (SQLException e) {

      }

    }

    pageContext.setAttribute("autorizzatoVisualizzazione", visualizzazione, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("autorizzatoModifica", modifica, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("autorizzatoModificaDopoInvio", modificaDopoInvio, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("autorizzatoEsecuzione", esecuzione, PageContext.REQUEST_SCOPE);

    return null;

  }

}
