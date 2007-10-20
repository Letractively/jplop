/**
 * 19 oct. 07
 */
package tifauv.jboard.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jboard.model.Backend;

/**
 * This servlet sends the backend.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class BackendServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = 8034380019578813009L;

	/** The If-Modified-Since header name. */
	private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	
	/** The Last-Modified header name.*/
	private static final String LAST_MODIFIED = "Last-Modified";
	
	
	// FIELDS \\
	/** The logger. */
	private Logger m_logger = Logger.getLogger(BackendServlet.class);
	

	// METHODS \\
	/**
	 * Ensures the Backend is created.
	 */
	@Override
	public final void init() {
		Backend.getInstance();
		m_logger.info("Backend servlet ready.");
	}
	
	
	/**
	 * Logs a message.
	 */
	@Override
	public final void destroy() {
		m_logger.info("Backend servlet stopped.");
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
		m_logger.info("New GET backend request from [" + p_request.getRemoteAddr() + "].");
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
		m_logger.info("New POST backend request from [" + p_request.getRemoteAddr() + "].");
		doWork(p_request, p_response);
	}
	
	
	/**
	 * Sends the backend if the If-Modified-Since request header is older than the
	 * Last-Modified date of the history.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 */
	private final void doWork(HttpServletRequest p_request, HttpServletResponse p_response) {
		String text = Backend.getInstance().getText(p_request.getParameter(IF_MODIFIED_SINCE));

		if (text == null) {
			p_response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			p_response.setHeader(LAST_MODIFIED, Backend.getInstance().getLastModified());
			p_response.setContentLength(0);
		}
		else {
			p_response.setStatus(HttpServletResponse.SC_OK);
			p_response.setContentType("text/xml");
			p_response.setHeader(LAST_MODIFIED, Backend.getInstance().getLastModified());
			p_response.setContentLength(text.length());
			try {
				p_response.getWriter().print(text);
			}
			catch (IOException e) {
				p_response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				p_response.setContentLength(0);
			}
		}
	}
}
