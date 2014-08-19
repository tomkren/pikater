package org.pikater.web.vaadin.gui.server.components.dbviews;

import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.batches.AbstractBatchTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractDBViewRoot;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TextField;

public class BatchDBViewRoot<V extends AbstractBatchTableDBView> extends AbstractDBViewRoot<V>
{
	public BatchDBViewRoot(V view)
	{
		super(view);
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		AbstractBatchTableDBView.Column specificColumn = (AbstractBatchTableDBView.Column) column;
		switch(specificColumn)
		{
			case FINISHED:
			case CREATED:
				return 75;
			
			case MAX_PRIORITY:
			case STATUS:
				return 100;
				
			case OWNER:
				return 100;
			case NAME:
				return 125;
			case NOTE:
				return 150;
			case ABORT:
				return 75;
			case RESULTS:
				return 100;
				
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
	
	@Override
	public ITableColumn getExpandColumn()
	{
		return AbstractBatchTableDBView.Column.NOTE;
	}
	
	@Override
	public void onCellCreate(ITableColumn column, AbstractComponent component)
	{
		AbstractBatchTableDBView.Column specificColumn = (AbstractBatchTableDBView.Column) column;
		if(specificColumn == AbstractBatchTableDBView.Column.NOTE)
		{
			TextField tf_value = (TextField) component;
			tf_value.setDescription(tf_value.getValue());
		}
	}

	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
	{
		// TODO Auto-generated method stub
	}
}