<%
			/*
       * Created on: 16.15 14/03/2007
       *
       * Copyright (c) EldaSoft S.p.A.
       * Tutti i diritti sono riservati.
       *
       * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
       * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
       * aver prima formalizzato un accordo specifico con EldaSoft.
       */
      /*
				Descrizione:
					Selezione del dettaglio delle categoria dell'intervento
					Parametri:
						param.modo 
							Modo di apertura MODIFICA per modifica o VISUALIZZA per visualizzazione
						param.campo 
							Nome del campo in cui vi è il dettaglio
						param.valore 
							Valore del campo
				Creato da:
					Marco Franceschin
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
		<gene:sqlSelect nome="outSel" tipoOut="VectorString" >
			select cpvcod4 from tabcpv where cpvcod='S2020' and cpvcod4 like '${fn:substring( param.valore, 0,8 )}%'
		</gene:sqlSelect>
		<c:set var="valore" value="${outSel[0]}" />
	</c:otherwise>
</c:choose>

<gene:template file="popup-template.jsp">
	<gene:setString name="titoloMaschera" value="Dettaglio codice CPV (Vocabolario principale)"/>
	<gene:redefineInsert name="corpo">
		<gene:formScheda gestisciProtezioni="false" entita="">
			<gene:campoScheda campo="CPVCOD" nome="CPVCOD" title="Codice C.P.V." definizione="T11" value="${valore}"/>
			<gene:campoScheda><td colspan="2">&nbsp;</td></gene:campoScheda>
			<gene:campoScheda campo="CPVCOD0" nome="CPVCOD0" title="" definizione="T2" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPV_TABCOD0" 
				value="${fn:substring( valore, 0,2 )}" />
			<gene:campoScheda campo="CPVCOD1" nome="CPVCOD1" title="" definizione="T2" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPV_TABCOD1"
				value="${fn:substring( valore, 2,4 )}" />
			<gene:campoScheda campo="CPVCOD2" nome="CPVCOD2" title="" definizione="T2" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPV_TABCOD2" 
				value="${fn:substring( valore, 4,6 )}" />
			<gene:campoScheda campo="CPVCOD3" nome="CPVCOD3" title="" definizione="T2" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoTABCPV_TABCOD3" 
				value="${fn:substring( valore, 6,8 )}" />
			<gene:campoScheda visibile='${param.modo eq "MODIFICA"}'>
				<td class="comandi-dettaglio" colSpan="2">	
						<INPUT type="button" class="bottone-azione" value="Conferma" title="Salva modifiche" onclick="javascript:conferma()" />
						<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla modifiche" onclick="javascript:annulla()" />
				</td>			
			</gene:campoScheda>
			
			<%/* Aggiunta dei calcoli sui campi */%>
			<gene:fnJavaScriptScheda funzione='modifyValue(0)' elencocampi="CPVCOD0" esegui="false" />
			<gene:fnJavaScriptScheda funzione='modifyValue(1)' elencocampi="CPVCOD1" esegui="false" />
			<gene:fnJavaScriptScheda funzione='modifyValue(2)' elencocampi="CPVCOD2" esegui="false" />
			<gene:fnJavaScriptScheda funzione='modifyValue(3)' elencocampi="CPVCOD3" esegui="false" />
			
			<gene:fnJavaScriptScheda funzione='reloadCPV()' elencocampi="CPVCOD" esegui="false" />

		</gene:formScheda>
		
  </gene:redefineInsert>

	<gene:javaScript>
	
		function modifyValue(numcampo){
			// Sbianco tutti i campi successivi e leggo i valori
			var i;
			for(i=numcampo+1;i<4;i++){
				setValue("CPVCOD"+i,"", false);
			}
			calcolaCPV();
		}
		
		function calcolaCPV(){
			var valore="";
			if(getValue("CPVCOD0")!=""){
				valore+=fillCharLeft(getValue("CPVCOD0"),2,"0");
				valore+=fillCharLeft(getValue("CPVCOD1"),2,"0");
				valore+=fillCharLeft(getValue("CPVCOD2"),2,"0");
				valore+=fillCharLeft(getValue("CPVCOD3"),2,"0");
			}
			setValue("CPVCOD",valore);
		}
		
		function reloadCPV() {
			bloccaRichiesteServer();
			document.location.href="${pageContext.request.contextPath}/ApriPagina.do?href=w3/commons/dettaglio-codice-cpv.jsp&modo=MODIFICA&campo=${param.campo}&valore="+getValue("CPVCOD");
		}
		
		function conferma(){
			opener.setValue("${param.campo}",getValue("CPVCOD"));
			window.close();
		}
		
		function annulla(){
			window.close();
		}

	</gene:javaScript>
</gene:template>

