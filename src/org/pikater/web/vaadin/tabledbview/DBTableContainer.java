package org.pikater.web.vaadin.tabledbview;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.RepresentativeDBViewValue;
import org.pikater.web.vaadin.gui.server.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

public class DBTableContainer implements Container.Sortable, ICommitable
{
	private static final long serialVersionUID = 9197656251784900256L;

	private final ISortableTableContainerContext context;
	private final AbstractTableDBView dbView;
	private final Set<IColumn> sortableColumns;
	
	private final DBTableContainerItems rows;
	
	public DBTableContainer(ISortableTableContainerContext context, AbstractTableDBView dbView)
	{
		this.context = context;
		this.dbView = dbView;
		this.sortableColumns = new HashSet<IColumn>();
		for(IColumn column : getContainerPropertyIds())
		{
			this.context.getParentTable().setColumnHeader(column, column.getDisplayName());
			this.context.getParentTable().setColumnAlignment(column, Align.CENTER);
			if(column.getColumnType().isSortable())
			{
				this.sortableColumns.add(column);
			}
		}
		
		this.rows = new DBTableContainerItems();
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
		return getPresentationType((IColumn) propertyId);
	}
	
	/**
	 * This method is called whenever table refresh occurs. So, we have to rebuild the
	 * internal index from scratch.
	 */
	@Override
	public Collection<?> getItemIds()
	{
		rows.loadRows(this, dbView.getRows(context.getQuery()));
		return rows.getRowIDs();
	}
	
	@Override
	public int size()
	{
		return rows.size();
	}
	
	@Override
	public boolean containsId(Object itemId)
	{
		return rows.getRowIDs().contains(itemId);
	}
	
	@Override
	public DBTableItem getItem(Object itemId)
	{
		return rows.getRow(itemId);
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
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	//-----------------------------------------------------------
	// CONTAINER-SPECIFIC SORTABLE REQUIRED INTERFACE
	
	public Collection<?> getSortableContainerPropertyIds()
	{
		return sortableColumns;
	}
	
	public Object firstItemId()
	{
		return rows.getFirstItemID();
	}

	public Object nextItemId(Object itemId)
	{
		return rows.getNextItemID(itemId);
	}

	public Object prevItemId(Object itemId)
	{
		return rows.getPrevItemID(itemId);
	}

	public Object lastItemId()
	{
		return rows.getLastItemID();
	}

	public boolean isFirstId(Object itemId)
	{
		return rows.isFirstID(itemId);
	}

	public boolean isLastId(Object itemId)
	{
		return rows.isLastID(itemId);
	}

	public void sort(Object[] propertyId, boolean[] ascending)
	{
		rows.setSortOrder(ascending[0]); // only look at the primary column sort order
	}

	//-----------------------------------------------------------
	// CONTAINER-SPECIFIC SORTABLE OPTIONAL INTERFACE
	
	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	//-----------------------------------------------------------
	// VIEW TYPE TO PRESENTATION TYPE BINDING METHODS
	
	public static Class<? extends Object> getPresentationType(IColumn column)
	{
		switch(column.getColumnType())
		{
			case BOOLEAN:
			case STRING:
				return column.getColumnType().getResultJavaType(); // simply forward the type declaration
			
			case REPRESENTATIVE:
				return ComboBox.class; // override the type declaration
			
			default:
				throw new IllegalStateException("Unknown state: " + column.getColumnType().name());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Property<? extends Object> getProperty(DBTableContainer container, IColumn column, AbstractDBViewValue<? extends Object> value)
	{
		switch(column.getColumnType())
		{
			case BOOLEAN:
			case STRING:
				return new DBTableItemPropertyGeneric<Object>(container, column, (AbstractDBViewValue<Object>) value);
			
			case REPRESENTATIVE:
				return new DBTableItemPropertyCombo(container, (RepresentativeDBViewValue) value);
				
			default:
				throw new IllegalStateException("Unknown state: " + column.getColumnType().name());
		}
	}
	
	//-----------------------------------------------------------
	// SPECIAL DATABASE INTERFACE
	
	@Override
	public void commitToDB()
	{
		rows.commitToDB();
	}
	
	//-----------------------------------------------------------
	// AND FINALLY, SOME ADDED VALUE
	
	public interface ISortableTableContainerContext
	{
		Table getParentTable();
		QueryConstraints getQuery();
	}
	
	public ISortableTableContainerContext getContext()
	{
		return context;
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