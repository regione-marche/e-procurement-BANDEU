<%
/*
 * Created on: 08-mar-2007
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>
<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "IMPR", "CODIMP")}' />
</c:if>

<c:set var="listaOpzioniDisponibili" value="${fn:join(opzDisponibili,'#')}#"/>

<gene:template file="popup-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="lista-imprese-popup">
	<gene:setString name="titoloMaschera" value="Selezione dell'impresa"/>
	<gene:redefineInsert name="corpo">
		<gene:formLista pagesize="25" tableclass="datilista" entita="IMPR" sortColumn="3" gestisciProtezioni="true" inserisciDaArchivio='${gene:checkProtFunz(pageContext,"INS","nuovo")}' where="${whereProtezioneArchivi}">
			<gene:campoLista title="Opzioni" width="50">
				<c:if test="${currentRow >= 0}" >
				<gene:PopUp variableJs="jvarRow${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
					<gene:PopUpItem title="Seleziona" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
				</gene:PopUp>
				</c:if>
			</gene:campoLista>
			<gene:campoLista title="Codice" campo="CODIMP" headerClass="sortable" width="90" href="javascript:archivioSeleziona(${datiArchivioArrayJs});" />
			<gene:campoLista title="Denominazione" campo="NOMEST" headerClass="sortable"/>
			<gene:campoLista campo="CFIMP" headerClass="sortable" />
			<gene:campoLista campo="PIVIMP" headerClass="sortable" />
			<gene:campoLista campo="INDIMP" headerClass="sortable" />
			<gene:campoLista campo="NCIIMP" headerClass="sortable" />
			<gene:campoLista entita="W3IMPR" campo="TOWN" where="IMPR.CODIMP = W3IMPR.CODIMP" />
			<gene:campoLista entita="W3IMPR" campo="NUTS" where="IMPR.CODIMP = W3IMPR.CODIMP" />
		</gene:formLista>
  </gene:redefineInsert>
</gene:template>
