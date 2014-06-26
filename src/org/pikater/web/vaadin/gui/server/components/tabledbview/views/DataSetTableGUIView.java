package org.pikater.web.vaadin.gui.server.components.tabledbview.views;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView.Column;

public class DataSetTableGUIView extends AbstractTableGUIView<DataSetTableDBView>
{
	public DataSetTableGUIView(AbstractTableDBView underlyingDBView)
	{
		super(underlyingDBView);
	}
	
	@Override
	public void onCellCreate(ITableColumn column, Object component)
	{
		super.onCellCreate(column, component);
		
		DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
		if(specificColumn == Column.DESCRIPTION)
		{
			// TODO: set description to the label somehow to display it whole?
		}
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
		switch(specificColumn)
		{
			case OWNER:
				return 125;
			
			case DEFAULT_TASK_TYPE:
				return 150;
				
			case NUMBER_OF_INSTANCES:
				return 75;
			
			case CREATED:
				return 100;
			
			case SIZE:
				return 75;
				
			case DESCRIPTION:
				return 300;
				
			case APPROVE:
			case DELETE:
				return 100;
				
			default:
				throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
		}
	}
}