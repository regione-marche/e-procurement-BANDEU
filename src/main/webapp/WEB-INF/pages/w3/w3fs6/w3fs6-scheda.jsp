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

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3FS6-scheda" schema="W3">

	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvsupp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.nuts.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery/cpvvp/jquery.cpvvp.mod.css" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >		
	</gene:redefineInsert>
	
	<c:set var="entita" value="W3FS6" />
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
		<c:set var="typeProcedure" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTipoProceduraFS6Function", pageContext, key)}' />
		<c:set var="divIntoLots" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetDivIntoLotsFS6Function", pageContext, key)}' />
		<gene:formPagine gestisciProtezioni="true">
			<gene:pagina title="Sez. I: Amministrazione aggiudicatrice" idProtezioni="W3FS6_1">
				<jsp:include page="w3fs6-pg-sezione1.jsp" />
			</gene:pagina>
			<gene:pagina title="Sez. II: Oggetto (II.1)" idProtezioni="W3FS6_2">
				<jsp:include page="w3fs6-pg-sezione2-1.jsp" />
			</gene:pagina>
			<gene:pagina title="Sez. II: Oggetto (II.2)" idProtezioni="W3FS6_2_1" visibile="${divIntoLots ne '1'}">
				<jsp:include page="w3fs6-pg-sezione2-2.jsp" />
			</gene:pagina>
			<gene:pagina title="Sez. II: Oggetto (II.2)" idProtezioni="W3FS6_2_2" visibile="${divIntoLots eq '1'}">
				<jsp:include page="w3fs6-pg-lista-sezione2-2.jsp" />
			</gene:pagina>			
			<gene:pagina title="Sez. IV: Procedura" idProtezioni="W3FS6_4">
				<jsp:include page="w3fs6-pg-sezione4.jsp" />
			</gene:pagina>
			<gene:pagina title="Sez. V: Aggiudicazione dell'appalto" idProtezioni="W3FS6_5_1" visibile="${divIntoLots ne '1'}">
				<jsp:include page="w3fs6-pg-sezione5.jsp" />
			</gene:pagina>				
			<gene:pagina title="Sez. V: Aggiudicazione dell'appalto" idProtezioni="W3FS6_5_2" visibile="${divIntoLots eq '1'}">
				<jsp:include page="w3fs6-pg-lista-sezione5.jsp" />
			</gene:pagina>			
			<gene:pagina title="Sez. VI: Altre informazioni" idProtezioni="W3FS6_6">
				<jsp:include page="w3fs6-pg-sezione6.jsp" />
			</gene:pagina>
			<gene:pagina title="Allegato D2" idProtezioni="W3FS6_ANNEXD" selezionabile="${typeProcedure eq '8'}">
				<jsp:include page="w3fs6-pg-annexd.jsp" />
			</gene:pagina>
		</gene:formPagine>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="addToAzioni" >
		<jsp:include page="/WEB-INF/pages/w3/commons/addtoazioni-simap.jsp" >
			<jsp:param name="formulario" value='W3FS6'/>
		</jsp:include>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="addToDocumenti" >
		<jsp:include page="/WEB-INF/pages/w3/commons/addtodocumenti-simap.jsp" >
			<jsp:param name="formulario" value='W3FS6'/>
		</jsp:include>
	</gene:redefineInsert>
	
	<gene:javaScript>
		<c:if test="${modo ne 'NUOVO'}">
			_getStatoValidazioneSIMAP(${id});
		</c:if>
	</gene:javaScript>
	
</gene:template>
<jsp:include page="../commons/include-formListaW3SIMAPEMAILWS.jsp" />
