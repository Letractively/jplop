/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.account;

import org.apache.wicket.markup.html.WebPage;

import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;
import tifauv.jplop.web.models.AccountModel;

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

		// The form
		AccountModel model = new AccountModel();
		model.setLogin(((JPlopSession)getSession()).getAccount().getLogin());
		add(new AccountForm("editAccount", model));
		
		add(new FooterPanel("footer"));
	}
}
