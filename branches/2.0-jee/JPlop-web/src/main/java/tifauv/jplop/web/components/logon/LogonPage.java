/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.logon;

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
public class LogonPage extends SignInPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public LogonPage() {
		add(new HeaderPanel("header"));
		add(new FooterPanel("footer"));
	}
}
