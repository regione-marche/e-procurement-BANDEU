<%
	/*
	 * Created on 07-Ott-2010
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
		<gene:campoScheda title="Id" entita="W3SIMAP_ADDR" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1" value="${item[0]}"/>
		<gene:campoScheda title="Numero" entita="W3SIMAP_ADDR" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1" value="${item[1]}"/>
		<gene:archivio titolo="Amministrazione aggiudicatrice"
			 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI.CODAMM"),"w3/w3ammi/w3ammi-lista-popup.jsp","")}'
			 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda.jsp","")}'
			 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda-popup.jsp","")}'
			 campi="W3AMMI.CODAMM;UFFINT.NOMEIN"
			 inseribile="false"
			 chiave="W3SIMAP_ADDR_CODAMM_${param.contatore}">
			<gene:campoScheda title="Amministrazione aggiudicatrice" entita="W3SIMAP_ADDR" campo="CODAMM_${param.contatore}" campoFittizio="true" visibile="true" definizione="T20;0" value="${item[2]}"/>
			<gene:campoScheda title="Denominazione" campo="NOMEIN_${param.contatore}" modificabile="false" campoFittizio="true" visibile="true" definizione="T254;0" value="${item[3]}"/>
		</gene:archivio>
	</c:when>

	<c:otherwise>
		<gene:campoScheda title="Id" entita="W3SIMAP_ADDR" campo="ID_${param.contatore}" campoFittizio="true" visibile="false" definizione="N10;1" value="${param.chiave}"/>
		<gene:campoScheda title="Numero" entita="W3SIMAP_ADDR" campo="NUM_${param.contatore}" campoFittizio="true" visibile="false" definizione="N2;1" />
				<gene:archivio titolo="Amministrazione aggiudicatrice"
			 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI.CODAMM"),"w3/w3ammi/w3ammi-lista-popup.jsp","")}'
			 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda.jsp","")}'
			 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda-popup.jsp","")}'
			 campi="W3AMMI.CODAMM;UFFINT.NOMEIN"
			 inseribile="false"
			 chiave="W3SIMAP_ADDR_CODAMM_${param.contatore}">
			<gene:campoScheda title="Amministrazione aggiudicatrice" entita="W3SIMAP_ADDR" campo="CODAMM_${param.contatore}" campoFittizio="true" visibile="true" definizione="T20;0" />
			<gene:campoScheda title="Denominazione" campo="NOMEIN_${param.contatore}" modificabile="false" campoFittizio="true" visibile="true" definizione="T254;0"/>
		</gene:archivio>
	</c:otherwise>

</c:choose>
