
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

<gene:formScheda entita="W3FS20" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS20" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS20.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.V")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Contratto d'appalto n. " campo="CONTRACT_NUMBER" />
	<gene:campoScheda title="Lotto n. " campo="LOT_NUMBER" />
	<gene:campoScheda campo="CONTRACT_TITLE" obbligatorio="true"/>

	<gene:campoScheda>
		<td colspan="2"><br><b>${gene:resource("label.simap.fs20.V.2.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="CONTRACT_AWARD_DATE" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.V.2.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="AWARDED_GROUP" />

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.V.2.3")}</b></td>
	</gene:campoScheda>
	
	<gene:callFunction obj="it.eldasoft.sil.w3.tags.funzioni.GestioneW3FS20AWARDCFunction" parametro='${gene:getValCampo(key, "ID")};1' />	
	<jsp:include page="/WEB-INF/pages/commons/interno-scheda-multipla.jsp" >
		<jsp:param name="entita" value='W3FS20AWARD_C'/>
		<jsp:param name="chiave" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="nomeAttributoLista" value='datiW3FS20AWARD_C' />
		<jsp:param name="idProtezioni" value="W3FS20AWARD_C" />
		<jsp:param name="sezioneListaVuota" value="true" />
		<jsp:param name="jspDettaglioSingolo" value="/WEB-INF/pages/w3/w3fs20award_c/w3fs20award_c-interno-scheda.jsp"/>
		<jsp:param name="arrayCampi" value="'W3FS20AWARD_C_ID_', 'W3FS20AWARD_C_ITEM_', 'W3FS20AWARD_C_NUM_', 'W3FS20AWARD_C_CONTRACTOR_CODIMP_', 'W3FS20AWARD_C_CONTRACTOR_SME_',  'NOMEST_'"/>
		<jsp:param name="titoloSezione" value="Contraente n." />
		<jsp:param name="titoloNuovaSezione" value="Nuovo contraente" />
		<jsp:param name="descEntitaVociLink" value="contraente" />
		<jsp:param name="msgRaggiuntoMax" value="i contraenti"/>
		<jsp:param name="usaContatoreLista" value="true"/>
		<jsp:param name="numMaxDettagliInseribili" value="5"/>
	</jsp:include>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.V.2.4")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="VAL_TOTAL" />

	
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


