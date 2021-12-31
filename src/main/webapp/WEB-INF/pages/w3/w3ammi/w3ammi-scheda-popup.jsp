<%
/*
 * Created on: 06-ott-2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Scheda a popup del tecnico progettista */
%>

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<gene:template file="popup-template.jsp" gestisciProtezioni="true" schema="W3" idMaschera="W3AMMI-scheda-popup" >

	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>

	<c:set var="entita" value="W3AMMI" />
	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>
	<gene:redefineInsert name="pulsanteNuovo" />
	<gene:redefineInsert name="pulsanteModifica" />
	<gene:redefineInsert name="corpo">
		<jsp:include page="w3ammi-interno-scheda.jsp" />
 	 </gene:redefineInsert>

</gene:template>
