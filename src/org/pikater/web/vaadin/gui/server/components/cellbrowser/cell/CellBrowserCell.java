package org.pikater.web.vaadin.gui.server.components.cellbrowser.cell;

import org.pikater.web.vaadin.MyResources;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;

public class CellBrowserCell extends Panel
{
	private static final long serialVersionUID = 694535234983389969L;
	
	private final CellBrowserCellSource thisCellsSource;
	private final CellBrowserCellExtension extension;
	private final CustomLayout innerLayout;
	
	public CellBrowserCell(AbstractComponent cellComponent, CellBrowserCellSource thisCellsSource, ClickListener clickListener)
	{
		super();
		setStyleName("cellbrowser-cell");
		
		this.extension = new CellBrowserCellExtension("cellbrowser-cell-inner-layout-table-selected");
		this.thisCellsSource = thisCellsSource;
		
		cellComponent.addStyleName("cellbrowser-cell-component");
		Image nextIconComponent = new Image(null, MyResources.img_nextIcon16);
		nextIconComponent.setStyleName("cellbrowser-cell-image");
		
		innerLayout = new CustomLayout("cellBrowserCellLayout");
		innerLayout.addStyleName("cellbrowser-cell-inner-layout");
		innerLayout.addComponent(cellComponent, "component");
		innerLayout.addComponent(nextIconComponent, "image");
		innerLayout.setData(thisCellsSource.source); // this is solely for DnD to work
		
		this.extension.extend(innerLayout); // this is required for selection to work
		
		addClickListener(clickListener);
		setContent(innerLayout);
	}
	
	public CellBrowserCellSource getSourceObject()
	{
		return thisCellsSource;
	}
	
	public void select()
	{
		this.extension.select();
	}
	
	public void deselect()
	{
		this.extension.deselect();
	}
	
	public void setLeaf()
	{
		innerLayout.removeComponent("image");
	}
}
