package nl.remco.scope.service;

public class ScopeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ScopeNotFoundException(String id) {
        super(String.format("No scope entry found with id: <%s>", id));
    }
}
