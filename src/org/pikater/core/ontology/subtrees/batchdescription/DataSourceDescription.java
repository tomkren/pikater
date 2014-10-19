package org.pikater.core.ontology.subtrees.batchdescription;

import org.pikater.core.CoreConstant;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class DataSourceDescription implements ISourceDescription {

	private static final long serialVersionUID = 2090764353306584887L;

    private IDataProvider dataProvider;
	private String outputType;
	private String inputType;


    public DataSourceDescription() {
    }

    public IDataProvider getDataProvider() {
        return dataProvider;
    }
    public void setDataProvider(IDataProvider dataProvider) {
    	
    	if (dataProvider instanceof FileDataProvider) {
    		this.setOutputType(CoreConstant.SlotContent.FILE_DATA.getSlotName());
    		
    	} else if (dataProvider instanceof ComputingAgent) {
    		this.setOutputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
    		
    	} else if (dataProvider instanceof CARecSearchComplex) {
    		this.setOutputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
    	}
    	
        this.dataProvider = dataProvider;
    }

    public String getOutputType() {
        return outputType;
    }
    public void setOutputType(String dataType) {
        this.outputType = dataType;
    }
    
    public String getInputType() {
		return inputType;
	}
	public void setInputType(String dataInputType) {
		this.inputType = dataInputType;
	}

	@Override
	public void importSource(IComputationElement element) {
		this.dataProvider = (IDataProvider) element;
	}

	@Override
	public IComputationElement exportSource() {
		return this.dataProvider;
	}

	@Override
	public DataSourceDescription clone()
	{
		DataSourceDescription dataSource;
		try
		{
			dataSource = (DataSourceDescription) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		dataSource.setInputType(this.getInputType());
		dataSource.setOutputType(this.getOutputType());
		dataSource.setDataProvider(getDataProvider().clone());
		return dataSource;
	}

}
