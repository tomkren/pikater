package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

public class StringValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		IFieldContext<String> context = getFieldContextFrom(value);
		addField("value", createTextField("Value:", context, new IOnValueChange<String>()
		{
			@Override
			public void valueChanged(String newValue)
			{
				value.setCurrentValue(new StringValue(newValue));
			}
		}));
	}
}