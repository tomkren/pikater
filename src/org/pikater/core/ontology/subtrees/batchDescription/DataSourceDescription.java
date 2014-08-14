package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.CoreConstants;


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
    		this.setOutputType(CoreConstants.SLOT_FILE_DATA);
    		
    	} else if (dataProvider instanceof ComputingAgent) {
    		this.setOutputType(CoreConstants.SLOT_COMPUTED_DATA);
    		
    	} else if (dataProvider instanceof CARecSearchComplex) {
    		this.setOutputType(CoreConstants.SLOT_COMPUTED_DATA);
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

	public DataSourceDescription clone() {
		
		DataSourceDescription dataSource = new DataSourceDescription();
		dataSource.setInputType(this.getInputType());
		dataSource.setOutputType(this.getOutputType());
		dataSource.setDataProvider(getDataProvider().clone());
		return dataSource;
	}

}
