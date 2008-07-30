/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.account;

import org.apache.wicket.markup.html.WebPage;

import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;

/**
 * This page displays the user's account.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class AccountPage extends WebPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public AccountPage() {
		add(new HeaderPanel("header"));
		add(new FooterPanel("footer"));
	}
}
