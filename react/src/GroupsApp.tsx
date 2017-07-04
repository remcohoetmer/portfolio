'use strict';
import Group from './types/Group';
import DialogProps from './types/DialogProps';
import GroupCompProps from './types/GroupCompProps';
import GroupsAppState from './types/GroupsAppState';
import GroupListProps from './types/GroupListProps';

// tag::vars[]
import React from 'react';
import ReactDOM from 'react-dom';
import Rx from 'rx-dom';

// end::vars[]
const url = 'http://localhost:8082/api/group';
class Util {
	postRequest(url: string, body: string): Promise<any> {
		return new Promise<any>(
			function (resolve, reject) {
				const request = new XMLHttpRequest();
				request.onload = function () {
					let self = this as XMLHttpRequest;
					if (self.status === 201) {
						resolve(self.response);
					} else {
						reject(new Error(self.statusText));
					}
				};
				request.onerror = function () {
					let self = this as XMLHttpRequest;
					reject(new Error('XMLHttpRequest Error: ' + self.statusText));
				};
				request.open('POST', url);
				request.setRequestHeader('Content-Type', 'application/json')
				request.send(body);
			});
	}
};
// tag::app[]
export class GroupsApp extends React.Component<{}, GroupsAppState> {
	constructor(props: any) {
		super(props);
		this.state = new GroupsAppState([]);
		this.onCreate = this.onCreate.bind(this);
		this.loadGroups = this.loadGroups.bind(this);
		this.setState = this.setState.bind(this);
		this.addGroup = this.addGroup.bind(this);
	}


	loadGroups() {
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

	componentDidMount() {
		this.loadGroups();
	}

	render() {
		return (
			<div>
				<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate} />
				<GroupListComp groups={this.state.groups} />
			</div>
		)
	}
	// tag::create[]
	onCreate(newGroup: Group) {
		let self = this;
		new Util().postRequest(url, JSON.stringify(newGroup))
			.then((json:string) => { self.addGroup(JSON.parse(json) as Group); });
	}
	// end::create[]
}
// end::app[]
// tag::create-dialog[]
class CreateDialog extends React.Component<DialogProps, {}> {

	constructor(props: DialogProps) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(e: any) {
		e.preventDefault();
		var newGroup: any = {};
		this.props.attributes.forEach(attribute => {
			newGroup[attribute] = (ReactDOM.findDOMNode(this.refs[attribute]) as HTMLInputElement).value.trim();
		});

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
// end::Group-list[]

// tag::Group[]
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
// end::Group[]
export default GroupsApp;
// tag::render[]
/*
ReactDOM.render(
	<GroupsApp />,
	document.getElementById('groupContent')
)
*/
// end::render[]
