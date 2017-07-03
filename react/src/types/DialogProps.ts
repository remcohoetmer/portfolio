import Employee from './Employee';
interface DialogProps {
  attributes: string[];
  onCreate(emp: Employee): void;
}

export default DialogProps;
