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
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

/**
 * This GUI component makes a best effort at displaying the given arguments,
 * even if not valid. That way, the box can still be usable in some 
 * situations.
 */
public class OptionValueForm extends CustomFormLayout
{
	private static final long serialVersionUID = 2200291325058461983L;
	
	/**
	 * The field contained within this form that holds the current value of
	 * the given option's value type. 
	 */
	private AbstractField<? extends Object> field_value;
	
	/*
	 * Programmatic variables.
	 */
	private final BidiMap<ValueType, String> typeToDisplayString;
	private final Collection<AbstractField<Object>> typeSpecificFormFields;
	
	public OptionValueForm(Value value, TypeRestriction restriction)
	{
		super(null);
		this.field_value = null;
		this.typeToDisplayString = new BidiMap<ValueType, String>();
		this.typeSpecificFormFields = new HashSet<AbstractField<Object>>();
		
		setupFields(value, restriction);
	}
	
	@Override
	public IOnSubmit getSubmitAction()
	{
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void addValueSpecificField(String notificationDescription, AbstractField<? extends Object> field)
	{
		typeSpecificFormFields.add((AbstractField<Object>) field);
		addField(notificationDescription, field);
	}
	
	//---------------------------------------------------------------------------------------
	// ROOT METHOD TO CREATE THE FORM - CREATES A TYPE FIELD AND CALLS OTHER METHODS
	
	private void setupFields(final Value value, TypeRestriction allowedTypes)
	{
		// go through allowed types, validate, register, sort & transform into strings
		List<String> typeOptions = new ArrayList<String>();
		for(ValueType type : allowedTypes.getTypes())
		{
			/*
			 * IMPORTANT: this is assumed to check various things which are needed both here
			 * and in other methods of this class.
			 */
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
				
				if(type.isRangeRestrictionDefined())
				{
					typeStr = typeStr + "[R]";
				}
				else if(type.isSetRestrictionDefined())
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
					for(AbstractField<Object> field : typeSpecificFormFields)
					{
						removeField(field);
					}
					typeSpecificFormFields.clear();

					// then change type and reset value to default
					ValueType newlySelectedType = typeToDisplayString.getKey((String) event.getProperty().getValue());
					value.setType(newlySelectedType);
					value.setCurrentValue(newlySelectedType.getDefaultValue().clone());
					
					// and then create new fields from it
					setupOtherFields(value);
				}
			});
			addField("type", cb_type);
			
			// create and setup other fields
			setupOtherFields(value);
			
