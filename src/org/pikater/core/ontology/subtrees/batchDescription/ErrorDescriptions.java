package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

public class ErrorDescriptions {

	private List<ErrorSourceDescription> errors;

	public ErrorDescriptions() {
		this.errors = new ArrayList<ErrorSourceDescription>();
	}
	
	public ErrorDescriptions(List<ErrorSourceDescription> errors) {
		this.errors = errors; 
	}
	
	public List<ErrorSourceDescription> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorSourceDescription> errors) {
		this.errors = errors;
	}
	public void addErrors(ErrorSourceDescription error) {
		this.errors.add(error);
	}
	
	@Override
	public ErrorDescriptions clone() throws CloneNotSupportedException
	{
		ErrorDescriptions result = (ErrorDescriptions) super.clone();
		for (ErrorSourceDescription errorI : errors)
		{
			result.addErrors(errorI.clone());
		}
		return result;
	}
}