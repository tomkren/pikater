package org.pikater.web.vaadin.gui.server.components.dbviews.base;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

import com.vaadin.ui.AbstractComponent;

public abstract class AbstractDBViewRoot<T extends AbstractTableDBView>
{
	private final T underlyingDBView;
	
	public AbstractDBViewRoot(T underlyingDBView)
	{
		this.underlyingDBView = underlyingDBView;
	}
	
	public T getUnderlyingDBView()
	{
		return underlyingDBView;
	}
	
	public abstract int getColumnSize(ITableColumn column);
	public abstract ITableColumn getExpandColumn();
	public abstract void onCellCreate(ITableColumn column, AbstractComponent component);
	public abstract void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action);
}