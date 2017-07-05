import React from 'react';

export class Scope {
	status: string = "";
	constructor(public id: string = "", public name: string = "") {
	}
}
interface ScopeListProps {
	scopes: Scope[];
	selectedID: string;
	selectScope(id: string): void;
}

export class ScopeSelectionComp extends React.Component<ScopeListProps, {}> {
	render() {
		var scopes = this.props.scopes.map(scope =>
			<option key={scope.id} value={scope.id}>{scope.name}</option>
		);
		return (
			<select value={this.props.selectedID} onChange={(e) => this.props.selectScope((e as any).target.value)}>
				<option value="">(scope)</option>
				{scopes}
			</select>

		)
	}
}
