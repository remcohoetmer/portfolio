import { Scope } from "./Scope"
import { Person } from "./Person"
import { Organisation } from "./Organisation"

export class Member {
    constructor(public id: string, public role: string, public person: Person) {
    }
}
export class Group {
    id: number;
    name: string;
    description: string;
    code: string;
    scope: Scope;
    status: string;
    organisation: Organisation;
    members: Array<Member>;
    setName(name: string): void {
        this.name = name;
    }
    constructor() {
        this.setName = this.setName.bind(this);
    }
}