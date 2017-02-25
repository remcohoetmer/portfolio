import {KlantenLijst} from "KlantenLijst";


$(document).ready(function () {
    $('#closeGebruikersBeheer').click(function () {
        window.location.href = "index.html";
    });
    let klantenlijst = new KlantenLijst();
    klantenlijst.initialise();
    let fun = klantenlijst.searchKlanten.bind(klantenlijst);
    $('#voornaam').change(fun);
    $('#achternaam').change(fun);
    $('#emailAdres').change(fun);
    $('#status').change(fun);
    $('#searchKlantOrganisatie').change(fun);

    fun.apply();
});