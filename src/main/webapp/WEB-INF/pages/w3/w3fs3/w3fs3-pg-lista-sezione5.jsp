
<%
	/*
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


<gene:set name="titoloMenu">
	<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
</gene:set>

<table class="dettaglio-tab-lista">
	<tr>
		<td><gene:formLista entita="W3FS3AWARD" pagesize="20" sortColumn="4"
			where="W3FS3AWARD.ID = #W3FS3.ID#" tableclass="datilista"
			gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS3AWARD">
			<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"	width="50">
				<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-lista-scheda.jsp">
					<jsp:param name="tblname" value="W3SIMAP"/>
					<jsp:param name="clm1name" value="ID"/>
					<jsp:param name="clm1value" value="${datiRiga.W3FS3_ID}"/>
				</jsp:include>
				<c:if test="${currentRow >= 0}">
					<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"	onClick="chiaveRiga='${chiaveRigaJava}'">
						<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza" title="Visualizza" />
						<c:if test='${gene:checkProtFunz(pageContext, "MOD","MOD") && autorizzatoModifica eq "true"}'>
							<gene:PopUpItemResource resource="popupmenu.tags.lista.modifica" title="Modifica" />
						</c:if>
						<c:if test='${gene:checkProtFunz(pageContext, "DEL","DEL") && autorizzatoModifica eq "true"}'>
							<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina" title="Elimina" />
						</c:if>
					</gene:PopUp>
					<c:if test='${gene:checkProtFunz(pageContext, "DEL","LISTADELSEL") && autorizzatoModifica eq "true"}'>
						<input type="checkbox" name="keys" value="${chiaveRiga}" />
					</c:if>
				</c:if>
			</gene:campoLista>
			
			<gene:campoLista campo="ID" visibile="false"/>
			<gene:campoLista title="N�" campo="ITEM" width="40" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();"/>
			<gene:campoLista title="Contratto d'appalto n. " campo="CONTRACT_NUMBER" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" width="40"/>
			<gene:campoLista title="Lotto n. " campo="LOT_NUMBER" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" width="40"/>
			<gene:campoLista campo="CONTRACT_TITLE" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();"/>
			<gene:campoLista title="Aggiudicato?" campo="AWARDED" width="40"/>
			<gene:campoLista entita="W3FS3" campo="ID" where="W3FS3.ID = W3FS3AWARD.ID" visibile="false" />
			
		</gene:formLista></td>
	</tr>
	<tr>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiListaPage.jsp" />
	</tr>
</table>


