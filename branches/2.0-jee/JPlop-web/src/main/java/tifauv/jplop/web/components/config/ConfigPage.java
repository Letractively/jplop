/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.config;

import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
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
		
		add(new Label("url-discover", "http://localhost:8080/jplop/discover"));
		add(new Label("site", "JPlop"));
		add(new Label("title", "Da JEE Board"));
		add(new Label("baseurl", "http://localhost:8080/jplop"));

		add(new Label("config-site", "JPlop"));
		add(new Label("config-login", "Da JEE Board"));
		add(new Label("url-backend", "http://localhost:8080/jplop/backend"));
		add(new Label("url-post", "http://localhost:8080/jplop/post"));
		add(new Label("config-maxlength", "512"));
		add(new Label("config-site2", "JPlop"));
		add(new Label("session-id", "DEADBEEF"));

		add(new FooterPanel("footer"));
	}
}
