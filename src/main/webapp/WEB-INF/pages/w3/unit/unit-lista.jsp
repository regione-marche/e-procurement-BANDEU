
<%
	/*
	 * Created on 27-Ago-2014
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
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>


<gene:template file="lista-template.jsp" gestisciProtezioni="true" idMaschera="UNIT-lista" schema="W3">
	<gene:setString name="titoloMaschera" value="Lista delle unit&agrave; organizzative" />
	<gene:setString name="entita" value="UNIT" />
	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td><gene:formLista entita="UNIT" pagesize="20" tableclass="datilista" gestisciProtezioni="true" sortColumn="2" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreUNIT"> 
					<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"	width="50">
						<c:if test="${currentRow >= 0}">
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"	onClick="chiaveRiga='${chiaveRigaJava}'">
								<gene:PopUpItemResource	resource="popupmenu.tags.lista.visualizza" title="Visualizza" />
								<c:if test='${gene:checkProtFunz(pageContext, "MOD","MOD")}'>
									<gene:PopUpItemResource	resource="popupmenu.tags.lista.modifica" title="Modifica" />
								</c:if>
								<c:if test='${gene:checkProtFunz(pageContext, "DEL","DEL")}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina" title="Elimina" />
								</c:if>
							</gene:PopUp>
							<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL")}'>
								<input type="checkbox" name="keys" value="${chiaveRiga}" />
							</c:if>
						</c:if>
					</gene:campoLista>
					<gene:campoLista width="90" campo="IDUNIT" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />	
					<gene:campoLista campo="DESCUNIT" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();"/>
				</gene:formLista></td>
			</tr>
			<tr><jsp:include page="/WEB-INF/pages/commons/pulsantiLista.jsp" /></tr>
		</table>
	</gene:redefineInsert>
	
</gene:template>