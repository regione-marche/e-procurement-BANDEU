
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


<c:set var="filtroPermessi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "W3SIMAPWS", "ID")}' />

<gene:template file="ricerca-template.jsp" idMaschera="W3SIMAPWS-trova"
	gestisciProtezioni="true" schema="W3">
	<gene:setString name="titoloMaschera" value="Ricerca invii/notifiche" />
	<gene:setString name="entita" value="W3SIMAPWS" />
	<gene:redefineInsert name="corpo">
		<gene:formTrova entita="W3SIMAPWS" gestisciProtezioni="true" >
			<tr>
				<td colspan="3"><b>Informazioni sull'invio</b></td>
			</tr>
			<gene:campoTrova title="Stato di lettura della notifica" campo="STATO" />
			<gene:campoTrova campo="PHASE" />
			<gene:campoTrova campo="SUBMISSION_DATE"/>
			<gene:campoTrova campo="SUBMISSION_ID"/>	
			<gene:campoTrova campo="STATUS_CODE" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSTATUSCODE"/>
			<gene:campoTrova campo="STATUS_DESCRIPTION" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSTATUSDESCRIPTION"/>
			
			<tr>
				<td colspan="3"><b>Informazioni sul bando/avviso inviato</b></td>
			</tr>
			<gene:campoTrova entita="W3SIMAP" campo="FORM" where="W3SIMAPWS.ID=W3SIMAP.ID" />			
			<gene:campoTrova entita="V_W3SIMAP" campo="TITLE_CONTRACT" where="W3SIMAPWS.ID=V_W3SIMAP.ID"/>
			<gene:campoTrova entita="V_W3SIMAP" campo="CTYPE" where="W3SIMAPWS.ID=V_W3SIMAP.ID"/>	

		</gene:formTrova>
	</gene:redefineInsert>
			
	<gene:javaScript>
		$("#Campo3 option").each(function() {
			var _v = $(this).val();
			if (_v == 'SUBMISSION_PENDING' || 
			    _v == 'SUBMITTED' || 
			    _v == 'IN_PREPARATION') {
				$(this).remove();
			}
		});
	</gene:javaScript>
	
</gene:template>
