import React from 'react';

export class Organisation {
	status: string = "";
	constructor(public id: string = "", public name: string = "") {
	}
}

export interface OrganisationListProps {
	organisations: Organisation[];
	selectedID: string;
	selectOrganisation(id: string): void;
}

export class OrganisationSelectionComp extends React.Component<OrganisationListProps, {}> {

	render() {
		var organisations: JSX.Element[] = [];
		this.props.organisations.forEach(organisation => {
			organisations.push(<option key={organisation.id} value={organisation.id}>{organisation.name}</option>);
		});
		return (
			<select value={this.props.selectedID} onChange={(e)=>this.props.selectOrganisation((e.target as any).value)}>
				<option value="">(organisation)</option>
				{organisations}
			</select>

		)
	}
}
