
var obj_ScopeTable= null;

$(document).ready(function() {
	/* Add a click handler to the rows - this could be used as a callback */
	$("#scopetable tbody").click(function(event) {
		jump2Edit( getIdFromEvent(event));
	});
	
	$('#but_create').click( function() {
		jump2Create( );
	} );
	
});

function searchEntities() {
	var filter= { string:""
	};
	
	updateFilterStringWildcard( filter, "scopeNaam", "naam");
	updateFilterString( filter, "scopeStatus", "status");
	

	$.ajax({
		url: service_path + filter.string,
		type: "GET",
		success: function( response, textStatus, jqXHR )
		{
			showSearchResults( response);
		},
        cache: false,
		error: ajaxErrorHandler
	});
}

function showSearchResults( response)
{


	$(document).ready(function() {
		obj_ScopeTable= $('#scopetable').dataTable( {
			"aaData": response.scopes,
			"bDestroy": true,
			"aoColumns": [
				{ "mData": "naam" },
       			{ "mData": "status" }
       			],
       		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull) {
       			$(nRow).attr( "id", "LEE" + aData.id);
       			return nRow;
       		}
		} );
		//$('#scopetable tr').click( selectionHandler);
	} );
}

			

