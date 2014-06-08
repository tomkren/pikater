package org.pikater.shared.database.views.jirka.abstractview;

public class QueryConstraints
{
	private IColumn sortOrder;
	private int offset;
	private int maxResults;
	
	public QueryConstraints(IColumn sortOrder, int offset, int maxResults)
	{
		this.sortOrder = sortOrder;
		this.maxResults = maxResults;
	}

	public IColumn getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(IColumn sortOrder)
	{
		this.sortOrder = sortOrder;
	}
	
	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(int maxResults)
	{
		this.maxResults = maxResults;
	}
}
