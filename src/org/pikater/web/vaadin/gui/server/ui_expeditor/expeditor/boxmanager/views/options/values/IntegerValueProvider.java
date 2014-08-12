package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class IntegerValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		IFieldContext<Integer> context = getFieldContextFrom(value);
		addField("value", createNumericField("Value:", context, new IOnValueChange<Integer>()
		{
			@Override
			public void valueChanged(Integer number)
			{
				value.setCurrentValue(new IntegerValue(number));
			}
		}));
	}
}