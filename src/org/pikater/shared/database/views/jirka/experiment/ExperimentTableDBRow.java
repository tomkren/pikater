package org.pikater.shared.database.views.jirka.experiment;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;
import org.pikater.shared.util.DateUtils;

public class ExperimentTableDBRow extends AbstractTableRowDBView {

	private JPAExperiment experiment=null;

	public ExperimentTableDBRow(JPAExperiment experiment)
	{
		this.experiment=experiment;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		ExperimentTableDBView.Column specificColumn = (ExperimentTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case CREATED:
			return new StringDBViewValue(DateUtils.toCzechDate(experiment.getCreated().getTime()), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case STARTED:
			return new StringDBViewValue(DateUtils.toCzechDate(experiment.getStarted().getTime()), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case FINISHED:
			return new StringDBViewValue(DateUtils.toCzechDate(experiment.getFinished().getTime()), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case OWNER:
			return new StringDBViewValue(experiment.getBatch()==null?"error":experiment.getBatch().getOwner().getLogin(), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case STATUS:
			return new StringDBViewValue(experiment.getStatus().name(), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case MODEL_STRATEGY:
			return new StringDBViewValue(experiment.getModelStrategy().name(), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case MODEL:
			//TODO: Implement best model retrieval
			return new NamedActionDBViewValue("Get Best Model") {	

				@Override
				public boolean isEnabled()
				{
					return true;
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
			if((experiment.getResults()==null) || experiment.getResults().isEmpty()){
				return new NamedActionDBViewValue("No Results") {
					
					@Override
					public boolean isEnabled()
					{
						return true;
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
			}else{
				return new NamedActionDBViewValue("Show Results") {	

					@Override
					public boolean isEnabled()
					{
						return true;
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
			}
		
		
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
