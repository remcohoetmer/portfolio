'use strict';
import Employee from './types/Employee';
import DialogProps from './types/DialogProps';
import EmployeeState from './types/EmployeeState';
import GroupsState from './types/GroupsState';
import EmployeeListProps from './types/EmployeeListProps';

// tag::vars[]
import React from 'react';
import ReactDOM from 'react-dom';
import Rx from 'rx-dom';


// end::vars[]
const url = 'http://localhost:8082/api/employee';
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
export class GroupsApp extends React.Component<{}, GroupsState> {
	constructor(props: any) {
		super(props);
		this.state = new GroupsState([]);
		this.onCreate = this.onCreate.bind(this);
		this.loadEmployees = this.loadEmployees.bind(this);
		this.setState = this.setState.bind(this);
	}


	loadEmployees() {
		Rx.DOM.fromEventSource(url)
			.map((json: string) => JSON.parse(json) as Employee)
			.subscribe((emp: Employee) => {
				return this.setState((prevState: GroupsState) => {
					prevState.employees.push(emp);
					return prevState;
				})
			});

	}

	loadEmployees2() {
		console.log(this);
		this.setState(new GroupsState([]), null);
	}

	componentDidMount() {
		this.loadEmployees();
	}

	render() {
		return (
			<div>
				<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate} />
				<EmployeeListComp employees={this.state.employees} />
			</div>
		)
	}
	// tag::create[]
	onCreate(newEmployee: Employee) {
		let self = this;
		new Util().postRequest(url, JSON.stringify(newEmployee))
			.then(() => { self.loadEmployees(); });
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
		var newEmployee: any = {};
		this.props.attributes.forEach(attribute => {
			newEmployee[attribute] = (ReactDOM.findDOMNode(this.refs[attribute]) as HTMLInputElement).value.trim();
		});

		this.props.onCreate(newEmployee);

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
				<a href="#createEmployee">Create</a>

				<div id="createEmployee" className="modalDialog">
					<div>
						<a href="#" title="Close" className="close">X</a>

						<h2>Create new employee</h2>

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
// tag::employee-list[]
class EmployeeListComp extends React.Component<EmployeeListProps, {}> {
	render() {
		var employees = this.props.employees.map(employee =>
			<EmployeeComp key={employee.id} employee={employee} />
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Description</th>
					</tr>
					{employees}
				</tbody>
			</table>
		)
	}
}
// end::employee-list[]

// tag::employee[]
class EmployeeComp extends React.Component<EmployeeState, {}>{
	render() {
		return (
			<tr>
				<td>{this.props.employee.firstName}</td>
				<td>{this.props.employee.lastName}</td>
				<td>{this.props.employee.description}</td>
			</tr>
		)
	}
}
// end::employee[]

// tag::render[]
/*
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
*/
// end::render[]
export default GroupsApp;