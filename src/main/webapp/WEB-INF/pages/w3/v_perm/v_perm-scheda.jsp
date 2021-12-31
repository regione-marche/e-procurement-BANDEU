
<%
	/*
	 * Created on 20-Ott-2008
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

<c:set var="isNavigazioneDisattiva" value="${isNavigazioneDisabilitata}" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<gene:template file="scheda-template.jsp">

	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${contextPath}/css/jquery/dataTable/dataTable/jquery.dataTables.css" >
		<script type="text/javascript" src="${contextPath}/js/jquery.cookie.js?v=${sessionScope.versioneModuloAttivo}"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery.permessi.js?v=${sessionScope.versioneModuloAttivo}"></script>
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
				height: 30px;
			}
		
			TABLE.scheda TR.sezione {
				background-color: #EFEFEF;
				border-bottom: 1px solid #A0AABA;
			}
			
			TABLE.scheda TR.sezione TD, TABLE.scheda TR.sezione TH {
				padding: 5 2 5 2;
				text-align: left;
				font-weight: bold;
				height: 25px;
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
				height: 25px;
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
				
			div.tooltip {
				width: 300px;
				margin-top: 3px;
				margin-bottom:3px;
				border: 1px solid #A0AABA;
				padding: 10px;
				display: none;
				position: absolute;
				z-index: 1000;
				background-color: #F4F4F4;
			}

				
		</style>
		
	</gene:redefineInsert>

	<gene:setString name="titoloMaschera" value="Permessi" />
	
	<gene:redefineInsert name="corpo">

		<form id="formVisualizzaPermessiUtentiGruppi" name="formVisualizzaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/VisualizzaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="${tblname}" />
			<input type="hidden" name="clm1name" id="clm1name" value="${clm1name}" />
			<input type="hidden" name="clm1value" id="clm1value" value="${clm1value}" />
			<input type="hidden" name="operation" id="operation" value="${operation}" />
		</form> 
		
		<form id="formModificaPermessiUtentiGruppi" name="formModificaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/ModificaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="${tblname}" />
			<input type="hidden" name="clm1name" id="clm1name" value="${clm1name}" />
			<input type="hidden" name="clm1value" id="clm1value" value="${clm1value}" />
			<input type="hidden" name="operation" id="operation" value="${operation}" />
		</form>		

		<table class="dettaglio-notab">
			<tr>
				<td>
					<p style="margin-top: 12px;">
						<img class="img_titolo" title="Gruppi di utenti" alt="Gruppi di utenti" src="${contextPath}/img/Users-23.png"><b>Gruppi di utenti</b>
					</p>
					<div id="gruppiContainer" style="margin-left:8px; width: 98%"></div>
					<br>
				</td>
			</tr>
			<tr>
				<td>
					<p style="margin-top: 12px;">
						<img class="img_titolo" title="Utenti" alt="Utenti" src="${contextPath}/img/Users-5.png"><b>Utenti</b>
					</p>
					<div id="utentiContainer" style="margin-left:8px; width: 98%"></div>
				    <br>
				</td>
			</tr>
			<tr>	
				<td class="comandi-dettaglio">
					<c:choose>
						<c:when test='${isNavigazioneDisattiva ne "1"}'>
							<INPUT type="button" id="pulsantemodificapermessi" class="bottone-azione" value='Modifica' title='Modifica'>
						</c:when>
						<c:otherwise>
							<INPUT type="button" id="pulsantesalvamodifichepermessi" class="bottone-azione" value="Salva" title="Salva">
							<INPUT type="button" id="pulsanteannullamodifichepermessi" class="bottone-azione" value="Annulla" title="Annulla">
						</c:otherwise>
					</c:choose>
					&nbsp;
				</td>
			</tr>
		</table>
	</gene:redefineInsert>

	<gene:redefineInsert name="addToAzioni" >
		<tr>
			<c:choose>
		        <c:when test='${isNavigazioneDisattiva ne "1"}'>
		        	<tr>
			        	<td class="vocemenulaterale">
							<a href="#" id="menumodificapermessi" title="Modifica" tabindex="1512">Modifica</a>
					  	</td>
					  </tr>
		        </c:when>
			    <c:otherwise>
			    	<tr>
				       	<td class="vocemenulaterale">
							<a href="#" id="menusalvamodifichepermessi" title="Salva" tabindex="1512">Salva</a>
					  	</td>
					</tr>
			       	<tr>
				       	<td class="vocemenulaterale">
							<a href="#" id="menuannullamodifichepermessi" title="Annulla" tabindex="1512">Annulla</a>
					  	</td>
					 </tr>		  	
			    </c:otherwise>
			</c:choose>
		</tr>
	</gene:redefineInsert>

	<gene:redefineInsert name="noteAvvisi"></gene:redefineInsert>

</gene:template>

