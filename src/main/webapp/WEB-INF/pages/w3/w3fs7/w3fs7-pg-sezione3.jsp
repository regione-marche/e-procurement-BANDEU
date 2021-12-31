
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

<gene:formScheda entita="W3FS7" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS7" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS7.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs7.III")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.III.1")}</b></td>
	</gene:campoScheda>

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs7.III.1.5")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Il contratto d'appalto &egrave; limitato a lavoratori protetti e operatori economici il cui obiettivo sia l'integrazione sociale e professionale delle persone disabili e svantaggiate ?" campo="RESTRICTED_SHELTERED" />
	<gene:campoScheda title="L'esecuzione del contratto d'appalto avviene nel contesto di programmi di lavoro protetti ?" campo="RESTRICTED_FRAME" />

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs7.III.1.9")}</b><i>&nbsp;(Sintesi delle condizioni e dei metodi principali)</i></td>
	</gene:campoScheda>	
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneFS7CondizioniFunction" parametro='${gene:getValCampo(key,"ID")}' />	
	<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
		<jsp:param name="entita" value='W3FS7_Q'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3FS7_Q' />
		<jsp:param name="idProtezioni" value="W3FS7_Q" />
		<jsp:param name="sezioneListaVuota" value="true" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3fs7_q/w3fs7_q-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3FS7_Q_ID_', 'W3FS7_Q_NUM_', 'W3FS7_Q_CONDITIONS_', 'W3FS7_Q_METHODS_'"/>
		<jsp:param name="titoloSezione" value="Condizioni" />
		<jsp:param name="titoloNuovaSezione" value="Nuova condizione" />
		<jsp:param name="descEntitaVociLink" value="condizione" />
		<jsp:param name="msgRaggiuntoMax" value="e condizioni"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>


	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs7.III.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs7.III.2.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="SERVICE_RESERVED" />
	<gene:campoScheda campo="SERVICE_RES_DESC" />
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs7.III.2.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="PERFORMANCE_CONDITIONS" />
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs7.III.2.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Obbligo di indicare i nomi e le qualifiche professionali del personale incaricato dell'esecuzione del contratto d'appalto ?" campo="REQUEST_NAMES" />
	
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

	<gene:fnJavaScriptScheda funzione="gestionePARTICULAR_PROFESSION('#W3FS7_PARTICULAR_PROFESSION#')" elencocampi="W3FS7_PARTICULAR_PROFESSION" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>


	function gestionePARTICULAR_PROFESSION(particular_profession){
		if (particular_profession == '1') {
			$("#rowW3FS7_REFERENCE_TO_LAW").show();
		} else {
			$("#rowW3FS7_REFERENCE_TO_LAW").hide();
			$("#W3FS7_REFERENCE_TO_LAW").val("");
		}
	}
	
</gene:javaScript>
