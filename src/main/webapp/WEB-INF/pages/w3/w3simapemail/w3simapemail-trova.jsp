
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

<c:set var="filtroLivelloUtente" value="W3SIMAPEMAIL.ID IN (SELECT W3SIMAP.ID FROM W3SIMAP WHERE W3SIMAP.SYSCON = ${profiloUtente.id})" />

<gene:template file="ricerca-template.jsp" idMaschera="W3SIMAPEMAIL-trova"
	gestisciProtezioni="true" schema="W3">
	<gene:setString name="titoloMaschera" value="Ricerca invii" />
	<gene:setString name="entita" value="W3SIMAPEMAIL" />
	<gene:redefineInsert name="corpo">
		<gene:formTrova entita="W3SIMAPEMAIL" filtro="${filtroLivelloUtente}" 
			gestisciProtezioni="true"
			lista="w3/w3simapemail/w3simapemail-lista-invii.jsp">
			<tr>
				<td colspan="3"><b>Informazioni sull'invio</b></td>
			</tr>
			<gene:campoTrova campo="DATA_SPEDIZIONE"/>	
			<gene:campoTrova campo="EMAILPHASE" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoEMAILPHASECompleto"/>		
			<tr>
				<td colspan="3"><b>Numero e data pubblicazione del bando/avviso</b></td>
			</tr>
			<gene:campoTrova entita="W3SIMAP" campo="NOTICE_NUMBER_OJ" />
			<gene:campoTrova entita="W3SIMAP" campo="DATE_OJ" />
			<tr>
				<td colspan="3"><b>Informazioni sul bando/avviso inviato</b></td>
			</tr>
			<gene:campoTrova entita="W3SIMAP" campo="FORM" where="W3SIMAPEMAIL.ID=W3SIMAP.ID" />			
			<gene:campoTrova entita="V_W3SIMAP" campo="CTYPE" where="W3SIMAPEMAIL.ID=V_W3SIMAP.ID"/>
			<gene:campoTrova entita="V_W3SIMAP" campo="TITLE_CONTRACT" where="W3SIMAPEMAIL.ID=V_W3SIMAP.ID"/>
	

		</gene:formTrova>
	</gene:redefineInsert>
</gene:template>
