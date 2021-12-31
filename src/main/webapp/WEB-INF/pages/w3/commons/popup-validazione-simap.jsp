
<%
	/*
	 * Created on 15-lug-2008
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

<c:set var="profiloUtente" value="${sessionScope.profiloUtente}" scope="request"/>

<div style="width:95%;">

<gene:template file="popup-template.jsp">

	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery/dataTable/dataTable/jquery.dataTables.css" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>

	<gene:redefineInsert name="addHistory" />
	<gene:redefineInsert name="gestioneHistory" />
	
	<c:set var="id" value="${param.id}" />

	<c:set var="descrizione" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMAPTitleFunction",pageContext,id)}' />
	<c:if test="${fn:length(descrizione) > 200}">
		<c:set var="descrizione" value="${fn:substring(descrizione,0,200)} ..." />
	</c:if>
	<gene:setString name="titoloMaschera"  value='${descrizione}' />
	
	<gene:redefineInsert name="corpo">
		<table class="lista">
			<c:set var="stato" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMAPFunction",pageContext,id,profiloUtente.id)}'/>		
			<gene:set name="titoloGenerico" value="${titolo}" />
			<gene:set name="listaGenericaControlli" value="${listaControlli}" />
			<gene:set name="numeroErrori" value="${numeroErrori}" />
			<gene:set name="numeroWarning" value="${numeroWarning}" />
			
			<c:choose>
				<c:when test="${!empty listaControlli}">
					<jsp:include page="../commons/popup-validazione-interno.jsp" />	
				</c:when>
				
				<c:otherwise>
					<tr>
						<td>
							<b>${titoloGenerico}</b>
							<br>
							<br>
							Non è stato rilevato alcun problema.
							<br>
							<br>				
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			
			<tr>
				<td class="comandi-dettaglio" colspan="2">
					<c:if test="${!empty listaControlli}">
						<INPUT type="button" class="bottone-azione" value="Controlla nuovamente" title="Controlla nuovamente" onclick="javascript:controlla()">
					</c:if>
					<INPUT type="button" class="bottone-azione" value="Chiudi" title="Chiudi" onclick="javascript:annulla()">&nbsp;
				</td>
			</tr>
			
		</table>

	</gene:redefineInsert>
	
<gene:javaScript>

	window.opener.currentPopUp=null;
	
    window.onfocus=resettaCurrentPopup;

	function resettaCurrentPopup() {
		window.opener.currentPopUp=null;
	}
	
	function annulla(){
		window.close();
	}

	function controlla(){
		window.location.reload();
	}
	
	 $('#tablevalidazionew3').DataTable( {
	 	"language": {
	 		"sInfo": "Visualizzazione da _START_ a _END_ di _TOTAL_ ",
	 		"sLengthMenu":  "Visualizza _MENU_ righe",
			"oPaginate": {
				"sFirst":      "Prima",
				"sPrevious":   "Precedente",
				"sNext":       "Successiva",
				"sLast":       "Ultima"
			}
		},
		"pagingType": "simple_numbers",
	    "lengthMenu": [[7, 10, 15, 20, 999], ["7 righe", "10 righe", "15 righe", "20 righe", "tutte le righe"]],
        "ordering": false,
        "info": false
    });
    
     $("#tablevalidazionew3_length").hide();
     $("#tablevalidazionew3_filter").hide();

</gene:javaScript>	
	
</gene:template>

</div>
