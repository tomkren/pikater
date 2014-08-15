package org.pikater.shared.database.views.tableview.users;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.UserDAO;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

public class UsersTableDBView extends AbstractTableDBView
{
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :).
	 * <p>
	 * This enum is used for create Criteria API query in the function
	 * {@link UserDAO#getAll(int, int, ITableColumn, org.pikater.shared.database.views.base.SortOrder)}.
	 * <p>
	 * If you want to change column names you can redefine function {@link Column#getDisplayName()}
	 *  
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		LOGIN,
		EMAIL,
		REGISTERED,
		
		/*
		 * Then the editable ones.
		 */
		STATUS,
		MAX_PRIORITY,
		
		/*
		 * And finally, custom actions.
		 */
		ADMIN, // CHECKBOX
		RESET_PSWD, // BUTTON
		DELETE; // BUTTON

		@Override
		public String getDisplayName()
		{
			if(this == MAX_PRIORITY)
			{
				return "USER_PRIORITY";
			}
			else
			{
				return this.name();
			}
		}

		@Override
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case LOGIN:
				case EMAIL:
				case REGISTERED:
					return DBViewValueType.STRING;
					
				case STATUS:
				case MAX_PRIORITY:
					return DBViewValueType.REPRESENTATIVE;
					
				case ADMIN:
					return DBViewValueType.BOOLEAN;
					
				case RESET_PSWD:
				case DELETE:
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
		return Column.LOGIN;
	}

	@Override
	public QueryResult queryUninitializedRows(final QueryConstraints constraints)
	{
		List<JPAUser> allUsers = DAOs.userDAO.getAll(constraints.getOffset(),constraints.getMaxResults(), constraints.getSortColumn(),constraints.getSortOrder());
		int allUsersCount=DAOs.userDAO.getAllCount();
		List<UsersTableDBRow> rows = new ArrayList<UsersTableDBRow>();
		
		for(JPAUser user : allUsers)
		{
			rows.add(new UsersTableDBRow(user));
		}
		
		return new QueryResult(rows, allUsersCount);
	}
}