package org.pikater.shared.experiment.universalformat;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.core.ontology.subtrees.batchDescription.ErrorDescription;
import org.pikater.core.ontology.subtrees.option.Option;

public class UniversalOntology
{
	private Class<?> type;

	private final Collection<UniversalConnector> inputSlots;
	private final Collection<ErrorDescription> errors;

	private final Collection<Option> options;
	
	public UniversalOntology()
	{
		this.inputSlots = new ArrayList<UniversalConnector>();
		this.errors = new ArrayList<ErrorDescription>();
		this.options = new ArrayList<Option>();
	}
	
	public Class<?> getType()
	{
		return type;
	}
	
	public void setType(Class<?> type)
	{
		this.type = type;
	}

    public Collection<Option> getOptions()
    {
		return options;
	}
    
	public void setOptions(Collection<Option> options)
	{
		if (options == null) {
			throw new NullPointerException("Argument options can't be null");
		}
		this.options.clear();
		this.options.addAll(options);
	}
	
	public Option getOptionByName(String name) {
		
		for (Option optionI : getOptions()) {
			if (optionI.getName().equals(name)) {
				return optionI;
			}
		}
		return null;
	} 
    
	public Collection<ErrorDescription> getErrors()
	{
		return errors;
	}
	
	public void setErrors(Collection<ErrorDescription> errors)
	{
		this.errors.clear();
		this.errors.addAll(errors);
	}

	public void setErrors(jade.util.leap.ArrayList errors)
	{
		if (errors == null)
		{
			this.errors.clear();
		}
		else
		{
			for (int i = 0; i < errors.size(); i++)
			{
				this.errors.add( (ErrorDescription) errors.get(i) );
			}
		}
	}
    
    public Collection<UniversalConnector> getInputSlots()
    {
    	return inputSlots;
    }

	public void addInputSlot(UniversalConnector connector)
	{
		inputSlots.add(connector);
	}
}