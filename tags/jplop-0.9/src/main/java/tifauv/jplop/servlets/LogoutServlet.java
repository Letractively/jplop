package tifauv.jplop.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jplop.auth.User;

/**
 * Servlet implementation class for Servlet: LogoutServlet
 *
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class LogoutServlet extends HttpServlet {
	
	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = -2521791355556452988L;
	
	/** The page displayed when the action finishes. */
	private static final String SUCCESS_PAGE = "/board.jsp";

	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(LogoutServlet.class);
	
	
	// METHODS \\
	/**
	 * Logs out the session current user.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected final void doGet(HttpServletRequest p_request, HttpServletResponse p_response)
	throws IOException,
	ServletException {
		m_logger.info("New GET logout request from [" + p_request.getRemoteAddr() + "].");
		User user = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		if (user != null) {
			p_request.getSession().removeAttribute(CommonConstants.USER_SESSION_ATTR);
			m_logger.info("User '" + user.getLogin() + "' logged out.");
		}
		else
			m_logger.warn("No user to logout.");
		
		getServletContext().getRequestDispatcher(SUCCESS_PAGE).forward(p_request, p_response);
	}  	  	  	    
}