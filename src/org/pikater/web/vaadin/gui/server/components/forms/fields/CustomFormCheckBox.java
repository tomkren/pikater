package org.pikater.web.vaadin.gui.server.components.forms.fields;

import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.OptionGroup;

public class CustomFormCheckBox extends OptionGroup
{
	private static final long serialVersionUID = -6320729319786554512L;
	
	private final Object itemID;
	
	public CustomFormCheckBox(String componentCaption, String checkBoxCaption, boolean initialState, boolean readOnly)
	{
		super(componentCaption);
		setImmediate(true);
		setMultiSelect(true);
		
		this.itemID = checkBoxCaption;
		addItem(this.itemID);
		if(initialState)
		{
			select(this.itemID);
		}
		
		setReadOnly(readOnly);
	}
	
	/**
	 * The provided listener should accept boolean properties.
	 */
	@Override
	public void addValueChangeListener(final com.vaadin.data.Property.ValueChangeListener listener)
	{
		super.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = -9171250614559884524L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
			{
				@SuppressWarnings("unchecked")
				final Collection<Object> selectedValues = (Collection<Object>) event.getProperty().getValue();
				listener.valueChange(new com.vaadin.data.Property.ValueChangeEvent()
				{
					private static final long serialVersionUID = -2648483124690298644L;

					@Override
					public Property<Boolean> getProperty()
					{
						return new ObjectProperty<Boolean>(!selectedValues.isEmpty());
					}
				});
			}
		});
	}
	
	public boolean isSelected()
	{
		return isSelected(itemID);
	}
}