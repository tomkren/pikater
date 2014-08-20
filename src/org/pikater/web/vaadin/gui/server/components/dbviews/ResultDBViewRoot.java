package org.pikater.web.vaadin.gui.server.components.dbviews;

import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractDBViewRoot;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TextField;

public class ResultDBViewRoot extends AbstractDBViewRoot<ResultTableDBView>
{
	public ResultDBViewRoot(ResultTableDBView view)
	{
		super(view);
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		switch(specificColumn)
		{
			case AGENT_NAME:
			case ERROR_RATE:
			case KAPPA:
			case REL_ABS_ERR:
			case MEAN_ABS_ERR:
				return 100;
				
			case ROOT_REL_SQR_ERR:
			case TRAINED_MODEL:
			case EXPORT:
				return 115;
				
			case WEKA_OPTIONS:
			case NOTE:
			case ROOT_MEAN_SQR_ERR:
				return 125;
			
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
	
	@Override
	public ITableColumn getExpandColumn()
	{
		return null;
	}
	
	@Override
	public void onCellCreate(ITableColumn column, AbstractComponent component)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		if(specificColumn == ResultTableDBView.Column.NOTE)
		{
			TextField tf_value = (TextField) component;
			tf_value.setDescription(tf_value.getValue());				
		}
	}

	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
	{
	}
}