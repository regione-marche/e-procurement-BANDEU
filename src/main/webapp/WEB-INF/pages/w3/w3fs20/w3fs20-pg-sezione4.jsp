
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
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.IV")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.IV.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.IV.2.1")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda title="Numero dell'avviso nella GU/S" campo="NOTICE_NUMBER_OJ" />
	
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
