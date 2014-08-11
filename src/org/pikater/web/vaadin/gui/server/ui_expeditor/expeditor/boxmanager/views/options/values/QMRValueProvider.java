package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class QMRValueProvider extends AbstractFieldProviderForValue
{
	@Override
	protected void doGenerateFields(Value value)
	{
		// first cast value
		final QuestionMarkRange currentValue = (QuestionMarkRange) value.getCurrentValue();

		// then create the min & max fields
		addRangeField("minimum", "Minimum:", currentValue.getMin(), new IOnValueChange<IComparableValueData>()
		{
			@Override
			public void valueChanged(IComparableValueData newValue)
			{
				currentValue.setMin(newValue);
			}
		});
		addRangeField("maximum", "Maximum:", currentValue.getMax(), new IOnValueChange<IComparableValueData>()
		{
			@Override
			public void valueChanged(IComparableValueData newValue)
			{
				currentValue.setMax(newValue);
			}
		});
		
		// and finally, create the attempts field
		Value fictionalValue = new Value(
				new IntegerValue(currentValue.getCountOfValuesToTry()),
				new RangeRestriction(new IntegerValue(1), null) // let agents handle the case where we have less integers than attempts (for the sake of abstraction)
		);
		addField("attempts", createNumericField(fictionalValue, "Attempts:", new IOnValueChange<Integer>()
		{
			@Override
			public void valueChanged(Integer newValue)
			{
				currentValue.setCountOfValuesToTry(newValue);
			}
		}));
	}
	
	private void addRangeField(String notificationIdentification, String caption, IValueData rangeValue, final IOnValueChange<IComparableValueData> valueChangeHandler)
	{
		// TODO: apply range restrictions to fictional values
		
		Class<? extends IValueData> typeClass = rangeValue.getClass();
		if(typeClass.equals(IntegerValue.class))
		{
			Value fictionalValue = new Value(rangeValue); // a mega hack
			addField(notificationIdentification, createNumericField(fictionalValue, caption, new IOnValueChange<Integer>()
			{
				@Override
				public void valueChanged(Integer newValue)
				{
					valueChangeHandler.valueChanged(new IntegerValue(newValue));
				}
			}));
		}
		else if(typeClass.equals(FloatValue.class))
		{
			Value fictionalValue = new Value(rangeValue); // a mega hack
			addField(notificationIdentification, createNumericField(fictionalValue, caption, new IOnValueChange<Float>()
			{
				@Override
				public void valueChanged(Float newValue)
				{
					valueChangeHandler.valueChanged(new FloatValue(newValue));
				}
			}));
		}
		else if(typeClass.equals(DoubleValue.class))
		{
			Value fictionalValue = new Value(rangeValue); // a mega hack
			addField(notificationIdentification, createNumericField(fictionalValue, caption, new IOnValueChange<Double>()
			{
				@Override
				public void valueChanged(Double newValue)
				{
					valueChangeHandler.valueChanged(new DoubleValue(newValue));
				}
			}));
		}
		else if(typeClass.equals(StringValue.class))
		{
			StringValue currentRangeValue = (StringValue) rangeValue;
			addField(notificationIdentification, createGeneralTextField(currentRangeValue.getValue(), caption, new IOnValueChange<String>()
			{
				@Override
				public void valueChanged(String newValue)
				{
					valueChangeHandler.valueChanged(new StringValue(newValue));
				}
			}));
		}
		else
		{
			throw new IllegalStateException(String.format("Unknown '%s' child: '%s'", 
					IComparableValueData.class.getSimpleName(), typeClass.getName()));
		}
	}
}