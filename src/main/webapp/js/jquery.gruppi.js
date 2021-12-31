
/*
 * Gestione degli utenti del gruppo
 */

$(window).on("load", function (){
	
	var _tableUtenti = null;
	
	_wait();
	_popolaUtenti();

	function _wait() {
		document.getElementById('bloccaScreen').style.visibility='visible';
		document.getElementById('wait').style.visibility='visible';
		$("#wait").offset({ top: $(window).height() / 2, left: ($(window).width() / 2) - 200});
	}
	
	function _nowait() {
		document.getElementById('bloccaScreen').style.visibility='hidden';
		document.getElementById('wait').style.visibility='hidden';
	}
	
	$.fn.dataTable.ext.order['dom-checkbox'] = function (settings, col)
	{
		return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
			return $('input',td).prop('checked') ? '1' : '0';
		} );
	};
	
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
	function _popolaUtenti() {
		_wait_utenti = true;
		_creaTabellaUtenti();
		_tableUtenti = $('#utenti').DataTable( {
			"ajax": {
				"url": "w3/GetListaUtentiGruppo.do",
				"data" : function (n) { 
					return {
						operation: $("#OPERATION").val(),
						idgrp: $("#GRP_IDGRP").val()
					};	
				},
				"complete": function() {
					_popolaListaSYSCON();
	            	_nowait();
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
					"data": "IDGRP.value",
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
    
});
