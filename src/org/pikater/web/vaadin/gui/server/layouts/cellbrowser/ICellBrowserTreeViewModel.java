package org.pikater.web.vaadin.gui.server.layouts.cellbrowser;

import java.io.Serializable;

public interface ICellBrowserTreeViewModel extends Serializable
{
	public ICellBrowserCellProvider getChildInfoForSource(final Object source);
	public boolean isValueInLeafColumn(Object value);
}
