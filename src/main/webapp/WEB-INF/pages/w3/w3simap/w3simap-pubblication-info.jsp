<%
	/*
	 * Created on 07-Ott-2010
	 *
	 * Copyright (c) EldaSoft S.p.A.
	 * Tutti i diritti sono riservati.
	 *
	 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
	 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
	 * aver prima formalizzato un accordo specifico con EldaSoft.
	 */

%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>




<gene:campoScheda entita="W3SIMAP" campo="FORM" visibile="false" modificabile="false" />
<gene:campoScheda visibile="${modo ne 'NUOVO'}">
	<td colspan="2"><b><br>Pubblicazione sulla Gazzetta Ufficiale dell'Unione Europea - Serie S</b>
		<c:if test="${datiRiga.W3SIMAP_FORM ne 'FS14'}">
			<div style="padding: 10 50 10 5">
				ATTENZIONE: i dati inviati con successo non possono pi&ugrave; essere modificati.
				<br>
				Per correggere i dati inviati o aggiungere altre informazioni &egrave; necessario attendere la pubblicazione sulla Gazzetta Ufficiale - Serie S e solo successivamente 
				creare <u>l'avviso di rettifica</u>. L'avviso di rettifica &egrave; un formulario specifico con il quale &egrave; possibile inviare la lista delle informazioni da correggere o aggiungere: in nessun caso &egrave; possibile modificare i dati originali gi&agrave; inviati.
			</div>
		</c:if>
	</td>
</gene:campoScheda>
<gene:campoScheda entita="W3SIMAP" campo="NO_DOC_EXT" visibile="${modo ne 'NUOVO'}" modificabile="false" />
<gene:campoScheda title="Numero avviso GU/S" campo="OJS" campoFittizio="true" definizione="T100;0" visibile="${modo ne 'NUOVO'}" modificabile="false"/>
<gene:campoScheda entita="W3SIMAP" campo="NOTICE_NUMBER_OJ" visibile="false" modificabile="false"/>
<gene:campoScheda entita="W3SIMAP" campo="DATE_OJ" visibile="${modo ne 'NUOVO'}" modificabile="false"/>
<gene:campoScheda entita="W3SIMAP" campo="TED_LINKS" visibile="false"/>

<c:if test="${modo ne 'NUOVO'}">
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneW3SIMAPWSFunction" parametro='${gene:getValCampo(key,"ID")}' />	
	
	<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
		<jsp:param name="entita" value='W3SIMAPWS'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3SIMAPWS' />
		<jsp:param name="idProtezioni" value="W3SIMAPWS" />
		<jsp:param name="sezioneListaVuota" value="false" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3simapws/w3simapws-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3SIMAPWS_ID_', 'W3SIMAPWS_NUM_', 'W3SIMAPWS_PHASE_', 'W3SIMAPWS_SUBMISSION_DATE_', 'W3SIMAPWS_SUBMISSION_ID_', 'W3SIMAPWS_STATUS_CODE_', 'W3SIMAPWS_STATUS_DESCRIPTION_', 'W3SIMAPWS_REPORT_DATE_'"/>
		<jsp:param name="titoloSezione" value="<br>Informazioni relative all'invio n. " />
		<jsp:param name="titoloNuovaSezione" value="Nuovo invio" />
		<jsp:param name="descEntitaVociLink" value="invio" />
		<jsp:param name="msgRaggiuntoMax" value="i invii"/>
		<jsp:param name="usaContatoreLista" value="false"/>
		<jsp:param name="numMaxDettagliInseribili" value="10"/>
		<jsp:param name="sezioneInseribile" value="false"/>
		<jsp:param name="sezioneEliminabile" value="false"/>
	</jsp:include>
</c:if>

<gene:javaScript>


	if ($("#W3SIMAP_TED_LINKS").val() != "") {
		$("#OJSview").html($("#W3SIMAP_NOTICE_NUMBER_OJ").val() + "&nbsp;&nbsp;&nbsp;&nbsp;");
		
		var _title = "Visualizza bando/avviso sul sito TED";
		var _link = $("<a/>", {"href": $("#W3SIMAP_TED_LINKS").val(), "text": _title, "title": _title, "target": "_blank"});
		
		var _link_img = $("<a/>", {"href": $("#W3SIMAP_TED_LINKS").val(), "target": "_blank"});
		var _img = $("<img/>", {"height": "18px", "width": "46px", "title": _title, "alt": _title, "src": "img/ted.jpg", "vertical-align": "top"});
		_link_img.append(_img);
		
		$("#OJSview").parent().append(_link);
		$("#OJSview").parent().append("&nbsp;&nbsp;");
		$("#OJSview").parent().append(_link_img);
		
	} else {
		$("#OJSview").html($("#W3SIMAP_NOTICE_NUMBER_OJ").val());
	}
		
</gene:javaScript>