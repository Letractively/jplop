/**
 * 5 juil. 2008
 */
package tifauv.jplop.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import tifauv.jplop.exceptions.ValidationException;

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
	@Embedded
	private Password m_password;
   
	/** The user roles. */
	@OneToMany
	private Set<Role> m_roles;
	
	/** The user's posts. */
	@OneToMany(mappedBy="m_author")
	@JoinColumn(name="account_id")
	private List<Post> m_posts;
	
	
	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public Account() {
		m_password = new Password();
	}
	
	
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
	public Password getPassword() {
		return m_password;
	}
	
	
	/**
	 * Tells whether the user has the given role.
	 * 
	 * @param p_role
	 *            the role 
	 * 
	 * @return <tt>true</tt> iff the user has the given role
	 */
	public boolean hasRole(Role p_role) {
		return m_roles.contains(p_role);
	}
	
	
	/**
	 * Gives all the user's posts.
	 */
	public List<Post> getPosts() {
		return m_posts;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the user's login.
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}
	
	
	public void setPassword(String p_password) {
		if (m_password == null)
			m_password = new Password();
		m_password.hashPassword(p_password);
	}
	
	/**
	 * Sets the user's password.
	 */
	public void setPassword(Password p_password) {
		m_password = p_password;
	}
	
	
	/**
	 * Adds a role to the user.
	 * 
	 * @param p_role
	 *            the role to add
	 */
	public void addRole(Role p_role) {
		m_roles.add(p_role);
	}
	
	
	/**
	 * Removes a role from the user.
	 * 
	 * @param p_role
	 *            the role to remove
	 */
	public void removeRole(Role p_role) {
		m_roles.remove(p_role);
	}
	
	
	// METHODS \\
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
