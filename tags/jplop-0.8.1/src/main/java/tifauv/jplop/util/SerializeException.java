/**
 * Dec 11, 2007
 */
package tifauv.jplop.util;

/**
 * This exception wraps any non-runtime exception thrown when
 * serializing a subclass of {@link tifauv.jplop.util.Serializable}.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class SerializeException extends Exception {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = -4408088046693855585L;


	// CONSTRUCTORS \\
	/**
	 * Standalone constructor.
	 * 
	 * @param p_message
	 *            the error message
	 */
	public SerializeException(String p_message) {
		super(p_message);
	}
	
	
	/**
	 * Wrapping constructor.
	 * 
	 * @param p_message
	 *            the error message
	 * @param p_cause
	 *            the error cause
	 */
	public SerializeException(String p_message, Throwable p_cause) {
		super(p_message, p_cause);
	}
}
