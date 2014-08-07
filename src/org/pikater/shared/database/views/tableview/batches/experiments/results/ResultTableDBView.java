package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A generic view for tables displaying result errors and statistic.  
 */
public class ResultTableDBView extends AbstractTableDBView
{
	private final JPAUser owner;
	private JPAExperiment experiment;
	
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
	
	private boolean adminMode()
	{
		return this.owner == null;
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
		OWNER, // owner is expected to be declared first in the {@link #getColumns()} method
		AGENT_NAME,
		WEKA_OPTIONS,
		ERROR_RATE,
		KAPPA_STATISTIC,
		MEAN_ABSOLUTE_ERROR,
		ROOT_MEAN_SQUARED_ERROR,
		RELATIVE_ABSOLUTE_ERROR,
		ROOT_RELATIVE_SQUARED_ERROR,
		STARTED,
		FINISHED,
		NOTE,
		CREATED_MODEL;
		

		@Override
		public String getDisplayName()
		{
			return this.name();
		}
	}
	
	@Override
	public DBViewValueType getTypeForColumn(ITableColumn column)
	{
		Column specificColumn = (Column) column;
		switch(specificColumn)
		{
			case OWNER:
	
			case AGENT_NAME:
			case NOTE:
			case WEKA_OPTIONS:
			
			case ERROR_RATE:
			case KAPPA_STATISTIC:
			case MEAN_ABSOLUTE_ERROR:
			case RELATIVE_ABSOLUTE_ERROR:
			case ROOT_MEAN_SQUARED_ERROR:
			case ROOT_RELATIVE_SQUARED_ERROR:
				
			case STARTED:
			case FINISHED:
				return DBViewValueType.STRING;
			
			case CREATED_MODEL:
				return DBViewValueType.NAMED_ACTION;
				
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}

	@Override
	public ITableColumn[] getColumns()
	{
		if(adminMode())
		{
			return Column.values();
		}
		else
		{
			ITableColumn[] allColumns = Column.values();
			return Arrays.copyOfRange(allColumns, 1, allColumns.length); // everything except owner, which is specified
		}
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return adminMode() ? Column.OWNER : Column.AGENT_NAME;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE

		List<JPAResult> results;
		
		if(owner==null){
			results=experiment.getResults();
		}else{
			if(owner.getLogin().equals(experiment.getBatch().getOwner().getLogin())){
				results=experiment.getResults();
			}else{
				results=new ArrayList<JPAResult>();
			}
		}
		
		
		List<ResultTableDBRow> rows = new ArrayList<ResultTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), results.size());
		for(JPAResult result : results.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new ResultTableDBRow(result));
		}
		return new QueryResult(rows, results.size());
	}
}
