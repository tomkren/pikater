package org.pikater.shared.database.views.tableview.batches.experiments;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;

/**
 * A generic view for tables displaying experiment information.  
 */
public class ExperimentTableDBView extends AbstractTableDBView
{
	private final JPAUser owner;
	private JPABatch batch;
	
	/**  
	 * @param user The user whose experiments to display. If null (admin mode), all datasets should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method.
	 * The owner of the batch is compared to the given user.
	 * @param batch The batch for which the experiments are listed
	 */
	public ExperimentTableDBView(JPAUser user,JPABatch batch)
	{
		this.owner = user;
		this.batch = batch;
	}
	
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		STATUS,
		STARTED,
		FINISHED,
		MODEL_STRATEGY,
		RESULTS,
		BEST_MODEL;

		@Override
		public String getDisplayName()
		{
			return this.name();
		}

		@Override
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case STARTED:
				case FINISHED:
				case STATUS:
				case MODEL_STRATEGY:
					return DBViewValueType.STRING;
					
				case BEST_MODEL:
				case RESULTS:
					return DBViewValueType.NAMED_ACTION;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public Set<ITableColumn> getAllColumns()
	{
		return new LinkedHashSet<ITableColumn>(EnumSet.allOf(Column.class));
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		return getAllColumns();
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.STATUS;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPAExperiment> allExperiments;
		if(owner==null)
		{
			allExperiments=batch.getExperiments();
		}
		else
		{
			if(owner.getId() == batch.getOwner().getId())
			{
				allExperiments=batch.getExperiments();
			}
			else
			{
				allExperiments=new ArrayList<JPAExperiment>();
			}
		}
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allExperiments.size());
		List<ExperimentTableDBRow> resultRows = new ArrayList<ExperimentTableDBRow>();
		for(JPAExperiment experiment : allExperiments.subList(constraints.getOffset(), endIndex))
		{
			resultRows.add(new ExperimentTableDBRow(experiment));
		}
		return new QueryResult(resultRows, allExperiments.size());
	}
}