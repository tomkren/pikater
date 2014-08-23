package org.pikater.shared.database.views.tableview.externalagents;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.ExternalAgentDAO;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;

/**
 * A generic view for tables displaying information about agents uploaded from external JAR files.  
 */
public class ExternalAgentTableDBView extends AbstractTableDBView
{
	private JPAUser owner;
	
	/**
	 * Creates an admin mode view. All agents of all users will be viewed.
	 */
	public ExternalAgentTableDBView()
	{
		this.owner = null;
	}
	
	/**
	 * Creates a user mode view. 
	 * @param owner the user whose agents to display
	 */
	public ExternalAgentTableDBView(JPAUser owner)
	{
		this.owner = owner;
	}
	
	private boolean adminMode()
	{
		return this.owner == null;
	}
	
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 * <p>
	 * This enum is used for create Criteria API query in functions
	 * {@link ExternalAgentDAO#getAllUserUpload(int, int, ITableColumn, org.pikater.shared.database.views.base.SortOrder)} and 
	 * {@link ExternalAgentDAO#getByOwner(JPAUser, int, int, ITableColumn, org.pikater.shared.database.views.base.SortOrder)}
	 * <p>
	 * If you want to change column names you can redefine function {@link Column#getDisplayName()}
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		OWNER, // owner is expected to be declared first in the {@link #getColumns()} method
		CREATED,
		NAME,
		AGENT_CLASS,
		DESCRIPTION,
		APPROVED,
		DOWNLOAD,
		DELETE;

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
				case AGENT_CLASS:
				case DESCRIPTION:
				case CREATED:
					return DBViewValueType.STRING;
					
				case APPROVED:
					return DBViewValueType.BOOLEAN;
					
				case DOWNLOAD:
				case DELETE:
					return DBViewValueType.NAMED_ACTION;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		public static EnumSet<Column> getAllColumns(boolean adminMode)
		{
			if(adminMode)
			{
				return EnumSet.allOf(Column.class); 
			}
			else
			{
				return EnumSet.complementOf(EnumSet.of(Column.OWNER, Column.APPROVED));
			}
		}
	}

	@Override
	public Set<ITableColumn> getAllColumns()
	{
		return new LinkedHashSet<ITableColumn>(Column.getAllColumns(adminMode()));
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		Set<ITableColumn> result = getAllColumns();
		result.remove(Column.AGENT_CLASS);
		return result;
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return adminMode() ? Column.OWNER : Column.CREATED;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPAExternalAgent> agents;
		int allAgentsCount=0;
		if(adminMode())
		{
			agents = DAOs.externalAgentDAO.getAll(constraints.getOffset(),constraints.getMaxResults(),constraints.getSortColumn(),constraints.getSortOrder());
			allAgentsCount=DAOs.externalAgentDAO.getAllCount();
		}
		else
		{
			agents=DAOs.externalAgentDAO.getByOwner(owner, constraints.getOffset(),constraints.getMaxResults(),constraints.getSortColumn(),constraints.getSortOrder());
			allAgentsCount=DAOs.externalAgentDAO.getByOwnerCount(owner);
		}
		
		List<ExternalAgentTableDBRow> resultRows = new ArrayList<ExternalAgentTableDBRow>();
		for(JPAExternalAgent agent : agents)
		{
			resultRows.add(new ExternalAgentTableDBRow(agent));
		}
		return new QueryResult(resultRows, allAgentsCount);
	}
}