package nl.remco.service.common.web;

public class BadRequestException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public BadRequestException( String message){
		super(message);

	}

}
