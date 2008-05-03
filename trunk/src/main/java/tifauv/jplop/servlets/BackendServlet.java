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
import tifauv.jplop.CommonConstants;

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
		String ifModifiedSince = p_request.getHeader(CommonConstants.IF_MODIFIED_SINCE_HDR);
		m_logger.info("If-Modified-Since : " + ifModifiedSince);
		m_logger.info("Last-Modified : " + Backend.getInstance().getLastModified());
		String text = Backend.getInstance().getText(ifModifiedSince);

		p_response.setHeader(CommonConstants.PRAGMA_HDR, CommonConstants.NO_CACHE);
		p_response.setHeader(CommonConstants.CACHE_CONTROL_HDR, CommonConstants.NO_CACHE);
		
		if (text == null) {
			p_response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			p_response.setHeader(CommonConstants.LAST_MODIFIED_HDR, Backend.getInstance().getLastModified());
			p_response.setContentLength(0);
		}
		else {
			p_response.setStatus(HttpServletResponse.SC_OK);
			p_response.setContentType("text/xml;charset=UTF-8");
			p_response.setCharacterEncoding("UTF-8");
			p_response.setHeader(CommonConstants.LAST_MODIFIED_HDR, Backend.getInstance().getLastModified());
			p_response.setContentLength(text.getBytes("UTF-8").length);
			p_response.getWriter().write(text);
		}
	}
}
