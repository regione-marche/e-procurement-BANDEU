<%/*
       * Created on 28-Mar-2008
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

<gene:template file="scheda-template.jsp" gestisciProtezioni="true" idMaschera="W3SIMAP-Scheda" schema="W3">

	<gene:redefineInsert name="head" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/w3.css" >
	</gene:redefineInsert>

	<c:set var="entita" value="W3SIMAP" />
	<gene:setString name="titoloMaschera" value="Nuovo bando o avviso" />
	<gene:redefineInsert name="corpo">
		<gene:formScheda entita="W3SIMAP" gestisciProtezioni="true" gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAP">
		
			<gene:campoScheda addTr="false">
				<tr>
					<td style="border-bottom: 0px;">
						<br>
						<b>Bandi e avvisi</b>
						<br>
						<br>
						<span width="400px;">
							In questa pagina sono elencati i bandi e gli avvisi gestiti dall'applicativo ed inviabili al <b>Supplemento della Gazzetta Ufficiale dell'Unione Europea - Serie S</b>.
						</span>
						<br>
						<br>
					
						<table class="grigliaw3" style="width: 90%; margin-left: 20px;">
							<tr class="intestazione">
								<td class="center">Settore</td>
								<td class="center">Direttiva</td>
								<td class="center">Sigla</td>								
								<td>Descrizione</td>
								<td>Crea nuovo<br>bando o avviso</td>
							</tr>
							<tr>
								<td rowspan="6" class="center">Ordinario</td>
								<td rowspan="6" class="center">2014/24/EU</td>
								<td class="center">F01</td>								
								<td>Avviso di preinformazione</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS1');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center">F02</td>
								<td>Bando di gara</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS2');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center">F03</td>
								<td>Avviso di aggiudicazione di appalto</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS3');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center">F08</td>
								<td>Avviso relativo al profilo di committente</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS8-24');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center"><i>F14</i></td>
								<td><i>Avviso di rettifica (**)</i></td>
								<td class="center">
									<i>Disponibile all'interno dei formulari<br>F01, F02, F03, F08 e F20</i>
								</td>
							</tr>
							<tr>
								<td class="center">F20</td>
								<td>Avviso di modifica di appalto durante il periodo di validit&agrave;</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS20-24');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td rowspan="7" class="center">Speciale</td>
								<td rowspan="7" class="center">2014/25/EU</td>							
								<td class="center">F04</td>
								<td>Avviso periodico indicativo</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS4');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center">F05</td>
								<td>Bando di gara</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS5');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>														
							<tr>
								<td class="center">F06</td>
								<td>Avviso di aggiudicazione di appalto</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS6');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center">F07</td>
								<td>Sistema di qualificazione</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS7');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center">F08</td>
								<td>Avviso relativo al profilo di committente</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS8-25');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
							<tr>
								<td class="center"><i>F14</i></td>
								<td><i>Avviso di rettifica (**)</i></td>
								<td class="center">
									<i>Disponibile all'interno dei formulari<br>F04, F05, F06, F07, F08 e F20</i>
								</td>
							</tr>							
							<tr>
								<td class="center">F20</td>
								<td>Avviso di modifica di appalto durante il periodo di validit&agrave;</td>
								<td class="center">
									<a href="javascript:creaNuovoAvviso('FS20-25');" title="Crea nuovo avviso o avviso" >
									<img width="20" height="20" title="Crea nuovo bando o avviso" alt="Crea nuovo bando o avviso" src="${pageContext.request.contextPath}/img/Files-5.png"/>
									</a>
								</td>
							</tr>
						</table>
					</td>
				</tr>			

				<tr>
					<td>
						<div style="width: 90%; margin-left: 20px;">
							<br>
							<br>
							<b>(**) F14 - Avviso di rettifica, regole di utilizzo</b>
							<br>
							<br>
							Per modificare un bando o avviso gi&agrave; inviato e pubblicato &egrave; necessario utilizzare l'avviso di rettifica, in nessun caso &egrave; possibile modificare i dati originali.
							<br>
							<br>
							Il formulario F14 deve essere utilizzato in caso di <b>procedure in corso</b> per comunicare solamente <b>modifiche minori in avvisi gi&agrave; pubblicati</b>.
							<br>
							<br>
							Il formulario F14 <b>non &egrave; utilizzabile</b> in caso di modifiche significative, il bando o avviso da modificare deve essere oggetto di una nuova pubblicazione.
							<br>
							<br>
							Il formulario F14 <b>non pu&ograve; essere utilizzato</b> per:
							<br> - modificare il tipo di appalto,
							<br> - modificare il tipo di procedura,
							<br> - modificare la direttiva di riferimento,
							<br> - annullare un bando di gara.
							<br>
							<br>
							Le modifiche introdotte con il formulario F14 devono riguardare solo avvisi pubblicati in precedenza nel TED:
							<br> - si prega di fornire il riferimento del pertinente avviso originale pubblicato nel TED,
							<br> - si prega di indicare chiaramente la relativa sezione e il numero di paragrafo nell'avviso originale da modificare,
							<br> - il testo da modificare nella parte "anzich&egrave;" deve corrispondere esattamente al testo pubblicato nell'avviso originale,
							<br> - il corpo del formulario F14 non deve essere utilizzato per introdurre modifiche ai documenti di gara (specifiche tecniche), tuttavia, &egrave; possibile utilizzare la sessione VII.2) "Altre informazioni complementari" solo per comunicare che vi sono modifiche nei documenti di gara.
							<br>
							<br>
							<b>Procedure incomplete o infruttuose</b>
							<br>
							Il formulario F14 <b>non &egrave; utilizzabile</b> per fornire informazioni su procedure incomplete o infruttuose. Ci&ograve; avviene tramite l'avviso relativo agli appalti aggiudicati (F03, F06), dove la non aggiudicazione pu&ograve; essere indicata, per ogni lotto, alla sezione V.1).
							<br>
							<br>
							<b>Modifica di un appalto/concessione durante il suo periodo di validit&agrave;</b>
							<br>
							Il formulario F14 <b>non &egrave; utilizzabile</b> per modificare un appalto/concessione durante il suo periodo di validit&agrave;. A tal fine, si prega di utilizzare il formulario F20.
						</div>
						<br>
						<br>
					</td>
				</tr>
			</gene:campoScheda>
			
			<gene:campoScheda>
				<td class="comandi-dettaglio" colSpan="2">
					<INPUT type="button" class="bottone-azione" value="Annulla" title="Annulla" onclick="javascript:annulla();">&nbsp;&nbsp;
				</td>
			</gene:campoScheda>
		</gene:formScheda>
	
		<form name="formSchedaBandoAvviso" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
			<input type="hidden" name="href" value="" /> 
			<input type="hidden" name="entita" value="" />
			<input type="hidden" name="key" value="" />
			<input type="hidden" name="metodo" value="apri" />
			<input type="hidden" name="modo" value="NUOVO" />
			<input type="hidden" name="activePage" value="0" />
			<input type="hidden" name="legal" value="" />
			<input type="hidden" name="notice_relation" value="" />
		</form>
	
		<gene:redefineInsert name="schedaConferma"></gene:redefineInsert>
		<gene:redefineInsert name="schedaAnnulla"></gene:redefineInsert>
		<gene:redefineInsert name="addToAzioni" >
			<tr>	  
				<td class="vocemenulaterale">
					<a href="javascript:annulla();" title="Annulla" tabindex="1610">Annulla</a>
				</td>
			</tr>
		</gene:redefineInsert>
		<gene:javaScript>
			function annulla(){
				schedaAnnulla();
			}
			
			function creaNuovoAvviso(formulario) {
			
				var pag="";
				var entita="";
				var legal="";
				var notice_relation="";
			
				if (formulario == "FS1") {
					pag = "w3/w3fs1/w3fs1-scheda.jsp";
					entita = "FS1";
					legal="32014L0024";
				}
				
				if (formulario == "FS2") {
					pag = "w3/w3fs2/w3fs2-scheda.jsp";
					entita = "FS2";
					legal="32014L0024";
				}
					
				if (formulario == "FS3") {
					pag = "w3/w3fs3/w3fs3-scheda.jsp";
					entita = "FS3";
					legal="32014L0024";
				}

				if (formulario == "FS4") {
					pag = "w3/w3fs4/w3fs4-scheda.jsp";
					entita = "FS4";
					legal="32014L0025";
				}

				if (formulario == "FS5") {
					pag = "w3/w3fs5/w3fs5-scheda.jsp";
					entita = "FS5";
					legal="32014L0025";
				}
					
				if (formulario == "FS6") {
					pag = "w3/w3fs6/w3fs6-scheda.jsp";
					entita = "FS6";
					legal="32014L0025";
				}

				if (formulario == "FS7") {
					pag = "w3/w3fs7/w3fs7-scheda.jsp";
					entita = "FS7";
					legal="32014L0025";
				}
					
				if (formulario == "FS8-24") {
					pag = "w3/w3fs8/w3fs8-scheda.jsp";
					entita = "FS8";
					notice_relation="3";
				}

				if (formulario == "FS8-25") {
					pag = "w3/w3fs8/w3fs8-scheda.jsp";
					entita = "FS8";
					notice_relation="4";
				}
				
				if (formulario == "FS20-24") {
					pag = "w3/w3fs20/w3fs20-scheda.jsp";
					entita = "FS20";
					legal="32014L0024";
				}

				if (formulario == "FS20-25") {
					pag = "w3/w3fs20/w3fs20-scheda.jsp";
					entita = "FS20";
					legal="32014L0025";
				}
				
				formSchedaBandoAvviso.href.value=pag;
				formSchedaBandoAvviso.entita.value=entita;
				formSchedaBandoAvviso.legal.value=legal;
				formSchedaBandoAvviso.notice_relation.value=notice_relation;
				formSchedaBandoAvviso.submit();
				
			}
			
			
		</gene:javaScript>
	</gene:redefineInsert>
</gene:template>


