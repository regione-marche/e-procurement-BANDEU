package it.eldasoft.sil.w3.bl;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS1;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS14;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS2;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS20;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS3;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS4;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS5;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS6;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS7;
import it.eldasoft.sil.w3.simap.validazione.ValidazioneFS8;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

public class ValidazioneSIMAPManager {

  static Logger      logger = Logger.getLogger(ValidazioneSIMAPManager.class);

  private SqlManager sqlManager;

  /**
   * 
   * @return
   */
  public SqlManager getSqlManager() {
    return this.sqlManager;
  }

  /**
   * @param sqlManager
   *        sqlManager da settare internamente alla classe.
   */
  public void setSqlManager(SqlManager sqlManager) {
    this.sqlManager = sqlManager;
  }

  public HashMap<String, Object> validate(Object[] params) throws JspException {
    HashMap<String, Object> infoValidazione = new HashMap<String, Object>();
    Long id = new Long((String) params[1]);
    Long syscon = new Long(((Integer) params[2]).intValue());
    infoValidazione = this.validate(id, syscon);
    return infoValidazione;
  }

  public HashMap<String, Object> validate(Long id, Long syscon) throws JspException {
    HashMap<String, Object> infoValidazione = new HashMap<String, Object>();
    List<Object> listaControlli = new Vector<Object>();

    try {

      String formulario = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });
      String titolo = (String) sqlManager.getObject("select tab2d2 from tab2 where tab2cod = ? and tab2tip = ?", new Object[] { "W3z64",
          formulario });

      // Risposta al primo invio del 13/12/2010. All Files: Wrong
      // NOTICE_DISPATCH_DATE: it should be the real sending date of XML files
      // String updateNoticeDate = "update W3" + formulario +
      // " set notice_date = ? where id = ?";
      // sqlManager.update(updateNoticeDate, new Object[] { new Date(), id });

      if ("FS1".equals(formulario)) {
        ValidazioneFS1 validazioneFS1 = new ValidazioneFS1();
        validazioneFS1.validazioneFS1(sqlManager, id, listaControlli);
      }

      if ("FS2".equals(formulario)) {
        ValidazioneFS2 validazioneFS2 = new ValidazioneFS2();
        validazioneFS2.validazioneFS2(sqlManager, id, listaControlli);
      }

      if ("FS3".equals(formulario)) {
        ValidazioneFS3 validazioneFS3 = new ValidazioneFS3();
        validazioneFS3.validazioneFS3(sqlManager, id, listaControlli);
      }

      if ("FS4".equals(formulario)) {
        ValidazioneFS4 validazioneFS4 = new ValidazioneFS4();
        validazioneFS4.validazioneFS4(sqlManager, id, listaControlli);
      }
      
      if ("FS5".equals(formulario)) {
        ValidazioneFS5 validazioneFS5 = new ValidazioneFS5();
        validazioneFS5.validazioneFS5(sqlManager, id, listaControlli);
      }

      if ("FS6".equals(formulario)) {
        ValidazioneFS6 validazioneFS6 = new ValidazioneFS6();
        validazioneFS6.validazioneFS6(sqlManager, id, listaControlli);
      }

      if ("FS7".equals(formulario)) {
        ValidazioneFS7 validazioneFS7 = new ValidazioneFS7();
        validazioneFS7.validazioneFS7(sqlManager, id, listaControlli);
      }
      
      if ("FS8".equals(formulario)) {
        ValidazioneFS8 validazioneFS8 = new ValidazioneFS8();
        validazioneFS8.validazioneFS8(sqlManager, id, listaControlli);
      }

      if ("FS14".equals(formulario)) {
        ValidazioneFS14 validazioneFS14 = new ValidazioneFS14();
        validazioneFS14.validazioneFS14(sqlManager, id, listaControlli);
      }

      if ("FS20".equals(formulario)) {
        ValidazioneFS20 validazioneFS20 = new ValidazioneFS20();
        validazioneFS20.validazioneFS20(sqlManager, id, listaControlli);
      }
      
      // Controllo informazioni utente connesso
      // String email = (String)
      // sqlManager.getObject("select email from usrsys where syscon = ?", new
      // Object[] { syscon });
      // if (email == null || (email != null && "".equals(email))) {
      // String descrizione =
      // "L\'utente connesso è privo dell\'indirizzo email";
      // String messaggio =
      // "Per procedere è obbligatorio valorizzare l\'indirizzo email";
      // listaControlli.add(((Object) (new Object[] { "E", "Utente applicativo",
      // descrizione, messaggio })));
      // }
      //
      // ValidazioneUtility.validazioneEmail(email, "USRSYS", "EMAIL",
      // "Utente applicativo", listaControlli);

      infoValidazione.put("titolo", titolo);
      infoValidazione.put("listaControlli", listaControlli);

      int numeroErrori = 0;
      int numeroWarning = 0;

      if (!listaControlli.isEmpty()) {
        for (int i = 0; i < listaControlli.size(); i++) {
          Object[] controllo = (Object[]) listaControlli.get(i);
          String tipo = (String) controllo[0];

          if ("E".equals(tipo)) {
            numeroErrori++;
          }
          if ("W".equals(tipo)) {
            numeroWarning++;
          }
        }
      }

