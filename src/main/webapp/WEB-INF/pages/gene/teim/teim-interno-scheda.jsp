<%
/*
 * Created on: 29-mag-2008
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Interno della scheda del tecnico delle imprese */
%>

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="isCodificaAutomatica" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.IsCodificaAutomaticaFunction", pageContext, "TEIM", "CODTIM")}'/>
<c:set var="obbligatorioCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "TEIM","CFTIM")}'/>
<c:set var="obbligatorioPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "TEIM","PIVATEI")}'/>
<c:set var="obbligatoriaCorrettezzaCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "TEIM","CFTIM")}'/>
<c:set var="obbligatoriaCorrettezzaPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "TEIM","PIVATEI")}'/>

<c:set var="tecnicoRegistrato" value='${gene:callFunction2("it.eldasoft.gene.tags.functions.TecnicoAssociatoImpresaRegistrataSuPortaleFunction",  pageContext, fn:substringAfter(key, ":") )}'/>

<c:set var="valoreItalia" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.GetCodiceTabellatoDaDescrFunction", pageContext, "Ag010","Italia")}'/>

<c:set var="archiviFiltrati" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.associazioneUffintAbilitata.archiviFiltrati")}'/>

<gene:formScheda entita="TEIM" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreTEIMtoW3" >
	<gene:gruppoCampi idProtezioni="GEN" >
		<gene:campoScheda>
			<td colspan="2"><b>Dati generali</b></td>
		</gene:campoScheda>
		<gene:campoScheda campo="CODTIM" keyCheck="true" modificabile='${modoAperturaScheda eq "NUOVO"}' gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoCodificaAutomatica" obbligatorio="${isCodificaAutomatica eq 'false'}" />
		<gene:campoScheda campo="COGTIM"/>
		<gene:campoScheda campo="NOMETIM"/>
		<gene:campoScheda campo="NOMTIM">
			<gene:calcoloCampoScheda funzione='calcolaFromCognome("#TEIM_NOMTIM#","#TEIM_COGTIM#","#TEIM_NOMETIM#")' elencocampi="TEIM_COGTIM" />
			<gene:calcoloCampoScheda funzione='calcolaFromNome("#TEIM_NOMTIM#","#TEIM_COGTIM#","#TEIM_NOMETIM#")' elencocampi="TEIM_NOMETIM" />
		</gene:campoScheda>
		<gene:campoScheda campo="INCTEC"/>
		<gene:campoScheda campo="INCTIM"/>
		<gene:campoScheda campo="CFTIM" obbligatorio='${obbligatorioCodFisc}'>
			<gene:checkCampoScheda funzione='checkCodFisNazionalita("##",document.getElementById("TEIM_NAZTIM"))' obbligatorio="${obbligatoriaCorrettezzaCodFisc}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		</gene:campoScheda>	
		<gene:campoScheda campo="PIVATEI" obbligatorio='${obbligatorioPIVA}'>
			<gene:checkCampoScheda funzione='checkPivaNazionalita("##",document.getElementById("TEIM_NAZTIM"))' obbligatorio="${obbligatoriaCorrettezzaPIVA}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		</gene:campoScheda>
		<gene:campoScheda campo="INDTIM"/>
		<gene:campoScheda campo="NCITIM"/>
		<gene:campoScheda campo="PROTIM"/>
		<gene:archivio titolo="Comuni" obbligatorio="false" scollegabile="true"
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.CAPTIM") and gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.PROTIM") and gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.LOCTIM") and gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.CITTEC"),"gene/commons/istat-comuni-lista-popup.jsp","")}' 
				scheda="" 
				schedaPopUp="" 
				campi="TB1.TABCOD3;TABSCHE.TABCOD4;TABSCHE.TABDESC;TABSCHE.TABCOD3" 
				chiave="" 
				where='${gene:if(!empty datiRiga.TEIM_PROTIM, gene:concat(gene:concat("TB1.TABCOD3 = \'", datiRiga.TEIM_PROTIM), "\'"), "")}'  
				formName="formIstat" 
				inseribile="false" >
			<gene:campoScheda campoFittizio="true" campo="COM_PROTIM" definizione="T9" visibile="false"/>
			<gene:campoScheda campo="CAPTIM"/>
			<gene:campoScheda campo="LOCTIM"/>
			<gene:campoScheda campo="CITTEC"/>
		</gene:archivio>
		<gene:campoScheda campo="NAZTIM"/>
		<gene:campoScheda campo="SEXTIM" gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoSesso" />
		<gene:campoScheda campo="PRONAS"/>
		<gene:archivio titolo="Comuni" obbligatorio="false" scollegabile="true"
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.PRONAS") and gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.CNATIM"),"gene/commons/istat-comuni-lista-popup.jsp","")}' 
				scheda="" 
				schedaPopUp="" 
				campi="TB1.TABCOD3;TABSCHE.TABDESC" 
				chiave="" 
				where='${gene:if(!empty datiRiga.TEIM_PRONAS, gene:concat(gene:concat("TB1.TABCOD3 = \'", datiRiga.TEIM_PRONAS), "\'"), "")}'  
				formName="formNascita" 
				inseribile="false" >
			<gene:campoScheda campoFittizio="true" campo="CNA_PNATIM" definizione="T9" visibile="false"/>
			<gene:campoScheda campo="CNATIM"/>
		</gene:archivio>
		<gene:campoScheda campo="DNATIM"/>
		<gene:campoScheda campo="TELTIM"/>
		<gene:campoScheda campo="FAXTIM"/>
		<gene:campoScheda campo="TELCEL"/>
		<gene:campoScheda campo="EMAIIM"/>
		<gene:campoScheda campo="EMAI2IM"/>
		<gene:campoScheda campo="MGSFLG"/>
		
		
		<gene:campoScheda campoFittizio="true" campo="PROVTEIM" visibile="false" definizione="T6"/>
		<gene:campoScheda campoFittizio="true" campo="IMPRTEIM" visibile="false" definizione="T10"/>
		<gene:campoScheda campo="CGENTIM" defaultValue="${gene:if(! empty sessionScope.uffint && fn:contains(archiviFiltrati,'TEIM'),sessionScope.uffint,'')}" visibile="false"/>
	</gene:gruppoCampi>

	<gene:gruppoCampi idProtezioni="ALT">
		<gene:campoScheda>
			<td colspan="2"><b>Altri dati</b></td>
		</gene:campoScheda>
		<gene:campoScheda campo="TIPALB"/>
		<gene:campoScheda campo="ALBTIM"/>
		<gene:campoScheda campo="DATALB"/>
		
		
		<gene:archivio titolo="Province" obbligatorio="false" 
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TEIM.PROALB"),"gene/commons/istat-province-lista-popup.jsp","")}' 
				scheda="" 
				schedaPopUp="" 
				campi="TABSCHE.TABCOD2;TABSCHE.TABDESC" 
				chiave="" 
				where=''  
				formName="formAlbo" 
				inseribile="false" >
			<gene:campoScheda campo="PROALB"/>
			<gene:campoScheda entita="TABSCHE" campo="TABDESC" title="Provincia" where="TABCOD='S2003' and TABSCHE.TABCOD1='07' and TEIM.PROALB = TABSCHE.TABCOD2" modificabile='${gene:checkProt( pageContext, "COLS.MOD.GENE.TEIM.PROALB")}' visibile='${gene:checkProt( pageContext, "COLS.VIS.GENE.TEIM.PROALB")}'/>
		</gene:archivio>
		
		
		<gene:campoScheda campo="TCAPRE"/>
		<gene:campoScheda campo="NCAPRE"/>
		
		<gene:campoScheda campo="NOTTIM"/>
	</gene:gruppoCampi>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="TEIM"/>
			<jsp:param name="clm1name" value="CODTIM"/>
			<jsp:param name="clm1value" value="${datiRiga.TEIM_CODTIM}"/>
		</jsp:include>
	</gene:campoScheda>
	
	<gene:campoScheda>	
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
		
	
		
	<c:if test='${tecnicoRegistrato eq "SI" and isIntegrazionePortaleAlice eq "true" }'>
		<gene:redefineInsert name="schedaModifica">
			<c:if test='${gene:checkProtFunz(pageContext,"MOD","SCHEDAMOD") && gene:checkProt(pageContext,"FUNZ.VIS.ALT.GENE.ModificaDatiRegistrati")}'>
				<tr>
					<td class="vocemenulaterale">
						<a href="javascript:schedaModifica();" title="Modifica dati registrati" tabindex="1501">
						Modifica dati registrati</a></td>
				</tr>
			</c:if>
		</gene:redefineInsert>
		<gene:redefineInsert name="pulsanteModifica" >
			<c:if test='${gene:checkProtFunz(pageContext,"MOD","SCHEDAMOD") && gene:checkProt(pageContext,"FUNZ.VIS.ALT.GENE.ModificaDatiRegistrati")}'>
				<INPUT type="button"  class="bottone-azione" value='Modifica dati registrati' title='Modifica dati registrati' onclick="javascript:schedaModifica()">
			</c:if>
		</gene:redefineInsert>
	</c:if>
	<c:if test='${tecnicoRegistrato eq "SI"  and isIntegrazionePortaleAlice ne "true"}'>
		<gene:redefineInsert name="schedaModifica"/>
		<gene:redefineInsert name="pulsanteModifica" />
	</c:if>
	
	<gene:fnJavaScriptScheda funzione='changeComune("#TEIM_PROTIM#", "COM_PROTIM")' elencocampi='TEIM_PROTIM' esegui="false"/>
	<gene:fnJavaScriptScheda funzione='setValueIfNotEmpty("TEIM_PROTIM", "#COM_PROTIM#")' elencocampi='COM_PROTIM' esegui="false"/>

	<gene:fnJavaScriptScheda funzione='changeComuneNas("#TEIM_PRONAS#", "CNA_PNATIM")' elencocampi='TEIM_PRONAS' esegui="false"/>
	<gene:fnJavaScriptScheda funzione='setValueIfNotEmpty("TEIM_PRONAS", "#CNA_PNATIM#")' elencocampi='CNA_PNATIM' esegui="false"/>
	
	<gene:fnJavaScriptScheda funzione='aggiornaNazionalita("#TEIM_LOCTIM#", "${valoreItalia}", "TEIM_NAZTIM")' elencocampi='TEIM_LOCTIM' esegui="false"/>
	
	
