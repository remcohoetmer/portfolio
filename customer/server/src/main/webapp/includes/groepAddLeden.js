var gAdd_Gebruikersgroep= null;
var gAdd_Rol= null;

$(document).ready(function() {
	$('#but_saveAddLeden').click( function() {
		submitAddLedenRequest( gAdd_Gebruikersgroep.id, gAdd_Rol,obj_GebruikersTable);
	} );
});

function jump2AddLeden( data)
{
	gAdd_Gebruikersgroep= data.groep;
	gAdd_Rol= data.rol;
	popupWindow( "popupboxAddLeden");
	if (data.groep.organisatie != null) {
		$('#searchGebruikerOrganisatie').val( data.groep.organisatie.id);
	}
	if (data.groep.locatie != null) {
		$('#searchGebruikerOrganisatie').val( data.groep.locatie.id);
	}
	searchGebruikers();
}

function submitAddLedenRequest( groepId, rol, obj_Table)
{
	var anSelected = fnGetSelected( obj_Table );
	var updateRequest= { 
		createLidmaatschappen: new Array()
	};
	for (var i=0;i<anSelected.length;i++) {
		var gebruikerId= getIdFromRow( anSelected[i]);
		if (gebruikerId) {
			updateRequest.createLidmaatschappen.push( {
				gebruiker: {id: gebruikerId},
				rol: rol
			});
		}
	}

	$.ajax({
		url: service_path + "/" + groepId,
		data: JSON.stringify(updateRequest),
		contentType: "application/json; charset=UTF-8",
		type: "PUT",
		headers: { "If-Unmodified-Since": gGebruikersgroepTimestamp},
		success: function( response, textStatus, jqXHR ) {
			// refresh
			jump2EditGroep( null); 
			closeWindow( "popupboxAddLeden");
		},
		error: ajaxErrorHandler
	});
}
