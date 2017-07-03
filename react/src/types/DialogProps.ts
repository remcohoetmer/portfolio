import Group from './Group';
interface DialogProps {
  attributes: string[];
  onCreate(group: Group): void;
}

export default DialogProps;
