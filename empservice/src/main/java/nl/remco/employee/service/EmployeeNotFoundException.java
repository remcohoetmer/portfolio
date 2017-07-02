package nl.remco.employee.service;

public class EmployeeNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmployeeNotFoundException(String id) {
        super(String.format("No employee entry found with id: <%s>", id));
    }
}
