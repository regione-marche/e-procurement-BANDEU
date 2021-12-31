
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

<gene:formScheda entita="W3FS4" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS4" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3FS4S2" campo="ID" visibile="false" where="W3FS4.ID = W3FS4S2.ID AND W3FS4S2.NUM = 1"/>
	<gene:campoScheda entita="W3FS4S2" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS4.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs4.IV")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_PROCEDURE" />
	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.1.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="FRAMEWORK" />
	<gene:campoScheda campo="FR_SEVERAL_OP" />
	<gene:campoScheda campo="FR_NB_PARTECIPANTS" />
	<gene:campoScheda campo="FR_JUSTIFICATION" />
	<gene:campoScheda campo="DPS" />
	<gene:campoScheda campo="DPS_ADD" />
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.1.6")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="EAUCTION" />
	<gene:campoScheda campo="EAUCTION_INFO" />

	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.1.8")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="GPA" />	

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs4.IV.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.2.2")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="DATE_RECEIPT" />	
	<gene:campoScheda campo="TIME_RECEIPT" />	
	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.2.4")}</b></td>
	</gene:campoScheda>
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneLanguageFunction" parametro='${gene:getValCampo(key,"ID")}' />	
	<jsp:include page="/WEB-INF/pages/w3/commons/interno-sezione-multipla-singola-riga.jsp" >
		<jsp:param name="entita" value='W3LANGUAGE'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3LANGUAGE' />
		<jsp:param name="idProtezioni" value="W3LANGUAGE" />
		<jsp:param name="sezioneListaVuota" value="false" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3language/w3language-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3LANGUAGE_ID_', 'W3LANGUAGE_NUM_', 'W3LANGUAGE_LANGUAGE_EC_'"/>
		<jsp:param name="titoloSezione" value="Lingue ufficiale UE" />
		<jsp:param name="titoloNuovaSezione" value="Nuovo lingua" />
		<jsp:param name="descEntitaVociLink" value="lingua" />
		<jsp:param name="msgRaggiuntoMax" value="e lingue"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>

	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs4.IV.2.5")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="DATE_AWARD" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS4_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>

	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	

	<gene:fnJavaScriptScheda funzione="gestioneFRAMEWORK('#W3FS4_FRAMEWORK#')" elencocampi="W3FS4_FRAMEWORK" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneFR_SEVERAL_OP('#W3FS4_FR_SEVERAL_OP#')" elencocampi="W3FS4_FR_SEVERAL_OP" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneDPS('#W3FS4_DPS#')" elencocampi="W3FS4_DPS" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneEAUCTION('#W3FS4_EAUCTION#')" elencocampi="W3FS4_EAUCTION" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	function gestioneFRAMEWORK(framework){
		if (framework == '1') {
			$("#rowW3FS4_FR_SEVERAL_OP").show();
			$("#rowW3FS4_FR_JUSTIFICATION").show();
		} else {
			$("#rowW3FS4_FR_SEVERAL_OP").hide();
			$("#rowW3FS4_FR_NB_PARTECIPANTS").hide();
			$("#rowW3FS4_FR_JUSTIFICATION").hide();
			$("#W3FS4_FR_SEVERAL_OP").val("");
			$("#W3FS4_FR_NB_PARTECIPANTS").val("");
			$("#W3FS4_FR_JUSTIFICATION").val("");
		}
	}

	function gestioneFR_SEVERAL_OP(fr_several_op){
		if (fr_several_op == '1') {
			$("#rowW3FS4_FR_NB_PARTECIPANTS").show();
		} else {
			$("#rowW3FS4_FR_NB_PARTECIPANTS").hide();
			$("#W3FS4_FR_NB_PARTECIPANTS").val("");
		}
	}

	function gestioneDPS(dps){
		if (dps == '1') {
			$("#rowW3FS4_DPS_ADD").show();
		} else {
			$("#rowW3FS4_DPS_ADD").hide();
			$("#W3FS4_DPS_ADD").val("");
		}
	}

	function gestioneEAUCTION(eauction){
		if (eauction == '1') {
			$("#rowW3FS4_EAUCTION_INFO").show();
		} else {
			$("#rowW3FS4_EAUCTION_INFO").hide();
			$("#W3FS4_EAUCTION_INFO").val("");
		}
	}
	
</gene:javaScript>
