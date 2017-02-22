/// <reference path="jquery.d.ts" />

class Organisation {
    service_path = "http://localhost:8080/rw/rest/organisatie";
    gOrganisatieId = null;

    obj_ScopeTable = null;
    initialise() {
        $('#closePopupboxEdit').click(function () {
            this.searchOrganisaties();
            this.closeWindow("popupboxEdit");
        });

        $('#closePopupboxCreate').click(function () {
            this.searchOrganisaties();
            this.closeWindow("popupboxCreate");
        });

        $('#closeMain').click(function () {
            window.location.href = "index.html";
        });


        /* Add a click handler to the rows - this could be used as a callback */
        $("#organisatietable tbody").click(function (event) {
            this.jump2EditOrganisatie(this.getIdFromEvent(event));
        });

        $('#but_create').click(function () {
            this.jump2Create();
        });
        $('#but_saveCreate').click(function () {
            // alert(configuration.getServicePath());
            this.submitCreateRequest();
        });
        $('#but_saveEdit').click(function () {
            this.submitUpdateAttributesRequest(this.gOrganisatieId);
        });

        this.searchOrganisaties();

    }


    searchOrganisaties(): any {
        var filter = { string: "" };
        var _this = this;
        this.updateFilterStringWildcard(filter, "organisatieSearchNaam", "naam");
        this.updateFilterString(filter, "organisatieSearchStatus", "status");

        $.ajax({
            url: _this.service_path + filter.string,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                _this.showSearchResults(response);
            },
            cache: false,
            error: _this.ajaxErrorHandler.bind(_this)
        });
    }

    showSearchResults(response) {
        var table: any= $('#organisatietable');
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
        //$('#scopetable tr').click( selectionHandler);
     
    }




    jump2EditOrganisatie(organisatieId): void {
        if (organisatieId != null) {
            this.gOrganisatieId = organisatieId;
        }
        this.popupWindow("popupboxEdit");
        this.retrieveDetails(this.gOrganisatieId);
    }

    retrieveDetails(organisatieId) {
        $.ajax({
            url: this.service_path + "/" + organisatieId,
            type: "GET",
            success: function (response, textStatus, jqXHR) {
                this.showEditData(response);
            },
            cache: false,
            error: this.ajaxErrorHandler.bind(this)
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
        $.ajax({
            url: this.service_path,
            data: JSON.stringify(updateRequest),
            type: "PUT",
            success: function (response, textStatus, jqXHR) {
                this.searchOrganisaties();
                this.closeWindow("popupboxEdit");
            },
            error: this.ajaxErrorHandler.bind(this)
        });
    }



    jump2Create(): void {
        this.popupWindow("popupboxCreate");
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

        $.ajax({
            url: this.service_path,
            data: JSON.stringify(createRequest),
            type: "POST",
            success: function (response, textStatus, jqXHR) {
                this.closeWindow("popupboxCreate");
                this.searchOrganisaties();
            },
            error: this.ajaxErrorHandler.bind(this)
        });
    }


    updateFilterString(filter, tag, paramnaam) {
        var value = $("#" + tag).val();
        if (value != "") {
            if (filter.string == "") {
                filter.string = "?";
            } else {
                filter.string += "&";
            }
            filter.string += paramnaam + '=' + value;
        }
    }
    updateFilterStringWildcard(filter, tag, paramnaam) {
        var value = $("#" + tag).val();
        if (value != "") {
            if (filter.string == "") {
                filter.string = "?";
            } else {
                filter.string += "&";
            }
            // HTML &#42; = *
            filter.string += paramnaam + "=\*" + value + "\*";
        }
    }

    listProperties(obj) {
        var propList = "";
        for (var propName in obj) {
            if (typeof (obj[propName]) != "undefined") {
                propList += (propName + ", ");
            }
        }
        alert(propList);
    }

    getIdFromEvent(event) {
        return this.getIdFromRow($(event.target.parentNode));
    }

    getIdFromRow(row) {
        return $(row).attr("id").substr(3);
    }


    /* Get the rows which are currently selected */
    fnGetSelected(tableLocal) {
        var aReturn = new Array();
        var aTrs = tableLocal.fnGetNodes();
        for (var i = 0; i < aTrs.length; i++) {
            if ($(aTrs[i]).hasClass('row_selected')) {
                aReturn.push(aTrs[i]);
            }
        }
        return aReturn;
    }

    selectionHandler() {
        if ($(this).hasClass('row_selected'))
            $(this).removeClass('row_selected');
        else
            $(this).addClass('row_selected');
    };

    ajaxErrorHandler(response, textStatus, jqXHR): void {
        if (response.status == 0) {
            // geen verbinding -> hier kan de klant niets mee
            return;
        }
        if (response.responseXML != null) {
            let newwindow: any = window.open('Error', 'height=200,width=200');

            newwindow.write(response.responseText);

            if (window.focus) {
                newwindow.focus()
            }
            return;
        }

        if (response.status < 500) {
            alert(response.statusText + "[" + response.status + "]:" + response.responseText);
        } else {
            alert(response.statusText + "[" + response.status + "]:Een systeemfout is opgetreden");
        }
    };
    popupWindow(popupid) {
        $('#' + popupid).fadeIn();
        var popuptopmargin = ($('#' + popupid).height() + 10) / 2;
        var popupleftmargin = ($('#' + popupid).width() + 10) / 2;
        // Then using .css function style our popup box for center alignment
        $('#' + popupid).css({
            'margin-top': -popuptopmargin,
            'margin-left': -popupleftmargin
        });
    }
    closeWindow(popupid) {
        $('#' + popupid).fadeOut();
    }
}
let org = new Organisation();
$(document).ready(function () {
    org.initialise();
});