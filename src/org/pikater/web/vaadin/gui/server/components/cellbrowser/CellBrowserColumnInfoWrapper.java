package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import java.io.Serializable;

public class CellBrowserColumnInfoWrapper implements Serializable
{
	private static final long serialVersionUID = 7562000322908861476L;
	
	public final CellBrowserColumn column;
	private CellBrowserCell selectedCell;

	public CellBrowserColumnInfoWrapper(CellBrowserColumn column)
	{
		this.column = column;
		this.selectedCell = null;
	}
	
	public CellBrowserCell getSelectedCell()
	{
		return selectedCell; 
	}
	
	public boolean isACellSelected()
	{
		return getSelectedCell() != null;
	}
	
	public void cellSelected(CellBrowserCell selectedCell)
	{
		cellUnselected();
		this.selectedCell = selectedCell;
		this.selectedCell.select();
	}
	
	public void cellUnselected()
	{
		if(this.selectedCell != null)
		{
			this.selectedCell.deselect();
		}
		selectedCell = null; 
	}
}
