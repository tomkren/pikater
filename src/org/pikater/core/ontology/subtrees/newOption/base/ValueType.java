package org.pikater.core.ontology.subtrees.newOption.base;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;

public class ValueType implements Concept
{
	private static final long serialVersionUID = -4658896847448815807L;

	private String className;
	private RangeRestriction rangeRestriction;
	private SetRestriction setRestriction;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public ValueType()
	{
	}
	public ValueType(Class<?> classs)
	{
		this(classs, null, null);
	}
	public ValueType(Class<?> classs, RangeRestriction rangeRestriction)
	{
		this(classs, rangeRestriction, null);
	}
	public ValueType(Class<?> classs, SetRestriction setRestriction)
	{
		this(classs, null, setRestriction);
	}
	public ValueType(Class<?> classs, RangeRestriction rangeRestriction, SetRestriction setRestriction)
	{
		this.className = classs.getName();
		this.rangeRestriction = rangeRestriction;
		this.setRestriction = setRestriction;
	}
	
	public String getClassName()
	{
		return className;
	}
	public void setClassName(String className)
	{
		this.className = className;
	}
	public RangeRestriction getRangeRestriction()
	{
		return rangeRestriction;
	}
	public void setRangeRestriction(RangeRestriction rangeRestriction)
	{
		this.rangeRestriction = rangeRestriction;
	}
	public SetRestriction getSetRestriction()
	{
		return setRestriction;
	}
	public void setSetRestriction(SetRestriction setRestriction)
	{
		this.setRestriction = setRestriction;
	}
	
	public Class<?> getTypeClass()
	{
		try
		{
			return Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			// can not happen with the current implementation, but still:
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isValid()
	{
		return rangeRestriction.isValid() && setRestriction.isValid();
	}
	
	@Override
	public ValueType clone()
	{
		return new ValueType(
				getTypeClass(),
				rangeRestriction != null ? rangeRestriction.clone() : null,
				setRestriction != null ? setRestriction.clone() : null
		);
	}
	
	@Override
	public String toString()
	{
		try
		{
			String result = getTypeClass().newInstance().toString();
			if(rangeRestriction != null)
			{
				result = result + "[R]";
			}
			if(setRestriction != null)
			{
				result = result + "[S]";
			}
			return result;
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
