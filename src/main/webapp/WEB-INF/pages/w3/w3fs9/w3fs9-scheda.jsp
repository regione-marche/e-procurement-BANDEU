<%/*
       * Created on 08-Ott-2010
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

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${contextPath}/js/funzioniSIMAP.js?v=${sessionScope.versioneModuloAttivo}"></script>

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3FS9-scheda" schema="W3">

	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvsupp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery/cpvvp/jquery.cpvvp.mod.css" >
	</gene:redefineInsert>

	<c:set var="entita" value="W3FS9" />
	<c:set var="id" value='${gene:getValCampo(key,"ID")}' scope="request" />
	<c:set var="titoloCompleto" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>
	<c:choose>
		<c:when test='${fn:length(titoloCompleto) > 90}'>
			<gene:setString name="titoloMaschera" value='${fn:substring(titoloCompleto,0,90)}...'/>
		</c:when>
		<c:otherwise>
			<gene:setString name="titoloMaschera" value='${titoloCompleto}' /> 
		</c:otherwise>
	</c:choose>
	<gene:redefineInsert name="corpo">
		<gene:formPagine gestisciProtezioni="true">
			<gene:pagina title="Sez. I e II: Amministrazione ed oggetto" idProtezioni="W3FS9_1-2">
				<jsp:include page="w3fs9-pg-sezione1-2.jsp" />
			</gene:pagina>
			<gene:pagina title="Sez. IV: Procedura" idProtezioni="W3FS9_4-6">
				<jsp:include page="w3fs9-pg-sezione4-6.jsp" />
			</gene:pagina>
		</gene:formPagine>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="addToAzioni" >
		<jsp:include page="/WEB-INF/pages/w3/commons/addtoazioni-simap.jsp" >
			<jsp:param name="formulario" value='W3FS9'/>
		</jsp:include>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="addToDocumenti" >
		<jsp:include page="/WEB-INF/pages/w3/commons/addtodocumenti-simap.jsp" >
			<jsp:param name="formulario" value='W3FS9'/>
		</jsp:include>
	</gene:redefineInsert>
	
</gene:template>
<jsp:include page="../commons/include-formListaW3SIMAPEMAILWS.jsp" />
