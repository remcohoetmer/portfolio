
var obj_GebruikersTable= null;

$(document).ready(function() {
	$.ajax({
		url: "rest/organisatie",
		type: "GET",
		success: function( response, textStatus, jqXHR ){
			var searchGebruikerOrganisatie = $("#searchGebruikerOrganisatie");
			$.each(response.organisaties, function() {
				searchGebruikerOrganisatie.append($("<option />").val(this.id).text(this.naam));
			});
		},
        cache: false,
		error: ajaxErrorHandler
	});
	
	//$( "#geboortedatum" ).datepicker({"dateFormat": "yy-mm-dd"});
	//	$("#geboortedatum").datepicker("setDate", new Date());
});

function searchGebruikers() {
	var filter = { string: "?select=organisaties"};
	
	updateFilterStringWildcard( filter, "voornaam", "voornaam");
	updateFilterStringWildcard( filter, "achternaam", "achternaam");
//	updateFilterString( filter, "geboortedatum", "geboortedatum");
	updateFilterString( filter, "geslacht", "geslacht");
	updateFilterString( filter, "emailAdres", "emailAdres");
	updateFilterString( filter, "status", "status");
	updateFilterString( filter, "searchGebruikerOrganisatie", "organisatieId");
	updateFilterString( filter, "searchGebruikerLocatie", "locatieId");
	updateFilterString( filter, "leerrichting", "leerrichting");
	updateFilterString( filter, "jaargroep", "jaargroep");
	updateFilterString( filter, "rol", "rol");
	

	$.ajax({
		url: "rest/klant" + filter.string,
		type: "GET",
		success: function( response, textStatus, jqXHR ){
			updateResults( response);
		},
		error: ajaxErrorHandler
	});
}



function updateResults( response)
{
	for (i=0;i<response.klanten.length;i++) {
		prepareInschrijving( response.klanten[i]);
	}
	$(document).ready(function() {
		obj_GebruikersTable= $('#example').dataTable( {
			"aaData": response.klanten,
			"bDestroy": true,
			"aoColumns": [
				{ "mData": "voornaam" },
				{ "mData": "voorletters" },
    			{ "mData": "voorvoegselAchternaam" },
    			{ "mData": "achternaam" },
    			{ "mData": "emailAdres" },
    			{ "mData": "geslacht" }
       			],
       		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull) {
       			$(nRow).attr( "id", "GEB" + aData.id);
       			return nRow;
       		}
		} );
		$('#example tr').click( selectionHandler);
	} );
}

			

