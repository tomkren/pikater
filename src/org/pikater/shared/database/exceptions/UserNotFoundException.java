package org.pikater.shared.database.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -2100395902905714861L;
	
	private final String login;
	
	public UserNotFoundException(String login)
	{
		this.login=login;
	}

	@Override
	public String getMessage()
	{
		return "User with the Login = "+login+" is not found in the database.";
	}
}