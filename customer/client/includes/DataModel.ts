
export class Organisatie {
    constructor(public id: number, naam: string) {
    }
}


export class Inschrijving {
    constructor(public id: number, public organisatie: Organisatie) {
    }
}

export class Klant {
    id: number;
    voornaam: string;
    achternaam: string;
    geslacht: string;
    emailAdres: string;
    status: string;
    product: string;
    geplandePeriode: string;
    rol: string;
    inschrijvingen: Array<Inschrijving>;
}