package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.web.vaadin.gui.server.components.forms.validators.NumberRangeValidator.NumberConstant;
import org.pikater.web.vaadin.gui.server.layouts.formlayout.FormFieldFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public abstract class AbstractFieldProviderForValue
{
	private final Map<String, AbstractField<? extends Object>> generatedFields;
	
	public AbstractFieldProviderForValue()
	{
		this.generatedFields = new HashMap<String, AbstractField<? extends Object>>();
	}
	
	public void generateFields(Value value)
	{
		generatedFields.clear();
		doGenerateFields(value);
	}
	
	public Map<String, AbstractField<? extends Object>> getGeneratedFields()
	{
		return generatedFields;
	}
	
	//--------------------------------------------------------------------------
	// MAIN FIELD CONSTRUCTION ROUTINES, CONVENIENCE INTERFACE & TYPES
	
	@SuppressWarnings("unchecked")
	protected <N extends Number & Comparable<? super N>> AbstractField<? extends Object> createNumericField(final Value value, String caption,
			final IOnValueChange<N> valueChangeHandler)
	{
		// first cast value
		final N currentValue = (N) value.getCurrentValue().hackValue();

		// create & bind with the value type
		if(value.getType().isSetRestrictionDefined())
		{
			return createEnumeratedField(value, (List<N>) getSortedEnumerationForValue(value), caption, valueChangeHandler);
		}
		else // whether range restriction is defined or not
		{
			N min = null, max = null;
			if(value.getType().isRangeRestrictionDefined())
			{
				min = (N) value.getType().getRangeRestriction().getMinValue().hackValue();
				max = (N) value.getType().getRangeRestriction().getMaxValue().hackValue();
			}
			final TextField tf_value = FormFieldFactory.getNumericField(caption, currentValue, min, max, true, false);
			tf_value.setSizeFull();
			tf_value.addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 3736100120428402858L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					NumberConstant numberParser = NumberConstant.fromNumberClass(currentValue.getClass());
					Number parsedNumber;
					try
					{
						parsedNumber = numberParser.parse(tf_value.getValue());
					}
					catch(Throwable t)
					{
						// if the field's content can not be parsed, do nothing (validation error will be displayed)
						return;
					}
					valueChangeHandler.valueChanged((N) parsedNumber); 
				}
			});
			return tf_value;
		}
	}

	@SuppressWarnings("unchecked")
	protected <O extends Object> AbstractField<? extends Object> createEnumeratedField(Value value, List<O> options, String caption,
			final IOnValueChange<O> valueChangeHandler)
	{
		// first cast value
		final Object currentValue = value.getCurrentValue().hackValue();

		// then create the field
		final ComboBox cb_value = FormFieldFactory.getGeneralComboBox(caption, options, currentValue, true, false);
		cb_value.setSizeFull();
		cb_value.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = -3938305148585892660L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				valueChangeHandler.valueChanged((O) event.getProperty().getValue());
			}
		});
		return cb_value;
	}

	@SuppressWarnings("unchecked")
	protected <O extends Object> List<O> getUnsortedEnumerationForValue(Value value)
	{
		List<O> options = new ArrayList<O>();
		for(IValueData possibleValue : value.getType().getSetRestriction().getValues())
		{
			options.add((O) possibleValue.hackValue());
		}
		return options;
	}

	@SuppressWarnings("unchecked")
	protected <OC extends Object & Comparable<? super OC>> List<OC> getSortedEnumerationForValue(Value value)
	{
		List<OC> options = (List<OC>) getUnsortedEnumerationForValue(value);
		Collections.sort(options);
		return options;
	}
	
	protected void addField(String notificationDescription, AbstractField<? extends Object> field)
	{
		generatedFields.put(notificationDescription, field);
	}
	
	protected interface IOnValueChange<O extends Object>
	{
		void valueChanged(O object);
	}

	//---------------------------------------------------
	// ABSTRACT INTERFACE
	
	protected abstract void doGenerateFields(Value value);
}