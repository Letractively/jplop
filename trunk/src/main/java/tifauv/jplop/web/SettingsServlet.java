/**
 * 22 nov. 08
 */
package tifauv.jplop.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tifauv.jplop.core.Backend;

/**
 * This servlet gives the settings of the current user.
 * The message is in the {@link #MESSAGE_PARAM} parameter.
 * If there is a message, the HTTP code 200 Created is returned,
 * otherwise it is 406 Not Acceptable. 
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class SettingsServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = -8369848408089154463L;

	
	// METHODS \\
	/**
	 * Builds the settings of the current user, and returns them.
	 * The settings are build by {@link Backend#getSettings(HttpServletRequest)}.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 * 
	 * @see Backend#getSettings(HttpServletRequest)
	 */
	@Override
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response)
	throws IOException {
		// Build the response
		String settings = Backend.getInstance().getSettings(p_request);
		
		// Send the response
		p_response.setStatus(HttpServletResponse.SC_OK);
		p_response.setContentType("text/xml;charset=UTF-8");
		p_response.setCharacterEncoding("UTF-8");
		p_response.setContentLength(settings.getBytes("UTF-8").length);
		p_response.getWriter().write(settings);
	}
}
