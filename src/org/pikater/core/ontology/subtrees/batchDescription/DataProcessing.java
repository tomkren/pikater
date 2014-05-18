package org.pikater.core.ontology.subtrees.batchDescription;




import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

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
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {

		UniversalOntology element = new UniversalOntology();
		element.setType(this.getClass());
		element.setOptions(options);

		for (int i = 0; i < dataSources.size(); i++) {

			DataSourceDescription dI =
					(DataSourceDescription) dataSources.get(i);
			UniversalConnector uc = dI.exportUniversalConnector(uModel);

			element.addInputSlot(uc);
		}

		UniversalElement wrapper = new UniversalElement(uModel);
		wrapper.setElement(element);
		
		return wrapper;
	}

}
