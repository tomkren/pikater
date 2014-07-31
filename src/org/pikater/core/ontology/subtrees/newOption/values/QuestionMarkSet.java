package org.pikater.core.ontology.subtrees.newOption.values;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValidatedValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class QuestionMarkSet implements IValidatedValueData
{
	private static final long serialVersionUID = 4434448192843651477L;

	private int countOfValuesToTry;
	private List<IValueData> values;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkSet() {}
	public QuestionMarkSet(List<IValueData> values, int countOfValuesToTry)
	{
		this.values = values;
		this.countOfValuesToTry = countOfValuesToTry;
	}
	
	public int getCountOfValuesToTry()
	{
		return countOfValuesToTry;
	}
	public void setCountOfValuesToTry(int countOfValuesToTry)
	{
		this.countOfValuesToTry = countOfValuesToTry;
	}
	public List<IValueData> getValues()
	{
		return values;
	}
	public void setValues(List<IValueData> values)
	{
		this.values = values;
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
		result = prime * result + countOfValuesToTry;
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
		QuestionMarkSet other = (QuestionMarkSet) obj;
		if (countOfValuesToTry != other.countOfValuesToTry)
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
	public Object hackValue()
	{
		return null;
	}
	@Override
	public IValueData clone()
	{
		List<IValueData> valuesCopied = new ArrayList<IValueData>();
		for(IValueData value : values)
		{
			valuesCopied.add(value.clone());
		}
		return new QuestionMarkSet(valuesCopied, countOfValuesToTry);
	}
	@Override
	public String exportToWeka()
	{
		return "?";
	}
	@Override
	public String toDisplayName()
	{
		return "QuestionMarkSet";
	}
	@Override
	public boolean isValid()
	{
		if((values == null) || values.isEmpty())
		{
			return false; 
		}
		return (countOfValuesToTry > 0) && (countOfValuesToTry <= values.size());
	}
}