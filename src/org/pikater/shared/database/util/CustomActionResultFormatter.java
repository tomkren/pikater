package org.pikater.shared.database.util;

import java.util.List;

import org.pikater.shared.database.exceptions.NoResultException;

public class CustomActionResultFormatter<T> extends ResultFormatter<T>
{
		
	public CustomActionResultFormatter(List<T> resultList)
	{
		super(resultList);
	}
	
	@Override
	public T getSingleResult() throws NoResultException
	{
		// TODO Auto-generated method stub
		return super.getSingleResult();
	}
	
	@Override
	public T getSingleResultWithNull()
	{
		// TODO Auto-generated method stub
		return super.getSingleResultWithNull();
	}
}
