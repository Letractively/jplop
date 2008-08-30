/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tifauv.jplop.web.models;

import java.io.Serializable;

/**
 *
 * @author tifauv
 *
 * @version 1.0
 */
public class UserProfile implements Serializable  {

	// CONSTANTS \\
	/** The serialization id. */


	// FIELDS \\
	/** The email. */
	private String m_email;


	// GETTERS \\
	public String getEmail() {
		return m_email;
	}


	// SETTERS \\
	public void setEmail(String p_email) {
		m_email = p_email;
	}
}
