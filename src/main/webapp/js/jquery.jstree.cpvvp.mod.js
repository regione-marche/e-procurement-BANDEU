/*	
 * Albero gestione CPV del vocabolario principale
 * 
 * 
 */


CpvVPGrpDiv = [{"grpid": "g1", "grpdesc": "Agricoltura e alimentazione", "grpdiv" : ["03","15","77"]},
     {"grpid": "g2", "grpdesc": "Altri servizi", "grpdiv" : ["50","51","55","63","64","65","75","85","92","98"]},
     {"grpid": "g3", "grpdesc": "Ambiente e risanamento", "grpdiv" : ["41","90"]},
     {"grpid": "g4", "grpdesc": "Difesa e sicurezza", "grpdiv" : ["31","35","45","50","51","72","73","75","80"]},
     {"grpid": "g5", "grpdesc": "Edilizia e proprietà immobiliare", "grpdiv" : ["45","70","71","79","90"]},
     {"grpid": "g6", "grpdesc": "Energia e relativi servizi", "grpdiv" : ["09","76"]},
     {"grpid": "g7", "grpdesc": "Estrazione e minerali", "grpdiv" : ["14","44"]},
     {"grpid": "g8", "grpdesc": "Finanza e relativi servizi", "grpdiv" : ["66"]},
     {"grpid": "g9", "grpdesc": "Informatica e relativi servizi", "grpdiv" : ["30","48","72"]},
     {"grpid": "g10", "grpdesc": "Istruzione", "grpdiv" : ["80"]},
     {"grpid": "g11", "grpdesc": "Materiali e prodotti", "grpdiv" : ["03","09","14","15","18","19","22","24","30","31","32","33","34","35","37","39","44"]},
     {"grpid": "g12", "grpdesc": "Ricerca e sviluppo", "grpdiv" : ["73"]},
     {"grpid": "g13", "grpdesc": "Stampa ed editoria", "grpdiv" : ["22","30","79"]},
     {"grpid": "g14", "grpdesc": "Tecnologia e attrezzature", "grpdiv" : ["16","30","31","32","33","34","35","38","39","42","43","48"]},
     {"grpid": "g15", "grpdesc": "Trasporti e relativi servizi", "grpdiv" : ["34","60","63"]}]
 

/*
 * Ritardo.
 */
var delay = (function(){
	  var timer = 0;
	  return function(callback, ms){
	    clearTimeout (timer);
	    timer = setTimeout(callback, ms);
	  };
})();



 /*
  * Crea finestra modale per il popolamento dell'albero
  */
 function _creaFinestraAlberoCpvVP() {
	var _finestraalberocpvvp = $("<div/>",{"id": "finestraalberocpvvp", "title":"Dettaglio codice CPV (Vocabolario principale)"});
	_finestraalberocpvvp.dialog({
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
   		width: 900,
   		modal: true,
   		focusCleanup: true,
   		cache: false,
   		position: { my: "top", at: "top+100"},
        buttons: {
        "Chiudi" : function() {
           		$(this).dialog( "close" );
        	}
        }
	});
 	
	// Messaggio
	var _divm =  $("<div/>",{"class": "cpvvpmessaggio", "text": "(*) Solo i codici evidenziati con lo sfondo colorato possono essere selezionati"});
	$("#finestraalberocpvvp").parent().find(".ui-dialog-buttonpane").append(_divm);
	
	_finestraalberocpvvp.on( "dialogclose", function( event, ui ) {
		$("#cpvvpcontainer").remove();
	});
} 
 

/*
 * Crea il link e l'evento associato per richiamare
 * la finestra modale contenente l'albero dei CPV
 */
