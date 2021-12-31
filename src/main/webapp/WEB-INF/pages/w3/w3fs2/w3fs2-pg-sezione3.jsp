
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

<gene:formScheda entita="W3FS2" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS2" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3FS2S36" campo="ID" obbligatorio="true" visibile="false" where="W3FS2.ID = W3FS2S36.ID"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS2.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III")}<br><br></b></td>
	</gene:campoScheda>
	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.III.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.III.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Elenco e breve descrizione delle condizioni" entita="W3FS2S36" campo="OPERATORS_PERSONAL_SITUATION" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III.1.2")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda entita="W3FS2S36" campo="ECO_CRITERIA_DOC" />
	<gene:campoScheda title="Elenco e breve descrizione dei criteri di selezione" entita="W3FS2S36" campo="EAF_CAPACITY_INFORMATION" />
	<gene:campoScheda title="Livelli minimi di capacità eventualmente richiesti" entita="W3FS2S36" campo="EAF_CAPACITY_MIN_LEVEL" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III.1.3")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda entita="W3FS2S36" campo="TECH_CRIT_DOC" />
	<gene:campoScheda title="Elenco e breve descrizione dei criteri di selezione" entita="W3FS2S36" campo="T_CAPACITY_INFORMATION" />
	<gene:campoScheda title="Livelli minimi di capacità eventulamente richiesti" entita="W3FS2S36" campo="T_CAPACITY_MIN_LEVEL" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III.1.5")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Il contratto d'appalto &egrave; limitato a lavoratori protetti e operatori economici il cui obiettivo sia l'integrazione sociale e professionale delle persone disabili e svantaggiate ?" entita="W3FS2S36" campo="RESTRICTED_SHELTERED" />
	<gene:campoScheda title="L'esecuzione del contratto d'appalto avviene nel contesto di programmi di lavoro protetti ?" entita="W3FS2S36" campo="RESTRICTED_FRAME" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.III.2.1")}</b>
		</td>
	</gene:campoScheda>
	<gene:campoScheda title="La prestazione del servizio è riservata ad una particolare professione ?" entita="W3FS2S36" campo="SERVICE_RESERVED" />
	<gene:campoScheda title="Citare le corrispondenti disposizioni legislative, regolamentari o amministrative" entita="W3FS2S36" campo="SERVICE_RES_DESC" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III.2.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3FS2S36" campo="PERFORMANCE_CONDITIONS" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.III.2.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Obbligo di indicare i nomi e le qualifiche professionali del personale incaricato ?" entita="W3FS2S36" campo="REQUEST_NAMES" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS2_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>
	
	<gene:fnJavaScriptScheda funzione="gestioneECO_CRITERIA_DOC('#W3FS2S36_ECO_CRITERIA_DOC#')" elencocampi="W3FS2S36_ECO_CRITERIA_DOC" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneTECH_CRIT_DOC('#W3FS2S36_TECH_CRIT_DOC#')" elencocampi="W3FS2S36_TECH_CRIT_DOC" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneSERVICE_RESERVED('#W3FS2S36_SERVICE_RESERVED#')" elencocampi="W3FS2S36_SERVICE_RESERVED" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	function gestioneECO_CRITERIA_DOC(eco_criteria_doc){
		if (eco_criteria_doc == '2') {
			$("#rowW3FS2S36_EAF_CAPACITY_INFORMATION").show();
			$("#rowW3FS2S36_EAF_CAPACITY_MIN_LEVEL").show();
		} else {
			$("#rowW3FS2S36_EAF_CAPACITY_INFORMATION").hide();
			$("#rowW3FS2S36_EAF_CAPACITY_MIN_LEVEL").hide();
			$("#W3FS2S36_EAF_CAPACITY_INFORMATION").val("");
			$("#W3FS2S36_EAF_CAPACITY_MIN_LEVEL").val("");
		}
	}

	function gestioneTECH_CRIT_DOC(tecn_crit_doc){
		if (tecn_crit_doc == '2') {
			$("#rowW3FS2S36_T_CAPACITY_INFORMATION").show();
			$("#rowW3FS2S36_T_CAPACITY_MIN_LEVEL").show();
		} else {
			$("#rowW3FS2S36_T_CAPACITY_INFORMATION").hide();
			$("#rowW3FS2S36_T_CAPACITY_MIN_LEVEL").hide();
			$("#W3FS2S36_T_CAPACITY_INFORMATION").val("");
			$("#W3FS2S36_T_CAPACITY_MIN_LEVEL").val("");
		}
	}

	function gestioneSERVICE_RESERVED(service_reserved){
		if (service_reserved == '1') {
			$("#rowW3FS2S36_SERVICE_RES_DESC").show();
		} else {
			$("#rowW3FS2S36_SERVICE_RES_DESC").hide();
			$("#W3FS2S36_SERVICE_RES_DESC").val("");
		}
	}
	
	
</gene:javaScript>
