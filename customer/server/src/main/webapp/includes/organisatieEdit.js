var gOrganisatieId= null;

$(document).ready(function() {
	$('#but_saveEdit').click( function() {
		submitUpdateAttributesRequest( gOrganisatieId);
	} );
});

function jump2EditOrganisatie( organisatieId) {
	if (organisatieId!= null) {
		gOrganisatieId= organisatieId;
	}
	popupWindow( "popupboxEdit");
	retrieveDetails( gOrganisatieId);
}

function retrieveDetails( organisatieId) {
	$.ajax({
		url: service_path+ "/"+organisatieId,
		type: "GET",
		success: function( response, textStatus, jqXHR ) {
			showEditData( response);
		},
        cache: false, 
		error: ajaxErrorHandler
	});	
}

function showEditData(response)
{
	if ( response.organisaties.length != 1) {
		return;
	}
	var groep= response.organisaties[0];
	
	$("#naamEdit").prop("value", groep.naam);
	$("#statusEdit").prop("value", groep.status);
}

function submitUpdateAttributesRequest( organisatieId)
{
	var updateRequest= { 
		id: organisatieId,
		naam: $("#naamEdit").val(),
		status: $("#statusEdit").val()
	};
	$.ajax({
		url: service_path,
		data: JSON.stringify(updateRequest),
		type: "PUT",
		contentType: "application/json; charset=UTF-8",
		success: function( response, textStatus, jqXHR )
		{
			searchOrganisaties();
			closeWindow( "popupboxEdit");
		},
		error: ajaxErrorHandler
	});
}


