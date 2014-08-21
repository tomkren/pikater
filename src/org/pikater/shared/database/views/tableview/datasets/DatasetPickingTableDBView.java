package org.pikater.shared.database.views.tableview.datasets;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.views.tableview.base.ITableColumn;

public class DatasetPickingTableDBView extends DataSetTableDBView
{
	@Override
	public Set<ITableColumn> getAllColumns()
	{
		return new LinkedHashSet<ITableColumn>(EnumSet.of(
				Column.CREATED,
				Column.DEFAULT_TASK_TYPE,
				Column.NUMBER_OF_INSTANCES,
				Column.FILENAME,
				Column.DESCRIPTION
		));
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		return getAllColumns();
	}
}