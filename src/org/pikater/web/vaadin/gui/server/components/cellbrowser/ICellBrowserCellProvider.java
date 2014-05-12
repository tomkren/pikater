package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import org.pikater.shared.util.CustomOrderSet;

import com.vaadin.ui.AbstractComponent;

public interface ICellBrowserCellProvider
{
	CustomOrderSet<CellBrowserCellSource> getSourceObjects();
	AbstractComponent getComponentForSource(CellBrowserCellSource source);
}
