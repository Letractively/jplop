/**
 * 03 dec. 2007
 */
package tifauv.jplop.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jplop.Backend;
import tifauv.jplop.CommonConstants;
import tifauv.jplop.auth.Authenticator;
import tifauv.jplop.auth.User;
import tifauv.jplop.auth.UserBase;

/**
 * Servlet implementation class for Servlet: LogonServlet
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
 public final class LogonServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = -666278634960900803L;
	
	/** The name of the page to forward to if the registration succeeds. */
	private static final String SUCCESS_PAGE = "/board.jsp";
	
	/** The name of the page to forward to if the registration fails. */
	private static final String FAILURE_PAGE = "/logon.jsp";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(LogonServlet.class);
	 

	// METHODS \\
	/**
	 * Searches the {@link CommonConstants#LOGIN_PARAM} and {@link CommonConstants#PASSWORD_PARAM} request parameters
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
	protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response)
	throws IOException,
	ServletException {
		m_logger.info("New POST logon request from [" + p_request.getRemoteAddr() + "].");
		try {
			p_request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Cannot happen
			m_logger.warn("Cannot decode the request as UTF-8 !");
		}
		
		// Check whether we are already logged on
		User currentUser = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		if (currentUser != null) {
			m_logger.warn("The user [" + currentUser.getLogin() + "] tried to logon but he is already authenticated.");
			getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
			currentUser = null;
			return;
		}
		
		UserBase users = Backend.getInstance().getUserBase();
		if (users != null) {
			// Check the parameters are all there
			String username = p_request.getParameter(CommonConstants.LOGIN_PARAM);
			if (username == null) {
				m_logger.warn("The '" + CommonConstants.LOGIN_PARAM + "' request parameter is null.");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le login est obligatoire.");
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
				return;
			}

			String password = p_request.getParameter(CommonConstants.PASSWORD_PARAM);
			if (password == null) {
				m_logger.warn("The '" + CommonConstants.PASSWORD_PARAM + "' request parameter is null.");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le mot de passe est obligatoire.");
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
				return;
			}

			// OK, now we are sure we got all the required fields 
			m_logger.info("Logon request for user [" + username + "].");
			Authenticator authn = new Authenticator(username, password);
			if (authn.authenticate()) {
				p_request.getSession().setAttribute(CommonConstants.USER_SESSION_ATTR, authn.getUser());
				getServletContext().getRequestDispatcher(SUCCESS_PAGE).forward(p_request, p_response);
			}
			else {
				m_logger.warn("Authentication failed for user [" + username + "].");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "L'authentification a échoué.");
				p_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
			}

			return;
		}

		m_logger.warn("There is no user base.");
		p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "La création de compte n'est pas possible.");
		getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
	}
}