function _creaLinkAlberoCpvVP(anchor, modo, cpvvpref, cpvvprefview) {
	setTimeout(function(){
		
		if (modo == "VISUALIZZA" && cpvvpref.val() != null && cpvvpref.val() != "") {
			cpvvprefview.parent().click(function(event) {
				event.preventDefault();
				_popolaFinestraAlberoCpvVP(modo, cpvvpref, cpvvprefview);
			});
		}
		
		if (modo != "VISUALIZZA") {
			var _jsPopUpLinkSet = "linksetJsPopUp" + cpvvpref.attr("id");
			var _idlinkcpvvp = cpvvpref.attr("id") + "_linkcpvvp";
			var _idlinkdelcpvvp = cpvvpref.attr("id") + "_linkdelcpvvp";
			
			window[_jsPopUpLinkSet] = creaVocePopUpChiusura("");
			
			var _divCMod = $("<div/>");
			var _divMod = $("<div/>",{"class":"menuitems"});
			var _aMod = $("<a/>",{"id": _idlinkcpvvp, "text":"Dettaglio CPV", "href": "#", "title": "Dettaglio CPV (Vocabolario principale)"});
			_divMod.append("&nbsp;").append(_aMod);
			_divCMod.append(_divMod);
			window[_jsPopUpLinkSet] += _divCMod.html();

			var _divCDel = $("<div/>");
			var _divDel = $("<div/>",{"class":"menuitems"});
			var _aDel = $("<a/>",{"id": _idlinkdelcpvvp, "text":"Cancella CPV", "href": "#", "title": "Cancella CPV"});
			_divDel.append("&nbsp;").append(_aDel);
			_divCDel.append(_divDel);
			window[_jsPopUpLinkSet] += _divCDel.html();
			
			$("body").delegate("#" + _idlinkcpvvp, "click" ,function(event) {
				event.preventDefault();
				_popolaFinestraAlberoCpvVP(modo, cpvvpref, cpvvprefview);
			});

			$("body").delegate("#" + _idlinkdelcpvvp, "click" ,function(event) {
				event.preventDefault();
				cpvvpref.val("");
				cpvvpref.change();
				cpvvprefview.text("");
			});
		}
		
	}, 300);
} 
 

/*
 * Crea la finestra (modale) con l'albero dei CPV.
 */
function _popolaFinestraAlberoCpvVP(modo, cpvvpref, cpvvprefview) {
	
	$("#finestraalberocpvvp").dialog("open");
	
	var _table = $("<table/>", {"id": "cpvvpcontainer", "class": "cpvvpcontainer"});
	
	// Ricerca
	var _tr = $("<tr/>");
	var _td =  $("<td/>",{"class": "etichetta", "text": "Ricerca"});
	_tr.append(_td);
	var _td = $("<td/>");
	var _cpvvprefval = cpvvpref.val();
	_td.append($("<input/>",{"id": "cpvvptextsearch", "type": "text", "size": "30", "value": _cpvvprefval}));
	_td.append($("<span/>",{"id":"cpvvpdeletesearch", "class": "cpvvpdeletesearch", "title": "Elimina ricerca"}));
	_tr.append(_td);
	_table.append(_tr);
	
	// Albero
	var _tr = $("<tr/>");
	var _td =  $("<td/>",{"colspan": "2"});
	_td.append($("<span/>",{"class": "openfolder"}));
	var _spancpvvp = $("<span/>",{"text":"CPV del vocabolario principale"});
	_td.append(_spancpvvp);   
	var _divcpvvptree = $("<div/>",{"id":"cpvvptree", "class": "cpvvptree"});
	_td.append(_divcpvvptree);
	_tr.append(_td);
	_table.append(_tr);
	
	$("#finestraalberocpvvp").append(_table);

	$('#cpvvpdeletesearch').click(function() {
		clearSearchCpvVP();
	});

	$('#cpvvptextsearch').keyup(function() {
		delay(function(){
			searchCpvVP();
    	}, 600);
	});

	if (modo == "VISUALIZZA") {
		$('#cpvvptextsearch').attr('readonly','readonly');
		$('#cpvvptextsearch').attr('tabindex','-1');
		$('#cpvvptextsearch').css('border-color','#A3A6FF');
		$('#cpvvptextsearch').css('border-width','1px');
		$('#cpvvptextsearch').css('background-color','#EFEFEF');
		$('#cpvvpdeletesearch').hide();
	}
	
	_caricaAlberoCpvVP(modo, cpvvpref, cpvvprefview);

}	




/*
 * Associazione eventi all'albero dei CPV
 */
function _associaEventiAlberoCpvVP(modo, cpvvpref, cpvvprefview) {
	
	// Associa il menu' alla selezione del nodo
    $('#cpvvptree').bind('select_node.jstree', function(e,data) {
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
					$('#cpvvptree').jstree("show_contextmenu",'#' + id, x, y);
				}
			}, 100);
		}
    });
}


/*
 * Creazione e caricamento dell'albero dei CPV
 */
