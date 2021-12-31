
<%
	/*
	 * Created on 15-nov-2010
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

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<gene:template file="popup-template.jsp">

	<gene:redefineInsert name="addHistory" />
	<gene:redefineInsert name="gestioneHistory" />
	
	<c:choose>
		<c:when test='${RISULTATO eq "COMPOSIZIONEESEGUITA"}'>
			<c:set var="id" value='${ID}' />
			<c:set var="num" value='${NUM}' />
		</c:when>
		<c:when test='${RISULTATO eq "ERRORI"}'>
			<c:set var="id" value='${ID}' />
			<c:set var="num" value='' />
		</c:when>
		<c:otherwise>
			<c:set var="id" value="${param.id}" />
			<c:set var="num" value='' />
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test='${RISULTATO eq "COMPOSIZIONEESEGUITA"}'>
			<c:set var="modo" value="APRI" scope="request" />
			<c:set var="key" value="W3SIMAPEMAIL.ID=N:${id};W3SIMAPEMAIL.NUM=N:${num}" />
		</c:when>
		<c:otherwise>
			<c:set var="modo" value="NUOVO" scope="request" />
		</c:otherwise>
	</c:choose>
	
	<c:set var="descrizione" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMAPTitleFunction",pageContext,id)}' />
	<gene:setString name="titoloMaschera"  value='${descrizione} - Invio del messaggio di posta elettronica' />
	
	<c:set var="listaIndirizzi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.ListaIndirizziSIMAPFunction",pageContext)}' />
	
	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3SIMAPEMAIL" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPEMAIL">

			<gene:campoScheda visibile="${!empty RISULTATO}">
				<c:if test='${RISULTATO eq "COMPOSIZIONEESEGUITA"}'>
					<td colSpan="2">
						<br>
						La composizione e l'invio del messaggio di posta elettronica è avvenuta con successo.
						<br>
						<br>
					</td>
				</c:if>
			</gene:campoScheda>

			<gene:campoScheda campo="ID" visibile="false" defaultValue='${id}' />
			<gene:campoScheda campo="NUM" visibile="false" />
			
			<gene:campoScheda>
				<td colspan="2">
					&nbsp;
				</td>
			</gene:campoScheda>
			
			<gene:campoScheda title="Classe eSender" modificabile="false" campo="TEDESENDERCLASS" campoFittizio="true" definizione="T1" value="${tedsenderclass}" />
			<gene:campoScheda campo="EMAILPHASE" modificabile="true" obbligatorio="true" defaultValue="GAMMA" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoEMAILPHASE"/>
			<gene:campoScheda>
				<td colspan="2">
					<br>
					<u>Fase ALFA</u>
					<br>
					Invio dei dati, in formato XML, al team di supporto TED eSender. <br><i>${destinatario_alfa}</i> 
					<br><br>
					<u>Fase BETA</u>
					<br>
					Invio dei dati di test all'ambiente di test dell'Ufficio Pubblicazioni della Comunit&agrave; Europea. <br><i>${destinatario_beta}</i> 
					<br><br>
					<u>Fase GAMMA</u>
					<br>
					Invio di dati reali all'ambiente di produzione dell'Ufficio Pubblicazioni della Comunit&agrave; Europea.
					<br><i>${destinatario_gamma}</i> 
					<br><br><u>I dati saranno automaticamente elaborati e pubblicati.</u>
					<br><br>
				</td>
			</gene:campoScheda>

			<gene:campoScheda>
				<c:choose>
					<c:when test='${!empty RISULTATO}'>
						<td class="comandi-dettaglio" colSpan="2">
							<INPUT type="button" class="bottone-azione" value="Chiudi" title="Chiudi" onclick="javascript:annulla();">&nbsp;&nbsp;
						</td>
					</c:when>
					<c:otherwise>
						<td class="comandi-dettaglio" colSpan="2">
							<INPUT type="button" class="bottone-azione" value="Invia" title="Invia" onclick="javascript:invia();">
							<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla" onclick="javascript:annulla();">&nbsp;&nbsp;
						</td>
					</c:otherwise>
				</c:choose>
			</gene:campoScheda>
					
		</gene:formScheda>
	</gene:redefineInsert>
	
	<gene:javaScript>
	
		showObj("jsPopUpW3SIMAPEMAIL_EMAILPHASE", false);
	
		document.forms[0].jspPathTo.value="w3/w3simapemail/w3simapemail-popup-invia.jsp";
	
	    <c:if test='${RISULTATO eq "COMPOSIZIONEESEGUITA"}'>
	    	window.opener.apriInvioSIMAP(${id},${num});
		</c:if>
		
		function annulla(){
			window.close();
		}
		
		function invia(){
			schedaConferma();
		}
	
		
	</gene:javaScript>	
</gene:template>


</div>

