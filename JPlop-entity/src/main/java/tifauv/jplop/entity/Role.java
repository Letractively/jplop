/**
 * 5 juil. 2008
 */
package tifauv.jplop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import tifauv.jplop.exceptions.ValidationException;

/**
 * This is a user role, used for fine-grained application security.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Entity
public class Role implements Serializable {
	
	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = 3114755699858789917L;

	
	// FIELDS \\
	/** The role identifier. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int m_id;
	
	/** The user login. */
	@Column(name="login", nullable=false)
	private String m_login;
	
	/** The role name. */
	@Column(name="role", nullable=false)
	private String m_role;
	
	
	// GETTERS \\
	/**
	 * Gives the role identifier.
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
	 * Gives the role name.
	 */
	public String getRole() {
		return m_role;
	}

	
	// SETTERS \\
	/**
	 * Sets the user login.
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}

	
	/**
	 * Sets the role name.
	 */
	public void setRole(String p_role) {
		m_role = p_role;
	}
	
	
	// METHODS \\
	/**
	 * Validates the data before storing the entity.
	 * We just check that the name is not null or empty.
	 */
	@PrePersist
	@PreUpdate
	protected void validateData() {
		if (getLogin() == null || getLogin().trim().isEmpty())
			throw new ValidationException("The user login cannot be null or empty");

		if (getRole() == null || getRole().trim().isEmpty())
			throw new ValidationException("The role name cannot be null or empty");
	}

	
	/**
	 * A Role is equal to an object o if and only if
	 * o is another non-null Role with the same id.
	 */
	@Override
	public boolean equals(Object p_object) {
		return p_object != null
				&& p_object instanceof Role
				&& getId() == ((Role)p_object).getId();
	}
	
	
	/**
	 * Returns the following string "<role>:<login>".
	 */
	@Override
	public String toString() {
		return getRole() + ':' + getLogin();
	}
	
	
	/**
	 * The hashcode is the role's identifier.
	 */
	@Override
	public int hashCode() {
		return getId();
	}
}
