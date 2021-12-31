
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
	
	<gene:setString name="titoloMaschera"  value="Invia dati a SIMAP per la pubblicazione nel Supplemento della Gazzetta Ufficiale dell'Unione Europea - Serie S"  />

	<gene:redefineInsert name="corpo">
	
		<table class="lista">
			<tr>
				<br>
				Questa funzione invia a SIMAP i dati del bando/avviso <b>${descrizione}</b> per la successiva pubblicazione sulla Gazzetta Ufficiale dell'Unione Europea - Serie S.
				<br>
				<br>
				La pubblicazione, tuttavia, &egrave; possibile solamente se, su tutti i dati inseriti, non sono stati rilevati errori bloccanti.
				<br>
				<br>
			</tr>
		
			<tr>
				<td class="comandi-dettaglio" colSpan="2">
					<INPUT type="button" class="bottone-azione" value="Invia dati a SIMAP" title="Invia dati a SIMAP" onclick="javascript:inviaSIMAPWS(${id});">
					<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla" onclick="javascript:annulla();">&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</gene:redefineInsert>
	<gene:javaScript>
		
		document.forms[0].jspPathTo.value="w3/commons/popup-avvia-invia-simapws.jsp";
		
		function annulla(){
			window.close();
		}
		
		function inviaSIMAPWS(id){
			var action = "${pageContext.request.contextPath}/w3/AvviaInviaSIMAPWS.do";
 			document.location.href=action+'?id=' + id;
		}
	
	</gene:javaScript>	
</gene:template>

</div>

