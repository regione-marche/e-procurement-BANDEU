
<%
	/*
	 * Created on 05-Ott-2010
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

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "W3AMMI", "CODAMM")}' />
</c:if>


<gene:template file="lista-template.jsp" gestisciProtezioni="true"
	idMaschera="W3AMMI-lista" schema="W3">
	<gene:setString name="titoloMaschera" value="Lista delle amministrazioni aggiudicatrici" />
	<gene:setString name="entita" value="W3AMMI" />
	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td><gene:formLista entita="W3AMMI" pagesize="20" tableclass="datilista" gestisciProtezioni="true" sortColumn="2" 
					gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3AMMI" where = "${whereProtezioneArchivi}"> 
					<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"	width="50">
						<c:if test="${currentRow >= 0}">
							<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "W3AMMI", "CODAMM", datiRiga.W3AMMI_CODAMM)}' />
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"	onClick="chiaveRiga='${chiaveRigaJava}'">
								<gene:PopUpItemResource	resource="popupmenu.tags.lista.visualizza" title="Visualizza" />
								<c:if test='${gene:checkProtFunz(pageContext, "MOD","MOD") && autorizzatoModifica eq "true"}'>
									<gene:PopUpItemResource	resource="popupmenu.tags.lista.modifica" title="Modifica" />
								</c:if>
								<c:if test='${gene:checkProtFunz(pageContext, "DEL","DEL") && autorizzatoModifica eq "true"}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina" title="Elimina" />
								</c:if>
								<c:if test='${autorizzatoEsecuzione eq "true" && gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.gestionePermessi")}'> 
									<gene:PopUpItem title="Gestione permessi" href="javascript:apriGestionePermessi('${datiRiga.W3AMMI_CODAMM}')"/>
								</c:if>
							</gene:PopUp>
							<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL") && autorizzatoModifica eq "true"}'>
								<input type="checkbox" name="keys" value="${chiaveRiga}" />
							</c:if>
						</c:if>
					</gene:campoLista>
					<gene:campoLista campo="CODAMM" visibile="true" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />	
					<gene:campoLista entita="UFFINT" campo="NOMEIN" where="W3AMMI.CODEIN=UFFINT.CODEIN" href="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" ordinabile="false"/>
					<gene:campoLista title="Identificativo amministrazione" campo="CUSTOMER_LOGIN" width="140"/>
					<gene:campoLista title="Persona di contatto" entita="UFFINT" campo="NOMRES" where="W3AMMI.CODEIN=UFFINT.CODEIN"	/>
				</gene:formLista></td>
			</tr>
			<tr><jsp:include page="/WEB-INF/pages/commons/pulsantiLista.jsp" /></tr>
		</table>
		
		<form name="formVisualizzaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/VisualizzaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="" />
			<input type="hidden" name="clm1name" id="clm1name" value="" />
			<input type="hidden" name="clm1value" id="clm1value" value="" />
		</form> 
		
		<gene:javaScript>
			function apriGestionePermessi(codamm) {
				bloccaRichiesteServer();
				formVisualizzaPermessiUtentiGruppi.tblname.value = "W3AMMI";
				formVisualizzaPermessiUtentiGruppi.clm1name.value = "CODAMM";
				formVisualizzaPermessiUtentiGruppi.clm1value.value = codamm;
				formVisualizzaPermessiUtentiGruppi.submit();
			}
		</gene:javaScript>
		
	</gene:redefineInsert>
	
</gene:template>