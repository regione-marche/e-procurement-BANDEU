/*	
 * Albero gestione CPV del vocabolario supplementare
 * 
 * 
 */
 

 /*
  * Crea finestra modale per il popolamento dell'albero
  */
 function _creaFinestraAlberoCpvVSUPP() {
	var _finestraalberocpvvsupp = $("<div/>",{"id": "finestraalberocpvvsupp", "title":"Dettaglio codice CPV (Vocabolario supplementare)"});
	_finestraalberocpvvsupp.dialog({
		open: function(event, ui) { 
			$(this).parent().css("background","#FFFFFF");
			$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
			$(this).parent().css("border-color","#C0C0C0");
			var _divtitlebar = $(this).parent().find("div.ui-dialog-titlebar");
	    	_divtitlebar.css("border","0px");
	    	_divtitlebar.css("background","#FFFFFF");
	    	$(this).parent().find("span.ui-dialog-title").addClass("cpvvptitolo");
	    	$(this).parent().find("div.ui-dialog-buttonpane").css("background","#FFFFFF");
		},
   		autoOpen: false,
       	show: {
       		effect: "blind",
       		duration: 350
           },
        hide: {
           	effect: "blind",
           	duration: 350
        },
   		resizable: false,
   		height: 500,
   		width: 850,
   		modal: true,
   		focusCleanup: true,
   		position: { my: "top", at: "top+100"},
   		cache: false,
        buttons: {
        "Chiudi" : function() {
           		$(this).dialog( "close" );
        	}
        }
	});
 	
	// Messaggio
	var _divm =  $("<div/>",{"class": "cpvvpmessaggio", "text": "(*) Solo i codici evidenziati con lo sfondo colorato possono essere selezionati"});
	$("#finestraalberocpvvsupp").parent().find(".ui-dialog-buttonpane").append(_divm);
	
	_finestraalberocpvvsupp.on( "dialogclose", function( event, ui ) {
		$("#cpvvsuppcontainer").remove();
	});
} 
 

/*
 * Crea il link e l'evento associato per richiamare
 * la finestra modale contenente l'albero dei CPV
 */
function _creaLinkAlberoCpvVSUPP(anchor, modo, cpvvsuppref, cpvvsupprefview) {
	setTimeout(function(){
		
		if (modo == "VISUALIZZA" && cpvvsuppref.val() != null && cpvvsuppref.val() != "") {
			cpvvsupprefview.parent().click(function(event) {
				event.preventDefault();
				_popolaFinestraAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview);
			});
		}
		
		if (modo != "VISUALIZZA") {
			var _jsPopUpLinkSet = "linksetJsPopUp" + cpvvsuppref.attr("id");
			var _idlinkcpvvsupp = cpvvsuppref.attr("id") + "_linkcpvvsupp";
			var _idlinkdelcpvvsupp = cpvvsuppref.attr("id") + "_linkdelcpvvsupp";
			
			window[_jsPopUpLinkSet] = creaVocePopUpChiusura("");
			
			var _divCMod = $("<div/>");
			var _divMod = $("<div/>",{"class":"menuitems"});
			var _aMod = $("<a/>",{"id": _idlinkcpvvsupp, "text":"Dettaglio CPV", "href": "#", "title": "Dettaglio CPV (Vocabolario supplementare)"});
			_divMod.append("&nbsp;").append(_aMod);
			_divCMod.append(_divMod);
			window[_jsPopUpLinkSet] += _divCMod.html();

			var _divCDel = $("<div/>");
			var _divDel = $("<div/>",{"class":"menuitems"});
			var _aDel = $("<a/>",{"id": _idlinkdelcpvvsupp, "text":"Cancella CPV", "href": "#", "title": "Cancella CPV"});
			_divDel.append("&nbsp;").append(_aDel);
			_divCDel.append(_divDel);
			window[_jsPopUpLinkSet] += _divCDel.html();
			
			$("body").delegate("#" + _idlinkcpvvsupp, "click" ,function(event) {
				event.preventDefault();
				_popolaFinestraAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview);
			});

			$("body").delegate("#" + _idlinkdelcpvvsupp, "click" ,function(event) {
				event.preventDefault();
				cpvvsuppref.val("");
				cpvvsuppref.change();
				cpvvsupprefview.text("");
			});
		}

	}, 300);
} 
 

