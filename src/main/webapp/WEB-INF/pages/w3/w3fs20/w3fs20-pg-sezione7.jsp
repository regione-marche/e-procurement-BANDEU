
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

<gene:formScheda entita="W3FS20" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS20" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS20.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.VII.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.VII.1.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="M_CPV" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Vocabolario supplementare 1" campo="M_CPVSUPP1" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Vocabolario supplementare 2" campo="M_CPVSUPP2" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio CPV" href="" />
	</gene:campoScheda>
		
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII.1.2")}</b></td>
	</gene:campoScheda>
	<jsp:include page="/WEB-INF/pages/w3/w3cpv/w3cpv-interno-scheda-complementare.jsp">
		<jsp:param name="ent" value='W3FS20'/>
		<jsp:param name="id" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="num" value='1'/>
	</jsp:include>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII.1.3")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Codice NUTS 1" campo="M_SITE_NUTS" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio NUTS" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Codice NUTS 2" campo="M_SITE_NUTS_2" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio NUTS" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Codice NUTS 3" campo="M_SITE_NUTS_3" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio NUTS" href="" />
	</gene:campoScheda>
	<gene:campoScheda title="Codice NUTS 4" campo="M_SITE_NUTS_4" href="#" speciale="true" >
		<gene:popupCampo titolo="Dettaglio NUTS" href="" />
	</gene:campoScheda>			
	<gene:campoScheda campo="M_SITE_LABEL" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII.1.4")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="M_DESCRIPTION" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII.1.5")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="M_DURATION_MONTH" />
	<gene:campoScheda campo="M_DURATION_DAY" />
	<gene:campoScheda campo="M_DATE_START" />
	<gene:campoScheda campo="M_DATE_STOP" />
	<gene:campoScheda title="Giustificazione per una durata superiore a 4 anni (Direttiva 2014/24/UE) oppure 8 anni (Direttiva 2015/25/UE)" 
		campo="M_JUSTIFICATION" gestore="it.eldasoft.gene.tags.decorators.campi.gestori.GestoreCampoNote"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII.1.6")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="M_VAL_TOTAL" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VII.1.7")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="M_AWARDED_GROUP" />	
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneW3FS20AWARDMFunction" parametro='${gene:getValCampo(key, "ID")};1' />	
	<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
		<jsp:param name="entita" value='W3FS20AWARD_M'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3FS20AWARD_M' />
		<jsp:param name="idProtezioni" value="W3FS20AWARD_M" />
		<jsp:param name="sezioneListaVuota" value="true" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3fs20award_m/w3fs20award_m-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3FS20AWARD_M_ID_', 'W3FS20AWARD_M_ITEM_', 'W3FS20AWARD_M_NUM_', 'W3FS20AWARD_M_CONTRACTOR_CODIMP_', 'W3FS20AWARD_M_CONTRACTOR_SME_',  'NOMEST_'"/>
		<jsp:param name="titoloSezione" value="Contraente n." />
		<jsp:param name="titoloNuovaSezione" value="Nuovo contraente" />
		<jsp:param name="descEntitaVociLink" value="contraente" />
		<jsp:param name="msgRaggiuntoMax" value="i contraenti"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>
	
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs20.VII.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.VII.2.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="M_SHORT_DESCR" />

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs20.VII.2.2")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda>
		<td colspan="2"><div style="padding: 5 20 5 20"><i>Necessit&agrave; di lavori, servizi o forniture supplementari da parte del contraente originale [articolo 72, paragrafo 1, lettera b) della direttiva 2014/24/UE, articolo 89, paragrafo 1, lettera b) della direttiva 2014/25/UE]</i></div></td>
	</gene:campoScheda>		
	<gene:campoScheda title="Descrizione dei motivi economici o tecnici e dei disguidi e della duplicazione dei costi che impediscono un cambiamento di contraente" campo="M_ADDITIONAL_NEED" />
		<gene:campoScheda>
			<td colspan="2"><div style="padding: 5 20 5 20"><i>Necessit&agrave; di modifica determinata da circostanze che un'amministrazione aggiundicatrice diligente non ha potuto prevedere [articolo 72, paragrafo 1, lettera c) della direttiva 2014/24/UE, articolo 89, paragrafo 1, lettera c) della direttiva 2014/25/UE]</i></div></td>
		</gene:campoScheda>		
	<gene:campoScheda title="Descrizione delle circostanze che hanno reso necessaria la modifica e spiegazione della natura imprevista di tali circostanze" campo="M_UNFORESSEN" />	
	
	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs20.VII.2.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Valore totale dell'appalto prima delle modifiche (tenendo conto di eventuali modifiche contrattuali e adeguamenti di prezzo precedenti)" campo="M_VAL_TOTAL_BEFORE" />
	<gene:campoScheda title="Valore totale dell'appalto dopo le modifiche" campo="M_VAL_TOTAL_AFTER" />

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS20_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	
	
	
</gene:formScheda>

<gene:javaScript>


	$(window).ready(function (){
		_creaFinestraAlberoCpvVP();
		_creaFinestraAlberoCpvVSUPP();
		_creaLinkAlberoCpvVP($("#W3FS20_M_CPV").parent(), "${modo}", $("#W3FS20_M_CPV"), $("#W3FS20_M_CPVview") );
		_creaLinkAlberoCpvVSUPP($("#W3FS20_M_CPVSUPP1").parent(), "${modo}", $("#W3FS20_M_CPVSUPP1"), $("#W3FS20_M_CPVSUPP1view") );
		_creaLinkAlberoCpvVSUPP($("#W3FS20_M_CPVSUPP2").parent(), "${modo}", $("#W3FS20_M_CPVSUPP2"), $("#W3FS20_M_CPVSUPP2view") );
		
		$("input[name*='CPV']").attr('readonly','readonly');
		$("input[name*='CPV']").attr('tabindex','-1');
		$("input[name*='CPV']").css('border-color','#A3A6FF');
		$("input[name*='CPV']").css('border-width','1px');
		$("input[name*='CPV']").css('background-color','#E0E0E0');
		
		_creaFinestraAlberoNUTS();
		_creaLinkAlberoNUTS($("#W3FS20_M_SITE_NUTS").parent(), "${modo}", $("#W3FS20_M_SITE_NUTS"), $("#W3FS20_M_SITE_NUTSview") );
		_creaLinkAlberoNUTS($("#W3FS20_M_SITE_NUTS_2").parent(), "${modo}", $("#W3FS20_M_SITE_NUTS_2"), $("#W3FS20_M_SITE_NUTS_2view") );
		_creaLinkAlberoNUTS($("#W3FS20_M_SITE_NUTS_3").parent(), "${modo}", $("#W3FS20_M_SITE_NUTS_3"), $("#W3FS20_M_SITE_NUTS_3view") );
		_creaLinkAlberoNUTS($("#W3FS20_M_SITE_NUTS_4").parent(), "${modo}", $("#W3FS20_M_SITE_NUTS_4"), $("#W3FS20_M_SITE_NUTS_4view") );

		$("input[name^='W3FS20_M_SITE_NUTS']").attr('readonly','readonly');
		$("input[name^='W3FS20_M_SITE_NUTS']").attr('tabindex','-1');
		$("input[name^='W3FS20_M_SITE_NUTS']").css('border-color','#A3A6FF');
		$("input[name^='W3FS20_M_SITE_NUTS']").css('border-width','1px');
		$("input[name^='W3FS20_M_SITE_NUTS']").css('background-color','#E0E0E0');	
		
	});

	
	
</gene:javaScript>
