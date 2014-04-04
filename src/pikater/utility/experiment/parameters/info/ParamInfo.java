package pikater.utility.experiment.parameters.info;

/**
 * A class encapsulating parameter information. The @displayName and @description fields will be displayed to the user.
 * 
 * PROBLEM:
 * This class is used as key in @Map collections and the @ParameterSlot class which means that the equals method is used to
 * differentiate keys. This method needs to be carefully overriden, because Map will no longer return values when key classes
 * are changed in a way that would also change the result of the equals method.
 * @see java.util.Map
 * 
 * SOLUTION: make sure a unique immutable ID is supplied and checked. Other fields (like display name and description) will be
 * subject to translation if the application supports multiple locales and so these fields should not be checked at all.
 * 
 * HINT: when exactly the same parameter needs to be used in various boxes and inheritance is out of the question, simply define
 * the parameter in one of the boxes:
 * public static final ParamInfo definedExampleParam = new ParamInfo(...);
 * 
 * and add a reference to the other like this:
 * public static final ParamInfo exampleReference = Box1Params.definedExampleParam;
 * 
 * IMPORTANT NOTE:
 * The overriden equals method implementation is also required in the @ParameterSlot class.
 * @see ParameterSlot#canBeConnectedToSpecific
 */
public class ParamInfo
{
	public final String id;
	public String displayName;
	public String description;
	
	public ParamInfo(String id, String displayName, String description)
	{
		this.id = id;
		this.displayName = displayName;
		this.description = description;
	}

	@Override
	public boolean equals(Object obj)
	{
		// initial checks that are commonly generated in this method
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		// next, as stated in the Javadoc of this class, only check identity of the ID fields (this code was also generated)
		ParamInfo other = (ParamInfo) obj;
		if (id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		}
		else if (!id.equals(other.id))
		{
			return false;
		}
		return true;
	}
}
