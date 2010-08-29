package tifauv.jplop.core;

import tifauv.jplop.core.auth.UserBase;
import tifauv.jplop.core.board.History;
import tifauv.jplop.core.config.Configuration;

public interface Backend {
	
	// GETTERS \\
	/**
	 * Gives the backend's configuration.
	 */
	Configuration getConfig();

	
	/**
	 * Gives the backend's configuration.
	 */
	History getHistory();

	
	/**
	 * Gives the user base.
	 */
	UserBase getUserBase();
	
	
	// METHODS \\
	/**
	 * Initializes the Backend.
	 */
	void init(String p_contextDir);
	
	
	/**
	 * Cleas the backup so that it is ready to stop.
	 * 
	 * @see #create(String)
	 */
	void clean();
}
