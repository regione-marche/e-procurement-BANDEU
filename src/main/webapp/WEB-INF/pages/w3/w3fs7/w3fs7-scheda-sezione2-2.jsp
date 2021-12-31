
<%
	/*
	 * Created on 08-Ott-2010
	 *
	 * Copyright (c) EldaSoft S.p.A.
	 * Tutti i diritti sono riservati.
	 *
	 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
	 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
	 * aver prima formalizzato un accordo specifico con EldaSoft.
	 */

	// Scheda degli intestatari della concessione stradale
%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3ANNEXB-Scheda" schema="W3">
	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvsupp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.nuts.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/funzioniSIMAP.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery/cpvvp/jquery.cpvvp.mod.css" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>

	<c:set var="entita" value="W3ANNEXB" />
	<c:set var="w3annexb_id" value='${gene:getValCampo(key,"ID")}' scope="request" />
	<c:set var="w3annexb_num" value='${gene:getValCampo(key,"NUM")}' scope="request" />
	
	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>
	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3ANNEXB" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3ANNEXB">
			<gene:campoScheda campo="ID" visibile="false" defaultValue='${gene:getValCampo(keyParent,"ID")}'/>
			<gene:campoScheda campo="NUM" visibile="false"/>

			<jsp:include page="w3fs7-interno-sezione2-2.jsp">
				<jsp:param value='${gene:getValCampo(key, "ID")}' name="w3annexb_id"/>
				<jsp:param value='${gene:getValCampo(key, "NUM")}' name="w3annexb_num"/>
			</jsp:include>

			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
					<jsp:param name="tblname" value="W3SIMAP"/>
					<jsp:param name="clm1name" value="ID"/>
					<jsp:param name="clm1value" value="${datiRiga.W3ANNEXB_ID}"/>
				</jsp:include>
			</gene:campoScheda>	
			
			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
			</gene:campoScheda>

		</gene:formScheda>
	</gene:redefineInsert>
	
		<gene:javaScript>
		<c:if test="${modo ne 'NUOVO'}">
			_getStatoValidazioneW3ANNEXBSIMAP(${w3annexb_id},${w3annexb_num});
		</c:if>
	</gene:javaScript>
	
</gene:template>