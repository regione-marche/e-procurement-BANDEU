/*	
 * Albero gestione CPV del vocabolario supplementare
 * 
 * 
 */
 

 /*
  * Crea finestra modale per il popolamento dell'albero
  */
 function _creaFinestraAlberoNUTS() {
	var _finestraalberonuts = $("<div/>",{"id": "finestraalberonuts", "title":"Dettaglio codice NUTS"});
	_finestraalberonuts.dialog({
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
   		width: 500,
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
	
	_finestraalberonuts.on( "dialogclose", function( event, ui ) {
		$("#nutscontainer").remove();
	});
} 
 

/*
 * Crea il link e l'evento associato per richiamare
 * la finestra modale contenente l'albero dei NUTS
 */
function _creaLinkAlberoNUTS(anchor, modo, nutsref, nutsrefview) {
	setTimeout(function(){
		
		if (modo == "VISUALIZZA" && nutsref.val() != null && nutsref.val() != "") {
			nutsrefview.parent().click(function(event) {
				event.preventDefault();
				_popolaFinestraAlberoNUTS(modo, nutsref, nutsrefview);
			});
		}
		
		if (modo != "VISUALIZZA") {
			var _jsPopUpLinkSet = "linksetJsPopUp" + nutsref.attr("id");
			var _idlinknuts = nutsref.attr("id") + "_linknuts";
			var _idlinkdelnuts = nutsref.attr("id") + "_linkdelnuts";
			
			window[_jsPopUpLinkSet] = creaVocePopUpChiusura("");
			
			var _divCMod = $("<div/>");
			var _divMod = $("<div/>",{"class":"menuitems"});
			var _aMod = $("<a/>",{"id": _idlinknuts, "text":"Dettaglio NUTS", "href": "#", "title": "Dettaglio NUTS"});
			_divMod.append("&nbsp;").append(_aMod);
			_divCMod.append(_divMod);
			window[_jsPopUpLinkSet] += _divCMod.html();

			var _divCDel = $("<div/>");
			var _divDel = $("<div/>",{"class":"menuitems"});
			var _aDel = $("<a/>",{"id": _idlinkdelnuts, "text":"Cancella NUTS", "href": "#", "title": "Cancella NUTS"});
			_divDel.append("&nbsp;").append(_aDel);
			_divCDel.append(_divDel);
			window[_jsPopUpLinkSet] += _divCDel.html();
			
			$("body").delegate("#" + _idlinknuts, "click" ,function(event) {
				event.preventDefault();
				_popolaFinestraAlberoNUTS(modo, nutsref, nutsrefview);
			});

			$("body").delegate("#" + _idlinkdelnuts, "click" ,function(event) {
				event.preventDefault();
				nutsref.val("");
				nutsref.change();
				nutsrefview.text("");
			});
		}

	}, 300);
} 
 

/*
 * Crea la finestra (modale) con l'albero dei codici NUTS.
 */
function _popolaFinestraAlberoNUTS(modo, nutsref, nutsrefview) {
	
	$("#finestraalberonuts").dialog("open");
	
	var _table = $("<table/>", {"id": "nutscontainer", "class": "cpvvpcontainer"});
	
	// Ricerca
	var _tr = $("<tr/>");
	var _td =  $("<td/>",{"class": "etichetta", "text": "Ricerca"});
	_tr.append(_td);
	var _td = $("<td/>");
	var _nutsrefval = nutsref.val();
	_td.append($("<input/>",{"id": "nutstextsearch", "type": "text", "size": "20", "value": _nutsrefval}));
	_td.append($("<input/>",{"id": "nutscodsearch", "type": "hidden", "size": "20", "value": _nutsrefval}));
	_td.append($("<span/>",{"id":"nutsdeletesearch", "class": "cpvvpdeletesearch", "title": "Elimina ricerca"}));
	_tr.append(_td);
	_table.append(_tr);
	
	// Albero
	var _tr = $("<tr/>");
	var _td =  $("<td/>",{"colspan": "2"});
	_td.append($("<span/>",{"class": "openfolder"}));
	var _spannuts = $("<span/>",{"text":"Codici NUTS"});
	_td.append(_spannuts);   
	var _divnutstree = $("<div/>",{"id":"nutstree", "class": "cpvvptree", "width" : "450"});
	_td.append(_divnutstree);
	_tr.append(_td);
	_table.append(_tr);
	
	$("#finestraalberonuts").append(_table);

	$('#nutsdeletesearch').click(function() {
		clearSearchNUTS();
	});

	$('#nutstextsearch').keyup(function() {
		delay(function(){
			searchNUTS();
    	}, 600);
	});

	if (modo == "VISUALIZZA") {
		$('#nutstextsearch').attr('readonly','readonly');
		$('#nutstextsearch').attr('tabindex','-1');
		$('#nutstextsearch').css('border-color','#A3A6FF');
		$('#nutstextsearch').css('border-width','1px');
		$('#nutstextsearch').css('background-color','#EFEFEF');
		$('#nutsdeletesearch').hide();
	}
	
	_caricaAlberoNUTS(modo, nutsref, nutsrefview);

}	




/*
 * Associazione eventi all'albero dei codice NUTS
 */
function _associaEventiAlberoNUTS(modo, nutsref, nutsrefview) {
	
	// Associa il menu' alla selezione del nodo
    $('#nutstree').bind('select_node.jstree', function(e,data) {
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
					$('#nutstree').jstree("show_contextmenu",'#' + id, x, y);
				}
			}, 100);
		}
    });
}


