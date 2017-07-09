import { Person } from "./Person"
import React from 'react';

export class Member {
    constructor(public role: string, public person: Person) {
    }
}

interface MemberListProps {
    memberships: Member[];
    deleteMember(member: Member): void;
}

export class MemberListComp extends React.Component<MemberListProps, {}> {
    render() {
        var members = null;
    
        if (this.props.memberships != undefined) {
            members = this.props.memberships.map(member =>
            <MemberComp key={member.person.id} member={member} deleteMember={this.props.deleteMember} />
        );
        }
        return (
            <table>
                <tbody>
                    <tr>
                        <th>Role</th>
                        <th>Name</th>
                        <th>Surname</th>
                        <th>Organisation</th>
                        <th></th>
                    </tr>
                    {members}
                </tbody>
            </table>
        )
    }
}

interface MemberCompProps {
    member: Member;
    deleteMember(member: Member): void;
}

class MemberComp extends React.Component<MemberCompProps, {}>{
    render() {
        var org = "";
        if (this.props.member.person.organisation != undefined) {
            org = this.props.member.person.organisation.name;
        }
        return (
            <tr>
                <td>{this.props.member.role}</td>
                <td>{this.props.member.person.name}</td>
                <td>{this.props.member.person.surname}</td>
                <td>{org}</td>
                <td ><a href="#" onClick={() => { this.props.deleteMember(this.props.member); }}>delete</a></td>
            </tr>
        )
    }
}
export default MemberListComp;

