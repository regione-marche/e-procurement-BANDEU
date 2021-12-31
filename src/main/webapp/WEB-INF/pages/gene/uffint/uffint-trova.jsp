<%
/*
 * Created on: 13-Lug-2008
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
 /* Form di ricerca degli uffici intestatari */
%>

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="archiviFiltrati" value='${gene:callFunction("it.eldasoft.gene.tags.functions.GetPropertyFunction", "it.eldasoft.associazioneUffintAbilitata.archiviFiltrati")}'/>

<c:set var="listaOpzioniUtenteAbilitate" value="${fn:join(profiloUtente.funzioniUtenteAbilitate,'#')}#" />

<fmt:setBundle basename="AliceResources" />
<c:set var="nomeEntitaParametrizzata" scope="request">
	<fmt:message key="label.tags.uffint.multiplo" />
</c:set>

<c:set var="filtroLivelloUtente"
	value='${gene:callFunction2("it.eldasoft.gene.tags.utils.functions.FiltroLivelloUtenteFunction", pageContext, "UFFINT")}' />

<gene:template file="ricerca-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="UFFINT-trova" >

	<gene:setString name="titoloMaschera" value="Ricerca ${fn:toLowerCase(nomeEntitaParametrizzata)}"/>

	<gene:redefineInsert name="corpo">
	<c:if test='${(! empty sessionScope.uffint && fn:contains(archiviFiltrati,"UFFINT")) || fn:contains(listaOpzioniUtenteAbilitate, "ou214#")}' >
		<gene:redefineInsert name="trovaCreaNuovo"></gene:redefineInsert>
	</c:if>
	
  	<gene:formTrova entita="UFFINT" filtro="${filtroLivelloUtente}" gestisciProtezioni="true" >
			<gene:campoTrova title="Codice" campo="CODEIN" />
			<gene:campoTrova campo="NOMEIN" />
			<gene:campoTrova campo="CFEIN" />
			<gene:campoTrova campo="IVAEIN" />
			<gene:campoTrova campo="CFEIN" />
			<gene:campoTrova campo="VIAEIN" />
			<gene:campoTrova title="N. civico" campo="NCIEIN" />
			<gene:campoTrova entita="W3UFFINT" campo="TOWN" where="UFFINT.CODEIN = W3UFFINT.CODEIN" />
			<gene:campoTrova entita="W3UFFINT" campo="NUTS" where="UFFINT.CODEIN = W3UFFINT.CODEIN" />
		</gene:formTrova>
	</gene:redefineInsert>
</gene:template>
