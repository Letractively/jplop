package tifauv.jplop.exceptions;

/**
 * This is...
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class PasswordException extends RuntimeException {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = -350171452138228499L;


	// CONSTRUCTORS \\
	public PasswordException(String p_message) {
		super(p_message);
	}

	
	public PasswordException(String p_message, Throwable p_cause) {
		super(p_message, p_cause);
	}
}
