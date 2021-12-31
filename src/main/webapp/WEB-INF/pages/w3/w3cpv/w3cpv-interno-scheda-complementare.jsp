
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

<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneCPVComplementariFunction" parametro='${chiave}' />	

<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
	<jsp:param name="entita" value='W3CPV'/>
	<jsp:param name="chiave" value='${chiave}'/>
	<jsp:param name="nomeAttributoLista" value='datiW3CPV' />
	<jsp:param name="idProtezioni" value="W3CPV" />
	<jsp:param name="sezioneListaVuota" value="false" />
	<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3cpv/w3cpv-complementari-interno-scheda.jsp"/>
	<jsp:param name="arrayCampi" value="'W3CPV_NUMCPV_', 'W3CPV_ENT_', 'W3CPV_ID_', 'W3CPV_NUM_',  'W3CPV_TIPCPV_', 'W3CPV_CPV_', 'W3CPV_CPVSUPP1_', 'W3CPV_CPVSUPP2_'"/>
	<jsp:param name="titoloSezione" value="CPV aggiuntivo n." />
	<jsp:param name="titoloNuovaSezione" value="Nuovo CPV aggiuntivo" />
	<jsp:param name="descEntitaVociLink" value="CPV aggiuntivo" />
	<jsp:param name="msgRaggiuntoMax" value="i CPV aggiuntivi"/>
	<jsp:param name="usaContatoreLista" value="true"/>
	<jsp:param name="numMaxDettagliInseribili" value="5"/>
</jsp:include>


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
