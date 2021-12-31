
/*
 * Gestione dei permessi
 */

$(window).on("load", function (){
	
	var _wait_gruppi = null;
	var _wait_utenti = null;
	var _tableGruppi = null;
	var _tableUtenti = null;

	//_popolaUnita();	
	_popolaGruppiUtenti();
	
	function _popolaGruppiUtenti() {
		_wait();
		_nowait();
		_popolaGruppi();
		_popolaUtenti();
	}

	$.fn.dataTable.ext.order['dom-checkbox'] = function (settings, col)
	{
		return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
			return $('input',td).prop('checked') ? '1' : '0';
		} );
	};
	
	function _wait() {
		document.getElementById('bloccaScreen').style.visibility='visible';
		document.getElementById('wait').style.visibility='visible';
		$("#wait").offset({ top: $(window).height() / 2, left: ($(window).width() / 2) - 200});
	}
	
	function _nowait() {
		var timeout = null;
		timeout = setInterval(function() {
			if (_wait_gruppi == false && _wait_utenti == false) {
				document.getElementById('bloccaScreen').style.visibility='hidden';
				document.getElementById('wait').style.visibility='hidden';
				clearTimeout(timeout);	
			}
	     }, 500);
	}
	
	function _creaTabellaGruppi() {
		var _table = $('<table/>', {"id": "gruppi", "class": "scheda", "cellspacing": "0", "width" : "100%"});
		var _thead = $('<thead/>');
		var _tr2 = $('<tr/>', {"class": "intestazione"});
		_tr2.append('<th/>');
		_tr2.append('<th/>');
		_tr2.append('<th/>');
		_tr2.append('<th/>', {"class": "ck"});
		_tr2.append('<th/>', {"class": "ck"});
		_tr2.append('<th/>', {"class": "ck"});
		_thead.append(_tr2);
		_table.append(_thead);
		$("#gruppiContainer").append(_table);
	}
	
	function _creaTabellaUtenti() {
		var _table = $('<table/>', {"id": "utenti", "class": "scheda", "cellspacing": "0", "width" : "100%"});
		var _thead = $('<thead/>');
		var _tr2 = $('<tr/>', {"class": "intestazione"});
		_tr2.append('<th/>');
		_tr2.append('<th/>');
		_tr2.append('<th/>');
		_tr2.append('<th/>');
		_tr2.append('<th/>', {"class": "ck"});
		_tr2.append('<th/>', {"class": "ck"});
		_tr2.append('<th/>', {"class": "ck"});
		_thead.append(_tr2);
		_table.append(_thead);
		$("#utentiContainer").append(_table);	
	}
	
	
	function _popolaUnita() {
		$.ajax({
            type: "GET",
            dataType: "json",
            async: false,
            beforeSend: function(x) {
			if(x && x.overrideMimeType) {
    			x.overrideMimeType("application/json;charset=UTF-8");
		       }
			},
            url: "GetListaUnita.do",
            success: function(data) {
            	var selectUnita = $('<select/>',{id: "selectUnita"});
            	selectUnita.append($("<option/>", {value: "-1", text: "Tutte" }));
            	if (data && data.length > 0) {
					$.map( data, function( item ) {
						selectUnita.append($("<option/>", {value: item[0].value, text: item[1].value }));
					});
            	}
            	$("#unitaContainer").append(selectUnita);
            },
            error: function(e){
                alert("Errore durante la lettura della lista delle unita' organizzative");
            }
        });
	}
	
    $('#selectUnita').change(
       	function() {
       		_tableGruppi.destroy(true);
       		_tableUtenti.destroy(true);
       		_popolaGruppiUtenti();
       	}
     );
	
	function _popolaGruppi() {
		_wait_gruppi = true;
		_creaTabellaGruppi();
		_tableGruppi = $('#gruppi').DataTable( {
			"ajax": {
				"url": "GetListaPermessiGruppi.do",
				"data" : function (n) { 
					return {
						operation: $("#operation").val(),
						tblname: $("#tblname").val(),
						clm1name: $("#clm1name").val(),
						clm1value: $("#clm1value").val(),
						idunit: $('#selectUnita').val()
					};	
				},
				"complete": function() {
	            	_wait_gruppi = false;
	            }
			},
			"columnDefs": [
				{	
					"data": "IDGRP.value",
					"visible": false,
					"searchable": false,
					"targets": [ 0 ]
				},
				{	
					"data": "IDPERM.value",
					"visible": false,
					"searchable": false,
					"targets": [ 2 ]
				},
				{
					"data": "DESCGRP.value",
					"visible": true,
					"sTitle": "Descrizione",
					"targets": [ 1 ],
					"sWidth": "600px",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>", {"text":full.DESCGRP.value});
						var _a = $("<a/>",{"id": "grp_a_" + full.IDGRP.value});
						var _img = $("<img/>", {"src": "../img/Users-23.png", "width": "14px", "height": "14px" });
						_a.append("&nbsp;&nbsp;");
						_a.append(_img);
						_div.append(_a);					
						return _div.html();
					}
				},
				{
					"data": "R.value",
					"visible": true,
					"targets": [ 3 ],
					"sTitle": "Lettura",
					"sWidth" : "95px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "grp_ck_r_" + full.IDGRP.value});
						if (data == 1) _check.attr("checked","checked");
						if ($("#operation").val() == "VISUALIZZA" || full.W.value == 1 || full.X.value == 1) _check.attr("disabled","disabled");
						_div.append(_check);					
						return _div.html();
					}
				},
				{
					"data": "W.value",
					"visible": true,
					"targets": [ 4 ],
					"sTitle": "Scrittura",
					"sWidth" : "95px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "grp_ck_w_" + full.IDGRP.value});
						if (data == 1) _check.attr("checked","checked");
						if ($("#operation").val() == "VISUALIZZA" || full.X.value == 1 ) _check.attr("disabled","disabled");
						_div.append(_check);					
						return _div.html();
					}
				},
				{
					"data": "X.value",
					"visible": true,
					"targets": [ 5 ],
					"sTitle": "Controllo completo",
					"sWidth" : "95px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "grp_ck_x_" + full.IDGRP.value});
						if (data == 1) _check.attr("checked","checked");
						if ($("#operation").val() == "VISUALIZZA") _check.attr("disabled","disabled");
						_div.append(_check);					
						return _div.html();
					}
				}
				
	        ],
	        "language": {
				"sEmptyTable":     "Nessun gruppo trovato",
				"sInfo":           "Visualizzazione da _START_ a _END_ di _TOTAL_ gruppi",
				"sInfoEmpty":      "Nessun gruppo trovato",
				"sInfoFiltered":   "(su _MAX_ gruppi totali)",
				"sInfoPostFix":    "",
				"sInfoThousands":  ",",
				"sLengthMenu":     "Visualizza _MENU_",
				"sLoadingRecords": "",
				"sProcessing":     "Elaborazione...",
				"sSearch":         "Cerca gruppi",
				"sZeroRecords":    "Nessun gruppo trovato",
				"oPaginate": {
					"sFirst":      "Prima",
					"sPrevious":   "Precedente",
					"sNext":       "Successiva",
					"sLast":       "Ultima"
				}
			},
			"lengthMenu": [[10, 25, 50, 100], ["10 gruppi", "25 gruppi", "50 gruppi", "100 gruppi"]],
	        "pagingType": "full_numbers",
	        "order": [[ 1, "asc" ]],
	        "aoColumns": [
			     null,
			     null,
			     null,
			     { "bSortable": true, "bSearchable": false },
			     { "bSortable": true, "bSearchable": false },
			     { "bSortable": true, "bSearchable": false }
			   ]
	    });
	}

	/*
	 * Popola la tabella con la lista degli utenti
	 */
	function _popolaUtenti() {
		_wait_utenti = true;
		_creaTabellaUtenti();
		_tableUtenti = $('#utenti').DataTable( {
			"ajax": {
				"url": "GetListaPermessiUtenti.do",
				"data" : function (n) { 
					return {
						operation: $("#operation").val(),
						tblname: $("#tblname").val(),
						clm1name: $("#clm1name").val(),
						clm1value: $("#clm1value").val(),
						idunit: $('#selectUnita').val()
					};	
				},
				"complete": function() {
	            	_wait_utenti = false;
	            }
			},
			"columnDefs": [
				{	
					"data": "SYSCON.value",
					"visible": false,
					"searchable": false,
					"targets": [ 0 ]
				},
				{	
					"data": "IDPERM.value",
					"visible": false,
					"searchable": false,
					"targets": [ 3 ]
				},
				{
					"data": "SYSUTE.value",
					"visible": true,
					"sTitle": "Descrizione",
					"targets": [ 1 ],
					"sWidth": "300px"
				},
				{
					"data": "EMAIL.value",
					"visible": true,
					"sTitle": "Email",
					"targets": [ 2 ],
					"sWidth": "300px"
				},
				{
					"data": "R.value",
					"visible": true,
					"targets": [ 4 ],
					"sTitle": "Lettura",
					"sWidth" : "95px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "usr_ck_r_" + full.SYSCON.value});
						if (data == 1) _check.attr("checked","checked");
						if ($("#operation").val() == "VISUALIZZA" || full.W.value == 1 || full.X.value == 1) _check.attr("disabled","disabled");
						_div.append(_check);					
						return _div.html();
					}
				},
				{
					"data": "W.value",
					"visible": true,
					"targets": [ 5 ],
					"sTitle": "Scrittura",
					"sWidth" : "95px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "usr_ck_w_" + full.SYSCON.value});
						if (data == 1) _check.attr("checked","checked");
						if ($("#operation").val() == "VISUALIZZA" || full.X.value == 1 ) _check.attr("disabled","disabled");
						_div.append(_check);					
						return _div.html();
					}
				},
				{
					"data": "X.value",
					"visible": true,
					"targets": [ 6 ],
					"sTitle": "Controllo completo",
					"sWidth" : "95px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "usr_ck_x_" + full.SYSCON.value});
						if (data == 1) _check.attr("checked","checked");
						if ($("#operation").val() == "VISUALIZZA") _check.attr("disabled","disabled");
						_div.append(_check);					
						return _div.html();
					}
				}
				
	        ],
	        "language": {
				"sEmptyTable":     "Nessun utente trovato",
				"sInfo":           "Visualizzazione da _START_ a _END_ di _TOTAL_ utenti",
				"sInfoEmpty":      "Nessun utente trovato",
				"sInfoFiltered":   "(su _MAX_ utenti totali)",
				"sInfoPostFix":    "",
				"sInfoThousands":  ",",
				"sLengthMenu":     "Visualizza _MENU_",
				"sLoadingRecords": "",
				"sProcessing":     "Elaborazione...",
				"sSearch":         "Cerca utenti",
				"sZeroRecords":    "Nessun utente trovato",
				"oPaginate": {
					"sFirst":      "Prima",
					"sPrevious":   "Precedente",
					"sNext":       "Successiva",
					"sLast":       "Ultima"
				}
			},
	        "pagingType": "full_numbers",
	        "lengthMenu": [[10, 25, 50, 100], ["10 utenti", "25 utenti", "50 utenti", "100 utenti"]],
	        "order": [[ 1, "asc" ],[ 2, "asc" ]],
	        "aoColumns": [
			     null,
			     null,
			     null,
			     null,
			     { "bSortable": true, "bSearchable": false },
			     { "bSortable": true, "bSearchable": false },
			     { "bSortable": true, "bSearchable": false }
			   ]
	    });
	}
    
	
	$("body").delegate('[id^="grp_a_"]', "mouseover",
		function(event) {
			var _id = $(this).attr("id");
			_idgrp = _id.substring(6);
			
			var _desc = "";	
			$.ajax({
				async: false,
				type: "GET",
				dataType: "json",
				beforeSend: function(x) {
				if(x && x.overrideMimeType) {
					x.overrideMimeType("application/json;charset=UTF-8");
				  }
				},
				"url": "GestioneAlberoUnitGrp.do?operation=load&livello=21&idgrp=" + _idgrp,
				"success": function(data) {
					$.map( data, function( item ) {
						if (_desc != "") _desc += ", ";
						_desc += item[1];
					});
				}
			});
			var _html = "<b>Utenti appartenenti al gruppo</b><br><br>" + _desc;
			var _position = $(this).position();
			var _top = _position.top;
			var _left = _position.left + 40;
			var _div = $("<div/>", {"id": "div_utenti_gruppo_" + _idgrp,  "class": "tooltip ui-corner-all", "html": _html});
			var _height = _div.height();
			_div.css('left', _left);
			_div.css('top', _top - 20);
			
			$(this).append(_div)
			_div.show(400);

		}
	);
	
	$("body").delegate('[id^="grp_a_"]', "mouseout",
		function() {
			var _id = $(this).attr("id");
			_idgrp = _id.substring(6);
			$("#div_utenti_gruppo_" + _idgrp).remove();
		}
	);
	
	$("body").delegate('[id^="grp_ck_x_"]', "click",
		function() {
			var _id = $(this).attr("id");
			_idgrp = _id.substring(9);
			if ($(this).is(':checked')) {
				$("#grp_ck_w_" + _idgrp).attr("checked","checked").attr("disabled","disabled");
				$("#grp_ck_r_" + _idgrp).attr("checked","checked").attr("disabled","disabled");
			} else {
				$("#grp_ck_w_" + _idgrp).removeAttr("disabled");
			}
		}
	);

	$("body").delegate('[id^="grp_ck_w_"]', "click",
		function() {
			var _id = $(this).attr("id");
			_idgrp = _id.substring(9);
			if ($(this).is(':checked')) {
				$("#grp_ck_r_" + _idgrp).attr("checked","checked").attr("disabled","disabled");
			} else {
				$("#grp_ck_r_" + _idgrp).removeAttr("disabled");
			}
		}
	);
	
	$("body").delegate('[id^="usr_ck_x_"]', "click",
		function() {
			var _id = $(this).attr("id");
			_syscon = _id.substring(9);
			if ($(this).is(':checked')) {
				$("#usr_ck_w_" + _syscon).attr("checked","checked").attr("disabled","disabled");
				$("#usr_ck_r_" + _syscon).attr("checked","checked").attr("disabled","disabled");
			} else {
				$("#usr_ck_w_" + _syscon).removeAttr("disabled");
			}
		}
	);

	$("body").delegate('[id^="usr_ck_w_"]', "click",
		function() {
			var _id = $(this).attr("id");
			_syscon = _id.substring(9);
			if ($(this).is(':checked')) {
				$("#usr_ck_r_" + _syscon).attr("checked","checked").attr("disabled","disabled");
			} else {
				$("#usr_ck_r_" + _syscon).removeAttr("disabled");
			}
		}
	);

	$('#menumodificapermessi, #pulsantemodificapermessi').click(function() {
		formModificaPermessiUtentiGruppi.operation.value="MODIFICA";
		formModificaPermessiUtentiGruppi.submit();
    }); 
	
	$('#menusalvamodifichepermessi, #pulsantesalvamodifichepermessi').click(function() {
		_salvaPermessiGRP();
		_salvaPermessiUSR();
		formVisualizzaPermessiUtentiGruppi.operation.value="VISUALIZZA";
		formVisualizzaPermessiUtentiGruppi.submit();
    });
	
	$('#menuannullamodifichepermessi, #pulsanteannullamodifichepermessi').click(function() {
		formVisualizzaPermessiUtentiGruppi.operation.value="VISUALIZZA";
		formVisualizzaPermessiUtentiGruppi.submit();
    });
	
	function _salvaPermessiGRP() {
		$.ajax({
			  "async": false,
			  "url": "SetPermessi.do?operation=DELETE&guperm=GRP&tblname=" + $("#tblname").val() + "&clm1name=" + $("#clm1name").val() + "&clm1value=" + $("#clm1value").val()
		});
		_tableGruppi.$("tr").each(function () {
			var _ick = $(this).find('input:checked');
			if (_ick.size() > 0) {

				var _syscon = 0;
				var _idgrp = 0;
				var _r = 0;
				var _w = 0;
				var _x = 0;
				
				_ick.each(function () {
					var _id = $(this).attr("id");
					_idgrp = _id.substring(9);
					if (_id.substring(0,9) == 'grp_ck_r_') _r = 1;
					if (_id.substring(0,9) == 'grp_ck_w_') _w = 1;
					if (_id.substring(0,9) == 'grp_ck_x_') _x = 1;
				});
			
				$.ajax({
					  "async": false,
					  "url": "SetPermessi.do?operation=INSERT&guperm=GRP&syscon=" + _syscon + "&idgrp=" + _idgrp + "&tblname=" + $("#tblname").val() + "&clm1name=" + $("#clm1name").val() + "&clm1value=" + $("#clm1value").val() + "&r=" + _r + "&w=" + _w + "&x=" + _x
				});
			}
		});
    };	

	function _salvaPermessiUSR() {
		$.ajax({
			  "async": false,
			  "url": "SetPermessi.do?operation=DELETE&guperm=USR&tblname=" + $("#tblname").val() + "&clm1name=" + $("#clm1name").val() + "&clm1value=" + $("#clm1value").val()
		});
		_tableUtenti.$("tr").each(function () {
			var _ick = $(this).find('input:checked');
			if (_ick.size() > 0) {

				var _syscon = 0;
				var _idgrp = 0;
				var _r = 0;
				var _w = 0;
				var _x = 0;
				
				_ick.each(function () {
					var _id = $(this).attr("id");
					_syscon = _id.substring(9);
					if (_id.substring(0,9) == 'usr_ck_r_') _r = 1;
					if (_id.substring(0,9) == 'usr_ck_w_') _w = 1;
					if (_id.substring(0,9) == 'usr_ck_x_') _x = 1;
				});
			
				$.ajax({
					  "async": false,
					  "url": "SetPermessi.do?operation=INSERT&guperm=USR&syscon=" + _syscon + "&idgrp=" + _idgrp + "&tblname=" + $("#tblname").val() + "&clm1name=" + $("#clm1name").val() + "&clm1value=" + $("#clm1value").val() + "&r=" + _r + "&w=" + _w + "&x=" + _x
				});
			}
		});
    };	
    
});
