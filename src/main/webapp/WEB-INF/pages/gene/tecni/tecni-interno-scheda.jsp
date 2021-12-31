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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="isCodificaAutomatica" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.IsCodificaAutomaticaFunction", pageContext, "TECNI", "CODTEC")}'/>
<c:set var="obbligatorioCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "TECNI","CFTEC")}'/>
<c:set var="obbligatorioPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloObbligatorietaCodFiscPivaFunction", pageContext, "TECNI","PIVATEC")}'/>
<c:set var="obbligatoriaCorrettezzaCodFisc" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "TECNI","CFTEC")}'/>
<c:set var="obbligatoriaCorrettezzaPIVA" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.EsisteControlloCorrettezzaFunction", pageContext, "TECNI","PIVATEC")}'/>

<c:set var="valoreItalia" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.GetCodiceTabellatoDaDescrFunction", pageContext, "Ag010","Italia")}'/>

<c:set var="listaOpzioniDisponibili" value="${fn:join(opzDisponibili,'#')}#"/>

<c:set var="archiviFiltrati" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.associazioneUffintAbilitata.archiviFiltrati")}'/>

<gene:formScheda entita="TECNI" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreTECNItoW3">
	<gene:gruppoCampi idProtezioni="GEN"  >
		<gene:campoScheda>
			<td colspan="2"><b>Dati generali</b></td>
		</gene:campoScheda>
		<gene:campoScheda campo="CODTEC" keyCheck="true" modificabile='${modoAperturaScheda eq "NUOVO"}' gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoCodificaAutomatica" obbligatorio="${isCodificaAutomatica eq 'false'}" />
		<gene:campoScheda campo="COGTEI"/>
		<gene:campoScheda campo="NOMETEI"/>
		<gene:campoScheda campo="NOMTEC">
			<gene:calcoloCampoScheda funzione='calcolaFromCognome("#TECNI_NOMTEC#","#TECNI_COGTEI#","#TECNI_NOMETEI#")' elencocampi="TECNI_COGTEI" />
			<gene:calcoloCampoScheda funzione='calcolaFromNome("#TECNI_NOMTEC#","#TECNI_COGTEI#","#TECNI_NOMETEI#")' elencocampi="TECNI_NOMETEI" />
		</gene:campoScheda>
		<gene:campoScheda campo="INCTEC"/>
		<gene:campoScheda campo="CFTEC" obbligatorio='${obbligatorioCodFisc}'>
			<gene:checkCampoScheda funzione='checkCodFisNazionalita("##",document.getElementById("TECNI_NAZTEI"))' obbligatorio="${obbligatoriaCorrettezzaCodFisc}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		</gene:campoScheda>	
		<gene:campoScheda campo="PIVATEC" obbligatorio='${obbligatorioPIVA}'>
			<gene:checkCampoScheda funzione='checkPivaNazionalita("##",document.getElementById("TECNI_NAZTEI"))' obbligatorio="${obbligatoriaCorrettezzaPIVA}" messaggio='Il valore specificato non è valido.' onsubmit="false"/>
		</gene:campoScheda>
		<gene:campoScheda campo="INDTEC"/>
		<gene:campoScheda campo="NCITEC"/>
		<gene:campoScheda campo="PROTEC"/>
		<gene:archivio titolo="Comuni" obbligatorio="false" scollegabile="true"
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.CAPTEC") and gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.PROTEC") and gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.LOCTEC") and gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.CITTEC"),"gene/commons/istat-comuni-lista-popup.jsp","")}' 
				scheda="" 
				schedaPopUp="" 
				campi="TB1.TABCOD3;TABSCHE.TABCOD4;TABSCHE.TABDESC;TABSCHE.TABCOD3" 
				chiave="" 
				where='${gene:if(!empty datiRiga.TECNI_PROTEC, gene:concat(gene:concat("TB1.TABCOD3 = \'", datiRiga.TECNI_PROTEC), "\'"), "")}'  
				formName="formIstat" 
				inseribile="false" >
			<gene:campoScheda campoFittizio="true" campo="COM_PROTEC" definizione="T9" visibile="false"/>
			<gene:campoScheda campo="CAPTEC"/>
			<gene:campoScheda campo="LOCTEC"/>
			<gene:campoScheda campo="CITTEC"/>
		</gene:archivio>
		<gene:campoScheda campo="NAZTEI"/>
		<gene:campoScheda campo="SEXTEI" gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoSesso" />
		<gene:campoScheda campo="PRONAS"/>
		<gene:archivio titolo="Comuni" obbligatorio="false"
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.PRONAS") and gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.CNATEC"),"gene/commons/istat-comuni-lista-popup.jsp","")}' 
				scheda="" 
				schedaPopUp="" 
				campi="TB1.TABCOD3;TABSCHE.TABDESC" 
				chiave="" 
				where='${gene:if(!empty datiRiga.TECNI_PRONAS, gene:concat(gene:concat("TB1.TABCOD3 = \'", datiRiga.TECNI_PRONAS), "\'"), "")}'  
				formName="formNascita" 
				inseribile="false" >
			<gene:campoScheda campoFittizio="true" campo="CNA_PNATEC" definizione="T9" visibile="false"/>
			<gene:campoScheda campo="CNATEC"/>
		</gene:archivio>
		<gene:campoScheda campo="DNATEC"/>
		<gene:campoScheda campo="TELTEC"/>
		<gene:campoScheda campo="FAXTEC"/>
		<gene:campoScheda campo="TELCEL"/>
		<gene:campoScheda campo="EMATEC"/>
		<gene:campoScheda campo="EMA2TEC"/>
		<gene:campoScheda campo="MGSFLG"/>
		<gene:campoScheda campo="CGENTEI" defaultValue="${gene:if(! empty sessionScope.uffint && fn:contains(archiviFiltrati,'TECNI'),sessionScope.uffint,'')}" visibile="false"/>
		
	</gene:gruppoCampi>

	<gene:gruppoCampi idProtezioni="ALT"  >
		<gene:campoScheda>
			<td colspan="2"><b>Altri dati</b></td>
		</gene:campoScheda>
		<gene:campoScheda campo="TIPALB"/>
		<gene:campoScheda campo="ALBTEC"/>
		<gene:campoScheda campo="DATALB"/>
		<gene:archivio titolo="Province" obbligatorio="false" 
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.PROALB"),"gene/commons/istat-province-lista-popup.jsp","")}' 
				scheda="" 
				schedaPopUp="" 
				campi="TABSCHE.TABCOD2;TABSCHE.TABDESC" 
				chiave="" 
				where="" 
				formName="formIstat1" 
				inseribile="false" >
			<gene:campoScheda campo="PROALB"/>
			<gene:campoScheda entita="TABSCHE" campo="TABDESC" title="Provincia" where="TABCOD='S2003' and TABSCHE.TABCOD1='07' and TECNI.PROALB = TABSCHE.TABCOD2" modificabile='${gene:checkProt( pageContext, "COLS.MOD.GENE.TECNI.PROALB")}' visibile='${gene:checkProt( pageContext, "COLS.VIS.GENE.TECNI.PROALB")}'/>
		</gene:archivio>
		<gene:campoScheda campo="TCAPRE"/>
		<gene:campoScheda campo="NCAPRE"/>
		<gene:campoScheda campo="ISCCOL"/>
		<gene:campoScheda campo="ISCCO1"/>
		<gene:campoScheda campo="NOTTEC" gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoNote"/>
		<gene:archivio titolo="Imprese" 
				lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.GENE.TECNI.CODSTU"),"gene/impr/impr-lista-popup.jsp","")}' 
				scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.ImprScheda"),"gene/impr/impr-scheda.jsp","")}' 
				schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.ImprScheda"),"gene/impr/impr-scheda-popup.jsp","")}'
				campi="IMPR.CODIMP;IMPR.NOMEST;IMPR.CGENIMP"
				chiave="TECNI_CODSTU" >
			<gene:campoScheda campo="CODSTU"/>
			<gene:campoScheda entita="IMPR" campo="NOMEST" where="TECNI.CODSTU = IMPR.CODIMP" modificabile='${gene:checkProt( pageContext, "COLS.MOD.GENE.TECNI.CODSTU")}' visibile='${gene:checkProt( pageContext, "COLS.VIS.GENE.TECNI.CODSTU")}'/>
			<gene:campoScheda title="Codice dell'Anagrafico Generale" entita="IMPR" campo="CGENIMP" where="TECNI.CODSTU = IMPR.CODIMP" visibile='${gene:checkProt(pageContext, "COLS.VIS.GENE.IMPR.CGENIMP") && fn:contains(listaOpzioniDisponibili, "OP127#")}'/>
		</gene:archivio>
		<gene:campoScheda campo="WEBUSR"/>
		<gene:campoScheda campo="WEBPWD"/>
	</gene:gruppoCampi>

	<jsp:include page="/WEB-INF/pages/gene/attributi/sezione-attributi-generici.jsp">
		<jsp:param name="entitaParent" value="TECNI"/>
	</jsp:include>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="TECNI"/>
			<jsp:param name="clm1name" value="CODTEC"/>
			<jsp:param name="clm1value" value="${datiRiga.TECNI_CODTEC}"/>
		</jsp:include>
	</gene:campoScheda>
	
	<gene:campoScheda>	
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>

	<gene:fnJavaScriptScheda funzione='changeComune("#TECNI_PROTEC#", "COM_PROTEC")' elencocampi='TECNI_PROTEC' esegui="false"/>
	<gene:fnJavaScriptScheda funzione='setValueIfNotEmpty("TECNI_PROTEC", "#COM_PROTEC#")' elencocampi='COM_PROTEC' esegui="false"/>

	<gene:fnJavaScriptScheda funzione='changeComuneNas("#TECNI_PRONAS#", "CNA_PNATEC")' elencocampi='TECNI_PRONAS' esegui="false"/>
	<gene:fnJavaScriptScheda funzione='setValueIfNotEmpty("TECNI_PRONAS", "#CNA_PNATEC#")' elencocampi='CNA_PNATEC' esegui="false"/>
	
	<gene:fnJavaScriptScheda funzione='aggiornaNazionalita("#TECNI_LOCTEC#", "${valoreItalia}", "TECNI_NAZTEI")' elencocampi='TECNI_LOCTEC' esegui="false"/>
	
