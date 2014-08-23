package org.pikater.web.vaadin.gui.server.components.dbviews.special;

import java.util.List;

import org.pikater.shared.database.views.tableview.datasets.DatasetPickingTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.DatasetDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.DialogCommons.IDialogResultPreparer;

import com.vaadin.ui.Label;

public class DatasetPickerComponent extends DBTableLayout implements IDialogResultPreparer
{
	private static final long serialVersionUID = 9055067769093710286L;

	public DatasetPickerComponent(String caption)
	{
		setSizeUndefined();
		setReadOnly(true);
		getTable().setMultiSelect(false);
		setView(new DatasetDBViewRoot<DatasetPickingTableDBView>(new DatasetPickingTableDBView()));
		addComponentAsFirst(new Label(caption));
	}
	
	@Override
	public boolean isResultReadyToBeHandled()
	{
		return getTable().isARowSelected();
	}

	@Override
	public void addArgs(List<Object> arguments)
	{
		arguments.add(getTable().getViewsOfSelectedRows()[0]);
	}
}