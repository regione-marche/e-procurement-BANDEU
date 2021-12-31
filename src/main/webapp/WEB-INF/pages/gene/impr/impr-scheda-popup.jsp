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
/* Scheda a popup dei dati generali dell'impresa */
%>

<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<gene:template file="popup-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="ImprScheda">

	<gene:redefineInsert name="head" >
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.cpvvsupp.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.nuts.mod.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery/cpvvp/jquery.cpvvp.mod.css" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>

	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.gene.tags.functions.GetTitleFunction",pageContext,"IMPR")}' />
	<gene:redefineInsert name="pulsanteNuovo" />
	<gene:redefineInsert name="corpo">
		<gene:formPagine gestisciProtezioni="true" >
			<gene:pagina title="Dati generali" idProtezioni="DATIGEN">
				<jsp:include page="impr-interno-scheda.jsp" />
			</gene:pagina>
		</gene:formPagine>
  </gene:redefineInsert>
</gene:template>
