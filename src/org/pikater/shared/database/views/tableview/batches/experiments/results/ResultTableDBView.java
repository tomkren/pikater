package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;

/**
 * A generic view for tables displaying result errors and statistic.  
 */
public class ResultTableDBView extends AbstractTableDBView
{
	protected final JPAUser owner;
	private JPAExperiment experiment;
	
	protected JPAExperiment getExperiment() {
		return experiment;
	}

	/**  
	 * @param user The user whose experiment results to display. If null (admin mode), all datasets should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method.
	 * The owner of the batch of experiment is compared to the given user.
	 * @param experiment The experiment for which the results are listed
	 */
	public ResultTableDBView(JPAUser user,JPAExperiment experiment)
	{
		this.owner = user;
		this.experiment = experiment;
	}
	
	protected ResultTableDBView(JPAUser user){
		this.owner=user;
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
		AGENT_NAME,
		WEKA_OPTIONS,
		ERROR_RATE,
		KAPPA,
		REL_ABS_ERR,
		MEAN_ABS_ERR,
		ROOT_REL_SQR_ERR,
		ROOT_MEAN_SQR_ERR,
		NOTE,
		TRAINED_MODEL;

		@Override
		public String getDisplayName()
		{
			switch(this)
			{
				case AGENT_NAME:
					return "AGENT";
				default:
					return name();
			}
		}

		@Override
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case AGENT_NAME:
				case WEKA_OPTIONS:
				case NOTE:
				case ERROR_RATE:
				case KAPPA:
				case MEAN_ABS_ERR:
				case REL_ABS_ERR:
				case ROOT_MEAN_SQR_ERR:
				case ROOT_REL_SQR_ERR:
					return DBViewValueType.STRING;
					
				case TRAINED_MODEL:
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
		Set<ITableColumn> result = getAllColumns();
		result.remove(Column.AGENT_NAME);
		result.remove(Column.WEKA_OPTIONS);
		result.remove(Column.NOTE);
		result.remove(Column.TRAINED_MODEL);
		result.remove(Column.AGENT_NAME);
		return result;
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.ERROR_RATE;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPAResult> allResults;
		if(owner==null)
		{
			allResults=experiment.getResults();
		}
		else
		{
			if(owner.getId() == experiment.getBatch().getOwner().getId())
			{
				allResults=experiment.getResults();
			}
			else
			{
				allResults=new ArrayList<JPAResult>();
			}
		}
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allResults.size());
		List<ResultTableDBRow> resultRows = new ArrayList<ResultTableDBRow>();
		for(JPAResult result : allResults.subList(constraints.getOffset(), endIndex))
		{
			resultRows.add(new ResultTableDBRow(result));
		}
		return new QueryResult(resultRows, allResults.size());
	}
}