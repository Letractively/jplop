/**
 * 19 oct. 2007
 */
package tifauv.jplop.servlets;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jplop.Backend;
import tifauv.jplop.auth.User;


/**
 * This servlet receives the post request.
 * The message is in the {@link #MESSAGE_PARAM} parameter.
 * If there is a message, the HTTP code 201 Created is returned,
 * otherwise it is 406 Not Acceptable. 
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class PostServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = -6158071296626158191L;

	/** The User-Agent request header. */
	private static final String USER_AGENT = "User-Agent";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(PostServlet.class);

	
	// METHODS \\
	/**
	 * Adds the {@link #MESSAGE_PARAM} request parameter to the history.
	 * Responds with a 201 CREATED status if the message is added.
	 * If no {@link #MESSAGE_PARAM} parameter exist in the request,
	 * responds with a 406 NOT_ACCEPTABLE.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 */
	@Override
	protected final void doPost(HttpServletRequest p_request, HttpServletResponse p_response) {
		m_logger.info("New POST message request from [" + p_request.getRemoteAddr() + "].");
		try {
			p_request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Cannot happen
			m_logger.warn("Cannot decode the request as UTF-8 !");
		}
		
		// Get the message parameter and check it
		String message = p_request.getParameter(CommonConstants.MESSAGE_PARAM);
		if (message == null) {
			m_logger.info("No message parameter, skipping...");
			p_response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		
		// Remove the starting and trailing spaces
		message = message.trim();

		// Get the login of the logged user if any
		String login = null;
		User currentUser = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		if (currentUser != null) {
			login = currentUser.getLogin();
			currentUser = null;
			m_logger.info("Message is '" + message + "' from '" + login + "'.");
		}
		else
			m_logger.info("Message is '" + message + "' from an anonymous coward.");
		
		// Add the message
		String userAgent = p_request.getHeader(USER_AGENT);
		Backend.getInstance().addMessage(userAgent, message, login);
		p_response.setStatus(HttpServletResponse.SC_CREATED);
	}
}
