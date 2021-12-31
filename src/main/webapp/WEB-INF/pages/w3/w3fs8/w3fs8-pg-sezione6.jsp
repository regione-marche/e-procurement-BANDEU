
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

<gene:formScheda entita="W3FS8" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS8" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS8.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS8" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>
	<gene:campoScheda entita="W3FS8S2" campo="ID" visibile="false" where="W3FS8.ID = W3FS8S2.ID"/>
	<gene:campoScheda entita="W3FS8S2" campo="NUM"  visibile="false" defaultValue="1" where="W3FS8.ID = W3FS8S2.ID"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" visibile="false" where="W3FS8.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" defaultValue="1" visibile="false" where="W3FS8.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.VI.3")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda title="Descrizione" entita="W3FS8" campo="ADDITIONAL_INFORMATION"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs2.VI.5")}</b></td>
	</gene:campoScheda>		
	<gene:campoScheda entita="V_W3SIMAP" campo="NOTICE_DATE" where="V_W3SIMAP.ID = W3FS8.ID" modificabile="false"/>
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS8_ID}"/>
		</jsp:include>
	</gene:campoScheda>	
	
	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:redefineInsert name="schedaNuovo"></gene:redefineInsert>
	<gene:redefineInsert name="pulsanteNuovo"></gene:redefineInsert>	
	
</gene:formScheda>


