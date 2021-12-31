<%
/*
 * Created on: 29-mag-2008
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Lista dei tecnici delle imprese */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "TEIM", "CODTIM")}' />
</c:if>

<gene:template file="popup-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ListaTeim">
	<gene:setString name="titoloMaschera" value="Selezione del tecnico"/>

	<gene:redefineInsert name="corpo">
		<gene:formLista entita="TEIM" sortColumn="3" pagesize="20" tableclass="datilista" gestisciProtezioni="true" inserisciDaArchivio='${gene:checkProtFunz(pageContext,"INS","nuovo")}' where="${whereProtezioneArchivi}"> 
			<gene:campoLista title="Opzioni" width="50">
				<c:if test="${currentRow >= 0}" >
				<gene:PopUp variableJs="jvarRow${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
					<gene:PopUpItem title="Seleziona" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
				</gene:PopUp>
				</c:if>
			</gene:campoLista>
			<% // Campi veri e propri %>

<c:set var="hrefDettaglio" value=""/>
<c:if test='${!gene:checkProt(pageContext, "COLS.VIS.GENE.TEIM.CODTIM")}'>
	<c:set var="hrefDettaglio" value="javascript:archivioSeleziona(${datiArchivioArrayJs});"/> 
</c:if>

			<gene:campoLista campo="CODTIM" headerClass="sortable" width="90" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
			<gene:campoLista campo="NOMTIM" headerClass="sortable" href="${hrefDettaglio}"/>
			<gene:campoLista campo="CFTIM" headerClass="sortable" width="120"/>
			<gene:campoLista campo="PIVATEI" headerClass="sortable" width="120"/>
		</gene:formLista>
  </gene:redefineInsert>
</gene:template>
