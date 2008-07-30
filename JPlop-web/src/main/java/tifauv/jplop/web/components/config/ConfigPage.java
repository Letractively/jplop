/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.config;

import org.apache.wicket.markup.html.WebPage;

import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;

/**
 * This page gives the configuration of the board.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class ConfigPage extends WebPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructors.
	 */
	public ConfigPage() {
		add(new HeaderPanel("header"));
		add(new FooterPanel("footer"));
	}
}
