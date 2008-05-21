/**
 * Feb 17, 2008
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
import tifauv.jplop.auth.PasswordException;
import tifauv.jplop.auth.User;
import tifauv.jplop.auth.UserBase;

/**
 * Servlet implementation class for Servlet: RegisterServlet
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class RegisterServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = 3353591499960135295L;
	
	/** The name of the page to forward to if the registration succeeds. */
	private static final String SUCCESS_PAGE = "/board.jsp";
	
	/** The name of the page to forward to if the registration fails. */
	private static final String FAILURE_PAGE = "/register.jsp";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(RegisterServlet.class);
	 

	// METHODS \\
	/**
	 * Searches the {@link CommonConstants#LOGIN_PARAM}, {@link CommonConstants#PASSWORD_PARAM} and {@link CommonConstants#PASSWORD_CONFIRM_PARAM}
	 * request parameters then tries to create a new account.
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
		m_logger.info("New POST register request from [" + p_request.getRemoteAddr() + "].");
		try {
			p_request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Cannot happen
			m_logger.warn("Cannot decode the request as UTF-8 !");
		}

		// Check whether we are already logged on
		User currentUser = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		if (currentUser != null) {
			m_logger.warn("The user '" + currentUser.getLogin() + "' tried to logon but he is already authenticated.");
			p_response.setStatus(HttpServletResponse.SC_CONFLICT);
			p_response.addHeader(CommonConstants.ERROR_HDR, "Already authenticated");
			getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
			currentUser = null;
			return;
		}
		
		UserBase users = Backend.getInstance().getUserBase();
		if (users != null) {
			// Check the parameters are all there
			String username = p_request.getParameter(CommonConstants.LOGIN_PARAM);
			if (username == null || username.trim().length() == 0) {
				m_logger.warn("The '" + CommonConstants.LOGIN_PARAM + "' is null or empty.");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le login est obligatoire.");
				p_response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				p_response.addHeader(CommonConstants.ERROR_HDR, "Missing " + CommonConstants.LOGIN_PARAM + " parameter");
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
				return;
			}

			String password = p_request.getParameter(CommonConstants.PASSWORD_PARAM);
			if (password == null || password.length() == 0) {
				m_logger.warn("The '" + CommonConstants.PASSWORD_PARAM + "' is null or empty.");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le mot de passe est obligatoire.");
				p_response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				p_response.addHeader(CommonConstants.ERROR_HDR, "Missing " + CommonConstants.PASSWORD_PARAM + " parameter");
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
				return;
			}

			String confirm  = p_request.getParameter(CommonConstants.PASSWORD_CONFIRM_PARAM);
			if (confirm == null || confirm.length() == 0) {
				m_logger.warn("The '" + CommonConstants.PASSWORD_CONFIRM_PARAM + "' is null or empty.");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "La confirmation du mot de passe est obligatoire.");
				p_response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				p_response.addHeader(CommonConstants.ERROR_HDR, "Missing " + CommonConstants.PASSWORD_CONFIRM_PARAM + " parameter");
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
				return;
			}

			// OK, now we are sure we got all the required fields 
			m_logger.info("Account creation request for user '" + username + "'.");
		
			// Fail if the password and confirmation don't match
			if (!password.equals(confirm)) {
				m_logger.warn("The password and confirmation for user '" + username + "' don't match.");
				p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le mot de passe et sa confirmation ne correspondent pas, veuillez réessayer.");
				p_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				p_response.addHeader(CommonConstants.ERROR_HDR, "Password confirmation failed");
				getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
				return;
			}

			synchronized (users) {
				// Fail if there is already an account for that username
				if (users.containsUser(username)) {
					m_logger.warn("There is already an account for user '" + username + "'.");
					p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le compte \"" + username + "\" existe déjà, veuillez choisir un autre login.");
					p_response.setStatus(HttpServletResponse.SC_CONFLICT);
					p_response.addHeader(CommonConstants.ERROR_HDR, "Account already exists");
					getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
					return;
				}
				
				// Create the user
				User user = new User();
				try {
					user.setLogin(username);
					user.setPassword(password);
					user.setRoles(users.getDefaultRoles());
				} catch (PasswordException e) {
					user = null;
					m_logger.warn("Could not create the user '" + username + "' : " + e.getMessage());
					p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "Le mot de passe ne correspond pas aux critères exigés.");
					p_response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					p_response.addHeader(CommonConstants.ERROR_HDR, "Invalid password");
					getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
					return;
				}
				
				// Add the user
				users.addUser(user);
				p_request.getSession().setAttribute(CommonConstants.USER_SESSION_ATTR, user);
				p_response.setStatus(HttpServletResponse.SC_CREATED);
				getServletContext().getRequestDispatcher(SUCCESS_PAGE).forward(p_request, p_response);
				return;
			}
		}

		m_logger.warn("There is no user base.");
		p_request.setAttribute(CommonConstants.ERROR_REQUEST_ATTR, "La création de compte n'est pas possible.");
		p_response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		p_response.addHeader(CommonConstants.ERROR_HDR, "Account management disabled");
		getServletContext().getRequestDispatcher(FAILURE_PAGE).forward(p_request, p_response);
	}
}