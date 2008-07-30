/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.about;

import org.apache.wicket.markup.html.WebPage;

import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;

/**
 * This is the about page.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class AboutPage extends WebPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public AboutPage() {
		add(new HeaderPanel("header"));
		add(new FooterPanel("footer"));
	}
}
