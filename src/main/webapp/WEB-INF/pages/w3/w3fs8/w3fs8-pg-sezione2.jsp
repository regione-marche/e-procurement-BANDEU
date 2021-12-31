
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

<gene:formScheda entita="W3FS8" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS8" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS8.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS8" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>
	<gene:campoScheda entita="W3FS8S2" campo="ID" visibile="false" where="W3FS8.ID = W3FS8S2.ID"/>
	<gene:campoScheda entita="W3FS8S2" campo="NUM"  visibile="false" defaultValue="1" where="W3FS8.ID = W3FS8S2.ID"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS8.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" where="W3FS8.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs8.II")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs8.II.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs8.II.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Denominazione" entita="W3FS8S2" campo="TITLE_CONTRACT" obbligatorio="true"/>
	<gene:campoScheda entita="W3FS8S2" campo="REFERENCE" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs8.II.1.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS8S2" campo="CPV" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS8S2" title="Vocabolario supplementare 1" campo="CPVSUPP1" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS8S2" title="Vocabolario supplementare 2" campo="CPVSUPP2" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs8.II.1.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS8S2" campo="TYPE_CONTRACT" obbligatorio="true"/>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.II.2")}</b></td>
	</gene:campoScheda>	
		<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.II.2.2")}</b></td>
	</gene:campoScheda>
	<jsp:include page="/WEB-INF/pages/w3/w3cpv/w3cpv-interno-scheda-complementare.jsp">
		<jsp:param name="ent" value='W3ANNEXB'/>
		<jsp:param name="id" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="num" value='1'/>
	</jsp:include>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.II.2.3")}</b></td>
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
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.II.2.4")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda entita="W3ANNEXB" campo="DESCRIPTION" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS8_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	
	
	<gene:fnJavaScriptScheda funzione="gestioneTYPE_CONTRACT('#W3FS8S2_TYPE_CONTRACT#')" elencocampi="W3FS8S2_TYPE_CONTRACT" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	$(window).ready(function (){
		_creaFinestraAlberoCpvVP();
		_creaFinestraAlberoCpvVSUPP();
		_creaLinkAlberoCpvVP($("#W3FS8S2_CPV").parent(), "${modo}", $("#W3FS8S2_CPV"), $("#W3FS8S2_CPVview") );
		_creaLinkAlberoCpvVSUPP($("#W3FS8S2_CPVSUPP1").parent(), "${modo}", $("#W3FS8S2_CPVSUPP1"), $("#W3FS8S2_CPVSUPP1view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS8S2_CPVSUPP2").parent(), "${modo}", $("#W3FS8S2_CPVSUPP2"), $("#W3FS8S2_CPVSUPP2view") );
		
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

	function gestioneTYPE_CONTRACT(type_contract){
		$("#W3SIMAP_CTYPE").val(type_contract);
	}
	
</gene:javaScript>
