<%
/*
 * Created on: 08-mar-2007
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
/* Form di ricerca dei tecnici */
%>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="listaOpzioniDisponibili" value="${fn:join(opzDisponibili,'#')}#"/>

<gene:template file="ricerca-template.jsp" gestisciProtezioni="true" schema="GENE" idMaschera="TrovaImprese" >

	<gene:setString name="titoloMaschera" value="Ricerca imprese"/>

	<gene:redefineInsert name="corpo">
  	<gene:formTrova entita="IMPR" gestisciProtezioni="true" >
			<gene:gruppoCampi idProtezioni="Gen">
				<tr><td colspan="3"><b>Dati generali</b></td></tr>
				<gene:campoTrova title="Codice" campo="CODIMP"/>
				<gene:campoTrova title="Denominazione" campo="NOMEST"/>
				<gene:campoTrova campo="CFIMP"/>
				<gene:campoTrova campo="PIVIMP"/>
				<gene:campoTrova campo="INDIMP"/>
				<gene:campoTrova campo="NCIIMP"/>
				<gene:campoTrova entita="W3IMPR" campo="TOWN" where="IMPR.CODIMP = W3IMPR.CODIMP"/>
				<gene:campoTrova entita="W3IMPR" campo="NUTS" where="IMPR.CODIMP = W3IMPR.CODIMP"/>
			</gene:gruppoCampi>

		</gene:formTrova>

		
	</gene:redefineInsert>
</gene:template>

