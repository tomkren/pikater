package org.pikater.core.ontology.subtrees.batchDescription;




import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;

/**
 * Created by Stepan on 20.4.14.
 */
public class DataProcessing extends AbstractDataProcessing implements IDataProvider {

	private static final long serialVersionUID = -2418323249803736416L;

	private boolean isPreprocessing;
	private List<Option> options;
    
	private List<ErrorDescription> errors;
	private List<DataSourceDescription> dataSources;

	public boolean isPreprocessing() {
		return isPreprocessing;
	}
	public void setPreprocessing(boolean isPreprocessing) {
		this.isPreprocessing = isPreprocessing;
	}

	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<ErrorDescription> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorDescription> errors) {
		this.errors = errors;
	}

    public List<DataSourceDescription> getDataSources() {
        return dataSources;
    }
    public void setDataSources(List<DataSourceDescription> dataSources) {
        this.dataSources = dataSources;
    }
    public void addDataSources(DataSourceDescription dataSources) {
    	if (this.dataSources == null) {
    		this.dataSources = new ArrayList<DataSourceDescription>();
    	}
        this.dataSources.add(dataSources);
    }

	@Override
	public List<Option> getUniversalOptions() {
		return this.options;
	}
	@Override
	public void setUniversalOptions(List<Option> options) {
		this.options = options;
	}
	
	@Override
	public List<ErrorDescription> getUniversalErrors() {
		return this.errors;
	}
	@Override
	public void setUniversalErrors(List<ErrorDescription> errors) {
		this.errors = errors;
		
	}
	
	@Override
	public List<Slot> getInputSlots() {
	
		List<Slot> slots = new ArrayList<Slot>();
		
		for (DataSourceDescription dI : dataSources ) {

			Slot slot = new Slot();
			slot.setInputDataType("");
			slot.setOutputDataType(dI.getDataType());
			slot.setAbstractDataProcessing(dI.getDataProvider());
			
			slots.add(slot);
		}
		
		return slots;
	}
	@Override
	public void setUniversalInputSlots(List<Slot> universalInputSlots) {
		// TODO Auto-generated method stub
		
	}

}
