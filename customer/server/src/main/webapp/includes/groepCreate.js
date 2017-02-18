
$(document).ready(function() {
	$('#but_saveCreateGroep').click( function() {		
		submitGroepCreateRequest();
	} );
});

function jump2CreateGroep( )
{
	popupWindow( "popupboxCreate");
	initialiseCreateGroep();
}

function initialiseCreateGroep()
{
	$("#naamCreate").prop("value", "");
	$("#beschrijvingCreate").text("");
	$("#statusCreate").prop("value", "Actief");
	$("#organisatieCreate").prop("value", "");
	$("#scopeCreate").prop("value", "");
	$("#productCreate").prop("value", "");
	$("#kenmerkenCreate").prop("value", "");
	$("input:radio[name=groepsMutatieTypeCreate][value=MANAGED]").prop("checked", true);
	$("#geplandePeriodeCreate").prop("value", "");
}

function submitGroepCreateRequest( )
{
	var kenmerken= null;
	var kenmerkenString= $("#kenmerkenCreate").val();
	if (kenmerkenString!= "") {
		kenmerken= kenmerkenString.split(" ");
	}
	var createRequest= { 
		naam: $("#naamCreate").val(),
		beschrijving: $("#beschrijvingCreate").val(),
		status: $("#statusCreate").val(),
		product: $("#productCreate").val(),
		kenmerken: kenmerken,
		groepsMutatieType: $("input[name=groepsMutatieTypeCreate]:checked").val()
	};
	
	if ($("#organisatieCreate").val()!= "") {
		createRequest.organisatie= {id: $("#organisatieCreate").val()};
	}

	if ($("#scopeCreate").val()!= "") {
		createRequest.scope= { id: $("#scopeCreate").val()};
	}
	if ($("#geplandePeriodeCreate").val()!= "") {
		createRequest.geplandePeriode= $("#geplandePeriodeCreate").val();
	}
	$.ajax({
		url: "rest/groep",
		data: JSON.stringify( createRequest),
		type: "POST",
		contentType: "application/json; charset=UTF-8",
		success: function( response, textStatus, jqXHR )
		{
			closeWindow( "popupboxCreate");
			searchGroepen( );
		},
		error: ajaxErrorHandler
	});
}

