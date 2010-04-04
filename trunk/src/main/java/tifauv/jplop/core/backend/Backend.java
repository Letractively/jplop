package tifauv.jplop.core.backend;

import javax.servlet.http.HttpServletRequest;

import tifauv.jplop.core.auth.UserBase;

public interface Backend {
	
	// GETTERS \\
	/**
	 * Gives the board's name.
	 */
	public String getName();

	
	/**
	 * Gives the board's full name.
	 */
	public String getFullName();
	
	
	/**
	 * Gives the board's URL.
	 */
	public String getURL();
	
	
	/**
	 * Gives the maximum number of messages of the history.
	 */
	public int getMaxSize();
	
	
	/**
	 * Gives the user base.
	 */
	public UserBase getUserBase();
	
	
	/**
	 * Gives the maximum length of the incoming messages.
	 * Bigger messages will be cut.
	 * 
	 * @see #addMessage(String, String, String)
	 */
	public int getMaxPostLength();
	
	
	/**
	 * Gives the Last-Modified header value to send.
	 */
	public String getLastModified();
	
	
	/**
	 * Gives the backend text.
	 */
	public String getText();

	
	/**
	 * Gives the backend text if it has been modified since the given date.
	 * Otherwise, just return <code>null</code>.
	 * 
	 * @param p_modifiedSince
	 *            the Modified-Since value
	 * 
	 * @return the backend text or <code>null</code>
	 */
	public String getText(String p_modifiedSince);
	
	
	/**
	 * Gives the board's configuration for compliant coincoins.
	 */
	public String getBoardConfig();
	
	
	/**
	 * 
	 * @return
	 */
	public String getSettings(HttpServletRequest p_request);
	
	
	// METHODS \\
	/**
	 * Loads the configuration of the Backend.
	 * 
	 * @param p_contextDir
	 *            the context directory
	 */
	public void loadConfig(String p_contextDir);

	
	/**
	 * Initializes the Backend.
	 */
	public void init();
	
	
	/**
	 * Adds a received message to the history.
	 * 
	 * @param p_info
	 *            the user-agent
	 * @param p_message
	 *            the message
	 * @param p_login
	 *            the login
	 * 
	 * @return the id of the new post
	 */
	public long addMessage(String p_info, String p_message, String p_login);
	
	
	/**
	 * Cleas the backup so that it is ready to stop.
	 * 
	 * @see #create(String)
	 */
	public void clean();
}
