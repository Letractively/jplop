/**
 * 6 juil. 2008
 */
package tifauv.jplop.exceptions;

/**
 * This is...
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class ValidationException extends RuntimeException {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = 174437317242143919L;

	
	// CONSTRUCTORS \\
	/**
	 * Simple constructor with a message.
	 */
	public ValidationException(String p_message) {
		super(p_message);
	}
}
