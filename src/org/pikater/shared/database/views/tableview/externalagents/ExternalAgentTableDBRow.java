package org.pikater.shared.database.views.tableview.externalagents;

import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.DateUtils;

public class ExternalAgentTableDBRow extends AbstractTableRowDBView {

	private JPAExternalAgent agent=null;

	public ExternalAgentTableDBRow(JPAExternalAgent agent)
	{
		this.agent=agent;
	}
	
	public JPAExternalAgent getAgent()
	{
		return agent;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		ExternalAgentTableDBView.Column specificColumn = (ExternalAgentTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case OWNER:
			return new StringReadOnlyDBViewValue(agent.getOwner().getLogin());
		case DESCRIPTION:
			return new StringReadOnlyDBViewValue(agent.getDescription());
		case AGENT_CLASS:
			return new StringReadOnlyDBViewValue(agent.getAgentClass());
		case NAME:
			return new StringReadOnlyDBViewValue(agent.getName());
		case CREATED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(agent.getCreated()));
			
		/*
		 * And then custom actions.
		 */
		case APPROVED:
			return new BooleanDBViewValue(agent.isApproved())
			{
				@Override
				protected void updateEntities(Boolean newValue)
				{
					agent.setApproved(newValue);
				}
				
				@Override
				protected void commitEntities()
				{
					commitRow();
				}
			};
		case DOWNLOAD:
			return new NamedActionDBViewValue("Download") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled()
				{
					return true;
				}
				
				@Override
				public void updateEntities()
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case DELETE:
			return new NamedActionDBViewValue("Delete")
			{
				@Override
				public boolean isEnabled()
				{
					return true;
				}
				
				@Override
				public void updateEntities()
				{
					agent.setVisible(false);
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
		DAOs.externalAgentDAO.updateEntity(agent);
	}
}