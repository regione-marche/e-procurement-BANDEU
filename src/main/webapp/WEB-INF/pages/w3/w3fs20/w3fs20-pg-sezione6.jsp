
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
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3FS20" plugin="it.eldasoft.sil.w3.tags.gestori.plugin.GestoreW3FS20" >

	<gene:campoScheda campo="ID" visibile="false"/>
	<gene:campoScheda entita="W3ANNEXB" campo="ID" obbligatorio="true" visibile="false" where="W3FS20.ID = W3ANNEXB.ID AND W3ANNEXB.NUM = 1"/>
	<gene:campoScheda entita="W3ANNEXB" campo="NUM" obbligatorio="true" defaultValue="1" visibile="false" />
	<gene:campoScheda entita="W3ANNEXB" campo="LOTNUM" defaultValue="1" visibile="false" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VI")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.VI.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="ADDITIONAL_INFORMATION"/>
	
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VI.4")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.VI.4.1")}</b></td>
	</gene:campoScheda>
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3FS20.ADDRESS_REVIEW_BODY"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3FS20_ADDRESS_REVIEW_BODY">
		<gene:campoScheda campo="ADDRESS_REVIEW_BODY" visibile="false"/>
		<gene:campoScheda title = "Organismo/ufficio" campo="ADDRESS_REVIEW_BODY_NOMEIN" value="${requestScope.address_review_bodyNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>
	
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs20.VI.4.2")}</b></td>
	</gene:campoScheda>
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3FS20.ADDRESS_MEDIATION_BODY"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3FS20_ADDRESS_MEDIATION_BODY">
		<gene:campoScheda campo="ADDRESS_MEDIATION_BODY" visibile="false"/>
		<gene:campoScheda title = "Organismo/ufficio" campo="ADDRESS_MEDIATION_BODY_NOMEIN" value="${requestScope.address_mediation_bodyNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VI.4.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="REVIEW_PROCEDURE"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VI.4.4")}</b></td>
	</gene:campoScheda>	
	<gene:archivio titolo="Ufficio o punto di contatto"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3FS20.ADDRESS_REVIEW_INFO"),"gene/uffint/uffint-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","GENE.SchedaUffint"),"gene/uffint/uffint-scheda-popup.jsp","")}'
		 campi="UFFINT.CODEIN;UFFINT.NOMEIN"
		 chiave="W3FS20ADDRESS_REVIEW_INFO">
		<gene:campoScheda campo="ADDRESS_REVIEW_INFO" visibile="false"/>
		<gene:campoScheda title = "Organismo/ufficio" campo="ADDRESS_REVIEW_INFO_NOMEIN" value="${requestScope.address_review_infoNomein}" campoFittizio="true" definizione="T254" />
	</gene:archivio>
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs20.VI.5")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda entita="V_W3SIMAP" campo="NOTICE_DATE" where="V_W3SIMAP.ID = W3FS20.ID" modificabile="false"/>
	
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
