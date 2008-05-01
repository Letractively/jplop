/**
 * 19 oct. 2007
 */
package tifauv.jplop.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tifauv.jplop.Backend;

/**
 * This servlet sends the backend.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class BackendServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = 8034380019578813009L;

	/** The If-Modified-Since header name. */
	private static final String HTTP_IF_MODIFIED_SINCE = "If-Modified-Since";
	
	/** The Last-Modified header name.*/
	private static final String HTTP_LAST_MODIFIED = "Last-Modified";
	
	/** The Pragma header name. */
	private static final String HTTP_PRAGMA = "Pragma";
	
	/** The Cache-Control name. */
	private static final String HTTP_CACHE_CONTROL = "Cache-Control";
	
	/** The no-cache header value. */
	private static final String NO_CACHE = "no-cache";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(BackendServlet.class);
	

	// METHODS \\
	/**
	 * Sends the backend if the If-Modified-Since request header is older than the
	 * Last-Modified date of the history.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 */
	@Override
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response)
	throws IOException {
		m_logger.info("New GET backend request from [" + p_request.getRemoteAddr() + "].");
		String ifModifiedSince = p_request.getHeader(HTTP_IF_MODIFIED_SINCE);
		m_logger.info("If-Modified-Since : " + ifModifiedSince);
		m_logger.info("Last-Modified : " + Backend.getInstance().getLastModified());
		String text = Backend.getInstance().getText(ifModifiedSince);

		p_response.setHeader(HTTP_PRAGMA, NO_CACHE);
		p_response.setHeader(HTTP_CACHE_CONTROL, NO_CACHE);
		
		if (text == null) {
			p_response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			p_response.setHeader(HTTP_LAST_MODIFIED, Backend.getInstance().getLastModified());
			p_response.setContentLength(0);
		}
		else {
			p_response.setStatus(HttpServletResponse.SC_OK);
			p_response.setContentType("text/xml;charset=UTF-8");
			p_response.setCharacterEncoding("UTF-8");
			p_response.setHeader(HTTP_LAST_MODIFIED, Backend.getInstance().getLastModified());
			p_response.setContentLength(text.getBytes("UTF-8").length);
			p_response.getWriter().write(text);
		}
	}
}
