/**
 * Feb 17, 2008
 */
package tifauv.jplop;

/**
 * This class contains some constants shared between the other classes.
 * For example, you will find here some attribute names used to exchange
 * data between servlets and JSP.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class CommonConstants {
	
	// REQUEST PARAMETERS \\
	/** The login request parameter. */
	public static final String LOGIN_PARAM = "login";
	
	/** The password request parameter. */
	public static final String PASSWORD_PARAM = "password";

	/** The password confirmation request parameter. */
	public static final String PASSWORD_CONFIRM_PARAM = "password-confirm";
	
	/** The message request parameter. */
	public static final String MESSAGE_PARAM = "message";
	
	
	// CONTEXT ATTRIBUTES \\
	/** The name of the application attribute that contains the backend. */
	public static final String BACKEND_APPLICATION_ATTR = "backend";

	/** The name of the session attribute that contains the authenticated subject. */
	public static final String USER_SESSION_ATTR = "subject";
	
	/** The name of the request attribute that contains an error message. */
	public static final String ERROR_REQUEST_ATTR = "errorMsg";
	
	
	// HTTP HEADERS \\
	/** The name of the User-Agent HTTP header. */
	public static final String USER_AGENT_HDR = "User-Agent";

	/** The name of the Post-Id HTTP header. */
	public static final String POSTID_HDR = "Post-Id";

	/** The If-Modified-Since header name. */
	public static final String IF_MODIFIED_SINCE_HDR = "If-Modified-Since";
	
	/** The Last-Modified header name.*/
	public static final String LAST_MODIFIED_HDR = "Last-Modified";
	
	/** The Pragma header name. */
	public static final String PRAGMA_HDR = "Pragma";
	
	/** The Cache-Control name. */
	public static final String CACHE_CONTROL_HDR = "Cache-Control";
	
	/** The no-cache header value. */
	public static final String NO_CACHE = "no-cache";

	
	// CONSTRUCTOR \\
	/**
	 * Prevent from instanciating this class.
	 */
	private CommonConstants() {
		// Nothing to do
	}
}
