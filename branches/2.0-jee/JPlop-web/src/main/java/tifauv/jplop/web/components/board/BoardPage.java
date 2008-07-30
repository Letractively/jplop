/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.board;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class BoardPage extends WebPage {

	// CONSTRUCTORS \\
	public BoardPage() {
		add(new HeaderPanel("header"));
		add(new Label("message", "Toto"));
		add(new FooterPanel("footer"));
	}
}
