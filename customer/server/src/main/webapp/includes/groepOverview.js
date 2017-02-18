
//var obj_GroepenTable= null;
$(document).ready(function() {
	/* Add a click handler to the rows - this could be used as a callback */
	$("#overviewTable tbody").click(function(event) {
		/*
		$(obj_GroepenTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});
		$(event.target.parentNode).addClass('row_selected');
		*/
		jump2EditGroep( getIdFromEvent(event));
	});
	
	$('#but_create_groep').click( function() {
		jump2CreateGroep( );
	} );
	
	$.ajax({
		url: "rest/scope",
		type: "GET",
		success: function( response, textStatus, jqXHR ){
			var searchScope = $("#searchScope");
			var scopeCreate = $("#scopeCreate");
			$.each(response.scopes, function() {
				searchScope.append($("<option />").val(this.id).text(this.naam));
				scopeCreate.append($("<option />").val(this.id).text(this.naam));
			});
		},
        cache: false,
		error: ajaxErrorHandler
	});
	$.ajax({
		url: "rest/organisatie",
		type: "GET",
		success: function( response, textStatus, jqXHR ){
			var searchOrganisatie = $("#searchOrganisatie");
			var organisatieCreate = $("#organisatieCreate");
			$.each(response.organisaties, function() {
				searchOrganisatie.append($("<option />").val(this.id).text(this.naam));
				organisatieCreate.append($("<option />").val(this.id).text(this.naam));
			});
		},
        cache: false,
		error: ajaxErrorHandler
	});
	
	
});

function searchGroepen() {
	var filter= { string: "?select=organisaties,scopes" };
	
	updateFilterStringWildcard( filter, "searchNaam", "naam");
	updateFilterStringWildcard( filter, "searchBeschrijving", "beschrijving");
	updateFilterString( filter, "searchGroepscode", "groepscode");
	updateFilterString( filter, "searchOrganisatie", "organisatieId");
	updateFilterString( filter, "searchScope", "scopeId");
	updateFilterString( filter, "searchProduct", "product");
	updateFilterString( filter, "searchStatus", "status");
	updateFilterString( filter, "searchKenmerk", "kenmerk");
	$.ajax({
		url: "rest/groep"+ filter.string,
		type: "GET",
		success: function( response, textStatus, jqXHR ) {
			showGroepSearchResults( response);
		},
        cache: false,
		error: ajaxErrorHandler
	});
}

function showGroepSearchResults( response)
{
	// pre-processing
	for (i=0;i<response.groepen.length;i++) {
		var groep= response.groepen[i];
		if (groep.organisatie == null) {
			groep.organisatie = { "naam": ""};
		}
		if (groep.scope == null) {
			groep.scope = { "naam": ""};
		}
	}
	var obj_GroepenTable= $('#overviewTable').dataTable( {
		"aaData": response.groepen,
		"bDestroy": true,
		"aoColumns": [
			{ "mData": "status" },
			{ "mData": "naam" },
			{ "mData": "beschrijving" },
			{ "mData": "groepsMutatieType" },
			{ "mData": "groepscode" },
			{ "mData": "organisatie.naam" },
			{ "mData": "scope.naam" },
			{ "mData": "product" },
			{ "mData": "geplandePeriode" }
		],
//		"iDisplayLength": "20",
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull) {
			 $(nRow).attr( "id", "GRP" + aData.id);
		     return nRow;
		}
	} );
}
	
