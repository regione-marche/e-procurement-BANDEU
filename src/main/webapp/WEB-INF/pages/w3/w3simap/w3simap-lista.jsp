
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
<%@ taglib uri="http://www.eldasoft.it/tags" prefix="elda"%>

<gene:template file="lista-template.jsp" gestisciProtezioni="true"
	idMaschera="V_W3SIMAP-lista" schema="W3">
	<gene:setString name="titoloMaschera" value="Lista dei bandi e degli avvisi" />
	<gene:setString name="entita" value="W3SIMAP" />
	<gene:redefineInsert name="corpo">
		<gene:set name="titoloMenu">
			<jsp:include page="/WEB-INF/pages/commons/iconeCheckUncheck.jsp" />
		</gene:set>
		<table class="lista">
			<tr>
				<td><gene:formLista entita="W3SIMAP" where="W3SIMAP.FORM NOT IN ('FS14')" pagesize="20"
					tableclass="datilista" gestisciProtezioni="true" sortColumn="-8"
					gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAP">
					
					<gene:campoLista title="Opzioni <br><center>${titoloMenu}</center>"	width="50">
						<c:if test="${currentRow >= 0}">
							<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, "W3SIMAP", "ID", datiRiga.W3SIMAP_ID)}' />
							<gene:PopUp variableJs="rigaPopUpMenu${currentRow}"	onClick="chiaveRiga='${chiaveRigaJava}'">
								<gene:PopUpItemResource	resource="popupmenu.tags.lista.visualizza" title="Visualizza"
									href="javascript:apriBandoAvviso('${datiRiga.W3SIMAP_FORM}','${datiRiga.W3SIMAP_ID}','${datiRiga.W3SIMAP_FSVERSION}','VISUALIZZA');" />
								<c:if test='${gene:checkProtFunz(pageContext, "MOD","MOD") and autorizzatoModifica eq "true" && (datiRiga.W3SIMAP_FSVERSION eq "2" || datiRiga.W3SIMAP_FSVERSION eq "3" || datiRiga.W3SIMAP_FSVERSION eq "4" || datiRiga.W3SIMAP_FSVERSION eq "5" )}'>
									<gene:PopUpItemResource	resource="popupmenu.tags.lista.modifica" title="Modifica" 
										href="javascript:apriBandoAvviso('${datiRiga.W3SIMAP_FORM}','${datiRiga.W3SIMAP_ID}','${datiRiga.W3SIMAP_FSVERSION}','MODIFICA');" />
								</c:if>
								<c:if test='${gene:checkProtFunz(pageContext, "DEL","DEL") and autorizzatoModifica eq "true"}'>
									<gene:PopUpItemResource resource="popupmenu.tags.lista.elimina"	title="Elimina" />
								</c:if>
								<c:if test='${autorizzatoEsecuzione eq "true" && gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.gestionePermessi")}'> 
									<gene:PopUpItem title="Gestione permessi" href="javascript:apriGestionePermessi('${datiRiga.W3SIMAP_ID}')"/>
								</c:if>
							</gene:PopUp>
							<c:if test='${gene:checkProtFunz(pageContext,"DEL","LISTADELSEL") and autorizzatoModifica eq "true"}'>
								<input type="checkbox" name="keys" value="${chiaveRiga}" />
							</c:if>
						</c:if>
					</gene:campoLista>
					<gene:campoLista campo="ID" visibile="false" />	
					<gene:campoLista campo="FORM" width="160" href="javascript:apriBandoAvviso('${datiRiga.W3SIMAP_FORM}','${datiRiga.W3SIMAP_ID}','${datiRiga.W3SIMAP_FSVERSION}','VISUALIZZA');" />
					
					<gene:campoLista title="Amministrazione aggiudicatrice" entita="UFFINT" campo="NOMEIN" 
						from="W3AMMI, W3AMMI" where="W3AMMI.CODAMM=W3SIMAP.CODAMM AND UFFINT.CODEIN=W3AMMI.CODEIN" />
					
					<gene:campoLista title="Denominazione dell'appalto" entita="V_W3SIMAP" campo="TITLE_CONTRACT" ordinabile="false" 
						where="W3SIMAP.ID=V_W3SIMAP.ID" href="javascript:apriBandoAvviso('${datiRiga.W3SIMAP_FORM}','${datiRiga.W3SIMAP_ID}','${datiRiga.W3SIMAP_FSVERSION}','VISUALIZZA');"/>
						
					<gene:campoLista title="Tipo di appalto" campo="CTYPE" width="70"  />
						
					<gene:campoLista title="Numero avviso GU/S" campo="NOTICE_NUMBER_OJ" />
					<gene:campoLista campo="DATE_OJ" />
					<gene:campoLista entita="V_W3SIMAP" width="70" campo="NOTICE_DATE" where="W3SIMAP.ID=V_W3SIMAP.ID" ordinabile="false"/>
					<gene:campoLista title="Versione formulario" campo="FSVERSION" />
					<gene:campoLista title="Identificativo gara SIMOG" entita="V_W3SIMAP" width="90" campo="IDGARA" where="W3SIMAP.ID=V_W3SIMAP.ID" ordinabile="false"/>
				</gene:formLista></td>
			</tr>
			
			<tr><jsp:include page="/WEB-INF/pages/commons/pulsantiLista.jsp" /></tr>
		</table>
		
		<form name="formSchedaBandoAvviso" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
			<input type="hidden" name="href" value="" /> 
			<input type="hidden" name="entita" value="W3SIMAP" />
			<input type="hidden" name="entitadettaglio" value="" />
			<input type="hidden" name="key" value="" />
			<input type="hidden" name="metodo" value="apri" />
			<input type="hidden" name="activePage" value="0" />
			<input type="hidden" name="modo" value="" />
		</form>
		
		<form name="formVisualizzaPermessiUtentiGruppi" action="${pageContext.request.contextPath}/w3/VisualizzaPermessiUtentiGruppi.do" method="post">
			<input type="hidden" name="tblname" id="tblname" value="" />
			<input type="hidden" name="clm1name" id="clm1name" value="" />
			<input type="hidden" name="clm1value" id="clm1value" value="" />
		</form> 
		
		<gene:javaScript>

			var columns = $("[id^='colV_W3SIMAP_TITLE_CONTRACT_'] a span");
			columns.each(function(i) {
				var _text = $(this).text();
				if (_text.length > 200) {
					$(this).text(_text.substring(0,200) + " ...");
				}
				$(this).attr("title",_text);
			});
					
			function apriBandoAvviso(entita,id,fsversion,modo) {
				var pag = "";
				var pk = "";

				if (fsversion == "1") {
					pag = "w3/w3simap/w3simap-scheda-208s02.jsp";
					pk = "W3SIMAP.ID=N:" + id;				
				} else {
					if (entita == "FS1") {
						pag = "w3/w3fs1/w3fs1-scheda.jsp";
						pk = "W3FS1.ID=N:" + id;
					}
				
					if (entita == "FS2") {
						pag = "w3/w3fs2/w3fs2-scheda.jsp";
						pk = "W3FS2.ID=N:" + id;
					}
					
					if (entita == "FS3") {
						pag = "w3/w3fs3/w3fs3-scheda.jsp";
						pk = "W3FS3.ID=N:" + id;
					}

					if (entita == "FS4") {
						pag = "w3/w3fs4/w3fs4-scheda.jsp";
						pk = "W3FS4.ID=N:" + id;
					}

					if (entita == "FS5") {
						pag = "w3/w3fs5/w3fs5-scheda.jsp";
						pk = "W3FS5.ID=N:" + id;
					}
					
					if (entita == "FS6") {
						pag = "w3/w3fs6/w3fs6-scheda.jsp";
						pk = "W3FS6.ID=N:" + id;
					}

					if (entita == "FS7") {
						pag = "w3/w3fs7/w3fs7-scheda.jsp";
						pk = "W3FS7.ID=N:" + id;
					}
					
					if (entita == "FS8") {
						pag = "w3/w3fs8/w3fs8-scheda.jsp";
						pk = "W3FS8.ID=N:" + id;
					}
					
					if (entita == "FS14") {
						pag = "w3/w3fs14/w3fs14-scheda.jsp";
						pk = "W3FS14.ID=N:" + id;
					}
					
					if (entita == "FS20") {
						pag = "w3/w3fs20/w3fs20-scheda.jsp";
						pk = "W3FS20.ID=N:" + id;
					}
					
				}
				
				formSchedaBandoAvviso.href.value=pag;
				formSchedaBandoAvviso.key.value=pk;
				formSchedaBandoAvviso.entitadettaglio.value=entita;
				formSchedaBandoAvviso.modo.value=modo;
				formSchedaBandoAvviso.submit();
			}
			
			function apriGestionePermessi(id) {
				bloccaRichiesteServer();
				formVisualizzaPermessiUtentiGruppi.tblname.value = "W3SIMAP";
				formVisualizzaPermessiUtentiGruppi.clm1name.value = "ID";
				formVisualizzaPermessiUtentiGruppi.clm1value.value = id;
				formVisualizzaPermessiUtentiGruppi.submit();
			}
			

		</gene:javaScript>
	</gene:redefineInsert>
</gene:template>