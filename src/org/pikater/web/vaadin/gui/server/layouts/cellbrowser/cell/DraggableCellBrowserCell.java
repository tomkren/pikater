package org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell;

import com.vaadin.ui.DragAndDropWrapper;

public class DraggableCellBrowserCell extends DragAndDropWrapper
{
	private static final long serialVersionUID = 3120427201110800928L;
	
	private final CellBrowserCell cellToWrap;
	
	public DraggableCellBrowserCell(CellBrowserCell cellToWrap)
	{
		super(cellToWrap);
		setSizeUndefined();
		setStyleName("cellbrowser-cell-container");
		
		this.cellToWrap = cellToWrap;
		
		enableDnD();
	}
	
	public void enableDnD()
	{
		setDragStartMode(DragStartMode.WRAPPER);
	}
	
	public void disableDnD()
	{
		setDragStartMode(DragStartMode.NONE);
	}
	
	public CellBrowserCell getWrappedCell()
	{
		return cellToWrap;
	}
}
