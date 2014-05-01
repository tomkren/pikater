package org.pikater.web.experiment.resources;

public class DataResource extends Resource
{
	/**
	 * A reference to the parameter from which the input will be derived (e.g. a filename). Although this field has
	 * no inherent use, it will be useful when converting the universal model to computation ontologies.
	 */
	public final ParamResource sourceSpecification;
	
	public DataResource(String description)
	{
		super(description);
		
		this.sourceSpecification = null;
	}
	
	public DataResource(ParamResource sourceSpecification, String description)
	{
		super(description);
		
		this.sourceSpecification = sourceSpecification;
	}
	
	public boolean isParameterized()
	{
		return sourceSpecification != null;
	}
}
