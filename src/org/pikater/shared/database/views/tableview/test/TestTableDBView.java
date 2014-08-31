package org.pikater.shared.database.views.tableview.test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;

public class TestTableDBView extends AbstractTableDBView
{
	public enum Column implements ITableColumn
	{
		COLUMN1,
		COLUMN2,
		COLUMN3,
		COLUMN4;

		@Override
		public String getDisplayName()
		{
			return name();
		}

		@Override
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case COLUMN1:
					return DBViewValueType.STRING;
				case COLUMN2:
					return DBViewValueType.BOOLEAN;
				case COLUMN3:
					return DBViewValueType.REPRESENTATIVE;
				case COLUMN4:
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
		return Column.COLUMN1;
	}

	@Override
	protected QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<TestTableDBRow> rows = new ArrayList<TestTableDBRow>();
		for(int i = 1; i <= Column.values().length; i++)
		{
			rows.add(new TestTableDBRow(i));
		}
		return new QueryResult(rows, Column.values().length); 
	}
}