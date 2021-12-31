<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.eldasoft.it/genetags" prefix="gene"%>
<c:set var="isNavigazioneDisattiva" value="${isNavigazioneDisabilitata}" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
	<c:when test='${isNavigazioneDisattiva ne "1"}'>
		<c:set var="esisteInvioEmail" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.EsisteInvioSimapFunction",pageContext,id)}' />
		<c:set var="esisteInvioWS" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.EsisteInvioSimapWSFunction",pageContext,id)}' />
		<c:set var="isRettificabile" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.IsRettificabileFunction",pageContext,id)}' />
		<c:set var="esisteRettifica" value='${gene:callFunction2("it.eldasoft.sil.w3.tags.funzioni.EsisteRettificaSimapFunction",pageContext,id)}' />
	</c:when>
	<c:otherwise>
		<c:set var="esisteInvioEmail" value="false" />
		<c:set var="esisteInvioWS" value="false" />
		<c:set var="isRettificabile" value="false" />
		<c:set var="esisteRettifica" value="false" />
	</c:otherwise>
</c:choose>
	
<c:if test='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.SIMAP.VALIDAZIONE") && autorizzatoModifica eq "true" && autorizzatoModificaDopoInvio eq "true"}'>	
	<tr>
		<c:choose>
	        <c:when test='${isNavigazioneDisattiva ne "1"}'>
	          <td class="vocemenulaterale" id="menulateralecontrolladati">
				<a href="javascript:popupSIMAPVALIDAZIONE('${id}');" title="Controlla dati inseriti" tabindex="1512">
					Controlla dati
				</a>
			  </td>
	        </c:when>
		    <c:otherwise>
		       <td id="menulateralecontrolladati">
				  Controlla dati
			   </td>
		    </c:otherwise>
		</c:choose>
	</tr>
</c:if>

<c:if test='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.SIMAP.INVIA.EMAIL") && autorizzatoModifica eq "true" && autorizzatoModificaDopoInvio eq "true"}'>	
	<tr>
		<c:choose>
	        <c:when test='${isNavigazioneDisattiva ne "1"}'>
	          <td class="vocemenulaterale">
				<a href="javascript:inviaSIMAPEMAIL('${id}');" title="Invia dati a SIMAP per la pubblicazione" tabindex="1513">
					Invia dati a SIMAP
				</a>
			  </td>
	        </c:when>
		    <c:otherwise>
		       <td>
				 Invia dati a SIMAP
			   </td>
		    </c:otherwise>
		</c:choose>
	</tr>
</c:if>

<c:if test='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.SIMAP.INVIA.WS") && autorizzatoModifica eq "true" && autorizzatoModificaDopoInvio eq "true"}'>
	<tr>
		<c:choose>
	        <c:when test='${isNavigazioneDisattiva ne "1"}'>
	          <td class="vocemenulaterale">
				<a href="javascript:inviaSIMAPWS('${id}');" title="Invia dati a SIMAP per la pubblicazione" tabindex="1514">
					Invia dati a SIMAP
				</a>
			  </td>
	        </c:when>
		    <c:otherwise>
		       <td>
				 Invia dati a SIMAP
			   </td>
		    </c:otherwise>
		</c:choose>
	</tr>
</c:if>	
	
<c:if test='${esisteInvioEmail eq "true" || esisteInvioWS eq "true"}'>

	<c:if test='${esisteInvioEmail eq "true"}'>
		<c:if test='${gene:checkProt(pageContext, "FUNZ.VIS.ALT.W3.SIMAP.LISTAINVII.EMAIL")}'>
			<tr>
				<c:choose>
			        <c:when test='${isNavigazioneDisattiva ne "1"}'>
			          <td class="vocemenulaterale">
						<a href="javascript:inviiSIMAPEMAIL('${id}');" title="Lista degli invii SIMAP effettuati via e-mail" tabindex="1515">
							Lista invii effettuati via e-mail
						</a>
					  </td>
			        </c:when>
				    <c:otherwise>
				       <td>
						 Lista invii effettuati via e-mail
					   </td>
				    </c:otherwise>
				</c:choose>
			</tr>
		</c:if>
	</c:if>
	
	<c:if test="${param.formulario ne 'W3FS14'}">
		
		<c:if test="${isRettificabile eq 'true' && autorizzatoModifica eq 'true'}">
			<tr>
				<c:choose>
			        <c:when test='${isNavigazioneDisattiva ne "1"}'>
			          <td class="vocemenulaterale">
						<a href="javascript:componiW3FS14('${id}');" title="Nuova rettifica" tabindex="1517">
							Nuova rettifica
						</a>
					  </td>
			        </c:when>
				    <c:otherwise>
				       <td>
						 Nuova rettifica
					   </td>
				    </c:otherwise>
				</c:choose>
			</tr>
		</c:if>
		
		<c:if test='${esisteRettifica eq "true"}'>
			<tr>
				<c:choose>
			        <c:when test='${isNavigazioneDisattiva ne "1"}'>
			          <td class="vocemenulaterale">
						<a href="javascript:listaW3FS14('${id}');" title="Lista delle rettifiche" tabindex="1518">
							Lista rettifiche
						</a>
					  </td>
			        </c:when>
				    <c:otherwise>
				       <td>
						 Lista rettifiche
					   </td>
				    </c:otherwise>
				</c:choose>
			</tr>
		</c:if>
	</c:if>
</c:if>	


	