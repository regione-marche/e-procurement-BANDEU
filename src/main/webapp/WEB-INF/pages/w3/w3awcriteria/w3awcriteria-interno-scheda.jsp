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

<c:choose>
	<c:when test="${param.tipoDettaglio eq 1}">
		<gene:campoScheda addTr="false">
			<tr id="rowW3AWCRITERIA_CRITERIA_TYPE_${param.contatore}">
		</gene:campoScheda>
		<gene:campoScheda addTr="false" hideTitle="true" title="Id" entita="W3AWCRITERIA" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1" value="${item[0]}"/>
		<gene:campoScheda addTr="false" hideTitle="true" title="Numero" entita="W3AWCRITERIA" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1" value="${item[1]}"/>
		<gene:campoScheda addTr="false" hideTitle="true" title="Ordinamento" entita="W3AWCRITERIA" campo="ACNUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1" value="${item[2]}"/>
		<gene:campoScheda addTr="false" hideTitle="true" title="Tipo" entita="W3AWCRITERIA" campo="CRITERIA_TYPE_${param.contatore}" campoFittizio="true" definizione="T5;0;W3z74" value="${item[3]}"/>
		<gene:campoScheda addTr="false" hideTitle="true" title="Criterio" entita="W3AWCRITERIA" campo="CRITERIA_${param.contatore}" campoFittizio="true" definizione="T200;0" value="${item[4]}"/>
		<gene:campoScheda addTr="false" hideTitle="true" title="Ponderazione" entita="W3AWCRITERIA" campo="WEIGHTING_${param.contatore}" campoFittizio="true" definizione="N4;0" value="${item[5]}"/>
		<gene:campoScheda>
			</tr>
		</gene:campoScheda>
	</c:when>
	<c:otherwise>
		<gene:campoScheda addTr="false">
			<tr id="rowW3AWCRITERIA_CRITERIA_TYPE_${param.contatore}">
		</gene:campoScheda>
		<gene:campoScheda addTr="false" hideTitle="true" title="Id" entita="W3AWCRITERIA" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1" />
		<gene:campoScheda addTr="false" hideTitle="true" title="Numero" entita="W3AWCRITERIA" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1" />
		<gene:campoScheda addTr="false" hideTitle="true" title="Ordinamento" entita="W3AWCRITERIA" campo="ACNUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1" />
		<gene:campoScheda addTr="false" hideTitle="true" title="Tipo" entita="W3AWCRITERIA" campo="CRITERIA_TYPE_${param.contatore}" campoFittizio="true" definizione="T5;0;W3z74" />		
		<gene:campoScheda addTr="false" hideTitle="true" title="Criterio" entita="W3AWCRITERIA" campo="CRITERIA_${param.contatore}" campoFittizio="true" definizione="T200;0" />
		<gene:campoScheda addTr="false" hideTitle="true" title="Ponderazione" entita="W3AWCRITERIA" campo="WEIGHTING_${param.contatore}" campoFittizio="true" definizione="N4;0" />
		<gene:campoScheda>
			</tr>
		</gene:campoScheda>
	</c:otherwise>
</c:choose>

<gene:fnJavaScriptScheda funzione="gestioneCRITERIA_TYPE_${param.contatore}('#W3AWCRITERIA_CRITERIA_TYPE_${param.contatore}#')" elencocampi="W3AWCRITERIA_CRITERIA_TYPE_${param.contatore}" esegui="true" />

<gene:javaScript>
	$("#W3AWCRITERIA_CRITERIA_TYPE_${param.contatore}").removeClass("valore-dato");
	$("#W3AWCRITERIA_CRITERIA_${param.contatore}").removeClass("valore-dato");
	$("#W3AWCRITERIA_WEIGHTING_${param.contatore}").removeClass("valore-dato");
	
	$("#jsPopUpW3AWCRITERIA_CRITERIA_TYPE_${param.contatore}").hide();
	$("#jsPopUpW3AWCRITERIA_CRITERIA_${param.contatore}").hide();
	$("#jsPopUpW3AWCRITERIA_WEIGHTING_${param.contatore}").hide();
	
	<c:if test="${modo ne 'VISUALIZZA'}">
		$('#W3AWCRITERIA_WEIGHTING_${param.contatore}').parent()
		.append(('&nbsp;&nbsp;'))
		.append($('#rowtitoloW3AWCRITERIA_${param.contatore}'));
	</c:if>
	
	$("#rowW3AWCRITERIA_CRITERIA_TYPE_${param.contatore}").find("td:eq(0)").css("width","80px");
	$("#rowW3AWCRITERIA_CRITERIA_TYPE_${param.contatore}").find("td:eq(1)").css("width","300px");
	$("#rowW3AWCRITERIA_CRITERIA_TYPE_${param.contatore}").find("td:eq(2)").css("width","80px");
	
	function gestioneCRITERIA_TYPE_${param.contatore}(criteria_type){
		if (criteria_type == 'P') {
			$("#W3AWCRITERIA_CRITERIA_${param.contatore}").hide();
		} else {
			$("#W3AWCRITERIA_CRITERIA_${param.contatore}").show();
		}
	}
	
	

</gene:javaScript>


