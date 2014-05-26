package org.pikater.core.ontology.subtrees.newOption.restriction;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class RangeRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9196397486608350955L;

	private IValue minValeu;
	private IValue maxValeu;
	
	public RangeRestriction() {}
	public RangeRestriction(IValue minValeu, IValue maxValeu) {
		this.minValeu = minValeu;
		this.maxValeu = maxValeu;
	}

	public IValue getMinValeu() {
		return minValeu;
	}
	public void setMinValeu(IValue minValeu) {
		this.minValeu = minValeu;
	}

	public IValue getMaxValeu() {
		return maxValeu;
	}
	public void setMaxValeu(IValue maxValeu) {
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

	public boolean contains(IValue value) {
		
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

}
