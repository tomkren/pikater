package org.pikater.web.vaadin.gui.server.components.tabledbview.views;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.datasets.DataSetTableDBView;
import org.pikater.shared.database.views.jirka.datasets.DataSetTableDBView.Column;

public class DataSetTableGUIView extends AbstractTableGUIView<DataSetTableDBView>
{
	public DataSetTableGUIView(AbstractTableDBView underlyingDBView)
	{
		super(underlyingDBView);
	}
	
	@Override
	public void onCellCreate(IColumn column, Object component)
	{
		super.onCellCreate(column, component);
		
		DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
		if(specificColumn == Column.DESCRIPTION)
		{
			// TODO: set description to the label somehow to display it whole?
		}
	}

	@Override
	public int getColumnSize(IColumn column)
	{
		DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
		switch(specificColumn)
		{
			case OWNER:
			case DEFAULT_TASK_TYPE:
			case NUMBER_OF_INSTANCES:
				return 150;
			
			case CREATED:
			case SIZE:
				return 100;
				
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