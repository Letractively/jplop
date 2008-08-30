/**
 * 31 juil. 08
 */
package tifauv.jplop.web.components.account;

import javax.ejb.EJB;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

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
public class AccountForm extends Form {

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
	public AccountForm(String p_name, UserAccount p_account) {
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
