package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.pikater.web.vaadin.gui.server.components.cellbrowser.CellBrowser.CellBrowserDragSelection;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.cell.CellBrowserCell;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.cell.CellBrowserCellSource;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.cell.DraggableCellBrowserCell;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.AbstractComponent;

public class CellBrowserCellProvider implements Serializable
{
	private static final long serialVersionUID = -902047803893738318L;
	
	private final DraggableCellBrowserCell[] childCells;
	
	public CellBrowserCellProvider(ICellBrowserCellProvider componentProvider, ClickListener sharedCellClickListener, CellBrowserDragSelection dragSelection)
	{
		Collection<AbstractComponent> columnComponentsColl = new ArrayList<AbstractComponent>();
		for(CellBrowserCellSource currentSource : componentProvider.getSourceObjects())
		{
			AbstractComponent component = componentProvider.getComponentForSource(currentSource);
			CellBrowserCell cell = new CellBrowserCell(component, currentSource, sharedCellClickListener);
			columnComponentsColl.add(new DraggableCellBrowserCell(cell));
		}
		this.childCells = columnComponentsColl.toArray(new DraggableCellBrowserCell[0]);
	}
	
	public boolean noChildCellsDefined()
	{
		return childCells.length == 0;
	}
	
	public DraggableCellBrowserCell[] getChildCells()
	{
		return childCells;
	}
}
