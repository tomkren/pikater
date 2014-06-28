package org.pikater.shared.database.views.tableview.externalagents;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A generic view for tables displaying information about agents uploaded from external JAR files.  
 */
public class ExternalAgentTableDBView extends AbstractTableDBView
{
	private JPAUser owner;
	
	/**
	 * By default, admin mode (all agents of all users) will be inspected. 
	 */
	public ExternalAgentTableDBView()
	{
		this.owner = null;
	}
	
	/** 
	 * @param owner The user whose agents to display. If null (admin mode), all agents should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method instead.
	 */
	public void setAgentOwner(JPAUser owner)
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
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		OWNER, // owner is expected to be declared first in the {@link #getColumns()} method
		NAME,
		AGENT_CLASS,
		DESCRIPTION,
		CREATED;

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
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		public static EnumSet<Column> getColumns(boolean adminMode)
		{
			return EnumSet.allOf(Column.class);
		}
	}

	@Override
	public ITableColumn[] getColumns()
	{
		return (ITableColumn[]) Column.getColumns(adminMode()).toArray(new Column[0]);
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return adminMode() ? Column.OWNER : Column.NAME;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		List<JPAExternalAgent> agents;
		
		if(this.adminMode()){
			agents = DAOs.externalAgentDAO.getAll();
		}else{
			agents=DAOs.externalAgentDAO.getByOwner(owner);
		}
		
		List<ExternalAgentTableDBRow> rows = new ArrayList<ExternalAgentTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), agents.size());
		for(JPAExternalAgent agent : agents.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new ExternalAgentTableDBRow(agent));
		}
		return new QueryResult(rows, agents.size());
	}
}
