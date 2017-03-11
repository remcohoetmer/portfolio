/// <reference path="./jquery.d.ts" />
import {WindowUtil} from "WindowUtil";
import {FilterUtil, Filter} from "FilterUtil";
import {Configuration} from "Configuration";

class Scopes {

    obj_ScopeTable = null;
    gScopeId = null;
    gScopeTimestamp = null;

    initialise() {
        let _this = this;


        /* Add a click handler to the rows - this could be used as a callback */
        $("#scopetable tbody").click(function (event) {
            _this.jump2Edit(FilterUtil.getIdFromEvent(event));
        });

        $('#but_create').click(function () {
            _this.jump2Create();
        });
        $('#closePopupboxEdit').click(function () {
            _this.searchEntities();
            WindowUtil.closeWindow("popupboxEdit");
        });

        $('#closePopupboxCreate').click(function () {
            _this.searchEntities();
            WindowUtil.closeWindow("popupboxCreate");
        });

        $('#closeMain').click(function () {
            window.location.href = "index.html";
        });

        $('#but_saveCreate').click(function () {
            _this.submitCreateRequest();
        });
        $('#but_saveEdit').click(function () {
            _this.submitUpdateAttributesRequest(_this.gScopeId);
        });
        $('#scopeNaamFilter').change(_this.searchEntities.bind(_this));
        $('#scopeStatusFilter').change(_this.searchEntities.bind(_this));

        this.searchEntities();

    }

    searchEntities() {
        let _this = this;
        let filter = new Filter();
        filter.updateWildcard("scopeNaamFilter", "name");
        filter.update("scopeStatusFilter", "status");
        $.ajax({
            url: Configuration.scope_service + filter.string,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.showSearchResults(response);
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
    }

    showSearchResults(response) {


        $(document).ready(function () {
            this.obj_ScopeTable = (<any>$('#scopetable')).dataTable({
                "aaData": response,
                "bDestroy": true,
                "aoColumns": [
                    { "mData": "name" },
                    { "mData": "status" }
                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $(nRow).attr("id", "LEE" + aData.id);
                    return nRow;
                }
            });
            //$('#scopetable tr').click( selectionHandler);
        });
    }



    jump2Create() {
        WindowUtil.popupWindow("popupboxCreate");
        this.initialiseCreate();
    }

    initialiseCreate() {
        $("#nameCreate").prop("value", "");
        $("#statusCreate").prop("value", "Active");
    }

    submitCreateRequest() {
        var createRequest = {
            name: $("#nameCreate").val(),
            status: $("#statusCreate").val(),
        };
        let _this = this;

        $.ajax({
            url: Configuration.scope_service,
            data: JSON.stringify(createRequest),
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            success: function (response, textStatus, jqXHR) {
                WindowUtil.closeWindow("popupboxCreate");
                _this.searchEntities();
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }


    jump2Edit(scopeId) {
        if (scopeId != null) {
            this.gScopeId = scopeId;
        }
        WindowUtil.popupWindow("popupboxEdit");
        this.retrieveDetails(this.gScopeId);
    }

    retrieveDetails(scopeId) {
        let _this = this;
        $.ajax({
            url: Configuration.scope_service + "/" + scopeId,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.gScopeTimestamp = jqXHR.getResponseHeader("Last-Modified");
                _this.showEditData(response);
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
    }

    showEditData(scope) {
        if (scope == null) {
            return;
        }

        $("#nameEdit").prop("value", scope.name);
        $("#statusEdit").prop("value", scope.status);
    }

    submitUpdateAttributesRequest(scopeId) {
        var updateRequest = {
            name: $("#nameEdit").val(),
            status: $("#statusEdit").val()
        };
        let _this = this;
           // headers: { "If-Unmodified-Since": _this.gScopeTimestamp },
        $.ajax({
            url: Configuration.scope_service + "/" + scopeId,
            data: JSON.stringify(updateRequest)  ,
            type: "PUT",
            contentType: "application/json; charset=UTF-8",
            success: function (response, textStatus, jqXHR) {
                _this.searchEntities();
                WindowUtil.closeWindow("popupboxEdit");
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }
}
$(document).ready(function () {
    var scopes = new Scopes();
    scopes.initialise();
});
