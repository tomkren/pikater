package pikater.utility.experiment.parameters;

import java.util.ArrayList;

public class EnumeratedValueParameter<T> extends ValueParameter<T>
{
    public final ArrayList<T> possibleValues;
	
    public EnumeratedValueParameter(T value, ArrayList<T> possibleValues)
	{
		super(value);
		
		this.possibleValues = possibleValues;
	}
    
    @Override
    public void setValue(T value)
    {
    	if(possibleValues.contains(value))
    	{
    		super.setValue(value);
    	}
    	else
    	{
    		throw new IllegalArgumentException();
    	}
    }
}
