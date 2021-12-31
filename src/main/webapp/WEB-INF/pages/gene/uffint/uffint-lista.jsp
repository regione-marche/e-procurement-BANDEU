<%
/*
 * Created on: 13-Lug-2009
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Lista degli uffici intestatari */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setBundle basename="AliceResources" />
<c:set var="nomeEntitaParametrizzata" scope="request">
	<fmt:message key="label.tags.uffint.multiplo" />
</c:set>
<c:set var="nomeEntitaSingolaParametrizzata" scope="request">
	<fmt:message key="label.tags.uffint.singolo" />
</c:set>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "UFFINT", "CODEIN")}' />
</c:if>

<gene:template file="lista-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ListaUffint">
	<gene:setString name="titoloMaschera" value="Lista ${fn:toLowerCase(nomeEntitaParametrizzata)}"/>
	<c:set var="visualizzaLink" value='${gene:checkProt(pageContext, "MASC.VIS.GENE.SchedaUffint")}' scope="request"/>

	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu" scope="requestScope">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td>
					<gene:formLista entita="UFFINT" sortColumn="3" pagesize="20" tableclass="datilista" gestisciProtezioni="true" where="${whereProtezioneArchivi}" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreUFFINTtoW3">
						<jsp:include page="uffint-interno-lista.jsp" />
					</gene:formLista>
				</td>
			</tr>
			<tr>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiLista.jsp" />
			</tr>
		</table>
		<form name="formVisualizzaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/VisualizzaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="" />
			<input type="hidden" name="clm1name" id="clm1name" value="" />
			<input type="hidden" name="clm1value" id="clm1value" value="" />
		</form> 
		
		<gene:javaScript>
			function apriGestionePermessi(codein) {
				bloccaRichiesteServer();
				formVisualizzaPermessiUtentiGruppi.tblname.value = "UFFINT";
				formVisualizzaPermessiUtentiGruppi.clm1name.value = "CODEIN";
				formVisualizzaPermessiUtentiGruppi.clm1value.value = codein;
				formVisualizzaPermessiUtentiGruppi.submit();
			}
		</gene:javaScript>
  </gene:redefineInsert>
</gene:template>
