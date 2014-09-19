package org.pikater.core.ontology.subtrees.newOption.restrictions;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.ValueType;

public class TypeRestriction implements IRestriction
{
	private static final long serialVersionUID = -135700897678377163L;
	
	private List<ValueType> types;

	/**
	 * Should only be used internally and by Jade.
	 */
	@Deprecated
	public TypeRestriction()
	{
		this.types = new ArrayList<ValueType>();
	}
	public TypeRestriction(List<ValueType> types)
	{
		this();
		this.types.addAll(types);
	}
	
	public List<ValueType> getTypes()
	{
		return types;
	}
	public void setTypes(List<ValueType> types)
	{
		this.types.clear();
		this.types.addAll(types);
	}
	
	/*
	 * Some convenience interface.
	 */
	public void addType(ValueType type)
	{
		types.add(type);
	}
	public boolean isValidAgainst(Object obj)
	{
		return isValid() && (obj instanceof ValueType) && types.contains((ValueType) obj);
	}
	
	/*
	 * Inherited interface.
	 */
	@Override
	public TypeRestriction clone() throws CloneNotSupportedException
	{
		TypeRestriction result = (TypeRestriction) super.clone();
		for(ValueType type : types)
		{
			result.addType(type.clone());
		}
		return result;
	}
	@Override
	public boolean isValid()
	{
		return (types != null) && !types.isEmpty();
	}
}