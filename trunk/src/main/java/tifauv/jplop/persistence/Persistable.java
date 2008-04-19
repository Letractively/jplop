package tifauv.jplop.persistence;

import java.io.File;

/**
 * This interface defines the common methods that are expected from persistant objects.
 * <p>Implementing this interface doesn't ensure the object will be persistant.
 * To do so, you have to register it into the {@link PersistenceManager}.
 * It will take care of loading and saving the object's state.
 * Note that for the persistance system to work properly, you should register
 * the object as soon as it is instanciated.</p>  
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public interface Persistable {

	/**
	 * Gives the name of the file to save the object to.
	 */
	public String getFilename();
	
	
	/**
	 * Loads the object's state from file.
	 * 
	 * @param p_file
	 *            the file from which to load the object's state
	 * 
	 * @throws DeserializeException
	 *            if something occurs that prevents the object's state from being restored
	 */
	public void loadFromFile(File p_file)
	throws DeserializeException;
	
	
	/**
	 * Saves the object's state to a file.
	 * 
	 * @param p_file
	 *            the file where to save the object's state
	 * 
	 * @throws SerializeException
	 *            if something occurs that prevents the object's state from being saved
	 */
	public void saveToFile(File p_file)
	throws SerializeException;
}
