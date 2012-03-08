/**
 * 18 jan. 2008
 */
package tifauv.jplop.core.board;


/**
 * Exception thrown when a {@link Post} cannot be built.
 * 
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class BadPostException extends Exception {
	
	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = 1L;


	// CONSTRUCTORS \\
	/**
	 * Simple constructor.
	 * 
	 * @param p_message
	 *            the error message
	 */
	public BadPostException(String p_message) {
		super(p_message);
	}
	
	
	/**
	 * Chaining constructor with a cause.
	 * 
	 * @param p_message
	 *            the error message
	 * @param p_cause
	 *            the cause of the error
	 */
	public BadPostException(String p_message, Throwable p_cause) {
		super(p_message, p_cause);
	}
}
