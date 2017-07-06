

export class RestClient {
    postRequest(url: string, body: string): Promise<any> {

        return new Promise<any>(
            function (resolve, reject) {
                const request = new XMLHttpRequest();
                request.onload = function () {
                    let self = this as XMLHttpRequest;
                    if (self.status === 201) {
                        resolve(self.response);
                    } else {
                        reject(new Error(self.statusText));
                    }
                };
                request.onerror = function () {
                    let self = this as XMLHttpRequest;
                    reject(new Error('XMLHttpRequest Error: ' + self.statusText));
                };
                request.open('POST', url);
                request.setRequestHeader('Content-Type', 'application/json')
                request.send(body);
            });
    }

    deleteRequest(url: string): Promise<any> {
        return new Promise<any>(
            function (resolve, reject) {
                const request = new XMLHttpRequest();
                request.onload = function () {
                    let self = this as XMLHttpRequest;
                    if (self.status === 200) {
                        resolve(self.response);
                    } else {
                        reject(new Error(self.statusText));
                    }
                };
                request.onerror = function () {
                    let self = this as XMLHttpRequest;
                    reject(new Error('XMLHttpRequest Error: ' + self.statusText));
                };
                request.open('DELETE', url);
                request.send();
            });
    }
    getRequest(url: string): Promise<any> {
        return new Promise<any>(
            function (resolve, reject) {
                const request = new XMLHttpRequest();
                request.onload = function () {
                    let self = this as XMLHttpRequest;
                    if (self.status === 200) {
                        resolve(self.response);
                    } else {
                        reject(new Error(self.statusText));
                    }
                };
                request.onerror = function () {
                    let self = this as XMLHttpRequest;
                    reject(new Error('XMLHttpRequest Error: ' + self.statusText));
                };
                request.open('GET', url);
                request.send();
            });
    }
};

export var restclient = new RestClient();