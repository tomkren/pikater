package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

public class StringValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		// first cast value
		StringValue currentValue = (StringValue) value.getCurrentValue();
		
		// then create the field & bind with the data source
		if(value.getType().isSetRestrictionDefined())
		{
			List<String> options = getSortedEnumerationForValue(value);
			addField("value", createEnumeratedField(currentValue.getValue(), options, "Value:", new IOnValueChange<String>()
			{
				@Override
				public void valueChanged(String newValue)
				{
					value.setCurrentValue(new StringValue(newValue));
				}
			}));
		}
		else // TODO: range restriction
		{
			addField("value", createGeneralTextField(currentValue.getValue(), "Value:", new IOnValueChange<String>()
			{
				@Override
				public void valueChanged(String newValue)
				{
					value.setCurrentValue(new StringValue(newValue));
				}
			}));
		}
	}
}