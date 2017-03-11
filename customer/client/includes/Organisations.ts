/// <reference path="./jquery.d.ts" />
import {WindowUtil} from "WindowUtil";
import {FilterUtil, Filter} from "FilterUtil";
import {Configuration} from "Configuration";
import { Person, Organisation } from "DataModel";
class Organisations {
    gOrganisationId = null;
    obj_ScopeTable = null;
    initialise() {
        var _this = this;
  
        $('#closeMain').click(function () {
            window.location.href = "index.html";
        });

        this.searchOrganisations();

    }


    searchOrganisations(): void {
        let filter: Filter= new Filter();
        FilterUtil.updateFilterStringWildcard(filter, "organisationSearchNaam", "naam");
        FilterUtil.updateFilterString(filter, "organisationSearchStatus", "status");
        let _this = this;
        let response: Array<Organisation> = [
            new Organisation("8000", "University"),
            new Organisation("8001", "The Floor"),
            new Organisation("8002", "The Shack")];
            
        _this.showSearchResults(response);

        return;
        /*
        $.ajax({
            url: Configuration.organisation_service + filter.string,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.showSearchResults(response);
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler.bind(FilterUtil)
        });
        */
    }

    showSearchResults(organisations: Array<Organisation>) {
        var table: any = $('#organisationtable');
        this.obj_ScopeTable = table.dataTable({
            "aaData": organisations,
            "bDestroy": true,
            "aoColumns": [
                { "mData": "id" },
                { "mData": "name" }
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "ORG" + aData.id);
                return nRow;
            }
        });
        $('#organisationtable tr').click( FilterUtil.selectionHandler);

    }



}
let org = new Organisations();
$(document).ready(function () {
    org.initialise();
});