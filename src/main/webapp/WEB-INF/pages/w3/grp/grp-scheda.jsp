<%/*
       * Created on 4-Giu-2013
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

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="GRP-scheda" schema="W3">

	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${contextPath}/css/jquery/dataTable/dataTable/jquery.dataTables.css" >
		<script type="text/javascript" src="${contextPath}/js/jquery.cookie.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery.gruppi.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery.dataTables.js?v=${sessionScope.versioneModuloAttivo}"></script>
		
		<style type="text/css">
		
			TABLE.scheda {
				margin-top: 5px;
				margin-bottom: 5px;
				padding: 0px;
				font-size: 11px;
				border-collapse: collapse;
				border-left: 1px solid #A0AABA;
				border-top: 1px solid #A0AABA;
				border-right: 1px solid #A0AABA;
			}
	
			TABLE.scheda TR.intestazione {
				background-color: #EFEFEF;
				border-bottom: 1px solid #A0AABA;
			}
			
			TABLE.scheda TR.intestazione TD, TABLE.scheda TR.intestazione TH {
				padding: 5 2 5 2;
				text-align: center;
				font-weight: bold;
				border-left: 1px solid #A0AABA;
				border-right: 1px solid #A0AABA;
				border-top: 1px solid #A0AABA;
				border-bottom: 1px solid #A0AABA;
				height: 22px;
			}
		
			TABLE.scheda TR.sezione {
				background-color: #EFEFEF;
				border-bottom: 1px solid #A0AABA;
			}
			
			TABLE.scheda TR.sezione TD, TABLE.scheda TR.sezione TH {
				padding: 5 2 5 2;
				text-align: left;
				font-weight: bold;
				height: 22px;
			}
		
			TABLE.scheda TR {
				background-color: #FFFFFF;
			}
	
			TABLE.scheda TR TD {
				padding-left: 3px;
				padding-top: 1px;
				padding-bottom: 1px;
				padding-right: 3px;
				text-align: left;
				border-left: 1px solid #A0AABA;
				border-right: 1px solid #A0AABA;
				border-top: 1px solid #A0AABA;
				border-bottom: 1px solid #A0AABA;
				height: 22px;
				font: 11px Verdana, Arial, Helvetica, sans-serif;
			}
			
			TABLE.scheda TR.intestazione TH.ck, TABLE.scheda TR TD.ck {
				width: 22px;
				text-align: center;
			}
			
			img.img_titolo {
				padding-left: 8px;
				padding-right: 8px;
				width: 24px;
				height: 24px;
				vertical-align: middle;
			}
			
			.dataTables_length, .dataTables_filter {
				padding-bottom: 5px;
			}
				
		</style>
		
	</gene:redefineInsert>

	<c:set var="entita" value="GRP"/>
	<gene:setString name="titoloMaschera" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.GetTitleFunction",pageContext,entita)}'/>
	
	<gene:redefineInsert name="corpo">
		<gene:formPagine gestisciProtezioni="true">
			<gene:pagina title="Gruppo" idProtezioni="GRP">
				<gene:formScheda entita="GRP" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreGRP" >

					<gene:campoScheda>
						<td colspan="2">
							<b><br>Gruppo</b>
						</td>
					</gene:campoScheda>
					<gene:campoScheda campo="IDGRP" visibile="false" />
					<gene:campoScheda title="Descrizione" campo="DESCGRP" obbligatorio="true" />
					<gene:campoScheda campo="NOTE" />
					<gene:campoScheda title="Operazione" campo="OPERATION" campoFittizio="true" value="${modo}" definizione="T30;0" visibile="false" />
					<gene:campoScheda title="Lista SYSCON" campo="LISTASYSCON" campoFittizio="true" definizione="T2000;0" visibile="false" />
					
					<gene:campoScheda>
						<td colspan="2">
							<b><br><br>Utenti appartenenti al gruppo</b>
						</td>
					</gene:campoScheda>
					
					<gene:campoScheda addTr="false">
						<tr>
							<td colspan="2">
								<br>
								<div id="utentiContainer"></div>
								<br>
							</td>
						</tr>
					</gene:campoScheda>
					
					<gene:campoScheda>
						<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
					</gene:campoScheda>
				</gene:formScheda>
			</gene:pagina>
		</gene:formPagine>
	</gene:redefineInsert>
	
</gene:template>


