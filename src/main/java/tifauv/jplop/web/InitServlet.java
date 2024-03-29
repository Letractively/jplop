/**
 * 03 dec. 2007
 */
package tifauv.jplop.web;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import tifauv.jplop.core.CommonConstants;
import tifauv.jplop.core.Main;

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
	public void init() {
		m_logger.info("====> \\_o< JPlop starting >o_/ <====");
		try {
			super.init();
			Main.create(getServletContext().getRealPath("/"));
			getServletContext().setAttribute(CommonConstants.BACKEND_APPLICATION_ATTR, Main.get());
		} catch (Exception e) {
			m_logger.error("Cannot initialize the backend.", e);
		}
	}

	
	/**
	 * Destroys the Backend.
	 */
	@Override
	public void destroy() {
		getServletContext().removeAttribute(CommonConstants.BACKEND_APPLICATION_ATTR);
		Main.destroy();
		m_logger.info("====> \\_x<~ JPlop stopped ~>x_/ <====");
	}
}
