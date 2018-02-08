'use strict';
import { Scope, ScopeSelectionComp } from './types/Scope';
import { Member, MemberListComp } from './types/Member';
import { Group } from './types/Group';
import { Organisation, OrganisationSelectionComp } from './types/Organisation';
import { StatusSelectionComp } from './types/Status';
import { restclient } from "./communication/restclient";
import React from 'react';
import Rx from 'rx-dom';

declare function escape(s: string): string;

const group_url = 'http://localhost/api/group';
const org_url = 'http://localhost/api/organisation';
const scope_url = 'http://localhost:81/api/scope';

export class GroupSelection {
	name: string;
	description: string;
	status: string;
	code: string;
	scopeID: string;
	organisationID: string;
	constructor() {
		this.name = "";
		this.description = "";
		this.status = "";
		this.code = "";
		this.scopeID = "";
		this.organisationID = "";
	}
}
class GroupsAppState {
	public organisations: Organisation[];
	public scopes: Scope[];
	public groupSelection: GroupSelection;
	public selectedGroup: Group;
	constructor(public groups: Group[] = []) {

		this.emptyGroups = this.emptyGroups.bind(this);
		this.organisations = [];
		this.scopes = [];
		this.groupSelection = new GroupSelection();
		this.selectedGroup = null;
	}

	emptyGroups(): GroupsAppState {
		this.groups = [];
		this.selectedGroup = null;
		return this;
	}
}

interface GroupSelector {
	createGroup(newGroup: Group): void;
	selectGroup(Group: Group): void;
	deleteGroup(group: Group): void;
	selectOrganisation(id: string): void;
	selectScope(id: string): void;
	setStatus(status: string): void;
	setName(name: string): void;
	setCode(code: string): void;
	setDescription(description: string): void;
	addMember(member: Member): void;
	removeMember(member: Member): void;

}
export class GroupsApp extends React.Component<{}, GroupsAppState> implements GroupSelector {
	constructor(props: any) {
		super(props);
		this.state = new GroupsAppState();

		this.loadGroups = this.loadGroups.bind(this);
		this.loadGroupDetails = this.loadGroupDetails.bind(this);
		this.setState = this.setState.bind(this);
		this.addGroup = this.addGroup.bind(this);
		this.addOrganisation = this.addOrganisation.bind(this);
		this.addScope = this.addScope.bind(this);

		this.createGroup = this.createGroup.bind(this);
		this.deleteGroup = this.deleteGroup.bind(this);
		this.selectGroup = this.selectGroup.bind(this);

		this.initialiseGroups = this.initialiseGroups.bind(this);
		this.selectOrganisation = this.selectOrganisation.bind(this);
		this.selectScope = this.selectScope.bind(this);
		this.setStatus = this.setStatus.bind(this);
		this.setName = this.setName.bind(this);
		this.setCode = this.setCode.bind(this);
		this.setDescription = this.setDescription.bind(this);
		this.removeMember = this.removeMember.bind(this);
		this.addMember = this.addMember.bind(this);
	}

	selectOrganisation(id: string): void {
		this.setState(prevState => { prevState.groupSelection.organisationID = id; return prevState; });
	}
	selectScope(id: string): void {
		this.setState(prevState => { prevState.groupSelection.scopeID = id; return prevState; });
	}
	setStatus(status: string): void {
		this.setState(prevState => { prevState.groupSelection.status = status; return prevState; });
	}
	setName(name: string): void {
		this.setState(prevState => { prevState.groupSelection.name = name; return prevState; });
	}
	setCode(code: string): void {
		this.setState(prevState => { prevState.groupSelection.code = code; return prevState; });
	}
	setDescription(description: string): void {
		this.setState(prevState => { prevState.groupSelection.description = description; return prevState; });
	}
	addMember(member: Member): void {
		let self = this;
		restclient.postRequest(group_url + "/" + this.state.selectedGroup.id + "/membership", JSON.stringify(member))
			.then(() => { self.loadGroups(); });
	}
	removeMember(member: Member): void {
		let self = this;
		restclient.deleteRequest(group_url + "/" + this.state.selectedGroup.id + "/membership/" + member.person.id)
			.then(() => { self.loadGroups(); });
	}

	createGroup(newGroup: Group): void {
		restclient.postRequest(group_url, JSON.stringify(newGroup))
			.then(() => { this.loadGroups(); });
		//(json: string) => self.addGroup(JSON.parse(json) as Group);
	}
	selectGroup(group: Group): void {
		this.setState((prevState) => { prevState.selectedGroup = group; return prevState });
		this.loadGroupDetails(group);
	}

