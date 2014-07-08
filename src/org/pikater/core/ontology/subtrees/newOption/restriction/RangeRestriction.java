package org.pikater.core.ontology.subtrees.newOption.restriction;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;

public class RangeRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9196397486608350955L;

	private ITypedValue minValeu;
	private ITypedValue maxValeu;
	
	public RangeRestriction() {}
	public RangeRestriction(ITypedValue minValeu, ITypedValue maxValeu) {
		this.minValeu = minValeu;
		this.maxValeu = maxValeu;
	}

	public ITypedValue getMinValeu() {
		return minValeu;
	}
	public void setMinValeu(ITypedValue minValeu) {
		this.minValeu = minValeu;
	}

	public ITypedValue getMaxValeu() {
		return maxValeu;
	}
	public void setMaxValeu(ITypedValue maxValeu) {
		this.maxValeu = maxValeu;
	}

	@Override
	public Type getClassName() {
		return new Type(minValeu.getClass());
	}

	@Override
	public boolean isValid() {
		
		return true;
	}

	public boolean contains(ITypedValue value) {
		
		if (value == null ||
				minValeu.getClass() != value.getClass() ) {
			return false;
		}
		
		if (value instanceof IntegerValue) {
			int min = ((IntegerValue)minValeu).getValue();
			int max = ((IntegerValue)maxValeu).getValue();
			
			int val = ((IntegerValue)value).getValue();
			
			if (min <= val && val <= max) {
				return true;
			}
		}

		//TODO: ostatni typy

		return false;
	}
	
	public RangeRestriction cloneRangeRestriction() {
		
		RangeRestriction rangeNew = new RangeRestriction();
		rangeNew.setMinValeu(minValeu.cloneValue());
		rangeNew.setMaxValeu(maxValeu.cloneValue());
		return rangeNew;
	}

}
