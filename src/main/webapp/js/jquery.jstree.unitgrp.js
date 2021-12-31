/*	
 * Albero delle unita' organizzative e dei gruppi.
 * Operazioni disponibili:
 * 
 * load - caricamento
 * search - ricerca
 * delete - cancellazione
 * 
 */


$(window).on("load", function (){
	
	var lunghezzamassimadescrizione = 90;
	
    $('#deletesearch').click(function() {
    	clearSearchUnitGrp();
    	$("#messaggioricerca").html("");
    });
    
    $('#textsearch').keyup(function() {
    	delay(function(){
    		searchUnitGrp();
    	}, 600);
    });
  
    
	/*
	 * Ad ogni apertura di un nodo evidenzia la stringa cercata.
	 */
	$("#unitgrptree").bind("open_node.jstree", function (e, data) {
		if ($("#textsearch").val() != null && $("#textsearch").val() != "")
		{
			var words = $("#textsearch").val().split(' ');
			for (var i = 0; i < words.length; i++) {
				word = words[i];
				if (word != "" && word != " " && word.length > 2) {
					$('#unitgrptree ul li').highlight(word);
				}
			}
		}
	});	

	$("#unitgrptree").bind("before.jstree", function (e, data) {
		if ($("#textsearch").val() != null && $("#textsearch").val() != "") {
			var visualizzamessaggio = false;
			var words = $("#textsearch").val().split(' ');
			for (var i = 0; i < words.length; i++) {
				word = words[i];
				if (word != "" && word != " " && word.length > 2 && visualizzamessaggio == false) {
					visualizzamessaggio = true;
				}
			}
			if (visualizzamessaggio == true) {
				searchMessaggio();
			} else {
				$("#messaggioricerca").html("Indicare almeno tre caratteri.");
			}
		}
	});

	
	/*
	 * Apro il menu', gia' disponibile con il tasto destro del mouse, 
	 * alla selezione del nodo.
	 */
    $('#unitgrptree').bind('select_node.jstree', function(e,data) {
		if (data.rslt.obj.attr("id")) {
			delay(function(){
				var x;
				var y;
				if (data.args.length > 2) {
					x = data.args[2].pageX;
					y = data.args[2].pageY;
					var id = data.rslt.obj.attr('id');
					id = id.replace(/\./g,'\\.');
					id = id.replace(/\//g,'\\/');
					$('#unitgrptree').jstree("show_contextmenu",'#' + id, x, y);
				}
			}, 100);
		}
    });
	
	
	/*
	 * Inizializzazione albero.
	 */
    $("#unitgrptree").jstree(
		{ 
			"core" : {
				"html_titles" : true,
				"animation" : 300
			},
			"plugins" : [ "themes", "json_data", "ui", "types", "search", "cookies", "crrm", "contextmenu"],
			"themes" : { "theme" : "classic", "url" : "css/jquery/jstree/themes/classic/style.css"  },
			"ui" : { "select_limit" : 1	},
			"contextmenu" : { 
				"items" : UnitGrpMenu,
				"show_at_node" : false
			},
			"types" : {
				"type_attr" : "tiponodo",
				"types" : {
					"GRP" : {
						"icon" : {"image" : "img/Users-23.gif"}
					},
					"UNIT" : {
						"icon" : {"image" : "img/Objects-14.gif"}
					},
					"USRSYS" : {
						"icon" : {"image" : "img/Users-5.gif"}
					}
				}
			},
			"search" : {
				"case_insensitive" : true,
				"show_only_matches" : false,
				"search_method" : "jstree_contains_any",
				"ajax" : {
					async: true,
					url: "w3/GestioneAlberoUnitGrp.do",
	                data : function (n) { 
						return {
							operation: "search",
							textsearch: $("#textsearch").val()
						};	
					},
					success: function( data ) {
						$("#attesa").show();
	                	SearchUnitGrpArray = [];
	                	if (data && data.length > 0) {
							$.map( data, function( item ) {
								var search_node = "#" + item[0];
								if (item[1] != "") {
									search_node += "_UNIT_" + item[1];
								} 
								if (item[2] != "") {
									search_node += "_GRP_" + item[2];
								}
								search_node = search_node.replace(/\./g,'\\.');
								SearchUnitGrpArray.push(search_node);
							});
							SearchUnitGrpArray.push("#UNIT");
							SearchUnitGrpArray.push("#GRP");
						} 
						return SearchUnitGrpArray;
					},
					complete: function (e) {
						$("#attesa").hide();
					}
				}
			},
			"json_data" : { 
				"ajax" : {
					async: true,
				    type: "GET",
	                dataType: "json",
	                beforeSend: function(x) {
	    			if(x && x.overrideMimeType) {
	        			x.overrideMimeType("application/json;charset=UTF-8");
				       }
					},
	                url: "w3/GestioneAlberoUnitGrp.do",
	                data : function (n) { 
						return {
							operation : "load",
							livello: n.attr ? n.attr("livello") : "0",
							idgrp : n.attr ? n.attr("idgrp") : "",
							idunit: n.attr ? n.attr("idunit") : ""
						};	
					},
	                success: function( data ) {
	                	$("#attesa").show();
	                	UnitGrpArray = [];
	                	if (data && data.length > 0) {
							$.map( data, function( item ) {
								
								/*
								 * Descrizione del nodo. 
								 */
								var descrizione = item[1];
								var descrizione_tooltip = '<span title="' + descrizione + '">';
								if (descrizione.length > lunghezzamassimadescrizione) {
									descrizione = descrizione.substring(0,lunghezzamassimadescrizione) + "...";
								}
								descrizione_tooltip += descrizione;
								
								/*
								 * Identificativo del nodo
								 *
								 */
								var nodeid = "";
								nodeid += item[2];
								if (item[3] != "") {
									nodeid += "_UNIT_" + item[3];
								} 
								if (item[4] != "") {
									nodeid += "_GRP_" + item[4];
								}
								if (item[5] != "") {
									nodeid += "_SYSCON_" + item[5];
								}
								
								/*
								 * Tipo di nodo (corrispondente alla sezione types)
								 */
								var tiponodo = "";
								if (item[0] == '11') {
									tiponodo = "UNIT";
								} else if (item[0] == '12' || item[0] == '21') {
									tiponodo = "GRP";
								} else if (item[0] == '13' || item[0] == '14' || item[0] == '22') {
									tiponodo = "USRSYS";
								}
								
								/* Stato del nodo */
								var statoNodo = "";
								if (item[0] == '13' || item[0] == '14' || item[0] == '22') {
									statoNodo = "";
								} else {
									statoNodo = "closed";
								}
								
								UnitGrpItem = {
									"data" : descrizione_tooltip,	
									"attr" : {
										"tiponodo" : tiponodo,
										"livello" : item[0],
										"descrizione" : item[1],
										"tipo" : item[2],
										"idunit" : item[3],
										"idgrp" : item[4],
										"syscon" : item[5],
										"id" : nodeid
									},
									"title" : item[1],
									"state" : statoNodo
								},
								UnitGrpArray.push(UnitGrpItem);
							});
						} 
						return UnitGrpArray;
					},
					complete : function( e ) {
						$("#attesa").hide();
					}
				}
			}
		}
	);
    
    
    /*
     * Menu' personalizzato
     */
    function UnitGrpMenu(node) {
    	
    	var items = null;
    	
    	if (node.attr("tipo") == 'UNIT' && (node.attr("livello") == '10' || node.attr("livello") == '11')) {
	        items = {
	        	addUNITItem: {
	           		label: "Nuova unit&agrave;",
	           		action: function (obj) { _addUNIT(); },
	           		_disabled : false
	            },
	        	visUNITItem: {
	        		label: "Visualizza unit&agrave;",
	        		action: function (obj) { _visUNIT(node); },
	        		"separator_before"  : true, 
	        		_disabled : false
	        	},
	        	updUNITItem: {
	        		label: "Modifica unit&agrave;",
	        		action: function (obj) { _updUNIT(node); },
	        		_disabled : false
	        	},
	        	delUNITItem: {
	        		label: "Elimina unit&agrave;",
	        		action: function (obj) { _delUNIT(node); },
	        		_disabled : false
	        	}
	        };
	        
	        if (node.attr("livello") == '10') {
	        	items.visUNITItem._disabled = true;
	        	items.updUNITItem._disabled = true;
	        	items.delUNITItem._disabled = true;
	        }
	        
	    } else if (node.attr("tipo") == 'GRP' && (node.attr("livello") == '20' || node.attr("livello") == '21'))  {
	    	items = {
	        	addGRPItem: {
	           		label: "Nuovo gruppo",
	           		action: function (obj) { _addGRP(); },
	           		_disabled : false
	            },
	        	visGRPItem: {
	        		label: "Visualizza gruppo",
	        		action: function (obj) { _visGRP(node); },
	        		"separator_before"  : true, 
	        		_disabled : false
	        	},
	        	updGRPItem: {
	        		label: "Modifica gruppo",
	        		action: function (obj) { _updGRP(node); },
	        		_disabled : false
	        	},
	        	delGRPItem: {
	        		label: "Elimina gruppo",
	        		action: function (obj) { _delGRP(node); },
	        		_disabled : false
	        	}
		    };
	    	
	        
	        if (node.attr("livello") == '20') {
	        	items.visGRPItem._disabled = true;
	        	items.updGRPItem._disabled = true;
	        	items.delGRPItem._disabled = true;
	        }
	    	
	    }
        
        return items;
    };
   
    
    function _addGRP() {
		document.formGrp.modo.value="NUOVO";
		bloccaRichiesteServer();
		document.formGrp.submit();
	}   
    
    function _visGRP(node) {
    	var idgrp = node.attr("idgrp");
		var key = "GRP.IDGRP=N:"+ idgrp;
		document.formGrp.key.value=key;
		document.formGrp.modo.value="VISUALIZZA";
		bloccaRichiesteServer();
		document.formGrp.submit();
	}

    function _updGRP(node) {
    	var idgrp = node.attr("idgrp");
		var key = "GRP.IDGRP=N:"+ idgrp;
		document.formGrp.key.value=key;
		document.formGrp.modo.value="MODIFICA";
		bloccaRichiesteServer();
		document.formGrp.submit();
	}

    function _delGRP(node) {
		$("#dialog-eliminaGRP").dialog( "option", { node: node } );
		$("#dialog-eliminaGRP").dialog( "open" );
    }
    
    function _addUNIT() {
		document.formUnit.modo.value="NUOVO";
		bloccaRichiesteServer();
		document.formUnit.submit();
	}
    
    function _visUNIT(node) {
    	var idunit = node.attr("idunit");
		var key = "UNIT.IDUNIT=N:"+ idunit;
		document.formUnit.key.value=key;
		document.formUnit.modo.value="VISUALIZZA";
		bloccaRichiesteServer();
		document.formUnit.submit();
	}

    function _updUNIT(node) {
    	var idunit = node.attr("idunit");
		var key = "UNIT.IDUNIT=N:"+ idunit;
		document.formUnit.key.value=key;
		document.formUnit.modo.value="MODIFICA";
		bloccaRichiesteServer();
		document.formUnit.submit();
	}
    
    function _delUNIT(node) {
		$("#dialog-eliminaUNIT").dialog( "option", { node: node } );
		$("#dialog-eliminaUNIT").dialog( "open" );
    }
    
    
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
	 * Ripulisce la ricerca (casella di input ed albero).
	 */
	function clearSearchUnitGrp() {
		if ($("#textsearch").val() != null && $("#textsearch").val() != "") {
    		$("#textsearch").val(null);
    		$('#unitgrptree ul li').unhighlight();
    		$("#unitgrptree").jstree("clear_search");
		}
	};
	
	
	/*
	 * Ricerca categorie.
	 */
    function searchUnitGrp() {
    	$("#attesa").show();
		$("#unitgrptree").jstree("close_all");
		$("#unitgrptree").jstree("clear_search");
		$('#unitgrptree ul li').unhighlight();
		$("#unitgrptree").jstree("search", $("#textsearch").val());
		
		var words = $("#textsearch").val().split(' ');
		for (var i = 0; i < words.length; i++) {
			word = words[i];
			if (word != "" && word != " " && word.length > 2) {
				$('#unitgrptree ul li').highlight(words);
			}
		}

		if ($("#textsearch").val() == null || $("#textsearch").val() == "") {
			$("#messaggioricerca").html("");
			$("#attesa").hide();
		}
	};
     
    
    /*
     * Conteggio elementi trovati
     */
    function searchMessaggio() {
    	var numero = $("a.jstree-search").length;
		if (numero) {
	    	if (numero == 0) {
				$("#messaggioricerca").html("Nessun elemento trovato.");
			} else if (numero == 1) {
				$("#messaggioricerca").html("Trovato 1 elemento.");
			} else {
				$("#messaggioricerca").html("Trovati " +  numero + " elementi.");
			}
		} else {
			$("#messaggioricerca").html("Nessun elemento trovato.");
		}
    };
	
    
	/*
	 * Metodo di ricerca aggiuntivo per "search" di jstree.
	 * Questo metodo permette la ricerca in OR di
	 * vari termini separati da "spazio".
	 */
	$.expr[':'].jstree_contains_any = function(a,i,m) {
		var word, words = [];
		var searchFor = m[3].toLowerCase().replace(/^\s+/g,'').replace(/\s+$/g,'');
		if (searchFor.indexOf(' ') >= 0) {
			words = searchFor.split(' ');
		}
		else {
			words = [searchFor];
		}
		for (var i = 0; i < words.length; i++) {
			word = words[i];
			if (word != "" && word != " ") {
				var descrizioneestesa = a.parentNode.attributes.descrizione.value;
				if (((a.textContent || a.innerText || "").toLowerCase().indexOf(word) >= 0) || 
				    ((descrizioneestesa || "").toLowerCase().indexOf(word) >= 0)) {
					return true;
				}
			}
		}
		return false;
	};

	
	/*
	 * Gestione maschera di dialogo per l'eliminazione dell'unita' organizzativa
	 */
    $( "#dialog-eliminaUNIT" ).dialog({
    	autoOpen: false,
    	width: 400,
    	height: 140,
    	show: {
    		effect: "blind",
    		duration: 200
        },
        hide: {
        	effect: "blind",
        	duration: 200
        },
        modal: true,
        resizable: false,
        buttons: {
            "Conferma" : function() {
           		var options = $("#dialog-eliminaUNIT").dialog("option");
           		_eliminaUNIT(options.node);

            },
            "Annulla" : function() {
            	$(this).dialog( "close" );
            }
          }
    });

    
    /*
     * Esegue l'operazione di eliminazione dell'unita' organizzativa
     */
	function _eliminaUNIT(node) {
		var idunit = node.attr("idunit");
		$.ajax({
			async: false,
			url: "w3/GestioneAlberoUnitGrp.do?operation=delete&tipo=UNIT&idunit=" + idunit
		}).done(function() {
			$("#dialog-eliminaUNIT").dialog("close");
			$("#unitgrptree").jstree("refresh", -1);
		});
	}
    
    
    /*
     * Gestione maschera di dialogo per l'eliminazione dell'unita' organizzativa
     */
    $( "#dialog-eliminaGRP" ).dialog({
    	autoOpen: false,
    	width: 400,
    	height: 140,
    	show: {
    		effect: "blind",
    		duration: 200
        },
        hide: {
        	effect: "blind",
        	duration: 200
        },
        modal: true,
        resizable: false,
        buttons: {
            "Conferma" : function() {
           		var options = $("#dialog-eliminaGRP").dialog("option");
           		_eliminaGRP(options.node);

            },
            "Annulla" : function() {
            	$(this).dialog( "close" );
            }
          }
    });
    
    
    /*
     * Esegue l'operazione di eliminazione del gruppo
     */
	function _eliminaGRP(node) {
		var idgrp = node.attr("idgrp");
		$.ajax({
			async: false,
			url: "w3/GestioneAlberoUnitGrp.do?operation=delete&tipo=GRP&idgrp=" + idgrp
		}).done(function() {
			$("#dialog-eliminaGRP").dialog("close");
			$("#unitgrptree").jstree("refresh", -1);
		});
	}
    
	
});
