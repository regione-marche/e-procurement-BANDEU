
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

<gene:formScheda entita="W3FS5" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS5"
	plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS5" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3FS5S36" campo="ID" obbligatorio="true" visibile="false" where="W3FS5.ID = W3FS5S36.ID"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS5.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />

	<jsp:include page="w3fs5-interno-sezione2-2.jsp">
		<jsp:param value='${gene:getValCampo(key, "ID")}' name="w3annexb_id"/>
		<jsp:param value='1' name="w3annexb_num"/>
	</jsp:include>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS5_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
</gene:formScheda>


