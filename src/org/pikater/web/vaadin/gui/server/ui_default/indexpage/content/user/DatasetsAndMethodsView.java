package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;

public class DatasetsAndMethodsView extends TabSheet implements IContentComponent
{
	private static final long serialVersionUID = -1101643157774750795L;
	
	// TODO: adding datasets: ARFF, CSV, XLS
	
	private final Table table_datasets;
	private final Table table_methods;

	public DatasetsAndMethodsView()
	{
		super();
		setSizeFull();
		
		this.table_datasets = new Table();
		
		this.table_methods = new Table();
		
		
		
		addTab(this.table_datasets, "Datasets");
		addTab(this.table_methods, "Methods");
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}
}