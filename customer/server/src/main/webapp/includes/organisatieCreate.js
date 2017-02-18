
$(document).ready(function() {
	$('#but_saveCreate').click( function() {		
		submitCreateRequest();
	} );
});

function jump2Create( )
{
	popupWindow( "popupboxCreate");
	initialiseCreate();
}

function initialiseCreate()
{
	$("#naamCreate").prop("value", "");
	$("#statusCreate").prop("value", "Actief");
}

function submitCreateRequest( )
{
	var createRequest= { 
		naam: $("#naamCreate").val(),
		status: $("#statusCreate").val(),
	};

	$.ajax({
		url: service_path,
		data: JSON.stringify( createRequest),
		type: "POST",
		contentType: "application/json; charset=UTF-8",
		success: function( response, textStatus, jqXHR )
		{
			closeWindow( "popupboxCreate");
			searchOrganisaties( );
		},
		error: ajaxErrorHandler
	});
}

