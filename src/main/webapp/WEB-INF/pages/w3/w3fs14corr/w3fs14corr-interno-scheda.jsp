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

<c:set var="posizione" value="${fn:indexOf(param.chiave,';')}" />
<c:set var="form_rif" value="${fn:substring(param.chiave, 0, posizione)}"/>
<c:set var="id" value="${fn:substring(param.chiave, posizione+1, fn:length(param.chiave))}"/>

<c:choose>
	<c:when test="${param.tipoDettaglio eq 1}">
		<gene:campoScheda title="Identificativo appalto" entita="W3FS14CORR" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3FS14CORRID" value="${item[0]}"/>
		<gene:campoScheda title="Contatore" entita="W3FS14CORR" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3FS14CORRNUM" value="${item[1]}"/>
		<gene:campoScheda title="Sezione" obbligatorio="true" entita="W3FS14CORR" campo="SECTION_${param.contatore}" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSECTION_${form_rif}" campoFittizio="true" visibile="true" definizione="T2000;0;;NOTE;W3FS14CORRSECTION" value="${item[4]}"/>			
		<gene:campoScheda title="Lotto n." entita="W3FS14CORR" campo="LOT_NO_${param.contatore}" campoFittizio="true" definizione="T20;0;;" value="${item[19]}"/>
		<gene:campoScheda title="Punto in cui si trova il testo" entita="W3FS14CORR" campoFittizio="true" campo="LABEL_TEXT_${param.contatore}" definizione="T60;0;;" value="${item[20]}"/>		
		<gene:campoScheda title="Tipo di informazione" obbligatorio="true" entita="W3FS14CORR" campo="OBJECT_${param.contatore}" campoFittizio="true" visibile="true" definizione="T5;0;W3z67;;W3FS14CORROBJECT" value="${item[3]}"/>			
		<gene:campoScheda title="Tipo di correzione" obbligatorio="true" entita="W3FS14CORR" campo="TYPE_${param.contatore}" campoFittizio="true" visibile="true" definizione="T5;0;W3z66;;W3FS14CORRTYPE" value="${item[2]}"/>			
		<gene:campoScheda title="Testo originale" entita="W3FS14CORR" campo="TEXT_OLD_${param.contatore}" campoFittizio="true" visibile="true" definizione="T2000;0;;CLOB;W3FS14CORRTEXTO" value="${item[5]}"/>			
		<gene:campoScheda title="Nuovo testo" entita="W3FS14CORR" campo="TEXT_NEW_${param.contatore}" campoFittizio="true" visibile="true" definizione="T2000;0;;CLOB;W3FS14CORRTEXTN" value="${item[6]}"/>			
		<gene:campoScheda title="Data originale" entita="W3FS14CORR" campo="DATE_OLD_${param.contatore}" campoFittizio="true" visibile="true" definizione="D;0;;DATA_ELDA;W3FS14CORRDATEO" value="${item[7]}"/>			
		<gene:campoScheda title="Nuova data" entita="W3FS14CORR" campo="DATE_NEW_${param.contatore}" campoFittizio="true" visibile="true" definizione="D;0;;DATA_ELDA;W3FS14CORRDATEN" value="${item[8]}"/>			
		<gene:campoScheda title="Orario originale" entita="W3FS14CORR" campo="TIME_OLD_${param.contatore}" campoFittizio="true" visibile="true" definizione="T6;0;;ORA;W3FS14CORRTIMEO" value="${item[9]}"/>			
		<gene:campoScheda title="Nuovo orario" entita="W3FS14CORR" campo="TIME_NEW_${param.contatore}" campoFittizio="true" visibile="true" definizione="T6;0;;ORA;W3FS14CORRTIMEN" value="${item[10]}"/>			
		<gene:campoScheda title="NUTS originale" entita="W3FS14CORR" campo="NUTS_OLD_${param.contatore}" href="javascript:formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_OLD_${param.contatore}')" campoFittizio="true" speciale="true" visibile="true" definizione="T5;0;;;W3FS14CORRNUTSO" value="${item[11]}" >			
			<gene:popupCampo titolo="Dettaglio codice NUTS" href="formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo NUTS" entita="W3FS14CORR" campo="NUTS_NEW_${param.contatore}" href="javascript:formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_NEW_${param.contatore}')" campoFittizio="true" speciale="true" visibile="true" definizione="T5;0;;;W3FS14CORRNUTSN" value="${item[12]}" >			
			<gene:popupCampo titolo="Dettaglio codice NUTS" href="formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_NEW_${param.contatore}')" />
		</gene:campoScheda>

		<gene:campoScheda title="CPV originale" entita="W3FS14CORR" campo="CPV_OLD_${param.contatore}" href="javascript:formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_OLD_${param.contatore}')"  speciale="true" campoFittizio="true" definizione="T10;0;;;W3FS14CORRCPVO" value="${item[13]}">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario principale)" href="formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="CPV supplementare 1 originale" entita="W3FS14CORR" campo="CPVSUPP1_OLD_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_OLD_${param.contatore}')"  speciale="true" campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP1O" value="${item[14]}" >
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="CPV supplementare 2 originale" entita="W3FS14CORR" campo="CPVSUPP2_OLD_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_OLD_${param.contatore}')"  speciale="true"  campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP2O" value="${item[15]}" >
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo CPV" entita="W3FS14CORR" campo="CPV_NEW_${param.contatore}" href="javascript:formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_NEW_${param.contatore}')" speciale="true" campoFittizio="true" definizione="T10;0;;;W3FS14CORRCPVN" value="${item[16]}">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario principale)" href="formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_NEW_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo CPV supplementare 1" entita="W3FS14CORR" campo="CPVSUPP1_NEW_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_NEW_${param.contatore}')" speciale="true" campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP1N" value="${item[17]}" >
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_NEW_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo CPV supplementare 2" entita="W3FS14CORR" campo="CPVSUPP2_NEW_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_NEW_${param.contatore}')" speciale="true"  campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP2N" value="${item[18]}" >
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_NEW_${param.contatore}')" />
		</gene:campoScheda>
		
	</c:when>

	<c:otherwise>
		<gene:campoScheda title="Identificativo appalto" entita="W3FS14CORR" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3FS14CORRID" value="${id}"/>
		<gene:campoScheda title="Contatore" entita="W3FS14CORR" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3FS14CORRNUM" />
		<gene:campoScheda title="Sezione" obbligatorio="true" entita="W3FS14CORR" campo="SECTION_${param.contatore}" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSECTION_${form_rif}" campoFittizio="true" visibile="true" definizione="T2000;0;;NOTE;W3FS14CORRSECTION"/>			
		<gene:campoScheda title="Lotto n."  entita="W3FS14CORR" campo="LOT_NO_${param.contatore}" campoFittizio="true" definizione="T20;0;;" />
		<gene:campoScheda title="Punto in cui si trova il testo" entita="W3FS14CORR" campo="LABEL_TEXT_${param.contatore}" campoFittizio="true" definizione="T60;0;;" />		
		<gene:campoScheda title="Tipo di informazione" obbligatorio="true" entita="W3FS14CORR" campo="OBJECT_${param.contatore}" campoFittizio="true" visibile="true" definizione="T5;0;W3z67;;W3FS14CORROBJECT" />			
		<gene:campoScheda title="Tipo di rettifica" obbligatorio="true" entita="W3FS14CORR" campo="TYPE_${param.contatore}" campoFittizio="true" visibile="true" definizione="T5;0;W3z66;;W3FS14CORRTYPE" />			
		<gene:campoScheda title="Testo originale" entita="W3FS14CORR" campo="TEXT_OLD_${param.contatore}" campoFittizio="true" visibile="true" definizione="T2000;0;;CLOB;W3FS14CORRTEXTO"/>			
		<gene:campoScheda title="Nuovo testo" entita="W3FS14CORR" campo="TEXT_NEW_${param.contatore}" campoFittizio="true" visibile="true" definizione="T2000;0;;CLOB;W3FS14CORRTEXTN"/>			
		<gene:campoScheda title="Data originale" entita="W3FS14CORR" campo="DATE_OLD_${param.contatore}" campoFittizio="true" visibile="true" definizione="D;0;;DATA_ELDA;W3FS14CORRDATEO"/>			
		<gene:campoScheda title="Nuova data" entita="W3FS14CORR" campo="DATE_NEW_${param.contatore}" campoFittizio="true" visibile="true" definizione="D;0;;DATA_ELDA;W3FS14CORRDATEN"/>			
		<gene:campoScheda title="Orario originale" entita="W3FS14CORR" campo="TIME_OLD_${param.contatore}" campoFittizio="true" visibile="true" definizione="T6;0;;ORA;W3FS14CORRTIMEO"/>			
		<gene:campoScheda title="Nuovo orario" entita="W3FS14CORR" campo="TIME_NEW_${param.contatore}" campoFittizio="true" visibile="true" definizione="T6;0;;ORA;W3FS14CORRTIMEN"/>			
		<gene:campoScheda title="NUTS originale" entita="W3FS14CORR" campo="NUTS_OLD_${param.contatore}" href="javascript:formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_OLD_${param.contatore}')" campoFittizio="true" speciale="true" visibile="true" definizione="T5;0;;;W3FS14CORRNUTSO" >			
			<gene:popupCampo titolo="Dettaglio codice NUTS" href="formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo NUTS" entita="W3FS14CORR" campo="NUTS_NEW_${param.contatore}" href="javascript:formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_NEW_${param.contatore}')" campoFittizio="true" speciale="true" visibile="true" definizione="T5;0;;;W3FS14CORRNUTSN" >			
			<gene:popupCampo titolo="Dettaglio codice NUTS" href="formNUTS(${requestScope.modCPVNUTS}, 'W3FS14CORR_NUTS_NEW_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="CPV originale" entita="W3FS14CORR" campo="CPV_OLD_${param.contatore}" href="javascript:formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_OLD_${param.contatore}')" speciale="true" campoFittizio="true" definizione="T10;0;;;W3FS14CORRCPVO">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario principale)" href="formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="CPV supplementare 1 originale" entita="W3FS14CORR" campo="CPVSUPP1_OLD_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_OLD_${param.contatore}')"  speciale="true" campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP1O">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="CPV supplementare 2 originale" entita="W3FS14CORR" campo="CPVSUPP2_OLD_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_OLD_${param.contatore}')" speciale="true"  campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP2O">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_OLD_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo CPV" entita="W3FS14CORR" campo="CPV_NEW_${param.contatore}" href="javascript:formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_NEW_${param.contatore}')" speciale="true" campoFittizio="true" definizione="T10;0;;;W3FS14CORRCPVN">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario principale)" href="formCPV(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPV_NEW_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo CPV supplementare 1" entita="W3FS14CORR" campo="CPVSUPP1_NEW_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_NEW_${param.contatore}')" speciale="true" campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP1N">
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP1_NEW_${param.contatore}')" />
		</gene:campoScheda>
		<gene:campoScheda title="Nuovo CPV supplementare 2" entita="W3FS14CORR" campo="CPVSUPP2_NEW_${param.contatore}" href="javascript:formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_NEW_${param.contatore}')" speciale="true"  campoFittizio="true" definizione="T6;0;;;W3FS14CORRCPVSUPP2N" >
			<gene:popupCampo titolo="Dettaglio codice CPV (Vocabolario supplementare)" href="formCPVSUPP(${requestScope.modCPVNUTS}, 'W3FS14CORR_CPVSUPP2_NEW_${param.contatore}')" />
		</gene:campoScheda>

	</c:otherwise>

</c:choose>

<gene:fnJavaScriptScheda funzione="gestioneOBJECT_${param.contatore}('#W3FS14CORR_OBJECT_${param.contatore}#')" elencocampi="W3FS14CORR_OBJECT_${param.contatore}" esegui="true" />
<gene:fnJavaScriptScheda funzione="gestioneTYPEOBJECT_${param.contatore}('#W3FS14CORR_TYPE_${param.contatore}#','#W3FS14CORR_OBJECT_${param.contatore}#')" elencocampi="W3FS14CORR_TYPE_${param.contatore};W3FS14CORR_OBJECT_${param.contatore}" esegui="true" />

<gene:javaScript>
	
	$(window).ready(function (){
		_creaLinkAlberoCpvVP($("#W3FS14CORR_CPV_OLD_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_CPV_OLD_${param.contatore}"), $("#W3FS14CORR_CPV_OLD_${param.contatore}view") );
		_creaLinkAlberoCpvVP($("#W3FS14CORR_CPV_NEW_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_CPV_NEW_${param.contatore}"), $("#W3FS14CORR_CPV_NEW_${param.contatore}view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS14CORR_CPVSUPP1_OLD_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_CPVSUPP1_OLD_${param.contatore}"), $("#W3FS14CORR_CPVSUPP1_OLD_${param.contatore}view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS14CORR_CPVSUPP2_OLD_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_CPVSUPP2_OLD_${param.contatore}"), $("#W3FS14CORR_CPVSUPP2_OLD_${param.contatore}view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS14CORR_CPVSUPP1_NEW_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_CPVSUPP1_NEW_${param.contatore}"), $("#W3FS14CORR_CPVSUPP1_NEW_${param.contatore}view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS14CORR_CPVSUPP2_NEW_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_CPVSUPP2_NEW_${param.contatore}"), $("#W3FS14CORR_CPVSUPP2_NEW_${param.contatore}view") );
		_creaLinkAlberoNUTS($("#W3FS14CORR_NUTS_OLD_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_NUTS_OLD_${param.contatore}"), $("#W3FS14CORR_NUTS_OLD_${param.contatore}view") );
		_creaLinkAlberoNUTS($("#W3FS14CORR_NUTS_NEW_${param.contatore}").parent(), "${modo}", $("#W3FS14CORR_NUTS_NEW_${param.contatore}"), $("#W3FS14CORR_NUTS_NEW_${param.contatore}view") );
		
		
		$("#W3FS14CORR_CPV_OLD_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_CPV_NEW_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_CPVSUPP1_OLD_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_CPVSUPP2_OLD_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_CPVSUPP1_NEW_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_CPVSUPP2_NEW_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_NUTS_OLD_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});

		$("#W3FS14CORR_NUTS_NEW_${param.contatore}").change(function(){
			$("#W3FS14CORR_MOD_W3FS14CORR_${param.contatore}").val("1");
		});
		
	});
	
	
	function gestioneOBJECT_${param.contatore}(object) {
		if(object=='CPV'){
			document.forms[0].W3FS14CORR_TYPE_${param.contatore}.value='REP';
			document.getElementById("rowW3FS14CORR_TYPE_${param.contatore}").style.display = 'none';
		} else {
			document.getElementById("rowW3FS14CORR_TYPE_${param.contatore}").style.display = '';
		}
	}
	

	function gestioneTYPEOBJECT_${param.contatore}(type, object){

		document.getElementById("rowW3FS14CORR_TEXT_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_DATE_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_TIME_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_NUTS_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_CPV_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_CPVSUPP1_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_CPVSUPP2_OLD_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_TEXT_NEW_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_DATE_NEW_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_TIME_NEW_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_NUTS_NEW_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_CPV_NEW_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_CPVSUPP1_NEW_${param.contatore}").style.display = 'none';
		document.getElementById("rowW3FS14CORR_CPVSUPP2_NEW_${param.contatore}").style.display = 'none';
	
		if (type == 'ADD') {
			document.getElementById("rowW3FS14CORR_TEXT_OLD_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_DATE_OLD_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_TIME_OLD_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_NUTS_OLD_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_CPV_OLD_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_CPVSUPP1_OLD_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_CPVSUPP2_OLD_${param.contatore}").style.display = 'none';		
			document.getElementById("rowW3FS14CORR_TEXT_NEW_${param.contatore}").style.display = (object=='TEXT' ? '':'none');
			document.getElementById("rowW3FS14CORR_DATE_NEW_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_TIME_NEW_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_NUTS_NEW_${param.contatore}").style.display = (object=='NUTS' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPV_NEW_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP1_NEW_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP2_NEW_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
		}
	
		if (type == 'REP') {
			document.getElementById("rowW3FS14CORR_TEXT_OLD_${param.contatore}").style.display = (object=='TEXT' ? '':'none');
			document.getElementById("rowW3FS14CORR_DATE_OLD_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_TIME_OLD_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_NUTS_OLD_${param.contatore}").style.display = (object=='NUTS' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPV_OLD_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP1_OLD_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP2_OLD_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_TEXT_NEW_${param.contatore}").style.display = (object=='TEXT' ? '':'none');
			document.getElementById("rowW3FS14CORR_DATE_NEW_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_TIME_NEW_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_NUTS_NEW_${param.contatore}").style.display = (object=='NUTS' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPV_NEW_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP1_NEW_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP2_NEW_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
		} 
		
		if (type == 'DEL') {
			document.getElementById("rowW3FS14CORR_TEXT_OLD_${param.contatore}").style.display = (object=='TEXT' ? '':'none');
			document.getElementById("rowW3FS14CORR_DATE_OLD_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_TIME_OLD_${param.contatore}").style.display = (object=='DATE' ? '':'none');
			document.getElementById("rowW3FS14CORR_NUTS_OLD_${param.contatore}").style.display = (object=='NUTS' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPV_OLD_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP1_OLD_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_CPVSUPP2_OLD_${param.contatore}").style.display = (object=='CPV' || object=='CPVA' ? '':'none');
			document.getElementById("rowW3FS14CORR_TEXT_NEW_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_DATE_NEW_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_TIME_NEW_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_NUTS_NEW_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_CPV_NEW_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_CPVSUPP1_NEW_${param.contatore}").style.display = 'none';
			document.getElementById("rowW3FS14CORR_CPVSUPP2_NEW_${param.contatore}").style.display = 'none';
		}

		
		if(object!='TEXT'){
			document.forms[0].W3FS14CORR_TEXT_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_TEXT_NEW_${param.contatore}.value='';
		}
		
		if(object!='DATE'){
			document.forms[0].W3FS14CORR_DATE_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_DATE_NEW_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_TIME_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_TIME_NEW_${param.contatore}.value='';
		}
		
		if(object!='NUTS'){
			document.forms[0].W3FS14CORR_NUTS_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_NUTS_NEW_${param.contatore}.value='';
		}
		
		if (object == 'CPV' || object == 'CPVA') {
			
		} else {
			document.forms[0].W3FS14CORR_CPV_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_CPV_NEW_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_CPVSUPP1_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_CPVSUPP1_NEW_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_CPVSUPP2_OLD_${param.contatore}.value='';
			document.forms[0].W3FS14CORR_CPVSUPP2_NEW_${param.contatore}.value='';
		}
		
	}

</gene:javaScript>

