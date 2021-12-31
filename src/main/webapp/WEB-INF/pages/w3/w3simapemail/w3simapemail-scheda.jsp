
<%
	/*
	 * Created on 10-Nov-2010
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
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3SIMAPEMAIL-Scheda" schema="W3">
	<c:set var="entita" value="W3SIMAPEMAIL" />
	<gene:setString name="titoloMaschera" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.GetTitleW3SIMAPEMAILFunction",pageContext)}'/>		
	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3SIMAPEMAIL" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPEMAIL">
	
			<gene:campoScheda campo="ID" visibile="false" defaultValue='${gene:getValCampo(keyParent,"ID")}' />
			<gene:campoScheda title="N°" visibile="false" modificabile="false" campo="NUM" defaultValue='${gene:getValCampo(keyParent,"NUM")}' />
			<gene:campoScheda campo="DATA_CREAZIONE" modificabile="false" visibile="false"/>
			<gene:campoScheda campo="DATA_SPEDIZIONE_S" modificabile="false"/>
			<gene:campoScheda campo="EMAILFROM" visibile="false"/>
			<gene:campoScheda campo="EMAILPHASE" modificabile="false" obbligatorio="true" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoEMAILPHASE"/>			
			<gene:campoScheda campo="EMAILTO" modificabile="false" obbligatorio="true" />
			<gene:campoScheda campo="EMAILSUBJECT" modificabile="false"/>
			<gene:campoScheda campo="EMAILBODY" modificabile="false"/>
			<gene:campoScheda campo="ATTACHMENT_NAME" visibile="false"/>
			<gene:campoScheda title="Visualizza o scarica l'allegato">
				<c:choose>
					<c:when test="${modo eq 'VISUALIZZA' && !empty datiRiga.W3SIMAPEMAIL_ID && !empty datiRiga.W3SIMAPEMAIL_NUM && !empty datiRiga.W3SIMAPEMAIL_ATTACHMENT_NAME}">
						<a href="javascript:visualizzaAttachment('${datiRiga.W3SIMAPEMAIL_ID}','${datiRiga.W3SIMAPEMAIL_NUM}','${datiRiga.W3SIMAPEMAIL_ATTACHMENT_NAME}');" title="Visualizza o scarica l'allegato" >
							<img width="24" height="24" title="Visualizza o scarica l'allegato" alt="Visualizza o scarica l'allegato" src="${pageContext.request.contextPath}/img/8629505_5.gif"/>
						</a>
					</c:when>
					<c:otherwise>
						<img width="24" height="24" title="" alt="" src="${pageContext.request.contextPath}/img/8629505_5.gif"/>
					</c:otherwise>
				</c:choose>				
			</gene:campoScheda>
			<gene:campoScheda campo="NOTERISPOSTA" />
			
			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
			</gene:campoScheda>
			
			<gene:redefineInsert name="pulsanteNuovo" />
			<gene:redefineInsert name="schedaNuovo" />
			
			
		</gene:formScheda>
	</gene:redefineInsert>
	
	<gene:javaScript>
	
		function visualizzaAttachment(id,num,attachment_name) {
			var href = "${pageContext.request.contextPath}/w3/VisualizzaAttachment.do";
			document.location.href=href+"?id=" + id + "&num=" + num + "&attachment_name=" + attachment_name;
		}
		

	</gene:javaScript>

</gene:template>