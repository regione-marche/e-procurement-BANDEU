<%
/*
 * Created on 15-ott-2007
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */

 // PAGINA CHE CONTIENE IL CODICE PER GENERARE LA TESTATA INFORMATIVA SUL PRODOTTO
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>

<fmt:setBundle basename="AliceResources" />
<c:set var="nomeEntitaSingolaBreveParametrizzata">
	<fmt:message key="label.tags.uffint.singoloBreve" />
</c:set>

<c:set var="moduloAttivo" value="${sessionScope.moduloAttivo}" scope="request" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="profiloUtente" value="${sessionScope.profiloUtente}" scope="request"/>
<c:set var="isNavigazioneDisattiva" value="${isNavigazioneDisabilitata}" />

<div class="banner">
<c:if test='${(! empty moduloAttivo) and (! empty profiloUtente) and (isNavigazioneDisattiva ne "1")}' >
	<a href="javascript:goHome('${moduloAttivo}');" title="Torna alla homepage" tabindex="10">
</c:if>
		<img src="${contextPath}/img/banner_logo.png" alt="Torna alla homepage" title="Torna alla homepage"><c:if test='${(! empty moduloAttivo) and (! empty profiloUtente) and (isNavigazioneDisattiva ne "1")}' ></a></c:if>
</div>

	
<script type="text/javascript">	
	$(document).ready(function() {
		$(document).bind('keydown', 'return', function(e) {
			if ((e.which || e.keyCode) == 116) {
				e.preventDefault();
			}
		});
	});	
</script>