	deleteGroup(group: Group) {
		restclient.deleteRequest(group_url + '/' + group.id)
			.then(() => { this.loadGroups(); });
	}

	append(url: string, name: string, value: string): string {
		var att = name + "=" + escape(value);

		if (url.endsWith("?")) {
			return url + att;
		}
		return url + "&" + att;

	}
	initialiseGroups() {
		restclient.getRequest(group_url + '/initialise')
			.then(() => { this.loadGroups(); });
	}
	loadGroups() {
		this.setState(this.state.emptyGroups());

		var url = group_url + "?selectOrganisations&selectScopes";
		if (this.state.groupSelection.name != "") {
			url = this.append(url, "name", this.state.groupSelection.name);
		}
		if (this.state.groupSelection.code != "") {
			url = this.append(url, "code", this.state.groupSelection.code);
		}
		if (this.state.groupSelection.description != "") {
			url = this.append(url, "description", this.state.groupSelection.description);
		}
		if (this.state.groupSelection.status != "") {
			url = this.append(url, "status", this.state.groupSelection.status);
		}
		if (this.state.groupSelection.scopeID != "") {
			url = this.append(url, "scopeId", this.state.groupSelection.scopeID);
		}
		if (this.state.groupSelection.organisationID != "") {
			url = this.append(url, "organisationId", this.state.groupSelection.organisationID);
		}

		Rx.DOM.fromEventSource(url, Rx.Observer.create(console.log, console.log))
			.map((json: string) => JSON.parse(json) as Group)
			.subscribe(this.addGroup);
	}

	loadGroupDetails(group: Group) {
		restclient.getRequest(group_url + '/' + group.id)
			.then((json: string) => JSON.parse(json) as Group)
			.then((groupWithDetails) => {
				this.setState((prevState) => { prevState.selectedGroup = groupWithDetails; return prevState; })
			});
	}

	addGroup(group: Group) {
		this.setState(prevState => {
			prevState.groups.push(group);
			return prevState;
		});
	}

	addOrganisation(organisation: Organisation) {
		this.setState(prevState => {
			prevState.organisations.push(organisation);
			return prevState;
		});
	}
	addScope(scope: Scope) {
		this.setState(prevState => {
			prevState.scopes.push(scope);
			return prevState;
		});
	}

	componentDidMount() {
		this.loadGroups();
		Rx.DOM.fromEventSource(org_url)
			.map((json: string) => JSON.parse(json) as Organisation)
			.subscribe(this.addOrganisation);
		Rx.DOM.fromEventSource(scope_url)
			.map((json: string) => JSON.parse(json) as Scope)
			.subscribe(this.addScope);
	}

	render() {
		return (
			<div id="container">
				<h1>Group Admin</h1>
				<button key="init" onClick={this.initialiseGroups}>Initialise</button>
				<div>

					<CreateDialog
						scopes={this.state.scopes}
						organisations={this.state.organisations}
						groupSelector={this} />
					<div>
						<input type="text" placeholder="name" value={this.state.groupSelection.name} onChange={(e) => { this.setName((e.target as any).value); }} />
						<input type="text" placeholder="description" value={this.state.groupSelection.description} onChange={(e) => { this.setDescription((e.target as any).value); }} />
						<input type="text" placeholder="code" value={this.state.groupSelection.code} onChange={(e) => { this.setCode((e.target as any).value); }} />

						<OrganisationSelectionComp organisations={this.state.organisations}
							selectedID={this.state.groupSelection.organisationID}
							selectOrganisation={this.selectOrganisation} />
						<ScopeSelectionComp scopes={this.state.scopes}
							selectedID={this.state.groupSelection.scopeID}
							selectScope={this.selectScope} />

						<StatusSelectionComp
							selectedStatus={this.state.groupSelection.status}
							selectStatus={this.setStatus} />
					</div>

					<button key="search" onClick={this.loadGroups}>Search</button>
					<button><a href="#createGroup">Create</a></button>
					<h3>Group List</h3>
					<GroupListComp groups={this.state.groups} selectedGroup={this.state.selectedGroup} deleteGroup={this.deleteGroup} selectGroup={this.selectGroup} />

					{(this.state.selectedGroup != undefined) &&
						<div>
							<h3>Group Members</h3>
							<MemberListComp memberships={this.state.selectedGroup.memberships} deleteMember={this.removeMember} />
						</div>

					}

				</div>

			</div>

		)
	}

}
// end::app[]
// tag::create-dialog[]
interface DialogProps {
	scopes: Scope[];
	organisations: Organisation[];
	groupSelector: GroupSelector;
}
interface DialogState {
	group: Group;
}
class DialogStateImpl implements DialogState {
	public group: Group;
	public constructor() {
		this.group = new Group();
		this.group.name = "";
		this.group.code = "";
		this.group.description = "";
		this.group.status = "";
		this.group.organisation = new Organisation();
		this.group.organisation.id = "";
		this.group.scope = new Scope();
		this.group.scope.id = "";
	}
}

