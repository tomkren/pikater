package org.pikater.shared.database.exceptions;

public class DataSetConverterException extends Exception {
	private static final long serialVersionUID = 1294904974363350993L;
	String msg;
	
	public DataSetConverterException(String message){
		this.msg=message;
	}
	
	public String getMessage(){
		return this.getClass().getName() +" " + msg;	
	}
}
