
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

<gene:formScheda entita="W3FS1" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS1" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3FS1S2" campo="ID" visibile="false" where="W3FS1.ID = W3FS1S2.ID AND W3FS1S2.NUM = 1"/>
	<gene:campoScheda entita="W3FS1S2" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS1.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs1.III")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs1.III.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs1.III.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="SUITABILITY" />

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs1.III.1.2")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="ECO_CRITERIA_DOC" />
	<gene:campoScheda campo="ECO_FIN_INFO" />
	<gene:campoScheda campo="ECO_FIN_MIN_LEVEL" />

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs1.III.1.3")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="TECH_CRIT_DOC" />
	<gene:campoScheda campo="TECH_PROF_INFO" />
	<gene:campoScheda campo="TECH_PROF_MIN_LEVEL" />

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs1.III.1.5")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Il contratto d'appalto &egrave; limitato a lavoratori protetti e operatori economici il cui obiettivo sia l'integrazione sociale e professionale delle persone disabili e svantaggiate ?" campo="RESTRICTED_SHELTERED" />
	<gene:campoScheda title="L'esecuzione del contratto d'appalto avviene nel contesto di programmi di lavoro protetti ?" campo="RESTRICTED_FRAME" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs1.III.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs1.III.2.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="PARTICULAR_PROFESSION" />
	<gene:campoScheda campo="REFERENCE_TO_LAW" />
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs1.III.2.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="PERFORMANCE_CONDITIONS" />
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs1.III.2.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="PERFORMANCE_STAFF" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS1_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	

	<gene:fnJavaScriptScheda funzione="gestioneECO_CRITERIA_DOC('#W3FS1_ECO_CRITERIA_DOC#')" elencocampi="W3FS1_ECO_CRITERIA_DOC" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneTECH_CRIT_DOC('#W3FS1_TECH_CRIT_DOC#')" elencocampi="W3FS1_TECH_CRIT_DOC" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestionePARTICULAR_PROFESSION('#W3FS1_PARTICULAR_PROFESSION#')" elencocampi="W3FS1_PARTICULAR_PROFESSION" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	function gestioneECO_CRITERIA_DOC(eco_criteria_doc){
		if (eco_criteria_doc == '2') {
			$("#rowW3FS1_ECO_FIN_INFO").show();
			$("#rowW3FS1_ECO_FIN_MIN_LEVEL").show();
		} else {
			$("#rowW3FS1_ECO_FIN_INFO").hide();
			$("#rowW3FS1_ECO_FIN_MIN_LEVEL").hide();
			$("#W3FS1_ECO_FIN_INFO").val("");
			$("#W3FS1_ECO_FIN_MIN_LEVEL").val("");
		}
	}

	function gestioneTECH_CRIT_DOC(tecn_crit_doc){
		if (tecn_crit_doc == '2') {
			$("#rowW3FS1_TECH_PROF_INFO").show();
			$("#rowW3FS1_TECH_PROF_MIN_LEVEL").show();
		} else {
			$("#rowW3FS1_TECH_PROF_INFO").hide();
			$("#rowW3FS1_TECH_PROF_MIN_LEVEL").hide();
			$("#W3FS1_TECH_PROF_INFO").val("");
			$("#W3FS1_TECH_PROF_MIN_LEVEL").val("");
		}
	}

	function gestionePARTICULAR_PROFESSION(particular_profession){
		if (particular_profession == '1') {
			$("#rowW3FS1_REFERENCE_TO_LAW").show();
		} else {
			$("#rowW3FS1_REFERENCE_TO_LAW").hide();
			$("#W3FS1_REFERENCE_TO_LAW").val("");
		}
	}
	
</gene:javaScript>
