package org.pikater.shared.database.experiment;


import org.pikater.core.ontology.description.ErrorDescription;
import org.pikater.core.ontology.messages.Option;

public class UniversalOntology {

	private Class<?> type;

	private java.util.ArrayList<UniversalConnector> inputSlots;
	private java.util.ArrayList<ErrorDescription> errors;

	private java.util.ArrayList<Option> options;

	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}

    public java.util.ArrayList<Option> getOptions() {
		return options;
	}
	public void setOptions(java.util.ArrayList<Option> options) {
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

    
	public java.util.ArrayList<ErrorDescription> getErrors() {
		return errors;
	}
	public void setErrors(java.util.ArrayList<ErrorDescription> errors) {
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

	public void addInputSlot(UniversalConnector connector) {
		
		if (inputSlots == null) { 
			inputSlots = new java.util.ArrayList<UniversalConnector>();
		}

		inputSlots.add(connector);
	}

}
