<%/*
       * Created on 14-set-2006
       *
       * Copyright (c) EldaSoft S.p.A.
       * Tutti i diritti sono riservati.
       *
       * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
       * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
       * aver prima formalizzato un accordo specifico con EldaSoft.
       */

      // PAGINA CHE CONTIENE IL TEMPLATE DELLE PAGINE DI POPUP
    %>


<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>

<%-- inserito per gestire i problemi con determinati plugin jquery nelle schede di dettaglio, in IE, per cui va cambiato il document type --%>
<gene:insert name="doctype" src="/WEB-INF/pages/commons/doctype.jsp" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<HTML>
<HEAD>
<c:if test="${requestScope.forzaRedirect}">
	<meta HTTP-EQUIV="REFRESH" content="0; url=${pageContext.request.contextPath}/ErrorOpenScheda.do"></meta>
</c:if>
<jsp:include page="/WEB-INF/pages/commons/headStd.jsp" />

<script type="text/javascript">
<!--
<jsp:include page="/WEB-INF/pages/commons/checkDisabilitaBack.jsp" />
  
$(function() {
	$( "input.data" ).datepicker($.datepicker.regional[ "it" ]);
});

window.onfocus=fnFocus;
  
  // al click nel documento si chiudono popup e menu
  if (ie4||ns6) document.onclick=hideSovrapposizioni;

  function hideSovrapposizioni() {
    hideMenuPopup();
    hideSubmenuNavbar();
  }
-->
</script>

<script type="text/javascript">	
	$(document).ready(function() {
		$(document).bind('keydown', 'return', function(e) {
			if ((e.which || e.keyCode) == 116 || (e.ctrlKey && (e.which || e.keyCode) == 82)) {
				e.preventDefault();
			}
		});
	});	
</script>

<gene:insert name="addHistory" >
<c:if test='${modo ne "NUOVO" and modo ne "MODIFICA" }'>
	<c:set var="idHistory" value='${gene:getString(pageContext,"titoloMaschera",gene:resource("label.tags.template.popup.titolo"))}' />
	<%/*Eseguo il replace per l eliminazione dell eventuale Update in inserimento */%>
	
	<gene:historyAdd titolo='${idHistory}' 
		id="${idHistory}" 
		replaceParam='${gene:if( param.metodo eq "update", "metodo;apri;modo;VISUALIZZA", "" )}' />
</c:if>
</gene:insert>
<!-- M.F. 27/09/2006 Aggiungo gli oggetti e le funzioni javascript per la gestione delle form e dei messaggi -->
<script type="text/javascript"
  src="${contextPath}/js/forms.js"></script>
<gene:insert name="jsAzioniTags" src="/WEB-INF/pages/commons/jsAzioniTags.jsp" />
<gene:insert name="head" >
  </gene:insert>
</HEAD>
<BODY onload="setVariables();checkLocation();">
<jsp:include page="/WEB-INF/pages/commons/bloccaRichieste.jsp" />


<table class="lista">
<gene:insert name="gestioneHistory" >
	<%/* Aggiungo il back solo se l'history è maggiore di 1*/%>
	<c:if test='${historySize > 1 and modo ne "NUOVO" and modo ne "MODIFICA" }' >
	<tr>
		<td class="comandi-dettaglio" colspan="2">
			<INPUT type="button" class="bottone-azione" value="Torna alla lista" title="Torna alla lista" onclick="javascript:historyVaiA(${historySize - 2});">
		&nbsp;
		<%/* Aggiungo seleziona da archivio solo per il dettaglio (seconda nell history) e solo sulla prima pagina */%>
		<c:if test='${historySize == 2 and activePage == 0 }'>
			<INPUT type="button" class="bottone-azione" value="Seleziona" title="Seleziona" onclick="javascript:archivioSelezionaScheda();">
		&nbsp;
		</c:if>
		</td>
	</tr>
	</c:if>
</gene:insert>
<tr>
	<td width="90%" align="left" colspan="2">
  	<div class="titolomaschera"><gene:getString name="titoloMaschera" defaultVal='${gene:resource("label.tags.template.popup.titolo")}'/></div>
	</td>
</tr>

</table>


<div class="contenitore-errori-arealavoro"><jsp:include page="/WEB-INF/pages/commons/serverMsg.jsp" /></div>
<div class="contenitore-errori-arealavoro"><jsp:include page="/WEB-INF/pages/commons/javascriptMsg.jsp" /></div>
		<div style="width:97%" class="contenitore-dettaglio">
			<gene:insert name="corpo" >
				<b><big>Qui bisogna inserire il corpo delle ricerca !!!<br/>
				Se vedete questa scritta manca qualcosa.</big></b>
			</gene:insert>
		</div>
		<%// Insert tag lasciato per la ridefinizione dei messaggi di debug %>
		<gene:insert name="debug" />
		<gene:insert name="debugDefault" src="/WEB-INF/pages/commons/sviluppo/sviluppo-debug.jsp"/>
		<%/* Aggiungo l eventuale archivio di passaggio */%>
		${gene:callFunction("it.eldasoft.gene.tags.utils.functions.ArchivioFormFunction",pageContext)}
		<!-- PARTE NECESSARIA PER VISUALIZZARE I POPUP MENU DI OPZIONI PER CAMPO -->
		<IFRAME class="gene" id="iframepopmenu"></iframe>
		<div id="popmenu" class="popupmenuskin"
			onMouseover="highlightMenuPopup(event,'on');"
			onMouseout="highlightMenuPopup(event,'off');"></div>
		<!-- CAMPO INDISPENSABILE PER ESEGUIRE IL COPIA/INCOLLA NEGLI APPUNTI DEL MNEMONICO CON IE -->
		<form action=""><input type="hidden" id="clipboard"></form>
		<gene:outJavaScript/>
		<script type="text/javascript">
    <!--
    	// Setto il numero di popup su tutte le form standard
    	setNumeroPopUp();
    	// introdotto il parametro "abilitaNuovo" che, per popup di livello 2 o superiore, se valorizzato a 1, 
    	// permette l'inserimento nell'archivio. Tale parametro va valorizzato dal chiamante nell'attributo "lista"
    	// del tag "gene:archivio"
    	if (document.forms[0].numeroPopUp && document.forms[0].numeroPopUp.value >= 2 && document.getElementById("btnNuovo") && ${param.abilitaNuovo ne 1}) {
    	    document.getElementById("btnNuovo").style.visibility = "hidden";
    	}
    	
    	<c:if test='${param.abilitaNuovo eq 1}'>
				document.forms[0].action = document.forms[0].action + "?abilitaNuovo=1";
    	</c:if>
    //-->
    </script>
</BODY>
</HTML>