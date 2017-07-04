import Organisation from './Organisation';

export default class Person {
    id: number;
    name: string;
    surname: string;
    dateofbirth: string;
    email: string;
    status: string;
    role: string;
    organisation: Organisation;
}