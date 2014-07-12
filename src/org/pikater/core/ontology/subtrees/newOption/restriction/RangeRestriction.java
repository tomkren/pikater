package org.pikater.core.ontology.subtrees.newOption.restriction;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;

public class RangeRestriction implements IRestriction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9196397486608350955L;

	private ITypedValue minValue;
	private ITypedValue maxValue;
	
	public RangeRestriction() {}
	public RangeRestriction(ITypedValue minValue, ITypedValue maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public ITypedValue getMinValue() {
		return minValue;
	}
	public void setMinValue(ITypedValue minValue) {
		this.minValue = minValue;
	}

	public ITypedValue getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(ITypedValue maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public Type getType() {
		return new Type(minValue.getClass());
	}

	@Override
	public boolean isValid() {
		
		return true;
	}

	public boolean contains(ITypedValue value) {
		
		if (value == null ||
				minValue.getClass() != value.getClass() ) {
			return false;
		}
		
		if (value instanceof IntegerValue) {
			int min = ((IntegerValue)minValue).getValue();
			int max = ((IntegerValue)maxValue).getValue();
			
			int val = ((IntegerValue)value).getValue();
			
			if (min <= val && val <= max) {
				return true;
			}
		}

		//TODO: ostatni typy

		return false;
	}
	
	@Override
	public RangeRestriction clone() {
		
		RangeRestriction rangeNew = new RangeRestriction();
		rangeNew.setMinValue(minValue.cloneValue());
		rangeNew.setMaxValue(maxValue.cloneValue());
		return rangeNew;
	}
}
