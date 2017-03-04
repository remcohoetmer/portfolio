package nl.remco.group.service;

public class GroupNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GroupNotFoundException(String id) {
        super(String.format("No scope entry found with id: <%s>", id));
    }
}
