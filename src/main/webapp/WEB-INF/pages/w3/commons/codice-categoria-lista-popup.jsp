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

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda" %>

<gene:template file="popup-template.jsp">
	<c:set var="entita" value="TABSCHE" />
	<gene:setString name="titoloMaschera" value="Seleziona categoria della stazione appaltante"/>
	<gene:redefineInsert name="corpo">
		<gene:formLista pagesize="25" tableclass="datilista" 
				entita="TABSCHE" sortColumn="2" inserisciDaArchivio="false" >
			<gene:formSelect>
				select tabsche.tabcod1, tabsche.tabdesc from tabsche where tabsche.tabcod='S2002' and tabsche.tabcod1 <>'99' 
			</gene:formSelect>
			<gene:campoLista title="Opzioni" width="50" >
				<c:if test="${currentRow >= 0}" >
				<gene:PopUp variableJs="jvarRow${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
					<gene:PopUpItem title="Seleziona" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
				</gene:PopUp>
				</c:if>
			</gene:campoLista>
			<gene:campoLista title="Codice categoria" entita="TABSCHE" campo="TABCOD1" definizione="T4" headerClass="sortable" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
			<gene:campoLista title="Descrizione categoria" entita="TABSCHE" campo="TABDESC" definizione="T120" headerClass="sortable" />
		</gene:formLista>
		
  </gene:redefineInsert>
</gene:template>
