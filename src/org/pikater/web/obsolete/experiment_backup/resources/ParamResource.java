package org.pikater.web.obsolete.experiment_backup.resources;

/**
 * A class encapsulating parameter information.
 * 
 * PROBLEM:
 * This class is used as key in @Map collections and the @ParameterSlot class which means that the equals method is used to
 * differentiate keys. This method needs to be carefully overriden, because Map will no longer return values when key classes
 * are changed in a way that would also change the result of the equals method.
 * @see java.util.Map
 * 
 * SOLUTION: let the parent class, containing an immutable ID, implement the equals method. The @displayName field will be subject
 * to translation if the application supports multiple locales and so this field should not be involved at all.
 * 
 * HINT: when exactly the same parameter needs to be used in various boxes and inheritance is out of the question, simply define
 * the parameter in one of the boxes:
 * public static final ParamResource definedExampleParam = new ParamResource(...);
 * 
 * and add a reference to the other like this:
 * public static final ParamResource exampleReference = Box1Params.definedExampleParam;
 * 
 */
public class ParamResource extends Resource
{
	public String displayName;
	
	public ParamResource(String displayName, String description)
	{
		super(description);
		
		this.displayName = displayName;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
}
