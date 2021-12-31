<%/*
       * Created on 04-ott-2013
       *
       * Copyright (c) EldaSoft S.p.A.
       * Tutti i diritti sono riservati.
       *
       * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
       * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
       * aver prima formalizzato un accordo specifico con EldaSoft.
       */

      %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="fnucase" value='${gene:callFunction("it.eldasoft.gene.tags.utils.functions.GetUpperCaseDBFunction", "")}' />

<c:if test='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.W3HOME.GestioneAmministrazioni")}'>
	<script type="text/javascript">
		document.location.href = "${contextPath}/ApriPagina.do?href=w3/w3ammi/w3ammi-trova.jsp";
	</script>
</c:if>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >


<c:set var="filtroLivelloUtenteSIMAP" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "W3SIMAP", "ID")}' />

<gene:setIdPagina schema="W3" maschera="HomeW3" />

<gene:historyClear />

<HTML>
<HEAD>
<jsp:include page="/WEB-INF/pages/commons/headStd.jsp" />
<script type="text/javascript">
<!--
  <jsp:include page="/WEB-INF/pages/commons/checkDisabilitaBack.jsp" />
  if (ie4||ns6) document.onclick=hideSovrapposizioni;

  function hideSovrapposizioni() {
    //hideSubmenuNavbar();
    hideMenuPopup();
    hideSubmenuNavbar();
  }
-->
</script>
<jsp:include page="/WEB-INF/pages/commons/jsSubMenuComune.jsp" />
<jsp:include page="/WEB-INF/pages/commons/jsSubMenuSpecifico.jsp" />
<jsp:include page="/WEB-INF/pages/commons/vuoto.jsp" />
<script type="text/javascript">
<!--
		function trovaBandoAvviso(){
		 	var lsStr=formfind.findstr.value.toUpperCase();
			var where = "";
			var parametri = "";
			var filtroUtenteSIMAP = "${filtroLivelloUtenteSIMAP}";
			if(lsStr!=null && lsStr!=""){
				where += " exists (select * from v_w3simap where w3simap.id = v_w3simap.id and (${fnucase}( V_W3SIMAP.TITLE_CONTRACT ) like ? ))";
				parametri += "T:%"+lsStr+"%";
			}
			if (filtroUtenteSIMAP != ""){
			  if (where != "") {	
			  where += " AND "
			  }
			  where += filtroUtenteSIMAP;
			}
			
			formTrova.trovaAddWhere.value=where;
			formTrova.trovaParameter.value=parametri;
			bloccaRichiesteServer();
			formTrova.submit();
		}
		
		function nuovoBandoAvviso(){
			bloccaRichiesteServer();
			listaNuovo.submit();
		}
		
		function trovaBandoAvvisoAvanzata(){
			apriPagina.href.value="w3/w3simap/w3simap-trova.jsp";
			bloccaRichiesteServer();
			apriPagina.submit();
		}
		
		function trovaInvii(){
			apriPagina.href.value="w3/w3simapemail/w3simapemail-trova.jsp";
			bloccaRichiesteServer();
			apriPagina.submit();
		}
		
		function trovaInviiWS(){
			apriPagina.href.value="w3/w3simapws/w3simapws-trova.jsp";
			bloccaRichiesteServer();
			apriPagina.submit();
		}
				
-->
</script>
</HEAD>

