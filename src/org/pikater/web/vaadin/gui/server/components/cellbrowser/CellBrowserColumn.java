package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import com.vaadin.ui.VerticalLayout;

public class CellBrowserColumn extends VerticalLayout
{
	private static final long serialVersionUID = -4046745874718664908L;
	
	private final int level;

	public CellBrowserColumn(int level)
	{
		super();
		
		this.level = level;
		addStyleName("cellbrowser-column");
	}
	
	public int getCellBrowserLevel()
	{
		return level;
	}
	
	public void setComponents(CellBrowserCellProvider cellProvider)
	{
		setImmediate(false);
		removeAllComponents();
		addComponents(cellProvider.getChildCells());
		setImmediate(true);
	}
}
