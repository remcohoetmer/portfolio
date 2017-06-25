export class Remco {
    status: string = "";
}
export class Organisation {
    status: string = "";
    constructor(public id: string = "", public name: string = "") {
    }
}
export class Scope {
    status: string = "";
    constructor(public id: string = "", public name: string = "") {
    }
}
export class Member {
    constructor(public role: string, public person: Person) {
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
export class Group {
    id: number;
    name: string;
    description: string;
    scope: Scope;
    status: string;
    organisation: Organisation;
    members: Array<Member>;
}