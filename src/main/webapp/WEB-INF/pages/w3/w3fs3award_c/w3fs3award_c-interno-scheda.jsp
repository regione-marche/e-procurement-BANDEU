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
		<gene:campoScheda title="Id" entita="W3FS3AWARD_C" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1" value="${item[0]}"/>
		<gene:campoScheda title="Item" entita="W3FS3AWARD_C" campo="ITEM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1" value="${item[1]}"/>
		<gene:campoScheda title="Numero" entita="W3FS3AWARD_C" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1" value="${item[2]}"/>
		<gene:archivio titolo="Imprese" obbligatorio="true"
			lista="gene/impr/impr-lista-popup.jsp"
			scheda="gene/impr/impr-scheda.jsp"
			schedaPopUp="gene/impr/impr-scheda-popup.jsp"
			campi="IMPR.CODIMP;IMPR.NOMEST"
			chiave="W3FS3AWARD_C_CONTRACTOR_CODIMP_${param.contatore}">
			<gene:campoScheda entita="W3FS3AWARD_C" campo="CONTRACTOR_CODIMP_${param.contatore}" campoFittizio="true" visibile="false" definizione="T10;0" value="${item[3]}"/>
			<gene:campoScheda title="Contraente" obbligatorio="true" campo="NOMEST_${param.contatore}" campoFittizio="true" visibile="true" definizione="T254;0" value="${item[5]}"/>
		</gene:archivio>
		<gene:campoScheda title="Il contraente &egrave; una PMI ?" entita="W3FS3AWARD_C" campo="CONTRACTOR_SME_${param.contatore}" campoFittizio="true" visibile="true" definizione="T2;0;;SN" value="${item[4]}"/>
	</c:when>

	<c:otherwise>
		<gene:campoScheda title="Id" entita="W3FS3AWARD_C" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1" />
		<gene:campoScheda title="Item" entita="W3FS3AWARD_C" campo="ITEM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N5;1" />
		<gene:campoScheda title="Numero" entita="W3FS3AWARD_C" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1" />
		<gene:archivio titolo="Imprese" obbligatorio="true"
			lista="gene/impr/impr-lista-popup.jsp"
			scheda="gene/impr/impr-scheda.jsp"
			schedaPopUp="gene/impr/impr-scheda-popup.jsp"
			campi="IMPR.CODIMP;IMPR.NOMEST"
			chiave="W3FS3AWARD_C_CONTRACTOR_CODIMP_${param.contatore}">
			<gene:campoScheda entita="W3FS3AWARD_C" campo="CONTRACTOR_CODIMP_${param.contatore}" visibile="false" campoFittizio="true" definizione="T10;0" />
			<gene:campoScheda title="Contraente" obbligatorio="true" campo="NOMEST_${param.contatore}" campoFittizio="true" visibile="true" definizione="T254;0" />
		</gene:archivio>
		<gene:campoScheda title="Il contraente &egrave; una PMI ?" entita="W3FS3AWARD_C" campo="CONTRACTOR_SME_${param.contatore}" campoFittizio="true" visibile="true" definizione="T2;0;;SN" />
	</c:otherwise>

</c:choose>
