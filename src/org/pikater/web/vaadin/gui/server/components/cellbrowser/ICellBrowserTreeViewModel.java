package org.pikater.web.vaadin.gui.server.components.cellbrowser;

public interface ICellBrowserTreeViewModel
{
	public ICellBrowserCellProvider getChildInfoForSource(final Object source);
	public boolean isValueInLeafColumn(Object value);
}
