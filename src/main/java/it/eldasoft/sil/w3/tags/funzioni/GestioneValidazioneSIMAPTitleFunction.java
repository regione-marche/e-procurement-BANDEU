package it.eldasoft.sil.w3.tags.funzioni;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.utils.spring.UtilitySpring;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

public class GestioneValidazioneSIMAPTitleFunction extends AbstractFunzioneTag {

  static Logger logger = Logger.getLogger(GestioneValidazioneSIMAPTitleFunction.class);

  public GestioneValidazioneSIMAPTitleFunction() {
    super(2, new Class[] { PageContext.class, String.class });
  }

  public String function(PageContext pageContext, Object params[]) throws JspException {
    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);
    Long id = new Long((String) params[1]);

    String title_contract = "";

    try {
      String form = (String) sqlManager.getObject("select form from w3simap where id = ?", new Object[] { id });

      if (form != null && !"FS14".equals(form)) {
        title_contract = (String) sqlManager.getObject("select title_contract from v_w3simap where id = ?",
            new Object[] { id });

      } else {

        String selectW3SIMAP = "select v_w3simap.title_contract from v_w3simap, w3fs14 where v_w3simap.id = w3fs14.id_rif and w3fs14.id = ?";
        title_contract = (String) sqlManager.getObject(selectW3SIMAP, new Object[] { id });

      }

    } catch (SQLException e) {
      throw new JspException("Errore nella selezione del titolo", e);
    }

    return title_contract;
  }

}
