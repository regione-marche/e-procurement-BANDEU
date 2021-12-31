package it.eldasoft.sil.w3.tags.funzioni;

import it.eldasoft.gene.tags.utils.AbstractFunzioneTag;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.bl.GestioneServiziSIMAPManager;
import it.eldasoft.utils.spring.UtilitySpring;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class GetSIMAPWSUserFunction extends AbstractFunzioneTag {

  public GetSIMAPWSUserFunction() {
    super(2, new Class[] { PageContext.class, String.class });
  }

  public String function(PageContext pageContext, Object[] params) throws JspException {

    Long id = new Long((String) params[1]);

    GestioneServiziSIMAPManager gestioneServiziSIMAPManager = (GestioneServiziSIMAPManager) UtilitySpring.getBean(
        "gestioneServiziSIMAPManager", pageContext, GestioneServiziSIMAPManager.class);

    String simapwsuser = "";
    String simapwspassword = "";
    
    try {
      HashMap<String, String> hMapSIMAPWSUserPass = new HashMap<String, String>();
      hMapSIMAPWSUserPass = gestioneServiziSIMAPManager.recuperaSIMAPWSUserPass(id);
      simapwsuser = ((String) hMapSIMAPWSUserPass.get("simapwsuser"));
      if ((String) hMapSIMAPWSUserPass.get("simapwspass") != null) {
        simapwspassword = "PWDPWDPWD";
      }
    } catch (GestoreException e) {
      throw new JspException("Errore nella lettura della username per l'accesso al web service SIMAP", e);
    }

    pageContext.setAttribute("simapwsuser", simapwsuser, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("simapwspassword", simapwspassword, PageContext.REQUEST_SCOPE);

    return null;

  }

}
