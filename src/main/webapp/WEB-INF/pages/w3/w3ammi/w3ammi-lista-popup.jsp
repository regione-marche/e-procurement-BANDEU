<%
/*
 * Created on: 06-ott-2010
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */

 /* Lista popup di selezione del tecnico */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "W3AMMI", "CODAMM")}' />
</c:if>


<gene:template file="popup-template.jsp" gestisciProtezioni="true" schema="W3" idMaschera="W3AMMI-lista-popup">
	<gene:setString name="titoloMaschera" value="Selezione dell'amministrazione"/>
	<gene:redefineInsert name="corpo">
		<gene:formLista pagesize="25" tableclass="datilista" entita="W3AMMI" sortColumn="2" gestisciProtezioni="true" 
			where= "${whereProtezioneArchivi}" inserisciDaArchivio='${gene:checkProtFunz(pageContext,"INS","nuovo")}' >
			<gene:campoLista title="Opzioni" width="50">
				<c:if test="${currentRow >= 0}" >
				<gene:PopUp variableJs="jvarRow${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
					<gene:PopUpItem title="Seleziona" href="javascript:archivioSeleziona(${datiArchivioArrayJs});"/>
				</gene:PopUp>
				</c:if>
			</gene:campoLista>
			<gene:campoLista campo="CODAMM" visibile="true" width="30" href="javascript:archivioSeleziona(${datiArchivioArrayJs});" />
			<gene:campoLista entita="UFFINT" campo="NOMEIN" where="W3AMMI.CODEIN=UFFINT.CODEIN"	href="javascript:archivioSeleziona(${datiArchivioArrayJs});" ordinabile="false"/>
			<gene:campoLista title="Identificativo amministrazione" campo="CUSTOMER_LOGIN" width="140"/>
			<gene:campoLista title="Persona di contatto" entita="UFFINT" campo="NOMRES" where="W3AMMI.CODEIN=UFFINT.CODEIN"	/>
		</gene:formLista>
  </gene:redefineInsert>
</gene:template>
