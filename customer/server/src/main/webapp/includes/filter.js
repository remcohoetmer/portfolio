
function updateFilterString( filter, tag, paramnaam)
{
	var value= $("#"+tag).val();
	if(value != "") {
		if (filter.string=="") {
			filter.string="?";
		}  else {
			filter.string+="&";
		}
		filter.string+= paramnaam + '=' + value;
	}
}
function updateFilterStringWildcard( filter, tag,paramnaam)
{
	var value= $("#"+tag).val();
	if(value != "") {
		if (filter.string=="") {
			filter.string="?";
		}  else {
			filter.string+="&";
		}
		// HTML &#42; = *
		filter.string+= paramnaam + "=\*"+ value + "\*";
	}
}

function listProperties(obj) {
   var propList = "";
   for(var propName in obj) {
      if(typeof(obj[propName]) != "undefined") {
         propList += (propName + ", ");
      }
   }
   alert(propList);
}

function getIdFromEvent( event)
{
	return getIdFromRow( $(event.target.parentNode));
}

function getIdFromRow( row)
{
	return $(row).attr( "id").substr( 3);
}


/* Get the rows which are currently selected */
function fnGetSelected( tableLocal )
{
	var aReturn = new Array();
	var aTrs = tableLocal.fnGetNodes();
	for ( var i=0 ; i<aTrs.length ; i++ ) {
		if ( $(aTrs[i]).hasClass('row_selected') ) {
			aReturn.push( aTrs[i] );
		}
	}
	return aReturn;
}

var selectionHandler= function() {
	if ( $(this).hasClass('row_selected') )
		$(this).removeClass('row_selected');
	else
		$(this).addClass('row_selected');
};

var ajaxErrorHandler= function( response, textStatus, jqXHR ) {
	if (response.status==0) {
		// geen verbinding -> hier kan de gebruiker niets mee
		return;
	}
	if (response.responseXML != null) {
		var newwindow=window.open('Error','height=200,width=200');
	
		newwindow.write(response.responseText);

		if (window.focus) {
			newwindow.focus()
		}
		return;
	}
	
	if (response.status<500) {
		alert( response.statusText + "[" + response.status + "]:" + response.responseText);
	} else {
		alert( response.statusText + "[" + response.status + "]:Een systeemfout is opgetreden");
	}
};

function prepareInschrijving( gebruiker)
{
	if (gebruiker.inschrijvingen== null ||  gebruiker.inschrijvingen.length==0) {
		gebruiker.inschrijvingen = [ { organisatie: null, locatie: null, leerrichting: "" , jaargroep:"", rol:""}];
	} 
	for (var i=0; i< gebruiker.inschrijvingen.length; i++) {
		var inschrijving= gebruiker.inschrijvingen[i];
		if (inschrijving.organisatie==null) {
			inschrijving.organisatie= {naam:""};
		}
		if (inschrijving.locatie==null) {
			inschrijving.locatie= {naam:""};
		}
	}
}

