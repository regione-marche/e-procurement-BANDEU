
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

<gene:formScheda entita="W3FS4" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS4"
	plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS4" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS4.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS4" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FSVERSION" defaultValue="5" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>
	<gene:campoScheda entita="W3FS4S2" campo="ID" visibile="false" where="W3FS4.ID = W3FS4S2.ID AND W3FS4S2.NUM = 1"/>
	<gene:campoScheda entita="W3FS4S2" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS4.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b>Avviso periodico indicativo</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3SIMAP" campo="LEGAL_BASIS" obbligatorio="true" modificabile="false" defaultValue="32014L0025" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoLEGALBASIS25"/>	
	<gene:campoScheda campo="NOTICE_F04" obbligatorio="true"/>

	<jsp:include page="/WEB-INF/pages/w3/w3simap/w3simap-pubblication-info.jsp"></jsp:include>	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs4.I")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>
			${gene:resource("label.simap.fs4.I.1")}
			<br>${gene:resource("label.simap.fs4.I.4")}
			<br>${gene:resource("label.simap.fs4.I.5")}</b>
		</td>
	</gene:campoScheda>
	<gene:archivio titolo="Amministrazione aggiudicatrice"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI.CODAMM"),"w3/w3ammi/w3ammi-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda-popup.jsp","")}'
		 campi="W3AMMI.CODAMM;UFFINT.NOMEIN"
		 inseribile="false"
		 chiave="W3SIMAP_CODAMM">
		<gene:campoScheda entita="W3SIMAP" campo="CODAMM" obbligatorio="true"/>
		<gene:campoScheda visibile="true" modificabile="false" entita="UFFINT" campo="NOMEIN" from="W3AMMI, W3SIMAP, W3SIMAP" where="UFFINT.CODEIN = W3AMMI.CODEIN AND W3AMMI.CODAMM = W3SIMAP.CODAMM AND W3SIMAP.ID = W3FS4.ID" />
	</gene:archivio>
	
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs4.I.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3SIMAP" campo="JOINT_PROCUREMENT"/>
	<gene:campoScheda entita="W3SIMAP" title="Nel caso di appalto congiunto che coinvolge diversi paesi, specificare le normative nazionali sugli appalti in vigore" campo="PROCUREMENT_LAW"/>

	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneSimapAddrFunction" parametro='${gene:getValCampo(key,"ID")}' />	
	<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
		<jsp:param name="entita" value='W3SIMAP_ADDR'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3SIMAP_ADDR' />
		<jsp:param name="idProtezioni" value="W3SIMAP_ADDR" />
		<jsp:param name="sezioneListaVuota" value="false" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3simap_addr/w3simap_addr-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3SIMAP_ADDR_ID_', 'W3SIMAP_ADDR_NUM_', 'W3SIMAP_ADDR_CODAMM_', 'NOMEIN_'"/>
		<jsp:param name="titoloSezione" value="Amministrazione aggiudicatrice congiunta n." />
		<jsp:param name="titoloNuovaSezione" value="Nuova amministrazione aggiudicatrice congiunta" />
		<jsp:param name="descEntitaVociLink" value="amministrazione aggiudicatrice congiunta" />
		<jsp:param name="msgRaggiuntoMax" value="e amministrazioni aggiudicatrici"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>

	<gene:campoScheda entita="W3SIMAP" campo="CENTRAL_PURCHASING"/>
	
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs4.I.3")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda>
		<td colspan="2"><b>Disponibilit&agrave; dei documenti di gara</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3SIMAP" campo="DOCUMENT_FU_RE"/>
	<gene:campoScheda entita="W3SIMAP" campo="DOCUMENT_URL"/>
	<gene:campoScheda>
		<td colspan="2"><br><b>Ulteriori informazioni</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="W3SIMAP" campo="FURTHER_INFO"/>
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3SIMAP.FURTHER_INFO_CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3SIMAP_FURTHER_INFO_CODEIN">
		<gene:campoScheda entita="W3SIMAP" campo="FURTHER_INFO_CODEIN" visibile="false"/>
		<gene:campoScheda title="Altro indirizzo/ufficio" campo="FURTHER_INFO_NOMEIN" value="${requestScope.furtherInfoNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>
		
	<gene:campoScheda>
		<td colspan="2"><br><b>Offerte e domande di partecipazione</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Le offerte e le domande di partecipazione vanno inviate presso" entita="W3SIMAP" campo="PARTICIPATION"/>
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3SIMAP.PARTICIPATION_CODEIN"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3SIMAP_PARTICIPATION_CODEIN">
		<gene:campoScheda entita="W3SIMAP" campo="PARTICIPATION_CODEIN" visibile="false"/>
		<gene:campoScheda title="Altro indirizzo/ufficio" campo="PARTICIPATION_NOMEIN" value="${requestScope.participationNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>

	<gene:campoScheda title="In versione elettronica ?" entita="W3SIMAP" campo="PARTICIPATION_EL"/>
	<gene:campoScheda title="Indirizzo per la comunicazione elettronica delle offerte o domande di partecipazione (URL)" entita="W3SIMAP" campo="PARTICIPATION_URL"/>

	<gene:campoScheda>
		<td colspan="2"><br><b>Comunicazione elettronica</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="La comunicazione elettronica richiede l'utilizzo di strumenti e dispositivi che in genere non sono disponibili. Questi strumenti e dispositivi sono disponibili per un accesso gratuito, illimitato e diretto presso (URL)" entita="W3SIMAP" campo="URL_TOOL"/>
	
	<gene:fnJavaScriptScheda funzione="gestioneNOTICE_F04('#W3FS4_NOTICE_F04#')" elencocampi="W3FS4_NOTICE_F04" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneJOINT_PROCUREMENT('#W3SIMAP_JOINT_PROCUREMENT#')" elencocampi="W3SIMAP_JOINT_PROCUREMENT" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneDOCUMENT_FU_RE('#W3SIMAP_DOCUMENT_FU_RE#')" elencocampi="W3SIMAP_DOCUMENT_FU_RE" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestioneFURTHER_INFO('#W3SIMAP_FURTHER_INFO#')" elencocampi="W3SIMAP_FURTHER_INFO" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestionePARTICIPATION('#W3SIMAP_PARTICIPATION#')" elencocampi="W3SIMAP_PARTICIPATION" esegui="true" />
	<gene:fnJavaScriptScheda funzione="gestionePARTICIPATION_EL('#W3SIMAP_PARTICIPATION_EL#')" elencocampi="W3SIMAP_PARTICIPATION_EL" esegui="true" />
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS4_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>
	
