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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "TECNI", "CODTEC")}' />
</c:if>

<gene:template file="lista-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ListaTecni">
	<gene:setString name="titoloMaschera" value="Lista anagrafica dei tecnici"/>
	<c:set var="visualizzaLink" value='${gene:checkProt(pageContext, "MASC.VIS.GENE.SchedaTecni")}'/>

	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td>
					<gene:formLista entita="TECNI" sortColumn="3" pagesize="20" tableclass="datilista" gestisciProtezioni="true" where="${whereProtezioneArchivi}" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreTECNItoW3"> 
						<gene:campoLista title="Opzioni<center>${titoloMenu}</center>" width="50">
							<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "TECNI", "CODTEC", datiRiga.TECNI_CODTEC)}' />
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
								<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL") and autorizzatoModifica eq "true"}'>
									<input type="checkbox" name="keys" value="${chiaveRiga}"  />
								</c:if>
								<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.SchedaTecni")}' >
									<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza" title="Visualizza anagrafica tecnico"/>
								</c:if>
								<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.SchedaTecni") and gene:checkProtFunz(pageContext, "MOD", "MOD") and autorizzatoModifica eq "true"}' >
									<gene:PopUpItemResource resource="popupmenu.tags.lista.modifica" title="Modifica anagrafica tecnico" />
								</c:if>
								<c:if test='${gene:checkProtFunz(pageContext, "DEL", "DEL") and autorizzatoModifica eq "true"}' >
									<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina" title="Elimina anagrafica tecnico" />
								</c:if>
								<c:if test='${autorizzatoEsecuzione eq "true" && gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.gestionePermessi")}'> 
									<gene:PopUpItem title="Gestione permessi" href="javascript:apriGestionePermessi('${datiRiga.TECNI_CODTEC}')"/>
								</c:if>
							</gene:PopUp>
						</gene:campoLista>
						<c:set var="link" value="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
						<gene:campoLista campo="CODTEC" headerClass="sortable" width="90" href="${gene:if(visualizzaLink, link, '')}"/>
						<gene:campoLista campo="NOMTEC" headerClass="sortable"/>
						<gene:campoLista campo="CFTEC" headerClass="sortable" width="120"/>
						<gene:campoLista campo="PIVATEC" headerClass="sortable" width="120"/>
					</gene:formLista>
				</td>
			</tr>
			<tr>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiLista.jsp" />
			</tr>
		</table>
		
		<form name="formVisualizzaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/VisualizzaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="" />
			<input type="hidden" name="clm1name" id="clm1name" value="" />
			<input type="hidden" name="clm1value" id="clm1value" value="" />
		</form> 
		
		<gene:javaScript>
			function apriGestionePermessi(codtec) {
				bloccaRichiesteServer();
				formVisualizzaPermessiUtentiGruppi.tblname.value = "TECNI";
				formVisualizzaPermessiUtentiGruppi.clm1name.value = "CODTEC";
				formVisualizzaPermessiUtentiGruppi.clm1value.value = codtec;
				formVisualizzaPermessiUtentiGruppi.submit();
			}
		</gene:javaScript>
  </gene:redefineInsert>
	
</gene:template>
