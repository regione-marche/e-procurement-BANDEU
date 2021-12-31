<%
	 /*
      *
      * Copyright (c) EldaSoft S.p.A.
      * Tutti i diritti sono riservati.
      *
      * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
      * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
      * aver prima formalizzato un accordo specifico con EldaSoft.
      */

%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda" %>


<gene:template file="popup-template.jsp">

	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.highlight.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvp.js?v=${sessionScope.versioneModuloAttivo}"></script>
		
		<style type="text/css">
			.jstree-classic a { 
				white-space:normal !important; height: auto !important;
			}
			
			.jstree-anchor {
				height: auto !important;
			}
			
			.jstree-classic li > ins { 
				vertical-align:top; 
			}
			
			.jstree-leaf {
				height: auto;
			}
			
			.jstree-leaf a {
				height: auto !important;
			}
					
		</style>
	</gene:redefineInsert>

	<input type="hidden" size="40" id="modo" value="${param.modo}"/>
	<input type="hidden" size="40" id="campoopener" value="${param.campo}"/>

	<gene:setString name="titoloMaschera" value="Dettaglio codice CPV (Vocabolario principale)"/>
	<gene:redefineInsert name="corpo">
		<table class="dettaglio-notab" >
			<tr>
				<td style="width: 50px; padding-right: 10px; padding-top: 5px; padding-bottom: 5px;">
					Ricerca:
				</td>
				<td style="padding-top: 10px; padding-bottom: 5px;">
					<input class="testo" style="vertical-align: middle;" type="text" size="40" id="textsearch" value="${param.valore}" title="Ricerca CPV del vocabolario principale"/>
					<span class="link-generico" id="deletesearch"><img title="Elimina ricerca" alt="Elimina ricerca" src="img/cancellaFiltro.gif"></span>
				</td>
			</tr>
			<tr>
				<td style="border-bottom: 0px; padding-right: 10px; padding-top: 5px; padding-bottom: 5px;" colspan="2">
					<img alt="CPV del vocabolario principale" src="img/open_folder.gif">
					<span style="vertical-align: middle;">
						CPV del vocabolario principale
					</span>
					<div id="cpvvptree" style="min-height: 350px;"></div>
				</td>
			</tr>
		</table>
  </gene:redefineInsert>
</gene:template>