<BODY onload="setVariables();checkLocation();initPage();">
<jsp:include page="/WEB-INF/pages/commons/bloccaRichieste.jsp" />
<TABLE class="arealayout">
	<!-- questa definizione dei gruppi di colonne serve a fissare la dimensione
	     dei td in modo da vincolare la posizione iniziale del menù di navigazione
	     sopra l'area lavoro appena al termine del menù contestuale -->
	<colgroup width="150px"></colgroup>
	
	<colgroup width="*"></colgroup>
	<TBODY>
		<TR class="testata">
			<TD colspan="2">
			<jsp:include page="/WEB-INF/pages/commons/testata.jsp" />
			</TD>
		</TR>
		<TR class="menuprincipale">
			<TD></TD>
			<TD>
			<table class="contenitore-navbar">
				<tbody>
					<tr>
						<jsp:include page="/WEB-INF/pages/commons/menuSpecifico.jsp" />
						<jsp:include page="/WEB-INF/pages/commons/menuComune.jsp" />
					</tr>
				</tbody>
			</table>

			<!-- PARTE NECESSARIA PER VISUALIZZARE I SOTTOMENU DEL MENU PRINCIPALE DI NAVIGAZIONE -->
			<iframe id="iframesubnavmenu" class="gene"></iframe>
			<div id="subnavmenu" class="subnavbarmenuskin"
				onMouseover="highlightSubmenuNavbar(event,'on');"
				onMouseout="highlightSubmenuNavbar(event,'off');"></div>
				
			<!-- PARTE NECESSARIA PER VISUALIZZARE I POPUP MENU DI OPZIONI PER CAMPO -->
			<IFRAME class="gene" id="iframepopmenu"></iframe>
			<div id="popmenu" class="popupmenuskin"
				onMouseover="highlightMenuPopup(event,'on');"
				onMouseout="highlightMenuPopup(event,'off');"></div>				
				
			</TD>
		</TR>
		<TR>
			<TD class="menuazioni" valign="top">
			<div id="menulaterale"></div>
			</TD>
			<TD class="arealavoro">
				<jsp:include page="/WEB-INF/pages/commons/areaPreTitolo.jsp" />
<%-- 				<c:set var="conteggi" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.GetContatoriSIMAPFunction", pageContext)}' /> --%>
				<div class="contenitore-arealavoro">
					<form name="formfind" action="javascript:trovaBandoAvviso();">
						<table align="left" class="arealayout" style="width: 500">
							<tr>
								<td><div class="margin0px titolomaschera">${sessionScope.nomeProfiloAttivo}</div>
								<p>${sessionScope.descProfiloAttivo}</p>

								</td>
							</tr>
							<tr>
								<td valign="middle">
									<br/><br/><div class="titoloInputGoogle">ricerca bandi ed avvisi</div>
									<input type="text" name="findstr" size="50" class="testo-cerca-oggetto" align="top" />
									<a href="javascript:trovaBandoAvviso();">
									<img src="${contextPath}/img/${applicationScope.pathImg}trova_oggetto.png" alt="Ricerca bandi ed avvisi" align="top">
									</a>
								</td>
							</tr>
					
							<c:if test='${gene:checkProt(pageContext, "MENU.VIS.BANDI")}'>
								<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.BANDI.Trova-bandi")}'>
									<tr>
										<td><br><br>
										<b>
										<a class="link-generico" href="javascript:trovaBandoAvvisoAvanzata()"
											title="Ricerca avanzata dei bandi e degli avvisi">Ricerca avanzata bandi ed avvisi</a>
										</b>
										</td>
									</tr>
								</c:if>
							</c:if>
							
