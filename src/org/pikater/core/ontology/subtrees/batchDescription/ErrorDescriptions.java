package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.util.ICloneable;
import org.pikater.shared.util.collections.CollectionUtils;

public class ErrorDescriptions implements ICloneable
{
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
	public ErrorDescriptions clone()
	{
		ErrorDescriptions result;
		try
		{
			result = (ErrorDescriptions) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setErrors(CollectionUtils.deepCopy(errors));
		return result;
	}
}