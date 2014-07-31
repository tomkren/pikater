package org.pikater.core.ontology.subtrees.newOption.restrictions;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.ValueType;

public class TypeRestriction implements IRestriction
{
	private static final long serialVersionUID = -135700897678377163L;
	
	private List<ValueType> types;

	public TypeRestriction() {}
	public TypeRestriction(List<ValueType> types)
	{
		setTypes(types);
	}
	
	public List<ValueType> getTypes()
	{
		return types;
	}
	public void setTypes(List<ValueType> types)
	{
		this.types = types;
	}
	
	public void addtype(ValueType type)
	{
		types.add(type);
	}
	
	@Override
	public TypeRestriction clone()
	{
		List<ValueType> typesCopied = new ArrayList<ValueType>();
		for(ValueType type : types)
		{
			typesCopied.add(type.clone());
		}
		return new TypeRestriction(typesCopied);
	}
	@Override
	public boolean isValid()
	{
		return (types != null) && !types.isEmpty();
	}
	@Override
	public boolean isValidAgainst(Object obj)
	{
		return isValid() && (obj instanceof ValueType) && types.contains((ValueType) obj);
	}
}