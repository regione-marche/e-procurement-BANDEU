<%
/*
 * Created on: 29-mag-2008
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Lista dei tecnici delle imprese */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="isProtezioneArchivi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.IsProtezioneArchiviFunction", pageContext)}'/>

<c:if test='${isProtezioneArchivi eq "true"}'>
	<c:set var="whereProtezioneArchivi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "TEIM", "CODTIM")}' />
</c:if>

<c:set var="isPopolatatW_PUSER"
	value='${gene:callFunction("it.eldasoft.gene.tags.functions.isPopolatatW_PUSERFunction", pageContext)}' />

<gene:template file="lista-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ListaTeim">
	<gene:setString name="titoloMaschera" value="Lista anagrafica dei tecnici delle imprese"/>
	<c:set var="visualizzaLink" value='${gene:checkProt(pageContext, "MASC.VIS.GENE.SchedaTeim")}'/>

	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
		<tr><td >
		<gene:formLista entita="TEIM" sortColumn="3" pagesize="20" tableclass="datilista" gestisciProtezioni="true" where="${whereProtezioneArchivi}" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreTEIMtoW3"> 
			<c:set var="tecnicoRegistrato" value='${gene:callFunction2("it.eldasoft.gene.tags.functions.TecnicoAssociatoImpresaRegistrataSuPortaleFunction",  pageContext, fn:substringAfter(chiaveRigaJava, ":") )}'/>
			
			<!-- Se il nome del campo è vuoto non lo gestisce come un campo normale -->
			<gene:campoLista title="Opzioni<center>${titoloMenu}</center>" width="50">
				<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "TEIM", "CODTIM", datiRiga.TEIM_CODTIM)}' />
				<gene:PopUp variableJs="rigaPopUpMenu${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
					<% //Aggiunta dei menu sulla riga %> 
					<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.SchedaTecni")}' >
						<gene:PopUpItemResource variableJs="rigaPopUpMenu${currentRow}" resource="popupmenu.tags.lista.visualizza" title="Visualizza anagrafica tecnico"/>
					</c:if>
					<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.SchedaTecni") and gene:checkProtFunz(pageContext, "MOD", "MOD") and tecnicoRegistrato ne "SI" and autorizzatoModifica eq "true"}' >
						<gene:PopUpItemResource variableJs="rigaPopUpMenu${currentRow}" resource="popupmenu.tags.lista.modifica" title="Modifica anagrafica tecnico" />
					</c:if>
					<c:if test='${gene:checkProtFunz(pageContext, "DEL", "DEL") and tecnicoRegistrato ne "SI" and autorizzatoModifica eq "true"}' >
						<gene:PopUpItemResource variableJs="rigaPopUpMenu${currentRow}" resource="popupmenu.tags.lista.elimina" title="Elimina anagrafica tecnico" />
					</c:if>
					
					
				</gene:PopUp>
				<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL") and tecnicoRegistrato ne "SI" and autorizzatoModifica eq "true"}'>
					<input type="checkbox" name="keys" value="${chiaveRiga}"  />
				</c:if>
			</gene:campoLista>
			<% // Campi veri e propri %>

			<c:set var="link" value="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
			<gene:campoLista campo="CODTIM" headerClass="sortable" width="90" href="${gene:if(visualizzaLink, link, '')}"/>
			<gene:campoLista campo="NOMTIM" headerClass="sortable"/>
			<gene:campoLista campo="CFTIM" headerClass="sortable" width="120"/>
			<gene:campoLista campo="PIVATEI" headerClass="sortable" width="120"/>
			<c:if test="${isPopolatatW_PUSER == 'SI' }">
				<gene:campoLista title="&nbsp;" width="20" >
					<c:if test="${tecnicoRegistrato == 'SI' }">
						<img width="16" height="16" title="Tecnico associato a impresa registrata su portale" alt="Tecnico associato a impresa registrata su portale" src="${pageContext.request.contextPath}/img/ditta_acquisita.gif"/>
					</c:if>
					
				</gene:campoLista>
			</c:if>
		</gene:formLista>
		</td></tr>
		<tr><jsp:include page="/WEB-INF/pages/commons/pulsantiLista.jsp" /></tr>
		</table>
  </gene:redefineInsert>
	
</gene:template>
