var gGebruikersgroep= null;
var gGebruikersgroepTimestamp= null;

var obj_GroepsleidersTable= null;
var obj_GroepsledenTable= null;

$(document).ready(function() {

	$('#but_delete_lid').click( function() {
		submitUpdateLidmaatschappenRequest( gGebruikersgroep.id, obj_GroepsledenTable, "Verwijderd");
	} );
	$('#but_activate_lid').click( function() {
		submitUpdateLidmaatschappenRequest( gGebruikersgroep.id, obj_GroepsledenTable, "Actief");
	} );
	$('#but_deactivate_lid').click( function() {
		submitUpdateLidmaatschappenRequest( gGebruikersgroep.id, obj_GroepsledenTable, "Passief");
	} );
	$('#but_delete_ldr').click( function() {
		submitUpdateLidmaatschappenRequest( gGebruikersgroep.id, obj_GroepsleidersTable, "Verwijderd");
	} );
	$('#but_activate_ldr').click( function() {
		submitUpdateLidmaatschappenRequest( gGebruikersgroep.id, obj_GroepsleidersTable, "Actief");
	} );
	$('#but_deactivate_ldr').click( function() {
		submitUpdateLidmaatschappenRequest( gGebruikersgroep.id, obj_GroepsleidersTable, "Passief");
	} );
	$('#but_saveEdit').click( function() {
		submitUpdateGroepAttributesRequest( gGebruikersgroep.id);
	} );

	$('#but_add_lid').click( function() {
		jump2AddLeden( { groep: gGebruikersgroep, rol: "GROEPSLID"});
	} );
	$('#but_add_ldr').click( function() {
		jump2AddLeden( { groep: gGebruikersgroep, rol: "GROEPSLEIDER"});
	} );
});

function jump2EditGroep( groepId) {

	popupWindow( "popupboxEdit");
	retrieveGroepDetails( groepId);
}

function retrieveGroepDetails( groepId) {
	if (groepId== null && gGebruikersgroep != null) {

		groepId= gGebruikersgroep.id;
	}
	$.ajax({
		url: service_path + "/" +groepId + 
		"?select=kenmerken,klanten,lidmaatschappen,organisaties,scopes,hoofdgroep",
		type: "GET",
		success: function( response, textStatus, jqXHR ) {
			if ( response==null) {
				//alert( "geen groepsdata");
				return;
			}
			gGebruikersgroep= response;
			gGebruikersgroepTimestamp= jqXHR.getResponseHeader("Last-Modified");
			showGroepsData( gGebruikersgroep);

		},
        cache: false,
		error: ajaxErrorHandler
	});	
}

