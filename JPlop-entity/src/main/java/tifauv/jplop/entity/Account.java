/**
 * 5 juil. 2008
 */
package tifauv.jplop.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import tifauv.jplop.exceptions.ValidationException;

/**
 * These are the queries used to manage the Account entities.
 */
@NamedQueries({
	
	/**
	 * Finds an account by the user's login.
	 * 
	 * @param login
	 *            the user login
	 * 
	 * @return the account (or <tt>null</tt>)
	 */
	@NamedQuery (
			name="findAccountByLogin",
			query="SELECT a FROM Account a WHERE a.m_login = :login"
	)
})


/**
 * This is a user account.
 * An account has a unique identifier (automatically generated),
 * a login (that can be changed) and roles.
 * The roles tell what a user can do within the application.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Entity
public class Account implements Serializable {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = 4191199079107454445L;

	
	// FIELDS \\
	/** The user unique identifier (an integer). */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int m_id;
	
	/** The user login. */
	@Column(name="login", nullable=false)
	private String m_login;
	
	/** The user password. */
	@Column(name="password", nullable=false)
	private String m_password;
	
	/** The user's posts. */
	@OneToMany(mappedBy="m_author")
	@JoinColumn(name="account_id")
	private List<Post> m_posts;
	
	/** The user's roles. */
	@OneToMany
	private Set<Role> m_roles;
	
	
	// GETTERS \\
	/**
	 * Gives the user unique identifier. 
	 */
	public int getId() {
		return m_id;
	}
	
	
	/**
	 * Gives the user login.
	 */
	public String getLogin() {
		return m_login;
	}
	
	
	/**
	 * Gives the user password.
	 */
	public String getPassword() {
		return m_password;
	}
	
	
	/**
	 * Gives all the user's posts.
	 */
	public List<Post> getPosts() {
		return m_posts;
	}
	
	
	/**
	 * Gives all the user's roles.
	 */
	public Set<Role> getRoles() {
		return m_roles;
	}
	
	
	/**
	 * Tells whether the user has the given role.
	 * 
	 * @param p_role
	 *            the role to check
	 * 
	 * @return <tt>true</tt> iff the user has the given role
	 */
	public boolean hasRole(Role p_role) {
		return getRoles().contains(p_role);
	}
	
	
	// SETTERS \\
	/**
	 * Sets the user's login.
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}
	
	
	/**
	 * Sets the user's password.
	 */
	public void setPassword(String p_password) {
		m_password = p_password;
	}
	
	
	// METHODS \\
	/**
	 * Adds a role to the user.
	 * 
	 * @param p_role
	 *            the role to add
	 */
	public void addRole(Role p_role) {
		getRoles().add(p_role);
	}
	
	
	/**
	 * Removes a role from the user.
	 * 
	 * @param p_role
	 *            the role to remove
	 */
	public void removeRole(Role p_role) {
		getRoles().remove(p_role);
	}
	
	
	/**
	 * Validates the data before storing the entity.
	 * We just check that the login is not null or empty.
	 */
	@PrePersist
	@PreUpdate
	protected void validateData() {
		if (getLogin() == null || getLogin().trim().isEmpty())
			throw new ValidationException("The user login cannot be null or empty");

		if (getPassword() == null)
			throw new ValidationException("The user password cannot be null");
	}

	
	/**
	 * A User is equal to an object o if and only if
	 * o is another non-null User with the same id.
	 */
	@Override
	public boolean equals(Object p_object) {
		return p_object != null
				&& p_object instanceof Account
				&& getId() == ((Account)p_object).getId();
	}
	
	
	/**
	 * Returns the following string "<login>".
	 */
	@Override
	public String toString() {
		return getLogin();
	}
	
	
	/**
	 * The hashcode is the user's identifier.
	 */
	@Override
	public int hashCode() {
		return getId();
	}
}
