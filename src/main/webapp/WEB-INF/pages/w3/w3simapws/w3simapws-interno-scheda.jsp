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

<c:if test="${param.tipoDettaglio eq 1}">
	<gene:campoScheda entita="W3SIMAPWS" campo="ID_${param.contatore}" visibile="false" campoFittizio="true" modificabile="false" definizione="N10;1;;;W3SIMAPWSID" value="${item[0]}"/>
	<gene:campoScheda title="N. invio" entita="W3SIMAPWS" campo="NUM_${param.contatore}" visibile="false" campoFittizio="true" modificabile="false" definizione="N5;1;;;W3SIMAPWSNUM" value="${item[1]}"/>
	<gene:campoScheda entita="W3SIMAPWS" campo="PHASE_${param.contatore}" campoFittizio="true" modificabile="false" 
		definizione="T100;0;W3z71;;W3SIMAPWSPHASE" value="${item[2]}" />
	<gene:campoScheda entita="W3SIMAPWS" campo="SUBMISSION_DATE_${param.contatore}" campoFittizio="true" modificabile="false" definizione="D;0;;DATA_ELDA;W3SIMAPWSSUBMISSDATE" value="${item[3]}"/>
	<gene:campoScheda entita="W3SIMAPWS" campo="SUBMISSION_ID_${param.contatore}" campoFittizio="true" modificabile="false" definizione="T100;0;;;W3SIMAPWSSUBMISSID" value="${item[4]}"/>
	<gene:campoScheda title="Stato" entita="W3SIMAPWS" campo="STATUS_CODE_${param.contatore}" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSTATUSCODE" campoFittizio="true" modificabile="false" definizione="T20;0;;;" value="${item[5]}"/>
	<gene:campoScheda title="Descrizione" entita="W3SIMAPWS" campo="STATUS_DESCRIPTION_${param.contatore}" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSTATUSDESCRIPTION" campoFittizio="true" modificabile="false" definizione="T2000;0;;;W3SIMAPWSSTATUSDESC" value="${item[6]}"/>
	<gene:campoScheda title="Stato aggiornato in data" entita="W3SIMAPWS" campo="REPORT_DATE_${param.contatore}" campoFittizio="true" modificabile="false" definizione="D;0;;;W3SIMAPWSREPORTDATE" value="${item[7]}"/>
</c:if>


