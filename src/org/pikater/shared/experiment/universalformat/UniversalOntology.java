package org.pikater.shared.experiment.universalformat;

import java.util.Collection;

import org.pikater.core.ontology.subtrees.description.ErrorDescription;
import org.pikater.core.ontology.subtrees.option.Option;

public class UniversalOntology {

	private Class<?> type;

	private java.util.List<UniversalConnector> inputSlots;
	private java.util.List<ErrorDescription> errors;

	private java.util.List<Option> options;

	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}

    public java.util.List<Option> getOptions() {
		return options;
	}
	public void setOptions(java.util.List<Option> options) {
		this.options = options;
	}
	public void setOptions(jade.util.leap.ArrayList options) {
		
    	if (options == null) {
    		this.options = null;
    		return;
    	}

		for (int i = 0; i <  options.size(); i++) {
			this.addOption( (Option) options.get(i) );
		}
	}
    public void addOption(Option option) {
    	
    	if (this.options == null) {
    		this.options = new java.util.ArrayList<Option>();
    	}
    	options.add(option);
	}

    
	public java.util.List<ErrorDescription> getErrors() {
		return errors;
	}
	public void setErrors(java.util.List<ErrorDescription> errors) {
		this.errors = errors;
	}

	public void setErrors(jade.util.leap.ArrayList errors) {

		if (errors == null) {
			this.errors = null;
			return;
		}

		for (int i = 0; i < errors.size(); i++) {
			this.addError( (ErrorDescription) errors.get(i) );
		}
	}
    public void addError(ErrorDescription error) {
    	if (errors == null) {
    		errors = new java.util.ArrayList<ErrorDescription>();
    	}
    	errors.add(error);
	}
    
    public Collection<UniversalConnector> getInputSlots()
    {
    	return inputSlots;
    }

	public void addInputSlot(UniversalConnector connector) {
		
		if (inputSlots == null) { 
			inputSlots = new java.util.ArrayList<UniversalConnector>();
		}

		inputSlots.add(connector);
	}

}
