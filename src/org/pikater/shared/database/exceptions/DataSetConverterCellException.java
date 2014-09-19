package org.pikater.shared.database.exceptions;

import java.text.MessageFormat;

public class DataSetConverterCellException extends DataSetConverterException {
	
	private static final long serialVersionUID = -8479278010017464622L;
	
	private final int row;
	private final int column;
	
	public DataSetConverterCellException(String message,int row,int column){
		super(message);
		
		this.row=row;
		this.column=column;
	}
	
	@Override
	public String getMessage()
	{
		return MessageFormat.format("{0} Position [row= {1},column= {2}]: {3}", this.getClass().getName(),this.row,this.column, super.getMessage());
	}
}
