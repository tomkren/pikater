package org.pikater.shared.database.utils.exception;

public class DataSetConverterCellException extends DataSetConverterException {
	
	private static final long serialVersionUID = -8479278010017464622L;
	int row;
	int column;
	
	public DataSetConverterCellException(String message,int row,int column){
		super(message);
		this.row=row;
		this.column=column;
	}
	
	public String getMessage(){
		return String.format("{0} Position [row= {1},column= {2}]: {3}", this.getClass().getName(),this.row,this.column,this.msg);
	}
}
