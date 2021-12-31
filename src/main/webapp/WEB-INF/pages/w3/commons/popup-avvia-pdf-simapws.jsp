
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

	<c:set var="id" value="${param.id}" />
	<c:set var="descrizione" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GestioneValidazioneSIMAPTitleFunction",pageContext,id)}' />
	<c:if test="${fn:length(descrizione) > 200}">
		<c:set var="descrizione" value="${fn:substring(descrizione,0,200)} ..." />
	</c:if>
	
	<gene:setString name="titoloMaschera"  value='Genera documento PDF' />

	<gene:redefineInsert name="corpo">
	
		<table class="lista">
			<tr>
				<br>
				Questa funzione invia a SIMAP i dati del bando/avviso <b>${descrizione}</b> e richiede la generazione di un documento PDF in bozza.
				<br><br>
				Il documento PDF generato <u>non &egrave; ufficiale</u> in quando privo delle informazioni di pubblicazione sulla Gazzetta Ufficiale dell'Unione Europea - Serie S.
				<br><br>
				<b>Il documento PDF ufficiale pu&ograve; essere scaricato, dopo la pubblicazione, consultando il collegamento "Visualizza bando/avviso sul sito TED" presente nella pagina principale di ogni bando/avviso.</b>
				<br>
				<br>
				La generazione del documento PDF, anche se in bozza, &egrave; possibile solamente se tutti i dati inseriti superano i controlli di validit&agrave;.
				<br>
				<br>
			</tr>
		
			<tr>
				<td class="comandi-dettaglio" colSpan="2">
					<INPUT type="button" class="bottone-azione" value="Genera documento PDF" title="Genera documento PDF" onclick="javascript:pdfSIMAPWS(${id});">
					<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla" onclick="javascript:annulla();">&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</gene:redefineInsert>
	<gene:javaScript>
		
		document.forms[0].jspPathTo.value="w3/commons/popup-avvia-pdf-simapws.jsp";
		
		function annulla(){
			window.close();
		}
		
		function pdfSIMAPWS(id){
			var action = "${pageContext.request.contextPath}/w3/AvviaPdfSIMAPWS.do";
 			document.location.href=action+'?id=' + id;
		}
	
	</gene:javaScript>	
</gene:template>

</div>

