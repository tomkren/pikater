package org.pikater.shared.database.views.tableview.batches.experiments.results.unused;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBRow;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBView;

public class ResultTableDBViewBatch extends ResultTableDBView
{
	private final JPABatch batch;
	
	public ResultTableDBViewBatch(JPABatch batch)
	{
		super();
		this.batch = batch;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPAResult> batchResultList = DAOs.batchDAO.getByIDwithResults(batch.getId(), constraints.getOffset(), constraints.getMaxResults());
		int resultCount = DAOs.batchDAO.getBatchResultCount(batch);
		
		List<ResultTableDBRow> resultRows = new ArrayList<ResultTableDBRow>();
	    for(JPAResult result : batchResultList)
	    {
	    	resultRows.add(new ResultTableDBRow(result));
		}
	    return new QueryResult(resultRows, resultCount);
	}
}