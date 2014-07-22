package org.pikater.shared.database.views.tableview.batches;

import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A generic view for tables displaying information on batches (experiments).  
 */
public abstract class AbstractBatchTableDBView extends AbstractTableDBView
{
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		FINISHED, // both users and admins should probably be most interested in the latest batches
		STATUS, // and whether they are yet finished or not
		PRIORITY,
		MAX_PRIORITY,
		CREATED,
		OWNER,
		NAME,
		NOTE; // this should be last because of potentially long text

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
				case OWNER:
				case NAME:
				case NOTE:
				case CREATED:
				case FINISHED:
				case STATUS:
					return DBViewValueType.STRING;
					
				// TODO: these should be editable?
				case PRIORITY:
				case MAX_PRIORITY:
					return DBViewValueType.STRING;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
}