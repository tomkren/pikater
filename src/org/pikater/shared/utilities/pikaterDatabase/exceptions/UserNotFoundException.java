package org.pikater.shared.utilities.pikaterDatabase.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -2100395902905714861L;
	
	private int id=-1;
	private String login=null;
	
	public UserNotFoundException(){
		super();
	}
	
	public UserNotFoundException(int id){
		this.id=id;
	}
	
	public UserNotFoundException(String login){
		this.login=login;
	}

	@Override
	public String getMessage() {
		if(id!=-1){
			return "User with the ID = "+id+" is not found in the database.";
		}else if(login!=null){
			return "User with the Login = "+login+" is not found in the database.";
		}else{
			return super.getMessage();
		}
	}

	
}
