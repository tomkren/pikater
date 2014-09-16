package org.pikater.web.vaadin.gui.server.components.forms.validators;

import java.util.Collection;
import java.util.HashSet;

import com.vaadin.data.Validator;

public class NumberRangeValidator<N extends Number> implements Validator
{
	private static final long serialVersionUID = 3351367465515564187L;
	
	private final NumberConstant thisNumber;
	private final Collection<IBoundsProvider> boundsProviders;
	
	public NumberRangeValidator(Class<? extends Number> numberClass, IBoundsProvider boundsProvider)
	{
		this.thisNumber = NumberConstant.fromNumberClass(numberClass);
		this.boundsProviders = new HashSet<IBoundsProvider>();
		addBoundsProvider(boundsProvider);
	}
	
	public NumberRangeValidator(Class<? extends Number> numberClass, final N min, final N max)
	{
		this(numberClass, new IBoundsProvider()
		{
			@Override
			public Number getMin()
			{
				return min;
			}

			@Override
			public Number getMax()
			{
				return max;
			}
		});
	}
	
	public void addBoundsProvider(IBoundsProvider boundsProvider)
	{
		this.boundsProviders.add(boundsProvider);
	}
	
	@SuppressWarnings("unchecked")
	public N parse(String value) throws InvalidValueException
	{
		try
		{
			return (N) thisNumber.parse(value);
		}
		catch(Exception t)
		{
			throw new InvalidValueException("Not " + thisNumber.errorDisplayString);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object value) throws InvalidValueException
	{
		N min = (N) this.thisNumber.absoluteMin;
		N max = (N) this.thisNumber.absoluteMax;
		for(IBoundsProvider boundsProvider : boundsProviders)
		{
			if((boundsProvider.getMin() != null) && (boundsProvider.getMin().doubleValue() > min.doubleValue()))
			{
				min = (N) boundsProvider.getMin();
			}
			if((boundsProvider.getMax() != null) && (boundsProvider.getMax().doubleValue() < max.doubleValue()))
			{
				max = (N) boundsProvider.getMax();
			}
		}
		
		N num = parse((String) value);
		if((min.doubleValue() > num.doubleValue()) || (num.doubleValue() > max.doubleValue()))
		{
			throw new InvalidValueException("Not " + thisNumber.errorDisplayString + " between " +
					min.toString() + " and " + max.toString());
		}
	}
	
	//--------------------------------------------------
	// SPECIAL TYPES
	
	public enum NumberConstant
	{
		INTEGER("an integer", Integer.MIN_VALUE, Integer.MAX_VALUE),
		FLOAT("a float", Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY),
		DOUBLE("a double", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		
		public final String errorDisplayString;
		public final Number absoluteMin;
		public final Number absoluteMax;
		
		private NumberConstant(String errorDisplayString, Number min, Number max)
		{
			this.errorDisplayString = errorDisplayString;
			this.absoluteMin = min;
			this.absoluteMax = max;
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
}