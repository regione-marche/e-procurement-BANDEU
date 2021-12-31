
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

<gene:formScheda entita="W3FS7" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS7"
	plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS7" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS7.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS7" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS7.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs7.II")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.II.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.II.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Denominazione" entita="W3FS7" campo="TITLE_CONTRACT" obbligatorio="true"/>
	<gene:campoScheda entita="W3FS7" campo="REFERENCE" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs7.II.1.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS7" campo="CPV" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Vocabolario supplementare 1" entita="W3FS7" campo="CPVSUPP1" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Vocabolario supplementare 2" entita="W3FS7" campo="CPVSUPP2" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs7.II.1.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS7" campo="TYPE_CONTRACT" obbligatorio="true"/>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS7_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	
	
	<gene:fnJavaScriptScheda funzione="gestioneTYPE_CONTRACT('#W3FS7_TYPE_CONTRACT#')" elencocampi="W3FS7_TYPE_CONTRACT" esegui="true" />

</gene:formScheda>

<gene:javaScript>

	$(window).ready(function (){
		_creaFinestraAlberoCpvVP();
		_creaFinestraAlberoCpvVSUPP();
		_creaLinkAlberoCpvVP($("#W3FS7_CPV").parent(), "${modo}", $("#W3FS7_CPV"), $("#W3FS7_CPVview") );
		_creaLinkAlberoCpvVSUPP($("#W3FS7_CPVSUPP1").parent(), "${modo}", $("#W3FS7_CPVSUPP1"), $("#W3FS7_CPVSUPP1view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS7_CPVSUPP2").parent(), "${modo}", $("#W3FS7_CPVSUPP2"), $("#W3FS7_CPVSUPP2view") );
		
		$("input[name*='CPV']").attr('readonly','readonly');
		$("input[name*='CPV']").attr('tabindex','-1');
		$("input[name*='CPV']").css('border-color','#A3A6FF');
		$("input[name*='CPV']").css('border-width','1px');
		$("input[name*='CPV']").css('background-color','#E0E0E0');
		
	});

	function gestioneTYPE_CONTRACT(type_contract){
		$("#W3SIMAP_CTYPE").val(type_contract);
	}
	
</gene:javaScript>

