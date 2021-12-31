<%
	/*
	 * Created on 29-Mar-2012
	 *
	 * Copyright (c) EldaSoft S.p.A.
	 * Tutti i diritti sono riservati.
	 *
	 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
	 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
	 * aver prima formalizzato un accordo specifico con EldaSoft.
	 */
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>


<c:choose>
	<c:when test="${param.tipoDettaglio eq 1}">
		<gene:campoScheda title="Codice amministrazione" entita="W3AMMI_PB" campo="CODAMM_${param.contatore}" campoFittizio="true" visibile="false" definizione="T20;1;;;" value="${item[0]}" />
		<gene:campoScheda title="Numero progressivo" entita="W3AMMI_PB" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N3;1;;;" value="${item[1]}" />
		<gene:archivio titolo="Ufficio o punto di contatto"
			 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI_PB.CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
			 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
			 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
			 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
			 chiave="W3AMMI_PB_CODEIN_${param.contatore}"
			 where="${archivioWhere}">
			<gene:campoScheda title="Codice ufficio o punto di contatto" entita="W3AMMI_PB" campo="CODEIN_${param.contatore}" campoFittizio="true" visibile="true" definizione="T16;0;;;" value="${item[2]}" />
			<gene:campoScheda title="Denominazione ufficio o punto di contatto" campo="NOMEIN_${param.contatore}" campoFittizio="true" definizione="T254;0;;;" value="${item[3]}"/>
		</gene:archivio>
	</c:when>
	<c:otherwise>
		<gene:campoScheda title="Codice amministrazione" entita="W3AMMI_PB" campo="CODAMM_${param.contatore}" campoFittizio="true" visibile="false" definizione="T20;1;;;" />
		<gene:campoScheda title="Numero progressivo" entita="W3AMMI_PB" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N3;1;;;" />
		<gene:archivio titolo="Ufficio o punto di contatto"
			 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI_PB.CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
			 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
			 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
			 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
			 chiave="W3AMMI_PB_CODEIN_${param.contatore}"
			 where="${archivioWhere}">
			<gene:campoScheda title="Codice ufficio o punto di contatto" entita="W3AMMI_PB" campo="CODEIN_${param.contatore}" campoFittizio="true" visibile="true" definizione="T16;0;;;" />
			<gene:campoScheda title="Denominazione ufficio o punto di contatto" campo="NOMEIN_${param.contatore}" campoFittizio="true" definizione="T254;0;;;" />
		</gene:archivio>
	</c:otherwise>
</c:choose>