/*
 * Crea la finestra (modale) con l'albero dei CPV.
 */
function _popolaFinestraAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview) {
	
	$("#finestraalberocpvvsupp").dialog("open");
	
	var _table = $("<table/>", {"id": "cpvvsuppcontainer", "class": "cpvvpcontainer"});
	
	// Ricerca
	var _tr = $("<tr/>");
	var _td =  $("<td/>",{"class": "etichetta", "text": "Ricerca"});
	_tr.append(_td);
	var _td = $("<td/>");
	var _cpvvsupprefval = cpvvsuppref.val();
	_td.append($("<input/>",{"id": "cpvvsupptextsearch", "type": "text", "size": "30", "value": _cpvvsupprefval}));
	_td.append($("<span/>",{"id":"cpvvsuppdeletesearch", "class": "cpvvpdeletesearch", "title": "Elimina ricerca"}));
	_tr.append(_td);
	_table.append(_tr);
	
	// Albero
	var _tr = $("<tr/>");
	var _td =  $("<td/>",{"colspan": "2"});
	_td.append($("<span/>",{"class": "openfolder"}));
	var _spancpvvsupp = $("<span/>",{"text":"CPV del vocabolario supplementare"});
	_td.append(_spancpvvsupp);   
	var _divcpvvsupptree = $("<div/>",{"id":"cpvvsupptree", "class": "cpvvptree"});
	_td.append(_divcpvvsupptree);
	_tr.append(_td);
	_table.append(_tr);
	
	$("#finestraalberocpvvsupp").append(_table);

	$('#cpvvsuppdeletesearch').click(function() {
		clearSearchCpvVSUPP();
	});

	$('#cpvvsupptextsearch').keyup(function() {
		delay(function(){
			searchCpvVSUPP();
    	}, 600);
	});

	if (modo == "VISUALIZZA") {
		$('#cpvvsupptextsearch').attr('readonly','readonly');
		$('#cpvvsupptextsearch').attr('tabindex','-1');
		$('#cpvvsupptextsearch').css('border-color','#A3A6FF');
		$('#cpvvsupptextsearch').css('border-width','1px');
		$('#cpvvsupptextsearch').css('background-color','#EFEFEF');
		$('#cpvvsuppdeletesearch').hide();
	}
	
	_caricaAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview);

}	




/*
 * Associazione eventi all'albero dei CPV
 */