      infoValidazione.put("numeroErrori", new Long(numeroErrori));
      infoValidazione.put("numeroWarning", new Long(numeroWarning));

    } catch (SQLException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    } catch (GestoreException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    }

    return infoValidazione;
  }

  public HashMap<String, Object> validateW3ANNEXB(Long id, Long num, Long syscon) throws JspException {
    HashMap<String, Object> infoValidazione = new HashMap<String, Object>();
    List<Object> listaControlli = new Vector<Object>();

    try {

      String formulario = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });
      String titolo = (String) sqlManager.getObject("select tab2d2 from tab2 where tab2cod = ? and tab2tip = ?", new Object[] { "W3z64",
          formulario });

      if ("FS1".equals(formulario)) {
        ValidazioneFS1 validazioneFS1 = new ValidazioneFS1();
        validazioneFS1.validazione_FS1_W3ANNEXB(sqlManager, id, num, listaControlli);
      }

      if ("FS2".equals(formulario)) {
        ValidazioneFS2 validazioneFS2 = new ValidazioneFS2();
        validazioneFS2.validazioneW3ANNEXB(sqlManager, id, num, listaControlli);
      }

      if ("FS3".equals(formulario)) {
        ValidazioneFS3 validazioneFS3 = new ValidazioneFS3();
        validazioneFS3.validazioneW3ANNEXB(sqlManager, id, num, listaControlli);
      }

      if ("FS5".equals(formulario)) {
        ValidazioneFS5 validazioneFS5 = new ValidazioneFS5();
        validazioneFS5.validazioneW3ANNEXB(sqlManager, id, num, listaControlli);
      }

      infoValidazione.put("titolo", titolo);
      infoValidazione.put("listaControlli", listaControlli);

      int numeroErrori = 0;
      int numeroWarning = 0;

      if (!listaControlli.isEmpty()) {
        for (int i = 0; i < listaControlli.size(); i++) {
          Object[] controllo = (Object[]) listaControlli.get(i);
          String tipo = (String) controllo[0];

          if ("E".equals(tipo)) {
            numeroErrori++;
          }
          if ("W".equals(tipo)) {
            numeroWarning++;
          }
        }
      }

      infoValidazione.put("numeroErrori", new Long(numeroErrori));
      infoValidazione.put("numeroWarning", new Long(numeroWarning));

    } catch (SQLException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    } catch (GestoreException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    }

    return infoValidazione;
  }

  public HashMap<String, Object> validateW3FS3AWARD(Long id, Long item, Long syscon) throws JspException {
    HashMap<String, Object> infoValidazione = new HashMap<String, Object>();
    List<Object> listaControlli = new Vector<Object>();

    try {

      String formulario = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });
      String titolo = (String) sqlManager.getObject("select tab2d2 from tab2 where tab2cod = ? and tab2tip = ?", new Object[] { "W3z64",
          formulario });

      ValidazioneFS3 validazioneFS3 = new ValidazioneFS3();
      validazioneFS3.validazioneW3FS3AWARD(sqlManager, id, item, listaControlli);

      infoValidazione.put("titolo", titolo);
      infoValidazione.put("listaControlli", listaControlli);

      int numeroErrori = 0;
      int numeroWarning = 0;

      if (!listaControlli.isEmpty()) {
        for (int i = 0; i < listaControlli.size(); i++) {
          Object[] controllo = (Object[]) listaControlli.get(i);
          String tipo = (String) controllo[0];

          if ("E".equals(tipo)) {
            numeroErrori++;
          }
          if ("W".equals(tipo)) {
            numeroWarning++;
          }
        }
      }

      infoValidazione.put("numeroErrori", new Long(numeroErrori));
      infoValidazione.put("numeroWarning", new Long(numeroWarning));

    } catch (SQLException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    } catch (GestoreException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    }

    return infoValidazione;
  }

  public HashMap<String, Object> validateW3FS6AWARD(Long id, Long item, Long syscon) throws JspException {
    HashMap<String, Object> infoValidazione = new HashMap<String, Object>();
    List<Object> listaControlli = new Vector<Object>();

    try {

      String formulario = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });
      String titolo = (String) sqlManager.getObject("select tab2d2 from tab2 where tab2cod = ? and tab2tip = ?", new Object[] { "W3z64",
          formulario });

      ValidazioneFS6 validazioneFS6 = new ValidazioneFS6();
      validazioneFS6.validazioneW3FS6AWARD(sqlManager, id, item, listaControlli);

      infoValidazione.put("titolo", titolo);
      infoValidazione.put("listaControlli", listaControlli);

      int numeroErrori = 0;
      int numeroWarning = 0;

      if (!listaControlli.isEmpty()) {
        for (int i = 0; i < listaControlli.size(); i++) {
          Object[] controllo = (Object[]) listaControlli.get(i);
          String tipo = (String) controllo[0];

          if ("E".equals(tipo)) {
            numeroErrori++;
          }
          if ("W".equals(tipo)) {
            numeroWarning++;
          }
        }
      }

      infoValidazione.put("numeroErrori", new Long(numeroErrori));
      infoValidazione.put("numeroWarning", new Long(numeroWarning));

    } catch (SQLException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    } catch (GestoreException e) {
      throw new JspException("Errore nella funzione di controllo dei dati", e);
    }

    return infoValidazione;
  }

}
