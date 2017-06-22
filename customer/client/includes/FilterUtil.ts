
export class Filter {
    constructor(public string: string = "") { }
    update(tag: string, paramnaam: string) {
        var value = $("#" + tag).val();
        if (value != undefined && value != "") {
            if (this.string == "") {
                this.string = "?";
            } else {
                this.string += "&";
            }
            this.string += paramnaam + '=' + value;
        }
    }
    updateWildcard(tag: string, paramnaam: string) {
        var value = $("#" + tag).val();
        if (value != "") {
            if (this.string == "") {
                this.string = "?";
            } else {
                this.string += "&";
            }
            // HTML &#42; = *
            this.string += paramnaam + "=\*" + value + "\*";
        }
    }
    toString():string  {
        return this.string;
    }
}
export class FilterUtil {
    static updateFilterString(filter: Filter, tag: string, paramnaam: string) {
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
    static updateFilterStringWildcard(filter: Filter, tag:string, paramnaam:string) {
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

    static listProperties(obj:any) {
        var propList = "";
        for (var propName in obj) {
            if (typeof (obj[propName]) != "undefined") {
                propList += (propName + ", ");
            }
        }
        alert(propList);
    }

    static getIdFromEvent(event: Event) :number {
        return Number(FilterUtil.getIdFromRow(<string><any>$((<Node>event.target).parentNode)));
    }

    static getIdFromRow(row:string) {
        return $(row).attr("id").substr(3);
    }


    /* Get the rows which are currently selected */
    static fnGetSelected(tableLocal:any) {
        var aReturn = new Array();
        var aTrs = tableLocal.fnGetNodes();
        for (var i = 0; i < aTrs.length; i++) {
            if ($(aTrs[i]).hasClass('row_selected')) {
                aReturn.push(aTrs[i]);
            }
        }
        return aReturn;
    }

    static selectionHandler () {
        if ($(this).hasClass('row_selected'))
            $(this).removeClass('row_selected');
        else
            $(this).addClass('row_selected');
    };

    static ajaxErrorHandler(response:any, textStatus:string, jqXHR:any) {
        if (response.status == 0) {
            // geen verbinding -> hier kan de klant niets mee
            return;
        }
        if (response.responseXML != null) {
            var newwindow = <any>window.open('Error', 'height=200,width=200');

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



}