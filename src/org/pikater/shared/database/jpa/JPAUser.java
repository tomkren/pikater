package org.pikater.shared.database.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class representing a data entry of the table containing information about users.
 */
@Entity
public class JPAUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(unique=true)
	private String login;
	@Column(nullable=false)
	private String password;
	private int priorityMax;
	private String email;
	private JPARole role;
	
	
	public int getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPriorityMax() {
		return priorityMax;
	}
	public void setPriorityMax(int priorityMax) {
		this.priorityMax = priorityMax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public JPARole getRole() {
		return role;
	}
	public void setRole(JPARole role) {
		this.role = role;
	}
	
}
