
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


<div style="width:97%;">

	<gene:template file="popup-template.jsp">
	
		<gene:redefineInsert name="addHistory" />
		<gene:redefineInsert name="gestioneHistory" />
	
		<c:set var="scheda_id" value="${param.scheda_id}" />
		<c:set var="descrizioneContratto" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneTitleFunction",pageContext,scheda_id)}' />
		<gene:setString name="titoloMaschera" value='${descrizioneContratto}' />
		<c:set var="statoDatiComuni" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMOGDatiComuniFunction",pageContext,scheda_id)}'/>
		<c:set var="statoListaFasi" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneListaFasiFunction",pageContext,scheda_id)}'/>
		<c:set var="contaListaControlli" value="0" />

		<gene:redefineInsert name="corpo">
			<table class="lista">
				<tr>
					<td>
						In questa pagina è presente il risultato dei controlli effettuati 
						sui dati comuni, sulle fasi e sulle anagrafiche del contratto <b>${descrizioneContratto}</b>.
						<br>
						<br>Le fasi controllate sono esclusivamente quelle il cui <u>stato di compilazione è completato</u>.
						<br> 
						<br>
					</td>
				</tr>
				
				<c:if test="${!empty listaControlliDatiComuni}">
					<gene:set name="titoloGenerico" value="Dati comuni" />
					<gene:set name="listaGenericaControlli" value="${listaControlliDatiComuni}" />
					<gene:set name="numeroErrori" value="${numeroErroriDatiComuni}" />
					<gene:set name="numeroWarning" value="${numeroWarningDatiComuni}" />
					<jsp:include page="../commons/popup-validazione-interno.jsp" />	
					<tr>
						<td>
							&nbsp;						
						</td>
					</tr>
					<c:set var="contaListaControlli" value='${contaListaControlli + 1}' />			
				</c:if>
				
				<c:if test="${!empty listaFasi}">
					<c:forEach items="${listaFasi}" step="1" var="fase" varStatus="status">
						<c:set var="schedacompleta_id" value="${fase[0]}" />
						<c:set var="fase_esecuzione" value="${fase[1]}" />
						<c:set var="num" value="${fase[2]}" />	
						<c:set var="stato" value='${gene:callFunction5("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMOGFunction",pageContext,scheda_id,schedacompleta_id,fase_esecuzione,num)}'/>		
						<c:if test="${!empty listaControlli}">
							<gene:set name="titoloGenerico" value="${titolo}" />
							<gene:set name="listaGenericaControlli" value="${listaControlli}" />
							<gene:set name="numeroErrori" value="${numeroErrori}" />
							<gene:set name="numeroWarning" value="${numeroWarning}" />
							<jsp:include page="../commons/popup-validazione-interno.jsp" />
							<tr>
								<td>
									&nbsp;						
								</td>
							</tr>
							<c:set var="contaListaControlli" value='${contaListaControlli + 1}' />	
						</c:if>
					</c:forEach>
				</c:if>
				
				<c:if test="${!empty listaControlliAggiudicatari}">
					<gene:set name="titoloGenerico" value="Archivio delle imprese" />
					<gene:set name="listaGenericaControlli" value="${listaControlliAggiudicatari}" />
					<gene:set name="numeroErrori" value="${numeroErroriAggiudicatari}" />
					<gene:set name="numeroWarning" value="${numeroWarningAggiudicatari}" />
					<jsp:include page="../commons/popup-validazione-interno.jsp" />	
					<tr>
						<td>
							&nbsp;						
						</td>
					</tr>
					<c:set var="contaListaControlli" value='${contaListaControlli + 1}' />				
				</c:if>
				
				<c:if test="${!empty listaControlliResponsabili}">
					<gene:set name="titoloGenerico" value="Archivio degli incaricati" />
					<gene:set name="listaGenericaControlli" value="${listaControlliResponsabili}" />
					<gene:set name="numeroErrori" value="${numeroErroriResponsabili}" />
					<gene:set name="numeroWarning" value="${numeroWarningResponsabili}" />
					<jsp:include page="../commons/popup-validazione-interno.jsp" />
					<tr>
						<td>
							&nbsp;						
						</td>
					</tr>
					<c:set var="contaListaControlli" value='${contaListaControlli + 1}' />					
				</c:if>
				
				<c:if test="${contaListaControlli eq '0'}">
					<tr>
						<td>
							<b>Non è stato rilevato alcun problema.</b>
							<br>&nbsp;<br> 						
						</td>
					</tr>
				</c:if>
				
				<tr>
					<td class="comandi-dettaglio">
						<INPUT type="button" class="bottone-azione" value="Controlla nuovamente" title="Controlla nuovamente" onclick="javascript:controlla()">
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
		
		</gene:javaScript>	
		
	</gene:template>

</div>
