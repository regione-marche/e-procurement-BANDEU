
<%
	/*
	 * Created on 22/02/2017
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


<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs20.II")}<br><br></b></td>
</gene:campoScheda>

<gene:campoScheda>
	<td colspan="2"><b>${gene:resource("label.simap.fs20.II.2")}</b></td>
</gene:campoScheda>	
<gene:campoScheda>
	<td colspan="2"><b>${gene:resource("label.simap.fs20.II.2.1")}</b></td>
</gene:campoScheda>
<gene:campoScheda title="Denominazione" entita="W3ANNEXB" campo="TITLE" />
<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" />

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs20.II.2.2")}</b></td>
</gene:campoScheda>
<jsp:include page="/WEB-INF/pages/w3/w3cpv/w3cpv-interno-scheda-complementare.jsp">
	<jsp:param name="ent" value='W3ANNEXB'/>
	<jsp:param name="id" value='${param.w3annexb_id}'/>
	<jsp:param name="num" value='${param.w3annexb_num}'/>
</jsp:include>

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs20.II.2.3")}</b></td>
</gene:campoScheda>
<gene:campoScheda title="Codice NUTS 1" entita="W3ANNEXB" campo="SITE_NUTS" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio NUTS" href="" />
</gene:campoScheda>
<gene:campoScheda title="Codice NUTS 2" entita="W3ANNEXB" campo="SITE_NUTS_2" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio NUTS" href="" />
</gene:campoScheda>
<gene:campoScheda title="Codice NUTS 3" entita="W3ANNEXB" campo="SITE_NUTS_3" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio NUTS" href="" />
</gene:campoScheda>
<gene:campoScheda title="Codice NUTS 4" entita="W3ANNEXB" campo="SITE_NUTS_4" href="#" speciale="true" >
	<gene:popupCampo titolo="Dettaglio NUTS" href="" />
</gene:campoScheda>			
<gene:campoScheda entita="W3ANNEXB" campo="SITE_LABEL" />

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs20.II.2.4")}</b></td>
</gene:campoScheda>	
<gene:campoScheda entita="W3ANNEXB" campo="DESCRIPTION" obbligatorio="true"/>

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs20.II.2.7")}</b></td>
</gene:campoScheda>	
<gene:campoScheda entita="W3ANNEXB" campo="WORK_MONTH" />
<gene:campoScheda entita="W3ANNEXB" campo="WORK_DAYS" />
<gene:campoScheda entita="W3ANNEXB" campo="WORK_START_DATE" />
<gene:campoScheda entita="W3ANNEXB" campo="WORK_END_DATE" />
<gene:campoScheda title="Giustificazione per una durata superiore a 4 anni (Direttiva 2014/24/UE) oppure 8 anni (Direttiva 2015/25/UE)" 
	entita="W3ANNEXB" campo="JUSTIFICATION" gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoNote"/>

<gene:campoScheda>
	<td colspan="2"><b><br>${gene:resource("label.simap.fs20.II.2.13")}</b></td>
</gene:campoScheda>	
<gene:campoScheda title="L'appalto &egrave; connesso ad un progetto e/o programma finanziato da fondi dell'Unione Europea ?" entita="W3ANNEXB" campo="EU_PROGR" />
<gene:campoScheda entita="W3ANNEXB" campo="EU_PROGR_DESCR" />

<gene:fnJavaScriptScheda funzione="gestioneEU_PROGR('#W3ANNEXB_EU_PROGR#')" elencocampi="W3ANNEXB_EU_PROGR" esegui="true" />

<gene:javaScript>

	$(window).ready(function (){
		_creaFinestraAlberoCpvVP();
		_creaFinestraAlberoCpvVSUPP();
		
		$("input[name*='CPV']").attr('readonly','readonly');
		$("input[name*='CPV']").attr('tabindex','-1');
		$("input[name*='CPV']").css('border-color','#A3A6FF');
		$("input[name*='CPV']").css('border-width','1px');
		$("input[name*='CPV']").css('background-color','#E0E0E0');
		
		_creaFinestraAlberoNUTS();
		_creaLinkAlberoNUTS($("#W3ANNEXB_SITE_NUTS").parent(), "${modo}", $("#W3ANNEXB_SITE_NUTS"), $("#W3ANNEXB_SITE_NUTSview") );
		_creaLinkAlberoNUTS($("#W3ANNEXB_SITE_NUTS_2").parent(), "${modo}", $("#W3ANNEXB_SITE_NUTS_2"), $("#W3ANNEXB_SITE_NUTS_2view") );
		_creaLinkAlberoNUTS($("#W3ANNEXB_SITE_NUTS_3").parent(), "${modo}", $("#W3ANNEXB_SITE_NUTS_3"), $("#W3ANNEXB_SITE_NUTS_3view") );
		_creaLinkAlberoNUTS($("#W3ANNEXB_SITE_NUTS_4").parent(), "${modo}", $("#W3ANNEXB_SITE_NUTS_4"), $("#W3ANNEXB_SITE_NUTS_4view") );

		$("input[name^='W3ANNEXB_SITE_NUTS']").attr('readonly','readonly');
		$("input[name^='W3ANNEXB_SITE_NUTS']").attr('tabindex','-1');
		$("input[name^='W3ANNEXB_SITE_NUTS']").css('border-color','#A3A6FF');
		$("input[name^='W3ANNEXB_SITE_NUTS']").css('border-width','1px');
		$("input[name^='W3ANNEXB_SITE_NUTS']").css('background-color','#E0E0E0');		
		
	});

	function gestioneEU_PROGR(eu_progr){
		if (eu_progr == '1') {
			$("#rowW3ANNEXB_EU_PROGR_DESCR").show();
		} else {
			$("#rowW3ANNEXB_EU_PROGR_DESCR").hide();
			$("#W3ANNEXB_EU_PROGR_DESCR").val("");
		}
	}
	
	$("#rowLinkAddW3AWCRITERIA td").attr("colspan","6");
	$("#rowMsgLastW3AWCRITERIA td").attr("colspan","6");

	
</gene:javaScript>
