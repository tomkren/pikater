package org.pikater.shared.database.jpa;

public enum BatchStatus {
	CREATED ,
	WAITING ,
	STARTED ,
	FINISHED,
	COMPUTING,
	FAILED
}
