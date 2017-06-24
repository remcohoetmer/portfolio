
export class Organisation {
    constructor(public id: string, public name: string) {
    }
}

export class Person {
    id: number;
    name: string;
    surname: string;
    dateofbirth: string;
    email: string;
    status: string;
    role: string;
    organisation: Organisation;
}