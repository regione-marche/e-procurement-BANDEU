
<%
	/*
	 * Created on 28-Apr-2014
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
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3SIMAPWS-Scheda" schema="W3">

	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>


	<c:set var="entita" value="W3SIMAPWS" />
	<gene:setString name="titoloMaschera" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.GetTitleW3SIMAPWSFunction",pageContext)}'/>		
	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3SIMAPWS" gestisciProtezioni="true">
		
			<gene:campoScheda>
				<td colspan="2"><b><br>Dati dell'invio</b></td>
			</gene:campoScheda>
		
			<gene:campoScheda campo="ID" visibile="false" defaultValue='${gene:getValCampo(keyParent,"ID")}' />
			<gene:campoScheda title="N°" visibile="false" modificabile="false" campo="NUM" defaultValue='${gene:getValCampo(keyParent,"NUM")}' />
			<gene:campoScheda campo="PHASE" modificabile="false"/>
			<gene:campoScheda campo="SUBMISSION_DATE" modificabile="false"/>
			<gene:campoScheda campo="SUBMISSION_ID" modificabile="false"/>
			<gene:campoScheda title="Stato" campo="STATUS_CODE" modificabile="false" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSTATUSCODE"/>
			<gene:campoScheda title="Descrizione" campo="STATUS_DESCRIPTION" modificabile="false" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoSTATUSDESCRIPTION"/>
			<gene:campoScheda title="Stato aggiornato in data" campo="REPORT_DATE" modificabile="false"/>

			<gene:campoScheda title="Risposta integrale da SIMAP" campo="NOTICEINFO" modificabile="false" visibile="false" />
			<gene:campoScheda>
				<td colspan="2">
					<b><br>Controlli tecnici e regole di validazione (segnalazioni provenienti dai servizi SIMAP)</b>
					<table id="controllitecnici" class="grigliaw3">
						<tr class="intestazione">
							<td width="50px" >Codice</td>
							<td>Messaggio</td>
							<td>Dettaglio</td>
							<td width="80px">Severit&agrave;</td>
							<td width="50px">Esito</td>
						</tr>
						<c:if test="${!empty datiRiga.W3SIMAPWS_NOTICEINFO}">
							<x:parse var="xnoticeinfo" xml="${datiRiga.W3SIMAPWS_NOTICEINFO}" />
							<x:forEach var="tech" select="$xnoticeinfo//technicalValidationReport/items" varStatus="techstatus">
								<x:set var="xtech" select="$tech" scope="request"/>
								<tr>
									<td style="text-align: center;"><x:out select="$xtech/name"/></td>
									<td><x:out select="$xtech/message"/></td>
									<td><x:out select="$xtech/details"/></td>
									<td style="text-align: center;"><x:out select="$xtech/severity"/></td>
									<td style="text-align: center;"><x:out select="$xtech/valid"/></td>
								</tr>
							</x:forEach>
							<x:forEach var="validation" select="$xnoticeinfo//validationRulesReport/items" varStatus="techstatus">
								<x:set var="xvalidation" select="$validation" scope="request"/>
								<tr>
									<td style="text-align: center;"><x:out select="$xvalidation/name"/></td>
									<td><x:out select="$xvalidation/message"/></td>
									<td><x:out select="$xvalidation/details"/></td>
									<td style="text-align: center;"><x:out select="$xvalidation/severity"/></td>
									<td style="text-align: center;"><x:out select="$xvalidation/valid"/></td>
								</tr>
							</x:forEach>
						</c:if>
					</table>
				</td>
			</gene:campoScheda>

			<gene:campoScheda addTr="false">
				<span id="linkApriBandoAvviso" style="display: none; float: right; vertical-align: top;">
					<a href="javascript:apriBandoAvviso('${datiRiga.W3SIMAP_FORM}','${datiRiga.W3SIMAP_ID}','VISUALIZZA');"  title="Apri bando/avviso">Apri bando/avviso</a>
				</span>
			</gene:campoScheda>
			<gene:campoScheda addTr="false">
				<span id="linkApriBandoAvvisoOriginale" style="display: none; float: right; vertical-align: top;">
					<a href="javascript:apriBandoAvviso('${datiRiga.W3FS14_FORM_RIF}','${datiRiga.W3FS14_ID_RIF}','VISUALIZZA');"  title="Apri bando/avviso originale">Apri bando/avviso originale</a>
				</span>
			</gene:campoScheda>
			
			<gene:campoScheda>
				<td colspan="2"><b><br>Informazioni sul bando/avviso</b></td>
			</gene:campoScheda>
			<gene:campoScheda entita="W3SIMAP" visibile="false" campo="ID" where="W3SIMAPWS.ID = W3SIMAP.ID" modificabile="false"/>
			<gene:campoScheda entita="W3SIMAP" modificabile="false" campo="FORM" where="W3SIMAPWS.ID = W3SIMAP.ID" />
			<gene:campoScheda entita="W3SIMAP" campo="NOTICE_NUMBER_OJ" modificabile="false" />
			<gene:campoScheda entita="W3SIMAP" campo="DATE_OJ" modificabile="false" />
				
			<gene:campoScheda visibile="${datiRiga.W3SIMAP_FORM eq 'FS14'}">
				<td colspan="2"><b>Altre informazioni sul bando/avviso originale</b></td>
			</gene:campoScheda>
			<gene:campoScheda entita="W3FS14" visibile="false" campo="ID_RIF" where="W3SIMAPWS.ID = W3FS14.ID" modificabile="false"/>
			<gene:campoScheda entita="W3FS14" visibile="${datiRiga.W3SIMAP_FORM eq 'FS14'}" modificabile="false" campo="FORM_RIF" where="W3SIMAPWS.ID = W3FS14.ID" />		
			<gene:campoScheda title="${gene:if(datiRiga.W3SIMAP_FORM eq 'FS14','Tipo di appalto (originale)','Tipo di appalto')}" entita="V_W3SIMAP" where="W3SIMAPWS.ID = V_W3SIMAP.ID" modificabile="false" campo="CTYPE" />
			<gene:campoScheda title="${gene:if(datiRiga.W3SIMAP_FORM eq 'FS14','Denominazione (originale)','Denominazione')}" entita="V_W3SIMAP" where="W3SIMAPWS.ID = V_W3SIMAP.ID" modificabile="false" campo="TITLE_CONTRACT" />
	
	

			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
			</gene:campoScheda>
			<gene:redefineInsert name="pulsanteNuovo" />
			<gene:redefineInsert name="schedaNuovo" />
			<gene:redefineInsert name="pulsanteModifica" />
			<gene:redefineInsert name="schedaModifica" />
		</gene:formScheda>
		
		<form name="formSchedaBandoAvviso" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
			<input type="hidden" name="href" value="" /> 
			<input type="hidden" name="entita" value="" />
			<input type="hidden" name="key" value="" />
			<input type="hidden" name="metodo" value="apri" />
			<input type="hidden" name="activePage" value="0" />
		</form>
		
	</gene:redefineInsert>
	
	<gene:javaScript>

		setW3SIMAPWSStato();

		function setW3SIMAPWSStato() {
			$.ajax({
				type: "POST",
				dataType: "json",
				async: true,
				beforeSend: function(x) {
					if(x && x.overrideMimeType) {
						x.overrideMimeType("application/json;charset=UTF-8");
					}
				},                                       
				url: "${pageContext.request.contextPath}/w3/SetW3SIMAPWSStato.do",
				data: {
					stato: "1",
					id : ${datiRiga.W3SIMAPWS_ID},
					num : ${datiRiga.W3SIMAPWS_NUM}
				}
			});
		}


		<c:if test='${modo eq "VISUALIZZA" && datiRiga.FORM ne "FS14"}'>
			<c:choose>
				<c:when test="${datiRiga.W3SIMAP_FORM ne 'FS14'}">
					$("#linkApriBandoAvviso").appendTo($("#W3SIMAP_FORMview"));
					$("#linkApriBandoAvviso").show();									
				</c:when>
				<c:otherwise>
					$("#linkApriBandoAvvisoOriginale").appendTo($("#W3FS14_FORM_RIFview"));
					$("#linkApriBandoAvvisoOriginale").show();									
				</c:otherwise>
			</c:choose>
		</c:if>
		
		$("#controllitecnici").parent().css("border-bottom","0px");
		
		function apriBandoAvviso(pagina,chiave,modo) {
			var paginaCompleta = "";

			if (pagina == "FS1") {
				paginaCompleta = "w3/w3fs1/w3fs1-scheda.jsp";
				chiaveCompleta = "W3FS1.ID=N:" + chiave;
			}
		
			if (pagina == "FS2") {
				paginaCompleta = "w3/w3fs2/w3fs2-scheda.jsp";
				chiaveCompleta = "W3FS2.ID=N:" + chiave;
			}
			
			if (pagina == "FS3") {
				paginaCompleta = "w3/w3fs3/w3fs3-scheda.jsp";
				chiaveCompleta = "W3FS3.ID=N:" + chiave;
			}
			
			if (pagina == "FS4") {
				paginaCompleta = "w3/w3fs4/w3fs4-scheda.jsp";
				chiaveCompleta = "W3FS4.ID=N:" + chiave;
			}

			if (pagina == "FS5") {
				paginaCompleta = "w3/w3fs5/w3fs5-scheda.jsp";
				chiaveCompleta = "W3FS5.ID=N:" + chiave;
			}

			if (pagina == "FS6") {
				paginaCompleta = "w3/w3fs6/w3fs6-scheda.jsp";
				chiaveCompleta = "W3FS6.ID=N:" + chiave;
			}
			
			if (pagina == "FS7") {
				paginaCompleta = "w3/w3fs7/w3fs7-scheda.jsp";
				chiaveCompleta = "W3FS7.ID=N:" + chiave;
			}			
			
			if (pagina == "FS8") {
				paginaCompleta = "w3/w3fs8/w3fs8-scheda.jsp";
				chiaveCompleta = "W3FS8.ID=N:" + chiave;
			}
			
			if (pagina == "FS9") {
				paginaCompleta = "w3/w3fs9/w3fs9-scheda.jsp";
				chiaveCompleta = "W3FS9.ID=N:" + chiave;
			}
			
			if (pagina == "FS14") {
				paginaCompleta = "w3/w3fs14/w3fs14-scheda.jsp";
				chiaveCompleta = "W3FS14.ID=N:" + chiave;
			}

			if (pagina == "FS20") {
				paginaCompleta = "w3/w3fs20/w3fs20-scheda.jsp";
				chiaveCompleta = "W3FS20.ID=N:" + chiave;
			}
			
			formSchedaBandoAvviso.href.value=paginaCompleta;
			formSchedaBandoAvviso.key.value=chiaveCompleta;
			formSchedaBandoAvviso.entita.value=pagina;
			formSchedaBandoAvviso.submit();
		}
		
	</gene:javaScript>

</gene:template>