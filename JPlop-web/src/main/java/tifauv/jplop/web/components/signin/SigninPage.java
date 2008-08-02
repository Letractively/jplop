/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.signin;

import org.apache.wicket.authentication.pages.SignInPage;

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
public class SigninPage extends SignInPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public SigninPage() {
		add(new HeaderPanel("header"));
		add(new FooterPanel("footer"));
	}
}
