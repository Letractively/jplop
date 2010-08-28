package tifauv.jplop.core;


/**
 * This is the singleton class that uses the Backend. 
 */
public final class Main {

	// FIELDS \\
	/** The instance of the backend. */
	private static Backend s_backend;
	
	
	// GETTERS \\
	/**
	 * Gives the current backend instance.
	 */
	public static Backend get() {
		return s_backend;
	}
	
	
	// SETTERS \\
	/**
	 * This method allows a direct set of the backend.
	 * <p>It is meant to be used by unit test classes only to provide
	 * their own stub implementation of the Backend interface.</p>
	 */
	public static void set(Backend p_backend) {
		s_backend = p_backend;
	}
	
	
	// METHODS \\
	public static void create(String p_contextDir) {
		if (s_backend == null) {
			s_backend = new BackendImpl();
			s_backend.init(p_contextDir);
		}
	}
	
	
	public static void destroy() {
		if (s_backend != null) {
			s_backend.clean();
			s_backend = null;
		}
	}
}
