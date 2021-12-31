
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

<gene:formScheda entita="W3FS5" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS5"
	plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS5" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS5.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS5" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>
	<gene:campoScheda entita="W3FS5S36" campo="ID" obbligatorio="true" visibile="false" where="W3FS5.ID = W3FS5S36.ID"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS5.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.II")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs5.II.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs5.II.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Denominazione" campo="TITLE_CONTRACT" obbligatorio="true"/>
	<gene:campoScheda campo="REFERENCE" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.II.1.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="CPV" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Vocabolario supplementare 1" campo="CPVSUPP1" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Vocabolario supplementare 2" campo="CPVSUPP2" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.II.1.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_CONTRACT" obbligatorio="true"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.II.1.4")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Breve descrizione" campo="SCOPE_TOTAL" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.II.1.5")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="SCOPE_COST" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoMoney"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.II.1.6")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Appalto suddiviso in lotti ?"  campo="DIV_INTO_LOTS" />
	<gene:campoScheda title="Le offerte vanno presentate per" campo="DIV_LOTS_VALUE" />
	<gene:campoScheda campo="DIV_LOTS_MAX" />
	<gene:campoScheda campo="LOTS_MAX_TENDERER" />
	<gene:campoScheda title="L'amministrazione aggiudicatrice si riserva la facolt&agrave; di aggiudicare i contratti d'appalto combinando i seguenti lotti o gruppi di lotti" campo="LOTS_COMBINING" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS5_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>
	
	<gene:fnJavaScriptScheda funzione="gestioneTYPE_CONTRACT('#W3FS5_TYPE_CONTRACT#')" elencocampi="W3FS5_TYPE_CONTRACT" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneDIV_INTO_LOTS('#W3FS5_DIV_INTO_LOTS#')" elencocampi="W3FS5_DIV_INTO_LOTS" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneDIV_LOTS_VALUE('#W3FS5_DIV_LOTS_VALUE#')" elencocampi="W3FS5_DIV_LOTS_VALUE" esegui="true" />

	
</gene:formScheda>

<gene:javaScript>

	$(window).ready(function (){
		_creaFinestraAlberoCpvVP();
		_creaFinestraAlberoCpvVSUPP();
		_creaLinkAlberoCpvVP($("#W3FS5_CPV").parent(), "${modo}", $("#W3FS5_CPV"), $("#W3FS5_CPVview") );
		_creaLinkAlberoCpvVSUPP($("#W3FS5_CPVSUPP1").parent(), "${modo}", $("#W3FS5_CPVSUPP1"), $("#W3FS5_CPVSUPP1view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS5_CPVSUPP2").parent(), "${modo}", $("#W3FS5_CPVSUPP2"), $("#W3FS5_CPVSUPP2view") );
		
		$("input[name*='CPV']").attr('readonly','readonly');
		$("input[name*='CPV']").attr('tabindex','-1');
		$("input[name*='CPV']").css('border-color','#A3A6FF');
		$("input[name*='CPV']").css('border-width','1px');
		$("input[name*='CPV']").css('background-color','#E0E0E0');
	});

	function gestioneTYPE_CONTRACT(type_contract){
		$("#W3SIMAP_CTYPE").val(type_contract);
	}

	function gestioneDIV_INTO_LOTS(div_into_lots){
		if (div_into_lots == '1') {
			$("#rowW3FS5_DIV_LOTS_VALUE").show();
			$("#rowW3FS5_LOTS_MAX_TENDERER").show();
			$("#rowW3FS5_LOTS_COMBINING").show();
		} else {
			$("#rowW3FS5_DIV_LOTS_VALUE").hide();
			$("#rowW3FS5_DIV_LOTS_MAX").hide();
			$("#rowW3FS5_LOTS_MAX_TENDERER").hide();
			$("#rowW3FS5_LOTS_COMBINING").hide();
			$("#W3FS5_DIV_LOTS_VALUE").val("");
			$("#W3FS5_DIV_LOTS_MAX").val("");
			$("#W3FS5_LOTS_MAX_TENDERER").val("");
			$("#W3FS5_LOTS_COMBINING").val("");
		}
	}

	function gestioneDIV_LOTS_VALUE(div_lots_value){
		if (div_lots_value == '2') {
			$("#rowW3FS5_DIV_LOTS_MAX").show();
		} else {
			$("#rowW3FS5_DIV_LOTS_MAX").hide();
			$("#W3FS5_DIV_LOTS_MAX").val("");
		}
	}
	
</gene:javaScript>
