package org.pikater.web.unused.cellbrowser;

import java.io.Serializable;

import org.pikater.shared.util.collections.CustomOrderSet;
import org.pikater.web.unused.cellbrowser.cell.CellBrowserCellSource;

import com.vaadin.ui.AbstractComponent;

public interface ICellBrowserCellProvider extends Serializable
{
	CustomOrderSet<CellBrowserCellSource> getSourceObjects();
	AbstractComponent getComponentForSource(CellBrowserCellSource source);
}
