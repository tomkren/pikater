package org.pikater.shared.database.views.tableview.users;

import java.util.EnumSet;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.DateUtils;
import org.pikater.shared.util.collections.CollectionUtils;

public class UsersTableDBRow extends AbstractTableRowDBView {
	private final JPAUser user;
	private String newPlainTextPassword;

	public UsersTableDBRow(JPAUser user) {
		this.user = user;
		this.newPlainTextPassword = null;
	}

	public JPAUser getUser() {
		return user;
	}

	public String getNewPlainTextPassword() {
		return newPlainTextPassword;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column) {
		UsersTableDBView.Column specificColumn = (UsersTableDBView.Column) column;
		switch (specificColumn) {
		/*
		 * First the read-only properties.
		 */
		case LOGIN:
			return new StringReadOnlyDBViewValue(user.getLogin());
		case EMAIL:
			return new StringReadOnlyDBViewValue(user.getEmail());
		case REGISTERED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(user.getCreated()));

			/*
			 * Then the editable ones.
			 */
		case MAX_PRIORITY:
			return new RepresentativeDBViewValue(CollectionUtils.rangeToStringSet(0, 9), String.valueOf(user.getPriorityMax())) {
				@Override
				protected void updateEntities(String newValue) {
					user.setPriorityMax(Integer.parseInt(newValue));
				}

				@Override
				protected void commitEntities() {
					commitRow();
				}
			};
		case STATUS:

			EnumSet<JPAUserStatus> enumSet = EnumSet.allOf(JPAUserStatus.class);
			Set<String> vals   = CollectionUtils.enumSetToStringSet(enumSet);
			String selectedVal = user.getStatus().name();

			return new RepresentativeDBViewValue(vals, selectedVal) {
				@Override
				protected void updateEntities(String newValue) {
					user.setStatus(JPAUserStatus.valueOf(newValue));
				}

				@Override
				protected void commitEntities() {
					commitRow();
				}
			};
		case ADMIN:
			return new BooleanDBViewValue(user.isAdmin()) {
				@Override
				protected void updateEntities(Boolean newValue) {
					user.setAdmin(newValue);
				}

				@Override
				protected void commitEntities() {
					commitRow();
				}
			};

			/*
			 * And finally, custom actions.
			 */
		case RESET_PSWD:
			return new NamedActionDBViewValue("Reset") {
				@Override
				public boolean isEnabled() {
					return true;
				}

				@Override
				public void updateEntities() {
					newPlainTextPassword = DAOs.userDAO.resetPasswordButDontUpdate(user); // needed for GUI to send email about this action
				}

				@Override
				protected void commitEntities() {
					commitRow();
				}
			};

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow() {
		DAOs.userDAO.updateEntity(user);
	}
}