<%/*
       * Created on 21-Ott-2008
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3SIMAP-scheda" schema="W3">
	
	<gene:setString name="titoloMaschera" value='Bando/avviso'/>

	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3SIMAP" gestisciProtezioni="true" >
			<gene:campoScheda>
				<td colspan="2"><b>Dati generali</b></td>
			</gene:campoScheda>	
			<gene:campoScheda campo="ID" visibile="false"/>
			<gene:campoScheda campo="FORM" />
			<gene:campoScheda title="Amministrazione aggiudicatrice" entita="UFFINT" campo="NOMEIN" from="W3AMMI, W3AMMI" where="W3AMMI.CODAMM=W3SIMAP.CODAMM AND UFFINT.CODEIN=W3AMMI.CODEIN" />
			<gene:campoScheda title="Denominazione dell'appalto" entita="V_W3SIMAP" campo="TITLE_CONTRACT" where="W3SIMAP.ID=V_W3SIMAP.ID" />
			<gene:campoScheda title="Tipo di appalto" campo="CTYPE" />
			<jsp:include page="/WEB-INF/pages/w3/w3simap/w3simap-pubblication-info.jsp"></jsp:include>	
			<gene:campoScheda>
				<td class="comandi-dettaglio" colspan="2">&nbsp;</td>
			</gene:campoScheda>
		</gene:formScheda>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="schedaModifica"></gene:redefineInsert>
	
</gene:template>

