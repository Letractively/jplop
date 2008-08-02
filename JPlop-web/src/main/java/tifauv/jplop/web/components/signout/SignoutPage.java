/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.signout;

import org.apache.wicket.authentication.pages.SignOutPage;

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
public class SignoutPage extends SignOutPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public SignoutPage() {
		add(new HeaderPanel("header"));
		add(new FooterPanel("footer"));
	}
}
