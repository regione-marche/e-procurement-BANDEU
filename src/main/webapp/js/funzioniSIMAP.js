
function popupSIMAPVALIDAZIONE(id) {
	openPopUpCustom("href=w3/commons/popup-validazione-simap.jsp&id=" + id, "validazioneSIMAP", 650, 650, "yes", "yes");
}
		
function inviaSIMAPEMAIL(id) {
	openPopUpCustom("href=w3/w3simapemail/w3simapemail-popup-componi.jsp&id=" + id, "componiSIMAP", 650, 550, "yes", "yes");
}

function inviaSIMAPWS(id) {
	openPopUpCustom("href=w3/commons/popup-avvia-invia-simapws.jsp&id=" + id, "avviainviaSIMAPWS", 650, 550, "yes", "yes");
}

function apriInvioSIMAP(id,num) {
	var key = "W3SIMAPEMAIL.ID=N:"+ id + ";W3SIMAPEMAIL.NUM=N:" + num;
	document.formSchedaW3SIMAPEMAIL.key.value=key;
	bloccaRichiesteServer();
	document.formSchedaW3SIMAPEMAIL.submit();
}

function inviiSIMAPEMAIL(id) {
	document.formListaW3SIMAPEMAIL.trovaAddWhere.value="W3SIMAPEMAIL.ID = ?";
	document.formListaW3SIMAPEMAIL.trovaParameter.value="N:" + id;
	bloccaRichiesteServer();
	document.formListaW3SIMAPEMAIL.submit();
}	

function inviiSIMAPWS(id) {
	document.formListaW3SIMAPWS.trovaAddWhere.value="W3SIMAPWS.ID = ?";
	document.formListaW3SIMAPWS.trovaParameter.value="N:" + id;
	bloccaRichiesteServer();
	document.formListaW3SIMAPWS.submit();
}

function componiW3FS14(id) {
	paginaFS14 = "w3/w3fs14/w3fs14-scheda.jsp";
	location.href = 'ApriPagina.do?href=' + paginaFS14 + '&modo=NUOVO&id_rif=' + id;	
}

function listaW3FS14(id_rif) {
	document.formListaW3FS14.trovaAddWhere.value="W3FS14.ID_RIF = ?";
	document.formListaW3FS14.trovaParameter.value="N:" + id_rif;
	bloccaRichiesteServer();
	document.formListaW3FS14.submit();
}	

function generaSIMAPDOC(id) {
	openPopUpCustom("href=w3/commons/popup-avvia-pdf-simapws.jsp&id=" + id, "avviapdfSIMAPWS", 650, 550, "yes", "yes");
}

function generatePdfTED(submissionId) {
	var action = "w3/GeneratePdfTED.do";
	document.location.href=action+'?submissionid=' + submissionId;
}

function _getStatoValidazioneSIMAP(id) {
	$.ajax({
        type: "GET",
        dataType: "json",
        async: true,
        beforeSend: function(x) {
		if(x && x.overrideMimeType) {
			x.overrideMimeType("application/json;charset=UTF-8");
	       }
		},
        url: "w3/GetStatoValidazioneSIMAP.do?id="+id,
        success: function(data){
        	if (data.numeroErrori > 0) {
        		var _contatoreerrorisimap = $("<span/>", {"id":"contatoreerrorisimap","class": "contatoreerrorisimap","title": "Numero errori bloccanti"});
        		_contatoreerrorisimap.text(data.numeroErrori);
				$("#menulateralecontrolladati").append(_contatoreerrorisimap);
				// aggiungiMessaggiControllo(data.listaControlli);	
        	} else {
        		$("#contatoreerrorisimap").remove();
        	}
        	
        	if (data.numeroWarning > 0) {
				var _contatorewarningsimap = $("<span/>", {"id":"contatorewarningsimap","class": "contatorewarningsimap","title": "Numero avvisi"});
				_contatorewarningsimap.text(data.numeroWarning);
				$("#menulateralecontrolladati").append(_contatorewarningsimap);
        	} else {
        		$("#contatorewarningsimap").remove();
        	}
        	
        }
    });
}


