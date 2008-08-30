/**
 * 31 juil. 08
 */
package tifauv.jplop.web.components.account;

import javax.ejb.EJB;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import tifauv.jplop.ejb.account.AccountLocal;
import tifauv.jplop.utils.PasswordUtils;
import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.board.BoardPage;
import tifauv.jplop.web.models.UserAccount;

/**
 * This is the account edition form.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class EditAccountForm extends Form {

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
