
export class Configuration {
    private static service_url = "http://localhost:8082/api/";
    static group_service = Configuration.service_url + "group";
    static organisation_service = Configuration.service_url + "organisation";
    static person_service = Configuration.service_url + "person";
    static scope_service = Configuration.service_url + "scope";
    static check_config ():void
    {
        if (!(<any>window).EventSource) {
            alert( "Browser does not support EventSource. Applications fails.")
        }
    }
}