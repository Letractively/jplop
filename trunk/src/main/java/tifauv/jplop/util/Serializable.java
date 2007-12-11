package tifauv.jplop.util;

import java.io.File;

/**
 * This interface defines the common methods that are expected
 * from objects that should load and save themselves.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public interface Serializable {

	// GETTERS \\
	/**
	 * Gives the file.
	 */
	public File getFile();
	
	
	// SETTERS \\
	/**
	 * Sets the file. If the given file path is not absolute,
	 * it will be prefixed with the context directory.
	 * 
	 * @param p_contextDir 
	 *            the current context directory
	 * @param p_file
	 *            the given file path
	 */
	public void setFile(String p_contextDir, String p_file);
	
	
	// METHODS \\
	/**
	 * Loads from the file.
	 */
	public void loadFromFile()
	throws DeserializeException;
	
	/**
	 * Saves to the file.
	 */
	public void saveToFile()
	throws SerializeException;
}
