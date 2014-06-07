package org.pikater.web.vaadin.tabledbview;

import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;

import com.vaadin.data.Property;

public class TableDBItemProperty<T extends Object> implements Property<T>
{
	private static final long serialVersionUID = -2142093421893500620L;
	
	private final TableDBContainer container;
	private final IColumn column;
	private final AbstractDBViewValue<T> valueWrapper;
	
	public TableDBItemProperty(TableDBContainer container, IColumn column, AbstractDBViewValue<T> valueWrapper)
	{
		this.container = container;
		this.column = column;
		this.valueWrapper = valueWrapper;
		
		// compatibility check
		Class<?> valueTypeClass = getValue().getClass();
		Class<?> viewBindingTypeClass = getType();
		if(!valueTypeClass.equals(viewBindingTypeClass))
		{
			throw new ClassCastException(
					String.format("Incorrect type binding detected. '%s' suggests '%s' but '%s' returns '%s'",
							column.getClass().getName(),
							viewBindingTypeClass.getName(),
							valueWrapper.getClass().getName(),
							valueTypeClass.getName())
			);
		}
	}

	@Override
	public T getValue()
	{
		return (T) valueWrapper.getValue();
	}

	@Override
	public void setValue(T newValue) throws com.vaadin.data.Property.ReadOnlyException
	{
		valueWrapper.setValue(newValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getType()
	{
		return (Class<T>) container.getType(column);
	}

	@Override
	public boolean isReadOnly()
	{
		return valueWrapper.isReadOnly();
	}

	@Override
	public void setReadOnly(boolean newStatus)
	{
		// TODO: temporary value change may be sometimes needed?
	}
}