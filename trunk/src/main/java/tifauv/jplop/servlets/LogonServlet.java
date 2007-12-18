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

/**
 * Servlet implementation class for Servlet: LogonServlet
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
	public static final String USER_ATTRIBUTE = "subject";
	
	
	// FIELDS \\
	/** The logger. */
	private Logger m_logger = Logger.getLogger(LogonServlet.class);
	 

	// METHODS \\
	/**
	 * Searches the {@link #LOGIN_PARAM} and {@link #PASSWORD_PARAM} request parameters
	 * then tries to identify and authenticate the user.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response) {
		m_logger.info("New POST logon request from [" + p_request.getRemoteAddr() + "].");
		UserBase users = Backend.getInstance().getUserBase();
		if (users != null) {
			String username = p_request.getParameter(LOGIN_PARAM);
			String password = p_request.getParameter(PASSWORD_PARAM);
			m_logger.info("Logon request for user '" + username + "'.");
		
			Authenticator authn = new Authenticator(username, password);
			if (authn.authenticate())
				p_request.getSession().setAttribute(USER_ATTRIBUTE, authn.getUser());
		}
	}
}
