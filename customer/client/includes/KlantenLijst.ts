import {WindowUtil} from "WindowUtil";
import {FilterUtil} from "FilterUtil";
import {Klant, Organisatie} from "DataModel";

export class KlantenLijst {
    obj_GebruikersTable = null;
    initialise() {

        $.ajax({
            url: "http://localhost:8080/rw/rest/organisatie",
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                var searchGebruikerOrganisatie = $("#searchGebruikerOrganisatie");
                $.each(response.organisaties, function () {
                    searchGebruikerOrganisatie.append($("<option />").val(this.id).text(this.naam));
                });
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });

        //$( "#geboortedatum" ).datepicker({"dateFormat": "yy-mm-dd"});
        //	$("#geboortedatum").datepicker("setDate", new Date());

    }

    searchKlanten() {
        var filter = { string: "?select=organisaties" };

        FilterUtil.updateFilterStringWildcard(filter, "voornaam", "voornaam");
        FilterUtil.updateFilterStringWildcard(filter, "achternaam", "achternaam");
        FilterUtil.updateFilterString(filter, "geslacht", "geslacht");
        FilterUtil.updateFilterString(filter, "emailAdres", "emailAdres");
        FilterUtil.updateFilterString(filter, "status", "status");
        FilterUtil.updateFilterString(filter, "searchKlantOrganisatie", "organisatieId");
        FilterUtil.updateFilterString(filter, "product", "product");
        FilterUtil.updateFilterString(filter, "geplandePeriode", "geplandePeriode");
        FilterUtil.updateFilterString(filter, "rol", "rol");

        let _this = this;
        $.ajax({
            url: "http://localhost:8080/rw/rest/klant" + filter.string,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.updateResults(response);
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }

    prepareInschrijving(klant: Klant) {
        if (klant.inschrijvingen === undefined) {
            klant.inschrijvingen = new Array();
            //[{ organisatie: null, locatie: null, product: "", geplandePeriode: "", rol: "" }];
        }
        for (let inschrijving of klant.inschrijvingen) {
            if (inschrijving.organisatie == null) {
                inschrijving.organisatie = new Organisatie(0, "");
            }
        }
    }

    updateResults(response) {

        for (let klant of response.klanten) {
            this.prepareInschrijving(klant);
        }

        this.obj_GebruikersTable = (<any>$('#example')).dataTable({
            "aaData": response.klanten,
            "bDestroy": true,
            "aoColumns": [
                { "mData": "voornaam" },
                { "mData": "voorletters" },
                { "mData": "achternaam" },
                { "mData": "emailAdres" },
                { "mData": "geslacht" }
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "GEB" + aData.id);
                return nRow;
            }
        });
        $('#example tr').click(FilterUtil.selectionHandler);

    }
}


