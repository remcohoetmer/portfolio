import { PersonList } from "PersonList";


$(document).ready(function () {
    $('#closePersonAdmin').click(function () {
        window.location.href = "index.html";
    });
    let personlist = new PersonList();
    personlist.initialise();
    let fun = personlist.searchPersons.bind(personlist);
    $('#name').change(fun);
    $('#surname').change(fun);
    $('#email').change(fun);
    $('#status').change(fun);
    $('#searchPersonOrganisation').change(fun);

    fun.apply();
});