/**
 * 28 juil. 08
 */
package tifauv.jplop.web.components.signup;

import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import tifauv.jplop.web.components.footer.FooterPanel;
import tifauv.jplop.web.components.header.HeaderPanel;
import tifauv.jplop.web.models.UserAccount;

/**
 * This page manages the user registration.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class SignupPage extends WebPage {

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public SignupPage() {
		add(new HeaderPanel("header"));

		add(new Label("title", new StringResourceModel("signup.title", this, null)));
		add(new FeedbackPanel("feedback"));
		IModel model = new CompoundPropertyModel( new UserAccount() );
		add(new SignupForm("signup-form", model));

		add(new FooterPanel("footer"));
	}
}