</gene:formScheda>
<gene:javaScript>

	function trim(stringa){
		while (stringa.substring(0,1) == ' '){
			stringa = stringa.substring(1, stringa.length);
		}
		while (stringa.substring(stringa.length-1, stringa.length) == ' '){
			stringa = stringa.substring(0,stringa.length-1);
		}
		return stringa;
	}

	function calcolaFromNome(intest,cognome, nome){
		if(intest==cognome){
			return trim(trim(cognome)+" "+trim(nome));
		}
		return intest;
	}

	function calcolaFromCognome(intest,cognome, nome){
		if(nome==""){
			return trim(trim(cognome)+" "+trim(nome));
		}
		return intest;
	}

	function changeComune(provincia, nomeUnCampoInArchivio) {
		changeFiltroArchivioComuni(provincia, nomeUnCampoInArchivio);
		setValue("TEIM_CAPTIM", "");
		setValue("TEIM_LOCTIM", "");
		setValue("TEIM_CITTEC", "");
	}

	function changeComuneNas(provincia, nomeUnCampoInArchivio) {
		changeFiltroArchivioComuni(provincia, nomeUnCampoInArchivio);
		setValue("TEIM_CNATIM", "");
	}
	
		
	<c:if test='${!(modo eq "VISUALIZZA")}'>
	 	var schedaConferma_Default = schedaConferma;
	 	
	 	function schedaConferma_Custom(){
	 	 var obbligatoriaCorrettezzaCodFisc="${obbligatoriaCorrettezzaCodFisc }";
	 	 var controlloOkCodFisc=true;
	 	 clearMsg();
	 	 
	 	 if (obbligatoriaCorrettezzaCodFisc== "true" ){
	 	 	var selectNazionalita= document.getElementById("TEIM_NAZTIM");
 	 		var isItalia= isNazionalitaItalia(selectNazionalita);
 	 		
 	 		if(isItalia == "si"){
		 	 	var codfisc=getValue("TEIM_CFTIM");
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
	 	 	var selectNazionalita= document.getElementById("TEIM_NAZTIM");
 	 		var isItalia= isNazionalitaItalia(selectNazionalita);
 	 		var piva=getValue("TEIM_PIVATEI");
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