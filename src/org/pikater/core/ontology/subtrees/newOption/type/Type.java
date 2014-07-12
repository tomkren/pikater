package org.pikater.core.ontology.subtrees.newOption.type;

import org.pikater.core.ontology.subtrees.newOption.restriction.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;


public class Type implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4658896847448815807L;

	private String className;

	private RangeRestriction rangeRestriction;
	private SetRestriction setRestriction;
	
	public Type() {}
	public Type(Class<?> classs) {
		this.className = classs.getName();
	}
	public Type(String className) {
		this.className = className;
	}
	public Type(Class<?> classs, RangeRestriction rangeRestriction) {
		this.className = classs.getName();
		this.rangeRestriction = rangeRestriction;
	}
	public Type(Class<?> classs, SetRestriction setRestriction) {
		this.className = classs.getName();
		this.setRestriction = setRestriction;
	}
	public Type(Class<?> classs, RangeRestriction rangeRestriction,
			SetRestriction setRestriction) {
		this.className = classs.getName();
		this.rangeRestriction = rangeRestriction;
		this.setRestriction = setRestriction;
	}
	
	public String getClassName()
	{
		return className;
	}

	public RangeRestriction getRangeRestriction() {
		return rangeRestriction;
	}
	public void setRangeRestriction(RangeRestriction rangeRestriction) {
		this.rangeRestriction = rangeRestriction;
	}

	public SetRestriction getSetRestriction() {
		return setRestriction;
	}
	public void setSetRestriction(SetRestriction setRestriction) {
		this.setRestriction = setRestriction;
	}
	@Override
	public boolean isValid() {

		boolean rangeOk = rangeRestriction.isValid();
		boolean setOk = setRestriction.isValid();

		return rangeOk && setOk;
	}

	@Override
	public Type getType() {

		return new Type(className);
	}

	@Override
	public Type clone() {
		
		Type typeNew = new Type(className);
		if (rangeRestriction != null) {
			typeNew.setRangeRestriction(rangeRestriction.clone());
		}
		if (setRestriction != null) {
			typeNew.setSetRestriction(setRestriction.clone());
		}
		return typeNew;
	}
	
}
