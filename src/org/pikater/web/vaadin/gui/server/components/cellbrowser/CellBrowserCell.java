package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import org.pikater.web.vaadin.MyResources;
import org.pikater.web.vaadin.gui.server.CellBrowserCellExtension;

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
		setStyleName("cellbrowser-column-cell");
		
		this.extension = new CellBrowserCellExtension("cellbrowser-column-cell-inner-layout-table-selected");
		this.thisCellsSource = thisCellsSource;
		
		cellComponent.addStyleName("cellbrowser-column-cell-component");
		Image nextIconComponent = new Image(null, MyResources.img_nextIcon16);
		nextIconComponent.setStyleName("cellbrowser-column-cell-image");
		
		innerLayout = new CustomLayout("cellBrowserColumnCellLayout");
		innerLayout.addStyleName("cellbrowser-column-cell-inner-layout");
		innerLayout.addComponent(cellComponent, "component");
		innerLayout.addComponent(nextIconComponent, "image");
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
