/**
 * 03 dec. 2007
 */
package tifauv.jplop.servlets;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import tifauv.jplop.Backend;
import tifauv.jplop.CommonConstants;

/**
 * This servlet initializes and cleans JPlop.
 * It does nothing more.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class InitServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = 8108637762046470098L;

	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(InitServlet.class);
	

	// METHODS \\
	/**
	 * Creates the Backend.
	 */
	@Override
	public final void init() {
		m_logger.info("====> \\_o< JPlop starting >o_/ <====");
		try {
			super.init();
			Backend.create(getServletContext().getRealPath("/"));
			getServletContext().setAttribute(CommonConstants.BACKEND_APPLICATION_ATTR, Backend.getInstance());
		} catch (Exception e) {
			m_logger.error("Cannot initialize the backend.", e);
		}
	}

	
	/**
	 * Destroys the Backend.
	 */
	@Override
	public final void destroy() {
		getServletContext().removeAttribute(CommonConstants.BACKEND_APPLICATION_ATTR);
		Backend.destroy();
		m_logger.info("====> \\_x<~ JPlop stopped ~>x_/ <====");
	}
}
