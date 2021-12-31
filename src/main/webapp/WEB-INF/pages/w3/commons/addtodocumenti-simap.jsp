<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<c:set var="isNavigazioneDisattiva" value="${isNavigazioneDisabilitata}" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:if test='${isNavigazioneDisattiva ne "1"}'>			        	
	<tr>
		<td class="vocemenulaterale">
			<a href="javascript:generaSIMAPDOC('${id}');" title="Genera PDF" tabindex="1513">Genera PDF</a>
	    </td>
	</tr>
</c:if>

	