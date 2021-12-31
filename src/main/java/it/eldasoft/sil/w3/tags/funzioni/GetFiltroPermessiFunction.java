package it.eldasoft.sil.w3.tags.funzioni;

import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.sil.w3.utils.UtilityPermessi;
import it.eldasoft.utils.spring.UtilitySpring;

public class GetFiltroPermessiFunction extends AbstractFunzioneTag {

  public GetFiltroPermessiFunction() {
    super(3, new Class[] { PageContext.class, String.class, String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", pageContext, SqlManager.class);
    ProfiloUtente profilo = (ProfiloUtente) this.getRequest().getSession().getAttribute(CostantiGenerali.PROFILO_UTENTE_SESSIONE);

    String tblname = (String) params[1];
    String clm1name = (String) params[2];

    String filtroPermessi;
    try {
      filtroPermessi = UtilityPermessi.getFiltroPermessi(profilo, tblname, clm1name, sqlManager);
    } catch (SQLException e) {
      throw new JspException("Errore nella creazione del filtro aggiuntivo sui permessi", e);
    }

    return filtroPermessi;

  }

}
