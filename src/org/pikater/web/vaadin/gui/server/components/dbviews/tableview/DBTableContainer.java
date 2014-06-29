package org.pikater.web.vaadin.gui.server.components.dbviews.tableview;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.pikater.shared.database.views.base.SortOrder;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;

public class DBTableContainer implements Container.Sortable, ICommitable
{
	private static final long serialVersionUID = 9197656251784900256L;

	private final DBTable parentTable;
	private final DBTableContainerItems rows;
	
	public DBTableContainer(DBTable parentTable)
	{
		this.parentTable = parentTable;
		this.rows = new DBTableContainerItems(parentTable);
	}
	
	//-----------------------------------------------------------
	// FIRST CONTAINER-SPECIFIC INHERITED REQUIRED INTERFACE
	
	@Override
	public Collection<ITableColumn> getContainerPropertyIds()
	{
		return Arrays.asList(viewRoot.getUnderlyingDBView().getColumns());
	}
	
	@Override
	public Class<? extends Object> getType(Object propertyId)
	{
		return getPresentationType((ITableColumn) propertyId);
	}
	
	/**
	 * This method is called whenever table refresh occurs. So, we have to rebuild the
	 * internal index from scratch.
	 */
	@Override
	public Collection<?> getItemIds()
	{
		rows.loadRows(this, viewRoot.getUnderlyingDBView().queryRows(parentTable.getQuery()));
		return rows.getRowIDs();
	}
	
	@Override
	public int size()
	{
		return rows.getTableItemCount();
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
		return viewRoot.getUnderlyingDBView().getSortableColumns();
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
	
	public static Class<? extends Object> getPresentationType(ITableColumn column)
	{
		switch(column.getColumnType())
		{
			case BOOLEAN:
				return CheckBox.class;
			
			case STRING:
				return column.getColumnType().getResultJavaType(); // simply forward the type declaration
			
			case REPRESENTATIVE:
				return ComboBox.class; // override the type declaration
				
			case NAMED_ACTION:
				return Button.class;
			
			default:
				throw new IllegalStateException("Unknown state: " + column.getColumnType().name());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Property<? extends Object> getProperty(DBTableContainer container, ITableColumn column, AbstractTableRowDBView row, 
			AbstractDBViewValue<? extends Object> value)
	{
		switch(column.getColumnType())
		{
			case BOOLEAN:
				return new DBTableItemPropertyCheck(container.getParentTable(), (BooleanDBViewValue) value);
				
			case STRING:
				return new DBTableItemPropertyGeneric<Object>(container, column, (AbstractDBViewValue<Object>) value);
			
			case REPRESENTATIVE:
				DBTableItemPropertyCombo result1 = new DBTableItemPropertyCombo(container.getParentTable(), (RepresentativeDBViewValue) value);
				result1.getValue().setWidth("100%");
				container.getViewRoot().onCellCreate(column, result1.getValue());
				return result1;
				
			case NAMED_ACTION:
				return new DBTableItemPropertyAction(container, column, row, (NamedActionDBViewValue) value);
				
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
	
	private IDBViewRoot<? extends AbstractTableDBView> viewRoot;
	
	public void setViewRoot(IDBViewRoot<? extends AbstractTableDBView> viewRoot)
	{
		this.viewRoot = viewRoot;		
	}
	
	public DBTable getParentTable()
	{
		return parentTable;
	}
	
	public IDBViewRoot<? extends AbstractTableDBView> getViewRoot()
	{
		return viewRoot;
	}
	
	public SortOrder getCurrentSortOrder()
	{
		return rows.getCurrentSortOrder();
	}
	
	public int getUnconstrainedQueryResultsCount()
	{
		return rows.getAllItemsCount();
	}
	
	// TODO: do we even need this?
	protected void batchSetValues(Set<Integer> ids, Header header, String newValue)
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