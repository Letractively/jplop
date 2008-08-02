package tifauv.jplop.web;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authentication.pages.SignInPage;
import org.apache.wicket.markup.html.WebPage;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;

import tifauv.jplop.web.components.about.AboutPage;
import tifauv.jplop.web.components.account.AccountPage;
import tifauv.jplop.web.components.board.BoardPage;
import tifauv.jplop.web.components.config.ConfigPage;
import tifauv.jplop.web.components.logon.LogonPage;
import tifauv.jplop.web.components.logout.LogoutPage;
import tifauv.jplop.web.components.register.RegisterPage;


/**
 * This is JPlop's web app main class.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class JPlopApplication extends AuthenticatedWebApplication {

	// METHODS \\
	/**
	 * Initializes the application.
	 */
	@Override
	protected void init() {
		super.init();
		
		// Manage @EJB injection
		addComponentInstantiationListener(new JavaEEComponentInjector(this));
		
		// Remove Wicket-specific markup
		getMarkupSettings().setStripWicketTags(true);
		
		// Pretty URLs
		mountBookmarkablePage("/about",    AboutPage.class);
		mountBookmarkablePage("/account",  AccountPage.class);
		mountBookmarkablePage("/board",    BoardPage.class);
		mountBookmarkablePage("/config",   ConfigPage.class);
		mountBookmarkablePage("/signup",   RegisterPage.class);
		mountBookmarkablePage("/signin",   LogonPage.class);
		mountBookmarkablePage("/signout",  LogoutPage.class);
	}
	
	
	/**
	 * Gives the Board page as home page.
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return BoardPage.class;
	}
	
	
	/**
	 * Gives the Logon page class.
	 */
	@Override
	public Class<? extends SignInPage> getSignInPageClass() {
		return LogonPage.class;
	}
	
	
	/**
	 * Gives the session class.
	 */
	@Override
	public Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return JPlopSession.class;
	}
	
	
	/**
	 * Builds a new session.
	 */
	@Override
	public Session newSession(Request request, Response response) {
	   return new JPlopSession(request);
	}
}
