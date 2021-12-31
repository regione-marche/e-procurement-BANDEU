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
		<gene:campoScheda title="Id" entita="W3CRITERIA" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3CRITERIAID" value="${item[0]}"/>
		<gene:campoScheda title="Numero" entita="W3CRITERIA" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CRITERIANUM" value="${item[1]}"/>
		<gene:campoScheda title="Ordinamento" entita="W3CRITERIA" campo="ORDER_C_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;0;;;W3CRITERIAORDER_C" value="${item[2]}"/>
		<gene:campoScheda title="Criterio" entita="W3CRITERIA" campo="CRITERIA_${param.contatore}" campoFittizio="true" definizione="T500;0;;;W3CRITERIACRITERIA" value="${item[3]}"/>
		<gene:campoScheda title="Ponderazione" entita="W3CRITERIA" campo="WEIGHTING_${param.contatore}" campoFittizio="true" definizione="N4;0;;;W3CRITERIAWEIGHTING" value="${item[4]}"/>
	</c:when>

	<c:when test="${param.tipoDettaglio eq 2}">
		<gene:campoScheda title="Id" entita="W3CRITERIA" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3CRITERIAID" value="${param.chiave}"/>
		<gene:campoScheda title="Numero" entita="W3CRITERIA" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CRITERIANUM" />
		<gene:campoScheda title="Ordinamento" entita="W3CRITERIA" campo="ORDER_C_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;0;;;W3CRITERIAORDER_C" />
		<gene:campoScheda title="Criterio" entita="W3CRITERIA" campo="CRITERIA_${param.contatore}" campoFittizio="true" definizione="T500;0;;;W3CRITERIACRITERIA" />
		<gene:campoScheda title="Ponderazione" entita="W3CRITERIA" campo="WEIGHTING_${param.contatore}" campoFittizio="true" definizione="N4;0;;;W3CRITERIAWEIGHTING" />
	</c:when>

	<c:when test="${param.tipoDettaglio eq 3}">
		<gene:campoScheda title="Id" entita="W3CRITERIA" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3CRITERIAID" value="${param.chiave}"/>
		<gene:campoScheda title="Numero" entita="W3CRITERIA" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1;;;W3CRITERIANUM" />
		<gene:campoScheda title="Ordinamento" entita="W3CRITERIA" campo="ORDER_C_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;0;;;W3CRITERIAORDER_C" />
		<gene:campoScheda title="Criterio" entita="W3CRITERIA" campo="CRITERIA_${param.contatore}" campoFittizio="true" definizione="T500;0;;;W3CRITERIACRITERIA" />
		<gene:campoScheda title="Ponderazione" entita="W3CRITERIA" campo="WEIGHTING_${param.contatore}" campoFittizio="true" definizione="N4;0;;;W3CRITERIAWEIGHTING" />
	</c:when>

</c:choose>


