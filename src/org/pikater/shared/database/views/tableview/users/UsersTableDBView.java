package org.pikater.shared.database.views.tableview.users;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;

public class UsersTableDBView extends AbstractTableDBView {
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :).
	 * <p>
	 * This enum is used for create Criteria API query in the function
	 * {@link UserDAO#getAllUserUpload(int, int, ITableColumn, org.pikater.shared.database.views.base.SortOrder)}.
	 * <p>
	 * If you want to change column names you can redefine function {@link Column#getDisplayName()}.  
	 */
	public enum Column implements ITableColumn {
		/*
		 * First the read-only properties.
		 */
		LOGIN, EMAIL, REGISTERED,

		/*
		 * Then the editable ones.
		 */
		STATUS, MAX_PRIORITY,

		/*
		 * And finally, custom actions.
		 */
		ADMIN, // CHECKBOX
		RESET_PSWD; // BUTTON

		@Override
		public String getDisplayName() {
			if (this == MAX_PRIORITY) {
				return "USER_PRIORITY";
			} else {
				return this.name();
			}
		}

		@Override
		public DBViewValueType getColumnType() {
			switch (this) {
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
				return DBViewValueType.NAMED_ACTION;

			default:
				throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public Set<ITableColumn> getAllColumns() {
		return new LinkedHashSet<ITableColumn>(EnumSet.allOf(Column.class));
	}

	@Override
	public Set<ITableColumn> getDefaultColumns() {
		return getAllColumns();
	}

	@Override
	public ITableColumn getDefaultSortOrder() {
		return Column.LOGIN;
	}

	@Override
	public QueryResult queryUninitializedRows(final QueryConstraints constraints) {
		List<JPAUser> resultUsers = DAOs.userDAO.getAll(constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(), constraints.getSortOrder());
		int allUsersCount = DAOs.userDAO.getAllCount();

		List<UsersTableDBRow> resultRows = new ArrayList<UsersTableDBRow>();
		for (JPAUser user : resultUsers) {
			resultRows.add(new UsersTableDBRow(user));
		}
		return new QueryResult(resultRows, allUsersCount);
	}
}