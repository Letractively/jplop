package tifauv.jplop.core.storage;

/**
 * This is the interface to the storage layer.
 * <p>This is how you should use it at initialization :
 * <pre>
 * StorageManager storage = new ...();
 * ... load the configuration ...
 * storage.init();
 * storage.attach(persistent_object); // Add as many times as you have objects to persist
 * storage.ready();
 * </pre></p>
 * <p>Don't forget to call <code>storage.clean()</code> when you want to quit.</p>
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
	 * 
	 * @throws StorageException
	 *            if the factory could not be created
	 */
	void init()
	throws StorageException;
	
	
	/**
	 * Loads all the attached delegates and starts the backup thread.
	 */
	void ready();
	
	
	/**
	 * Cleaning method called before the application stops.
	 * Last time to asks all the delegates to save their targets.
	 */
	void clean();
}
