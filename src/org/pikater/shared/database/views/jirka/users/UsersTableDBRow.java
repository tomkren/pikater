package org.pikater.shared.database.views.jirka.users;

import java.util.EnumSet;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.ActionDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;
import org.pikater.shared.util.DateUtils;
import org.pikater.shared.util.collections.CollectionUtils;

public class UsersTableDBRow extends AbstractTableRowDBView
{
	public final JPAUser user;
	
	public UsersTableDBRow(JPAUser user)
	{
		this.user = user;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		UsersTableDBView.Column specificColumn = (UsersTableDBView.Column) column;
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
				return new StringDBViewValue(DateUtils.toCzechDate(user.getCreated()), true)
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
				return new RepresentativeDBViewValue(CollectionUtils.enumSetToStringSet(EnumSet.allOf(JPAUserStatus.class)), user.getStatus().name(), false)
				{
					@Override
					protected void updateEntities(String newValue)
					{
						user.setStatus(JPAUserStatus.valueOf(newValue));
					}

					@Override
					protected void commitEntities()
					{
						commitRow();
					}
				};
				
			case MAXIMUM_PRIORITY:
				return new RepresentativeDBViewValue(CollectionUtils.rangeToStringSet(0, 9), String.valueOf(user.getPriorityMax()), false)
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
				
			case RESET_PASSWORD:
				return new ActionDBViewValue("Reset")
				{
					@Override
					public void executeAction()
					{
						// TODO: reset user password
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