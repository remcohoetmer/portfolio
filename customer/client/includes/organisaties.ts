//import * as $ from "jquery";

class Configuration {

    service_path = "http://localhost:8080/rw/rest";
    getServicePath(): string {
        console.log("typescript " + this.service_path);
        return this.service_path;
    }
}
var configuration = new Configuration();

