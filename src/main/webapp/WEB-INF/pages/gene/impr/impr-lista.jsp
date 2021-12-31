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
/* Lista dei tecnici progettisti */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>
<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "IMPR", "CODIMP")}' />
</c:if>

<gene:template file="lista-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ImprLista" >
	<gene:setString name="titoloMaschera" value="Lista anagrafica delle imprese"/>
	<c:set var="visualizzaLink" value='${gene:checkProt(pageContext, "MASC.VIS.GENE.ImprScheda")}'/>

	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td>
					<gene:formLista entita="IMPR" sortColumn="3" pagesize="20" tableclass="datilista" where="${whereProtezioneArchivi}" 
						gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreIMPRtoW3"> 
						<gene:redefineInsert name="listaNuovo" />
						<gene:redefineInsert name="listaEliminaSelezione" />
						<gene:redefineInsert name="addToAzioni" >
							<c:if test='${gene:checkProtFunz(pageContext,"INS","LISTANUOVO")}'>
								<tr>
									<td class="vocemenulaterale">
										<a href="javascript:listaNuovo();" title="Inserisci" tabindex="1502">${gene:resource("label.tags.template.lista.listaNuovo")}</a>
									</td>
								</tr>
							</c:if>
							<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL")}'>
								<tr>
									<td class="vocemenulaterale">
										<a href="javascript:listaEliminaSelezione();" title="Elimina selezionati" tabindex="1503">${gene:resource("label.tags.template.lista.listaEliminaSelezione")}</a>
									</td>
								</tr>
							</c:if>
						</gene:redefineInsert>
				
						<gene:campoLista title="Opzioni<center>${titoloMenu}</center>" width="50">
							<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "IMPR", "CODIMP", datiRiga.IMPR_CODIMP)}' />
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
								<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.ImprScheda")}' >
									<gene:PopUpItemResource variableJs="rigaPopUpMenu${currentRow}" resource="popupmenu.tags.lista.visualizza" title="Visualizza anagrafica impresa"/>
								</c:if>
								<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.ImprScheda") and gene:checkProtFunz(pageContext, "MOD", "MOD") and autorizzatoModifica eq "true"}' >
									<gene:PopUpItemResource variableJs="rigaPopUpMenu${currentRow}" resource="popupmenu.tags.lista.modifica" title="Modifica anagrafica impresa" />
								</c:if>
								<c:if test='${gene:checkProtFunz(pageContext, "DEL", "DEL") and autorizzatoModifica eq "true"}' >
									<gene:PopUpItemResource variableJs="rigaPopUpMenu${currentRow}" resource="popupmenu.tags.lista.elimina" title="Elimina anagrafica impresa" href="eliminaImpresa()"/>
								</c:if>
								<c:if test='${autorizzatoEsecuzione eq "true" && gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.gestionePermessi")}'> 
									<gene:PopUpItem title="Gestione permessi" href="javascript:apriGestionePermessi('${datiRiga.IMPR_CODIMP}')"/>
								</c:if>
							</gene:PopUp>
							<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL") and autorizzatoModifica eq "true"}'>
								<input type="checkbox" name="keys" value="${chiaveRiga}"  />
							</c:if>
						</gene:campoLista>
		
						<c:set var="link" value="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
						<gene:campoLista campo="CODIMP" headerClass="sortable" width="90" href="${gene:if(visualizzaLink, link, '')}"/>
						<gene:campoLista title="Denominazione" campo="NOMEST" headerClass="sortable"/>
						<gene:campoLista campo="CFIMP" headerClass="sortable" />
						<gene:campoLista campo="PIVIMP" headerClass="sortable" />
						<gene:campoLista campo="INDIMP" headerClass="sortable" />
						<gene:campoLista campo="NCIIMP" headerClass="sortable" />
						<gene:campoLista entita="W3IMPR" campo="TOWN" where="IMPR.CODIMP = W3IMPR.CODIMP" />
						<gene:campoLista entita="W3IMPR" campo="NUTS" where="IMPR.CODIMP = W3IMPR.CODIMP" />
					</gene:formLista>
				</td>
			</tr>
			<tr>
				<td class="comandi-dettaglio" colSpan="2">
					<gene:insert name="addPulsanti"/>
					<gene:insert name="pulsanteListaInserisci">
						<c:if test='${gene:checkProtFunz(pageContext,"INS","LISTANUOVO")}'>
							<INPUT type="button"  class="bottone-azione" value='${gene:resource("label.tags.template.lista.listaNuovo")}' title='${gene:resource("label.tags.template.lista.listaNuovo")}' onclick="javascript:listaNuovo()">
						</c:if>
					</gene:insert>
					<gene:insert name="pulsanteListaEliminaSelezione">
						<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL")}'>
							<INPUT type="button"  class="bottone-azione" value='${gene:resource("label.tags.template.lista.listaEliminaSelezione")}' title='${gene:resource("label.tags.template.lista.listaEliminaSelezione")}' onclick="javascript:listaEliminaSelezione()">
						</c:if>
					</gene:insert>
					&nbsp;
				</td>
			</tr>
		</table>
		
		<form name="formVisualizzaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/VisualizzaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="" />
			<input type="hidden" name="clm1name" id="clm1name" value="" />
			<input type="hidden" name="clm1value" id="clm1value" value="" />
		</form> 
		
	</gene:redefineInsert>
	
	<gene:javaScript>
		function eliminaImpresa(){
			var msg="Procedere con l'eliminazione?";
			if(classePopUpElimina==null){
				if(confirm(msg)){
					listaEliminaPopUp();
				}
			}else{
				showConfermaPopUp("elimina",classePopUpElimina,chiaveRiga,"listaEliminaPopUp");
			}
		}
		
		function apriGestionePermessi(codimp) {
			bloccaRichiesteServer();
			formVisualizzaPermessiUtentiGruppi.tblname.value = "IMPR";
			formVisualizzaPermessiUtentiGruppi.clm1name.value = "CODIMP";
			formVisualizzaPermessiUtentiGruppi.clm1value.value = codimp;
			formVisualizzaPermessiUtentiGruppi.submit();
		}
	</gene:javaScript>	
</gene:template>
