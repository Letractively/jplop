/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.account;

import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;
import tifauv.jplop.web.models.UserAccount;

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

		// The page title
		add(new Label("page-title", new StringResourceModel("account.page.title", null)));
		
		// The edit form
		UserAccount model = new UserAccount();
		model.setLogin(((JPlopSession)getSession()).getAccount().getLogin());
		add(new EditAccountForm("editAccount", new CompoundPropertyModel(model)));
		
		// The account closing form
		add(new Label("close-title", new StringResourceModel("account.close.title", null)));
		add(new Label("close-message", new StringResourceModel("account.close.message", null)));
		add(new CloseAccountForm("closeAccount", null));
		
		add(new FooterPanel("footer"));
	}
}
