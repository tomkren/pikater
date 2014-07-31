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

	private boolean nullable;
	private List<IValueData> values;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public SetRestriction() {}
	@SuppressWarnings("unchecked")
	public SetRestriction(boolean nullable, List<? extends IValueData> values)
	{
		this.nullable = nullable;
		this.values = (List<IValueData>) values;
	}

	public boolean isNullable()
	{
		return nullable;
	}
	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
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
	
	/* -------------------------------------------------------------
	 * CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	 * - generate again when you change local fields or their types
	 * - required in {@link org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (nullable ? 1231 : 1237);
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SetRestriction other = (SetRestriction) obj;
		if (nullable != other.nullable)
			return false;
		if (values == null)
		{
			if (other.values != null)
				return false;
		}
		else if (!values.equals(other.values))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public SetRestriction clone()
	{
		List<IValueData> valuesCopied = new ArrayList<IValueData>();
		for(IValueData value : values)
		{
			valuesCopied.add(value.clone());
		}
		return new SetRestriction(nullable, valuesCopied);
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