function _associaEventiAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview) {
	
	// Associa il menu' alla selezione del nodo
    $('#cpvvsupptree').bind('select_node.jstree', function(e,data) {
		if (data.rslt.obj.attr("id")) {
			setTimeout(function(){
				var x;
				var y;
				if (data.args.length > 2) {
					x = data.args[2].pageX;
					y = data.args[2].pageY;
					var id = data.rslt.obj.attr('id');
					id = id.replace(/\./g,'\\.');
					id = id.replace(/\//g,'\\/');
					$('#cpvvsupptree').jstree("show_contextmenu",'#' + id, x, y);
				}
			}, 100);
		}
    });
}


/*
 * Creazione e caricamento dell'albero dei CPV
 */
function _caricaAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview) {
	
    $("#cpvvsupptree").jstree(
		{ 
			"core" : {
				"html_titles" : true,
				"animation" : 100
			},
			"plugins" : [ "themes", "json_data", "ui", "types", "search", "crrm", "contextmenu"],
			"themes" : { "theme" : "classic", "url" : "css/jquery/jstree/themes/classic/style.css"  },
			"ui" : { "select_limit" : 1	},
			"contextmenu" : { 
				"items" : CpvVSUPPMenu,
				"show_at_node" : false
			},
			"types" : {
				"type_attr" : "tiponodo"
			},
			"json_data" : { 
				"ajax" : {
					async: true,
				    type: "POST",
	                dataType: "json",
	                beforeSend: function(x) {
	    			if(x && x.overrideMimeType) {
	        			x.overrideMimeType("application/json;charset=UTF-8");
				       }
					},
	                url: "w3/GestioneAlberoCpvVSUPP.do",
	                data : function (n) { 
						return {
							livello: n.attr ? n.attr("livello") : "1",
							cpvvsuppsezione : n.attr ? n.attr("cpvvsuppsezione") : "",
							cpvvsuppgruppo : n.attr ? n.attr("cpvvsuppgruppo") : "",
							cpvvsuppdivisione : n.attr ? n.attr("cpvvsuppdivisione") : "",
							textsearch: $("#cpvvsupptextsearch").val()
						};	
					},
	                success: function( data ) {
	                	CpvVSUPPArray = [];
	                	if (data && data.length > 0) {
							$.map( data, function( item ) {
								
								var descrizione = "";
								if (item[0] > 1) descrizione += item[5] + " - ";
								descrizione += item[6];
								if ($("#cpvvsupptextsearch").val() != null && $("#cpvvsupptextsearch").val() != "") {
									if (modo != "VISUALIZZA") {
										descrizione += "&nbsp;[" + item[8] + "]";
									}
								}

								var statonodo = "";
								if (item[7] == 0) {
									statonodo = "";	
								} else {
									statonodo = "closed";
								}
								
								var tiponodo = "";
								if (item[0] > 3) {
									tiponodo = "CPV";
								} else {
									tiponodo = "GRP";
								}
								
								CpvVSUPPItem = {
									"data" : descrizione,	
									"attr" : {
										"tiponodo": tiponodo,
										"livello" : item[0],
										"id": item[1],
										"cpvvsuppsezione": item[2],
										"cpvvsuppgruppo": item[3],
										"cpvvsuppdivisione": item[4],
										"cpvvsuppcodice": item[5],
										"cpvvsuppdescrizione" : item[6],
										"cntcpvvsupp": item[8]
									},
									"title" : item[1],
									"state" : statonodo
								},
								CpvVSUPPArray.push(CpvVSUPPItem);
							});
						} 
						return CpvVSUPPArray;
					},
					complete: function() {
						var cntcpvsupptotali = 0;
						var grp = $("#cpvvsupptree").find("li[livello='2']");
						grp.each(function () { 
				        	cntcpvsupptotali += parseInt($(this).attr("cntcpvvsupp"));
				        });
				        if (cntcpvsupptotali > 0 && cntcpvsupptotali <= 10) {
				        	$("#cpvvsupptree").jstree("open_all");
				        }	
					}
				}
			}
		}
	);

    /*
     * Menu' personalizzato
     */
    function CpvVSUPPMenu(node) {
    	if (modo != "VISUALIZZA" && node.attr("livello") > 3) {
	        var items = {
	        	selItem: {
	           		label: "Seleziona CPV e chiudi",
		           		action: function (obj) { _selcpvvsupp(node, cpvvsuppref, cpvvsupprefview); },
		           		_disabled : false
		            }
			    };
	        }
	     return items;
	};
	
	/*
	 * Associazione eventi all'albero appena creato
	 */
	 _associaEventiAlberoCpvVSUPP(modo, cpvvsuppref, cpvvsupprefview);
	 
}    
	    
	    
/*
 * Selezione del CPV
 */
function _selcpvvsupp(node, cpvvsuppref, cpvvsupprefview) {
	var cpvvsuppcodice = node.attr("cpvvsuppcodice");
	cpvvsuppref.val(cpvvsuppcodice);
	cpvvsuppref.change();
	cpvvsupprefview.text(cpvvsuppcodice);
	$("#finestraalberocpvvsupp").dialog( "close" );
}


/*
 * Pulizia parametri di ricerca
 */
function clearSearchCpvVSUPP() {
	if ($("#cpvvsupptextsearch").val() != null && $("#cpvvsupptextsearch").val() != "") {
		$("#cpvvsupptextsearch").val(null);
		$("#cpvvsupptree").jstree("close_all");
		$("#cpvvsupptree").jstree("refresh","-1");
	}
};


/*
 * Ricerca per descrizione (multipla)
 */
function searchCpvVSUPP() {
	$("#cpvvsupptree").jstree("close_all");
	$("#cpvvsupptree").jstree("refresh","-1");
};

	

