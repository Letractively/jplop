package tifauv.jplop.auth;

import javax.security.auth.Subject;

public interface Authenticator {

	public Subject getSubject();
	public boolean authenticate();
}
