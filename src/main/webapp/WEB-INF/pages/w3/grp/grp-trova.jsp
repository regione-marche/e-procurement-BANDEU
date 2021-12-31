
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

<gene:template file="ricerca-template.jsp" idMaschera="GRP-trova"
	gestisciProtezioni="true" schema="W3">
	<gene:setString name="titoloMaschera" value="Ricerca gruppi" />
	<gene:setString name="entita" value="GRP" />
	<gene:redefineInsert name="corpo">
		<gene:formTrova entita="GRP" gestisciProtezioni="true">

			<gene:campoTrova title="Descrizione gruppo" campo="DESCGRP" />
			<gene:campoTrova campo="NOTE" />
			<gene:campoTrova entita="USRSYS" campo="SYSUTE" from="GRPUSRSYS" where="USRSYS.SYSCON = GRPUSRSYS.SYSCON AND GRPUSRSYS.IDGRP = GRP.IDGRP" />

		</gene:formTrova>
	</gene:redefineInsert>
</gene:template>