</gene:formScheda>
<gene:javaScript>
		function trim(stringa){
			while (stringa.substring(0,1) == ' ')
			{
				stringa = stringa.substring(1, stringa.length);
			}
			while (stringa.substring(stringa.length-1, stringa.length) == ' ')
			{
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
	setValue("TECNI_CAPTEC", "");
	setValue("TECNI_LOCTEC", "");
	setValue("TECNI_CITTEC", "");
}
function changeComuneNas(provincia, nomeUnCampoInArchivio) {
	changeFiltroArchivioComuni(provincia, nomeUnCampoInArchivio);
	setValue("TECNI_CNATEC", "");
}

<c:if test='${!(modo eq "VISUALIZZA")}'>
 	var schedaConferma_Default = schedaConferma;
 	
 	function schedaConferma_Custom(){
 	 var obbligatoriaCorrettezzaCodFisc="${obbligatoriaCorrettezzaCodFisc }";
 	 var controlloOkCodFisc=true;
 	 clearMsg();
 	 
 	 if (obbligatoriaCorrettezzaCodFisc== "true"){
 	 	var selectNazionalita= document.getElementById("TECNI_NAZTEI");
 		var isItalia= isNazionalitaItalia(selectNazionalita);
 		
 		if(isItalia == "si"){
 			var codfisc=getValue("TECNI_CFTEC");
	 	 	controlloOkCodFisc=checkCodFis(codfisc);
	 	 	if(!controlloOkCodFisc){
	 	 		outMsg("Il valore del codice fiscale specificato non è valido", "ERR");
				onOffMsg();
	 	 	}
 		}
 	 	
 	 }
 	 
 	 var obbligatoriaCorrettezzaPIVA="${obbligatoriaCorrettezzaPIVA }";
 	 var controlloOkPIVA=true;
 	 if (obbligatoriaCorrettezzaPIVA=="true"){
 	 	var selectNazionalita= document.getElementById("TECNI_NAZTEI");
 		var isItalia= isNazionalitaItalia(selectNazionalita);
 		var piva=getValue("TECNI_PIVATEC");
 		if(isItalia == "si"){
 			controlloOkPIVA=checkParIva(piva);
	 	}else{
 			controlloOkPIVA = checkPivaEuropea(piva);
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
