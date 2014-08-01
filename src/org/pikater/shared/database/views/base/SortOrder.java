package org.pikater.shared.database.views.base;

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