			// add a special button to reset value to default (default value of the currently selected type)
			addCustomButtonInterface(new Button("Reset value", new Button.ClickListener()
			{
				private static final long serialVersionUID = 838180535195566779L;

				@SuppressWarnings("unchecked")
				@Override
				public void buttonClick(ClickEvent event)
				{
					value.setCurrentValue(value.getType().getDefaultValue().clone());
					if(field_value.getValue() instanceof String)
					{
						((AbstractField<String>) field_value).setValue(value.getCurrentValue().hackValue().toString());
					}
					else
					{
						((AbstractField<Object>) field_value).setValue(value.getCurrentValue().hackValue());
					}
				}
			}));
		}
	}
	
	//---------------------------------------------------------------------------------------
	// SECOND-LEVEL METHOD TO CREATE THE FORM - CREATES FIELDS IN TYPE-SPECIFIC MANNER
	
	/**
	 * The {@link #setupFields(Value, TypeRestriction)} method ensures that the 
	 * {@link Value#getCurrentValue()} doesn't return null for the argument and
	 * thus, this method requires it.</br>
	 * IMPORTANT: this method is required to set the {@link #field_value} field.
	 * @param value
	 */
	private void setupOtherFields(final Value value)
	{
		// create form fields according to the given value type
		Class<? extends IValueData> typeClass = value.getCurrentValue().getClass();
		if(typeClass.equals(BooleanValue.class))
		{
			// first cast value
			Boolean currentValue = ((BooleanValue) value.getCurrentValue()).getValue();
			
			// then create the field & bind with the data source
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
			
			// and finalize
			addValueSpecificField("value", cb_value);
			field_value = cb_value;
		}
		else if(typeClass.equals(StringValue.class))
		{
			if(value.getType().isSetRestrictionDefined())
			{
				List<String> options = getSortedEnumerationForValue(value);
				field_value = setupEnumeratedField(value, options, "Value:", "value", new IOnValueChange<String>()
				{
					@Override
					public void valueChanged(String newValue)
					{
						value.setCurrentValue(new StringValue(newValue));
					}
				});
			}
			else
			{
				// first cast value
				String currentValue = ((StringValue) value.getCurrentValue()).getValue();
				
				// then create the field & bind with the data source
				TextField tf_value = FormFieldFactory.getGeneralTextField("Value:", null, currentValue, true, false);
				tf_value.setSizeFull();
				tf_value.addValueChangeListener(new Property.ValueChangeListener()
				{
					private static final long serialVersionUID = 3736100120428402858L;

					@Override
					public void valueChange(ValueChangeEvent event)
					{
						value.setCurrentValue(new StringValue((String) event.getProperty().getValue()));
					}
				});
				
				// and finalize
				addValueSpecificField("value", tf_value);
				field_value = tf_value;
			}
		}
		else if(typeClass.equals(IntegerValue.class))
		{
			field_value = setupNumericField(value, "Value:", "value", new IOnValueChange<Integer>()
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
			field_value = setupNumericField(value, "Value:", "value", new IOnValueChange<Float>()
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
			field_value = setupNumericField(value, "Value:", "value", new IOnValueChange<Double>()
			{
				@Override
				public void valueChanged(Double number)
				{
					value.setCurrentValue(new DoubleValue(number));
				}
			});
		}
		else if(typeClass.equals(NullValue.class))
		{
			// these values can not be set and hence no field is created whatsoever
		}
		else
		{
			// TODO: question mark values
			PikaterLogger.logThrowable("", new IllegalStateException(String.format("Unimplemented value type used: '%s'.", typeClass.getName())));
			MyNotifications.showWarning("Unsupported option type", typeClass.getSimpleName());
		}
	}
	
	//---------------------------------------------------------------------------------------
	// THIRD-LEVEL METHODS TO CREATE THE ACTUAL FIELDS
	
	@SuppressWarnings("unchecked")
	private <N extends Number & Comparable<? super N>> AbstractField<? extends Object> setupNumericField(final Value value, String caption,
			String notificationIdentifier, final IOnValueChange<N> valueChangeHandler)
	{
		// first cast value
		final N currentValue = (N) value.getCurrentValue().hackValue();
		
		// create & bind with the value type
		if(value.getType().isSetRestrictionDefined())
		{
			return setupEnumeratedField(value, (List<N>) getSortedEnumerationForValue(value), caption, notificationIdentifier, valueChangeHandler);
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

			// finalize
			addValueSpecificField(notificationIdentifier, tf_value);
			return tf_value;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <O extends Object> AbstractField<? extends Object> setupEnumeratedField(Value value, List<O> options, String caption,
			String notificationIdentifier, final IOnValueChange<O> valueChangeHandler)
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

		// and finalize
		addValueSpecificField(notificationIdentifier, cb_value);
		return cb_value;
	}
	
	//-------------------------------------------------------------------
	// MISCELLANEOUS FIELD RELATED INTERFACE
	
	@SuppressWarnings("unchecked")
	private <O extends Object> List<O> getUnsortedEnumerationForValue(Value value)
	{
		List<O> options = new ArrayList<O>();
		for(IValueData possibleValue : value.getType().getSetRestriction().getValues())
		{
			options.add((O) possibleValue.hackValue());
		}
		return options;
	}
	
	@SuppressWarnings("unchecked")
	private <OC extends Object & Comparable<? super OC>> List<OC> getSortedEnumerationForValue(Value value)
	{
		List<OC> options = (List<OC>) getUnsortedEnumerationForValue(value);
		Collections.sort(options);
		return options;
	}
	
	//-------------------------------------------------------------------
	// PRIVATE TYPES
		
	private interface IOnValueChange<O extends Object>
	{
		void valueChanged(O object);
	}
}