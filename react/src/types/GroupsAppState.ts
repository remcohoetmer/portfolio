import Group from './Group';

class GroupsAppState {
  attributes: string[];
  constructor(public groups: Group[]) {
    this.attributes = ["name", "code", "description", "status"];
  }
}

export default GroupsAppState;
