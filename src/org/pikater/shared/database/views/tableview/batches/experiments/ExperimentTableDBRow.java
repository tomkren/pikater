package org.pikater.shared.database.views.tableview.batches.experiments;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.DateUtils;

public class ExperimentTableDBRow extends AbstractTableRowDBView {

	private JPAExperiment experiment=null;

	public ExperimentTableDBRow(JPAExperiment experiment)
	{
		this.experiment=experiment;
	}
	
	public JPAExperiment getExperiment()
	{
		return experiment;
	}
	
	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		ExperimentTableDBView.Column specificColumn = (ExperimentTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case STARTED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(experiment.getStarted()));
		case FINISHED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(experiment.getFinished()));
		case STATUS:
			return new StringReadOnlyDBViewValue(experiment.getStatus().name());
		case MODEL_STRATEGY:
			return new StringReadOnlyDBViewValue(experiment.getModelStrategy().name());
			
		/*
		 * And then custom actions.
		 */
		case BEST_MODEL:
			//TODO: Implement best model retrieval
			return new NamedActionDBViewValue("Download") {	

				@Override
				public boolean isEnabled()
				{
					return true; // TODO: is this correct?
				}

				@Override
				protected void updateEntities()
				{
					// TODO Auto-generated method stub
				}

				@Override
				protected void commitEntities()
				{
					// TODO Auto-generated method stub
				}
			};
		case RESULTS:
			return new NamedActionDBViewValue("Download")
			{
				@Override
				public boolean isEnabled()
				{
					// TODO Auto-generated method stub
					return false; // whether results are available
				}
				
				@Override
				protected void commitEntities()
				{
					// TODO Auto-generated method stub
				}
				
				@Override
				protected void updateEntities()
				{
					// TODO Auto-generated method stub
				}
			};
		
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
