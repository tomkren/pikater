package org.pikater.web.vaadin.gui.server.components.dbviews;

import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.batches.experiments.ExperimentTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractDBViewRoot;

import com.vaadin.ui.AbstractComponent;

public class ExperimentDBViewRoot extends AbstractDBViewRoot<ExperimentTableDBView>
{
	public ExperimentDBViewRoot(ExperimentTableDBView view)
	{
		super(view);
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		ExperimentTableDBView.Column specificColumn = (ExperimentTableDBView.Column) column;
		switch(specificColumn)
		{
			case STATUS:
				return 100;
			
			case STARTED:
			case FINISHED:
				return 75;
			
			case MODEL_STRATEGY:
			case BEST_MODEL:
			case RESULTS:
				return 100;
			
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
	}

	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
	{
		ExperimentTableDBView.Column specificColumn = (ExperimentTableDBView.Column) column;
		if(specificColumn == ExperimentTableDBView.Column.BEST_MODEL)
		{
			// TODO: talk with Peter about this
		}
		else if(specificColumn == ExperimentTableDBView.Column.RESULTS)
		{
			// TODO: wait for Peter to confirm this
		}
		else
		{
			throw new IllegalStateException(String.format("Action '%s' has to be approved before being executed", specificColumn.name())); 
		}
	}
}