class CreateDialog extends React.Component<DialogProps, DialogState> {

	constructor(props: DialogProps) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.state = new DialogStateImpl();
	}
	handleSubmit(e: any) {
		e.preventDefault();
		this.props.groupSelector.createGroup(this.state.group);
		// Navigate away from the dialog to hide it.
		(window as any).location = "#";
	}
	//TODO: this.setState({ group: { name: (e.target as any).value } } as DialogState);

	render() {
		return (

			<div id="createGroup" className="modalDialog">
				<div>
					<a href="#" title="Close" className="close">X</a>

					<h2>Create Group</h2>

					<form>
						<table>
							<tbody>
								<tr>
									<td>Name</td>
									<td><input type="text" value={this.state.group.name}
										onChange={(e) => {
											const value = (e.target as any).value;
											this.setState((prevState) => { prevState.group.name = value; return prevState; });
										}} className="field" /></td>
								</tr>
								<tr>
									<td>Description</td>
									<td><input type="text" value={this.state.group.description}
										onChange={(e) => {
											const value = (e.target as any).value;
											this.setState((prevState) => { prevState.group.description = value; return prevState; })
										}} className="field" /></td>
								</tr>
								<tr>
									<td>Code</td>
									<td><input type="text" value={this.state.group.code}
										onChange={(e) => {
											const value = (e.target as any).value;
											this.setState((prevState) => { prevState.group.code = value; return prevState; })
										}} className="field" /></td>
								</tr>
								<tr>
									<td>Status</td>
									<td><StatusSelectionComp
										selectedStatus={this.state.group.status}
										selectStatus={(status) => { this.setState((prevState) => { prevState.group.status = status; return prevState; }) }} /></td>
								</tr>
								<tr>
									<td>Organisation</td>
									<td><OrganisationSelectionComp organisations={this.props.organisations}
										selectedID={this.state.group.organisation.id}
										selectOrganisation={(organisationID: string) => { this.setState((prevState) => { prevState.group.organisation.id = organisationID; return prevState; }) }} /></td>
								</tr>
								<tr>
									<td>Scope</td>
									<td><ScopeSelectionComp scopes={this.props.scopes}
										selectedID={this.state.group.scope.id}
										selectScope={(scopeID) => { this.setState((prevState) => { prevState.group.scope.id = scopeID; return prevState; }) }} /></td>
								</tr>
							</tbody>
						</table>

						<button onClick={this.handleSubmit}>Create</button>
					</form>
				</div>
			</div>

		)
	}

}
// end::create-dialog[]


// tag::Group-list[]

interface GroupListProps {
	groups: Group[];
	selectedGroup: Group;
	deleteGroup(group: Group): void;
	selectGroup(group: Group): void;
}
class GroupListComp extends React.Component<GroupListProps, {}> {
	render() {
		var groups = this.props.groups.map(group =>
			<GroupComp key={group.id} isSelected={(this.props.selectedGroup != undefined) && (group.id == this.props.selectedGroup.id)}
				group={group} deleteGroup={this.props.deleteGroup} selectGroup={this.props.selectGroup} />
		);
		return (
			<table className="groups">
				<tbody>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th>Organisation</th>
						<th>Scope</th>
						<th>Status</th>
						<th></th>
					</tr>
					{groups}
				</tbody>
			</table>
		)
	}
}

interface GroupCompProps {
	group: Group;
	isSelected: boolean;
	deleteGroup(group: Group): void;
	selectGroup(group: Group): void;
}

class GroupComp extends React.Component<GroupCompProps, {}>{
	render() {
		var org = "";
		if (this.props.group.organisation != undefined) {
			org = this.props.group.organisation.name;
		}
		var scope = "";
		if (this.props.group.scope != undefined) {
			scope = this.props.group.scope.name;
		}
		var className = this.props.isSelected ? "selected" : "normal";
		return (
			<tr className={className} onClick={() => { this.props.selectGroup(this.props.group); }}>
				<td>{this.props.group.name}</td>
				<td>{this.props.group.description}</td>
				<td>{org}</td>
				<td>{scope}</td>
				<td>{this.props.group.status}</td>
				<td ><a href="#" onClick={() => { this.props.deleteGroup(this.props.group); }}>delete</a></td>
			</tr>
		)
	}
}
export default GroupsApp;
