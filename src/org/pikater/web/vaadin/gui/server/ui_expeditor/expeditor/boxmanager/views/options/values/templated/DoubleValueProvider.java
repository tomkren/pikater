package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.templated;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.AbstractFieldProviderForValue;

public class DoubleValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		IFieldContext<Double> context = getFieldContextFrom(value); 
		addField("value", createNumericField("Value:", context, new IOnValueChange<Double>()
		{
			@Override
			public void valueChanged(Double number)
			{
				value.setCurrentValue(new DoubleValue(number));
			}
		}));
	}
}