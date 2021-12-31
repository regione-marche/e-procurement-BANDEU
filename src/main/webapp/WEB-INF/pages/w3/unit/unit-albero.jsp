<%
/*
 * Created on: 02/09/2014
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" schema="W3" idMaschera="UNIT-albero">
	
	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${contextPath}/js/jquery.cookie.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery.jstree.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery.jstree.unitgrp.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery.highlight.js?v=${sessionScope.versioneModuloAttivo}"></script>
		
		<style type="text/css">
			.highlight {
			    background-color: #FFDB05;
			    -moz-box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4); /* FF3.5+ */
			    -webkit-box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4); /* Saf3.0+, Chrome */
			    box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4); /* Opera 10.5+, IE 9.0 */
			}
			
			.ui-dialog-titlebar {
				display: none;
			}
			
			.ui-widget-overlay { 
				background: rgb(128, 128, 128); opacity: .20; filter:Alpha(Opacity=20);
			}
			
			.ui-corner-all, .ui-corner-top, .ui-corner-left, .ui-corner-tl { -moz-border-radius-topleft: 0px; -webkit-border-top-left-radius: 0px; -khtml-border-top-left-radius: 0px; border-top-left-radius: 0px; }
			.ui-corner-all, .ui-corner-top, .ui-corner-right, .ui-corner-tr { -moz-border-radius-topright: 0px; -webkit-border-top-right-radius: 0px; -khtml-border-top-right-radius: 0px; border-top-right-radius: 0px; }
			.ui-corner-all, .ui-corner-bottom, .ui-corner-left, .ui-corner-bl { -moz-border-radius-bottomleft: 0px; -webkit-border-bottom-left-radius: 0px; -khtml-border-bottom-left-radius: 0px; border-bottom-left-radius: 0px; }
			.ui-corner-all, .ui-corner-bottom, .ui-corner-right, .ui-corner-br { -moz-border-radius-bottomright: 0px; -webkit-border-bottom-right-radius: 0px; -khtml-border-bottom-right-radius: 0px; border-bottom-right-radius: 0px; }
						
		</style>
	</gene:redefineInsert>
	
	<gene:setString name="titoloMaschera" value="Unit&agrave; organizzative e gruppi"/>
	<gene:redefineInsert name="azioniContesto"></gene:redefineInsert>
	<gene:redefineInsert name="corpo">
	
		<table class="dettaglio-notab">
			<tr>
				<td style="text-align: right; width: 70px; padding-right: 10px;">
					Legenda:
				</td>
				<td>
					<img style="padding-left: 4px; padding-right: 5px;" title="Unit&agrave; organizzativa" alt="Unit&agrave; organizzative" src="img/Objects-14.gif">Unit&agrave; organizzativa
					<img style="padding-left: 10px; padding-right: 5px;" title="Gruppo di utenti" alt="Gruppo di utenti" src="img/Users-23.gif">Gruppo di utenti
					<img style="padding-left: 10px; padding-right: 5px;" title="Utente" alt="Utente" src="img/Users-5.gif">Utente
				</td>
			</tr>
			<tr>
				<td style="text-align: right; width: 70px; padding-right: 10px;">
					Ricerca:
				</td>
				<td>
					<input class="testo" style="vertical-align: middle;" type="text" size="40" id="textsearch" title="Ricerca unit&agrave; organizzative, gruppi e utenti"/>
					<span class="link-generico" id="deletesearch"><img title="Elimina ricerca" alt="Elimina ricerca" src="img/cancellaFiltro.gif"></span>
					&nbsp;
					<span style="vertical-align: middle;" id="messaggioricerca"></span>
				</td>
			</tr>
			<tr>
				<td style="border-bottom: 0px;" colspan="2" class="valore-dato-trova">
					<img alt="Unit&agrave; organizzative e gruppi" src="img/open_folder.gif">
					<span style="vertical-align: middle;">
						<span style="display: none;" id="attesa" >
							<img title="Attesa" alt="Attesa" src="${contextPath}/css/jquery/jstree/themes/classic/throbber.gif">
						</span>
						Unit&agrave; organizzative e gruppi
					</span>
					<div id="unitgrptree" style="width:780px; min-height: 250px; padding-left: 0px; margin-left: 0px;"></div>
				</td>
			</tr>
		</table>
	
		<form name="formUnit" action="${contextPath}/ApriPagina.do" method="post">
			<input type="hidden" name="href" value="w3/unit/unit-scheda.jsp" />
			<input type="hidden" name="metodo" value="apri" />
			<input type="hidden" name="modo" value="">
			<input type="hidden" name="entita" value="UNIT" />
			<input type="hidden" name="key" value="" />
			<input type="hidden" name="keyparent" value="" />
			<input type="hidden" name="activePage" value="0" />
		</form>	

		<form name="formGrp" action="${contextPath}/ApriPagina.do" method="post">
			<input type="hidden" name="href" value="w3/grp/grp-scheda.jsp" />
			<input type="hidden" name="metodo" value="apri" />
			<input type="hidden" name="modo" value="">
			<input type="hidden" name="entita" value="GRP" />
			<input type="hidden" name="key" value="" />
			<input type="hidden" name="keyparent" value="" />
			<input type="hidden" name="activePage" value="0" />
		</form>	
		
		<div id="dialog-eliminaUNIT" title="Elimina unit&agrave; organizzativa" style="display: none;">
			<br>
			<br>
			Eliminare l'unit&agrave; organizzativa selezionata ?
		</div>
		
		<div id="dialog-eliminaGRP" title="Elimina gruppo" style="display: none;">
			<br>
			<br>
			Eliminare il gruppo selezionato ?
		</div>
	
	</gene:redefineInsert>
</gene:template>

