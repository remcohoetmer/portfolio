﻿/// <reference path="./jquery.d.ts" />
/// <reference path="./sse.d.ts" />

import { WindowUtil } from "WindowUtil";
import { FilterUtil, Filter } from "FilterUtil";
import { Configuration } from "Configuration";
import { Person, Organisation } from "DataModel";
class Organisations {
    gOrganisationId: number = null;
    obj_ScopeTable: any = null;
    initialise() {
        var _this = this;

        $('#closeMain').click(function () {
            window.location.href = "index.html";
        });

        this.searchOrganisations();
        console.log( $('#organisationSearchName'));
        (<any>$('#organisationSearchName')).change( function () {_this.searchOrganisations()});
        (<any>$('#organisationSearchStatus')).on( "change", this.searchOrganisations.bind(this));

    }


    searchOrganisations(): void {
        let filter: Filter = new Filter();
        FilterUtil.updateFilterStringWildcard(filter, "organisationSearchName", "name");
        FilterUtil.updateFilterString(filter, "organisationSearchStatus", "status");
        let _this = this;


        $('#organisationtable tbody').empty();
        var source = new EventSource(Configuration.organisation_service + filter.string);
        source.addEventListener('message', function (e) {
            console.log(e.data);
            var organisation: Organisation = JSON.parse(e.data);
            _this.showOrganisation(organisation);
        });
        source.onerror = function (e) { source.close(); };

        $('#organisationtable tr').click(FilterUtil.selectionHandler);
    }

    showOrganisation(organisation: Organisation) {

        $('#organisationtable tbody').append(
            `<tr id="org${organisation.id}">
            <td>${organisation.name}</td>
            <td>${organisation.status}</td>
            </tr>`);
    }



}
let org = new Organisations();
$(document).ready(function () {
    org.initialise();
});