<!-- 							<tr> -->
<!-- 								<td> -->
<!-- 									<br> -->
<%-- 									Bandi/avvisi inseriti in banca dati: <b>${totali}</b> --%>
<!-- 									<br> -->
<%-- 									Bandi/avvisi inviati a SIMAP: <b>${inviati}</b> --%>
<!-- 									<br> -->
<%-- 									Bandi/avvisi pubblicati sulla Gazzetta Ufficiale dell'Unione Europea: <b>${pubblicati}</b> --%>
<!-- 								</td> -->
<!-- 							</tr> -->
					
							<c:if test='${gene:checkProt(pageContext, "MENU.VIS.BANDI")}'>
								<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.BANDI.Trova-invii")}'>
									<tr>
										<td><br><br>
										<b>
										<a class="link-generico" href="javascript:trovaInvii()"
											title="Ricerca avanzata degli invii e delle notifiche">Ricerca avanzata invii/notifiche</a>
										</b>
										</td>
									</tr>
								</c:if>
							</c:if>
					
							<c:if test='${gene:checkProt(pageContext, "MENU.VIS.BANDI")}'>
								<c:if test='${gene:checkProt(pageContext, "SUBMENU.VIS.BANDI.Trova-invii-WS")}'>
									<tr>
										<td><br><br>
										<b>
										<a class="link-generico" href="javascript:trovaInviiWS()"
											title="Ricerca avanzata degli invii e delle notifiche">Ricerca avanzata invii/notifiche</a>
										</b>
										</td>
									</tr>
								</c:if>
							</c:if>
					
							<c:if test='${gene:checkProtFunz(pageContext,"INS","SCHEDANUOVO")}'>
								<tr>
									<td><br>
									<br><b>
									<a class="link-generico" href="javascript:nuovoBandoAvviso();"
										title="Nuovo bando o avviso">Crea nuovo bando o avviso</a></b></td>
								</tr>
							</c:if>
							
							<c:set var="numeroNotifiche" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.GetNumeroNotificheNonLetteFunction", pageContext)}' />
							<c:if test='${numeroNotifiche > 0}'>
								<tr>
									<td>
										<br><br>
										<table class="arealayout">
											<tr>
												<td>
													<img width="32" height="32" src="${pageContext.request.contextPath}/img/Communication-61.png"/>&nbsp;
												</td>
												<td>
													<b>Notifiche provenienti dai servizi SIMAP</b><br>
													<c:choose>
														<c:when test="${numeroNotifiche > 1}">
															Sono presenti ${numeroNotifiche} notifiche non lette, consulta la <b>
															<a class="link-generico" href="${contextPath}/ApriPagina.do?href=w3/w3simapws/w3simapws-lista.jsp&stato=2"
																title="Lista degli invii/notifiche">lista degli invii/notifiche.</a></b></td>
														</c:when>
														<c:otherwise>
															E' presente una notifica non letta, consulta la <b>
															<a class="link-generico" href="${contextPath}/ApriPagina.do?href=w3/w3simapws/w3simapws-lista.jsp&stato=2"
																title="Lista degli invii/notifiche">lista degli invii/notifiche.</a></b></td>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>								
										</table>							
									</td>
								</tr>
							</c:if>	

							<tr>
								<td align="center"><br>
								<br>
								<br>
								<br>
								<p style="color: #707070;">
									<jsp:include page="/WEB-INF/pages/gene/login/infoCustom.jsp"/>					
								</p>
								</td>
							</tr>
						</table>
					</form>
					<form name="formTrova" action="${contextPath}/ApriPagina.do" method="post">
						<input type="hidden" name="href" value="w3/w3simap/w3simap-lista.jsp" /> 
						<input type="hidden" name="entita" value="W3SIMAP" /> 
						<input type="hidden" name="trovaAddWhere" value="" />
						<input type="hidden" name="trovaParameter" value="" /> 
						<input type="hidden" name="risultatiPerPagina" value="20" />
		 			</form>
					<form name="listaNuovo" action="${contextPath}/Lista.do" method="post">
						<input type="hidden" name="jspPath" value="/WEB-INF/pages/w3/w3simap/w3simap-lista.jsp" /> 
						<input type="hidden" name="jspPathTo" value="" /> 
						<input type="hidden" name="activePage" value="" /> 
						<input type="hidden" name="isPopUp" value="0" /> 
						<input type="hidden" name="numeroPopUp" value="" /> 
						<input type="hidden" name="metodo" value="nuovo" /> 
						<input type="hidden" name="entita" value="W3SIMAP" /> 
						<input type="hidden" name="gestisciProtezioni" value="1" />
					</form>
					<form name="apriPagina" action="${contextPath}/ApriPagina.do" method="post">
						<input type="hidden" name="href" value="w3/w3simap/w3simap-lista.jsp" />
					</form>
				</div>
			</TD>
		</TR>
		<TR>
			<TD COLSPAN="2">
			<div id="footer">
				<jsp:include page="/WEB-INF/pages/commons/footer.jsp" />
			</div>
			</TD>
		</TR>
	</TBODY>
</TABLE>

</BODY>
</HTML>