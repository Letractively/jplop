/**
 * 4 avr. 2010
 */
package tifauv.jplop.core.storage.file;

import tifauv.jplop.core.auth.UserBase;
import tifauv.jplop.core.board.History;
import tifauv.jplop.core.storage.StorageDelegate;
import tifauv.jplop.core.storage.StorageFactory;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class FileStorageFactory implements StorageFactory {

	/**
	 * @param p_object
	 *            the user base
	 * 
	 * @return the storage delegate
	 * 
	 * @see tifauv.jplop.core.storage.StorageFactory#createDelegate(Object)
	 */
	@Override
	public StorageDelegate<?> createDelegate(Object p_object) {
		if (p_object == null)
			throw new IllegalArgumentException("Cannot build a storage delegate from a null object");
		
		StorageDelegate<?> delegate = null;
		if (p_object instanceof UserBase)
			delegate = new UserDelegate((UserBase)p_object);
		else if (p_object instanceof History)
			delegate = new BoardDelegate((History)p_object);
		
		return delegate;
	}
}
