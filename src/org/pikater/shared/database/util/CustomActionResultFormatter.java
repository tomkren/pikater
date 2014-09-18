package org.pikater.shared.database.util;

import java.util.List;

public class CustomActionResultFormatter<T> extends ResultFormatter<T>
{
		
	public CustomActionResultFormatter(List<T> resultList)
	{
		super(resultList);
	}
}