function _getStatoValidazioneW3ANNEXBSIMAP(id, num) {
//	$.ajax({
//        type: "GET",
//        dataType: "json",
//        async: true,
//        beforeSend: function(x) {
//		if(x && x.overrideMimeType) {
//			x.overrideMimeType("application/json;charset=UTF-8");
//	       }
//		},
//        url: "w3/GetStatoValidazioneW3ANNEXBSIMAP.do?id="+id + "&num=" + num,
//        success: function(data){
//        	if (data.numeroErrori > 0) {
//				aggiungiMessaggiControllo(data.listaControlli);	
//        	} 
//        }
//    });
}

function _getStatoValidazioneW3FS3AWARDSIMAP(id, item) {
//	$.ajax({
//        type: "GET",
//        dataType: "json",
//        async: true,
//        beforeSend: function(x) {
//		if(x && x.overrideMimeType) {
//			x.overrideMimeType("application/json;charset=UTF-8");
//	       }
//		},
//        url: "w3/GetStatoValidazioneW3FS3AWARDSIMAP.do?id="+id + "&item=" + item,
//        success: function(data){
//        	if (data.numeroErrori > 0) {
//				aggiungiMessaggiControllo(data.listaControlli);	
//        	} 
//        }
//    });	
}

function _getStatoValidazioneW3FS6AWARDSIMAP(id, item) {
	
	
}


function aggiungiMessaggiControllo(listaControlli) {
	$.each(listaControlli,function( index, value ) {
		var _sez = value[1];
		_sez = _sez.replace(/(\(|\)).*/g,"");
		$("table tr td").each(function(){						
			var _tx = $(this).text();
			_tx = _tx.replace(/(\(|\)).*/g,"");
			if (_tx == _sez) {
				
				var _div = $("<div/>");
				var _messaggio = $("<div/>");
				
				_div.css("padding","4 8 4 8");
				_div.css("vertical-align","middle");
				
				_messaggio.append(value[1]);
				_messaggio.append(" - ");
				_messaggio.append(value[2]);
				_messaggio.append(" - ");
				_messaggio.append(value[3]);
				
				_messaggio.css("background-color","#EAEAEA"); 
				_messaggio.css("font-style","italic");
				_messaggio.css("padding-left","5px");
				_messaggio.css("padding-right","5px");
				_messaggio.css("padding-top","2px");
				_messaggio.css("padding-bottom","2px");
				_messaggio.css("border-left","6px solid #D30000"); 
				_messaggio.css("border-right","1px solid #D3D3D3"); 
				_messaggio.css("border-top","1px solid #D3D3D3");
				_messaggio.css("border-bottom","1px solid #D3D3D3");
				_messaggio.css("-moz-border-radius-topleft","4px"); 
				_messaggio.css("-webkit-border-top-left-radius","4px"); 
				_messaggio.css("-khtml-border-top-left-radius","4px"); 
				_messaggio.css("border-top-left-radius","4px"); 
				_messaggio.css("-moz-border-radius-bottomleft","4px"); 
				_messaggio.css("-webkit-border-bottom-left-radius","4px"); 
				_messaggio.css("-khtml-border-bottom-left-radius","4px"); 
				_messaggio.css("border-bottom-left-radius","4px"); 
				_messaggio.css("-moz-border-radius-topright","4px");
				_messaggio.css("-webkit-border-top-right-radius","4px");
				_messaggio.css("-khtml-border-top-right-radius","4px");
				_messaggio.css("border-top-right-radius","4px");
				_messaggio.css("-moz-border-radius-bottomright","4px");
				_messaggio.css("-webkit-border-bottom-right-radius","4px");
				_messaggio.css("-khtml-border-bottom-right-radius","4px");
				_messaggio.css("border-bottom-right-radius","4px");
				
				_div.append(_messaggio);
				$(this).append(_div);
			
			}
		});
	});
}
