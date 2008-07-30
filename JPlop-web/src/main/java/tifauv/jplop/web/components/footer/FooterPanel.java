/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.footer;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import tifauv.jplop.web.components.about.AboutPage;

/**
 * This is the footer panel.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class FooterPanel extends Panel {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = -780689107623442355L;


	// CONSTRUCTORS \\
	/**
	 * Creates the panel.
	 * 
	 * @param p_id
	 *            the footer panel identifier
	 */
	public FooterPanel(String p_id) {
		super(p_id);
		add(new BookmarkablePageLink("about", AboutPage.class));
	}
}
