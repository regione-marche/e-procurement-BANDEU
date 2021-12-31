
/*
 * Gestione degli utenti del gruppo
 */

$(window).on("load", function (){

	var _wait_gruppi = null;
	var _wait_utenti = null;
	
	_wait();
	function _wait() {
		document.getElementById('bloccaScreen').style.visibility='visible';
		document.getElementById('wait').style.visibility='visible';
		$("#wait").offset({ top: $(window).height() / 2, left: ($(window).width() / 2) - 200});
	}
	
	var timeout = null;
	timeout = setInterval(function() {
		if (_wait_gruppi == false && _wait_utenti == false) {
			document.getElementById('bloccaScreen').style.visibility='hidden';
			document.getElementById('wait').style.visibility='hidden';
			clearTimeout(timeout);	
		}
     }, 500);
	
	var _tableGruppi = null;
	var _tableUtenti = null;
	_popolaGruppi();
	_popolaUtenti();
	
	$.fn.dataTable.ext.order['dom-checkbox'] = function (settings, col)
	{
		return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
			return $('input',td).prop('checked') ? '1' : '0';
		} );
	};

	function _creaTabellaGruppi() {
		var _table = $('<table/>', {"id": "gruppi", "class": "scheda", "cellspacing": "0", "width" : "100%"});
		var _thead = $('<thead/>');
		var _tr1 = $('<tr/>', {"class": "intestazione"});
		_tr1.append('<th/>', {"class": "ck"});
		_tr1.append('<th/>');
		_tr1.append('<th/>');
		_thead.append(_tr1);
		_table.append(_thead);
		$("#gruppiContainer").append(_table);	
	}
	
	function _creaTabellaUtenti() {
		var _table = $('<table/>', {"id": "utenti", "class": "scheda", "cellspacing": "0", "width" : "100%"});
		var _thead = $('<thead/>');
		var _tr1 = $('<tr/>', {"class": "intestazione"});
		_tr1.append('<th/>', {"class": "ck"});
		_tr1.append('<th/>');
		_tr1.append('<th/>');
		_tr1.append('<th/>');
		_thead.append(_tr1);
		_table.append(_thead);
		$("#utentiContainer").append(_table);	
	}
	
	/*
	 * Popola la tabella con la lista degli utenti
	 */
	function _popolaGruppi() {
		_wait_gruppi = true;
		_creaTabellaGruppi();
		_tableGruppi = $('#gruppi').DataTable( {
			"ajax": {
				"url": "w3/GetListaGruppiUnita.do",
				"data" : function (n) { 
					return {
						operation: $("#OPERATION").val(),
						idunit: $("#UNIT_IDUNIT").val()
					};	
				},
				"complete": function() {
					_popolaListaIDGRP();
					_wait_gruppi = false;
	            }
			},
			"columnDefs": [
				{	
					"data": "IDGRP.value",
					"visible": false,
					"searchable": false,
					"targets": [ 1 ]
				},
				{
					"data": "DESCGRP.value",
					"visible": true,
					"sTitle": "Descrizione",
					"targets": [ 2 ],
					"sWidth": "300px"
				},
				{
					"data": "IDUNIT.value",
					"visible": $("#OPERATION").val() == "VISUALIZZA" ? false : true,
					"targets": [ 0 ],
					"sTitle": "Associato",
					"sWidth" : "85px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "grp_ck_" + full.IDGRP.value});
						if (data != null) _check.attr("checked","checked");
						if ($("#OPERATION").val() == "VISUALIZZA") _check.attr("disabled","disabled");
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
	        "pagingType": "full_numbers",
	        "lengthMenu": [[10, 25, 50, 100], ["10 gruppi", "25 gruppi", "50 gruppi", "100 gruppi"]],
	        "order": [[ 2, "asc" ]],
	        "aoColumns": [
			     { "bSortable": true, "bSearchable": false },
			     null,
			     null
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
				"url": "w3/GetListaUtentiUnita.do",
				"data" : function (n) { 
					return {
						operation: $("#OPERATION").val(),
						idunit: $("#UNIT_IDUNIT").val()
					};	
				},
				"complete": function() {
					_popolaListaSYSCON();
					_wait_utenti = false;
	            }
			},
			"columnDefs": [
				{	
					"data": "SYSCON.value",
					"visible": false,
					"searchable": false,
					"targets": [ 1 ]
				},
				{
					"data": "SYSUTE.value",
					"visible": true,
					"sTitle": "Descrizione",
					"targets": [ 2 ],
					"sWidth": "300px"
				},
				{
					"data": "EMAIL.value",
					"visible": true,
					"sTitle": "Email",
					"targets": [ 3 ],
					"sWidth": "300px"
				},
				{
					"data": "IDUNIT.value",
					"visible": $("#OPERATION").val() == "VISUALIZZA" ? false : true,
					"targets": [ 0 ],
					"sTitle": "Associato",
					"sWidth" : "85px",
					"class" : "ck",
					"orderDataType": "dom-checkbox",
					"render": function ( data, type, full, meta ) {
						var _div = $("<div/>");
						var _check = $("<input/>",{"type":"checkbox", "id": "usr_ck_" + full.SYSCON.value});
						if (data != null) _check.attr("checked","checked");
						if ($("#OPERATION").val() == "VISUALIZZA") _check.attr("disabled","disabled");
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
	        "order": [[ 2, "asc" ],[ 3, "asc" ]],
	        "aoColumns": [
			     { "bSortable": true, "bSearchable": false },
			     null,
			     null,
			     null
			   ]
	    });
	}
	
	$("body").delegate('[id^="usr_ck_"]', "click",
		function() {
			_popolaListaSYSCON();
		}
	);

	$("body").delegate('[id^="grp_ck_"]', "click",
		function() {
			_popolaListaIDGRP();
		}
	);
	
	function _popolaListaSYSCON() {
		var _listasyscon = [];
		_tableUtenti.$("tr").each(function () {
			var _ick = $(this).find('input:checked');
			if (_ick.size() > 0) {
				_ick.each(function () {
					var _id = $(this).attr("id");
					_syscon = _id.substring(7);
					_listasyscon.push(_syscon);
				});
			}
		});
		$("#LISTASYSCON").val(_listasyscon);
    };

    
	function _popolaListaIDGRP() {
		var _listaidgrp = [];
		_tableGruppi.$("tr").each(function () {
			var _ick = $(this).find('input:checked');
			if (_ick.size() > 0) {
				_ick.each(function () {
					var _id = $(this).attr("id");
					_idgrp = _id.substring(7);
					_listaidgrp.push(_idgrp);
				});
			}
		});
		$("#LISTAIDGRP").val(_listaidgrp);
    };
    
});
