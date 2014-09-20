package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.CoreConstant;
import org.pikater.shared.util.ICloneable;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ErrorSourceDescription implements ISourceDescription, ICloneable {

	private static final long serialVersionUID = 1229354484860699593L;
	
	private IErrorProvider provider;
	private String outputType;
	private String inputType;

    public IErrorProvider getProvider() {
        return provider;
    }
    public void setProvider(IErrorProvider errorProvider) {
    	if (errorProvider instanceof FileDataProvider) {
    		this.setOutputType(CoreConstant.Slot.SLOT_ERRORS.get());
    	}
        this.provider = errorProvider;
    }
    
    public String getOutputType() {
		return outputType;
	}
	public void setOutputType(String dataOutputType) {
		this.outputType = dataOutputType;
	}
	
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String dataInputType) {
		this.inputType = dataInputType;
	}
	
	@Override
	public void importSource(IComputationElement element) {
		this.provider = (IErrorProvider) element;
		
	}
	@Override
	public IComputationElement exportSource() {
		return this.provider;
	}
	
	@Override
	public ErrorSourceDescription clone()
	{
    	ErrorSourceDescription description;
		try
		{
			description = (ErrorSourceDescription) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
    	description.setProvider(provider);
    	description.setInputType(getInputType());
    	description.setOutputType(getOutputType());
    	return description;
    }
	
}
