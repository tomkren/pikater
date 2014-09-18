package org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.SimpleIDGenerator;

public class DBTableContainerItems implements ICommitable
{
	private final DBTable parentTable;
	private final Map<Integer, DBTableItem> rows;
	
	private QueryResult lastQueryResult;
	
	public DBTableContainerItems(DBTable parentTable)
	{
		this.parentTable = parentTable;
		this.rows = new HashMap<Integer, DBTableItem>();
	}
	
	@Override
	public void commitToDB()
	{
		for(DBTableItem row : rows.values())
		{
			row.commitToDB();
		}
	}
	
	//-----------------------------------------------
	// MAIN CONTAINER INTERFACE
	
	public void loadRows(DBTableContainer container, QueryResult queryResult)
	{
		// first, clear the previous rows
		this.rows.clear();
		
		// then generate the new ones
		SimpleIDGenerator ids = new SimpleIDGenerator();
		for(AbstractTableRowDBView row : queryResult.getConstrainedResults())
		{
			this.rows.put(ids.getAndIncrement(), new DBTableItem(container, row, parentTable));
		}
		
		// and finally:
		lastQueryResult = queryResult;
	}
	
	public Set<Integer> getRowIDs()
	{
		return this.rows.keySet();
	}
	
	public DBTableItem getRow(Object rowID)
	{
		return this.rows.get(rowID);
	}
	
	public int getTableItemCount()
	{
		return this.rows.size();
	}
	
	public int getAllItemsCount()
	{
		return lastQueryResult.getAllResultsCount();
	}
	
	//-----------------------------------------------
	// SORT INTERFACE - REFER TO THE CONTAINER.SORTABLE INTERFACE JAVADOC WHEN IMPLEMENTING
	
	public Object getFirstItemID()
	{
		if(!rows.isEmpty())
		{
			return SimpleIDGenerator.getFirstID();
		}
		else
		{
			return null;
		}
	}

	public Object getNextItemID(Object itemId)
	{
		if(isLastID(itemId))
		{
			return null;
		}
		else
		{
			Integer integer = (Integer) itemId;
			return integer + 1;
		}
	}

	public Object getPrevItemID(Object itemId)
	{
		if(isFirstID(itemId))
		{
			return null;
		}
		else
		{
			Integer integer = (Integer) itemId;
			return integer - 1;
		}
	}

	public Object getLastItemID()
	{
		if(!rows.isEmpty())
		{
			return rows.size() - 1;
		}
		else
		{
			return null;
		}
	}

	public boolean isFirstID(Object itemId)
	{
		if(itemId == null)
		{
			return false;
		}
		else
		{
			return itemId.equals(SimpleIDGenerator.getFirstID());
		}
	}

	public boolean isLastID(Object itemId)
	{
		if(itemId == null)
		{
			return false;
		}
		else
		{
			return itemId.equals(getLastItemID());
		}
	}
}