package org.pikater.core.ontology.description;


import org.pikater.core.ontology.messages.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

import jade.util.leap.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent extends AbstractDataProcessing implements IDataProvider, IComputingAgent, IErrorProvider {

	private static final long serialVersionUID = 2127755171666013125L;

	private String modelClass;
    private ArrayList options;

    private DataSourceDescription trainingData;
    private DataSourceDescription testingData;
    private DataSourceDescription validationData;

    public String getModelClass() {
		return modelClass;
	}
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

    public DataSourceDescription getTrainingData() {
        return trainingData;
    }
    public void setTrainingData(DataSourceDescription trainingData) {
        this.trainingData = trainingData;
    }

    public DataSourceDescription getTestingData() {
        return testingData;
    }
    public void setTestingData(DataSourceDescription testingData) {
        this.testingData = testingData;
    }

    public DataSourceDescription getValidationData() {
        return validationData;
    }
    public void setValidationData(DataSourceDescription validationData) {
        this.validationData = validationData;
    }
    
    public ArrayList getOptions() {
        return options;
    }
    public void setOptions(ArrayList options) {
        this.options = options;
    }
    public void addOption(Option option) {
        this.options.add(option);
    }


	@Override
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {
			    
	    UniversalConnector universalTrainingData =
	    		trainingData.exportUniversalConnector(uModel);
	    universalTrainingData.setInputDataType("trainingData");

		UniversalOntology element = new UniversalOntology();
		element.setType(this.getClass());
		element.setOptions(options);
		element.addInputSlot(universalTrainingData);
		
		UniversalElement wrapper =
				new UniversalElement(uModel);
		wrapper.setElement(element);
		
		return wrapper;
	}

}

