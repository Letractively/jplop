package tifauv.jplop.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tifauv.jplop.Backend;

/**
 * This servlet returns the board's configuration.
 *
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
 public class DiscoveryServlet extends HttpServlet {
	 
	// CONSTANTS \\
    /** The serialization identifier. */
	private static final long serialVersionUID = 4478788310550784958L;
	
	
	// METHODS \\
	/**
	 * Returns the configuration of the board.
	 * 
	 * @param p_request
	 *            the HTTP request
	 * @param p_response
	 *            the HTTP response
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) {
		String config = Backend.getInstance().getBoardConfig();
		
		// Send the response
		p_response.setStatus(HttpServletResponse.SC_OK);
		p_response.setContentType("text/xml;charset=UTF-8");
		p_response.setCharacterEncoding("UTF-8");
		try {
			p_response.setContentLength(config.getBytes("UTF-8").length);
			p_response.getWriter().write(config);
		}
		catch (Exception e) {
			p_response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			p_response.setContentLength(0);
		}
	}  	  	  	    
}
