package exceptions;

public class EndOfSessionException extends Exception {

	private static final long serialVersionUID = 1L;

	public EndOfSessionException(String s){
		super(s);
	}
}
