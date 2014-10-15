package br.com.unb.exception;

public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserException(String reason) {
		super(reason);
	}

}
