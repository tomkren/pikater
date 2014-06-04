package org.pikater.web.vaadin.tabledbview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.util.SimpleIDGenerator;
import org.pikater.web.vaadin.gui.server.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class TableDBContainer implements Container
{
	private static final long serialVersionUID = 9197656251784900256L;

	private final Table parentTable;
	private final AbstractTableDBView dbView;
	private final Map<Integer, TableDBItem> rows;
	
	public TableDBContainer(Table parentTable, AbstractTableDBView dbView)
	{
		this.parentTable = parentTable;
		this.dbView = dbView;
		this.rows = new HashMap<Integer, TableDBItem>();
		for(IColumn column : getContainerPropertyIds())
		{
			parentTable.setColumnHeader(column, column.getDisplayName());
		}
	}
	
	//-----------------------------------------------------------
	// FIRST CONTAINER-SPECIFIC INHERITED REQUIRED INTERFACE
	
	@Override
	public Collection<IColumn> getContainerPropertyIds()
	{
		return Arrays.asList(dbView.getColumns());
	}
	
	@Override
	public Class<? extends Object> getType(Object propertyId)
	{
		IColumn column = (IColumn) propertyId;
		return column.getColumnType().getResultJavaType();
	}
	
	/**
	 * This method is called whenever table refresh occurs. So, we have to rebuild the
	 * internal index from scratch.
	 */
	@Override
	public Collection<?> getItemIds()
	{
		// first, clear the previous rows
		rows.clear();
		
		// then generate the new ones
		SimpleIDGenerator ids = new SimpleIDGenerator();
		for(AbstractTableRowDBView row : dbView.getRows(null))
		{
			this.rows.put(ids.getAndIncrement(), new TableDBItem(this, row));
		}
		
		// and finally, do some filtering if needed
		Collection<Integer> result = new ArrayList<Integer>(rows.keySet());
		/*
		if(filterOnlyMachinesFromThisTopology != null)
		{
			for(Entry<Integer, RemoteServerInfoItemInner> entry : data.entrySet())
			{
				if(entry.getValue().serverInfoProperties.getProperty(Header.TOPOLOGYNAME).getValue() == filterOnlyMachinesFromThisTopology)
				{
					result.add(entry.getKey());
				}
			}
		}
		else
		{
			result.addAll(data.keySet());
		}
		
		if(filterFilled)
		{
			Collection<Integer> toRemove = new ArrayList<Integer>();
			for(Integer idToCheck : result)
			{
				if(!data.get(idToCheck).serverInfoProperties.isHostnameUsernameOrPasswordMissing()) // nothing is missing
				{
					toRemove.add(idToCheck);
				}
			}
			result.removeAll(toRemove);
		}
		*/
		return result;
	}
	
	@Override
	public int size()
	{
		return rows.size();
	}
	
	@Override
	public boolean containsId(Object itemId)
	{
		return rows.containsKey(itemId);
	}
	
	@Override
	public TableDBItem getItem(Object itemId)
	{
		return rows.get(itemId);
	}

	@Override
	public Property<? extends Object> getContainerProperty(Object itemId, Object propertyId)
	{
		return getItem(itemId).getItemProperty(propertyId);
	}
	
	//-----------------------------------------------------------
	// THEN CONTAINER-SPECIFIC INHERITED OPTIONAL INTERFACE
	
	/*
	 * If you want these methods, override them in a child implementation.
	 */
	
	@Override
	public Object addItem() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	// ------------------------------------------------------------------------------------------------
	// BANNED INHERITED INTERFACE
	
	/*
	 * We won't be adding or removing table columns from the GUI. 
	 */
	
	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException
	{
		return false;
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException
	{
		return false;
	}
	
	//-----------------------------------------------------------
	// AND FINALLY, SOME ADDED VALUE
	
	public boolean isTableInImmediateMode()
	{
		return parentTable.isImmediate();
	}
	
	private void batchSetValues(Set<Integer> ids, Header header, String newValue)
	{
		if(header.supportsBatchSet())
		{
			for(Integer itemID : ids)
			{
				//getItem(itemID).serverInfoProperties.setValueForProperty(header, newValue);
			}
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}
}