/**
 * Dec 20, 2007
 */
package tifauv.jplop.auth;

/**
 * Exception thrown when an incorrect password is used.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class PasswordException extends Exception {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = 1335912380916009108L;


	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 * 
	 * @param p_message
	 *            the error message
	 */
	public PasswordException(String p_message) {
		super(p_message);
	}
	
	
	/**
	 * Chain constructor.
	 * 
	 * @param p_message
	 *            the error message
	 * @param p_cause
	 *            the root error
	 */
	public PasswordException(String p_message, Throwable p_cause) {
		super(p_message, p_cause);
	}
}
