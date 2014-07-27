package org.pikater.core.ontology.subtrees.account;

import java.util.Date;

import jade.content.Concept;

public class User implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9056359798650843276L;

	private int id;
	private String login;
	private int priorityMax;
	private String email;
	private Date created;
	private Date lastLogin;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getPriorityMax() {
		return priorityMax;
	}

	public void setPriorityMax(int priorityMax) {
		if (priorityMax < 0 || 9 < priorityMax) {
			throw new IllegalArgumentException(
					"Argument priorityMax have to be in the interval <0,9>");
		}
		this.priorityMax = priorityMax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

}