</gene:formScheda>

<gene:javaScript>

	$("#W3FS4_NOTICE_F04 option").each(function() {
		$(this).text($(this).attr("title"));
	});

	$("#W3SIMAP_DOCUMENT_FU_RE option").each(function() {
		$(this).text($(this).attr("title"));
	});

	function gestioneNOTICE_F04(notice_f04){
		if (notice_f04 == 3) {
			var _messageNoticeF04 = $("<div/>",{"id":"messageNoticeF04"});
			_messageNoticeF04.css("font-style","italic");
			_messageNoticeF04.css("padding-top","5px");
			_messageNoticeF04.text("Gli operatori interessati devono informare l'ente aggiudicatore del loro interesse per il contratto d'appalto. L'appalto sar� aggiudicato senza pubblicazione di un ulteriore bando di gara.");
			$("#W3FS4_NOTICE_F04").parent().append(_messageNoticeF04);
		} else {
			$("#messageNoticeF04").hide();
		}
	}
	
	function gestioneJOINT_PROCUREMENT(joint_procurement){
		if (joint_procurement == '1') {
			$("#rowW3SIMAP_PROCUREMENT_LAW").show();
			$("#rowLinkAddW3SIMAP_ADDR").show();
		} else {
			$("#rowW3SIMAP_PROCUREMENT_LAW").hide();
			$("#W3SIMAP_PROCUREMENT_LAW").val("");
			$("#rowLinkAddW3SIMAP_ADDR").hide();
		}
	}
	
	function gestioneDOCUMENT_FU_RE(document_fu_re){
		if (document_fu_re == 1 || document_fu_re == 2) {
			$("#rowW3SIMAP_DOCUMENT_URL").show();
		} else {
			$("#rowW3SIMAP_DOCUMENT_URL").hide();
			$("#W3SIMAP_DOCUMENT_URL").val("");
		}
	}
	
	function gestioneFURTHER_INFO(further_info){
		if (further_info == 2) {
			$("#rowW3SIMAP_FURTHER_INFO_CODEIN").show();
			$("#rowFURTHER_INFO_NOMEIN").show();
		} else {
			$("#rowW3SIMAP_FURTHER_INFO_CODEIN").hide();
			$("#rowFURTHER_INFO_NOMEIN").hide();
			$("#W3SIMAP_FURTHER_INFO_CODEIN").val("");
			$("#FURTHER_INFO_NOMEIN").val("");
		}
	}

	function gestionePARTICIPATION(participation){
		if (participation == '2') {
			$("#rowW3SIMAP_PARTICIPATION_CODEIN").show();
			$("#rowPARTICIPATION_NOMEIN").show();
		} else {
			$("#rowW3SIMAP_PARTICIPATION_CODEIN").hide();
			$("#rowPARTICIPATION_NOMEIN").hide();
			$("#W3SIMAP_PARTICIPATION_CODEIN").val("");
			$("#PARTICIPATION_NOMEIN").val("");
		}
	}

	function gestionePARTICIPATION_EL(participation_el){
		if (participation_el == '1') {
			$("#rowW3SIMAP_PARTICIPATION_URL").show();
		} else {
			$("#rowW3SIMAP_PARTICIPATION_URL").hide();
			$("#W3SIMAP_PARTICIPATION_URL").val("");
		}
	}

</gene:javaScript>
