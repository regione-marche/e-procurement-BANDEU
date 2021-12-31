
<%
	/*
	 * Created on 10-Nov-2010
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

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3SIMAPEMAIL-Scheda" schema="W3">
	<c:set var="entita" value="W3SIMAPEMAIL" />
	<gene:setString name="titoloMaschera" value='${gene:callFunction("it.eldasoft.sil.w3.tags.funzioni.GetTitleW3SIMAPEMAILFunction",pageContext)}'/>

	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3SIMAPEMAIL" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPEMAIL">
			<gene:campoScheda>
				<td colspan="2"><b>Informazioni invio</b></td>
			</gene:campoScheda>
			<gene:campoScheda campo="ID" visibile="false" defaultValue='${gene:getValCampo(keyParent,"ID")}' />
			<gene:campoScheda title="N°" modificabile="false" campo="NUM" visibile="false" defaultValue='${gene:getValCampo(keyParent,"NUM")}' />
			<gene:campoScheda campo="DATA_CREAZIONE" modificabile="false" visibile="false"/>
			<gene:campoScheda campo="DATA_SPEDIZIONE_S" modificabile="false"/>
			<gene:campoScheda campo="EMAILFROM" visibile="false"/>
			<gene:campoScheda campo="EMAILPHASE" modificabile="false" obbligatorio="true" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoEMAILPHASE"/>			
			<gene:campoScheda campo="EMAILTO" modificabile="false" obbligatorio="true" />
			<gene:campoScheda campo="EMAILSUBJECT" modificabile="false"/>
			<gene:campoScheda campo="EMAILBODY" modificabile="false"/>
			<gene:campoScheda campo="ATTACHMENT_NAME" visibile="false"/>
			<gene:campoScheda title="Visualizza o scarica l'allegato">
				<c:choose>
					<c:when test="${modo eq 'VISUALIZZA' && !empty datiRiga.W3SIMAPEMAIL_ID && !empty datiRiga.W3SIMAPEMAIL_NUM && !empty datiRiga.W3SIMAPEMAIL_ATTACHMENT_NAME}">
						<a href="javascript:visualizzaAttachment('${datiRiga.W3SIMAPEMAIL_ID}','${datiRiga.W3SIMAPEMAIL_NUM}','${datiRiga.W3SIMAPEMAIL_ATTACHMENT_NAME}');" title="Visualizza o scarica l'allegato" >
							<img width="24" height="24" title="Visualizza o scarica l'allegato" alt="Visualizza o scarica l'allegato" src="${pageContext.request.contextPath}/img/8629505_5.gif"/>
						</a>
					</c:when>
					<c:otherwise>
						<img width="24" height="24" title="" alt="" src="${pageContext.request.contextPath}/img/8629505_5.gif"/>
					</c:otherwise>
				</c:choose>				
			</gene:campoScheda>
			<gene:campoScheda campo="NOTERISPOSTA" />
			
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
				<td colspan="2"><b>Informazioni sul bando/avviso</b></td>
			</gene:campoScheda>
			<gene:campoScheda entita="W3SIMAP" visibile="false" campo="ID" where="W3SIMAPEMAIL.ID = W3SIMAP.ID" />
			<gene:campoScheda entita="W3SIMAP" modificabile="false" campo="FORM" where="W3SIMAPEMAIL.ID = W3SIMAP.ID"/>
			<gene:campoScheda entita="W3SIMAP" campo="NOTICE_NUMBER_OJ" modificabile="false"/>
			<gene:campoScheda entita="W3SIMAP" campo="DATE_OJ" modificabile="false"/>
				
			<gene:campoScheda visibile="${datiRiga.W3SIMAP_FORM eq 'FS14'}">
				<td colspan="2"><b>Altre informazioni sul bando/avviso originale</b></td>
			</gene:campoScheda>
			<gene:campoScheda entita="W3FS14" visibile="false" campo="ID_RIF" where="W3SIMAPEMAIL.ID = W3FS14.ID" />
			<gene:campoScheda entita="W3FS14" visibile="${datiRiga.W3SIMAP_FORM eq 'FS14'}" modificabile="false" campo="FORM_RIF" where="W3SIMAPEMAIL.ID = W3FS14.ID" />		
			<gene:campoScheda title="${gene:if(datiRiga.W3SIMAP_FORM eq 'FS14','Tipo di appalto (originale)','Tipo di appalto')}" entita="V_W3SIMAP" where="W3SIMAPEMAIL.ID = V_W3SIMAP.ID" modificabile="false" campo="CTYPE" />
			<gene:campoScheda title="${gene:if(datiRiga.W3SIMAP_FORM eq 'FS14','Denominazione (originale)','Denominazione')}"entita="V_W3SIMAP" where="W3SIMAPEMAIL.ID = V_W3SIMAP.ID" modificabile="false" campo="TITLE_CONTRACT" />
	
	
			<gene:campoScheda>
				<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
			</gene:campoScheda>
			
			<gene:redefineInsert name="pulsanteNuovo" />
			<gene:redefineInsert name="schedaNuovo" />
			
			
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
	
		function visualizzaAttachment(id,num,attachment_name) {
			var href = "${pageContext.request.contextPath}/w3/VisualizzaAttachment.do";
			document.location.href=href+"?id=" + id + "&num=" + num + "&attachment_name=" + attachment_name;
		}
		
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
			
			formSchedaBandoAvviso.href.value=paginaCompleta;
			formSchedaBandoAvviso.key.value=chiaveCompleta;
			formSchedaBandoAvviso.entita.value=pagina;
			formSchedaBandoAvviso.submit();
		}


	</gene:javaScript>

</gene:template>