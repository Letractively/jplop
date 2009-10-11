package tifauv.jplop.core.backend.file.persistence;


/**
 * This exception wraps any non-runtime exception thrown when
 * deserializing a subclass of {@link tifauv.jplop.persistence.Persistent}.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class DeserializeException extends Exception {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = 1L;


	// CONSTRUCTORS \\
	/**
	 * Standalone constructor.
	 * 
	 * @param p_message
	 *            the error message
	 */
	public DeserializeException(String p_message) {
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
	public DeserializeException(String p_message, Throwable p_cause) {
		super(p_message, p_cause);
	}
}
