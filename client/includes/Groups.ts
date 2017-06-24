
/// <reference path="./sse.d.ts" />
import { WindowUtil } from "WindowUtil";
import { FilterUtil, Filter } from "FilterUtil";
import { Person, Organisation, Group, Scope } from "DataModel";
import { PersonList } from "PersonList";
import { Configuration } from "Configuration";

export class Groups {

    gAddGroup: any = null;
    gAdd_Rol: any = null;
    personList: PersonList;
    gGroup: any = null;
    gGroupTimestamp: any = null;

    obj_groupsleidersTable: any = null;
    obj_groupsledenTable: any = null;

    initialise(personList: PersonList): void {
        Configuration.check_config();
        this.personList = personList;
        let _this = this;
        $('#but_saveAddLeden').click(function () {
            _this.submitAddLedenRequest(_this.gAddGroup.id, _this.gAdd_Rol);
        });
        $('#closePopupboxEdit').click(function () {
            _this.searchGroups();
            WindowUtil.closeWindow("popupboxEdit");
        });

        $('#closePopupboxCreate').click(function () {
            _this.searchGroups();
            WindowUtil.closeWindow("popupboxCreate");
        });

        $('#closePopupboxAddLeden').click(function () {
            _this.jump2EditGroup(null);
            WindowUtil.closeWindow("popupboxAddLeden");
        });

        $('#closeGroupAdmin').click(function () {
            window.location.href = "index.html";
        });
        $('#but_saveCreateGroup').click(function () {
            _this.submitGroupCreateRequest();
        });



        $('#but_delete_lid').click(function () {
            _this.submitUpdatemembershipsRequest(_this.gGroup.id, _this.obj_groupsledenTable, "Verwijderd");
        });
        $('#but_activate_lid').click(function () {
            _this.submitUpdatemembershipsRequest(_this.gGroup.id, _this.obj_groupsledenTable, "Actief");
        });
        $('#but_deactivate_lid').click(function () {
            _this.submitUpdatemembershipsRequest(_this.gGroup.id, _this.obj_groupsledenTable, "Passief");
        });
        $('#but_delete_ldr').click(function () {
            _this.submitUpdatemembershipsRequest(_this.gGroup.id, _this.obj_groupsleidersTable, "Verwijderd");
        });
        $('#but_activate_ldr').click(function () {
            _this.submitUpdatemembershipsRequest(_this.gGroup.id, _this.obj_groupsleidersTable, "Actief");
        });
        $('#but_deactivate_ldr').click(function () {
            _this.submitUpdatemembershipsRequest(_this.gGroup.id, _this.obj_groupsleidersTable, "Passief");
        });
        $('#but_saveEdit').click(function () {
            _this.submitUpdateGroupAttributesRequest(_this.gGroup.id);
        });
        $('#but_deleteGroup').click(function () {
            _this.submitDeleteGroupRequest(_this.gGroup.id);
        });
        /* Add a click handler to the rows - this could be used as a callback */
        $("#overviewTable tbody").click(function (event) {
            /*
            $(obj_groupsTable.fnSettings().aoData).each(function (){
                $(this.nTr).removeClass('row_selected');
            });
            $(event.target.parentNode).addClass('row_selected');
            */
            _this.jump2EditGroup(FilterUtil.getIdFromEvent(event));
        });

        $('#but_create_group').click(function () {
            _this.jump2CreateGroup();
        });


        var source = new EventSource(Configuration.organisation_service);
        source.addEventListener('message', function (e) {
            var org = JSON.parse(e.data);
            //console.log("SSE: " + e.data);
            _this.populateOrganisation(org);
        });
        source.onerror = function (e) { source.close(); };

        let src = new EventSource(Configuration.scope_service);
        src.addEventListener('message', function (e) {
            var scope = JSON.parse(e.data);
            console.log("SSE: " + e.data);
            _this.populateScope(scope);
        });
        src.onerror = function (e) { src.close(); };


        let fun = this.searchGroups.bind(this);
        $('#searchMame').change(fun);
        $('#searchDescription').change(fun);
        $('#searchCode').change(fun);
        $('#searchOrganisation').change(fun);
        $('#searchScope').change(fun);
        $('#searchFeature').change(fun);
        $('#searchStatus').change(fun);
        this.searchGroups();
    }
    populateOrganisation(organisation: Organisation) {
        var searchorganisation = $("#searchOrganisation");
        var organisationCreate = $("#organisationCreate");
        let option = `<option value="${organisation.id}">${organisation.name}</option>`;
        searchorganisation.append(option);
        organisationCreate.append(option);
    }
    populateScope(scope: Scope) {
        var searchScope = $("#searchScope");
        var scopeCreate = $("#scopeCreate");
        let option = `<option value="${scope.id}">${scope.name}</option>`;
        searchScope.append(option);
        scopeCreate.append(option);
    }
    jump2AddLeden(data) {
        this.gAddGroup = data.group;
        this.gAdd_Rol = data.rol;
        WindowUtil.popupWindow("popupboxAddLeden");
        if (data.group.organisation != null) {
            $('#searchpersonorganisation').val(data.group.organisation.id);
        }
        this.personList.searchPersons();
    }

