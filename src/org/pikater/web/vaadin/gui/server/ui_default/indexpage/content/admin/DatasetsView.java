package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.views.jirka.datasets.DataSetTableDBView;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class DatasetsView extends DBTableLayout implements IContentComponent
{
	private static final long serialVersionUID = -5934021726787719602L;

	public DatasetsView()
	{
		super(new DataSetTableDBView(null), true);
		setSizeUndefined();
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