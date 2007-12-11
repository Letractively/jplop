/**
 * 03 dec. 2007
 */
package tifauv.jplop;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import tifauv.jplop.model.Backend;

/**
 * This servlet initializes and cleans JPlop.
 * It does nothing more.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class InitServlet extends HttpServlet {

	// CONSTANTS \\
	/** The serialization UID. */
	private static final long serialVersionUID = 8108637762046470098L;
	
	/** The name of the servlet attribute that contains the backend. */
	private static final String BACKEND_ATTRIBUTE = "backend";

	
	// FIELDS \\
	/** The logger. */
	private Logger m_logger = Logger.getLogger(InitServlet.class);
	

	// METHODS \\
	/**
	 * Creates the Backend.
	 */
	@Override
	public final void init() {
		m_logger.info("====> \\_o< JPlop starting >o_/ <====");
		try {
			super.init();
			Backend.getInstance().init(getServletContext().getRealPath("/"));
			Backend.getInstance().loadFromCache();
			getServletContext().setAttribute(BACKEND_ATTRIBUTE, Backend.getInstance());
		}
		catch (Exception e) {
			m_logger.error("Cannot initialize the backend.", e);
		}
	}

	
	/**
	 * Destroys the Backend.
	 */
	@Override
	public final void destroy() {
		getServletContext().removeAttribute(BACKEND_ATTRIBUTE);
		Backend.destroy();
		m_logger.info("====> \\_x<~ JPlop stopped ~>x_/ <====");
	}
}
