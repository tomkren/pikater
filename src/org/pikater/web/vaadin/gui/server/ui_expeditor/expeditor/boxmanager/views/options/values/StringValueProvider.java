package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.web.vaadin.gui.server.layouts.formlayout.FormFieldFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.TextField;

public class StringValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		if(value.getType().isSetRestrictionDefined())
		{
			List<String> options = getSortedEnumerationForValue(value);
			addField("value", createEnumeratedField(value, options, "Value:", new IOnValueChange<String>()
			{
				@Override
				public void valueChanged(String newValue)
				{
					value.setCurrentValue(new StringValue(newValue));
				}
			}));
		}
		else
		{
			// first cast value
			String currentValue = ((StringValue) value.getCurrentValue()).getValue();
			
			// then create the field & bind with the data source
			TextField tf_value = FormFieldFactory.getGeneralTextField("Value:", null, currentValue, true, false);
			tf_value.setSizeFull();
			tf_value.addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 3736100120428402858L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					value.setCurrentValue(new StringValue((String) event.getProperty().getValue()));
				}
			});
			
			// and finalize
			addField("value", tf_value);
		}
	}
}