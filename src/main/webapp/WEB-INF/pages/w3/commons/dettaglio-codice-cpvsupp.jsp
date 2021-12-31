<%
/*
     * Created on: 14/10/2010
     *
     * Copyright (c) EldaSoft S.p.A.
     * Tutti i diritti sono riservati.
     *
     * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
     * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
     * aver prima formalizzato un accordo specifico con EldaSoft.

     Descrizione:
		Selezione del dettaglio delle categoria dell'intervento
		Parametri:
			param.modo 
				Modo di apertura MODIFICA per modifica o VISUALIZZA per visualizzazione
			param.campo 
				Nome del campo in cui vi è il dettaglio
			param.valore 
				Valore del campo

	*/
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda" %>
<c:choose>
	<c:when test='${empty param.valore}'>
		<c:set var="valore" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="valore" value="${param.valore}" />
		<c:if test="${fn:length(valore) >= 4}">
			<gene:sqlSelect nome="outCodice" tipoOut="VectorString" >
				select codice from tabcpvsupp where codice like '${fn:substring( param.valore, 0,4 )}%'
			</gene:sqlSelect>
			<c:set var="valore" value="${outCodice[0]}" />		
		</c:if>
	</c:otherwise>
</c:choose>

<gene:template file="popup-template.jsp">
	<gene:setString name="titoloMaschera" value="Dettaglio codice CPV (Vocabolario supplementare)"/>
	<gene:redefineInsert name="corpo">
		<gene:formScheda gestisciProtezioni="false" entita="">
			<gene:campoScheda campo="CODICE" nome="CODICE" title="Codice CPV (Vocabolario supplementare)" modificabile="false" definizione="T6" value="${valore}"/>
			<gene:campoScheda><td colspan="2">&nbsp;</td></gene:campoScheda>
			<gene:campoScheda campo="SEZIONE" nome="SEZIONE" title="Sezione" definizione="T1" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPVSUPP_SEZIONE" 
				value="${fn:substring( valore, 0,1 )}" />
			<gene:campoScheda campo="GRUPPO" nome="GRUPPO" title="Gruppo" definizione="T1" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPVSUPP_GRUPPO"
				value="${fn:substring( valore, 1,2 )}" />
			<gene:campoScheda campo="DIVISIONE" nome="DIVISIONE" title="Divisione" definizione="T2" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPVSUPP_DIVISIONE" 
				value="${fn:substring( valore, 2,4 )}" />
			<gene:campoScheda visibile='${param.modo eq "MODIFICA"}'>
				<td class="comandi-dettaglio" colSpan="2">	
						<INPUT type="button" class="bottone-azione" value="Conferma" title="Salva modifiche" onclick="javascript:conferma()" />
						<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla modifiche" onclick="javascript:annulla()" />
				</td>			
			</gene:campoScheda>

			<gene:fnJavaScriptScheda funzione='modifySezione()' elencocampi="SEZIONE" esegui="false" />
			<gene:fnJavaScriptScheda funzione='modifyGruppo()' elencocampi="GRUPPO" esegui="false" />
			<gene:fnJavaScriptScheda funzione='modifyDivisione()' elencocampi="DIVISIONE" esegui="false" />
			
			<gene:fnJavaScriptScheda funzione='reloadCPVSUPP()' elencocampi="CODICE" esegui="false" />
			
		</gene:formScheda>
		
  </gene:redefineInsert>

	<gene:javaScript>
	
		function modifySezione(){
			setValue("GRUPPO","", false);
			setValue("DIVISIONE","", false);
			calcolaCPVSUPP();
		}


		function modifyGruppo(){
			setValue("DIVISIONE","", false);
			calcolaCPVSUPP();
		}
		
		
		function modifyDivisione(){
			calcolaCPVSUPP();
		}

		
		function calcolaCPVSUPP(){
			var valore="";
			if(getValue("SEZIONE")!=""){
				valore+=getValue("SEZIONE");
				valore+=getValue("GRUPPO");
				valore+=getValue("DIVISIONE");
			}
			setValue("CODICE",valore);
		}
		
		function reloadCPVSUPP() {
			bloccaRichiesteServer();
			document.location.href="${pageContext.request.contextPath}/ApriPagina.do?href=w3/commons/dettaglio-codice-cpvsupp.jsp&modo=MODIFICA&campo=${param.campo}&valore="+getValue("CODICE");
		}
		
		function conferma(){
			if (getValue("SEZIONE") != "" && (getValue("GRUPPO") == "" || getValue("DIVISIONE") == "")) {
				alert("Per procedere è necessario indicare tutti i livelli del codice CPV supplementare.");
			} else {
				opener.setValue("${param.campo}",getValue("CODICE"));
				window.close();
			}
		}
		
		function annulla(){
			window.close();
		}

	</gene:javaScript>
</gene:template>

