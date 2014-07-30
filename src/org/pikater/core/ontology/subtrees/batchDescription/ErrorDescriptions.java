package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;

public class ErrorDescriptions {

	private List<ErrorDescription> errors;

	public ErrorDescriptions() {}
	
	public ErrorDescriptions(List<ErrorDescription> errors) {
		this.errors = errors; 
	}
	
	public List<ErrorDescription> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorDescription> errors) {
		this.errors = errors;
	}
	public void addErrors(ErrorDescription error) {
		this.errors.add(error);
	}
	
	public ErrorDescriptions clone() {
		
		ErrorDescriptions errorsOnt = new ErrorDescriptions();
		for (ErrorDescription errorI : errors) {
			errorsOnt.addErrors(errorI.clone());
		}
		
		return errorsOnt;
	}
	
}
