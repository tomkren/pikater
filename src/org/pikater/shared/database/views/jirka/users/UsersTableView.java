package org.pikater.shared.database.views.jirka.users;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;

public class UsersTableView extends AbstractTableDBView
{
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements IColumn
	{
		/*
		 * First the read-only properties.
		 */
		LOGIN,
		EMAIL,
		REGISTERED_AT,
		
		/*
		 * And then the editable ones.
		 */
		ACCOUNT_STATUS,
		MAXIMUM_PRIORITY;

		@Override
		public String getDisplayName()
		{
			return this.name();
		}

		@Override
		public ColumnType getColumnType()
		{
			switch(this)
			{
				case EMAIL:
				case LOGIN:
				case REGISTERED_AT:
					return ColumnType.STRING;
					
				case ACCOUNT_STATUS:
				case MAXIMUM_PRIORITY:
					return ColumnType.REPRESENTATIVE;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
	
	@Override
	public IColumn[] getColumns()
	{
		return Column.values();
	}

	@Override
	public Collection<? extends AbstractTableRowDBView> getUninitializedRowsAscending(QueryConstraints constraints)
	{
		// IMPORTANT: as stated in Javadoc, the result collection should not be internally cached, so:
		
		// TODO: use constraints
		
		Collection<UsersTableRow> rows = new ArrayList<UsersTableRow>();
		for(JPAUser user : DAOs.userDAO.getAll())
		{
			rows.add(new UsersTableRow(user));
		}
		return rows;
	}
}