
<%
	/*
	 * Created on 12-nov-2010
	 *
	 * Copyright (c) EldaSoft S.p.A.
	 * Tutti i diritti sono riservati.
	 *
	 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
	 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
	 * aver prima formalizzato un accordo specifico con EldaSoft.
	 */
%>

<%@ taglib uri="http://www.owasp.org/index.php/Category:OWASP_CSRFGuard_Project/Owasp.CsrfGuard.tld" prefix="csrf" %>


<form name="formSchedaW3SIMAPEMAIL" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
	<input type="hidden" name="href" value="w3/w3simapemail/w3simapemail-scheda.jsp" /> 
	<input type="hidden" name="entita" value="W3SIMAPEMAIL" />
	<input type="hidden" name="key" value="" />
	<input type="hidden" name="metodo" value="apri" />
	<input type="hidden" name="activePage" value="0" />
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
</form>

<form name="formSchedaW3SIMAPWS" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
	<input type="hidden" name="href" value="w3/w3simapws/w3simapws-scheda.jsp" /> 
	<input type="hidden" name="entita" value="W3SIMAPWS" />
	<input type="hidden" name="key" value="" />
	<input type="hidden" name="metodo" value="apri" />
	<input type="hidden" name="activePage" value="0" />
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
</form>

<form name="formListaW3SIMAPEMAIL" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
	<input type="hidden" name="href" value="w3/w3simapemail/w3simapemail-lista.jsp" /> 
	<input type="hidden" name="entita" value="W3SIMAPEMAIL" />
	<input type="hidden" name="trovaAddWhere" value="" />
	<input type="hidden" name="trovaParameter" value="" /> 
	<input type="hidden" name="risultatiPerPagina" value="20" />
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
</form>

<form name="formListaW3SIMAPWS" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
	<input type="hidden" name="href" value="w3/w3simapws/w3simapws-lista.jsp" /> 
	<input type="hidden" name="entita" value="W3SIMAPWS" />
	<input type="hidden" name="trovaAddWhere" value="" />
	<input type="hidden" name="trovaParameter" value="" /> 
	<input type="hidden" name="risultatiPerPagina" value="20" />
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
</form>

<form name="formListaW3FS14" action="${pageContext.request.contextPath}/ApriPagina.do" method="post">
	<input type="hidden" name="href" value="w3/w3fs14/w3fs14-lista.jsp" /> 
	<input type="hidden" name="entita" value="W3FS14" />
	<input type="hidden" name="trovaAddWhere" value="" />
	<input type="hidden" name="trovaParameter" value="" /> 
	<input type="hidden" name="risultatiPerPagina" value="20" />
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
</form>

