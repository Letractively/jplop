/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class CloseAccountForm extends Form {

	// CONSTANTS \\
	/** The serialization id. */


	
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
