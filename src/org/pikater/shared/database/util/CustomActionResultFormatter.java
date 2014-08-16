package org.pikater.shared.database.util;

import java.util.List;

import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;

public class CustomActionResultFormatter<T> extends ResultFormatter<T>
{
	private final EmptyResultAction era;
	
	public CustomActionResultFormatter(List<T> resultList, EmptyResultAction era)
	{
		super(resultList);
		this.era = era;
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
