package org.pikater.web.vaadin.tabledbview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.util.SimpleIDGenerator;

public class DBTableContainerItems implements ICommitable
{
	public enum SortOrder
	{
		ASCENDING,
		DESCENDING
	}
	
	private final Map<Integer, DBTableItem> rows;
	private SortOrder currentSortOrder;
	
	public DBTableContainerItems()
	{
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
	
	public void loadRows(DBTableContainer container, Collection<? extends AbstractTableRowDBView> queryResult)
	{
		// first, clear the previous rows
		this.rows.clear();
		
		// then generate the new ones
		SimpleIDGenerator ids = new SimpleIDGenerator();
		for(AbstractTableRowDBView row : queryResult)
		{
			this.rows.put(ids.getAndIncrement(), new DBTableItem(container, row));
		}
		
		// and reset the sort order
		resetSortOrder();
	}
	
	public Set<Integer> getRowIDs()
	{
		return this.rows.keySet();
	}
	
	public DBTableItem getRow(Object rowID)
	{
		return this.rows.get(rowID);
	}
	
	public int size()
	{
		return this.rows.size();
	}
	
	//-----------------------------------------------
	// SORT INTERFACE
	
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
		return itemId.equals(SimpleIDGenerator.getFirstID());
	}

	public boolean isLastID(Object itemId)
	{
		return itemId.equals(getLastItemID());
	}
	
	//-----------------------------------------------
	// PRIVATE INTERFACE
	
	private void resetSortOrder()
	{
		this.currentSortOrder = SortOrder.ASCENDING;
	}
}