<%
/*
 * Created on: 08-mar-2007
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Interno della scheda del tecnico */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<fmt:setBundle basename="AliceResources" />

<c:set var="isCodificaAutomatica" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.IsCodificaAutomaticaFunction", pageContext, "IMPR", "CODIMP")}'/>
<c:set var="obbligatorioCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "IMPR","CFIMP")}'/>
<c:set var="obbligatorioPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "IMPR","PIVIMP")}'/>
<c:set var="obbligatoriaCorrettezzaCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "IMPR","CFIMP")}'/>
<c:set var="obbligatoriaCorrettezzaPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "IMPR","PIVIMP")}'/>

<c:set var="listaOpzioniDisponibili" value="${fn:join(opzDisponibili,'#')}#"/>

<c:set var="archiviFiltrati" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.associazioneUffintAbilitata.archiviFiltrati")}'/>

<gene:formScheda entita="IMPR" gestisciProtezioni="true" plugin="it.eldasoft.gene.web.struts.tags.gestori.plugin.GestoreImpresa" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreIMPRtoW3">
	
	<c:if test='${modoAperturaScheda eq "VISUALIZZA"}'>
		<c:set var="codiceImpresa" value='${fn:substringAfter(key, ":")}' />
		<c:set var="impresaRegistrata" value='${gene:callFunction2("it.eldasoft.gene.tags.functions.ImpresaRegistrataSuPortaleFunction",  pageContext, codiceImpresa )}'/>
	</c:if>
			
	<c:if test='${modoAperturaScheda eq "VISUALIZZA" and (tipologiaImpresa ne 3 and tipologiaImpresa ne 10) and isIntegrazionePortaleAlice eq "true"}'>
		<gene:redefineInsert name="addToAzioni" >
			<c:if test='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.GENE.ImprScheda.RegistraSuPortale") and impresaRegistrata ne "SI"}' >
				<tr>
					<td class="vocemenulaterale" >
						<a href="javascript:registraSuPortale()" title="Registra su portale" tabindex="1505">Registra su portale</a>
					</td>
				</tr>
			</c:if>
			<c:if test='${bloccoImpresaRegistrata eq "1" and gene:checkProt(pageContext, "FUNZ.VIS.ALT.GENE.ImprScheda.InviaMailAttivazioneSuPortale")}'>
				<tr>
					<td class="vocemenulaterale" >
						<a href="javascript:inviaMailAttivazioneSuPortale()" title="Invia mail di attivazione utenza su portale" tabindex="1505">Invia mail di attivazione utenza su portale</a>
					</td>
				</tr>
			</c:if>
		</gene:redefineInsert>
	</c:if>
	
	<gene:gruppoCampi idProtezioni="GEN" >
		<gene:campoScheda  nome="GEN">
			<td colspan="2"><b>Dati generali</b></td>
		</gene:campoScheda>

		<gene:campoScheda title="Codice" campo="CODIMP" keyCheck="true" modificabile='${modoAperturaScheda eq "NUOVO"}' gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoCodificaAutomatica" obbligatorio="${isCodificaAutomatica eq 'false'}" />
		<gene:campoScheda title="Denominazione" campo="NOMEST" gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoNote"/>
		<gene:campoScheda campo="NOMIMP" visibile="false" >
			<gene:calcoloCampoScheda funzione='"#IMPR_NOMEST#".substr(0,60)' elencocampi="IMPR_NOMEST" />
		</gene:campoScheda>
		<gene:campoScheda campo="TIPIMP"/>
		<gene:campoScheda campo="TIPRTI"/>
		<gene:campoScheda campo="NATGIUI"/>
		<gene:campoScheda campo="GFIIMP" visibile="false" />
		<c:choose>
		<c:when test='${isModificaDatiRegistrati eq "true"}'>
		 <gene:campoScheda campo="CFIMP" obbligatorio='${obbligatorioCodFisc}'>
			<gene:checkCampoScheda funzione='checkCodFisNazionalita("##",document.getElementById("IMPR_NAZIMP"))' obbligatorio="true" messaggio='Il valore del codice fiscale specificato non è valido.' onsubmit="false"/>
		 </gene:campoScheda>	
		</c:when>
		<c:otherwise>
		 <gene:campoScheda campo="CFIMP" obbligatorio='${obbligatorioCodFisc}'>
			<gene:checkCampoScheda funzione='checkCodFisNazionalita("##",document.getElementById("IMPR_NAZIMP"))' obbligatorio="${obbligatoriaCorrettezzaCodFisc}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		 </gene:campoScheda>	
		</c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test='${isModificaDatiRegistrati eq "true"}'>
		 <gene:campoScheda campo="PIVIMP" title ='Partita I.V.A. ${gene:if(modo ne "VISUALIZZA" && obbligatorioPIVA eq "true", "(*)","") }'>
			<gene:checkCampoScheda funzione='checkPivaNazionalita("##",document.getElementById("IMPR_NAZIMP"))' obbligatorio="true" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		 </gene:campoScheda>
		</c:when>
		<c:otherwise>
		 <gene:campoScheda campo="PIVIMP" title ='Partita I.V.A. ${gene:if(modo ne "VISUALIZZA" && obbligatorioPIVA eq "true", "(*)","") }'>
			<gene:checkCampoScheda funzione='checkPivaNazionalita("##",document.getElementById("IMPR_NAZIMP"))' obbligatorio="${obbligatoriaCorrettezzaPIVA}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		 </gene:campoScheda>
		</c:otherwise>
		</c:choose>
		<gene:campoScheda campo="CGENIMP" defaultValue="${gene:if(fn:contains(archiviFiltrati,'IMPR') && !empty sessionScope.uffint,sessionScope.uffint,'')}" visibile='${fn:contains(listaOpzioniDisponibili, "OP127#")}'/>
	</gene:gruppoCampi>
	
	<gene:gruppoCampi idProtezioni="IND" >
		<gene:campoScheda  nome="IND">
			<td colspan="2"><b>Indirizzo</b></td>
		</gene:campoScheda>
		<gene:campoScheda campo="INDIMP"/>
		<gene:campoScheda campo="NCIIMP"/>
		<gene:campoScheda entita="W3IMPR" campo="CODIMP" where="IMPR.CODIMP = W3IMPR.CODIMP" visibile="false"/>
		<gene:campoScheda entita="W3IMPR" campo="TOWN" where="IMPR.CODIMP = W3IMPR.CODIMP"/>
		<gene:campoScheda entita="W3IMPR" campo="NUTS" where="IMPR.CODIMP = W3IMPR.CODIMP" href="#" speciale="true" >
			<gene:popupCampo titolo="Dettaglio NUTS" href="" />
		</gene:campoScheda>
		<gene:campoScheda campo="CAPIMP"/>
		<gene:campoScheda campo="NAZIMP"/>
		<gene:campoScheda campo="TELIMP"/>
		<gene:campoScheda campo="FAXIMP"/>
		<gene:campoScheda campo="TELCEL"/>
		<c:choose>
			<c:when test='${isModificaDatiRegistrati eq "true"}'>
			 	<gene:campoScheda campo="EMAIIP">
					<gene:checkCampoScheda funzione='isMailValida("##")' obbligatorio="true" messaggio="L'indirizzo email non e' sintatticamente valido." />
			 	</gene:campoScheda>	
			 	<gene:campoScheda campo="EMAI2IP">
					<gene:checkCampoScheda funzione='isMailValida("##")' obbligatorio="true" messaggio="L'indirizzo pec non e' sintatticamente valido." />
			 	</gene:campoScheda>	
			</c:when>
			<c:otherwise>
		 		<gene:campoScheda campo="EMAIIP"/>
		 		<gene:campoScheda campo="EMAI2IP"/>
			</c:otherwise>
		</c:choose>
		<gene:campoScheda campo="INDWEB"/>
		<gene:campoScheda campo="MGSFLG"/>
	</gene:gruppoCampi>

	<jsp:include page="/WEB-INF/pages/gene/attributi/sezione-attributi-generici.jsp">
		<jsp:param name="entitaParent" value="IMPR"/>
	</jsp:include>

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="IMPR"/>
			<jsp:param name="clm1name" value="CODIMP"/>
			<jsp:param name="clm1value" value="${datiRiga.IMPR_CODIMP}"/>
		</jsp:include>
	</gene:campoScheda>

	<gene:campoScheda>	
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>

	<input  type="hidden" name="MOD_DATI_REG" title = "MOD_DATI_REG" value="${isModificaDatiRegistrati}" />
	