    submitAddLedenRequest(groupId: number, rol) {
        var anSelected = FilterUtil.fnGetSelected(this.personList.obj_PersonTable);
        var updateRequest = {
            creatememberships: new Array()
        };
        for (var i = 0; i < anSelected.length; i++) {
            var personId = FilterUtil.getIdFromRow(anSelected[i]);
            if (personId) {
                updateRequest.creatememberships.push({
                    gebruiker: { id: personId },
                    rol: rol
                });
            }
        }
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + groupId,
            data: JSON.stringify(updateRequest),
            contentType: "application/json; charset=UTF-8",
            type: "PUT",
            //            headers: { "If-Unmodified-Since": this.gGroupTimestamp },
            success: function (response, textStatus, jqXHR) {
                // refresh
                _this.jump2EditGroup(null);
                WindowUtil.closeWindow("popupboxAddLeden");
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }

    jump2CreateGroup() {
        WindowUtil.popupWindow("popupboxCreate");
        this.initialiseCreategroup();
    }

    initialiseCreategroup() {
        $("#nameCreate").prop("value", "");
        $("#descriptionCreate").text("");
        $("#statusCreate").prop("value", "Active");
        $("#organisationCreate").prop("value", "");
        $("#scopeCreate").prop("value", "");
        $("#featuresCreate").prop("value", []);
    }

    submitGroupCreateRequest() {
        var features = null;

        var featuresString = $("#featuresCreate").val();
        if (featuresString != "") {
            features = featuresString.split(" ");
        }
        var createRequest: any = {
            name: $("#nameCreate").val(),
            description: $("#descriptionCreate").val(),
            status: $("#statusCreate").val(),
            features: features
        };

        if ($("#organisationCreate").val() != "") {
            createRequest.organisation = { id: $("#organisationCreate").val() };
        }
        if ($("#scopeCreate").val() != "") {
            createRequest.scope = { id: $("#scopeCreate").val() };
        }


        let _this = this;
        $.ajax({
            url: Configuration.group_service,
            data: JSON.stringify(createRequest),
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            success: function (response, textStatus, jqXHR) {
                WindowUtil.closeWindow("popupboxCreate");
                _this.searchGroups();
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }


    jump2EditGroup(groupId: number) {

        WindowUtil.popupWindow("popupboxEdit");
        this.retrieveGroupDetails(groupId);
    }

    retrieveGroupDetails(groupId: number) {
        if (groupId == null && this.gGroup != null) {

            groupId = this.gGroup.id;
        }
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + groupId +
            "?selectPersons&selectOrganisations&selectScopes",
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                if (response == null) {
                    //alert( "geen groupsdata");
                    return;
                }
                _this.gGroup = response;
                _this.gGroupTimestamp = jqXHR.getResponseHeader("Last-Modified");
                _this.showgroupsData(_this.gGroup);

            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
    }

    showgroupsData(group) {
        $("#nameEdit").prop("value", group.name);
        $("#descriptionEdit").text(group.description);
        $("#statusEdit").prop("value", group.status);
        if (group.organisation)
            $("#organisationEdit").prop("value", group.organisation.name);
        else
            $("#organisationEdit").prop("value", "");
        if (group.scope)
            $("#scopeEdit").prop("value", group.scope.name);
        else
            $("#scopeEdit").prop("value", "");
        if (group.features)
            $("#featuresEdit").prop("value", group.features.join(" "));
        else
            $("#featuresEdit").prop("value", "")
        if (group.code)
            $("#codeEdit").prop("value", group.code);
        else
            $("#codeEdit").prop("value", "")

        if (group.master)
            $("#masterEdit").prop("value", group.master.name);
        else
            $("#masterEdit").prop("value", "");

        var groupsleiders = new Array();
        var groupsleden = new Array();


        if (group.memberships != undefined) {
            // introduce for null-safety
            for (var membership of group.memberships) {
                var membership_person = membership.person;
                // indien niet gedefinieerd: definieer leeg
                this.personList.prepareOrganisation(membership_person);
                // voeg de status & id van het membership toe aan de persongegevens
                membership_person.status_membership = membership.status;
                membership_person.id_membership = membership.id;

                if (membership.rol == "groupSLEIDER") {
                    groupsleiders.push(membership_person);
                } else {
                    groupsleden.push(membership_person);
                }
            }
        }

        var aoColumns = [{ "mData": "status_membership" },
        { "mData": "status" },
        { "mData": "name" },
        { "mData": "surname" },
        { "mData": "emailAdres" },
        { "mData": "dateofbirth" }
        ];
    }

    submitUpdateGroupAttributesRequest(groupId: number) {
        var updateRequest = {
            name: $("#nameEdit").prop("value"),
            description: $("#descriptionEdit").val(),
            status: $("#statusEdit").prop("value")
        };
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + groupId,
            data: JSON.stringify(updateRequest),
            type: "PUT",
            //          headers: { "If-Unmodified-Since": this.gGroupTimestamp },
            contentType: "application/json; charset=UTF-8",
            success: function (response, textStatus, jqXHR) {
                _this.searchGroups();
                WindowUtil.closeWindow("popupboxEdit");
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }
    submitDeleteGroupRequest(groupId: number) {
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + groupId,
            type: "DELETE",
            //          headers: { "If-Unmodified-Since": this.gGroupTimestamp },
            success: function (response, textStatus, jqXHR) {
                _this.searchGroups();
                WindowUtil.closeWindow("popupboxEdit");
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }

    submitUpdatemembershipsRequest(groupId: number, obj_Table, updStatus) {
        var anSelected = FilterUtil.fnGetSelected(obj_Table);
        var updateRequest = null;
        if (updStatus === "Verwijderd") {
            updateRequest = {
                deletememberships: new Array()
            };
            for (var i = 0; i < anSelected.length; i++) {
                var membership_id = FilterUtil.getIdFromRow(anSelected[i]);
                var laatstgewijzigd = $(anSelected[i]).attr("laatstgewijzigd");

                if (membership_id) {
                    updateRequest.deletememberships.push({
                        id: membership_id,
                        laatstgewijzigd: laatstgewijzigd
                    });
                }
            }
        } else {
            updateRequest = {
                updatememberships: new Array()
            };
            for (var i = 0; i < anSelected.length; i++) {
                var membership_id = FilterUtil.getIdFromRow(anSelected[i]);
                var laatstgewijzigd = $(anSelected[i]).attr("laatstgewijzigd");
                if (membership_id) {
                    updateRequest.updatememberships.push({
                        id: membership_id,
                        laatstgewijzigd: laatstgewijzigd,
                        status: updStatus
                    });
                }
            }
        }
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + groupId,
            data: JSON.stringify(updateRequest),
            contentType: "application/json; charset=UTF-8",
            type: "PUT",
            headers: { "If-Unmodified-Since": this.gGroupTimestamp },
            success: function (response, textStatus, jqXHR) {
                _this.retrieveGroupDetails(groupId);
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }


    searchGroups() {
        let filter = new Filter("?selectOrganisations&selectScopes");

        filter.updateWildcard("searchName", "name");
        filter.updateWildcard("searchDescription", "description");
        filter.update("searchCode", "code");
        filter.update("searchOrganisation", "organisationId");
        filter.update("searchScope", "scopeId");
        filter.update("searchStatus", "status");
        filter.update("searchFeatures", "feature");
        let _this = this;
        $('#overviewTable tbody').empty();
        var source = new EventSource(Configuration.group_service + filter.string);
        source.addEventListener('message', function (e) {
            var group: Group = JSON.parse(e.data);
            console.log("JSON: " + e.data);
            console.log("Group: " + group);
            _this.showGroup(group);
        });
        source.onerror = function (e) { source.close(); };
    }

    showGroup(group: Group) {
        console.log("Group: " + group);
        if (group.organisation == null) {
            group.organisation = new Organisation();
        }
        if (group.scope == null) {
            group.scope = new Scope();
        }
        $('#overviewTable tbody').append(
            `<tr id="grp${group.id}" onclick='alert("Group id="+ this.id)'>
            <td>${group.name}</td>
            <td>${group.description}</td>
            <td>${group.organisation.name}</td>
            <td>${group.scope.name}</td>
            <td>${group.status}</td>
            </tr>`);
    }
}

$(document).ready(function () {
    let groups = new Groups();
    let personList = new PersonList();
    personList.initialise();
    groups.initialise(personList);

});

