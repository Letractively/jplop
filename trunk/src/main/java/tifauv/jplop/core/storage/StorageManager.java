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
	
	// CONSTANTS \\
	/** The default auto-save interval is every 5 minutes. */
	public static final int DEFAULT_AUTOSAVE_INTERVAL = 5 * 60;

	
	// SETTERS \\
	/**
	 * Sets the auto-save interval in seconds.
	 */
	void setAutoSaveInterval(long m_seconds);
	
	
	/**
	 * Sets the factory used to build the delegates.
	 */
	void setStorageFactory(StorageFactory p_factory);
	

	// METHODS \\
	/**
	 * Attaches a storage delegate.
	 * 
	 * @throws IllegalArgumentException
	 *            if the given object is null
	 * @throws StorageException
	 *            if the factory is null or couldn't return a delegate
	 */
	void attach(Object p_object)
	throws StorageException;

	
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
