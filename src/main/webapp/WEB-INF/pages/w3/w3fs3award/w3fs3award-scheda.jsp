
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

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3FS3AWARD-Scheda" schema="W3">

	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/funzioniSIMAP.js?v=${sessionScope.versioneModuloAttivo}"></script>
	</gene:redefineInsert>

	<c:set var="entita" value="W3FS3AWARD" />
	<c:set var="w3fs3award_id" value='${gene:getValCampo(key,"ID")}' scope="request" />
	<c:set var="w3fs3award_item" value='${gene:getValCampo(key,"ITEM")}' scope="request" />
	
	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>

	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3FS3AWARD" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS3AWARD">
			<gene:campoScheda entita="W3FS3AWARD" campo="ID" visibile="false" defaultValue='${gene:getValCampo(keyParent,"ID")}'/>
			<gene:campoScheda entita="W3FS3AWARD" campo="ITEM" visibile="false" />
			
			<jsp:include page="w3fs3award-interno-scheda.jsp" >
				<jsp:param value='${gene:getValCampo(key, "ID")}' name="w3fs3award_id"/>
				<jsp:param value='${gene:getValCampo(key, "ITEM")}' name="w3fs3award_item"/>
			</jsp:include>
					
			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
					<jsp:param name="tblname" value="W3SIMAP"/>
					<jsp:param name="clm1name" value="ID"/>
					<jsp:param name="clm1value" value="${datiRiga.W3FS3AWARD_ID}"/>
				</jsp:include>
			</gene:campoScheda>	
			
			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
			</gene:campoScheda>
		
		</gene:formScheda>
		
		<gene:javaScript>
			<c:if test="${modo ne 'NUOVO'}">
				_getStatoValidazioneW3FS3AWARDSIMAP(${w3fs3award_id},${w3fs3award_item});
			</c:if>
		</gene:javaScript>
		
	</gene:redefineInsert>
</gene:template>