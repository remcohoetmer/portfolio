/// <reference path="lib/jquery.d.ts" />
/// <reference path="lib/sse.d.ts" />
/// <reference path="../node_modules/@types/rx-dom/index.d.ts" />

import { WindowUtil } from "./WindowUtil";
import { FilterUtil, Filter } from "./FilterUtil";
import { Configuration } from "./Configuration";
import { Person, Organisation } from "./DataModel";

class Organisations {
    gOrganisationId: number = null;
    obj_ScopeTable: any = null;
    initialise() {
        var _this = this;
        $('#closeMain').click(function () {
            window.location.href = "index.html";
        });
        $('#organisationtable tr').click(FilterUtil.selectionHandler);

        this.searchOrganisations();
        (<any>$('#organisationSearchName')).on("change", this.searchOrganisations.bind(this));
        (<any>$('#organisationSearchStatus')).on("change", this.searchOrganisations.bind(this));
    }

    searchOrganisations(): void {
        let filter: Filter = new Filter();
        FilterUtil.updateFilterStringWildcard(filter, "organisationSearchName", "name");
        FilterUtil.updateFilterString(filter, "organisationSearchStatus", "status");

        let url = Configuration.organisation_service + filter.string;

        Rx.DOM.fromEventSource(url,
            Rx.Observer.create<string>(this.clearOrganisations, this.errorHandler("Open")))
            .map(json => JSON.parse(json))
            .subscribe(this.showOrganisation, this.errorHandler("Data"));
    }
    errorHandler(eventName: string) {
        return function (e: any) {
            console.log(eventName + " Error: ", e);
        }
    }

    clearOrganisations(): void {
        $('#organisationtable tbody').empty();
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