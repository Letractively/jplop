/**
 * 03 dec. 2007
 */
package tifauv.jplop.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jplop.Backend;
import tifauv.jplop.auth.Authenticator;
import tifauv.jplop.auth.UserBase;
import tifauv.jplop.auth.UserBaseAuthn;

/**
 * Servlet implementation class for Servlet: LogonServlet
 *
 * @web.servlet
 *   name="LogonServlet"
 *   display-name="LogonServlet" 
 *   description="The servlet used to log on the board" 
 *
 * @web.servlet-mapping
 *   url-pattern="/logon"
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
 public class LogonServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = -666278634960900803L;
	
	/** The login request parameter. */
	private static final String LOGIN_PARAM = "login";
	
	/** The password request parameter. */
	private static final String PASSWORD_PARAM = "password";
	
	/** The name of the session attribute that contains the authenticated subject. */
	public static final String SUBJECT_ATTRIBUTE = "subject";
	
	
	// FIELDS \\
	/** The logger. */
	private Logger m_logger = Logger.getLogger(LogonServlet.class);
	 

	// METHODS \\
	/**
	 * Calls {@link #doWork(HttpServletRequest, HttpServletResponse)}.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 * 
	 * @see #doWork(HttpServletRequest, HttpServletResponse)
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) {
		m_logger.info("New GET logon request from [" + p_request.getRemoteAddr() + "].");
		doWork(p_request, p_response);
	}

	
	/**
	 * Calls {@link #doWork(HttpServletRequest, HttpServletResponse)}.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 * 
	 * @see #doWork(HttpServletRequest, HttpServletResponse)
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response) {
		m_logger.info("New POST logon request from [" + p_request.getRemoteAddr() + "].");
		doWork(p_request, p_response);
	}
	
	
	/**
	 * .
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 */
	private final void doWork(HttpServletRequest p_request, HttpServletResponse p_response) {
		UserBase users = Backend.getInstance().getUserBase();
		if (users != null) {
			String username = p_request.getParameter(LOGIN_PARAM);
			String password = p_request.getParameter(PASSWORD_PARAM);
			m_logger.info("Logon request for user '" + username + "'.");
		
			Authenticator authn = new UserBaseAuthn(username, password);
			if (authn.authenticate())
				p_request.getSession().setAttribute(SUBJECT_ATTRIBUTE, authn.getSubject());
		}
	}
}