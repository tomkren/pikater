package org.pikater.web.vaadin.gui.server.components.tabledbview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class DBTableItem implements Item, ICommitable
{
	private static final long serialVersionUID = -8884535836440270245L;
	
	private final DBTableContainer container;
	private final AbstractTableRowDBView rowView;
	private final Map<IColumn, Property<? extends Object>> columnToValue;
	
	public DBTableItem(DBTableContainer container, AbstractTableRowDBView rowView, DBTable parentTable)
	{
		this.container = container;
		this.rowView = rowView;
		
		this.columnToValue = new HashMap<IColumn, Property<? extends Object>>();
		for(IColumn column : getItemPropertyIds())
		{
			this.columnToValue.put(column, DBTableContainer.getProperty(container, column, rowView.getValueWrapper(column)));
		}
	}
	
	@Override
	public void commitToDB()
	{
		rowView.commitRow();
	}
	
	@Override
	public Collection<IColumn> getItemPropertyIds()
	{
		/*
		 * By default, all rows have a value for every table column. If no such value can
		 * be provided, let
		 */
		return container.getContainerPropertyIds();
	}
	
	@Override
	public Property<? extends Object> getItemProperty(Object id)
	{
		return columnToValue.get(id);
	}
	
	public AbstractTableRowDBView getRowView()
	{
		return rowView;
	}
	
	// ------------------------------------------------------------------------------------------------
	// OPTIONAL INHERITED INTERFACE
	
	/*
	 * We won't be adding or removing table columns from the GUI. 
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException
	{
		return false;
	}

	@Override
	public boolean removeItemProperty(Object id) throws UnsupportedOperationException
	{
		return false;
	}
}