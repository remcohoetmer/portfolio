/// <reference path="./jquery.d.ts" />
import {WindowUtil} from "WindowUtil";
import {FilterUtil, Filter} from "FilterUtil";
import {Configuration} from "Configuration";

class Organisation {
    gOrganisatieId = null;
    obj_ScopeTable = null;
    initialise() {
        var _this = this;
        $('#closePopupboxEdit').click(function () {
            _this.searchOrganisaties();
            WindowUtil.closeWindow("popupboxEdit");
        });

        $('#closePopupboxCreate').click(function () {
            _this.searchOrganisaties();
            WindowUtil.closeWindow("popupboxCreate");
        });

        $('#closeMain').click(function () {
            window.location.href = "index.html";
        });


        /* Add a click handler to the rows - this could be used as a callback */
        $("#organisatietable tbody").click(function (event) {
            _this.jump2EditOrganisatie(this.getIdFromEvent(event));
        });

        $('#but_create').click(function () {
            _this.jump2Create();
        });
        $('#but_saveCreate').click(function () {
            // alert(configuration.getServicePath());
            _this.submitCreateRequest();
        });
        $('#but_saveEdit').click(function () {
            _this.submitUpdateAttributesRequest(_this.gOrganisatieId);
        });

        this.searchOrganisaties();

    }


    searchOrganisaties(): void {
        let filter: Filter= new Filter();
        FilterUtil.updateFilterStringWildcard(filter, "organisatieSearchNaam", "naam");
        FilterUtil.updateFilterString(filter, "organisatieSearchStatus", "status");
        let _this = this;
        $.ajax({
            url: Configuration.organisation_service + filter.string,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.showSearchResults(response);
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler.bind(FilterUtil)
        });
    }

    showSearchResults(response) {
        var table: any = $('#organisatietable');
        this.obj_ScopeTable = table.dataTable({
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
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "ORG" + aData.id);
                return nRow;
            }
        });
        $('#organisatietable tr').click( FilterUtil.selectionHandler);

    }


    jump2EditOrganisatie(organisatieId): void {
        if (organisatieId != null) {
            this.gOrganisatieId = organisatieId;
        }
        WindowUtil.popupWindow("popupboxEdit");
        this.retrieveDetails(this.gOrganisatieId);
    }

    retrieveDetails(organisatieId) {
        var _this = this;
        $.ajax({
            url: Configuration.organisation_service + "/" + organisatieId,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.showEditData(response);
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler.bind(FilterUtil)
        });
    }

    showEditData(response) {
        if (response.organisaties.length != 1) {
            return;
        }
        var groep = response.organisaties[0];

        $("#naamEdit").prop("value", groep.naam);
        $("#statusEdit").prop("value", groep.status);
    }

    submitUpdateAttributesRequest(organisatieId) {
        let updateRequest = {
            id: organisatieId,
            naam: $("#naamEdit").val(),
            status: $("#statusEdit").val()
        };
        var _this = this;
        $.ajax({
            url: Configuration.organisation_service,
            data: JSON.stringify(updateRequest),
            type: "PUT",
            success: function (response, textStatus, jqXHR) {
                _this.searchOrganisaties();
                WindowUtil.closeWindow("popupboxEdit");
            },
            error: FilterUtil.ajaxErrorHandler.bind(this)
        });
    }



    jump2Create(): void {
        WindowUtil.popupWindow("popupboxCreate");
        this.initialiseCreate();
    }

    initialiseCreate(): void {
        $("#naamCreate").prop("value", "");
        $("#statusCreate").prop("value", "Actief");
    }

    submitCreateRequest() {
        let createRequest = {
            naam: $("#naamCreate").val(),
            status: $("#statusCreate").val(),
        };
        let _this = this;
        $.ajax({
            url: Configuration.organisation_service,
            data: JSON.stringify(createRequest),
            type: "POST",
            success: function (response, textStatus, jqXHR) {
                WindowUtil.closeWindow("popupboxCreate");
                _this.searchOrganisaties();
            },
            error: FilterUtil.ajaxErrorHandler.bind(FilterUtil)
        });
    }

}
let org = new Organisation();
$(document).ready(function () {
    org.initialise();
});