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
		<gene:campoScheda title="Id" entita="W3LANGUAGE" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3LANGUAGEID" value="${item[0]}"/>
		<gene:campoScheda title="Numero" entita="W3LANGUAGE" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1;;;W3LANGUAGENUM" value="${item[1]}"/>
		<gene:campoScheda title="Lingua ufficiale UE" entita="W3LANGUAGE" campo="LANGUAGE_EC_${param.contatore}" obbligatorio="true" campoFittizio="true" visibile="true" definizione="T5;0;W3z69;;W3LANGUAGE_EC" value="${item[2]}"/>
	</c:when>

	<c:when test="${param.tipoDettaglio eq 2}">
		<gene:campoScheda title="Id" entita="W3LANGUAGE" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3LANGUAGEID" value="${param.chiave}"/>
		<gene:campoScheda title="Numero" entita="W3LANGUAGE" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1;;;W3LANGUAGENUM" />
		<gene:campoScheda title="Lingua ufficiale UE" entita="W3LANGUAGE" campo="LANGUAGE_EC_${param.contatore}" obbligatorio="true" campoFittizio="true" visibile="true" definizione="T5;0;W3z69;;W3LANGUAGE_EC" />
	</c:when>

	<c:when test="${param.tipoDettaglio eq 3}">
		<gene:campoScheda title="Id" entita="W3LANGUAGE" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1;;;W3LANGUAGEID" value="${param.chiave}"/>
		<gene:campoScheda title="Numero" entita="W3LANGUAGE" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1;;;W3LANGUAGENUM" />
		<gene:campoScheda title="Lingua ufficiale UE" entita="W3LANGUAGE" campo="LANGUAGE_EC_${param.contatore}" obbligatorio="true" campoFittizio="true" visibile="true" definizione="T5;0;W3z69;;W3LANGUAGE_EC" />
	</c:when>

</c:choose>


<gene:javaScript>

	<c:if test="${modo ne 'VISUALIZZA'}">
		$('#W3LANGUAGE_LANGUAGE_EC_${param.contatore}').parent()
		.append(('&nbsp;&nbsp;'))
		.append($('#rowtitoloW3LANGUAGE_${param.contatore}'));
	</c:if>
	
</gene:javaScript>