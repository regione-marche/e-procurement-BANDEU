
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
	
	<gene:setString name="titoloMaschera"  value='${descrizione} - Componi un nuovo messaggio di posta elettronica'  />

	<gene:redefineInsert name="corpo">
	
		<table class="lista">
			<tr>
				<br>
				Questa funzione compone, ed invia al SIMAP, un <b>nuovo messaggio di posta elettronica</b> contenente i dati del
				bando/avviso <b>${descrizione}</b>.
				<br>
				<br>
				La composizione del nuovo messaggio, tuttavia, è possibile solamente se tutti i dati inseriti per il bando/avviso 
				superano i controlli di validit&agrave;.
				<br>
				<br>
			</tr>
		
			<tr>
				<td class="comandi-dettaglio" colSpan="2">
					<INPUT type="button" class="bottone-azione" value="Componi il messaggio" title="Componi il messaggio" onclick="javascript:componi(${id});">
					<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla" onclick="javascript:annulla();">&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</gene:redefineInsert>
	<gene:javaScript>
		document.forms[0].jspPathTo.value="w3/w3simapemail/w3simapemail-popup-componi.jsp";
		
		function annulla(){
			window.close();
		}
		
		function componi(id){
			var action = "${pageContext.request.contextPath}/w3/ComponiSIMAPEmail.do";
 			document.location.href=action+'?id=' + id;
		}
	
	</gene:javaScript>	
</gene:template>

</div>

