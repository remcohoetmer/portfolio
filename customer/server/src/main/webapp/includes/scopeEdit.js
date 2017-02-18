var gGebruikersgroepscopeId= null;
var gGebruikersgroepscopeTimestamp= null;

$(document).ready(function() {
	$('#but_saveEdit').click( function() {
		submitUpdateAttributesRequest( gGebruikersgroepscopeId);
	} );
});

function jump2Edit( gebruikersgroepscopeId) {
	if (gebruikersgroepscopeId!= null) {
		gGebruikersgroepscopeId= gebruikersgroepscopeId;
	}
	popupWindow( "popupboxEdit");
	retrieveDetails( gGebruikersgroepscopeId);
}

function retrieveDetails( gebruikersgroepscopeId) {

	$.ajax({
		url: service_path+ "/"+gebruikersgroepscopeId,
		type: "GET",
		success: function( response, textStatus, jqXHR ) {
			gGebruikersgroepscopeTimestamp= jqXHR.getResponseHeader("Last-Modified");
			showEditData( response);
		},
        cache: false,
		error: ajaxErrorHandler
	});	
}

function showEditData(groepscope)
{
	if ( groepscope == null) {
		return;
	}

	$("#naamEdit").prop("value", groepscope.naam);
	$("#statusEdit").prop("value", groepscope.status);
}

function submitUpdateAttributesRequest( gebruikersgroepscopeId)
{
	var updateRequest= { 
		id: gebruikersgroepscopeId,
		naam: $("#naamEdit").val(),
		status: $("#statusEdit").val()
	};
	$.ajax({
		url: service_path,
		data: JSON.stringify(updateRequest),
		type: "PUT",
		headers: { "If-Unmodified-Since": gGebruikersgroepscopeTimestamp},
		contentType: "application/json; charset=UTF-8",
		success: function( response, textStatus, jqXHR )
		{
			searchEntities();
			closeWindow( "popupboxEdit");
		},
		error: ajaxErrorHandler
	});
}


