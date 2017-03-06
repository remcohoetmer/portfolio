interface fetchInterface {
    (arg: string): any;
}
declare var fetch: fetchInterface;

class Streaming {
    handler(response) {
        var reader = response.body.getReader();
        var bytesReceived = 0;

        reader.read().then(function processResult(result) {
            if (result.done) {
                $("#out").append("Fetch complete");
                return;
            }
            bytesReceived += result.value.length;

            $("#out").append(`Received ${bytesReceived} bytes of data so far<br/>`);
            return reader.read().then(processResult);
        });
    };
    proc(): void {
        var p = fetch('https://html.spec.whatwg.org/');
        p.then(this.handler.bind(this));
    }
}
$(document).ready(function () {
    new Streaming().proc();
});
