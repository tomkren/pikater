package org.pikater.web.vaadin.gui.server.components.tabledbview.views;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTable;

import com.vaadin.ui.Component;

public abstract class AbstractTableGUIView<T extends AbstractTableDBView>
{
	private final AbstractTableDBView underlyingDBView;

	public AbstractTableGUIView(AbstractTableDBView underlyingDBView)
	{
		this.underlyingDBView = underlyingDBView;
	}
	
	public AbstractTableDBView getUnderlyingDBView()
	{
		return underlyingDBView;
	}
	
	public void onCellCreate(ITableColumn column, Object component)
	{
		((Component) component).setWidth("100%");
	}
	
	public void setColumnSizes(DBTable table)
	{
		for(ITableColumn column : underlyingDBView.getColumns())
		{
			table.setColumnWidth(column, getColumnSize(column));
			// TODO: table.setColumnExpandRatio(propertyId, expandRatio); // override fixed column width
		}
	}
	
	public abstract int getColumnSize(ITableColumn column);
	
	public static AbstractTableGUIView<? extends AbstractTableDBView> getInstanceFromDBView(AbstractTableDBView dbView)
	{
		if(dbView instanceof UsersTableDBView)
		{
			return new UsersTableGUIView(dbView);
		}
		else if(dbView instanceof DataSetTableDBView)
		{
			return new DataSetTableGUIView(dbView);
		}
		else
		{
			throw new IllegalStateException(String.format("No binding between DB and GUI views was found for DB view '%s'.", dbView.getClass().getSimpleName()));
		}
	}
}
