package org.pikater.core.ontology.subtrees.newOption;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;

public class ValueType implements Concept
{
	private static final long serialVersionUID = -4658896847448815807L;

	private String className;

	private RangeRestriction rangeRestriction;
	private SetRestriction setRestriction;
	
	public ValueType() {}
	public ValueType(Class<?> classs) {
		this.className = classs.getName();
	}
	public ValueType(String className) {
		this.className = className;
	}
	public ValueType(Class<?> classs, RangeRestriction rangeRestriction) {
		this.className = classs.getName();
		this.rangeRestriction = rangeRestriction;
	}
	public ValueType(Class<?> classs, SetRestriction setRestriction) {
		this.className = classs.getName();
		this.setRestriction = setRestriction;
	}
	public ValueType(Class<?> classs, RangeRestriction rangeRestriction,
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
	
	public boolean isValid()
	{
		return rangeRestriction.isValid() && setRestriction.isValid();
	}
	
	@Override
	public ValueType clone() {
		
		ValueType typeNew = new ValueType(className);
		if (rangeRestriction != null) {
			typeNew.setRangeRestriction(rangeRestriction.clone());
		}
		if (setRestriction != null) {
			typeNew.setSetRestriction(setRestriction.clone());
		}
		return typeNew;
	}
	
}
