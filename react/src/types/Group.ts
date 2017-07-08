import { Scope } from "./Scope"
import { Organisation } from "./Organisation"
import { Member } from "./Member"


export class Group {
    id: number;
    name: string;
    description: string;
    code: string;
    scope: Scope;
    status: string;
    organisation: Organisation;
    memberships: Array<Member>;
    setName(name: string): void {
        this.name = name;
    }
    constructor() {
        this.setName = this.setName.bind(this);
    }
}