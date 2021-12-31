<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="listaOpzioniDisponibili" value="${fn:join(opzDisponibili,'#')}#" />
<c:set var="listaOpzioniUtenteAbilitate" value="${fn:join(profiloUtente.funzioniUtenteAbilitate,'#')}#" />


<div class="contenitore-arealavoro">
<div class="titolomaschera">Documenti e manuali</div>

<DIV class="contenitore-ricerca">
<table class="elencoManuali">
	<%
	//inizio riga per note di rilascio
	%>
	<tr id="rowNoteRilascio">
		<td>
			<b><a class="link-generico" href="javascript:apriManuale('${contextPath}/doc/note_release.pdf');" tabindex="2000">
							Note di rilascio</a></b><br>
				<br>
					Il documento descrive le principali novit&agrave; introdotte nel prodotto partendo da quelle relative alla 
					versione attuale fino a quelle relative a versioni meno recenti.
	</td>
	</tr>
	<%
	//fine riga per note di rilascio
	%>

	<tr><td><hr></td></tr>
	

	<%
	//inizio riga per manuale Generatore report
	%>
	<c:if test='${fn:contains(listaOpzioniDisponibili, "OP2#") && (fn:contains(listaOpzioniUtenteAbilitate, "ou48#") || fn:contains(listaOpzioniUtenteAbilitate, "ou49#"))}'>
	<tr id="rowGeneratoreReport">
		<td>
			 <b><a class="link-generico" href="javascript:apriManuale('${contextPath}/doc/generatore_report.pdf');" tabindex="2040">
							Generatore report</a></b><br>
			        <br>
					Il documento illustra l'utilizzo del generatore report per la definizione, modifica, amministrazione dei report
					di qualsiasi tipologia.
	</td>
	</tr>
	</c:if>
	<%
	//fine riga per manuale Generatore report
	%>
	<%
	//inizio riga per manuale Generatore modelli
	%>
	<c:if test='${fn:contains(listaOpzioniDisponibili, "OP1#") && (fn:contains(listaOpzioniUtenteAbilitate, "ou50#") || fn:contains(listaOpzioniUtenteAbilitate, "ou51#"))}'>
	<tr id="rowGeneratoreModelli">
		<td>
			<b><a class="link-generico" href="javascript:apriManuale('${contextPath}/doc/generatore_modelli_base.pdf');" tabindex="2050">
							Generatore modelli</a></b><br>
			        <br>
					Il documento illustra l'utilizzo del generatore modelli per la definizione, modifica, amministrazione di testi
					per la produzione di stampe.
	</td>
	</tr>
	<tr id="rowGeneratoreModelliIstruzioniAvanzate">
		<td>
			<b><a class="link-generico" href="javascript:apriManuale('${contextPath}/doc/generatore_modelli_avanzato.pdf');" tabindex="2060">
							Generatore modelli - Istruzioni avanzate</a></b><br>
			        <br>
					Il documento fornisce istruzioni per produrre stampe sulla base di una avanzata elaborazione dei dati.
	</td>
	</tr>
	<tr id="rowGeneratoreModelliIstruzioniSpeciali">
		<td>
			<b><a class="link-generico" href="javascript:apriManuale('${contextPath}/doc/generatore_modelli_speciali.pdf');" tabindex="2070">
							Generatore modelli - Istruzioni speciali</a></b><br>
			        <br>
					Il documento fornisce istruzioni speciali per produrre stampe ed elaborazioni dei dati ad uso di amministratori di sistema o 
					programmatori esperti.
	</td>
	</tr>
	</c:if>
	<%
	//fine riga per manuale Generatore modelli
	%>
	<%
	//inizio riga per manuale Gestione utenti, gruppi, profili
	%>
	<c:if test='${not empty sessionScope.profiloAttivo && fn:contains(listaOpzioniDisponibili, "OP101#") && (fn:contains(listaOpzioniUtenteAbilitate, "ou11#"))}'>
	<tr id="rowGestioneUtenti">
		<td>
			 <b><a class="link-generico" href="javascript:apriManuale('${contextPath}/doc/guida_amm.pdf');" tabindex="2080">
							Gestione utenti<c:if test='${sessionScope.moduloAttivo eq "W0"}'>, gruppi</c:if> e profili</a></b><br>
			        <br>
					Il documento descrive l'amministrazione degli utenti  
					<c:if test='${sessionScope.moduloAttivo eq "W0"}'>, la definizione di gruppi, ovvero accorpamenti 
					di utenti per la pubblicazione di report e modelli</c:if> e l'associazione degli utenti stessi ai 
					profili applicativi.
	</td>
	</tr>
	</c:if>
	<%
	//fine riga per manuale Gestione utenti, gruppi, profili
	%>
	
	<jsp:include page="/doc/manualiCustom.jsp"/>
	
</table>

</DIV>
</DIV>