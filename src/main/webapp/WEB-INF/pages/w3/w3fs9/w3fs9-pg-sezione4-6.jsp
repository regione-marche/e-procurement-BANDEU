
<%
	/*
	 * Created on 08-Ott-2010
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

<gene:formScheda entita="W3FS9" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS9" >

	<gene:campoScheda campo="ID" visibile="false"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.IV")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.IV.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.IV.1.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Tipo di procedura" campo="TYPE_PROCEDURE" campoFittizio="true" definizione="T100" value="Aperta" modificabile="false"/>

	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.IV.1.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="IS_ELECTRONIC" />
	<gene:campoScheda campo="USE_ELECTRONIC" /> 

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.IV.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.IV.2.1")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda campo="REFERENCE_NUMBER_ATTRIBUTED"/>
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.IV.2.2")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="NOTICE_NUMBER_OJ"/>
	<gene:campoScheda campo="DATE_OJ"/>
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.IV.2.3")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="TIME_LIMIT_DATE" obbligatorio="true" />
	<gene:campoScheda campo="TIME_LIMIT_TIME" obbligatorio="true" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.IV.2.4")}</b></td>
	</gene:campoScheda>
	
	<gene:campoScheda campo="LANGUAGE_ANY_EC" />
	
	<gene:campoScheda>
		<td colspan="2">&nbsp;</td>
	</gene:campoScheda>
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneLanguageFunction" parametro='${gene:getValCampo(key,"ID")}' />	
	<jsp:include page="/WEB-INF/pages/w3/commons/interno-sezione-multipla-singola-riga.jsp" >
		<jsp:param name="entita" value='W3LANGUAGE'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3LANGUAGE' />
		<jsp:param name="idProtezioni" value="W3LANGUAGE" />
		<jsp:param name="sezioneListaVuota" value="true" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3language/w3language-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3LANGUAGE_ID_', 'W3LANGUAGE_NUM_', 'W3LANGUAGE_LANGUAGE_EC_'"/>
		<jsp:param name="titoloSezione" value="Lingue utilizzabili" />
		<jsp:param name="titoloNuovaSezione" value="Nuovo lingua" />
		<jsp:param name="descEntitaVociLink" value="lingua" />
		<jsp:param name="msgRaggiuntoMax" value="e lingue"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>
	<gene:campoScheda>
		<td colspan="2">&nbsp;</td>
	</gene:campoScheda>
	
	<gene:campoScheda campo="LANGUAGE_OTHER" />	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.VI")}<br><br></b></td>
	</gene:campoScheda>	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.VI.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="ADDITIONAL_INFORMATION"/>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.VI.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="V_W3SIMAP" campo="NOTICE_DATE" where="V_W3SIMAP.ID = W3FS9.ID" modificabile="false"/>

	<gene:fnJavaScriptScheda funzione="gestioneIS_ELECTRONIC('#W3FS9_IS_ELECTRONIC#')" elencocampi="W3FS9_IS_ELECTRONIC" esegui="true" />

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS9_ID}"/>
		</jsp:include>
	</gene:campoScheda>	

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
</gene:formScheda>

<gene:javaScript>

	function gestioneIS_ELECTRONIC(is_electronic){
		document.getElementById("rowW3FS9_USE_ELECTRONIC").style.display = (is_electronic=='1' ? '':'none');
		if (is_electronic!='1') {
			document.forms[0].W3FS9_USE_ELECTRONIC.value='';
		}
	}
</gene:javaScript>

