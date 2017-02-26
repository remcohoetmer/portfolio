
export class Configuration {
    private static service_url = "http://localhost:8080/rw/resources/";
    static group_service = Configuration.service_url + "group";
    static organisation_service = Configuration.service_url + "organisatie";
    static customer_service = Configuration.service_url + "klant";
    static scope_service = Configuration.service_url + "scope";
}