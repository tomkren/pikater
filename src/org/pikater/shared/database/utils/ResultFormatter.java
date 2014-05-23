package org.pikater.shared.database.utils;

import java.util.List;

import org.pikater.shared.database.exceptions.NoResultException;

public class ResultFormatter<T> {
	
	private List<T> resList;
	
	/**
	 * Creates an instance of ResultFormatter for the given list of results
	 * @param resultList
	 */
	public ResultFormatter(List<T> resultList){
		this.resList=resultList;
	}
	
	/**
	 * Returns the first item from the result list. If no items are in the result
	 * NoResultException exception is thrown.
	 * @return the first result
	 * @throws NoResultException
	 */
	public T getSingleResult() throws NoResultException{
		if(resList.size()>0){
			return resList.get(0);
		}else{
			throw new NoResultException();
		}
	}
	
	/**
	 * Returns the first item from the result list. If no items are in the result
	 * null is returned.
	 * @return the first result or null
	 */
	public T getSingleResultWithNull(){
		if(resList.size()>0){
			return resList.get(0);
		}else{
			return null;
		}
	}
	
}
