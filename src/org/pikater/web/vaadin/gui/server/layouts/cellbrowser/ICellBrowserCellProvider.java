package org.pikater.web.vaadin.gui.server.layouts.cellbrowser;

import java.io.Serializable;

import org.pikater.shared.util.collections.CustomOrderSet;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell.CellBrowserCellSource;

import com.vaadin.ui.AbstractComponent;

public interface ICellBrowserCellProvider extends Serializable
{
	CustomOrderSet<CellBrowserCellSource> getSourceObjects();
	AbstractComponent getComponentForSource(CellBrowserCellSource source);
}
