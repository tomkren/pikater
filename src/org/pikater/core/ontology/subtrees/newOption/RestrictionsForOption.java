package org.pikater.core.ontology.subtrees.newOption;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.ICloneable;
import org.pikater.core.ontology.subtrees.newOption.base.IValidated;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;

public class RestrictionsForOption implements Concept, ICloneable, IValidated 
{
	private static final long serialVersionUID = 236577012228824852L;
	
	private List<TypeRestriction> restrictions;
	
	/**
	 * Should only be used internally and by JADE.
	 */
	@Deprecated
	public RestrictionsForOption()
	{
		this.restrictions = new ArrayList<TypeRestriction>();
	}
	public RestrictionsForOption(List<TypeRestriction> restrictions)
	{
		this();
		this.restrictions.addAll(restrictions);
	}

	public List<TypeRestriction> getRestrictions()
	{
		return restrictions;
	}
	public void setRestrictions(List<TypeRestriction> restrictions)
	{
		this.restrictions = restrictions;
	}
	
	/*
	 * Some convenience interface.
	 */
	public TypeRestriction fetchByIndex(int index)
	{
		return restrictions.get(index);
	}
	public void add(TypeRestriction restriction)
	{
		this.restrictions.add(restriction);
	}
	public int size()
	{
		return restrictions.size();
	}
	
	/*
	 * Inherited interface.
	 */
	@Override
	public RestrictionsForOption clone() throws CloneNotSupportedException
	{
		RestrictionsForOption result = (RestrictionsForOption) super.clone();
		for(TypeRestriction restriction : restrictions)
		{
			result.add(restriction.clone());
		}
		return result;
	}
	@Override
	public boolean isValid()
	{
		for(TypeRestriction restriction : restrictions)
		{
			if(!restriction.isValid())
			{
				return false;
			}
		}
		return true;
	}
}