
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
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery/cpvvp/jquery.cpvvp.mod.css" >
	</gene:redefineInsert>

	<c:set var="entita" value="W3ANNEXB" />
	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>

	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3ANNEXB" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3ANNEXB">
	
			<gene:campoScheda campo="ID" visibile="false" defaultValue='${gene:getValCampo(keyParent,"ID")}'/>
			<gene:campoScheda campo="NUM" visibile="false"/>
		
			<gene:campoScheda>
				<td colspan="2"><b><br>${gene:resource("label.simap.annexb")}<br><br></b></td>
			</gene:campoScheda>
			<gene:campoScheda campo="TITLE" obbligatorio="true"/>
			<gene:campoScheda campo="CIG" />
			
			<gene:campoScheda>
				<td colspan="2"><b><br>${gene:resource("label.simap.annexb.1")}</b></td>
			</gene:campoScheda>
			<gene:campoScheda campo="DESCRIPTION" />
			
			<gene:campoScheda>
				<td colspan="2"><b><br>${gene:resource("label.simap.annexb.2")}</b></td>
			</gene:campoScheda>
			<jsp:include page="/WEB-INF/pages/w3/w3cpv/w3cpv-interno-scheda.jsp">
				<jsp:param name="ent" value='W3ANNEXB'/>
				<jsp:param name="id" value='${gene:getValCampo(key, "ID")}'/>
				<jsp:param name="num" value='${gene:getValCampo(key, "NUM")}'/>
			</jsp:include>
			
			<gene:campoScheda>
				<td colspan="2"><b><br>${gene:resource("label.simap.annexb.3")}</b></td>
			</gene:campoScheda>
			<gene:campoScheda campo="TOTAL" />
			<gene:campoScheda campo="COST" />
			<gene:campoScheda campo="LOW" />
			<gene:campoScheda campo="HIGH" />
			
			<c:choose>
				<c:when test="${entitaRiferimento eq 'W3FS1'}">
					<c:set var="titoloSezione" value='${gene:resource("label.simap.annexb.4.fs1")}'/>
					<c:set var="dataInizio" value="Data di inizio"/>
					<c:set var="dataFine" value="Data di fine"/>
				</c:when>
				<c:otherwise>
					<c:set var="titoloSezione" value='${gene:resource("label.simap.annexb.4.fs2")}'/>
					<c:set var="dataInizio" value="Data prevista per l'inizio dei lavori"/>
					<c:set var="dataFine" value="Data prevista per la fine dei lavori"/>
				</c:otherwise>
			</c:choose>
		
			<gene:campoScheda>
				<td colspan="2"><b><br>${titoloSezione}</b></td>
			</gene:campoScheda>
			<gene:campoScheda campo="PROCEDURE_DATE_STARTING" visibile="${entitaRiferimento eq 'W3FS1'}" />
			<gene:campoScheda campo="WORK_MONTH" />
			<gene:campoScheda campo="WORK_DAYS" />
			<gene:campoScheda title="${dataInizio}" campo="WORK_START_DATE" />
			<gene:campoScheda title="${dataFine}" campo="WORK_END_DATE" />
			
			<gene:campoScheda>
				<td colspan="2"><b><br>${gene:resource("label.simap.annexb.5")}</b></td>
			</gene:campoScheda>
			<gene:campoScheda campo="ADDITIONAL_INFORMATION" />
			
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
		
		<gene:javaScript>
			
		</gene:javaScript>
		
	</gene:redefineInsert>

</gene:template>