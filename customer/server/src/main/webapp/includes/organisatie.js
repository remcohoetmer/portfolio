
var obj_ScopeTable= null;

$(document).ready(function() {
	/* Add a click handler to the rows - this could be used as a callback */
	$("#organisatietable tbody").click(function(event) {
		jump2EditOrganisatie( getIdFromEvent(event));
	});
	
	$('#but_create').click( function() {
		jump2Create( );
	} );
	
});

function searchOrganisaties() {
	var filter= { string:""};
	
	updateFilterStringWildcard( filter, "organisatieSearchNaam", "naam");
	updateFilterString( filter, "organisatieSearchStatus", "status");

	$.ajax({
		url: service_path + filter.string,
		type: "GET",
		success: function( response, textStatus, jqXHR ) {
			showSearchResults( response);
		},
        cache: false,
		error: ajaxErrorHandler
	});
}

function showSearchResults( response)
{

	$(document).ready(function() {
		obj_ScopeTable= $('#organisatietable').dataTable( {
			"aaData": response.organisaties,
			"bDestroy": true,
			"aoColumns": [
			    { "mData": "naam" },
				{ "mData": "straatnaam" },
				{ "mData": "nummer" },
				{ "mData": "postcode" },
				{ "mData": "plaatsnaam" },
				{ "mData": "kvkNummer" },
       			{ "mData": "status" }
       			],
       		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull) {
       			$(nRow).attr( "id", "ORG" + aData.id);
       			return nRow;
       		}
		} );
		//$('#scopetable tr').click( selectionHandler);
	} );
}

			

