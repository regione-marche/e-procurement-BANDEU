<%
/*
 * Created on: 13-lug-2009
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */

 /* Lista popup di selezione degli uffici intestatari */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setBundle basename="AliceResources" />
<c:set var="nomeEntitaSingolaParametrizzata">
	<fmt:message key="label.tags.uffint.singolo" />
</c:set>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "UFFINT", "CODEIN")}' />
</c:if>

<gene:template file="popup-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ListaUffint">
	<gene:setString name="titoloMaschera" value="Selezione dell'${fn:toLowerCase(nomeEntitaSingolaParametrizzata)}"/>
	<gene:redefineInsert name="corpo">
		<gene:formLista pagesize="25" tableclass="datilista" entita="UFFINT" sortColumn="3" where="${whereProtezioneArchivi}" gestisciProtezioni="true" inserisciDaArchivio='${gene:checkProtFunz(pageContext,"INS","nuovo")}' >
			<% // Aggiungo gli item al menu contestuale di riga %>
			<gene:campoLista title="Opzioni" width="50">
				<c:if test="${currentRow >= 0}" >
				<gene:PopUp variableJs="jvarRow${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
					<gene:PopUpItem title="Seleziona" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
				</gene:PopUp>
				</c:if>
			</gene:campoLista>
			<% // Campi della lista %>

<c:set var="hrefDettaglio" value=""/>
<c:if test='${!gene:checkProt(pageContext, "COLS.VIS.GENE.UFFINT.CODEIN")}'>
	<c:set var="hrefDettaglio" value="javascript:archivioSeleziona(${datiArchivioArrayJs});"/> 
</c:if>

			<gene:campoLista campo="CODEIN" headerClass="sortable" width="90" href="javascript:archivioSeleziona(${datiArchivioArrayJs});" />
			<gene:campoLista campo="NOMEIN" headerClass="sortable" href="${hrefDettaglio}"/>
			<gene:campoLista campo="CFEIN" headerClass="sortable" width="120"/>
		</gene:formLista>
  </gene:redefineInsert>
</gene:template>
