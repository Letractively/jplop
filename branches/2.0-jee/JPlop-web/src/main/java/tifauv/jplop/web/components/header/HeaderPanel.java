/**
 * 27 juil. 08
 */
package tifauv.jplop.web.components.header;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

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
	
	
	// FIELDS \\
	/** The account manager. */
	

	
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
		add(new Label("name", "JPLop"));
		add(new Label("title", "Da JEE board"));
		
		// Links
		add(new BookmarkablePageLink("board",    BoardPage.class));
		add(new BookmarkablePageLink("config",   ConfigPage.class));
		add(new BookmarkablePageLink("signup",   SignupPage.class));
		add(new BookmarkablePageLink("signin",   SigninPage.class) {
			private static final long serialVersionUID = -1652492751770135649L;

			@Override
			public boolean isVisible() {
				return !((JPlopSession)getSession()).isSignedIn();
			}});
		add(new BookmarkablePageLink("account",  AccountPage.class));
		add(new BookmarkablePageLink("signout",  SignoutPage.class) {
			private static final long serialVersionUID = -3491536600221055012L;

			@Override
			public boolean isVisible() {
				return ((JPlopSession)getSession()).isSignedIn();
			}});
	}
}
