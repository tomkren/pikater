package org.pikater.shared.database.views.tableview.batches.experiments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

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
		this.batch=batch;
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
		STATUS,
		MODEL_STRATEGY,
		CREATED,
		STARTED,
		FINISHED,
		MODEL,
		RESULTS;

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
			case CREATED:
			case STARTED:
			case FINISHED:
			case STATUS:
			case MODEL_STRATEGY:
				return DBViewValueType.STRING;
				
			case MODEL:
			case RESULTS:
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
		return adminMode() ? Column.OWNER : Column.CREATED;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE

		List<JPAExperiment> experiments;
		
		if(owner==null){
			experiments=batch.getExperiments();
		}else{
			if(owner.getLogin().equals(batch.getOwner().getLogin())){
				experiments=batch.getExperiments();
			}else{
				experiments=new ArrayList<JPAExperiment>();
			}
		}
		
		
		List<ExperimentTableDBRow> rows = new ArrayList<ExperimentTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), experiments.size());
		for(JPAExperiment experiment : experiments.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new ExperimentTableDBRow(experiment));
		}
		return new QueryResult(rows, experiments.size());
	}
}
