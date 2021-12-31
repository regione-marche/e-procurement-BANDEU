<%
/*
 * Created on: 06/08/2014
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
%>
<% // sezione di codice per disabilitare i comandi per entrare in modifica dei dati %>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test='${modoAperturaScheda eq "VISUALIZZA"}'>
	<c:set var="result" value='${gene:callFunction4("it.eldasoft.sil.w3.tags.funzioni.GetPermessiFunction", pageContext, param.tblname, param.clm1name, param.clm1value)}' />
	<c:if test='${autorizzatoModifica eq "false" || autorizzatoModificaDopoInvio eq "false"}'>
		<gene:redefineInsert name="schedaModifica"></gene:redefineInsert>
		<gene:redefineInsert name="pulsanteModifica"></gene:redefineInsert>
	</c:if>
</c:if>
