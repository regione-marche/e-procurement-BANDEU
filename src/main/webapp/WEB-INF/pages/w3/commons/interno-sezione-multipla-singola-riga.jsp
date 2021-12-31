<%
/*
 * Created on: 21/11/2008
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<% // STRUTTURA DI BASE PER LA GENERAZIONE DI UNA SCHEDA MULTIPLA 

   // ESEMPIO CON TUTTI I PARAMETRI GESTITI
   // <jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
   //   <jsp:param name="entita" value='GARSTR'/>
   //   <jsp:param name="chiave" value='${gene:getValCampo(key, "NGARA")}'/>
   //   <jsp:param name="nomeAttributoLista" value='trattiStrada' />
   //   <jsp:param name="idProtezioni" value="TRSTRADA" />
   //   <jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/gare/garstr/tratto-strada.jsp"/>
   //   <jsp:param name="arrayCampi" value="'GARSTR_NGARA_', 'GARSTR_NUMSTR_', 'GARSTR_CODVIA_', 'ASTRA_VIAPIA_', 'GARSTR_NOTSTR_'"/>
   //   <jsp:param name="titoloSezione" value="Tratto di strada" />
   //   <jsp:param name="titoloNuovaSezione" value="Nuovo tratto di strada" />
   //   <jsp:param name="descEntitaVociLink" value="tratto di strada" />
   //   <jsp:param name="msgRaggiuntoMax" value="i tratti di strada"/>
   //   <jsp:param name="arrayVisibilitaCampi" value=""/>
   //   <jsp:param name="sezioneListaVuota" value="true" />
   //   <jsp:param name="usaContatoreLista value="false"/>
   //   <jsp:param name="numMaxDettagliInseribili" value="5"/>
   //   <jsp:param name="funzEliminazione" value="deleteTratto"/>
   // </jsp:include>
   //
   // i parametri da "arrayVisibilitaCampi" in poi non sono obbligatori e prevedono la valorizzazione di default:
   // arrayVisibilitaCampi = ""
   // sezioneListaVuota = "true"
   // numMaxDettagliInseribili = "5"
   // funzEliminazione = "delElementoSchedaMultipla"
   // sezioneInseribile = "true"
   // sezioneEliminabile = "true"; 
   //
   //
   // NELLE CHIAMATE ALLA PAGINA DI DETTAGLIO ${param.jspDettaglioSingolo}, "tipoDettaglio" VALE: 
   // 1) PER I DETTAGLI DEI RECORD ESISTENTI
   // 2) PER IL DETTAGLIO VUOTO SE NON ESISTE ALCUN RECORD
   // 3) PER LE SEZIONI NASCOSTE PER ULTERIORI INSERIMENTI
   // 
   // NEL CASO DI INSERIMENTO DI CODICE JS SPECIFICO NELLA JSP ${param.jspDettaglioSingolo} 
   // PER LE PAGINE DI MODIFICA, TESTARE IN TALE PAGINA IL PARAMETRO addJs VALORIZZATO A "true" 
   // DOPO L'ULTIMA GENERAZIONE DI UNA NUOVA SCHEDA DI INSERIMENTO.
%>

<fmt:setBundle basename="AliceResources" />

<c:set var="contatore" value="1" scope="page"/>

<c:set var="funzDELProfilo" value="DEL-${param.idProtezioni}" scope="page"/>
<c:set var="funzINSProfilo" value="INS-${param.idProtezioni}" scope="page"/>

<c:set var="arrayVisibilitaCampi" value="" scope="page"/>
<c:if test="${!empty param.arrayVisibilitaCampi}">
	<c:set var="arrayVisibilitaCampi" value="${param.arrayVisibilitaCampi}" scope="page"/>
</c:if>

<c:set var="usaContatoreLista" value="false" scope="page"/>
<c:if test="${!empty param.usaContatoreLista}">
	<c:set var="usaContatoreLista" value="${param.usaContatoreLista}" scope="page"/>
</c:if>

<c:set var="sezioneListaVuota" value="true" scope="page"/>
<c:if test="${!empty param.sezioneListaVuota}">
	<c:set var="sezioneListaVuota" value="${param.sezioneListaVuota}" scope="page"/>
</c:if>

<c:set var="numMaxDettagliInseribili" value="5" scope="page"/>
<c:if test="${!empty param.numMaxDettagliInseribili}">
	<c:set var="numMaxDettagliInseribili" value="${param.numMaxDettagliInseribili}" scope="page"/>
</c:if>

<c:set var="funzEliminazione" value="delElementoSchedaMultipla" scope="page"/>
<c:if test="${!empty param.funzEliminazione}">
	<c:set var="funzEliminazione" value="${param.funzEliminazione}" scope="page"/>
</c:if>

<c:set var="sezioneInseribile" value="true" scope="page"/>
<c:if test="${!empty param.sezioneInseribile}">
	<c:set var="sezioneInseribile" value="${param.sezioneInseribile}" scope="page"/>
</c:if>

<c:set var="sezioneEliminabile" value="true" scope="page"/>
<c:if test="${!empty param.sezioneEliminabile}">
	<c:set var="sezioneEliminabile" value="${param.sezioneEliminabile}" scope="page"/>
</c:if>

<c:choose>
	<c:when test='${not empty requestScope[param.nomeAttributoLista]}'>
		<c:forEach items="${requestScope[param.nomeAttributoLista]}" var="item" varStatus="stato">
			<gene:gruppoCampi idProtezioni="${param.idProtezioni}">
				<gene:campoScheda campo="DEL_${param.idProtezioni}_${contatore}" entita="${param.entita}" campoFittizio="true" visibile="false" definizione="T1" />
				<gene:campoScheda campo="MOD_${param.idProtezioni}_${contatore}" entita="${param.entita}" campoFittizio="true" visibile="false" definizione="T1" />
				<c:set var="nomeSezVis" value="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" />
				<c:choose>
					<c:when test='${empty param[nomeSezVis] or modo eq "VISUALIZZA" or modo eq "NUOVO"}'>
						<input type="hidden" name="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" id="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" value="1" />
					</c:when>
					<c:otherwise>
						<input type="hidden" name="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" id="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" value="${param[nomeSezVis]}" />
					</c:otherwise>
				</c:choose>

				<span id="rowtitolo${param.idProtezioni}_${contatore}" style="padding-top: 0px; padding-bottom: 0px;">
					<c:if test='${sezioneEliminabile and modoAperturaScheda ne "VISUALIZZA" and gene:checkProtFunz(pageContext, "DEL", funzDELProfilo)}'>
						<a href="javascript:${funzEliminazione}(${contatore}, '${param.entita}_DEL_${param.idProtezioni}_', '${param.idProtezioni}', new Array(${param.arrayCampi}))" title="Elimina ${param.descEntitaVociLink}" class="link-generico"><img src='${pageContext.request.contextPath}/img/opzioni_del.gif' height='16' width='16' alt='Elimina ${param.descEntitaVociLink}'></a>
					</c:if>
				</span>

				<c:set var="item" value="${item}" scope="request" />
				<jsp:include page="${param.jspDettaglioSingolo}" >
					<jsp:param name="tipoDettaglio" value="1" />
					<jsp:param name="contatore" value='${contatore}'/>
					<jsp:param name="chiave" value='${param.chiave}'/>
					<jsp:param name="addJs" value='false'/>
				</jsp:include>
				<c:set var="tmp" value="_${contatore}'" />
				<c:set var="elencocampiPerJS" value='${fn:replace(fn:replace(param.arrayCampi, " ", ""), ",", ";")}' />
				<c:set var="elencocampiPerJS" value='${fn:replace(fn:replace(elencocampiPerJS, "_\'", tmp), "\'", "")}' />
				<gene:fnJavaScriptScheda funzione="setModificatoElementoSchedaMultipla(${contatore}, '${param.entita}_MOD_${param.idProtezioni}_', '${param.idProtezioni}', new Array(${param.arrayCampi}))" elencocampi='${elencocampiPerJS}' esegui="false" />
			</gene:gruppoCampi>

			<c:set var="contatore" value="${contatore + 1}" />
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:if test="${sezioneListaVuota}">
			<gene:gruppoCampi idProtezioni="${param.idProtezioni}">
				<gene:campoScheda campo="DEL_${param.idProtezioni}_${contatore}" entita="${param.entita}" campoFittizio="true" visibile="false" definizione="T1" />
				<gene:campoScheda campo="MOD_${param.idProtezioni}_${contatore}" entita="${param.entita}" campoFittizio="true" visibile="false" definizione="T1" />
				<c:set var="nomeSezVis" value="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" />
				<c:choose>
					<c:when test='${empty param[nomeSezVis] or modo eq "VISUALIZZA" or modo eq "NUOVO"}'>
						<input type="hidden" name="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" id="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" value="1" />
					</c:when>
					<c:otherwise>
						<input type="hidden" name="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" id="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" value="${param[nomeSezVis]}" />
					</c:otherwise>
				</c:choose>
				
				<span id="rowtitolo${param.idProtezioni}_${contatore}" style="padding-top: 0px; padding-bottom: 0px;">
					<c:if test='${sezioneEliminabile and modoAperturaScheda ne "VISUALIZZA" and gene:checkProtFunz(pageContext, "DEL", funzDELProfilo)}'>
						<a href="javascript:${funzEliminazione}(${contatore}, '${param.entita}_DEL_${param.idProtezioni}_', '${param.idProtezioni}', new Array(${param.arrayCampi}))" title="Elimina ${param.descEntitaVociLink}" class="link-generico"><img src='${pageContext.request.contextPath}/img/opzioni_del.gif' height='16' width='16' alt='Elimina ${param.descEntitaVociLink}'></a>
					</c:if>
				</span>
				
				<jsp:include page="${param.jspDettaglioSingolo}" >
					<jsp:param name="tipoDettaglio" value="2" />
					<jsp:param name="contatore" value='${contatore}'/>
					<jsp:param name="chiave" value='${param.chiave}'/>
					<jsp:param name="addJs" value='false'/>
				</jsp:include>
				<c:set var="tmp" value="_${contatore}'" />
				<c:set var="elencocampiPerJS" value='${fn:replace(fn:replace(param.arrayCampi, " ", ""), ",", ";")}' />
				<c:set var="elencocampiPerJS" value='${fn:replace(fn:replace(elencocampiPerJS, "_\'", tmp), "\'", "")}' />
				<gene:fnJavaScriptScheda funzione="setModificatoElementoSchedaMultipla(${contatore}, '${param.entita}_MOD_${param.idProtezioni}_', '${param.idProtezioni}', new Array(${param.arrayCampi}))" elencocampi='${elencocampiPerJS}' esegui="false" />
			</gene:gruppoCampi>

			<c:set var="contatore" value="${contatore + 1}" />
		</c:if>
	</c:otherwise>
</c:choose>

<c:if test='${modoAperturaScheda ne "VISUALIZZA"}'>
	<c:forEach var="ELEMENTOINSERIBILE" begin="1" end="${numMaxDettagliInseribili}" varStatus="stato">

		<gene:gruppoCampi idProtezioni="${param.idProtezioni}">
			<gene:campoScheda campo="DEL_${param.idProtezioni}_${contatore}" entita="${param.entita}" campoFittizio="true" visibile="false" definizione="T1" />
			<gene:campoScheda campo="MOD_${param.idProtezioni}_${contatore}" entita="${param.entita}" campoFittizio="true" visibile="false" definizione="T1" />
			<c:set var="nomeSezVis" value="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" />
			<c:choose>
				<c:when test='${empty param[nomeSezVis] or modo eq "VISUALIZZA" or modo eq "NUOVO"}'>
					<input type="hidden" name="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" id="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" value="0" />
				</c:when>
				<c:otherwise>
					<input type="hidden" name="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" id="elementoSchedaMultiplaVisibile${param.idProtezioni}_${contatore}" value="${param[nomeSezVis]}" />
				</c:otherwise>
			</c:choose>
			
			<span id="rowtitolo${param.idProtezioni}_${contatore}" style="padding-top: 0px; padding-bottom: 0px;">
				<c:if test='${sezioneEliminabile and modoAperturaScheda ne "VISUALIZZA" and gene:checkProtFunz(pageContext, "DEL", funzDELProfilo)}'>
					<a href="javascript:${funzEliminazione}(${contatore}, '${param.entita}_DEL_${param.idProtezioni}_', '${param.idProtezioni}', new Array(${param.arrayCampi}))" title="Elimina ${param.descEntitaVociLink}" class="link-generico"><img src='${pageContext.request.contextPath}/img/opzioni_del.gif' height='16' width='16' alt='Elimina ${param.descEntitaVociLink}'></a>
				</c:if>
			</span>

				<jsp:include page="${param.jspDettaglioSingolo}" >
					<jsp:param name="tipoDettaglio" value="3" />
					<jsp:param name="contatore" value='${contatore}'/>
					<jsp:param name="chiave" value='${param.chiave}'/>
					<jsp:param name="addJs" value='${gene:if(stato.last, "true", "false")}'/>
				</jsp:include>

			<c:set var="tmp" value="_${contatore}'" />
			<c:set var="elencocampiPerJS" value='${fn:replace(fn:replace(param.arrayCampi, " ", ""), ",", ";")}' />
			<c:set var="elencocampiPerJS" value='${fn:replace(fn:replace(elencocampiPerJS, "_\'", tmp), "\'", "")}' />
			<gene:fnJavaScriptScheda funzione="setModificatoElementoSchedaMultipla(${contatore}, '${param.entita}_MOD_${param.idProtezioni}_', '${param.idProtezioni}', new Array(${param.arrayCampi}))" elencocampi='${elencocampiPerJS}' esegui="false" />
		</gene:gruppoCampi>

		<c:set var="contatore" value="${contatore + 1}" />
	</c:forEach>

	<c:if test='${sezioneInseribile and gene:checkProtFunz(pageContext, "INS", funzINSProfilo)}'>
		<gene:campoScheda nome="LinkAdd${param.idProtezioni}">
			<td colspan="2" class="valore-dato">
				<a href="javascript:showNextElementoSchedaMultipla('${param.idProtezioni}', new Array(${param.arrayCampi}), new Array(${arrayVisibilitaCampi}));" class="link-generico"><img src="${pageContext.request.contextPath}/img/opzioni_add.gif" title="Aggiungi ${param.descEntitaVociLink}" alt="Aggiungi ${param.descEntitaVociLink}" height="16" width="16"></a>&nbsp;
				<a href="javascript:showNextElementoSchedaMultipla('${param.idProtezioni}', new Array(${param.arrayCampi}), new Array(${arrayVisibilitaCampi}));" class="link-generico">Aggiungi ${param.descEntitaVociLink}</a>
			</td>
		</gene:campoScheda>
		<gene:campoScheda nome="MsgLast${param.idProtezioni}">
			<td colspan="2" class="valore-dato">
				<fmt:message key="info.scheda.modifica.raggiuntoMaxDatiInseribili">
					<fmt:param value="${param.msgRaggiuntoMax}"/>
				</fmt:message>
			</td>
		</gene:campoScheda>
	</c:if>
	<gene:campoScheda campo="NUMERO_${param.idProtezioni}" campoFittizio="true" visibile="false" definizione="N3" value="${contatore-1}" />
	<gene:campoScheda campo="INDICE_${param.idProtezioni}" campoFittizio="true" visibile="false" definizione="N3" />
</c:if>

<gene:javaScript>

	// Variabili Javascript globali necessarie alle sezioni per '${param.titoloSezione}'
<c:choose>
	<c:when test='${empty requestScope[param.nomeAttributoLista]}'>
		var lastId${param.idProtezioni}Visualizzata = 0;
		<c:if test="${sezioneListaVuota}">
		// se è prevista la gestione di almeno un elemento vuoto, allora si incrementa la sezione
		lastId${param.idProtezioni}Visualizzata += 1;
		</c:if>
	</c:when>
	<c:otherwise>
		var lastId${param.idProtezioni}Visualizzata= ${fn:length(requestScope[param.nomeAttributoLista])};
	</c:otherwise>
</c:choose>
		var maxId${param.idProtezioni}Visualizzabile = lastId${param.idProtezioni}Visualizzata + ${numMaxDettagliInseribili};

<c:if test='${(modoAperturaScheda ne "VISUALIZZA")}'>
	
	if (getValue("INDICE_${param.idProtezioni}") != "") lastId${param.idProtezioni}Visualizzata = new Number(getValue("INDICE_${param.idProtezioni}"));
	// Cambio lo stato della variabile globale 'controlloSezioniDinamiche'
	// per attivare i controlli sulle sezioni dinamiche presenti nella
	// pagina al momento del salvataggio
	controlloSezioniDinamiche = true;
	var arrayCampiSezioneDinamica${param.idProtezioni} = new Array(${param.arrayCampi});
	arraySezioniDinamicheObj.push(new SezioneDinamicaObj(arrayCampiSezioneDinamica${param.idProtezioni}, maxId${param.idProtezioni}Visualizzabile, "rowtitolo${param.idProtezioni}_"));

	// inizializzazione della visualizzazione della pagina
	if (getValue("NUMERO_${param.idProtezioni}") == getValue("INDICE_${param.idProtezioni}")) 
		showObj("rowLinkAdd${param.idProtezioni}", false);
	else
		showObj("rowMsgLast${param.idProtezioni}", false);

<c:choose>
	<c:when test="${sezioneListaVuota}" >
		var indiceProgressivo = 2;
	</c:when>
	<c:otherwise>
		var indiceProgressivo = 1;
	</c:otherwise>
</c:choose>

	for(indiceProgressivo; indiceProgressivo <= lastId${param.idProtezioni}Visualizzata; indiceProgressivo++){
		var valore = document.getElementById("elementoSchedaMultiplaVisibile${param.idProtezioni}_" + indiceProgressivo).value;
		if("" == valore || valore == 0){
			document.getElementById("elementoSchedaMultiplaVisibile${param.idProtezioni}_" + indiceProgressivo).value = 0;
			hideElementoSchedaMultipla(indiceProgressivo, "${param.idProtezioni}", new Array(${param.arrayCampi}), false);
		} else {
			document.getElementById("elementoSchedaMultiplaVisibile${param.idProtezioni}_" + indiceProgressivo).value = 1;
		}
	}
	
	for(indiceProgressivo= lastId${param.idProtezioni}Visualizzata + 1; indiceProgressivo <= maxId${param.idProtezioni}Visualizzabile; indiceProgressivo++){
		var valore = document.getElementById("elementoSchedaMultiplaVisibile${param.idProtezioni}_" + indiceProgressivo).value;
		if("" == valore || valore == 0){
			document.getElementById("elementoSchedaMultiplaVisibile${param.idProtezioni}_" + indiceProgressivo).value = 0;
			hideElementoSchedaMultipla(indiceProgressivo, "${param.idProtezioni}", new Array(${param.arrayCampi}), false);
		} else {
			document.getElementById("elementoSchedaMultiplaVisibile${param.idProtezioni}_" + indiceProgressivo).value = 1;
			showElementoSchedaMultipla(indiceProgressivo, '${param.idProtezioni}', new Array(${param.arrayCampi}), new Array(${arrayVisibilitaCampi}));
		}
	}

</c:if>
</gene:javaScript>