package org.pikater.web.vaadin.gui.server.components.forms.validators;

import com.vaadin.data.Validator;

public class NumberRangeValidator<N extends Number> implements Validator
{
	private static final long serialVersionUID = 3351367465515564187L;
	
	public static enum NumberConstant implements Validator
	{
		INTEGER("an integer", Integer.MIN_VALUE, Integer.MAX_VALUE),
		FLOAT("a float", Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY),
		DOUBLE("a double", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		
		private final String errorDisplayString;
		private Number min;
		private Number max;
		
		private NumberConstant(String errorDisplayString, Number min, Number max)
		{
			this.errorDisplayString = errorDisplayString;
			this.min = min;
			this.max = max;
		}
		
		public void setMin(Number min)
		{
			this.min = min;
		}
		
		public void setMax(Number max)
		{
			this.max = max;
		}
		
		public Number parse(String value) throws NumberFormatException
		{
			switch(this)
			{
				case DOUBLE:
					return Double.parseDouble(value);
				case FLOAT:
					return Float.parseFloat(value);
				case INTEGER:
					return Integer.parseInt(value);
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public void validate(Object value) throws InvalidValueException
		{
			Number num;
			try
			{
				num = parse((String) value);
			}
			catch(Throwable t)
			{
				throw new InvalidValueException("Not " + errorDisplayString);
			}
			
			if((min.doubleValue() > num.doubleValue()) || (num.doubleValue() > max.doubleValue()))
			{
				throw new InvalidValueException("Not " + errorDisplayString + " between " +
						min.toString() + " and " + max.toString());
			}
		}
		
		public static NumberConstant fromNumberClass(Class<? extends Number> clazz)
		{
			if(clazz.equals(Integer.class))
			{
				return INTEGER;
			}
			else if(clazz.equals(Float.class))
			{
				return FLOAT;
			}
			else if(clazz.equals(Double.class))
			{
				return DOUBLE;
			}
			else
			{
				throw new IllegalArgumentException("Unknown number type: " + clazz.getName());
			}
		}
	}
	
	private final NumberConstant thisNumber;
	
	public NumberRangeValidator(Class<? extends Number> numberClass, N min, N max)
	{
		this.thisNumber = NumberConstant.fromNumberClass(numberClass);
		if(min != null)
		{
			this.thisNumber.setMin(min);
		}
		if(max != null)
		{
			this.thisNumber.setMax(max);
		}
	}
	
	@Override
	public void validate(Object value) throws InvalidValueException
	{
		thisNumber.validate(value);
	}
}