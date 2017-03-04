
export class Configuration {
    private static service_url = "http://localhost:8080/rw/resources/";
    static group_service = Configuration.service_url + "group";
    static organisation_service = Configuration.service_url + "organisation";
    static customer_service = Configuration.service_url + "customer";
    static scope_service = "http://localhost:8081/api/" + "scope";
}