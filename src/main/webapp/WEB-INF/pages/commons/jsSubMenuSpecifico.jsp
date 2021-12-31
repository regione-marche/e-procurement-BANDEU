<%
  /*
   * Created on 20-Ott-2008
   *
   * Copyright (c) EldaSoft S.p.A.
   * Tutti i diritti sono riservati.
   *
   * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
   * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di
   * aver prima formalizzato un accordo specifico con EldaSoft.
   */

%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="listaOpzioniDisponibili" value="${fn:join(opzDisponibili,'#')}#"/>
<c:set var="listaOpzioniUtenteAbilitate" value="${fn:join(profiloUtente.funzioniUtenteAbilitate,'#')}#" />

<script type="text/javascript">


	var linksetSubMenuBandi = "";
	<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.BANDI.Trova-bandi")}'>
		linksetSubMenuBandi += creaVoceSubmenu("${contextPath}/ApriPagina.do?href=w3/w3simap/w3simap-trova.jsp", 1240, "Ricerca bandi ed avvisi");
	</c:if>
	<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.BANDI.Trova-invii")}'>
		linksetSubMenuBandi += creaVoceSubmenu("${contextPath}/ApriPagina.do?href=w3/w3simapemail/w3simapemail-trova.jsp", 1250, "Ricerca invii/notifiche");
	</c:if>
	<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.BANDI.Trova-invii-WS")}'>
		linksetSubMenuBandi += creaVoceSubmenu("${contextPath}/ApriPagina.do?href=w3/w3simapws/w3simapws-trova.jsp", 1250, "Ricerca invii/notifiche");
	</c:if>
	<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.ARCHIVI.Archivio-amministrazioni")}'>
		linksetSubMenuArchivi += creaVoceSubmenu("${contextPath}/ApriPagina.do?href=w3/w3ammi/w3ammi-trova.jsp", 1290, "Archivio amministrazioni");
	</c:if>
	<c:if test='${fn:contains(listaOpzioniDisponibili, "OP101#") && (fn:contains(listaOpzioniUtenteAbilitate, "ou11#") && !fn:contains(listaOpzioniUtenteAbilitate, "ou12#"))}'>
		<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.UTILITA.gestione-unita-gruppi")}'>
			linksetSubMenuUtilita += creaVoceSubmenu("${contextPath}/ApriPagina.do?href=w3/unit/unit-albero.jsp", 1300, "Gestione unit&agrave organizzative e gruppi");
		</c:if>		
	</c:if>

	function initPage(){
	}
	
</script>



