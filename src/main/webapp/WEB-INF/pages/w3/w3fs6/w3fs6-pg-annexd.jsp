
<%
	/*
	 * Created on 08-Ott-2010
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

<gene:formScheda entita="W3FS6" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS6" tableClass="dettaglio-tab-width-50">

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.annexd2")}<br><br></b></td>
	</gene:campoScheda>

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS6.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b>1. Motivazione della scelta della procedura negoziata senza previa pubblicazione di un avviso di indizione di gara, conformemente all'articolo 50, della direttiva 2014/25/UE</b></td>
	</gene:campoScheda>	
	
	<gene:campoScheda entita="W3ANNEXD" campo="ID" visibile="false" where="W3FS6.ID = W3ANNEXD.ID"/>
	<gene:campoScheda title="Non è stata presentata alcuna offerta o alcuna offerta appropriata, né alcuna domanda di partecipazione o alcuna domanda di partecipazione appropriata in risposta ad una procedura con precedente indizione di gara" entita="W3ANNEXD" campo="NO_TENDERS_REQUESTS"/>
	<gene:campoScheda title="L'appalto in questione è destinato solo a scopi di ricerca, di sperimentazione, di studio o di sviluppo, alle condizioni fissate dalla direttiva (unicamente per le forniture)" entita="W3ANNEXD" campo="MANUFACTURED"/>
	<gene:campoScheda title="I lavori, le forniture o i servizi possono essere forniti unicamente da un determinato operatore economico per una delle seguenti ragioni" entita="W3ANNEXD" campo="PARTICULAR_TENDERED"/>
	<gene:campoScheda title="Ragioni di estrema urgenza derivanti da eventi imprevedibili dall'amministrazione aggiudicatrice, nel rispetto delle rigorose condizioni fissate dalla direttiva" entita="W3ANNEXD" campo="EXTREME_URGENCY"/>
	<gene:campoScheda title="Consegne complementari effettuate dal fornitore originario, nel rispetto delle rigorose condizioni fissate dalla direttiva" entita="W3ANNEXD" campo="ADDITIONAL_WORKS"/>
	<gene:campoScheda title="Nuovi lavori o servizi consistenti nella ripetizione di lavori o servizi precedenti, nel rispetto delle rigorose condizioni fissate dalla direttiva" entita="W3ANNEXD" campo="WORKS_REPETITION"/>
	<gene:campoScheda title="Appalto di servizi da aggiudicare al vincitore o a uno dei vincitori in base alle norme previste nel concorso di progettazione" entita="W3ANNEXD" campo="SERVICE_CONTRACT"/>
	<gene:campoScheda title="Forniture quotate e acquistate sul mercato delle materie prime" entita="W3ANNEXD" campo="SUPPLIES_QUOTED"/>
	<gene:campoScheda title="Acquisto di forniture o servizi a condizioni particolarmente vantaggiose" entita="W3ANNEXD" campo="PURCHASE_SUPPLIES"/>
	<gene:campoScheda title="Acquisto di opportunità effettuato approfittando di un'occasione particolarmente vantaggiosa ma di breve durata, ad un prezzo sensibilmente inferiore ai prezzi di mercato" entita="W3ANNEXD" campo="BARGAIN_PURCHASE"/>

	<gene:campoScheda>
		<td colspan="2"><br><b>2. Altre motivazioni per l'aggiudicazione dell'appalto senza previa pubblicazione di un avviso di indizione di gara nella Gazzetta Ufficiale dell'Unione Europea</b></td>
	</gene:campoScheda>	
	<gene:campoScheda entita="W3ANNEXD" campo="OUTSIDE_SCOPE"/>	

	<gene:campoScheda>
		<td colspan="2"><br><b>3. Spiegazione</b></td>
	</gene:campoScheda>	
	<gene:campoScheda hideTitle="true" entita="W3ANNEXD" campo="REASON_CONTRACT_LAWFUL"/>

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS6_ID}"/>
		</jsp:include>
	</gene:campoScheda>

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
</gene:formScheda>


<gene:javaScript>

	$("#W3ANNEXD_PARTICULAR_TENDERED option").each(function() {
		$(this).text($(this).attr("title"));
	});

	$("#W3ANNEXD_PURCHASE_SUPPLIES option").each(function() {
		$(this).text($(this).attr("title"));
	});
	
	$("#W3ANNEXD_PARTICULAR_TENDERED").css("width","350px");
	$("#W3ANNEXD_PURCHASE_SUPPLIES").css("width","350px");
	$("#W3ANNEXD_REASON_CONTRACT_LAWFUL").parent().css("height","30px");
	$("#W3ANNEXD_REASON_CONTRACT_LAWFUL").css("width","750px");

</gene:javaScript>