</gene:formScheda>
<gene:javaScript>

	$(window).ready(function (){
		_creaFinestraAlberoNUTS();
		_creaLinkAlberoNUTS($("#W3IMPR_NUTS").parent(), "${modo}", $("#W3IMPR_NUTS"), $("#W3IMPR_NUTSview") );
		$("#W3IMPR_NUTS").attr('readonly','readonly');
		$("#W3IMPR_NUTS").attr('tabindex','-1');
		$("#W3IMPR_NUTS").css('border-color','#A3A6FF');
		$("#W3IMPR_NUTS").css('border-width','1px');
		$("#W3IMPR_NUTS").css('background-color','#E0E0E0');	
	});
 
 
	 <c:if test='${!(modo eq "VISUALIZZA")}'>
	 	var schedaConferma_Default = schedaConferma;
	 	
	 	function schedaConferma_Custom(){
	 	 var tipimp=getValue("IMPR_TIPIMP");
	 	 var obbligatoriaCorrettezzaCodFisc="${obbligatoriaCorrettezzaCodFisc }";
	 	 var controlloOkCodFisc=true;
	 	 var isModificaDatiRegistrati = "${isModificaDatiRegistrati }";
	 	 clearMsg();
	 	  	 	  	 	  	 
	 	 if ((obbligatoriaCorrettezzaCodFisc== "true" || isModificaDatiRegistrati == "true") && tipimp!=3 && tipimp!=10){
	 	 	var selectNazionalita= document.getElementById("IMPR_NAZIMP");
	 	 	var isItalia= isNazionalitaItalia(selectNazionalita);
	 	 	
	 	 	if(isItalia == "si"){
		 	 	var codfisc=getValue("IMPR_CFIMP");
		 	 	controlloOkCodFisc=checkCodFis(codfisc);
		 	 	if(!controlloOkCodFisc){
		 	 		outMsg("Il valore del codice fiscale specificato non è valido", "ERR");
					onOffMsg();
		 	 	}
	 	 	}
	 	 }
	 	 
	 	 var esitoControlloPIVA = true;
	 	 var obbligatorioPIVA="${obbligatorioPIVA }";
	 	 if(obbligatorioPIVA == "true"){
	 	 	var saltareControlloObbligPivaLibProfessionista = "${saltareControlloObbligPivaLibProfessionista }";
	 	 	if (!(tipimp==3 || tipimp==10 || (tipimp == 6 && saltareControlloObbligPivaLibProfessionista == "true"))){
	 	 		var piva=getValue("IMPR_PIVIMP");
	 	 		if(piva==null || piva==""){
	 	 			outMsg("Il campo partita I.V.A. è obbligatorio", "ERR");
					onOffMsg();
					esitoControlloPIVA = false;
				}
	 	 	} 
	 	 		
	 	 }
	 	 
	 	 var obbligatoriaCorrettezzaPIVA="${obbligatoriaCorrettezzaPIVA }";
	 	 var controlloOkPIVA=true;
	 	 if ((obbligatoriaCorrettezzaPIVA=="true" || isModificaDatiRegistrati == "true") && tipimp!=3 && tipimp!=10){
	 	 	var selectNazionalita= document.getElementById("IMPR_NAZIMP");
	 	 	var isItalia= isNazionalitaItalia(selectNazionalita);
	 	 	var piva=getValue("IMPR_PIVIMP");
	 	 	if(isItalia == "si"){
	 	 		controlloOkPIVA=checkParIva(piva);
		 	}else {
	 	 		controlloOkPIVA = checkPivaEuropea(piva);
	 	 	}
	 	 	if(!controlloOkPIVA){
	 	 		outMsg("Il valore della Partita I.V.A. o V.A.T. specificato non è valido", "ERR");
				onOffMsg();
	 	 	}
	 	 }
	 	  	 
	 	 if(controlloOkCodFisc && controlloOkPIVA && esitoControlloPIVA)
	 	 	schedaConferma_Default();
	 	}
	 	
	 	schedaConferma =   schedaConferma_Custom;
	
		function changeProvincia(provincia, nomeUnCampoInArchivio) {
			changeFiltroArchivioComuni(provincia, nomeUnCampoInArchivio);
			setValue("IMPR_CNATEC", "");
		}
		
	 </c:if>
 
</gene:javaScript>