function showGroepsData(groep)
{
	$("#naamEdit").prop("value", groep.naam);
	$("#beschrijvingEdit").text(groep.beschrijving);
	$("#statusEdit").prop("value", groep.status);
	if (groep.organisatie) 
		$("#organisatieEdit").prop("value", groep.organisatie.naam);
	else 
		$("#organisatieEdit").prop("value", "");
	if (groep.scope) 
		$("#scopeEdit").prop("value", groep.scope.naam);
	else 
		$("#scopeEdit").prop("value", "");
	if (groep.product) 
		$("#productEdit").prop("value", groep.product);
	else 
		$("#productEdit").prop("value", "");
	if (groep.kenmerken) 
		$("#kenmerkenEdit").prop("value", groep.kenmerken.join(" "));
	else 
		$("#kenmerkenEdit").prop("value", "")

		$("#groepsMutatieTypeEdit_" + groep.groepsMutatieType).prop("checked", true);
	if (groep.groepscode) 
		$("#groepscodeEdit").prop("value", groep.groepscode);
	else 
		$("#groepscodeEdit").prop("value", "")

		if (groep.geplandePeriode) 
			$("#geplandePeriodeEdit").prop("value", groep.geplandePeriode);
		else 
			$("#geplandePeriodeEdit").prop("value", "");

	if (groep.hoofdgroep) 
		$("#hoofdgroepEdit").prop("value", groep.hoofdgroep.naam);
	else 
		$("#hoofdgroepEdit").prop("value", "");

	var groepsleiders= new Array();	
	var groepsleden= new Array();


	if (groep.lidmaatschappen != null) {
		// introduce for null-safety
		for (i=0;i<groep.lidmaatschappen.length;i++) {
			var lidmaatschap= groep.lidmaatschappen[i];
			var lidmaatschap_klant= lidmaatschap.klant;
			// indien niet gedefinieerd: definieer leeg
			prepareInschrijving( lidmaatschap_klant);
			// voeg de status & id van het lidmaatschap toe aan de klantgegevens
			lidmaatschap_klant.status_lidmaatschap=lidmaatschap.status;
			lidmaatschap_klant.laatstgewijzigd=lidmaatschap.laatstgewijzigd;
			lidmaatschap_klant.id_lidmaatschap=lidmaatschap.id;

			if (lidmaatschap.rol== "GROEPSLEIDER") {
				groepsleiders.push( lidmaatschap_klant);
			} else {
				groepsleden.push( lidmaatschap_klant);
			}
		}
	}

	var aoColumns = [   { "mData": "status_lidmaatschap" },
	                    { "mData": "status" },
	                    { "mData": "voornaam" },
	                    { "mData": "voorletters" },
	                    { "mData": "voorvoegselAchternaam" },
	                    { "mData": "achternaam" },
	                    { "mData": "emailAdres" },
	                    { "mData": "geslacht" }
	                    ];
	obj_GroepsledenTable= $('#groepsledenTable').dataTable( {
		"aaData": groepsleden,
		"bDestroy": true,
		"aoColumns": aoColumns,
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull) {
			$(nRow).attr( "id", "LID" + aData.id_lidmaatschap);
			$(nRow).attr( "laatstgewijzigd", aData.laatstgewijzigd);
			return nRow;
		}
	} );
	$('#groepsledenTable tr').click( selectionHandler);

	obj_GroepsleidersTable= $('#groepsleidersTable').dataTable( {
		"aaData": groepsleiders,
		"bDestroy": true,
		"aoColumns": aoColumns,
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull) {
			$(nRow).attr( "id", "LDR" + aData.id_lidmaatschap);
			$(nRow).attr( "laatstgewijzigd", aData.laatstgewijzigd);
			return nRow;
		}
	} );
	$('#groepsleidersTable tr').click( selectionHandler);

}

function submitUpdateGroepAttributesRequest( groepId)
{
	var updateRequest= { 
			naam: $("#naamEdit").prop("value"),
			beschrijving: $("#beschrijvingEdit").val(),
			status: $("#statusEdit").prop("value")
	};
	$.ajax({
		url: service_path + "/" + groepId,
		data: JSON.stringify(updateRequest),
		type: "PUT",
		headers: { "If-Unmodified-Since": gGebruikersgroepTimestamp},
		contentType: "application/json; charset=UTF-8",
		success: function( response, textStatus, jqXHR )
		{
			searchGroepen();
			closeWindow( "popupboxEdit");
		},
		error: ajaxErrorHandler
	});
}

function submitUpdateLidmaatschappenRequest( groepId, obj_Table, updStatus)
{
	var anSelected = fnGetSelected( obj_Table );
	var updateRequest= null;
	if (updStatus== "Verwijderd") {
		updateRequest= { 
				deleteLidmaatschappen: new Array()
		};
		for (var i=0;i<anSelected.length;i++) {
			var lidmaatschap_id= getIdFromRow( anSelected[i]);
			var laatstgewijzigd= $(anSelected[i]).attr( "laatstgewijzigd");

			if (lidmaatschap_id ) {
				updateRequest.deleteLidmaatschappen.push( {
					id: lidmaatschap_id,
					laatstgewijzigd: laatstgewijzigd
				});
			}
		}
	} else {
		updateRequest= { 
				updateLidmaatschappen: new Array()
		};
		for (var i=0;i<anSelected.length;i++) {
			var lidmaatschap_id= getIdFromRow( anSelected[i]);
			var laatstgewijzigd= $(anSelected[i]).attr( "laatstgewijzigd");
			if (lidmaatschap_id ) {
				updateRequest.updateLidmaatschappen.push( {
					id: lidmaatschap_id,
					laatstgewijzigd: laatstgewijzigd,
					status: updStatus
				});
			}
		}
	}
	$.ajax({
		url: service_path + "/" + groepId,
		data: JSON.stringify(updateRequest),
		contentType: "application/json; charset=UTF-8",
		type: "PUT",
		headers: { "If-Unmodified-Since": gGebruikersgroepTimestamp},
		success: function( response, textStatus, jqXHR ) {
			retrieveGroepDetails( groepId);
		},
		error: ajaxErrorHandler
	});
}
