package org.pikater.web.vaadin.tabledbview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class TableDBItem implements Item
{
	private static final long serialVersionUID = -8884535836440270245L;
	
	private final TableDBContainer container;
	private final Map<IColumn, TableDBItemProperty<? extends Object>> columnToValue;
	
	@SuppressWarnings("unchecked")
	public TableDBItem(TableDBContainer container, AbstractTableRowDBView rowView)
	{
		this.container = container;
		
		this.columnToValue = new HashMap<IColumn, TableDBItemProperty<? extends Object>>();
		for(IColumn column : getItemPropertyIds())
		{
			this.columnToValue.put(column, new TableDBItemProperty<Object>(container, column, (AbstractDBViewValue<Object>) rowView.getValueWrapper(column)));
		}
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
	public TableDBItemProperty<? extends Object> getItemProperty(Object id)
	{
		return columnToValue.get(id);
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