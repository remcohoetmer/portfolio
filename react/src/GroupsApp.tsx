'use strict';
import { Scope, ScopeSelectionComp } from './types/Scope';
import { Group } from './types/Group';
import { Organisation, OrganisationSelectionComp } from './types/Organisation';

import { restclient } from "./communication/restclient";

// tag::vars[]
import React from 'react';
import ReactDOM from 'react-dom';
import Rx from 'rx-dom';

// end::vars[]
const url = 'http://localhost:8082/api/group';
const org_url = 'http://localhost:8082/api/organisation';
const scope_url = 'http://localhost:8082/api/scope';

// tag::app[]
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
	attributes: string[];
	public organisations: Organisation[];
	public scopes: Scope[];
	groupSelection: GroupSelection;
	constructor(public groups: Group[] = []) {

		this.attributes = ["name", "code", "description", "status"];
		this.emptyGroups = this.emptyGroups.bind(this);
		this.organisations = [];
		this.scopes = [];
		this.groupSelection = new GroupSelection();
	}

	emptyGroups(): GroupsAppState {
		this.groups = [];
		return this;
	}
}
interface GroupSelector {
	selectOrganisation(id: string): void;
	selectScope(id: string): void;
	selectStatus(status: string): void;
}

export class GroupsApp extends React.Component<{}, GroupsAppState> implements GroupSelector {
	constructor(props: any) {
		super(props);
		this.state = new GroupsAppState();

		this.onCreate = this.onCreate.bind(this);
		this.loadGroups = this.loadGroups.bind(this);
		this.setState = this.setState.bind(this);
		this.addGroup = this.addGroup.bind(this);
		this.addOrganisation = this.addOrganisation.bind(this);
		this.selectOrganisation = this.selectOrganisation.bind(this);
		this.selectScope = this.selectScope.bind(this);
		this.selectStatus = this.selectStatus.bind(this);
		this.addScope = this.addScope.bind(this);
	}

	selectOrganisation(id: string) {
		this.setState(prevState => { prevState.groupSelection.organisationID = id; return prevState; });
	}
	selectScope(id: string) {
		this.setState(prevState => { prevState.groupSelection.scopeID = id; return prevState; });
	}
	selectStatus(status: string) {
		this.setState(prevState => { prevState.groupSelection.status = status; return prevState; });
	}

	loadGroups() {
		this.setState(this.state.emptyGroups());

		Rx.DOM.fromEventSource(url)
			.map((json: string) => JSON.parse(json) as Group)
			.subscribe(this.addGroup);
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
				<div>
					<div>
						<input type="text" placeholder="name" id="searchName" />
						<input type="text" placeholder="description" id="searchDescription" />
						<input type="text" placeholder="code" id="searchCode" />

						<OrganisationSelectionComp organisations={this.state.organisations}
							selectedID={this.state.groupSelection.organisationID}
							selectOrganisation={this.selectOrganisation} />
						<ScopeSelectionComp scopes={this.state.scopes}
							selectedID={this.state.groupSelection.scopeID}
							selectScope={this.selectScope} />

						<input type="text" placeholder="feature" id="searchFeature" />

						<select id="searchStatus">
							<option value="">(status)</option>
							<option value="Active">Active</option>
							<option value="Inactive">Inactive</option>
						</select>
					</div>
					<div>
						<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}
							scopes={this.state.scopes}
							organisations={this.state.organisations}
							groupSelection={this.state.groupSelection}
							groupSelector={this} />
						<GroupListComp groups={this.state.groups} />
					</div>
				</div>
			</div>

		)
	}
	// tag::create[]
	onCreate(newGroup: Group) {
		let self = this;
		restclient.postRequest(url, JSON.stringify(newGroup))
			.then(() => { self.loadGroups(); });
		//(json: string) => self.addGroup(JSON.parse(json) as Group);
	}
	// end::create[]
}
// end::app[]
// tag::create-dialog[]
interface DialogProps {
	attributes: string[];
	groupSelection: GroupSelection;
	scopes: Scope[];
	organisations: Organisation[];
	groupSelector: GroupSelector;
	onCreate(group: Group): void;
}
class CreateDialog extends React.Component<DialogProps, {}> {

	constructor(props: DialogProps) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(e: any) {
		e.preventDefault();
		var newGroup: Group = new Group();
		this.props.attributes.forEach(attribute => {
			(newGroup as any)[attribute] = (ReactDOM.findDOMNode(this.refs[attribute]) as HTMLInputElement).value.trim();
		});
		newGroup.organisation = new Organisation(this.props.groupSelection.organisationID);
		newGroup.scope = new Scope(this.props.groupSelection.scopeID);
		this.props.onCreate(newGroup);

		// clear out the dialog's inputs
		this.props.attributes.forEach(attribute => {
			(ReactDOM.findDOMNode(this.refs[attribute]) as HTMLInputElement).value = '';
		});

		// Navigate away from the dialog to hide it.

		(window as any).location = "#";
	}

	render() {
		var inputs = this.props.attributes.map(attribute =>
			<p key={attribute}>
				<input type="text" placeholder={attribute} ref={attribute} className="field" />
			</p>
		);

		return (
			<div>
				<a href="#createGroup">Create</a>

				<div id="createGroup" className="modalDialog">
					<div>
						<a href="#" title="Close" className="close">X</a>

						<h2>Create new Group</h2>

						<form>
							{inputs}
							<ScopeSelectionComp scopes={this.props.scopes}
								selectedID={this.props.groupSelection.scopeID}
								selectScope={this.props.groupSelector.selectScope} />
							<OrganisationSelectionComp organisations={this.props.organisations}
								selectedID={this.props.groupSelection.organisationID}
								selectOrganisation={this.props.groupSelector.selectOrganisation} />
							<button onClick={this.handleSubmit}>Create</button>
						</form>
					</div>
				</div>
			</div>
		)
	}

}
// end::create-dialog[]


// tag::Group-list[]

interface GroupListProps {
	groups: Group[];
}
class GroupListComp extends React.Component<GroupListProps, {}> {
	render() {
		var groups = this.props.groups.map(group =>
			<GroupComp key={group.id} group={group} />
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th>Organisation</th>
						<th>Scope</th>
						<th>Status</th>
					</tr>
					{groups}
				</tbody>
			</table>
		)
	}
}

interface GroupCompProps {
	group: Group;
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
		return (
			<tr>
				<td>{this.props.group.name}</td>
				<td>{this.props.group.description}</td>
				<td>{org}</td>
				<td>{scope}</td>
				<td>{this.props.group.status}</td>
			</tr>
		)
	}
}
export default GroupsApp;
