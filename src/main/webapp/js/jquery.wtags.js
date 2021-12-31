/**	
 * Gestione link collegamenti al documentale per l'interrogazione degli atti
 */
	
	var myTags = myTags || (function(){
		var _ctx;
		var _codapp;
		var _modo;
		var _tags;
		var _tagsList;
		
		return {
			init : function(ctx, codapp) {
				_ctx = ctx;
				_codapp = codapp;
				$("<style type='text/css'> .CDEFAULT{ background-color:#C0C0C0; border:1px solid #C0C0C0; color: #000000;} </style>").appendTo("head");
				$("<style type='text/css'> .ui-tooltip{font: 11px Verdana, Arial, Helvetica, sans-serif; color: #606060; box-shadow: 4px 4px 4px #606060;} </style>").appendTo("head");
			},
			addTags: function() {
				_addTags();
			},
			getCtx: function() {
				return _ctx;
			},
			setTagsList: function(tagsList) {
				_tagsList = tagsList;
			},
			getTagsList: function() {
				return _tagsList;
			},
			setCodapp: function(codapp) {
				_codapp = codapp;
			},
			getCodapp: function() {
				return _codapp;
			}
			
		};
	}());
	
	
	function _addTags() {
		
		var tagscode_on = [];
		
		$.ajax({
			type: "POST",
			dataType: "json",
			async: true,
			beforeSend: function(x) {
				if(x && x.overrideMimeType) {
					x.overrideMimeType("application/json;charset=UTF-8");
				}
			},
			url: myTags.getCtx() + "/GetWTags.do?q=tags&codapp=" + myTags.getCodapp(),
			success: function(json){
				 if (json.tags) {
					 $("#footer").find(".superiore").find(".left").append("&nbsp;&nbsp;&nbsp;");
					 $.each(json.tags,function( index, tagsObject ) {
						
						var tagcod = tagsObject.c;
						var tagview = tagsObject.v
						var tagdesc = tagsObject.d;
						var tagcolor = tagsObject.cl;
						var tagbordercolor = tagsObject.bcl;
						 
						var _style = "<style type='text/css'> .C";
						_style += tagcod;
						_style += "{ background-color:";
						_style += tagcolor;
						_style += "; border:1px solid ";
						_style += tagbordercolor;
						_style += "; color: #000000;} </style>";
						
						$(_style).appendTo("head");
						
						var _span = $("<span/>",{"id": "toogle" + tagcod, "text": tagview, "title": tagdesc});
						_span.css("margin-top","2px");
						_span.css("margin-bottom","2px");
						_span.css("margin-left","2px");
						_span.css("margin-right","2px");
						_span.css("vertical-align","middle");
						_span.css("padding-left","5px");
						_span.css("padding-right","5px");
						_span.css("padding-top","1px");
						_span.css("padding-bottom","1px");
						_span.css("-moz-border-radius-topleft","6px"); 
						_span.css("-webkit-border-top-left-radius","6px"); 
						_span.css("-khtml-border-top-left-radius","6px"); 
						_span.css("border-top-left-radius","6px"); 
						
						if ($.cookie(tagcod) == "OFF") {
							_span.addClass("CDEFAULT");
						} else {
							_span.addClass("C" + tagcod);
							tagscode_on.push(tagcod);
						}
						
						_span.click(function() {
							  if ($(this).hasClass("CDEFAULT")) {
								  $(this).removeClass("CDEFAULT");
								  $(this).addClass("C" + tagcod);
								  $.cookie(tagcod, "ON", {expires: 365 });
								  _addTagsList(tagcod);
							  } else {
								  $(this).removeClass("C" + tagcod);
								  $(this).addClass("CDEFAULT");
								  $.cookie(tagcod, "OFF", {expires: 365 });
								  $(".info" + tagcod).remove();
							  }
						});
						
						$("#footer").find(".superiore").find(".left").append(_span);
											
					 });
				 }
			},
			complete: function() {
				if (tagscode_on.length > 0) {
					_readTagsList(tagscode_on);
				}
			}
		});
	}
	
	
	function _addTagsList(tagcod) {
		var _tagsList = myTags.getTagsList();
		if (_tagsList == null || _tagsList == undefined) {
			_readTagsList([tagcod]);
		} else {		
			_disposeTagsList(_tagsList, [tagcod]);
		}
	}
	
	
	function _readTagsList(tagscode_on) {
		$.ajax({
			type: "POST",
			dataType: "json",
			async: true,
			beforeSend: function(x) {
				if(x && x.overrideMimeType) {
					x.overrideMimeType("application/json;charset=UTF-8");
				}
			},
			url: myTags.getCtx() + "/GetWTags.do?q=tagslist&codapp=" + myTags.getCodapp(),
			success: function(json){
				 if (json.tagslist) {
					 myTags.setTagsList(json.tagslist);
					 $.each(tagscode_on,function( index, tagcod ) {
						 _disposeTagsList(json.tagslist,tagcod);
					 });
				 }
			}
		});
	}
	
	
	function _getTaginfo(tagentity, tagfield, tagcod) {
		var taginfo;
		$.ajax({
			type: "POST",
			dataType: "json",
			async: false,
			beforeSend: function(x) {
				if(x && x.overrideMimeType) {
					x.overrideMimeType("application/json;charset=UTF-8");
				}
			},
			url: myTags.getCtx() + "/GetWTags.do?q=taginfo&codapp=" + myTags.getCodapp() + "&tagentity=" + tagentity + "&tagfield=" + tagfield + "&tagcod=" + tagcod,
			success: function(json){
				taginfo = json.taginfo;
			}
		});
		return taginfo;
	}

	
	function _disposeTagsList(tagsList, tagcod) {
		 $.each(tagsList,function( index, tagsListObject ) {
			if (tagsListObject.c == tagcod) {
				var _id = "";
				if (tagsListObject.e == "" || tagsListObject.e == null) {
					_id = tagsListObject.f;
				} else {
					_id = tagsListObject.e + "_" + tagsListObject.f;
				}
				
				var _tds = $("[id='row" + _id + "'] td.valore-dato, [id^='row" + _id + "_'] td.valore-dato");
				if (_tds.size() > 0) {
					$.each(_tds,function( index_td, _td ) {						
						var _span = $("<span/>",{"class": "info" + tagsListObject.c, "text": tagsListObject.v, "title": tagsListObject.v});
						_span.addClass("C" + tagsListObject.c);
						_span.css("margin-left","3px");
						_span.css("margin-bottom","2px");
						_span.css("display","inline-block");
						_span.css("float","right");
						_span.css("padding-left","5px");
						_span.css("padding-right","5px");
						_span.css("padding-top","1px");
						_span.css("padding-bottom","1px");
						_span.css("-moz-border-radius-topleft","6px"); 
						_span.css("-webkit-border-top-left-radius","6px"); 
						_span.css("-khtml-border-top-left-radius","6px"); 
						_span.css("border-top-left-radius","6px"); 
						_span.tooltip({
							position: { my: "right-15 top-15", at: "left top" }
						});
						
						$(this).prepend(_span);
						
						_span.on( "tooltipopen", function( event, ui ) {
							var _taginfo = _getTaginfo(tagsListObject.e, tagsListObject.f, tagsListObject.c);
							$(this).tooltip({
								content: _taginfo
							});
						});
					});
				}
				
				var _tds = $("[id='row" + _id + "'] td:eq(0)[colspan=2]");
				if (_tds.size() > 0) {
					$.each(_tds,function( index_td, _td ) {						
						var _span = $("<span/>",{"class": "info" + tagsListObject.c, "text": tagsListObject.v, "title": tagsListObject.v});
						_span.addClass("C" + tagsListObject.c);
						_span.css("margin-left","3px");
						_span.css("margin-bottom","2px");
						_span.css("display","inline-block");
						//_span.css("float","left");
						_span.css("padding-left","5px");
						_span.css("padding-right","5px");
						_span.css("padding-top","1px");
						_span.css("padding-bottom","1px");
						_span.css("-moz-border-radius-topleft","6px"); 
						_span.css("-webkit-border-top-left-radius","6px"); 
						_span.css("-khtml-border-top-left-radius","6px"); 
						_span.css("border-top-left-radius","6px"); 
						_span.tooltip({
							position: { my: "right-15 top-15", at: "left top" }
						});
						
						$(this).append(_span);
						
						_span.on( "tooltipopen", function( event, ui ) {
							var _taginfo = _getTaginfo(tagsListObject.e, tagsListObject.f, tagsListObject.c);
							$(this).tooltip({
								content: _taginfo
							});

						});
					});
				}
				
			 }
		});	
	}
	

