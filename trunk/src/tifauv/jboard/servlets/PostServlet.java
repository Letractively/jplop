/**
 * 19 oct. 07
 */
package tifauv.jboard.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jboard.model.Backend;

/**
 * 
 *
 * @version 1.0
 *
 * @author Olivier Serve <olivier.serve@bull.net>
 */
public class PostServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = -6158071296626158191L;

	/** The User-Agent request header. */
	private static final String USER_AGENT = "User-Agent";
	
	/** The message request parameter. */
	private static final String MESSAGE_PARAM = "message";
	
	
	// FIELDS \\
	/** The logger. */
	private Logger m_logger = Logger.getLogger(PostServlet.class);

	
	// METHODS \\
	/**
	 * Ensures the Backend is created.
	 */
	@Override
	public final void init() {
		Backend.getInstance();
		m_logger.info("Post servlet ready.");
	}
	
	
	/**
	 * Logs a message.
	 */
	@Override
	public final void destroy() {
		m_logger.info("Post servlet stopped.");
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
	 */
	@Override
	public final void doGet(HttpServletRequest p_request, HttpServletResponse p_response) {
		m_logger.info("New GET message request from [" + p_request.getRemoteAddr() + "].");
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
	 */
	@Override
	public final void doPost(HttpServletRequest p_request, HttpServletResponse p_response) {
		m_logger.info("New POST message request from [" + p_request.getRemoteAddr() + "].");
		doWork(p_request, p_response);
	}


	/**
	 * Adds the {@link #MESSAGE_PARAM} request parameter to the history.
	 * Responds with a 202 ACCEPTED status if the message is added.
	 * If no {@link #MESSAGE_PARAM} parameter exist in the request,
	 * responds with a 406 NOT_ACCEPTABLE.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 */
	private final void doWork(HttpServletRequest p_request, HttpServletResponse p_response) {
		String message = p_request.getParameter(MESSAGE_PARAM);
		
		if (message == null) {
			p_response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
		else {
			String userAgent = p_request.getHeader(USER_AGENT);
			Backend.getInstance().addMessage(userAgent, message);
			p_response.setStatus(HttpServletResponse.SC_ACCEPTED);
		}
	}
}
