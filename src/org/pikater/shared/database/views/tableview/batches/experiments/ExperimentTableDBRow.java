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
			// TODO: Implement best model retrieval
			// DAOs.resultDAO.getByExperimentBestResult(experiment).getCreatedModel();
			return new NamedActionDBViewValue("Download") // no DB changes needed - this is completely GUI managed
			{	
				@Override
				public boolean isEnabled()
				{
					// no DB changes needed - this is completely GUI managed
					return (experiment.getResults() != null) && !experiment.getResults().isEmpty();
				}

				@Override
				protected void updateEntities()
				{
				}

				@Override
				protected void commitEntities()
				{
				}
			};
		case RESULTS:
			return new NamedActionDBViewValue("Download") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled()
				{
					return (experiment.getResults() != null) && !experiment.getResults().isEmpty();
				}
				
				@Override
				protected void commitEntities()
				{
				}
				
				@Override
				protected void updateEntities()
				{
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
