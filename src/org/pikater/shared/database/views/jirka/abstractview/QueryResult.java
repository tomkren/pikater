package org.pikater.shared.database.views.jirka.abstractview;

import java.util.Collection;

public class QueryResult
{
	private final Collection<? extends AbstractTableRowDBView> constrainedResults;
	private final int allResultsCount;
	
	/**
	 * One and only way to use this class.
	 * @param constrainedResults The rows displayed in the result table. Make sure the number items doesn't
	 * exceed the maximum number of items given by query constraints.
	 * @param allResultsCount The total number of related results found. This value is used to determine the
	 * number of pages for the result table.
	 */
	public QueryResult(Collection<? extends AbstractTableRowDBView> constrainedResults, int allResultsCount)
	{
		this.constrainedResults = constrainedResults;
		this.allResultsCount = allResultsCount;
	}

	public Collection<? extends AbstractTableRowDBView> getConstrainedResults()
	{
		return constrainedResults;
	}

	public int getAllResultsCount()
	{
		return allResultsCount;
	}
}
