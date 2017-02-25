import {KlantenLijst} from "KlantenLijst";


$(document).ready(function () {
    $('#closeGebruikersBeheer').click(function () {
        window.location.href = "index.html";
    });
    let klantenlijst = new KlantenLijst();
    klantenlijst.initialise();
    klantenlijst.searchKlanten();
});