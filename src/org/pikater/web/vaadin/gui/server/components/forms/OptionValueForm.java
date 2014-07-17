package org.pikater.web.vaadin.gui.server.components.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.util.collections.BidiMap;
import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;
import org.pikater.web.vaadin.gui.server.components.forms.validators.NumberRangeValidator.NumberConstant;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

/**
 * This GUI component makes a best effort at displaying the given arguments,
 * even if not valid. That way, the box can still be usable in some 
 * situations.
 */
public class OptionValueForm extends CustomFormLayout
{
	private static final long serialVersionUID = 2200291325058461983L;
	
	private final BidiMap<ValueType, String> typeToDisplayString;
	private final Collection<AbstractField<Object>> typeSpecificFormFields;
	
	public OptionValueForm(Value value, TypeRestriction restriction)
	{
		super(null);
		this.typeToDisplayString = new BidiMap<ValueType, String>();
		this.typeSpecificFormFields = new HashSet<AbstractField<Object>>();
		
		setupFields(value, restriction);
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void setupFields(final Value value, TypeRestriction allowedTypes)
	{
		// go through allowed types, validate, register, sort & transform into strings
		List<String> typeOptions = new ArrayList<String>();
		for(ValueType type : allowedTypes.getTypes())
		{
			if(type.isValid())
			{
				String typeStr = null;
				try
				{
					typeStr = type.getDefaultValue().getClass().newInstance().toDisplayName();
				}
				catch (Throwable t)
				{
					PikaterLogger.logThrowable(String.format("Could not transform the '%s' value type to display string.", 
							type.getDefaultValue().getClass()), t);
					continue;
				}
				
				if(type.getRangeRestriction() != null)
				{
					typeStr = typeStr + "[R]";
				}
				if(type.getSetRestriction() != null)
				{
					typeStr = typeStr + "[S]";
				}
				
				if(typeToDisplayString.containsValue(typeStr))
				{
					PikaterLogger.logThrowable("Duplicate type detected.", new IllegalStateException());
				}
				else
				{
					this.typeToDisplayString.put(type, typeStr);
					typeOptions.add(typeStr);
				}
			}
		}
		Collections.sort(typeOptions);
		
		// display a warning to the user if there was a problem with the allowed types
		if(typeOptions.isEmpty())
		{
			MyNotifications.showError("No types defined or all invalid", "Please, contact the admins.");
		}
		else if(typeOptions.size() != allowedTypes.getTypes().size()) // a type is missing
		{
			MyNotifications.showWarning("Some types were filtered out", "If this is a bug, contact admins.");
		}
		else
		{
			String selectedType;
			if((value.getType() != null) && typeToDisplayString.containsKey(value.getType())) // value has defined type and it is a known type in restrictions
			{
				selectedType = typeToDisplayString.getValue(value.getType());
				if(value.getCurrentValue() == null)
				{
					value.setCurrentValue(value.getType().getDefaultValue().clone());
				}
			}
			else // auto-correct accidental invalid type binding
			{
				selectedType = typeOptions.get(0);
				value.setType(typeToDisplayString.getKey(selectedType));
				value.setCurrentValue(typeToDisplayString.getKey(selectedType).getDefaultValue().clone());
			}
			// at this point, current value needs to be set (non-null) for the 'value' argument
			
			// create type combobox and set it up
			ComboBox cb_type = FormFieldFactory.getGeneralComboBox(
					"Type:",
					typeOptions,
					selectedType,
					true,
					false
			);
			cb_type.setSizeFull();
			cb_type.addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 3736100120428402858L;

				/**
				 * Reloads type specific forms fields.
				 */
				@Override
				public void valueChange(ValueChangeEvent event)
				{
					// first get rid of the old fields
					for(AbstractField<Object> field : typeSpecificFormFields) // TODO: actually use this collection in other methods
					{
						removeField(field);
					}
					typeSpecificFormFields.clear();

					// then change type
					String newlySelectedType = (String) event.getProperty().getValue();
					value.setType(typeToDisplayString.getKey(newlySelectedType));
					value.setCurrentValue(typeToDisplayString.getKey(newlySelectedType).getDefaultValue().clone()); // don't forget this
					
					// and then create new fields from it
					setupOtherFields(value);
				}
			});
			addField("type", cb_type);
			
			// create and setup other fields
			setupOtherFields(value);
		}
	}
	
	/**
	 * The {@link #setupFields(Value, TypeRestriction)} method ensures that the 
	 * {@link Value#getCurrentValue()} doesn't return null for the argument and
	 * thus, this method requires it.
	 * @param value
	 */
	private void setupOtherFields(final Value value)
	{
		// create value related form fields according to its type
		Class<? extends IValueData> typeClass = value.getCurrentValue().getClass();
		if(typeClass.equals(NullValue.class))
		{
			// these values can not be set and hence no field is created whatsoever
		}
		else if(typeClass.equals(BooleanValue.class))
		{
			// cast value
			Boolean currentValue = ((BooleanValue) value.getCurrentValue()).getValue();
			
			// create the field & bind with the data source
			ComboBox cb_value = FormFieldFactory.getGeneralCheckField("Value:", currentValue, true, false);
			cb_value.setSizeFull();
			cb_value.addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 3736100120428402858L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					value.setCurrentValue(new BooleanValue((Boolean) event.getProperty().getValue()));
				}
			});
			
			// finalize
			addField("value", cb_value);
		}
		
		else if(typeClass.equals(QuestionMarkRange.class))
		{
			// TODO: range can be set by user or is it fixed?
			
		}
		else if(typeClass.equals(QuestionMarkSet.class))
		{
			// TODO: set can be edited by user or is it fixed?
			
		}
		else
		{
			setupNumericFieldFromValueType(typeClass, value, "Value:", "value");
		}
	}
	
	private void setupNumericFieldFromValueType(Class<? extends IValueData> typeClass, final Value value, String caption,
			String notificationIdentifier)
	{
		if(typeClass.equals(IntegerValue.class))
		{
			setupNumericField(value, caption, notificationIdentifier, new IOnNumericValueChange<Integer>()
			{
				@Override
				public void valueChanged(Integer number)
				{
					value.setCurrentValue(new IntegerValue(number));
				}
			});
		}
		else if(typeClass.equals(FloatValue.class))
		{
			setupNumericField(value, caption, notificationIdentifier, new IOnNumericValueChange<Float>()
			{
				@Override
				public void valueChanged(Float number)
				{
					value.setCurrentValue(new FloatValue(number));
				}
			});
		}
		else if(typeClass.equals(DoubleValue.class))
		{
			setupNumericField(value, caption, notificationIdentifier, new IOnNumericValueChange<Double>()
			{
				@Override
				public void valueChanged(Double number)
				{
					value.setCurrentValue(new DoubleValue(number));
				}
			});
		}
		else
		{
			throw new IllegalStateException(String.format("Unknown value type: '%s'", typeClass.getName()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private <N extends Number & Comparable<? super N>> void setupNumericField(final Value value, String caption,
			String notificationIdentifier, final IOnNumericValueChange<N> valueChangeHandler)
	{
		// cast value
		final N currentValue = (N) value.getCurrentValue().getValue();

		// create & bind with the value type
		if(value.getType().getSetRestriction() != null)
		{
			List<N> options = new ArrayList<N>();
			for(IValueData possibleValue : value.getType().getSetRestriction().getValues())
			{
				options.add((N) possibleValue.getValue());
			}
			Collections.sort(options);
			
			final ComboBox cb_value = FormFieldFactory.getGeneralComboBox(caption, options, currentValue, true, false);
			cb_value.setSizeFull();
			cb_value.addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = -3938305148585892660L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					valueChangeHandler.valueChanged((N) event.getProperty().getValue());
				}
			});

			// finalize
			addField(notificationIdentifier, cb_value);
		}
		else // whether range restriction is defined or not
		{
			N min = null, max = null;
			if(value.getType().getRangeRestriction() != null)
			{
				min = (N) value.getType().getRangeRestriction().getMinValue().getValue();
				max = (N) value.getType().getRangeRestriction().getMaxValue().getValue();
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

			// finalize
			addField(notificationIdentifier, tf_value);
		}
	}

	@Override
	public IOnSubmit getSubmitAction()
	{
		return null;
	}
	
	//-------------------------------------------------------------------
	// PRIVATE TYPES
	
	private interface IOnNumericValueChange<N extends Number & Comparable<? super N>>
	{
		void valueChanged(N number);
	}
}