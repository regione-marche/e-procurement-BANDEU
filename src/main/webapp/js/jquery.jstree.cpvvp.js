/*	
 * Albero delle unita' organizzative e dei gruppi.
 * Operazioni disponibili:
 * 
 * 
 */


$(window).on("load", function (){
	
	/*
	 * Associazione in gruppi delle divisioni 
	 */
	 CpvVPGrpDiv = [
	                     {"grpid": "g1", "grpdesc": "Agricoltura e alimentazione", "grpdiv" : ["03","15","77"]},
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
	                     {"grpid": "g15", "grpdesc": "Trasporti e relativi servizi", "grpdiv" : ["34","60","63"]}
	          	]
	 
    $('#deletesearch').click(function() {
    	clearSearchCpvVP();
    });
    
    $('#textsearch').keyup(function() {
    	setTimeout(function(){
    		searchCpvVP();
		}, 600);
    });
	
	if ($("#modo").val() == "VISUALIZZA") {
		$('#textsearch').attr('readonly','readonly');
		$('#textsearch').attr('tabindex','-1');
		$('#textsearch').css('border-color','#A3A6FF');
		$('#textsearch').css('border-width','1px');
		$('#textsearch').css('background-color','#EFEFEF');
		$('#deletesearch').hide();
	}
	
	/*
	 * Apro il menu', gia' disponibile con il tasto destro del mouse, 
	 * alla selezione del nodo.
	 */
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
	
	
	/*
	 * Inizializzazione albero.
	 */
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
				"type_attr" : "tiponodo",
				"types" : {
					"GRP" : {
						"icon" : {"image" : "img/categoria_rosso.gif"}
					},
					"CPV" : {
						"icon" : {"image" : "img/categoria_verde.gif"}
					}
				}
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
							livello: n.attr ? n.attr("livello") : "0",
							id : n.attr ? n.attr("id") : "",									
							cpvvpcod : n.attr ? n.attr("cpvvpcod") : "",
							textsearch: $("#textsearch").val()
						};	
					},
	                success: function( data ) {
	                	CpvVPArray = [];
	                	if (data && data.length > 0) {
							$.map( data, function( item ) {
								
								var descrizione = "";
								if (item[0] > 2) descrizione += item[2] + " - ";
								descrizione += item[3];
								if ($("#textsearch").val() != null && $("#textsearch").val() != "") {
									descrizione += " [" + item[5] + "]";
								}

								var statonodo = "";
								if (item[4] == 0) {
									statonodo = ""	
								} else {
									statonodo = "closed"
								}
								
								var tiponodo = "";
								if (item[0] > 4) {
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
										"cpvvpdesc" : item[3]
									},
									"title" : item[1],
									"state" : statonodo
								},
								CpvVPArray.push(CpvVPItem);
							});
						} 
						return CpvVPArray;
					},
					complete : function( e ) {
						
					}
				}
			}
		}
	);
    
    /*
     * Menu' personalizzato
     */
    function CpvVPMenu(node) {
    	var items = null;
    	
    	if ($("#modo").val() != "VISUALIZZA" && node.attr("livello") > 4) {
	        items = {
	        	selItem: {
	           		label: "Seleziona",
	           		action: function (obj) { _selcpvvp(node); },
	           		_disabled : false
	            }
		    };
        }
        
        return items;
    };
   
    /*
     * Selezione del CPV
     */
    function _selcpvvp(node) {
    	var cpvvpcod = node.attr("cpvvpcod");
    	var campoopener = window.opener.jQuery("#" + $("#campoopener").val());
    	campoopener.val(cpvvpcod);
    	var campoopenerview = window.opener.jQuery("#" + $("#campoopener").val() + "view");
    	campoopenerview.text(cpvvpcod);
    	window.close();
	}

    /*
     * Pulizia parametri di ricerca
     */
	function clearSearchCpvVP() {
		if ($("#textsearch").val() != null && $("#textsearch").val() != "") {
    		$("#textsearch").val(null);
    		$("#cpvvptree").jstree("close_all");
    		$("#cpvvptree").jstree("refresh");
		}
	};
	
	/*
	 * Ricerca per descrizione (multipla)
	 */
    function searchCpvVP() {
		$("#cpvvptree").jstree("refresh");
	};

	
});
