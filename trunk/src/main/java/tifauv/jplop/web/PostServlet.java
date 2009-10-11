/**
 * 19 oct. 2007
 */
package tifauv.jplop.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jplop.core.Backend;
import tifauv.jplop.core.CommonConstants;
import tifauv.jplop.core.auth.User;


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
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(PostServlet.class);

	
	// METHODS \\
	/**
	 * Adds the {@link #MESSAGE_PARAM} request parameter to the history.
	 * Responds with a 201 CREATED status if the message is added. The id
	 * of the new message is also sent back in the response's body.
	 * If no {@link #MESSAGE_PARAM} parameter exist in the request,
	 * responds with a 406 NOT_ACCEPTABLE.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 */
	@Override
	protected void doPost(HttpServletRequest p_request, HttpServletResponse p_response) {
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
			p_response.addHeader(CommonConstants.ERROR_HDR, "No message parameter");
			return;
		}
		
		// Remove the starting and trailing spaces
		message = message.trim();

		// Get the login of the logged user if any
		String login = null;
		String userAgent = p_request.getHeader(CommonConstants.USER_AGENT_HDR);
		User currentUser = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		if (currentUser != null) {
			login = currentUser.getLogin();
			if (currentUser.getNick() != null)
				userAgent = currentUser.getNick();
			m_logger.info("Message is '" + message + "' from '" + login + "'.");
		}
		else {
			String nick = (String)p_request.getSession().getAttribute(CommonConstants.NICK_SESSION_ATTR);
			if (nick != null) {
				userAgent = nick;
				m_logger.info("Message is '" + message + "' from an anonymous [" + nick + "].");
			}
			else
				m_logger.info("Message is '" + message + "' from an anonymous coward.");
		}
		
		// Check if the message is a known command
		boolean addMessage = true;
		if (message.length() > 1 && message.charAt(0) == '/') {
			if (message.startsWith("/nick ")) {
				String nick = message.substring("/nick ".length());
				if (currentUser != null)
					currentUser.setNick(nick);
				else
					p_request.getSession().setAttribute(CommonConstants.NICK_SESSION_ATTR, nick);
				
				addMessage = false;
			}
			else if (message.startsWith("/anonymous ")) {
				userAgent = "[Anonymous Coward]";
				message = message.substring("/anonymous ".length());
				login = null;
			}
		}
		
		// Add the message
		if (addMessage) {
			long id = Backend.getInstance().addMessage(userAgent, message, login);
			p_response.addHeader(CommonConstants.POSTID_HDR, Long.toString(id));
			p_response.setStatus(HttpServletResponse.SC_CREATED);
		}
		// Otherwise, it was a command
		else
			p_response.setStatus(HttpServletResponse.SC_OK);
	}
}
