
<%
	/*
	 * Created on 20-Ott-2008
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

<c:set var="ent" value="${param.ent}" />
<c:set var="id" value="${param.id}" />
<c:set var="num" value="${param.num}" />
<c:set var="chiave" value="${ent};${id};${num}" />

<gene:campoScheda entita="${ent}" campo="CPV" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio CPV" href="" />
</gene:campoScheda>
<gene:campoScheda entita="${ent}" campo="CPVSUPP1" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio CPV" href="" />
</gene:campoScheda>
<gene:campoScheda entita="${ent}" campo="CPVSUPP2" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio CPV" href="" />
</gene:campoScheda>

<gene:javaScript>

	$(window).ready(function (){
		_creaLinkAlberoCpvVP($("#${ent}_CPV").parent(), "${modo}", $("#${ent}_CPV"), $("#${ent}_CPVview") );
		_creaLinkAlberoCpvVSUPP($("#${ent}_CPVSUPP1").parent(), "${modo}", $("#${ent}_CPVSUPP1"), $("#${ent}_CPVSUPP1view") );
		_creaLinkAlberoCpvVSUPP($("#${ent}_CPVSUPP2").parent(), "${modo}", $("#${ent}_CPVSUPP2"), $("#${ent}_CPVSUPP2view") );
	});

	$("input[name*='CPV']").attr('readonly','readonly');
	$("input[name*='CPV']").attr('tabindex','-1');
	$("input[name*='CPV']").css('border-color','#A3A6FF');
	$("input[name*='CPV']").css('border-width','1px');
	$("input[name*='CPV']").css('background-color','#E0E0E0');
	


</gene:javaScript>
