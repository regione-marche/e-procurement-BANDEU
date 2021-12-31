
<%
	/*
	 * Created on 01-Set-2014
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

<gene:template file="ricerca-template.jsp" idMaschera="UNIT-trova"
	gestisciProtezioni="true" schema="W3">
	<gene:setString name="titoloMaschera" value="Ricerca unit&agrave organizzative" />
	<gene:setString name="entita" value="UNIT" />
	<gene:redefineInsert name="corpo">
		<gene:formTrova entita="UNIT" gestisciProtezioni="true">
			<gene:campoTrova campo="DESCUNIT" />
			<gene:campoTrova campo="NOTE" />
		</gene:formTrova>
	</gene:redefineInsert>
</gene:template>
