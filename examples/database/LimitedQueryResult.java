package vietpot.server.Database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LimitedQueryResult<R>
{
	public List<R> resultEntities;
	public int offset;
	public Date queryFinishTime;

	public LimitedQueryResult(List<R> resultEntities, int offset, Date queryFinishTime)
	{
		this.resultEntities = new ArrayList<R>(resultEntities);
		this.offset = offset;
		this.queryFinishTime = queryFinishTime;
	}
}
