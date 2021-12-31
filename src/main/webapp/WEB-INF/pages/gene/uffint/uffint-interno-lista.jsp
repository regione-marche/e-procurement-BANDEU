<%
/*
 * Created on: 06-Nov-2009
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Lista degli uffici intestatari */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

	<gene:campoLista title="Opzioni<center>${titoloMenu}</center>" width="50">
		<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "UFFINT", "CODEIN", datiRiga.UFFINT_CODEIN)}' />
		<gene:PopUp variableJs="rigaPopUpMenu${currentRow}" onClick="chiaveRiga='${chiaveRigaJava}'">
		<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL") and autorizzatoModifica eq "true"}'>
			<input type="checkbox" name="keys" value="${chiaveRiga}"  />
		</c:if>
		<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.SchedaUffint")}' >
			<gene:PopUpItemResource resource="popupmenu.tags.lista.visualizza" title="Visualizza"/>
		</c:if>
		<c:if test='${gene:checkProtObj(pageContext, "MASC.VIS", "GENE.SchedaUffint") and autorizzatoModifica eq "true" and gene:checkProtFunz(pageContext, "MOD", "MOD")}' >
			<gene:PopUpItemResource resource="popupmenu.tags.lista.modifica" title="Modifica" />
		</c:if>
		<c:if test='${gene:checkProtFunz(pageContext, "DEL", "DEL") and autorizzatoModifica eq "true"}' >
			<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina" title="Elimina" />
		</c:if>
		<c:if test='${autorizzatoEsecuzione eq "true" && gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.gestionePermessi")}'> 
			<gene:PopUpItem title="Gestione permessi" href="javascript:apriGestionePermessi('${datiRiga.UFFINT_CODEIN}')"/>
		</c:if>
		</gene:PopUp>
	</gene:campoLista>

	<c:set var="link" value="javascript:chiaveRiga='${chiaveRigaJava}';listaVisualizza();" />
	<gene:campoLista title="Codice" campo="CODEIN" headerClass="sortable" width="90" href="${gene:if(visualizzaLink, link, '')}"/>
	<gene:campoLista campo="NOMEIN" headerClass="sortable"/>
	<gene:campoLista campo="CFEIN" headerClass="sortable" width="120"/>
	<gene:campoLista campo="IVAEIN" headerClass="sortable" />
	<gene:campoLista campo="VIAEIN" headerClass="sortable" />
	<gene:campoLista title="N. civico" campo="NCIEIN" headerClass="sortable"/>
	<gene:campoLista entita="W3UFFINT" campo="TOWN" where="UFFINT.CODEIN = W3UFFINT.CODEIN" headerClass="sortable"/>
	<gene:campoLista entita="W3UFFINT" campo="NUTS" where="UFFINT.CODEIN = W3UFFINT.CODEIN" headerClass="sortable"/>