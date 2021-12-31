
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


<c:set var="filtroPermessi" value='${gene:callFunction3("it.eldasoft.sil.w3.tags.funzioni.GetFiltroPermessiFunction", pageContext, "W3SIMAP", "ID")}' />

<gene:template file="ricerca-template.jsp" idMaschera="W3SIMAP-trova"
	gestisciProtezioni="true" schema="W3">
	<gene:setString name="titoloMaschera" value="Ricerca bandi ed avvisi" />
	<gene:setString name="entita" value="W3SIMAP" />
	<gene:redefineInsert name="corpo">
		<gene:formTrova entita="W3SIMAP" filtro="${filtroPermessi}" gestisciProtezioni="true">
			<tr>
				<td colspan="3"><b>Informazioni sul bando/avviso</b></td>
			</tr>
			<gene:campoTrova campo="FORM" gestore="it.eldasoft.sil.w3.tags.funzioni.decoratori.campi.GestoreCampoFORM"/>			
			<gene:campoTrova title="Amministrazione aggiudicatrice" entita="UFFINT" campo="NOMEIN" 
				from="W3AMMI, W3AMMI" where="W3AMMI.CODAMM=W3SIMAP.CODAMM AND UFFINT.CODEIN=W3AMMI.CODEIN" />
				 
			<gene:campoTrova entita="V_W3SIMAP" campo="TITLE_CONTRACT" where="W3SIMAP.ID=V_W3SIMAP.ID"/>
			<gene:campoTrova title="Tipo di appalto" campo="CTYPE"/>
			
			<tr>
				<td colspan="3"><b>Numero e data pubblicazione del bando/avviso</b></td>
			</tr>
			<gene:campoTrova campo="NOTICE_NUMBER_OJ" />
			<gene:campoTrova campo="DATE_OJ" />	
			<tr>
				<td colspan="3"><b>Altre informazioni</b></td>
			</tr>
			<gene:campoTrova campo="NO_DOC_EXT" />
			<gene:campoTrova entita="V_W3SIMAP" campo="NOTICE_DATE" where="W3SIMAP.ID=V_W3SIMAP.ID"/>
			<gene:campoTrova campo="FSVERSION" />
			<gene:campoTrova entita="V_W3SIMAP" campo="IDGARA" where="W3SIMAP.ID=V_W3SIMAP.ID"/>
		</gene:formTrova>
	</gene:redefineInsert>
</gene:template>
