package org.pikater.web.unused.cellbrowser.cell;

import java.io.Serializable;

public class CellBrowserCellSource implements Serializable
{
	private static final long serialVersionUID = 6526292595399305748L;
	
	public final Object source;

	public CellBrowserCellSource(Object source)
	{
		this.source = source;
	}
}
