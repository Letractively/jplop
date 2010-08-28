/**
 * 27 mars 2010
 */
package tifauv.jplop.core.storage;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class StorageException extends Exception {

	// FIELDS \\
	/** The serialization id. */
	private static final long serialVersionUID = 1L;

	
	// CONSTRUCTORS \\
	/**
	 * 
	 */
	public StorageException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param p_message
	 */
	public StorageException(String p_message) {
		super(p_message);
	}

	
	/**
	 * @param p_cause
	 */
	public StorageException(Throwable p_cause) {
		super(p_cause);
	}

	
	/**
	 * @param p_message
	 * @param p_cause
	 */
	public StorageException(String p_message, Throwable p_cause) {
		super(p_message, p_cause);
	}
}
