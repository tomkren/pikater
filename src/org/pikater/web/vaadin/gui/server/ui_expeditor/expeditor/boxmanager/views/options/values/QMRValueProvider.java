package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.web.vaadin.gui.server.layouts.formlayout.FormFieldFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;

public class QMRValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(Value value)
	{
		// first cast value
		QuestionMarkRange currentValue = (QuestionMarkRange) value.getCurrentValue();

		// create the minimum field
		/*
		if(currentValue.getm)
		addField("minimum", createNumericField(, caption, valueChangeHandler)
		
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
		*/
	}
}