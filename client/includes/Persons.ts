import { WindowUtil } from "./WindowUtil";
import { FilterUtil, Filter } from "./FilterUtil";
import { Person, Organisation } from "./DataModel";
import { Configuration } from "./Configuration";


class PersonList {
    obj_PersonTable: any = null;
    initialise() {
        /*
        $.ajax({
            url: Configuration.organisation_service,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                var searchPersonOrganisation = $("#searchPersonOrganisation");
                for (let org of response.organisaties) {
                    searchPersonOrganisation.append(`<option value="${org.id}">${org.name}</option>`);
                }
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
        */
    }

    searchPersons(): void {
        var filter = new Filter("?selectOrganisations");

        filter.updateWildcard("name", "name");
        filter.updateWildcard("surname", "surname");
        filter.update("email", "email");
        filter.update("searchPersonOrganisation", "organisatieId");


        let _this = this;
        $.ajax({
            url: Configuration.person_service + filter,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.updateResults(response);
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }

    prepareOrganisation(person: Person) {
        if (person.organisation === undefined) {

            person.organisation = new Organisation("0", "");
        }
    }

    updateResults(response) {

        for (let klant of response.persons) {
            this.prepareOrganisation(klant);
        }

        this.obj_PersonTable = (<any>$('#example')).dataTable({
            "aaData": response.persons,
            "bDestroy": true,
            "aoColumns": [
                { "mData": "name" },
                { "mData": "surname" },
                { "mData": "email" },
                { "mData": "dateofbirth" },
                { "mData": "organisation.name" }
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "PER" + aData.id);
                return nRow;
            }
        });
        $('#example tr').click(FilterUtil.selectionHandler);

    }
}



$(document).ready(function () {
    $('#closePersonAdmin').click(function () {
        window.location.href = "index.html";
    });
    const personlist = new PersonList();
    personlist.initialise();
    let fun = personlist.searchPersons.bind(personlist);
    $('#name').change(fun);
    $('#surname').change(fun);
    $('#email').change(fun);
    $('#status').change(fun);
    $('#searchPersonOrganisation').change(fun);

    fun.apply();
});