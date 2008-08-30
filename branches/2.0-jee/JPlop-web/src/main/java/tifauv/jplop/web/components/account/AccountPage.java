/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.account;

import javax.ejb.EJB;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import tifauv.jplop.ejb.account.AccountLocal;
import tifauv.jplop.utils.PasswordUtils;
import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.board.BoardPage;
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


/**
 * This is the account edition form.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
class EditAccountForm extends Form {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = 6824569790279063844L;

	
	// FIELDS \\
	/** The Accounts manager bean. */
	@EJB(name="ejb/stateless/Account")
	private AccountLocal m_accountBean;
	
	
	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public EditAccountForm(String p_name, IModel p_model) {
		super(p_name, p_model);
		
		/* The account part */
		add(new Label("account-title", new StringResourceModel("account.edit.title", null)));

		add(new Label("login-label", new StringResourceModel("account.edit.login.label", null)));
		add(new TextField("login").setRequired(true));
		
		add(new Label("password-label", new StringResourceModel("account.edit.password.label", null)));
		add(new PasswordTextField("password"));

		add(new Label("confirm-label", new StringResourceModel("account.edit.confirm.label", null)));
		add(new PasswordTextField("confirm"));
		
		/* The profile part. */
		add(new Label("profile-title", new StringResourceModel("profile.edit.title", null)));

		add(new Label("email-label", new StringResourceModel("profile.edit.email.label", null)));
		add(new TextField("profile.email"));
	}
	
	
	// METHODS \\
	/**
	 * Called when the form is submitted.
	 */
	@Override
	public void onSubmit() {
		UserAccount model = (UserAccount)getModel().getObject();
		model.validate();
		
		// Manage the password special case
		String password = model.getPassword();
		if (password != null && !password.isEmpty())
			password = PasswordUtils.sha256B64(model.getPassword());
		else
			password = null;
		m_accountBean.update(((JPlopSession)Session.get()).getAccount(), model.getLogin(), password);

		setResponsePage(BoardPage.class);
	}
}


/**
 * This is the account edition form.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
class CloseAccountForm extends Form {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = 1L;

	
	// FIELDS \\
	/** The Accounts manager bean. */
	@EJB(name="ejb/stateless/Account")
	private AccountLocal m_accountBean;
	
	
	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public CloseAccountForm(String p_name, IModel p_model) {
		super(p_name, p_model);
	}
	
	
	// METHODS \\
	/**
	 * Called when the form is submitted.
	 */
	@Override
	public void onSubmit() {
		//m_accountBean.update(((JPlopSession)Session.get()).getAccount(), model.getLogin(), password);

		setResponsePage(BoardPage.class);
	}
}
