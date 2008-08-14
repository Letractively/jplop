/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.header;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.model.StringResourceModel;
import tifauv.jplop.web.JPlopSession;
import tifauv.jplop.web.components.account.AccountPage;
import tifauv.jplop.web.components.board.BoardPage;
import tifauv.jplop.web.components.config.ConfigPage;
import tifauv.jplop.web.components.signin.SigninPage;
import tifauv.jplop.web.components.signout.SignoutPage;
import tifauv.jplop.web.components.signup.SignupPage;

/**
 * This is the header panel.
 * It displays the board's name and title and the main navigation links.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class HeaderPanel extends Panel {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = 5136142326896758733L;
	

	
	// CONSTRUCTORS \\
	/**
	 * Creates the panel's labels.
	 * 
	 * @param p_id
	 *            the header panel identifier
	 */
	public HeaderPanel(String p_id) {
		super(p_id);
		
		// Title
		add(new Label("name",  new StringResourceModel("header.name", this, null)));
		add(new Label("title", new StringResourceModel("header.title", this, null)));
		
		// Links
		BookmarkablePageLink boardLink = new BookmarkablePageLink("board", BoardPage.class);
		boardLink.add(new Label("board-label", new StringResourceModel("header.board.label", this, null)));
		add(boardLink);

		BookmarkablePageLink configLink = new BookmarkablePageLink("config", ConfigPage.class);
		configLink.add(new Label("config-label",  new StringResourceModel("header.config.label", this, null)));
		add(configLink);
		
		BookmarkablePageLink signupLink = new BookmarkablePageLink("signup",   SignupPage.class);
		signupLink.add(new Label("signup-label",  new StringResourceModel("header.signup.label", this, null)));
		add(signupLink);
		
		BookmarkablePageLink signinLink = new BookmarkablePageLink("signin",   SigninPage.class) {
			private static final long serialVersionUID = -1652492751770135649L;

			@Override
			public boolean isVisible() {
				return !((JPlopSession)getSession()).isSignedIn();
			}};
		signinLink.add(new Label("signin-label",  new StringResourceModel("header.signin.label", this, null)));
		add(signinLink);
		
		BookmarkablePageLink accountLink = new BookmarkablePageLink("account",  AccountPage.class);
		accountLink.add(new Label("account-label",  new StringResourceModel("header.account.label", this, null)));
		add(accountLink);
		
		BookmarkablePageLink signoutLink = new BookmarkablePageLink("signout",  SignoutPage.class) {
			private static final long serialVersionUID = -3491536600221055012L;

			@Override
			public boolean isVisible() {
				return ((JPlopSession)getSession()).isSignedIn();
			}};
		signoutLink.add(new Label("signout-label",  new StringResourceModel("header.signout.label", this, null)));
		add(signoutLink);
	}
}
