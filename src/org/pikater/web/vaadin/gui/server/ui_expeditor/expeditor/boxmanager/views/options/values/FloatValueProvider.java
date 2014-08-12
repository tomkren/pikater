package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;

public class FloatValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		IFieldContext<Float> context = getFieldContextFrom(value);
		addField("value", createNumericField("Value:", context, new IOnValueChange<Float>()
		{
			@Override
			public void valueChanged(Float number)
			{
				value.setCurrentValue(new FloatValue(number));
			}
		}));
	}
}