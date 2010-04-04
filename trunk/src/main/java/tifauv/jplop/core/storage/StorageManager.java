package tifauv.jplop.core.storage;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public interface StorageManager {

	// SETTERS \\
	/**
	 * Sets the auto-save interval in seconds.
	 */
	void setAutoSaveInterval(long m_seconds);
	

	// METHODS \\
	/**
	 * Attaches a storage delegate.
	 */
	void attach(StorageDelegate<?> p_delegate);
	
	
	/**
	 * Detaches a storage delegate.
	 */
	void detach(StorageDelegate<?> p_delegate);

	
	/**
	 * Runs any initializations needed.
	 */
	void init();
	
	
	/**
	 * Cleaning method called before the application stops.
	 * Last time to asks all the delegates to save their targets.
	 */
	void clean();
}
