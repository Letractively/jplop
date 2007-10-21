/**
 * 19 oct. 07
 */
package tifauv.jboard.model;

/**
 * 
 *
 * @version 0.1
 *
 * @author Olivier Serve <olivier.serve@bull.net>
 */
public class Backend {

	// STATIC FIELDS \\
	private static Backend s_instance;
	
	
	// FIELDS \\
	private History m_history;
	

	// CONSTRUCTORS \\
	private Backend() {
		m_history = new History("http://localhost:8080/jboard");
	}
	

	// GETTERS \\
	public final String getLastModified() {
		return m_history.getLastModified();
	}
	
	public final String getText() {
		return m_history.toString();
	}
	
	public final synchronized String getText(String p_modifiedSince) {
		if (m_history.isModifiedSince(p_modifiedSince))
			return m_history.toString();
		return null;
	}
	
	public final synchronized void addMessage(String p_info, String p_message) {
		m_history.addPost(new Post(p_info, p_message));
	}
	
	public final synchronized void addMessage(String p_info, String p_message, String p_login) {
		m_history.addPost(new Post(p_info, p_message, p_login));
	}
	

	// METHODS \\
	public static synchronized Backend getInstance() {
		if (s_instance == null) {
			s_instance = new Backend();
		}
		return s_instance;
	}
}
