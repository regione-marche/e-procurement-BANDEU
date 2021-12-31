
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

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>

<gene:template file="lista-template.jsp" gestisciProtezioni="true"
	idMaschera="W3SIMAPEMAIL-lista" schema="W3">
	<gene:setString name="titoloMaschera" value="Lista degli invii SIMAP" />
	<gene:setString name="entita" value="W3SIMAPEMAIL" />
	
	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td><gene:formLista entita="W3SIMAPEMAIL" pagesize="20"
					tableclass="datilista" gestisciProtezioni="true" sortColumn="-4"
					gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPEMAIL"
					pathScheda="w3/w3simapemail/w3simapemail-scheda-invio.jsp">
					<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"
						width="50">
						<c:if test="${currentRow >= 0}">
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"
								onClick="chiaveRiga='${chiaveRigaJava}'">
								<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza"
									title="Visualizza" />
								<c:if test='${gene:checkProtFunz(pageContext, "MOD","MOD")}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.modifica"
										title="Modifica" />
								</c:if>
							</gene:PopUp>
						</c:if>
					</gene:campoLista>
					<gene:campoLista campo="ID" visibile="false" />	
					<gene:campoLista campo="NUM" visibile="false" />
					<gene:campoLista campo="DATA_SPEDIZIONE"  href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
					<gene:campoLista campo="EMAILPHASE" />
					<gene:campoLista entita="W3SIMAP" campo="NOTICE_NUMBER_OJ" where="W3SIMAPEMAIL.ID = W3SIMAP.ID"/>
					<gene:campoLista entita="W3SIMAP" campo="DATE_OJ" />
					<gene:campoLista entita="W3SIMAP" campo="FORM" />			
					<gene:campoLista entita="V_W3SIMAP" campo="CTYPE" where="W3SIMAPEMAIL.ID = V_W3SIMAP.ID"/>
					<gene:campoLista entita="V_W3SIMAP" campo="TITLE_CONTRACT" />
						

				</gene:formLista></td>
			</tr>
			<gene:redefineInsert name="listaNuovo" />
		</table>
	
	</gene:redefineInsert>

	
</gene:template>