package tifauv.jplop.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tifauv.jplop.core.Main;
import tifauv.jplop.core.config.export.BoardConfigExporter;

/**
 * This servlet returns the board's configuration.
 *
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
 public final class DiscoveryServlet extends HttpServlet {
	 
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
	 * @see Backend#getBoardConfig()
	 */
	@Override
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response)
	throws IOException {
		StringBuffer buffer = new StringBuffer();
		new BoardConfigExporter().export(Main.get().getConfig(), buffer);
		String config = buffer.toString();
		
		// Send the response
		p_response.setStatus(HttpServletResponse.SC_OK);
		p_response.setContentType("text/xml;charset=UTF-8");
		p_response.setCharacterEncoding("UTF-8");
		p_response.setContentLength(config.getBytes("UTF-8").length);
		p_response.getWriter().write(config);
	}  	  	  	    
}
