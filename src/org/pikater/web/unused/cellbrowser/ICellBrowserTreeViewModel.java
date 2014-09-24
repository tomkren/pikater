package org.pikater.web.unused.cellbrowser;

import java.io.Serializable;

public interface ICellBrowserTreeViewModel extends Serializable
{
	public ICellBrowserCellProvider getChildInfoForSource(final Object source);
	public boolean isValueInLeafColumn(Object value);
}
