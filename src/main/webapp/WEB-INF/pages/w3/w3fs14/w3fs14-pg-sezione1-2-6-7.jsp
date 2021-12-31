
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

<gene:formScheda entita="W3FS14" gestisciProtezioni="true" 
gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS14" plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS14" >


	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda campo="ID_RIF" visibile="false" defaultValue="${id_rif}"/>
	<gene:campoScheda campo="FORM_RIF" visibile="false" defaultValue="${form_rif}"/>
	
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS14.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS14" visibile="false" />
	
	<jsp:include page="/WEB-INF/pages/w3/w3simap/w3simap-pubblication-info.jsp"></jsp:include>	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs14.VI")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs14.VI.5")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="V_W3SIMAP" campo="NOTICE_DATE" where="V_W3SIMAP.ID = W3FS14.ID" modificabile="false"/>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs14.VII")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs14.VII.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs14.VII.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="CORRECT_REASON" obbligatorio = "true"/>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs14.VII.1.2")}</b></td>
	</gene:campoScheda>
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneW3FS14CORRFunction" parametro='${gene:getValCampo(key, "ID")}' />	
	<c:set var="id" value="${gene:getValCampo(key, 'ID')}" />
	<c:set var="chiave" value="${form_rif};${id}" />
	<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
		<jsp:param name="entita" value='W3FS14CORR'/>
		<jsp:param name="chiave" value='${chiave}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3FS14CORR' />
		<jsp:param name="idProtezioni" value="W3FS14CORR" />
		<jsp:param name="sezioneListaVuota" value="true" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3fs14corr/w3fs14corr-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3FS14CORR_ID_', 'W3FS14CORR_NUM_', 'W3FS14CORR_TYPE_', 'W3FS14CORR_OBJECT_',  'W3FS14CORR_SECTION_', 'W3FS14CORR_TEXT_OLD_', 'W3FS14CORR_TEXT_NEW_', 'W3FS14CORR_DATE_OLD_','W3FS14CORR_DATE_NEW_','W3FS14CORR_TIME_OLD_','W3FS14CORR_TIME_NEW_','W3FS14CORR_NUTS_OLD_','W3FS14CORR_NUTS_NEW_','W3FS14CORR_CPV_OLD_','W3FS14CORR_CPVSUPP1_OLD_','W3FS14CORR_CPVSUPP2_OLD_','W3FS14CORR_CPV_NEW_','W3FS14CORR_CPVSUPP1_NEW_','W3FS14CORR_CPVSUPP2_NEW_','W3FS14CORR_LOT_NO_','W3FS14CORR_LABEL_TEXT_'"/>
		<jsp:param name="arrayVisibilitaCampi" value="false,false,true,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true" />
		<jsp:param name="titoloSezione" value="Correzione n." />
		<jsp:param name="titoloNuovaSezione" value="Altra correzione" />
		<jsp:param name="descEntitaVociLink" value="altra correzione" />
		<jsp:param name="msgRaggiuntoMax" value="e correzioni"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>
	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs14.VII.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="OTHER_INFORMATION" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS14_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="pulsanteNuovo" />
	<gene:redefineInsert name="schedaNuovo" />
	
</gene:formScheda>

<gene:javaScript>

	$("#W3FS14_CORRECT_REASON option").each(function() {
		$(this).text($(this).attr("title"));
	});

	$(window).ready(function (){
		_creaFinestraAlberoCpvVP();
		_creaFinestraAlberoCpvVSUPP();
		_creaFinestraAlberoNUTS();
	});
	
	$("input[name^='W3FS14CORR_NUTS']").attr('readonly','readonly');
	$("input[name^='W3FS14CORR_NUTS']").attr('tabindex','-1');
	$("input[name^='W3FS14CORR_NUTS']").css('border-color','#A3A6FF');
	$("input[name^='W3FS14CORR_NUTS']").css('border-width','1px');
	$("input[name^='W3FS14CORR_NUTS']").css('background-color','#E0E0E0');
	
	$("input[name^='W3FS14CORR_CPV']").attr('readonly','readonly');
	$("input[name^='W3FS14CORR_CPV']").attr('tabindex','-1');
	$("input[name^='W3FS14CORR_CPV']").css('border-color','#A3A6FF');
	$("input[name^='W3FS14CORR_CPV']").css('border-width','1px');
	$("input[name^='W3FS14CORR_CPV']").css('background-color','#E0E0E0');

</gene:javaScript>

