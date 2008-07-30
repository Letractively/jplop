/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.register;

import org.apache.wicket.markup.html.WebPage;

import tifauv.jplop.web.components.account.AccountModel;
import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;

/**
 * This page manages the user registration.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class RegisterPage extends WebPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public RegisterPage() {
		add(new HeaderPanel("header"));
		
		// The form
		add(new RegisterForm("register", new AccountModel()));
		
		add(new FooterPanel("footer"));
	}
}
