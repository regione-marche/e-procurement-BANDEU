<%
  /*
   * Created on 20-ott-2008
   *
   * Copyright (c) EldaSoft S.p.A.
   * Tutti i diritti sono riservati.
   *
   * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
   * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di
   * aver prima formalizzato un accordo specifico con EldaSoft.
   */

%>

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="isNavigazioneDisattiva" value="${isNavigazioneDisabilitata}" />			

<c:if test='${gene:checkProt(pageContext,"MENU.VIS.BANDI")}'>
 	  <td>
  	  <c:choose>
	  	  <c:when test='${isNavigazioneDisattiva eq "1"}'>
		  		<span><c:out value="Bandi ed avvisi" /></span>
		  	</c:when>
		  	<c:otherwise>
		  	  <a id="lnavbarBandi" href="javascript:showSubmenuNavbar('lnavbarBandi',linksetSubMenuBandi);" tabindex="1000">Bandi ed avvisi</a>
		  	</c:otherwise>
  	  </c:choose>
 	  </td>
</c:if>
