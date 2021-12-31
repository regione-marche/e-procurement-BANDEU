
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
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS6" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS6.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs6.IV")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs6.IV.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs6.IV.1.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="TYPE_PROCEDURE" obbligatorio="true"/>
	<gene:campoScheda campo="ACCELERATED" />
	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs6.IV.1.3")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda campo="FRAMEWORK" />
	<gene:campoScheda campo="DPS" />

	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs6.IV.1.6")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda campo="IS_ELECTRONIC" />	
	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs6.IV.1.8")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda title="L'appalto &egrave; disciplinato dall'accordo sugli appalti pubblici ?" campo="CONTRACT_COVERED_GPA" />		
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs6.IV.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs6.IV.2.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Numero dell'avviso nella GU/S" campo="NOTICE_NUMBER_OJ" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs6.IV.2.8")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="L'avviso comporta la chiusura del sistema dinamico di acquisizione pubblicato nel bando di gara di cui sopra" campo="TERMINATION_DPS" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs6.IV.2.9")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="L'amministrazione aggiudicatrice non aggiudicher&agrave; altri contratti d'appalto sulla base dell'avviso di preinformazione sopraindicato" campo="TERMINATION_PIN" />

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

	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>

	<gene:fnJavaScriptScheda funzione="gestioneTYPE_PROCEDURE('#W3FS6_TYPE_PROCEDURE#')" elencocampi="W3FS6_TYPE_PROCEDURE" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	$("#W3FS6_TYPE_PROCEDURE option").each(function() {
		$(this).text($(this).attr("title"));
	});

	function gestioneTYPE_PROCEDURE(type_procedure) {
		if (type_procedure == '9' || type_procedure == '5' || type_procedure == '7') {
			$("#rowW3FS6_ACCELERATED").show();					
		} else {
			$("#rowW3FS6_ACCELERATED").hide();
			$("#W3FS6_ACCELERATED").val("");
		}
	}

</gene:javaScript>
