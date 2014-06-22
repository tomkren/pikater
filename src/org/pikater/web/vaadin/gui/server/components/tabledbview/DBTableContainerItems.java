package org.pikater.web.vaadin.gui.server.components.tabledbview;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IExpandableDBView;
import org.pikater.shared.database.views.jirka.abstractview.QueryResult;
import org.pikater.shared.database.views.jirka.abstractview.SortOrder;
import org.pikater.shared.util.SimpleIDGenerator;

public class DBTableContainerItems implements ICommitable
{
	private final DBTable parentTable;
	private final Map<Integer, DBTableItem> rows;
	
	private QueryResult lastQueryResult;
	private SortOrder currentSortOrder;
	
	public DBTableContainerItems(DBTable parentTable)
	{
		this.parentTable = parentTable;
		this.rows = new HashMap<Integer, DBTableItem>();
		resetSortOrder();
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
			if(parentTable.rowsExpandIntoOtherViews() && !(row instanceof IExpandableDBView))
			{
				throw new IllegalArgumentException("Expandable row view is expected.");
			}
			else
			{
				this.rows.put(ids.getAndIncrement(), new DBTableItem(container, row, parentTable));
			}
		}
		
		// and finally:
		resetSortOrder();
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
	
	public SortOrder getCurrentSortOrder()
	{
		return currentSortOrder;
	}
	
	//-----------------------------------------------
	// SORT INTERFACE - REFER TO THE CONTAINER.SORTABLE INTERFACE JAVADOC WHEN IMPLEMENTING
	
	public void setSortOrder(boolean ascending)
	{
		if(SimpleIDGenerator.getFirstID() != 0)
		{
			throw new IllegalStateException("The sorting algorithm requires the first ID of SimpleIDGenerator to be '0'.");
		}
		
		SortOrder newSortOrder = ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING;
		if(this.currentSortOrder != newSortOrder)
		{
			// linear order reverse
			int switchCount = rows.size() / 2;
			for(int i = 0; i < switchCount; i++)
			{
				// swap values
				DBTableItem tmp = rows.get(i);
				int otherIndex = rows.size() - i - 1; // this is where the 'SimpleIDGenerator.getFirstID() != 0' condition comes into play
				rows.put(i, rows.get(otherIndex));
				rows.put(otherIndex, tmp);
			}
		}
		
		this.currentSortOrder = newSortOrder; 
	}
	
	public Object getFirstItemID()
	{
		if(rows.size() > 0)
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
		if(rows.size() > 0)
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
	
	//-----------------------------------------------
	// PRIVATE INTERFACE
	
	private void resetSortOrder()
	{
		this.currentSortOrder = SortOrder.ASCENDING;
	}
}