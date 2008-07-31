/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.register;

import javax.ejb.EJB;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import tifauv.jplop.ejb.account.AccountLocal;
import tifauv.jplop.entity.Account;
import tifauv.jplop.utils.PasswordUtils;
import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.account.AccountPage;
import tifauv.jplop.web.models.AccountModel;

/**
 * This is the account registration form.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class RegisterForm extends Form {

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
	public RegisterForm(String p_name, AccountModel p_account) {
		super(p_name, new CompoundPropertyModel(p_account));
		add(new TextField("login"));
		add(new PasswordTextField("password"));
		add(new PasswordTextField("confirm"));
	}
	
	
	// METHODS \\
	/**
	 * Called when the form is submitted.
	 */
	@Override
	public void onSubmit() {
		AccountModel model = (AccountModel)getModel().getObject();
		model.validate();
		
		Account user = m_accountBean.createUser(new Account(), model.getLogin(), PasswordUtils.sha256B64(model.getPassword()));
		((JPlopSession)Session.get()).setAccount(user);

		setResponsePage(AccountPage.class);
	}
}