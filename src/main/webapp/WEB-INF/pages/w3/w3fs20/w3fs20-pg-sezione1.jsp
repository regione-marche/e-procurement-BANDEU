
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

<gene:formScheda entita="W3FS20" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS20" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS20.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS20" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FSVERSION" defaultValue="5" visibile="false"/>	
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS20.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />

	<gene:campoScheda>
		<td colspan="2"><b>Avviso di modifica durante il periodo di validit&agrave;</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3SIMAP" campo="LEGAL_BASIS" obbligatorio="true" defaultValue="${param.legal}" modificabile="false" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoLEGALBASIS2425"/>

	<jsp:include page="/WEB-INF/pages/w3/w3simap/w3simap-pubblication-info.jsp"></jsp:include>	

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.I")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>
			${gene:resource("label.simap.fs20.I.1")}
		</td>
	</gene:campoScheda>
	<gene:archivio titolo="Amministrazione aggiudicatrice"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI.CODAMM"),"w3/w3ammi/w3ammi-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda-popup.jsp","")}'
		 campi="W3AMMI.CODAMM;UFFINT.NOMEIN"
		 inseribile="false"
		 chiave="W3SIMAP_CODAMM">
		<gene:campoScheda entita="W3SIMAP" campo="CODAMM" obbligatorio="true"/>
		<gene:campoScheda visibile="true" modificabile="false" entita="UFFINT" campo="NOMEIN" from="W3AMMI, W3SIMAP, W3SIMAP" where="UFFINT.CODEIN = W3AMMI.CODEIN AND W3AMMI.CODAMM = W3SIMAP.CODAMM AND W3SIMAP.ID = W3FS20.ID" />
	</gene:archivio>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS20_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	
	
</gene:formScheda>