function _caricaAlberoCpvVP(modo, cpvvpref, cpvvprefview) {
	
	var init_livello;
	if (modo == "VISUALIZZA") {
		init_livello = "1";
	} else {
		init_livello = "1";
	}
	
    $("#cpvvptree").jstree(
		{ 
			"core" : {
				"html_titles" : true,
				"animation" : 100
			},
			"plugins" : [ "themes", "json_data", "ui", "types", "search", "crrm", "contextmenu"],
			"themes" : { "theme" : "classic", "url" : "css/jquery/jstree/themes/classic/style.css"  },
			"ui" : { "select_limit" : 1	},
			"contextmenu" : { 
				"items" : CpvVPMenu,
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
	                url: "w3/GestioneAlberoCpvVP.do",
	                data : function (n) { 
						return {
							cpvvpgrpdiv: JSON.stringify(CpvVPGrpDiv),
							livello: n.attr ? n.attr("livello") : init_livello,
							id : n.attr ? n.attr("id") : "",									
							cpvvpcod : n.attr ? n.attr("cpvvpcod") : "",
							textsearch: $("#cpvvptextsearch").val()
						};	
					},
	                success: function( data ) {
	                	CpvVPArray = [];
	                	if (data && data.length > 0) {
							$.map( data, function( item ) {
								
								var descrizione = "";
								if (item[0] > 1) descrizione += item[2] + " - ";
								descrizione += item[3];
								if ($("#cpvvptextsearch").val() != null && $("#cpvvptextsearch").val() != "") {
									if (modo != "VISUALIZZA") {
										descrizione += "&nbsp;[" + item[5] + "]";
									}
								}

								var statonodo = "";
								if (item[4] == 0) {
									statonodo = "";	
								} else {
									statonodo = "closed";
								}
								
								var tiponodo = "";
								if (item[0] > 2) {
									tiponodo = "CPV";
								} else {
									tiponodo = "GRP";
								}
								
								CpvVPItem = {
									"data" : descrizione,	
									"attr" : {
										"tiponodo": tiponodo,
										"livello" : item[0],
										"id": item[1],
										"cpvvpcod": item[2],
										"cpvvpdesc" : item[3],
										"cntcpv": item[5]
									},
									"title" : item[1],
									"state" : statonodo
								},
								CpvVPArray.push(CpvVPItem);
							});
						} 
						return CpvVPArray;
					},
					complete: function() {
						var cntcpvtotali = 0;
						var grp = $("#cpvvptree").find("li[livello='2']");
						grp.each(function () { 
				        	cntcpvtotali += parseInt($(this).attr("cntcpv"));
				        });
				        if (cntcpvtotali > 0 && cntcpvtotali <= 10) {
				        	$("#cpvvptree").jstree("open_all");
				        }	
					}
				}
			}
		}
	);

    /*
     * Menu' personalizzato
     */
    function CpvVPMenu(node) {
    	if (modo != "VISUALIZZA" && node.attr("livello") > 2) {
	        var items = {
	        	selItem: {
	           		label: "Seleziona CPV e chiudi",
		           		action: function (obj) { _selcpvvp(node, cpvvpref, cpvvprefview); },
		           		_disabled : false
		            }
			    };
	        }
	        return items;
	    };
	
	/*
	 * Associazione eventi all'albero appena creato
	 */
	 _associaEventiAlberoCpvVP(modo, cpvvpref, cpvvprefview);
	 
}    
	    
	    
/*
 * Selezione del CPV
 */
function _selcpvvp(node, cpvvpref, cpvvprefview) {
	var cpvvpcod = node.attr("cpvvpcod");
	cpvvpref.val(cpvvpcod);
	cpvvpref.change();
	cpvvprefview.text(cpvvpcod);
	$("#finestraalberocpvvp").dialog( "close" );
}


/*
 * Pulizia parametri di ricerca
 */
function clearSearchCpvVP() {
	if ($("#cpvvptextsearch").val() != null && $("#cpvvptextsearch").val() != "") {
		$("#cpvvptextsearch").val(null);
		$("#cpvvptree").jstree("close_all");
		$("#cpvvptree").jstree("refresh","-1");
	}
};


/*
 * Ricerca per descrizione (multipla)
 */
function searchCpvVP() {
	$("#cpvvptree").jstree("close_all");
	$("#cpvvptree").jstree("refresh","-1");
};

	

