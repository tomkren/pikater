package org.pikater.shared.database.jpa.security;

public enum PikaterRole{
	ADMIN,
	USER;
	public String getDescription(){
		switch(this){
			case ADMIN:
				return "Administrator role";
			case USER:
				return "User role";
			default:
				return null;
		}
	}
}
