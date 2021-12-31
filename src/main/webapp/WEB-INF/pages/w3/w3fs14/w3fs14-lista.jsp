
<%
	/*
	 * Created on 2-Dic-2010
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

<gene:template file="lista-template.jsp" gestisciProtezioni="true"
	idMaschera="W3FS14-lista" schema="W3">
	<gene:setString name="titoloMaschera" value="Lista delle rettifiche" />
	<gene:setString name="entita" value="W3FS14" />
	
	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td><gene:formLista entita="W3FS14" pagesize="20"
					tableclass="datilista" gestisciProtezioni="true" sortColumn="3"
					gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS14">
					<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"
						width="50">
						<c:if test="${currentRow >= 0}">
							<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "W3SIMAP", "ID", datiRiga.W3FS14_ID_RIF)}' />
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"
								onClick="chiaveRiga='${chiaveRigaJava}'">
								<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza"
									title="Visualizza" />
								<c:if test='${gene:checkProtFunz(pageContext, "MOD","MOD") and autorizzatoModifica eq "true"}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.modifica"
										title="Modifica" />
								</c:if>
								<c:if test='${gene:checkProtFunz(pageContext, "DEL","DEL") and autorizzatoModifica eq "true"}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina"
										title="Elimina" />
								</c:if>
							</gene:PopUp>
							<c:if test='${gene:checkProtFunz(pageContext, "DEL","LISTADELSEL") and autorizzatoModifica eq "true"}'>
								<input type="checkbox" name="keys" value="${chiaveRiga}" />
							</c:if>
						</c:if>
					</gene:campoLista>
					<gene:campoLista campo="ID" visibile="false" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
					<gene:campoLista title="Motivo della modifica" campo="CORRECT_REASON" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
					<gene:campoLista title="Numero avviso GU/S" entita="W3SIMAP" campo="NOTICE_NUMBER_OJ" where="W3SIMAP.ID = W3FS14.ID"/>
					<gene:campoLista entita="W3SIMAP" campo="DATE_OJ" where="W3SIMAP.ID = W3FS14.ID"/>
					<gene:campoLista entita="V_W3SIMAP" width="70" campo="NOTICE_DATE" where="V_W3SIMAP.ID = W3FS14.ID" ordinabile="false"/>
					<gene:campoLista campo="ID_RIF" visibile="false"/>
				</gene:formLista></td>
			</tr>
			<tr>
				<td class="comandi-dettaglio" colSpan="2">
					<gene:insert name="pulsanteListaEliminaSelezione">
						<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL")}'>
							<INPUT type="button"  class="bottone-azione" value='${gene:resource("label.tags.template.lista.listaEliminaSelezione")}' title='${gene:resource("label.tags.template.lista.listaEliminaSelezione")}' onclick="javascript:listaEliminaSelezione()">
						</c:if>
					</gene:insert>
				</td>
			</tr>
			<gene:redefineInsert name="listaNuovo" />
		</table>
	
	</gene:redefineInsert>
	
</gene:template>