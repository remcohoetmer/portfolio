import React from 'react';

interface StatusListProps {
	selectedStatus: string;
	selectStatus(status: string): void;
}

export class StatusSelectionComp extends React.Component<StatusListProps, {}> {
	render() {
		var statuses = [
			<option key="0" value="">(none)</option>,
			<option key="1" value="Active">Active</option>,
			<option key="2" value="Inactive">Inactive</option>
		];
		return (
			<select value={this.props.selectedStatus} onChange={(e) => this.props.selectStatus((e as any).target.value)}>
				{statuses}
			</select>

		)
	}
}
