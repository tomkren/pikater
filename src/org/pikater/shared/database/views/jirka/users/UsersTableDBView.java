package org.pikater.shared.database.views.jirka.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;
import org.pikater.shared.database.views.jirka.abstractview.QueryResult;

public class UsersTableDBView extends AbstractTableDBView
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
		 * Then the editable ones.
		 */
		ACCOUNT_STATUS,
		MAXIMUM_PRIORITY,
		
		/*
		 * And finally, custom actions.
		 */
		RESET_PASSWORD;

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
					
				case RESET_PASSWORD:
					return ColumnType.ACTION;
					
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
	public IColumn getDefaultSortOrder()
	{
		return Column.LOGIN;
	}

	@Override
	public QueryResult queryUninitializedRows(final QueryConstraints constraints)
	{
		/*
		 * IMPORTANT - as stated in Javadoc:
		 * - the result collection should not be internally cached,
		 * - the result collection should be sorted in ascending order.
		 */
		
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		List<JPAUser> allUsers = DAOs.userDAO.getAll();
		List<UsersTableDBRow> rows = new ArrayList<UsersTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allUsers.size());
		for(JPAUser user : allUsers.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new UsersTableDBRow(user));
		}
		
		Collections.sort(rows, new Comparator<UsersTableDBRow>()
		{
			@Override
			public int compare(UsersTableDBRow o1, UsersTableDBRow o2)
			{
				Column sortColumn = (Column) constraints.getSortOrder();
				switch(sortColumn)
				{
					case LOGIN:
						return o1.user.getLogin().compareToIgnoreCase(o2.user.getLogin());
					case EMAIL:
						return o1.user.getEmail().compareToIgnoreCase(o2.user.getEmail());
					case MAXIMUM_PRIORITY:
						return o1.user.getPriorityMax().compareTo(o2.user.getPriorityMax());
					case REGISTERED_AT:
						return o1.user.getCreated().compareTo(o2.user.getCreated());
					default:
						// throw new IllegalStateException("Unknown column: " + sortColumn.name());
						break; // do nothing
				}
				return 0;
			}
		});
		
		return new QueryResult(rows, allUsers.size());
	}
}