/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.signup;

import org.apache.wicket.markup.html.WebPage;

import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;
import tifauv.jplop.web.models.AccountModel;

/**
 * This page manages the user registration.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class SignupPage extends WebPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public SignupPage() {
		add(new HeaderPanel("header"));
		
		// The form
		add(new SignupForm("register", new AccountModel()));
		
		add(new FooterPanel("footer"));
	}
}
