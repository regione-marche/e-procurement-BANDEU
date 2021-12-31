
<%
	/*
	 * Created on 18-feb-2009
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

			
<tr>
	<td>
		<b>${titoloGenerico}</b>
	</td>
</tr>

<c:if test="${numeroErrori > 1 && numeroWarning == 0}">
	<tr>
		<td>		
			Durante il controllo dei dati sono stati rilevati <span class="contatoreerrorisimap">${numeroErrori}</span> errori bloccanti.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori > 1 && numeroWarning == 1}">
	<tr>
		<td>		
			Durante il controllo dei dati sono stati rilevati <span class="contatoreerrorisimap">${numeroErrori}</span> errori bloccanti e <span class="contatorewarningsimap">${numeroWarning}</span> avviso.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori > 1 && numeroWarning > 1}">
	<tr>
		<td>		
			Durante il controllo dei dati sono stati rilevati <span class="contatoreerrorisimap">${numeroErrori}</span> errori bloccanti e <span class="contatorewarningsimap">${numeroWarning}</span> avvisi.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori == 1 && numeroWarning == 0}">
	<tr>
		<td>		
			Durante il controllo dei dati &egrave; stato rilevato <span class="contatoreerrorisimap">${numeroErrori}</span> errore bloccante.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori == 1 && numeroWarning == 1}">
	<tr>
		<td>		
			Durante il controllo dei dati sono stati rilevati <span class="contatoreerrorisimap">${numeroErrori}</span> errore bloccante e <span class="contatorewarningsimap">${numeroWarning}</span> avviso.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori == 1 && numeroWarning > 1}">
	<tr>
		<td>		
			Durante il controllo dei dati sono stati rilevati <span class="contatoreerrorisimap">${numeroErrori}</span> errore bloccante e <span class="contatorewarningsimap">${numeroWarning}</span> avvisi.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori == 0 && numeroWarning == 1}">
	<tr>
		<td>		
			Durante il controllo dei dati &egrave; stato rilevato <span class="contatorewarningsimap">${numeroWarning}</span> avviso.
			<br>
		</td>
	</tr>
</c:if>

<c:if test="${numeroErrori == 0 && numeroWarning > 1}">
	<tr>
		<td>		
			Durante il controllo dei dati sono stati rilevati <span class="contatorewarningsimap">${numeroWarning}</span> avvisi.
			<br>
		</td>
	</tr>
</c:if>

<tr>
	<td>
		<table id="tablevalidazionew3" class="validazionew3" width="100%">
			<thead>
				<tr class="intestazione">
					<td width="30px">N.</td>
					<td>Sezione</td>
					<td>Informazione o gruppo di informazioni</td>
					<td>Messaggio</td>				
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listaGenericaControlli}" step="1" var="controllo" varStatus="status" >
					<tr>
						<c:choose>
							<c:when test="${controllo[0] == 'T'}">
								<td style="border-right: 0px; width="30px">&nbsp;</td>
								<td style="border-left: 0px; border-right: 0px;"><b>${controllo[1]}</b></td>
								<td style="border-left: 0px; border-right: 0px;">&nbsp;</td>
								<td style="border-left: 0px;">&nbsp;</td>
							</c:when>
							<c:otherwise>
								<c:set var="cntw3" value="${cntw3 + 1}" />
								<td width="30px" class="center">${cntw3}</td>
								<td>${controllo[1]}</td>
								<td>${controllo[2]}</td>
								<td>
									<c:if test="${controllo[0] == 'W'}">
										<span class="contatorewarningsimap" style="margin-left: 0px;">Avviso</span><br>
									</c:if>
									${controllo[3]}
								</td>		
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</td>
</tr>

			



