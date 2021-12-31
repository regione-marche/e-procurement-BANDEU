
<%
	/*
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

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div style="width:97%;">

<gene:template file="popup-template.jsp">

	<gene:redefineInsert name="addHistory" />
	<gene:redefineInsert name="gestioneHistory" />
	
	<c:set var="id" value="${param.id}" />
	<c:set var="phase" value="${param.phase}" />

	<c:set var="descrizione" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMAPTitleFunction",pageContext,id)}' />
	<c:set var="credenziali" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetSIMAPWSUserFunction",pageContext,id)}' />
	
	<c:if test="${fn:length(descrizione) > 200}">
		<c:set var="descrizione" value="${fn:substring(descrizione,0,200)} ..." />
	</c:if>
	
	<c:set var="alfa" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.simap.ws.alfa.url")}'/>
	<c:set var="beta" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.simap.ws.beta.url")}'/>
	<c:set var="gamma" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.simap.ws.gamma.url")}'/>

	
	<gene:setString name="titoloMaschera"  value='Genera documento PDF' />

	<gene:redefineInsert name="corpo">
		<form action="${contextPath}/w3/PdfSIMAPWS.do" method="post" name="formPdfSIMAPWS">
		
			<input type="hidden" name="entita" value="${param.entita}" />
			<input type="hidden" name="id" value="${param.id}" />
			
			<table class="dettaglio-notab">
			
				<tr>
					<td colspan="2">
						<br>
						Indicare la <b>fase</b> richiesta
						<br>
					</td>
				</tr>
				
				<tr id="rowphase">
					<td class="etichetta-dato">Fase (*)</td>
					<td class="valore-dato">
						<select id="phase" name="phase">
							<c:if test="${!empty alfa}">
								<option value="ALFA">Test</option>
							</c:if>
							<c:if test="${!empty beta}">
								<option value="BETA">Qualificazione</option>
							</c:if>
							<c:if test="${!empty gamma}">
								<option value="GAMMA">Produzione e pubblicazione</option>
							</c:if>
						</select>
						<input type="hidden" name="operation" id="operation" value="SUBMITNOTICE" />
					</td>
				</tr>
			
				<tr>
					<td colspan="2">
						<br>
						<b>Credenziali</b> di accesso al servizio
						<br>
					</td>
				</tr>
				<tr>	
					<td class="etichetta-dato">Utente/Password</td>
					<td class="valore-dato">
						<input type="text" name="simapwsuser" id="simapwsuser" size="20" value="${simapwsuser}" disabled="disabled"/>
						<input type="password" name="simapwspass" id="simapwspass" size="20" value="${simapwspassword}" disabled="disabled"/>
					</td>
				</tr>

				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>

				<tr>
					<td colspan="2" class="comandi-dettaglio">
						<INPUT type="button" class="bottone-azione" value="Genera documento PDF" title="Genera documento PDF" onclick="javascript:pdfSIMAPWS();">
						<INPUT type="button" class="bottone-azione" value="Chiudi" title="Chiudi" onclick="javascript:annulla();">&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</form>	
	</gene:redefineInsert>
	
	<gene:javaScript>
	
		<c:if test="${!empty p_phase}">
		    $("#phase option[value='${phase}']").attr('selected', 'selected');
		</c:if>
		
		function annulla(){
			window.close();
		}
		
		function pdfSIMAPWS(){
			if ($("#phase option:selected").val() == "" || $("#phase option:selected").val() == undefined) {
				alert("Indicare la fase.");
				invia = "false";
			} else if ($("#simapwsuser").val() == "") {
				alert("L'utente non e' valorizzato. Contattare l'amministratore di sistema.");
			} else if ($("#simapwspass").val() == "") {
				alert("La password non e' valorizzata. Contattare l'amministratore di sistema.");
			} else {
				document.formPdfSIMAPWS.submit();
			}				
		}

	
	</gene:javaScript>	
</gene:template>

</div>

