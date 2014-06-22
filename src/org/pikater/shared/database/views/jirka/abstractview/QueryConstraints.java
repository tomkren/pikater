package org.pikater.shared.database.views.jirka.abstractview;

public class QueryConstraints
{
	private final IColumn sortColumn;
	private final SortOrder sortOrder;
	private final int offset;
	private final int maxResults;
	
	public QueryConstraints(IColumn sortColumn, SortOrder sortOrder, int offset, int maxResults)
	{
		this.sortColumn = sortColumn;
		this.sortOrder = sortOrder;
		this.offset = offset;
		this.maxResults = maxResults;
	}

	public IColumn getSortColumn()
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