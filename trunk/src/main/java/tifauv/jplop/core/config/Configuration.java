/**
 * 29 août 2010
 */
package tifauv.jplop.core.config;


/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public interface Configuration {
	
	// GETTERS \\
	/**
	 * Gives the webapp's context directory.
	 */
	String getContextDir();

	
	/**
	 * Gives the board's name.
	 */
	String getName();

	
	/**
	 * Gives the board's full name.
	 */
	String getFullName();
	
	
	/**
	 * Gives the board's URL.
	 */
	String getURL();
	
	
	/**
	 * Gives the maximum number of messages of the history.
	 */
	int getMaxSize();
	
	
	int getAutoSaveInterval();
	
	
	/**
	 * Gives the maximum length of the incoming messages.
	 * Bigger messages will be cut.
	 * 
	 * @see #addMessage(String, String, String)
	 */
	int getMaxPostLength();
	
	
	/**
	 * Gives the name of the storage factory to use.
	 */
	String getStorageFactoryName();

	
	/**
	 * Gives the string value of a configuration property.
	 * 
	 * @param p_key
	 *            the name of the property
	 * 
	 * @return the value of the property as a string
	 */
	String getString(String p_key);
	
	
	/**
	 * Gives the integer value of a configuration property.
	 * 
	 * @param p_key
	 *            the name of the property
	 * 
	 * @return the value of the property as an integer
	 * 
	 * @exception NumberFormatException
	 *            if the property value cannot be parsed as an integer
	 */
	int getInt(String p_key);
	
	
	// METHODS \\
	/**
	 * Loads the configuration.
	 * 
	 * @param p_contextDir
	 *            the context directory
	 */
	void load(String p_contextDir);
}