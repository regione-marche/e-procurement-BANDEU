<%
/*
 * Created on: 24-ott-2007
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Finanziamenti del lavoro */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="isCodificaAutomatica" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.IsCodificaAutomaticaFunction", pageContext, "W3AMMI", "CODAMM")}'/>

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" schema="W3" idMaschera="W3AMMI-scheda">
	
	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>
	
	<c:set var="entita" value="W3AMMI"/>
	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>
	<gene:redefineInsert name="corpo">
		<jsp:include page="w3ammi-interno-scheda.jsp" />
	</gene:redefineInsert>
</gene:template>





