
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

<c:set var="filtroPermessi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "W3SIMAPWS", "ID")}' />

<gene:template file="lista-template.jsp" gestisciProtezioni="true"
	idMaschera="W3SIMAPWS-lista" schema="W3">
	<gene:setString name="titoloMaschera" value="Lista degli invii/notifiche" />
	<gene:setString name="entita" value="W3SIMAPWS" />
	
	<gene:redefineInsert name="head">
		<style type="text/css">
		
			span.notificanonletta {
				color: black;
				float: left;
				background-color: #FFA293; 
				padding-left: 5px;
				padding-right: 15px;
				padding-top: 1px;
				padding-bottom: 1px;
				border: 1px solid #C60010; 
				-moz-border-radius-topleft: 4px; 
				-webkit-border-top-left-radius: 4px; 
				-khtml-border-top-left-radius: 4px; 
				border-top-left-radius: 4px; 
			}

			span.notificaletta {
				color: black;
				float: left;
				margin-left: 5px;
				padding-left: 15px;
				padding-right: 20px;
				padding-top: 1px;
				padding-bottom: 1px;
			}
			
		</style>
	
	</gene:redefineInsert>
	
	<gene:redefineInsert name="corpo">
		<table class="lista">
			<tr>
				<td><gene:formLista entita="W3SIMAPWS" where="${filtroPermessi}" pagesize="20" tableclass="datilista" gestisciProtezioni="true" sortColumn="-4;-6" >
					<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"
						width="50">
						<c:if test="${currentRow >= 0}">
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"	onClick="chiaveRiga='${chiaveRigaJava}'">
								<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza" title="Visualizza" />
								<c:if test='${gene:checkProtFunz(pageContext, "DEL","DEL") && datiRiga.W3SIMAPWS_PHASE ne "GAMMA"}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina" title="Elimina" />
								</c:if>								
							</gene:PopUp>
						</c:if>
					</gene:campoLista>
					<gene:campoLista campo="ID" visibile="false" />	
					<gene:campoLista campo="NUM" visibile="false" />
					<gene:campoLista title="Stato notifica" campo="STATO" width="75" />
					<gene:campoLista campo="PHASE" width="90" />
					<gene:campoLista campo="SUBMISSION_DATE" width="90"/>
					<gene:campoLista campo="SUBMISSION_ID" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
					<gene:campoLista campo="STATUS_CODE" />
					<gene:campoLista campo="STATUS_DESCRIPTION" />
					<gene:campoLista entita="W3SIMAP" campo="FORM" where="W3SIMAPWS.ID = W3SIMAP.ID"/>			
					<gene:campoLista title="Denominazione dell'appalto" entita="V_W3SIMAP" campo="TITLE_CONTRACT" where="W3SIMAPWS.ID = V_W3SIMAP.ID"/>
					<gene:campoLista entita="V_W3SIMAP" campo="CTYPE" where="W3SIMAPWS.ID = V_W3SIMAP.ID"/>
				</gene:formLista></td>
			</tr>
		</table>
	</gene:redefineInsert>
	
	<gene:redefineInsert name="listaNuovo" />
	<gene:redefineInsert name="listaEliminaSelezione" />
	
	<gene:javaScript>
	
		evidenziaStatoLettura();
	
		function evidenziaStatoLettura() {
			$("[id^=colW3SIMAPWS_STATO_]").each(function() {
				var _v = $(this).text();
				if (_v == 'Non letta') {
					$(this).addClass("notificanonletta");
				} else if (_v == 'Non letta') {
					$(this).addClass("notificanonletta");
				}
			
			});
		}
	
	
	</gene:javaScript>
	
</gene:template>