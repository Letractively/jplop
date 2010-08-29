package tifauv.jplop.core.storage;

/**
 * Main storage abstraction.
 *
 * @param <T> an object to load and store
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public interface StorageDelegate<T> {
	
	// GETTERS \\
	/**
	 * Gives the object to load and save.
	 * 
	 * @see #load()
	 * @see #save()
	 */
	T getObject();
	
	
	// METHODS \\
	/**
	 * Loads the object returned by {@link #getObject()}.
	 * 
	 * @see #save()
	 */
	void load()
	throws StorageException;
	
	
	/**
	 * Saves the object returned by {@link #getObject()}.
	 * 
	 * @see #load()
	 */
	void save()
	throws StorageException;
}
