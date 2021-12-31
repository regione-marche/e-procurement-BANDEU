<%
/*
 * Created on 19-nov-2007
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
 
 // PAGINA CHE CONTIENE L'ISTANZA DELLA SOTTOPARTE DELLA PAGINA DI DETTAGLIO
 // DEI DATI GENERALI DI UN ACCOUNT CONTENENTE LA SEZIONE RELATIVA ALLE
 // SOTTOSEZIONI DELLA PAGINA STESSA.
 // QUESTA PAGINA E' STATA RIDEFINITA NEL PROGETTO PL-WEB PER UNA
 // PERSONALIZZAZIONE DEI DATI GENERALI DELL'ACCOUNT.
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>

<c:set var="listaOpzioniUtenteSys" value="${fn:join(accountForm.opzioniUtenteSys,'#')}#" />
<c:set var="account" value="${accountForm}"/>

<tr>
	<td colspan="2">
		<b>Configurazione di accesso ai dati</b>
	</td>
</tr>
<tr>
	<td class="etichetta-dato">Privilegi dell'utente</td>
	<td class="valore-dato">
		<c:choose>
			<c:when test='${account.abilitazioneLavori eq "A"}'>
				Amministratore (accesso consentito a tutti i dati)
			</c:when>
			<c:when test='${account.abilitazioneLavori eq "U"}'>
				Utente (accesso consentito solo ai dati assegnati)
			</c:when>
		</c:choose>
	</td>
</tr>
