<%
/*
 * Created on: 13-lug-2008
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Interno della scheda dell'ufficio intestatario */
%>

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<fmt:setBundle basename="AliceResources" />
<c:set var="nomeEntitaSingolaParametrizzata">
	<fmt:message key="label.tags.uffint.singolo" />
</c:set>
<c:set var="isCodificaAutomatica" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.IsCodificaAutomaticaFunction", pageContext, "UFFINT", "CODEIN")}'/>
<c:set var="obbligatorioCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "UFFINT","CFEIN")}'/>
<c:set var="obbligatorioPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "UFFINT","IVAEIN")}'/>
<c:set var="obbligatoriaCorrettezzaCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "UFFINT","CFEIN")}'/>
<c:set var="obbligatoriaCorrettezzaPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "UFFINT","IVAEIN")}'/>

<gene:formScheda entita="UFFINT" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreUFFINTtoW3" >
	<gene:gruppoCampi idProtezioni="GEN">
		<gene:campoScheda>
			<td colspan="2"><b>Dati generali</b></td>
		</gene:campoScheda>
		<gene:campoScheda title="Codice" campo="CODEIN" keyCheck="true" modificabile='${modoAperturaScheda eq "NUOVO"}' gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoCodificaAutomatica" obbligatorio="${isCodificaAutomatica eq 'false'}" />
		<gene:campoScheda campo="NOMEIN"/>
		<gene:campoScheda campo="CFEIN" obbligatorio='${obbligatorioCodFisc}'>
			<gene:checkCampoScheda funzione='checkCodFisNazionalita("##",document.getElementById("UFFINT_CODNAZ"))' obbligatorio="${obbligatoriaCorrettezzaCodFisc}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		</gene:campoScheda>	
		<gene:campoScheda campo="IVAEIN" obbligatorio='${obbligatorioPIVA}'>
			<gene:checkCampoScheda funzione='checkPivaNazionalita("##",document.getElementById("UFFINT_CODNAZ"))' obbligatorio="${obbligatoriaCorrettezzaPIVA}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		</gene:campoScheda>
		<gene:campoScheda campo="VIAEIN"/>
		<gene:campoScheda campo="NCIEIN"/>
		<gene:campoScheda entita="W3UFFINT" campo="CODEIN" where="UFFINT.CODEIN = W3UFFINT.CODEIN" visibile="false"/>
		<gene:campoScheda entita="W3UFFINT" campo="TOWN" where="UFFINT.CODEIN = W3UFFINT.CODEIN"/>
		<gene:campoScheda entita="W3UFFINT" campo="NUTS" where="UFFINT.CODEIN = W3UFFINT.CODEIN" href="#" speciale="true" >
			<gene:popupCampo titolo="Dettaglio NUTS" href="" />
		</gene:campoScheda>
		<gene:campoScheda campo="CAPEIN"/>
		<gene:campoScheda campo="CODNAZ"/>
		<gene:campoScheda campo="TELEIN"/>
		<gene:campoScheda campo="FAXEIN"/>
		<gene:campoScheda campo="EMAIIN"/>
		<gene:campoScheda campo="EMAI2IN"/>
		<gene:campoScheda campo="INDWEB"/>
		<gene:campoScheda campo="PROFCO"/>
	</gene:gruppoCampi>
	
	<gene:gruppoCampi idProtezioni="RESP">
		<gene:campoScheda title="Persona di contatto" campo="NOMRES" />
	</gene:gruppoCampi>
		
	<jsp:include page="/WEB-INF/pages/gene/attributi/sezione-attributi-generici.jsp">
		<jsp:param name="entitaParent" value="UFFINT"/>
	</jsp:include>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="UFFINT"/>
			<jsp:param name="clm1name" value="CODEIN"/>
			<jsp:param name="clm1value" value="${datiRiga.UFFINT_CODEIN}"/>
		</jsp:include>
	</gene:campoScheda>
	
	<gene:campoScheda>	
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>

</gene:formScheda>

<gene:javaScript>

	$(window).ready(function (){
		_creaFinestraAlberoNUTS();
		_creaLinkAlberoNUTS($("#W3UFFINT_NUTS").parent(), "${modo}", $("#W3UFFINT_NUTS"), $("#W3UFFINT_NUTSview") );
		$("#W3UFFINT_NUTS").attr('readonly','readonly');
		$("#W3UFFINT_NUTS").attr('tabindex','-1');
		$("#W3UFFINT_NUTS").css('border-color','#A3A6FF');
		$("#W3UFFINT_NUTS").css('border-width','1px');
		$("#W3UFFINT_NUTS").css('background-color','#E0E0E0');	
	});

	<c:if test='${! empty sessionScope.uffint && modoAperturaScheda eq "MODIFICA"}'>
		document.formResponsabile.archWhereLista.value="TECNI.CGENTEI='${datiRiga.UFFINT_CODEIN}'";
	</c:if>

	<c:if test='${!(modo eq "VISUALIZZA")}'>
	 	var schedaConferma_Default = schedaConferma;
	 	
	 	function schedaConferma_Custom(){
	 	 var obbligatoriaCorrettezzaCodFisc="${obbligatoriaCorrettezzaCodFisc }";
	 	 var controlloOkCodFisc=true;
	 	 clearMsg();
	 	 
	 	 if (obbligatoriaCorrettezzaCodFisc== "true" ){
	 	 	var selectNazionalita= document.getElementById("UFFINT_CODNAZ");
	 		var isItalia= isNazionalitaItalia(selectNazionalita);
	 		
	 		if(isItalia == "si"){
	 			var codfisc=getValue("UFFINT_CFEIN");
		 	 	controlloOkCodFisc=checkCodFis(codfisc);
		 	 	if(!controlloOkCodFisc){
		 	 		outMsg("Il valore del codice fiscale specificato non è valido", "ERR");
					onOffMsg();
		 	 	}
	 		}
	 	 	
	 	 }
	 	 
	 	 var obbligatoriaCorrettezzaPIVA="${obbligatoriaCorrettezzaPIVA }";
	 	 var controlloOkPIVA=true;
	 	 if (obbligatoriaCorrettezzaPIVA=="true" ){
	 	 	var selectNazionalita= document.getElementById("UFFINT_CODNAZ");
	 		var isItalia= isNazionalitaItalia(selectNazionalita);
	 		var piva=getValue("UFFINT_IVAEIN");
	 		if(isItalia == "si"){
	 			controlloOkPIVA=checkParIva(piva);
		 	}else{
	 			controlloOkPIVA=checkPivaEuropea(piva);
	 		}
	 	 	if(!controlloOkPIVA){
	 	 		outMsg("Il valore della Partita I.V.A. o V.A.T. specificato non è valido", "ERR");
				onOffMsg();
	 	 	}
	 	 }
	 	 
	 	 if(controlloOkCodFisc && controlloOkPIVA)
	 	 	schedaConferma_Default();
	 	}
	 	
	 	schedaConferma =   schedaConferma_Custom;

	 </c:if>
</gene:javaScript>