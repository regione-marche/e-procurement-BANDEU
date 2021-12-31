
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
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV")}<br><br></b></td>
	</gene:campoScheda>	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.IV.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.IV.1.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_PROCEDURE" obbligatorio="true"/>
	<gene:campoScheda campo="ACCELERATED" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.1.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="L'avviso comporta la conclusione di un accordo quadro ?" campo="FRAMEWORK" />
	<gene:campoScheda campo="FR_SEVERAL_OP" />
	<gene:campoScheda title="Numero massimo partecipanti all'accordo quadro" campo="FRAME_OPERATORS_NUMBER" />
	<gene:campoScheda campo="DPS" />
	<gene:campoScheda campo="DPS_ADD" />
	<gene:campoScheda title="In caso di accordi quadro, giustificazione per una durata superiore a 4 anni" campo="FRAME_JUSTIFICATION" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.1.4")}</i></td>
	</gene:campoScheda>
	<gene:campoScheda title="Ricorso ad una procedura in più fasi al fine di ridurre progressivamente il numero di soluzioni da discutere o di offerte da negoziare ?" campo="REDUCTION" />
	
	<gene:campoScheda addTr="false">
		<tr id="rowIV15">
			<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.1.5")}</i></td>
		</tr>
	</gene:campoScheda>
	<gene:campoScheda title="L'amministrazione aggiudicatrice si riserva la facolt&agrave; di aggiudicare il contratto d'appalto sulla base delle offerte iniziali senza condurre una negoziazione ?" campo="RIGHT_CONTRACT" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.1.6")}</i></td>
	</gene:campoScheda>
	<gene:campoScheda campo="IS_ELECTRONIC" />	
	<gene:campoScheda campo="USE_ELECTRONIC" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.1.8")}</i></td>
	</gene:campoScheda>
	<gene:campoScheda campo="GPA" />		
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.2")}</i></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs2.IV.2.1")}</i></td>
	</gene:campoScheda>
	<gene:campoScheda campo="NOTICE_NUMBER_OJ" />
		
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.2.2")}</i></td>
	</gene:campoScheda>
	<gene:campoScheda title="Data" campo="RECEIPT_LIMIT_DATE" />
	<gene:campoScheda title="Ora" campo="RECEIPT_LIMIT_TIME" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.2.3")}</i></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Data" campo="DISPATCH_INVITATIONS_DATE" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.2.4")}</i></td>
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
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.2.6")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="L'offerta deve essere valida fino al" campo="UNTIL_DATE" />
	<gene:campoScheda title="Durata in mesi dal termine ultimi per il ricevimento delle offerte" campo="PERIOD_MONTH" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.IV.2.7")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Data" campo="OPENING_TENDERS_DATE" />
	<gene:campoScheda title="Ora" campo="OPENING_TENDERS_TIME" />
	<gene:campoScheda title="Luogo" campo="OPENING_TENDERS_PLACE" />
	<gene:campoScheda title="Informazioni relative alle persone ammesse e alla procedura di apertura" campo="AUTHORISED_PERSONS_DESC" />
	
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
	
	<gene:fnJavaScriptScheda funzione="gestioneTYPE_PROCEDURE('#W3FS2_TYPE_PROCEDURE#')" elencocampi="W3FS2_TYPE_PROCEDURE" esegui="true" />	
	<gene:fnJavaScriptScheda funzione="gestioneFRAMEWORK('#W3FS2_FRAMEWORK#')" elencocampi="W3FS2_FRAMEWORK" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneFR_SEVERAL_OP('#W3FS2_FR_SEVERAL_OP#')" elencocampi="W3FS2_FR_SEVERAL_OP" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneDPS('#W3FS2_DPS#')" elencocampi="W3FS2_DPS" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneIS_ELECTRONIC('#W3FS2_IS_ELECTRONIC#')" elencocampi="W3FS2_IS_ELECTRONIC" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	function gestioneTYPE_PROCEDURE(type_procedure) {
		if (type_procedure == '3' || type_procedure == '5' || type_procedure == '7') {
			$("#rowW3FS2_ACCELERATED").show();					
		} else {
			$("#rowW3FS2_ACCELERATED").hide();
			$("#W3FS2_ACCELERATED").val("");
		}
		
		if (type_procedure == '4' || type_procedure == '5') {
			$("#rowIV15").show();
			$("#rowW3FS2_RIGHT_CONTRACT").show();
		} else {
			$("#rowIV15").hide();
			$("#rowW3FS2_RIGHT_CONTRACT").hide();
			$("#W3FS2_RIGHT_CONTRACT").val("");
		}
	}


	function gestioneFRAMEWORK(framework){
		if (framework == '1') {
			$("#rowW3FS2_FR_SEVERAL_OP").show();
			$("#rowW3FS2_FRAME_JUSTIFICATION").show();
		} else {
			$("#rowW3FS2_FR_SEVERAL_OP").hide();
			$("#rowW3FS2_FRAME_OPERATORS_NUMBER").hide();
			$("#rowW3FS2_FRAME_JUSTIFICATION").hide();
			$("#W3FS2_FR_SEVERAL_OP").val("");
			$("#W3FS2_FRAME_OPERATORS_NUMBER").val("");
			$("#W3FS2_FRAME_JUSTIFICATION").val("");
		}
	}
	
	function gestioneFR_SEVERAL_OP(fr_several_op){
		if (fr_several_op == '1') {
			$("#rowW3FS2_FRAME_OPERATORS_NUMBER").show();
		} else {
			$("#rowW3FS2_FRAME_OPERATORS_NUMBER").hide();
			$("#W3FS2_FRAME_OPERATORS_NUMBER").val("");
		}
	}
	
	function gestioneDPS(dps){
		if (dps == '1') {
			$("#rowW3FS2_DPS_ADD").show();
		} else {
			$("#rowW3FS2_DPS_ADD").hide();
			$("#W3FS2_DPS_ADD").val("");
		}
	}
	
	function gestioneIS_ELECTRONIC(is_electronic){
		if (is_electronic == '1') {
			$("#rowW3FS2_USE_ELECTRONIC").show();
		} else {
			$("#rowW3FS2_USE_ELECTRONIC").hide();
			$("#W3FS2_USE_ELECTRONIC").val("");
		}
	}

	
</gene:javaScript>
