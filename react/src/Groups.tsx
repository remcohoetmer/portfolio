'use strict';
import EmployeeState from './types/EmployeeState';
import GroupsState from './types/GroupsState';
import EmployeeListProps from './types/EmployeeListProps';

// tag::vars[]
import React from 'react';
//import ReactDOM from 'react-dom';
import Rx from 'rx-dom';

// end::vars[]

// tag::app[]
export class GroupsApp extends React.Component<{}, GroupsState> {

	constructor(props: any) {
		super(props);
		this.state = { employees: [] };
	}

	componentDidMount() {
		Rx.DOM.fromEventSource<string>('http://localhost:8082/api/employee')
			.map(json => JSON.parse(json))
			.subscribe(emp => {
				return this.setState((prevState: GroupsState) => {
					prevState.employees.push(emp);
					return prevState;
				})
			});
	}

	render() {
		return (
			<EmployeeListComp employees={this.state.employees} />
		)
	}
}
// end::app[]

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