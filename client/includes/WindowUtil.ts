/// <reference path="lib/jquery.d.ts" />

export class WindowUtil {
    static errorHandler(eventName: string) {
        return function (e: any) {
            console.log(eventName + " Error: ", e);
        }
    }

    static popupWindow(popupid:string) {
        $('#' + popupid).fadeIn();
        var popuptopmargin = ($('#' + popupid).height() + 10) / 2;
        var popupleftmargin = ($('#' + popupid).width() + 10) / 2;
        // Then using .css function style our popup box for center alignment
        $('#' + popupid).css({
            'margin-top': -popuptopmargin,
            'margin-left': -popupleftmargin
        });
    }
    static closeWindow(popupid:string) {
        $('#' + popupid).fadeOut();
    }
    static initialise() {
        $(document).ready(function () {

            // Here we will write a function when link click under class popup				   
            $('a.popup').click(function () {
                var popupid = $(this).attr('rel');
                WindowUtil.popupWindow(popupid);


            });
            // Here we will write a function when link click under class close				   
            $('a.close').click(function () {
                var popupid = $(this).attr('rel');
                WindowUtil.closeWindow(popupid);
            });
        });

    }
}
export var windowUtil = new WindowUtil();
export var Util = WindowUtil;
//windowUtil.initialise();