/*
 * Creazione e caricamento dell'albero dei codice NUTS
 */
function _caricaAlberoNUTS(modo, nutsref, nutsrefview) {
	
    $("#nutstree").jstree(
		{ 
			"core" : {
				"html_titles" : true,
				"animation" : 100
			},
			"plugins" : [ "themes", "json_data", "ui", "types", "search", "crrm", "contextmenu"],
			"themes" : { "theme" : "classic", "url" : "css/jquery/jstree/themes/classic/style.css"  },
			"ui" : { "select_limit" : 1	},
			"contextmenu" : { 
				"items" : NUTSMenu,
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
	                url: "w3/GestioneAlberoNUTS.do",
	                data : function (n) { 
						return {
							livello: n.attr ? n.attr("livello") : "1",
							paese : n.attr ? n.attr("paese") : "",
							area : n.attr ? n.attr("area") : "",
							regione : n.attr ? n.attr("regione") : "",
							provincia : n.attr ? n.attr("provincia") : "",
							textsearch: $("#nutstextsearch").val(),
							codsearch: $("#nutscodsearch").val(),
							modopagina: modo
						};	
					},
	                success: function( data ) {
	                	NUTSArray = [];
	                	if (data && data.length > 0) {
							$.map( data, function( item ) {
								
								var descrizione = "";
								if (item[0] > 1) descrizione += item[6] + " - ";
								descrizione += item[7];
								if ($("#nutstextsearch").val() != null && $("#nutstextsearch").val() != "") {
									if (modo != "VISUALIZZA") {
										descrizione += "&nbsp;[" + item[9] + "]";
									}
								}
								
								var statonodo = "";
								if (item[8] == 0) {
									statonodo = "";	
								} else {
									statonodo = "closed";
								}

								var	tiponodo = "GRP";
								
								NUTSItem = {
									"data" : descrizione,	
									"attr" : {
										"tiponodo": tiponodo,
										"livello" : item[0],
										"id": item[1],
										"paese": item[2],
										"area": item[3],
										"regione": item[4],
										"provincia": item[5],
										"codice": item[6],
										"descrizione" : item[7],
										"cntnuts": item[9]
									},
									"title" : item[1],
									"state" : statonodo
								},
								NUTSArray.push(NUTSItem);
							});
						} 
						return NUTSArray;
					},
					complete: function() {
						var cntnutstotali = 0;
						var grp = $("#nutstree").find("li[livello='2']");
						grp.each(function () { 
				        	cntnutstotali += parseInt($(this).attr("cntnuts"));
				        });
				        if (cntnutstotali > 0 && cntnutstotali <= 10) {
				        	$("#nutstree").jstree("open_all");
				        }	
					}
				}
			}
		}
	);

    /*
     * Menu' personalizzato
     */
    function NUTSMenu(node) {
    	if (modo != "VISUALIZZA" && node.attr("livello") > 1) {
	        var items = {
	        	selItem: {
	           		label: "Seleziona NUTS e chiudi",
		           		action: function (obj) { _selNUTS(node, nutsref, nutsrefview); },
		           		_disabled : false
		            }
			    };
	        }
	     return items;
	};
	
	/*
	 * Associazione eventi all'albero appena creato
	 */
	 _associaEventiAlberoNUTS(modo, nutsref, nutsrefview);
	 
}    
	    
	    
/*
 * Selezione del codice NUTS
 */
function _selNUTS(node, nutsref, nutsrefview) {
	var nutscodice = node.attr("codice");
	nutsref.val(nutscodice);
	nutsref.change();
	nutsrefview.text(nutscodice);
	$("#finestraalberonuts").dialog( "close" );
}


/*
 * Pulizia parametri di ricerca
 */
function clearSearchNUTS() {
	$("#nutstextsearch").val(null);
	$("#nutscodsearch").val(null);
	$("#nutstree").jstree("close_all");
	$("#nutstree").jstree("refresh","-1");
};


/*
 * Ricerca per descrizione (multipla)
 */
function searchNUTS() {
	$("#nutscodsearch").val(null);
	$("#nutstree").jstree("close_all");
	$("#nutstree").jstree("refresh","-1");
};

	

