package org.pikater.shared.database.views.jirka.users;

import java.util.EnumSet;
import java.util.Set;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;

public class UsersTableRow extends AbstractTableRowDBView
{
	private final JPAUser user;
	
	public UsersTableRow(JPAUser user)
	{
		this.user = user;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		UsersTableView.Column specificColumn = (UsersTableView.Column) column;
		switch(specificColumn)
		{
			/*
			 * First the read-only properties.
			 */
			case LOGIN:
				return new StringDBViewValue(user.getLogin(), true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
			case EMAIL:
				return new StringDBViewValue(user.getEmail(), true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
			case REGISTERED_AT:
				return new StringDBViewValue(user.getCreated().toString(), true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
				
			/*
			 * And then the editable ones.
			 */
			case ACCOUNT_STATUS:
                //TODO
//                EnumSet<String> enum=EnumSet.allOf(JPAUserStatus.class);
//                Set<String> values=AppHelper.enumSetToStringSet(enum);
//				return new RepresentativeDBViewValue(values, user.getStatus().name(), false)
//				{
//					@Override
//					protected void commitValue()
//					{
//						RepresentativeDBViewValue valueWrapper = (RepresentativeDBViewValue) getValueWrapper(column);
//						user.setStatus(JPAUserStatus.valueOf(valueWrapper.getValue()));
//						commitRow();
//					}
//				};
			case MAXIMUM_PRIORITY:
				return new RepresentativeDBViewValue(AppHelper.rangeToStringSet(0, 9), String.valueOf(user.getPriorityMax()), false)
				{
					@Override
					protected void updateEntities(String newValue)
					{
						user.setPriorityMax(Integer.parseInt(newValue));
					}
					
					@Override
					protected void commitEntities()
					{
						commitRow();
					}
				};
			
			default:
				throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}
	
	@Override
	public void commitRow()
	{
		DAOs.userDAO.updateEntity(user);
	}
}