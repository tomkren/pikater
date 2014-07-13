package org.pikater.core.ontology.subtrees.newOption.restrictions;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.ValueType;

public class TypeRestriction implements Concept
{
	private static final long serialVersionUID = -135700897678377163L;
	
	private List<ValueType> types;

	public TypeRestriction() {}
	public TypeRestriction(List<ValueType> types) {
		setTypes(types);
	}
	
	public List<ValueType> getTypes() {
		return types;
	}
	public void setTypes(List<ValueType> types) {
		this.types = types;
	}
	
	@Override
	public TypeRestriction clone()
	{
		return new TypeRestriction(new ArrayList<ValueType>(types));
	}
}