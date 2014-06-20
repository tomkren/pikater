package org.pikater.shared.database.views.jirka.abstractview;

public class QueryConstraints
{
	private final IColumn sortOrder;
	private final int offset;
	private final int maxResults;
	
	public QueryConstraints(IColumn sortOrder, int offset, int maxResults)
	{
		this.sortOrder = sortOrder;
		this.offset = offset;
		this.maxResults = maxResults;
	}

	public IColumn getSortOrder()
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
