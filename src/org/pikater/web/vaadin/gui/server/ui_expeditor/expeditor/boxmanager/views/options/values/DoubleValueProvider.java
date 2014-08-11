package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;

public class DoubleValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		addField("value", createNumericField(value, "Value:", new IOnValueChange<Double>()
		{
			@Override
			public void valueChanged(Double number)
			{
				value.setCurrentValue(new DoubleValue(number));
			}
		}));
	}
}