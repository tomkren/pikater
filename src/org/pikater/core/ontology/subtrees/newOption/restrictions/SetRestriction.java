package org.pikater.core.ontology.subtrees.newOption.restrictions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class SetRestriction implements IRestriction
{
	private static final long serialVersionUID = 611963641797046162L;

	private List<IValueData> values;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public SetRestriction() {}
	public SetRestriction(List<IValueData> values)
	{
		this.values = values;
	}

	public List<IValueData> getValues()
	{
		return values;
	}
	public void setValues(List<IValueData> values)
	{
		this.values = values;
	}
	public void addValues(List<IValueData> values)
	{
		this.values.addAll(values);
	}
	
	@Override
	public SetRestriction clone()
	{
		List<IValueData> valuesCopied = new ArrayList<IValueData>();
		for(IValueData value : values)
		{
			valuesCopied.add(value.clone());
		}
		return new SetRestriction(valuesCopied);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid()
	{
		if((values == null) || values.isEmpty())
		{
			return false;
		}
		else
		{
			// check if all values are instances of same class
			Set<Class<IValueData>> types = new HashSet<Class<IValueData>>();
			for(IValueData value : values)
			{
				types.add((Class<IValueData>) value.getClass());
			}
			types.remove(NullValue.class);
			return types.size() < 2;
		}
	}

	@Override
	public boolean isValidAgainst(Object obj)
	{
		if(isValid() && values.get(0).getClass().equals(obj.getClass()))
		{
			if(obj instanceof NullValue)
			{
				return true;
			}
			else if(obj instanceof IComparableValueData)
			{
				IComparableValueData valueComp = (IComparableValueData) obj;
				for(IValueData val : values)
				{
					if(valueComp.compareTo((IComparableValueData) val) == 0)
					{
						return true;
					}
				}
				return false; 
			}
		}
		return false;
	}
}