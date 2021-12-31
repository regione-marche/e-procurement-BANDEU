
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

<gene:formScheda entita="W3FS5" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS5"
	plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS5" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3FS5S36" campo="ID" obbligatorio="true" visibile="false" where="W3FS5.ID = W3FS5S36.ID"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS5.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
		
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI")}<br><br></b></td>
	</gene:campoScheda>	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs5.VI.1")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda entita="W3FS5S36" campo="RECURRENT_PROC"/>
	<gene:campoScheda entita="W3FS5S36" campo="RECURRENT_PROC_DESC"/>	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.2")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda entita="W3FS5S36" campo="EORDERING"/>
	<gene:campoScheda entita="W3FS5S36" campo="EINVOICING"/>
	<gene:campoScheda entita="W3FS5S36" campo="EPAYMENT"/>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.3")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda title="Descrizione" entita="W3FS5S36" campo="ADDITIONAL_INFORMATION"/>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.4")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs5.VI.4.1")}</b></td>
	</gene:campoScheda>	
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3FS5.APPEAL_PROCEDURE_CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3FS5S36_APPEAL_PROCEDURE_CODEIN">
		<gene:campoScheda entita="W3FS5S36" campo="APPEAL_PROCEDURE_CODEIN" visibile="false"/>
		<gene:campoScheda title="Organismo/ufficio" campo="APPEAL_PROCEDURE_NOMEIN" value="${requestScope.appealProcedureNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.4.2")}</b></td>
	</gene:campoScheda>	
	
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3FS5.MEDIATION_PROCEDURE_CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3FS5S36_MEDIATION_PROCEDURE_CODEIN">
		<gene:campoScheda entita="W3FS5S36" campo="MEDIATION_PROCEDURE_CODEIN" visibile="false"/>
		<gene:campoScheda title="Organismo/ufficio" campo="MEDIATION_PROCEDURE_NOMEIN" value="${requestScope.mediationProcedureNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.4.3")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda title="Informazioni dettagliate sui termini di presentazione dei ricorsi" entita="W3FS5S36" campo="APPEAL_PRECISION"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.4.4")}</b></td>
	</gene:campoScheda>			
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3FS5.APPEAL_SERVICE_CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3FS5S36_APPEAL_SERVICE_CODEIN">
		<gene:campoScheda entita="W3FS5S36" campo="APPEAL_SERVICE_CODEIN" visibile="false"/>
		<gene:campoScheda title="Organismo/ufficio" campo="APPEAL_SERVICE_NOMEIN" value="${requestScope.appealServiceNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs5.VI.5")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda entita="V_W3SIMAP" campo="NOTICE_DATE" where="V_W3SIMAP.ID = W3FS5.ID" modificabile="false"/>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS5_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:fnJavaScriptScheda funzione="gestioneRECURRENT_PROC('#W3FS5S36_RECURRENT_PROC#')" elencocampi="W3FS5S36_RECURRENT_PROC" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	function gestioneRECURRENT_PROC(recurrent_proc){
		if (recurrent_proc == '1') {
			$("#rowW3FS5S36_RECURRENT_PROC_DESC").show();
		} else {
			$("#rowW3FS5S36_RECURRENT_PROC_DESC").hide();
			$("#W3FS5S36_RECURRENT_PROC_DESC").val("");
		}
	}
	
</gene:javaScript>
