package org.pikater.shared.database.views.base.query;

import org.pikater.shared.database.views.base.ITableColumn;

public class QueryConstraints
{
	private final ITableColumn sortColumn;
	private final SortOrder sortOrder;
	private final int offset;
	private final int maxResults;
	
	public QueryConstraints(ITableColumn sortColumn, SortOrder sortOrder, int offset, int maxResults)
	{
		this.sortColumn = sortColumn;
		this.sortOrder = sortOrder;
		this.offset = offset;
		this.maxResults = maxResults;
	}

	public ITableColumn getSortColumn()
	{
		return sortColumn;
	}

	public SortOrder getSortOrder()
	{
		return sortOrder;
	}

	public int getOffset()
	{
		return offset;
	}

	public int getMaxResults()
	{
		return maxResults;
	}
}