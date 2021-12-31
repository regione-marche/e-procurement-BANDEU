
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

<gene:formScheda entita="W3FS7" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS7" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS7.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs7.IV")}<br><br></b></td>
	</gene:campoScheda>

	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.IV.1.6")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="IS_ELECTRONIC" />
	<gene:campoScheda campo="USE_ELECTRONIC" />

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs7.IV.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.IV.2.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="NOTICE_NUMBER_OJ" />	

	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.IV.2.4")}</b></td>
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
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS7_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>

	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	

	<gene:fnJavaScriptScheda funzione="gestioneISELECTRONIC('#W3FS7_IS_ELECTRONIC#')" elencocampi="W3FS7_IS_ELECTRONIC" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	
	function gestioneISELECTRONIC(iselectronic){
		if (iselectronic == '1') {
			$("#rowW3FS7_USE_ELECTRONIC").show();
		} else {
			$("#rowW3FS7_USE_ELECTRONIC").hide();
			$("#W3FS7_USE_ELECTRONIC").val("");
		}
	}
	
</gene:javaScript>
