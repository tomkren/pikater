package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.AbstractComponent;

public class CellBrowserCellProvider implements Serializable
{
	private static final long serialVersionUID = -902047803893738318L;
	
	private final Collection<CellBrowserCell> columnComponents;
	
	public CellBrowserCellProvider(ICellBrowserCellProvider componentProvider, ClickListener sharedCellClickListener)
	{
		this.columnComponents = new ArrayList<CellBrowserCell>();
		for(CellBrowserCellSource currentSource : componentProvider.getSourceObjects())
		{
			AbstractComponent component = componentProvider.getComponentForSource(currentSource);
			columnComponents.add(new CellBrowserCell(component, currentSource, sharedCellClickListener));
		}
	}
	
	public Collection<CellBrowserCell> getCells()
	{
		return columnComponents;
	}
}
