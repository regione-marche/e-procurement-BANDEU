
<%
	/*
	 * Created on 05-Ott-2010
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

<gene:template file="ricerca-template.jsp" idMaschera="W3AMMI-trova"
	gestisciProtezioni="true" schema="W3">
	<gene:setString name="titoloMaschera" value="Ricerca amministrazioni aggiudicatrici" />
	<gene:setString name="entita" value="W3AMMI" />
	<gene:redefineInsert name="corpo">
		<gene:formTrova entita="W3AMMI" gestisciProtezioni="true">

			<gene:campoTrova entita="UFFINT" campo="NOMEIN" where="W3AMMI.CODEIN=UFFINT.CODEIN"/>

		</gene:formTrova>
	</gene:redefineInsert>
</gene:template>
