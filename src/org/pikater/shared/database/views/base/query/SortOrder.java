package org.pikater.shared.database.views.base.query;

public enum SortOrder
{
	ASCENDING,
	DESCENDING;
	
	public SortOrder invert()
	{
		if(this == ASCENDING)
		{
			return DESCENDING;
		}
		else
		{
			return ASCENDING;
		}
	}
}