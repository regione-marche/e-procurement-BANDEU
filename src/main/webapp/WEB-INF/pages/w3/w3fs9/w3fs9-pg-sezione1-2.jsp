
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

<gene:formScheda entita="W3FS9" gestisciProtezioni="true"
	gestore="it.eldasoft.sil.w3.tags.gestori.submit.GestoreW3SIMAPW3FS9" >

	<gene:campoScheda campo="ID" visibile="false"/>
	
	<gene:campoScheda entita="W3SIMAP" campo="ID"  where="W3FS9.ID = W3SIMAP.ID" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="FORM" defaultValue="FS9" visibile="false"/>
	<gene:campoScheda entita="W3SIMAP" campo="CTYPE" visibile="false"/>

	<jsp:include page="/WEB-INF/pages/w3/w3simap/w3simap-pubblication-info.jsp"></jsp:include>

	<gene:campoScheda>
		<td colspan="2"><b><br>ANAC</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="IDGARA" />

	<gene:campoScheda>
		<td colspan="2"><b>&nbsp;</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="NOTICE_COVERED"/>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.I")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.I.1")}</b></td>
	</gene:campoScheda>
	<gene:archivio titolo="Amministrazione aggiudicatrice"
		 lista='${gene:if(gene:checkProt(pageContext, "COLS.MOD.W3.W3AMMI.CODAMM"),"w3/w3ammi/w3ammi-lista-popup.jsp","")}'
		 scheda='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda.jsp","")}'
		 schedaPopUp='${gene:if(gene:checkProtObj( pageContext, "MASC.VIS","W3AMMI-scheda"),"w3/w3ammi/w3ammi-scheda-popup.jsp","")}'
		 campi="W3AMMI.CODAMM;UFFINT.NOMEIN;UFFINT.NOMRES"
		 inseribile="false"
		 chiave="W3SIMAP_CODAMM">
		<gene:campoScheda entita="W3SIMAP" campo="CODAMM" />
		<gene:campoScheda visibile="true" modificabile="false" entita="UFFINT" campo="NOMEIN" from="W3AMMI, W3SIMAP, W3SIMAP" where="UFFINT.CODEIN = W3AMMI.CODEIN AND W3AMMI.CODAMM = W3SIMAP.CODAMM AND W3SIMAP.ID = W3FS9.ID" />
		<gene:campoScheda visibile="false" modificabile="false" entita="UFFINT" campo="NOMRES" from="W3AMMI, W3SIMAP, W3SIMAP" where="UFFINT.CODEIN = W3AMMI.CODEIN AND W3AMMI.CODAMM = W3SIMAP.CODAMM AND W3SIMAP.ID = W3FS2.ID" />
	</gene:archivio>
	<gene:campoScheda entita="W3SIMAP" campo="ATTENTION" />
	
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.II")}<br><br></b></td>
	</gene:campoScheda>
	<gene:campoScheda>
		<td colspan="2"><b>${gene:resource("label.simap.fs9.II.1")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TITLE_CONTRACT" obbligatorio="true"/>
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.II.2")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="TYPE_CONTRACT" obbligatorio="true"/>
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.II.3")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda campo="SHORT_DESCRIPTION" obbligatorio="true" />
	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.II.4")}</b></td>
	</gene:campoScheda>
	<gene:campoScheda title="Numero fittizio" campo="NUM" campoFittizio="true" definizione="N5" value="-1" visibile="false"/>	
	<jsp:include page="/WEB-INF/pages/w3/w3cpv/w3cpv-interno-scheda.jsp">
		<jsp:param name="ent" value='W3FS9'/>
		<jsp:param name="id" value='${gene:getValCampo(key, "ID")}'/>
		<jsp:param name="num" value='-1'/>
	</jsp:include>

	<gene:campoScheda>
		<td colspan="2"><b><br>${gene:resource("label.simap.fs9.II.5")}</b></td>
	</gene:campoScheda>	
	<gene:campoScheda campo="SCOPE_TOTAL"/>
	<gene:campoScheda campo="SCOPE_COST"/>
	<gene:campoScheda campo="SCOPE_LOW"/>
	<gene:campoScheda campo="SCOPE_HIGH"/>

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/permessi-blocca-scheda.jsp">
			<jsp:param name="tblname" value="W3SIMAP"/>
			<jsp:param name="clm1name" value="ID"/>
			<jsp:param name="clm1value" value="${datiRiga.W3FS9_ID}"/>
		</jsp:include>
	</gene:campoScheda>	

	<gene:campoScheda>
		<jsp:include page="/WEB-INF/pages/commons/pulsantiScheda.jsp" />
	</gene:campoScheda>
	
	<gene:fnJavaScriptScheda funzione="gestioneATTENTION('#UFFINT_NOMRES#')" elencocampi="UFFINT_NOMRES" esegui="false" />
	<gene:fnJavaScriptScheda funzione="gestioneTYPE_CONTRACT('#W3FS9_TYPE_CONTRACT#')" elencocampi="W3FS9_TYPE_CONTRACT" esegui="true" />
	
</gene:formScheda>

<gene:javaScript>

	showObj("jsPopUpUFFINT_NOMEIN", false);

	function gestioneATTENTION(value) {
		document.forms[0].W3SIMAP_ATTENTION.value=value;
	}

	function gestioneTYPE_CONTRACT(type_contract){
		document.forms[0].W3SIMAP_CTYPE.value=type_contract;
	}
	
</gene:javaScript>
