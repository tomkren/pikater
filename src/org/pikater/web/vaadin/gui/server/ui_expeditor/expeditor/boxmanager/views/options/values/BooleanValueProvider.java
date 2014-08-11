package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.web.vaadin.gui.server.layouts.formlayout.FormFieldFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;

public class BooleanValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(final Value value)
	{
		// first cast value
		Boolean currentValue = ((BooleanValue) value.getCurrentValue()).getValue();

		// then create the field & bind with the data source
		CheckBox chb_value = FormFieldFactory.getGeneralCheckField("value", currentValue, false);
		chb_value.setSizeFull();
		chb_value.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 3736100120428402858L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				value.setCurrentValue(new BooleanValue((Boolean) event.getProperty().getValue()));
			}
		});
		addField("value", chb_value);
	}
}