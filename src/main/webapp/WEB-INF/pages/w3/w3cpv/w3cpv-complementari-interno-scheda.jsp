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


<c:set var="primoPuntoVirgola" value="${fn:indexOf(param.chiave,';')}" />
<c:set var="ent" value="${fn:substring(param.chiave, 0, primoPuntoVirgola)}"/>
<c:set var="tmpChiave" value="${fn:substring(param.chiave, primoPuntoVirgola+1, fn:length(param.chiave))}"/>
<c:set var="secondoPuntoVirgola" value="${fn:indexOf(tmpChiave,';')}" />
<c:set var="id" value="${fn:substring(tmpChiave, 0, secondoPuntoVirgola)}"/>
<c:set var="num" value="${fn:substring(tmpChiave, secondoPuntoVirgola + 1, fn:length(tmpChiave))}"/>

<c:choose>
	<c:when test="${param.tipoDettaglio eq 1}">
		<gene:campoScheda title="Contatore" entita="W3CPV" campo="NUMCPV_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CPVNUMCPV" value="${item[0]}"/>
		<gene:campoScheda title="Entità di riferimento" entita="W3CPV" campo="ENT_${param.contatore}" campoFittizio="true" visibile="false" definizione="T30;1;;;W3CPVENT" value="${item[1]}"/>
		<gene:campoScheda title="Identificativo appalto" entita="W3CPV" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3CPVID" value="${item[2]}"/>
		<gene:campoScheda title="Numero progressivo" entita="W3CPV" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CPVNUM" value="${item[3]}"/>
		<gene:campoScheda title="Tipo di oggetto" entita="W3CPV" campo="TIPCPV_${param.contatore}" campoFittizio="true" visibile="false" definizione="N1;0;;;W3CPVTIPCPV" value="${item[4]}"/>
		<gene:campoScheda title="Vocabolario principale" entita="W3CPV" campo="CPV_${param.contatore}" href="#" speciale="true" campoFittizio="true" definizione="T10;0;;;W3CPVCPV" value="${item[5]}" >
			<gene:popupCampo titolo="Dettaglio CPV" href="" />
		</gene:campoScheda>
		<gene:campoScheda title="Vocabolario supplementare 1" entita="W3CPV" campo="CPVSUPP1_${param.contatore}" href="#" speciale="true" campoFittizio="true" definizione="T6;0;;;W3CPVCPVSUPP1" value="${item[6]}" >
			<gene:popupCampo titolo="Dettaglio CPV" href="" />
		</gene:campoScheda>
		<gene:campoScheda title="Vocabolario supplementare 2" entita="W3CPV" campo="CPVSUPP2_${param.contatore}" href="#" speciale="true" campoFittizio="true" definizione="T6;0;;;W3CPVCPVSUPP2" value="${item[7]}" >
			<gene:popupCampo titolo="Dettaglio CPV" href="" />
		</gene:campoScheda>
	</c:when>

	<c:otherwise>
		<gene:campoScheda title="Contatore" entita="W3CPV" campo="NUMCPV_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CPVNUMCPV" />
		<gene:campoScheda title="Entità di riferimento" entita="W3CPV" campo="ENT_${param.contatore}" campoFittizio="true" visibile="false" definizione="T30;1;;;W3CPVENT" value="${ent}"/>
		<gene:campoScheda title="Identificativo appalto" entita="W3CPV" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3CPVID" value="${id}"/>
		<gene:campoScheda title="Numero progressivo" entita="W3CPV" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CPVNUM" value="${num}"/>
		<gene:campoScheda title="Tipo di oggetto" entita="W3CPV" campo="TIPCPV_${param.contatore}" campoFittizio="true" visibile="false" definizione="N1;0;;;W3CPVTIPCPV" value="2"/>
		<gene:campoScheda title="Vocabolario principale" entita="W3CPV" campo="CPV_${param.contatore}" href="#" speciale="true" campoFittizio="true" definizione="T10;0;;;W3CPVCPV" >
			<gene:popupCampo titolo="Dettaglio CPV" href="" />
		</gene:campoScheda>
		<gene:campoScheda title="Vocabolario supplementare 1" entita="W3CPV" campo="CPVSUPP1_${param.contatore}" href="#" speciale="true" campoFittizio="true" definizione="T6;0;;;W3CPVCPVSUPP1" >
			<gene:popupCampo titolo="Dettaglio CPV" href="" />
		</gene:campoScheda>
		<gene:campoScheda title="Vocabolario supplementare 2" entita="W3CPV" campo="CPVSUPP2_${param.contatore}" href="#" speciale="true" campoFittizio="true" definizione="T6;0;;;W3CPVCPVSUPP2" >
			<gene:popupCampo titolo="Dettaglio CPV" href="" />
		</gene:campoScheda>
	</c:otherwise>

</c:choose>

<gene:javaScript>
	$(window).ready(function (){
		_creaLinkAlberoCpvVP($("#W3CPV_CPV_${param.contatore}").parent(), "${modo}", $("#W3CPV_CPV_${param.contatore}"), $("#W3CPV_CPV_${param.contatore}view") );
		_creaLinkAlberoCpvVSUPP($("#W3CPV_CPVSUPP1_${param.contatore}").parent(), "${modo}", $("#W3CPV_CPVSUPP1_${param.contatore}"), $("#W3CPV_CPVSUPP1_${param.contatore}view") );
		_creaLinkAlberoCpvVSUPP($("#W3CPV_CPVSUPP2_${param.contatore}").parent(), "${modo}", $("#W3CPV_CPVSUPP2_${param.contatore}"), $("#W3CPV_CPVSUPP2_${param.contatore}view") );
		
		$("#W3CPV_CPV_${param.contatore}").change(function(){
			$("#W3CPV_MOD_W3CPV_${param.contatore}").val("1");
		});

		$("#W3CPV_CPVSUPP1_${param.contatore}").change(function(){
			$("#W3CPV_MOD_W3CPV_${param.contatore}").val("1");
		});

		$("#W3CPV_CPVSUPP2_${param.contatore}").change(function(){
			$("#W3CPV_MOD_W3CPV_${param.contatore}").val("1");
		});
		
	});
</gene:javaScript>
