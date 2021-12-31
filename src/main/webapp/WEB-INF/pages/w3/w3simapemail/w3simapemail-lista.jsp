
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
	<gene:setString name="titoloMaschera" value="Lista degli invii SIMAP effettuati via e-mail" />
	<gene:setString name="entita" value="W3SIMAPEMAIL" />
	
	<gene:redefineInsert name="corpo">
		<table class="lista">
			<tr>
				<td>
					<gene:formLista entita="W3SIMAPEMAIL" pagesize="20"	tableclass="datilista" gestisciProtezioni="true" sortColumn="3"
						gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPEMAIL">
						<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"
							width="50">
							<c:if test="${currentRow >= 0}">
								<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"	onClick="chiaveRiga='${chiaveRigaJava}'">
									<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza" title="Visualizza" />
								</gene:PopUp>
							</c:if>
						</gene:campoLista>
						<gene:campoLista campo="ID" visibile="false" />	
						<gene:campoLista title="N°" campo="NUM" width="30" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
						<gene:campoLista campo="DATA_CREAZIONE" visibile="false"/>
						<gene:campoLista campo="DATA_SPEDIZIONE_S" />
						<gene:campoLista campo="EMAILPHASE" />
						<gene:campoLista campo="EMAILTO" />
						<gene:campoLista campo="EMAILSUBJECT" />
						<gene:campoLista campo="ATTACHMENT_NAME"  visibile="false"/>
						<gene:campoLista title="&nbsp;" width="24">
							<a href="javascript:visualizzaAttachment('${datiRiga.W3SIMAPEMAIL_ID}','${datiRiga.W3SIMAPEMAIL_NUM}','${datiRiga.W3SIMAPEMAIL_ATTACHMENT_NAME}');" title="Visualizza o scarica l'allegato" >
								<img width="24" height="24" title="Visualizza o scarica l'allegato" alt="Visualizza o scarica l'allegato" src="${pageContext.request.contextPath}/img/8629505_5.gif"/>
							</a>
						</gene:campoLista>
					</gene:formLista>
				</td>
			</tr>
		</table>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="listaNuovo" />
	<gene:redefineInsert name="listaEliminaSelezione" />
	
	<gene:javaScript>
	
		function visualizzaAttachment(id,num,attachment_name) {
			var href = "${pageContext.request.contextPath}/w3/VisualizzaAttachment.do";
			document.location.href=href+"?id=" + id + "&num=" + num + "&attachment_name=" + attachment_name;
		}
		

	</gene:javaScript>	
	
</gene:template>