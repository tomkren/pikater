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
	public ErrorDescriptions clone() {
		
		ErrorDescriptions errorsOnt = new ErrorDescriptions();
		for (ErrorSourceDescription errorI : errors) {
			errorsOnt.addErrors(errorI.clone());
		}
		
		return errorsOnt;
	}
	
}
