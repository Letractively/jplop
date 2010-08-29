/**
 * 22 nov. 08
 */
package tifauv.jplop.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tifauv.jplop.core.CommonConstants;
import tifauv.jplop.core.auth.User;

/**
 * This servlet gives the settings of the current user.
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
		String settings = getSettings(p_request);
		
		// Send the response
		p_response.setStatus(HttpServletResponse.SC_OK);
		p_response.setContentType("text/xml;charset=UTF-8");
		p_response.setCharacterEncoding("UTF-8");
		p_response.setContentLength(settings.getBytes("UTF-8").length);
		p_response.getWriter().write(settings);
	}
	
	
	/**
	 * 
	 * @param p_request
	 * @return
	 */
	private String getSettings(HttpServletRequest p_request) {
		User user = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		String login = null;
		String nick;
		String userAgent = p_request.getHeader(CommonConstants.USER_AGENT_HDR);

		// Compute the buffer's needed length
		int length = 89 + userAgent.length();
		if (user != null) {
			login = user.getLogin();
			length += 28 + login.length();
		}
		if (user != null && user.getNick() != null)
			nick = user.getNick();
		else
			nick = (String)p_request.getSession().getAttribute(CommonConstants.NICK_SESSION_ATTR);
		if (nick != null)
			length += 27 + nick.length();
		
		// Create and fill the buffer
		StringBuffer buffer = new StringBuffer(length);
		buffer.append("<?xml version=\"1.0\"?>")
		.append("<settings version=\"1.0\">");
		if (login != null)
			buffer.append("<param name=\"Login\">").append(login).append("</param>");
		if (nick != null)
			buffer.append("<param name=\"Nick\">").append(nick).append("</param>");
		buffer.append("<param name=\"User-Agent\">")
		.append(userAgent)
		.append("</param>");
		buffer.append("</settings>");
		return buffer.toString();
	}
}
