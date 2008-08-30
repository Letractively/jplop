/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.signup;

import javax.ejb.EJB;

import javax.ejb.EJBException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import tifauv.jplop.exceptions.ValidationException;
import tifauv.jplop.ejb.account.AccountLocal;
import tifauv.jplop.entity.Account;
import tifauv.jplop.utils.PasswordUtils;
import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.account.AccountPage;
import tifauv.jplop.web.models.UserAccount;

/**
 * This is the account registration form.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class SignupForm extends Form {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = 2502959180024187180L;


	// FIELDS \\
	/** The Accounts manager bean. */
	@EJB(name="ejb/stateless/Account")
	private AccountLocal m_accountBean;


	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public SignupForm(String p_name, IModel p_model) {
		super(p_name, p_model);

		add(new Label("login-label", new StringResourceModel("account.signup.login.label", null)));
		add(new TextField("login").setRequired(true));
		
		add(new Label("password-label", new StringResourceModel("account.signup.password.label", null)));
		add(new PasswordTextField("password").setRequired(true));

		add(new Label("confirm-label", new StringResourceModel("account.signup.confirm.label", null)));
		add(new PasswordTextField("confirm").setRequired(true));
	}


	// METHODS \\
	/**
	 * Called when the form is submitted.
	 */
	@Override
	public void onSubmit() {
		try {
			UserAccount model = (UserAccount)getModelObject();
			
			// Check the data validity (password == confirm)
			model.validate();

			// Create the account
			m_accountBean.createUser(new Account(), model.getLogin(), PasswordUtils.sha256B64(model.getPassword()));
			
			// Auto-login
			((JPlopSession)Session.get()).signIn(model.getLogin(), model.getPassword());
			
			// Next : my account
			setResponsePage(AccountPage.class);
		} catch (ValidationException e) {
			// Bad data
			error(new StringResourceModel("account.signup.validation.error", getWebPage(), null).getString());
		} catch (EJBException e) {
			// Couldn't create the user
			error(new StringResourceModel("account.signup.creation.error", getWebPage(), null).getString());
		}
	}
}
