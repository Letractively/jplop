/**
 * 4 avr. 2010
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
public interface StorageFactory {

	StorageDelegate<?> createDelegate(Object p_userBase); 
}
