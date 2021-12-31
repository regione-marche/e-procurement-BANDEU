<%
/*
 * Created on: 06-ott-2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Finanziamenti del lavoro */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="isCodificaAutomatica" value='${gene:callFunction3("it.eldasoft.gene.tags.functions.IsCodificaAutomaticaFunction", pageContext, "W3AMMI", "CODAMM")}'/>

<gene:formScheda entita="W3AMMI" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3AMMI">
	<c:set var="codamm" value='${gene:getValCampo(key,"CODAMM")}' scope="request" />
	
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.w3ammi.I")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.w3ammi.I.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="CODAMM" keyCheck="true" modificabile='${modoAperturaScheda eq "NUOVO"}' gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoCodificaAutomatica" obbligatorio="${isCodificaAutomatica eq 'false'}" />

	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI.CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3AMMI_CODEIN">
		<gene:campoScheda campo="CODEIN" obbligatorio="true"/>
		<gene:campoScheda entita="UFFINT" campo="NOMEIN" title="Denominazione ufficio o punto di contatto" where="UFFINT.CODEIN = W3AMMI.CODEIN" obbligatorio="true"/>
	</gene:archivio>
	<gene:campoScheda title="Indirizzo principale (URL)" campo="URL_GENERAL" />
	<gene:campoScheda title="Indirizzo del profilo del committente (URL)" campo="URL_BUYER" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.w3ammi.I.4")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_AUTHORITY" />
	<gene:campoScheda campo="TYPE_AUTHORITY_OTHER" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.w3ammi.I.5")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_CAACTI" />
	<gene:campoScheda campo="TYPE_CAACTI_OTHER" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.w3ammi.I.6")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_CEACTI" />
	<gene:campoScheda campo="TYPE_CEACTI_OTHER" />

	<gene:campoScheda>
		<td colspan="2"><b><br>Informazioni per l'invio a SIMAP</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="CUSTOMER_LOGIN" modificabile='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.W3HOME.GestioneAmministrazioni")}'/>

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3AMMI"/>
			<jsp:param name="clm1name" value="CODAMM"/>
			<jsp:param name="clm1value" value="${datiRiga.W3AMMI_CODAMM}"/>
		</jsp:include>
	</gene:campoScheda>	

	<gene:campoScheda>	
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>

</gene:formScheda>

<gene:javaScript>
	$("table.grigliaw3 tr td.valore-dato").removeClass("valore-dato").addClass("ck");
	$("#W3AMMI_TYPE_ACTIVITY_OTHER").parent().attr("colspan","3");
	$("#W3AMMI_ACTIVITY_OF_CONTRACTING_EO").parent().attr("colspan","3");
	$("#W3AMMI_TYPE_ACTIVITY_OTHER").css("width","520px");
	$("#W3AMMI_ACTIVITY_OF_CONTRACTING_EO").css("width","520px");
</gene:javaScript>




