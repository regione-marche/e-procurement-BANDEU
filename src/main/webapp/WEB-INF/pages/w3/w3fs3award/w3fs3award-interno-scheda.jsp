
<%
	/*
	 * Created on 08-Ott-2010
	 *
	 * Copyright (c) EldaSoft S.p.A.
	 * Tutti i diritti sono riservati.
	 *
	 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
	 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
	 * aver prima formalizzato un accordo specifico con EldaSoft.
	 */

	// Scheda degli intestatari della concessione stradale
%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>


<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V")}<br><br></b></td>
</gene:campoScheda>
<gene:campoScheda entita="W3FS3AWARD" title="Contratto d'appalto n. " campo="CONTRACT_NUMBER" />
<gene:campoScheda entita="W3FS3AWARD" title="Lotto n. " campo="LOT_NUMBER" />
<gene:campoScheda entita="W3FS3AWARD" campo="CONTRACT_TITLE" obbligatorio="true"/>
<gene:campoScheda entita="W3FS3AWARD" campo="AWARDED" obbligatorio="true"/>

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V.1")}</b></td>
</gene:campoScheda>	
<gene:campoScheda entita="W3FS3AWARD" title="Motivo" campo="NO_AWARDED_TYPE" />
<gene:campoScheda entita="W3FS3AWARD" campo="ORIGINAL" />
<gene:campoScheda entita="W3FS3AWARD" campo="DATE_ORIGINAL" definizione="D;0"/>

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V.2")}</b></td>
</gene:campoScheda>	
<gene:campoScheda>
	<td colspan="2"><b>${gene:resource("label.simap.fs3.V.2.1")}</b></td>
</gene:campoScheda>	
<gene:campoScheda entita="W3FS3AWARD" campo="CONTRACT_AWARD_DATE" />

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V.2.2")}</b></td>
</gene:campoScheda>	
<gene:campoScheda entita="W3FS3AWARD" campo="OFFERS_RECEIVED" />
<gene:campoScheda entita="W3FS3AWARD" campo="NB_SME" />
<gene:campoScheda entita="W3FS3AWARD" campo="NB_OTHER_EU" />
<gene:campoScheda entita="W3FS3AWARD" campo="NB_OTHER_NON_EU" />
<gene:campoScheda entita="W3FS3AWARD" campo="OFFERS_RECEIVED_MEANING" />
<gene:campoScheda entita="W3FS3AWARD" campo="AWARDED_GROUP" />

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V.2.3")}</b></td>
</gene:campoScheda>		
<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneW3FS3AWARDCFunction" parametro='${param.w3fs3award_id};${param.w3fs3award_item}' />	
<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
	<jsp:param name="entita" value='W3FS3AWARD_C'/>
	<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
	<jsp:param name="nomeAttributoLista" value='datiW3FS3AWARD_C' />
	<jsp:param name="idProtezioni" value="W3FS3AWARD_C" />
	<jsp:param name="sezioneListaVuota" value="true" />
	<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3fs3award_c/w3fs3award_c-interno-scheda.jsp"/>
	<jsp:param name="arrayCampi" value="'W3FS3AWARD_C_ID_', 'W3FS3AWARD_C_ITEM_', 'W3FS3AWARD_C_NUM_', 'W3FS3AWARD_C_CONTRACTOR_CODIMP_', 'W3FS3AWARD_C_CONTRACTOR_SME_',  'NOMEST_'"/>
	<jsp:param name="titoloSezione" value="Contraente n." />
	<jsp:param name="titoloNuovaSezione" value="Nuovo contraente" />
	<jsp:param name="descEntitaVociLink" value="contraente" />
	<jsp:param name="msgRaggiuntoMax" value="i contraenti"/>
	<jsp:param name="usaContatoreLista" value="true"/>
	<jsp:param name="numMaxDettagliInseribili" value="5"/>
</jsp:include>


<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V.2.4")}</b></td>
</gene:campoScheda>	
<gene:campoScheda entita="W3FS3AWARD" title="Valore totale inizialmente stimato del contratto d'appalto" campo="INITIAL_COST" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoMoney"/>
<gene:campoScheda entita="W3FS3AWARD" title="Valore totale del contratto d'appalto" campo="FINAL_COST"  gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoMoney"/>
<gene:campoScheda entita="W3FS3AWARD" campo="FINAL_LOW"  gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoMoney"/>
<gene:campoScheda entita="W3FS3AWARD" campo="FINAL_HIGH"  gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoMoney"/>

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs3.V.2.5")}</b></td>
</gene:campoScheda>				
<gene:campoScheda entita="W3FS3AWARD" title="E' probabile che il contratto d'appalto venga subappaltato ?" campo="SUB_CONTRACTED" />
<gene:campoScheda entita="W3FS3AWARD" title="Valore" campo="SUB_VALUE" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoMoney"/>
<gene:campoScheda entita="W3FS3AWARD" title="Percentuale" campo="SUB_PRCT" >
	<gene:addValue value="" descr=""/>
	<c:forEach begin="1" end="100" var="prct">
		<gene:addValue value="${prct}" descr="${prct}"/>
	</c:forEach>
</gene:campoScheda>
<gene:campoScheda entita="W3FS3AWARD" title="Breve descrizione della porzione del contratto d'appalto da subappaltare" campo="ADDITIONAL_INFORMATION" />
			
<gene:fnJavaScriptScheda funzione="gestioneNO_AWARDED_TYPE('#W3FS3AWARD_NO_AWARDED_TYPE#')" elencocampi="W3FS3AWARD_NO_AWARDED_TYPE" esegui="true" />
<gene:fnJavaScriptScheda funzione="gestioneSUB_CONTRACTED('#W3FS3AWARD_SUB_CONTRACTED#')" elencocampi="W3FS3AWARD_SUB_CONTRACTED" esegui="true" />
			
<gene:javaScript>

	$("#W3FS3AWARD_NO_AWARDED_TYPE option").each(function() {
		$(this).text($(this).attr("title"));
	});

	function gestioneNO_AWARDED_TYPE(awarded_type){
		if (awarded_type == '1') {
			$("#rowW3FS3AWARD_ORIGINAL").show();
			$("#rowW3FS3AWARD_DATE_ORIGINAL").show();
		} else {
			$("#rowW3FS3AWARD_ORIGINAL").hide();
			$("#rowW3FS3AWARD_DATE_ORIGINAL").hide();
			$("#W3FS3AWARD_ORIGINAL").val("");
			$("#W3FS3AWARD_DATE_ORIGINAL").val("");
		}
	}
	
	function gestioneSUB_CONTRACTED(sub_contracted){
		if (sub_contracted == '1') {
			$("#rowW3FS3AWARD_SUB_VALUE").show();
			$("#rowW3FS3AWARD_SUB_PRCT").show();
			$("#rowW3FS3AWARD_ADDITIONAL_INFORMATION").show();
		} else {
			$("#rowW3FS3AWARD_SUB_VALUE").hide();
			$("#rowW3FS3AWARD_SUB_PRCT").hide();
			$("#rowW3FS3AWARD_ADDITIONAL_INFORMATION").hide();				
			$("#W3FS3AWARD_SUB_VALUE").val("");
			$("#W3FS3AWARD_SUB_PRCT").val("");
			$("#W3FS3AWARD_ADDITIONAL_INFORMATION").val("");
		}
	}
	

	

</gene:javaScript>
