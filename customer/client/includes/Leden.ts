


var webSocket;
var messages = document.getElementById("messages");

function openSocket() {
    // Ensures only one connection is open at a time
    if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
        writeResponse("WebSocket is already opened.");
        return;
    }
    // Create a new instance of the websocket
    webSocket = new WebSocket("ws://localhost:8080/jersey/echo");

    /**
     * Binds functions to the listeners for the websocket.
     */
    webSocket.onopen = function (event) {
        // For reasons I can't determine, onopen gets called twice
        // and the first time event.data is undefined.
        // Leave a comment if you know the answer.
        if (event.data === undefined)
            return;

        writeResponse(event.data);
    };

    webSocket.onmessage = function (event) {
        writeResponse(event.data);
        let command = event.data.substring(0, 10);
        let leden= JSON.parse(event.data.substring(10));
        for (let lid of leden) {
            $('#leden').append(`<tr><td>${lid.klant.voornaam}</td><td>${lid.klant.achternaam}</td><td>${lid.rol}</td></tr>`);
        }

    };

    webSocket.onclose = function (event) {
        writeResponse("Connection closed");
    };
}

/**
 * Sends the value of the text input to the server
 */
function send() {
    var text = (<any>document.getElementById("messageinput")).value;
    webSocket.send(text);
}

function closeSocket() {
    webSocket.close();
}

function writeResponse(text) {
    messages.innerHTML += "<br/>" + text;
}

