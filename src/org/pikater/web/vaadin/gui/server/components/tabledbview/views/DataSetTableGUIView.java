package org.pikater.web.vaadin.gui.server.components.tabledbview.views;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.datasets.DataSetTableDBView;

public class DataSetTableGUIView extends AbstractTableGUIView<DataSetTableDBView>
{
	public DataSetTableGUIView(AbstractTableDBView underlyingDBView)
	{
		super(underlyingDBView);
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
				
			default:
				throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
		}
	}
}