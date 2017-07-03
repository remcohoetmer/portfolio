import Employee from './Employee';

class GroupsState {
  attributes: string[];
  constructor(public employees: Employee[]) {
    this.attributes = ["firstName", "lastName", "description"];
  }
}

export default GroupsState;
