package it.eldasoft.sil.w3.tags.funzioni;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.sql.sqlparser.JdbcParametro;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

public class GetTitleW3SIMAPEMAILFunction extends AbstractFunzioneTag {

  static Logger logger = Logger.getLogger(GetTitleW3SIMAPEMAILFunction.class);

  public GetTitleW3SIMAPEMAILFunction() {
    super(2, new Class[] { PageContext.class, String.class });
  }

  public String function(PageContext pageContext, Object params[])
      throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager",
        pageContext, SqlManager.class);

    HashMap keyParent = UtilityTags.stringParamsToHashMap(
        (String) pageContext.getAttribute(
            UtilityTags.DEFAULT_HIDDEN_KEY_TABELLA, PageContext.REQUEST_SCOPE),
        null);

    Long id = new Long(
        ((JdbcParametro) keyParent.get("W3SIMAPEMAIL.ID")).getStringValue());
    Long num = new Long(
        ((JdbcParametro) keyParent.get("W3SIMAPEMAIL.NUM")).getStringValue());

    String titolo = "";

    try {
      // Tipo di bando/avviso
      String form = (String) sqlManager.getObject(
          "select form from w3simap where id = ?", new Object[] { id });

      String title_contract = null;
      if (form != null && !"FS14".equals(form)) {
        // Se non � rettifica ricavo direttamente la descrizione
        title_contract = (String) sqlManager.getObject(
            "select title_contract from v_w3simap where id = ?",
            new Object[] { id });
      } else {
        // Se si tratta di rettifica ricavo la descrizione del bando/avviso
        // associato
        title_contract = (String) sqlManager.getObject(
            "select v_w3simap.title_contract from v_w3simap, w3fs14 where v_w3simap.id = w3fs14.id_rif and w3fs14.id = ?",
            new Object[] { id });
      }

      // Data ed ora di invio
      String data_spedizione_s = (String) sqlManager.getObject(
          "select data_spedizione_s from w3simapemail where id = ? and num = ?",
          new Object[] { id, num });

      titolo = title_contract
          + " - Invio n. "
          + num.toString()
          + " del "
          + data_spedizione_s;

    } catch (SQLException e) {
      throw new JspException("Errore nella selezione del titolo", e);
    }

    return titolo;
  }

}
