import {WindowUtil} from "WindowUtil";
import {FilterUtil,Filter} from "FilterUtil";
import {Klant, Organisatie} from "DataModel";
import {KlantenLijst} from "KlantenLijst";
import {Configuration} from "Configuration";

export class Groepen {

    gAdd_Gebruikersgroep = null;
    gAdd_Rol = null;
    klantenLijst: KlantenLijst;
    gGebruikersgroep = null;
    gGebruikersgroepTimestamp = null;

    obj_GroepsleidersTable = null;
    obj_GroepsledenTable = null;

    initialise(klantenLijst: KlantenLijst): void {
        this.klantenLijst = klantenLijst;
        let _this = this;
        $('#but_saveAddLeden').click(function () {
            _this.submitAddLedenRequest(_this.gAdd_Gebruikersgroep.id, _this.gAdd_Rol);
        });
        $('#closePopupboxEdit').click(function () {
            _this.searchGroepen();
            WindowUtil.closeWindow("popupboxEdit");
        });

        $('#closePopupboxCreate').click(function () {
            _this.searchGroepen();
            WindowUtil.closeWindow("popupboxCreate");
        });

        $('#closePopupboxAddLeden').click(function () {
            _this.jump2EditGroep(null);
            WindowUtil.closeWindow("popupboxAddLeden");
        });

        $('#closeGebruikersgroepenBeheer').click(function () {
            window.location.href = "index.html";
        });
        $('#but_saveCreateGroep').click(function () {
            _this.submitGroepCreateRequest();
        });
        $('#but_delete_lid').click(function () {
            _this.submitUpdateLidmaatschappenRequest(_this.gGebruikersgroep.id, _this.obj_GroepsledenTable, "Verwijderd");
        });
        $('#but_activate_lid').click(function () {
            _this.submitUpdateLidmaatschappenRequest(_this.gGebruikersgroep.id, _this.obj_GroepsledenTable, "Actief");
        });
        $('#but_deactivate_lid').click(function () {
            _this.submitUpdateLidmaatschappenRequest(_this.gGebruikersgroep.id, _this.obj_GroepsledenTable, "Passief");
        });
        $('#but_delete_ldr').click(function () {
            _this.submitUpdateLidmaatschappenRequest(_this.gGebruikersgroep.id, _this.obj_GroepsleidersTable, "Verwijderd");
        });
        $('#but_activate_ldr').click(function () {
            _this.submitUpdateLidmaatschappenRequest(_this.gGebruikersgroep.id, _this.obj_GroepsleidersTable, "Actief");
        });
        $('#but_deactivate_ldr').click(function () {
            _this.submitUpdateLidmaatschappenRequest(_this.gGebruikersgroep.id, _this.obj_GroepsleidersTable, "Passief");
        });
        $('#but_saveEdit').click(function () {
            _this.submitUpdateGroepAttributesRequest(_this.gGebruikersgroep.id);
        });

        $('#but_add_lid').click(function () {
            _this.jump2AddLeden({ groep: _this.gGebruikersgroep, rol: "GROEPSLID" });
        });
        $('#but_add_ldr').click(function () {
            _this.jump2AddLeden({ groep: _this.gGebruikersgroep, rol: "GROEPSLEIDER" });
        });
        /* Add a click handler to the rows - this could be used as a callback */
        $("#overviewTable tbody").click(function (event) {
            /*
            $(obj_GroepenTable.fnSettings().aoData).each(function (){
                $(this.nTr).removeClass('row_selected');
            });
            $(event.target.parentNode).addClass('row_selected');
            */
            _this.jump2EditGroep(FilterUtil.getIdFromEvent(event));
        });

        $('#but_create_groep').click(function () {
            _this.jump2CreateGroep();
        });

        $.ajax({
            url: Configuration.scope_service,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                var searchScope = $("#searchScope");
                var scopeCreate = $("#scopeCreate");
                $.each(response.scopes, function () {
                    searchScope.append($("<option />").val(this.id).text(this.naam));
                    scopeCreate.append($("<option />").val(this.id).text(this.naam));
                });
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
        $.ajax({
            url: Configuration.organisation_service,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                var searchOrganisatie = $("#searchOrganisatie");
                var organisatieCreate = $("#organisatieCreate");
                $.each(response.organisaties, function () {
                    searchOrganisatie.append($("<option />").val(this.id).text(this.naam));
                    organisatieCreate.append($("<option />").val(this.id).text(this.naam));
                });
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
        let fun = this.searchGroepen.bind(this);
        $('#searchNaam').change(fun);
        $('#searchBeschrijving').change(fun);
        $('#searchGroepscode').change(fun);
        $('#searchOrganisatie').change(fun);
        $('#searchScope').change(fun);
        $('#searchProduct').change(fun);
        $('#searchKenmerk').change(fun);
        $('#searchStatus').change(fun);
        this.searchGroepen();
    }

    jump2AddLeden(data) {
        this.gAdd_Gebruikersgroep = data.groep;
        this.gAdd_Rol = data.rol;
        WindowUtil.popupWindow("popupboxAddLeden");
        if (data.groep.organisatie != null) {
            $('#searchKlantOrganisatie').val(data.groep.organisatie.id);
        }
        this.klantenLijst.searchKlanten();
    }

    submitAddLedenRequest(groepId, rol) {
        var anSelected = FilterUtil.fnGetSelected(this.klantenLijst.obj_GebruikersTable);
        var updateRequest = {
            createLidmaatschappen: new Array()
        };
        for (var i = 0; i < anSelected.length; i++) {
            var klantId = FilterUtil.getIdFromRow(anSelected[i]);
            if (klantId) {
                updateRequest.createLidmaatschappen.push({
                    gebruiker: { id: klantId },
                    rol: rol
                });
            }
        }
        let _this = this;
        $.ajax({
            url: Configuration.group_service+ "/" + groepId,
            data: JSON.stringify(updateRequest),
            contentType: "application/json; charset=UTF-8",
            type: "PUT",
            headers: { "If-Unmodified-Since": this.gGebruikersgroepTimestamp },
            success: function (response, textStatus, jqXHR) {
                // refresh
                _this.jump2EditGroep(null);
                WindowUtil.closeWindow("popupboxAddLeden");
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }

    jump2CreateGroep() {
        WindowUtil.popupWindow("popupboxCreate");
        this.initialiseCreateGroep();
    }

    initialiseCreateGroep() {
        $("#naamCreate").prop("value", "");
        $("#beschrijvingCreate").text("");
        $("#statusCreate").prop("value", "Actief");
        $("#organisatieCreate").prop("value", "");
        $("#scopeCreate").prop("value", "");
        $("#productCreate").prop("value", "");
        $("#kenmerkenCreate").prop("value", "");
        $("input:radio[name=groepsMutatieTypeCreate][value=MANAGED]").prop("checked", true);
        $("#geplandePeriodeCreate").prop("value", "");
    }

    submitGroepCreateRequest() {
        var kenmerken = null;
        /*
        var kenmerkenString= $("#kenmerkenCreate").val();
        if (kenmerkenString!= "") {
            kenmerken= kenmerkenString.split(" ");
        }*/
        var createRequest: any = {
            naam: $("#naamCreate").val(),
            beschrijving: "Decription",//$("#beschrijvingCreate").val(),
            status: 'Actief',//$("#statusCreate").val(),
            product: '',//$("#productCreate").val(),
            kenmerken: '', //kenmerken,
            groepsMutatieType: 'SELFSERVICE'//$("input[name=groepsMutatieTypeCreate]:checked").val()
        };
        createRequest.organisatie = { id: 8000 };
        createRequest.scope = { id: 123 };
        createRequest.geplandePeriode = '';
        /*
            if ($("#organisatieCreate").val()!= "") {
                createRequest.organisatie= {id: $("#organisatieCreate").val()};
            }
        
            if ($("#scopeCreate").val()!= "") {
                createRequest.scope= { id: $("#scopeCreate").val()};
            }
            if ($("#geplandePeriodeCreate").val()!= "") {
                createRequest.geplandePeriode= $("#geplandePeriodeCreate").val();
            }
            */
        let _this = this;
        $.ajax({
            url: Configuration.group_service,
            data: JSON.stringify(createRequest),
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            success: function (response, textStatus, jqXHR) {
                WindowUtil.closeWindow("popupboxCreate");
                _this.searchGroepen();
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }


    jump2EditGroep(groepId) {

        WindowUtil.popupWindow("popupboxEdit");
        this.retrieveGroepDetails(groepId);
    }

    retrieveGroepDetails(groepId) {
        if (groepId == null && this.gGebruikersgroep != null) {

            groepId = this.gGebruikersgroep.id;
        }
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + groepId +
            "?select=kenmerken,klanten,lidmaatschappen,organisaties,scopes,hoofdgroep",
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                if (response == null) {
                    //alert( "geen groepsdata");
                    return;
                }
                _this.gGebruikersgroep = response;
                _this.gGebruikersgroepTimestamp = jqXHR.getResponseHeader("Last-Modified");
                _this.showGroepsData(_this.gGebruikersgroep);

            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
    }

    showGroepsData(groep) {
        $("#naamEdit").prop("value", groep.naam);
        $("#beschrijvingEdit").text(groep.beschrijving);
        $("#statusEdit").prop("value", groep.status);
        if (groep.organisatie)
            $("#organisatieEdit").prop("value", groep.organisatie.naam);
        else
            $("#organisatieEdit").prop("value", "");
        if (groep.scope)
            $("#scopeEdit").prop("value", groep.scope.naam);
        else
            $("#scopeEdit").prop("value", "");
        if (groep.product)
            $("#productEdit").prop("value", groep.product);
        else
            $("#productEdit").prop("value", "");
        if (groep.kenmerken)
            $("#kenmerkenEdit").prop("value", groep.kenmerken.join(" "));
        else
            $("#kenmerkenEdit").prop("value", "")

        $("#groepsMutatieTypeEdit_" + groep.groepsMutatieType).prop("checked", true);
        if (groep.groepscode)
            $("#groepscodeEdit").prop("value", groep.groepscode);
        else
            $("#groepscodeEdit").prop("value", "")

        if (groep.geplandePeriode)
            $("#geplandePeriodeEdit").prop("value", groep.geplandePeriode);
        else
            $("#geplandePeriodeEdit").prop("value", "");

        if (groep.hoofdgroep)
            $("#hoofdgroepEdit").prop("value", groep.hoofdgroep.naam);
        else
            $("#hoofdgroepEdit").prop("value", "");

        var groepsleiders = new Array();
        var groepsleden = new Array();


        if (groep.lidmaatschappen != undefined) {
            // introduce for null-safety
            for (var lidmaatschap of groep.lidmaatschappen) {
                var lidmaatschap_klant = lidmaatschap.klant;
                // indien niet gedefinieerd: definieer leeg
                this.klantenLijst.prepareInschrijving(lidmaatschap_klant);
                // voeg de status & id van het lidmaatschap toe aan de klantgegevens
                lidmaatschap_klant.status_lidmaatschap = lidmaatschap.status;
                lidmaatschap_klant.laatstgewijzigd = lidmaatschap.laatstgewijzigd;
                lidmaatschap_klant.id_lidmaatschap = lidmaatschap.id;

                if (lidmaatschap.rol == "GROEPSLEIDER") {
                    groepsleiders.push(lidmaatschap_klant);
                } else {
                    groepsleden.push(lidmaatschap_klant);
                }
            }
        }

        var aoColumns = [{ "mData": "status_lidmaatschap" },
            { "mData": "status" },
            { "mData": "voornaam" },
            { "mData": "voorletters" },
            { "mData": "voorvoegselAchternaam" },
            { "mData": "achternaam" },
            { "mData": "emailAdres" },
            { "mData": "geslacht" }
        ];
        this.obj_GroepsledenTable = (<any>$('#groepsledenTable')).dataTable({
            "aaData": groepsleden,
            "bDestroy": true,
            "aoColumns": aoColumns,
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "LID" + aData.id_lidmaatschap);
                $(nRow).attr("laatstgewijzigd", aData.laatstgewijzigd);
                return nRow;
            }
        });
        $('#groepsledenTable tr').click(FilterUtil.selectionHandler);

        this.obj_GroepsleidersTable = (<any>$('#groepsleidersTable')).dataTable({
            "aaData": groepsleiders,
            "bDestroy": true,
            "aoColumns": aoColumns,
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "LDR" + aData.id_lidmaatschap);
                $(nRow).attr("laatstgewijzigd", aData.laatstgewijzigd);
                return nRow;
            }
        });
        $('#groepsleidersTable tr').click(FilterUtil.selectionHandler);

    }

    submitUpdateGroepAttributesRequest(gebruikersgroepId) {
        var updateRequest = {
            naam: $("#naamEdit").prop("value"),
            beschrijving: $("#beschrijvingEdit").val(),
            status: $("#statusEdit").prop("value")
        };
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + gebruikersgroepId,
            data: JSON.stringify(updateRequest),
            type: "PUT",
            headers: { "If-Unmodified-Since": this.gGebruikersgroepTimestamp },
            contentType: "application/json; charset=UTF-8",
            success: function (response, textStatus, jqXHR) {
                _this.searchGroepen();
                WindowUtil.closeWindow("popupboxEdit");
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }

    submitUpdateLidmaatschappenRequest(gebruikersgroepId, obj_Table, updStatus) {
        var anSelected = FilterUtil.fnGetSelected(obj_Table);
        var updateRequest = null;
        if (updStatus === "Verwijderd") {
            updateRequest = {
                deleteLidmaatschappen: new Array()
            };
            for (var i = 0; i < anSelected.length; i++) {
                var lidmaatschap_id = FilterUtil.getIdFromRow(anSelected[i]);
                var laatstgewijzigd = $(anSelected[i]).attr("laatstgewijzigd");

                if (lidmaatschap_id) {
                    updateRequest.deleteLidmaatschappen.push({
                        id: lidmaatschap_id,
                        laatstgewijzigd: laatstgewijzigd
                    });
                }
            }
        } else {
            updateRequest = {
                updateLidmaatschappen: new Array()
            };
            for (var i = 0; i < anSelected.length; i++) {
                var lidmaatschap_id = FilterUtil.getIdFromRow(anSelected[i]);
                var laatstgewijzigd = $(anSelected[i]).attr("laatstgewijzigd");
                if (lidmaatschap_id) {
                    updateRequest.updateLidmaatschappen.push({
                        id: lidmaatschap_id,
                        laatstgewijzigd: laatstgewijzigd,
                        status: updStatus
                    });
                }
            }
        }
        let _this = this;
        $.ajax({
            url: Configuration.group_service + "/" + gebruikersgroepId,
            data: JSON.stringify(updateRequest),
            contentType: "application/json; charset=UTF-8",
            type: "PUT",
            headers: { "If-Unmodified-Since": this.gGebruikersgroepTimestamp },
            success: function (response, textStatus, jqXHR) {
                _this.retrieveGroepDetails(gebruikersgroepId);
            },
            error: FilterUtil.ajaxErrorHandler
        });
    }


    searchGroepen() {
        let filter = new Filter ("?select=organisaties,scopes" );

        filter.updateWildcard("searchNaam", "naam");
        filter.updateWildcard("searchBeschrijving", "beschrijving");
        filter.update("searchGroepscode", "groepscode");
        filter.update("searchOrganisatie", "organisatieId");
        filter.update("searchScope", "scopeId");
        filter.update("searchProduct", "product");
        filter.update("searchStatus", "status");
        filter.update("searchKenmerk", "kenmerk");
        let _this = this;
        $.ajax({
            url: Configuration.group_service + filter.string,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.showGroepSearchResults(response);
            },
            cache: false,
            error: FilterUtil.ajaxErrorHandler
        });
    }

    showGroepSearchResults(response) {
        // pre-processing
        for (let groep of response.groepen) {
            if (groep.organisatie == null) {
                groep.organisatie = { "naam": "" };
            }
            if (groep.scope == null) {
                groep.scope = { "naam": "" };
            }
        }
        var obj_GroepenTable = (<any>$('#overviewTable')).dataTable({
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
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", "GRP" + aData.id);
                return nRow;
            }
        });
    }

}

$(document).ready(function () {
    let groepen = new Groepen();
    let klantenLijst = new KlantenLijst();
    klantenLijst.initialise();
    groepen.initialise(klantenLijst);

});

