package org.pikater.core.ontology.description;


import jade.util.leap.ArrayList;

import org.pikater.shared.database.experiment.UniversalConnector;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;

/**
 * Created by Stepan on 20.4.14.
 */
public class DataProcessing extends AbstractDataProcessing implements IDataProvider {

	private boolean isPreprocessing;
	private ArrayList options;
    
	private ArrayList errors;
	private ArrayList dataSources;

	public boolean isPreprocessing() {
		return isPreprocessing;
	}
	public void setPreprocessing(boolean isPreprocessing) {
		this.isPreprocessing = isPreprocessing;
	}

	public ArrayList getOptions() {
		return options;
	}
	public void setOptions(ArrayList options) {
		this.options = options;
	}

	public ArrayList getErrors() {
		return errors;
	}
	public void setErrors(ArrayList errors) {
		this.errors = errors;
	}

    public ArrayList getDataSources() {
        return dataSources;
    }
    public void setDataSources(ArrayList dataSources) {
        this.dataSources = dataSources;
    }
    public void addDataSources(DataSourceDescription dataSources) {
    	if (this.dataSources == null) {
    		this.dataSources = new ArrayList();
    	}
        this.dataSources.add(dataSources);
    }

	@Override
	UniversalElementWrapper exportUniversalElement() {

		UniversalElement element = new UniversalElement();
		element.setType(this.getClass());
		element.setOptions(options);

		for (int i = 0; i < dataSources.size(); i++) {

			DataSourceDescription dI =
					(DataSourceDescription) dataSources.get(i);
			UniversalConnector uc = dI.exportUniversalConnector();

			element.addInputSlot(uc);
		}

		UniversalElementWrapper wrapper = new UniversalElementWrapper();
		wrapper.setElement(element);
		
		return wrapper;
	}

}
