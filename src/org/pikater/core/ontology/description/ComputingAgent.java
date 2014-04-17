package org.pikater.core.ontology.description;

import java.util.ArrayList;

import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.ontology.messages.Option;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent implements IDataProvider, IComputingAgent, IErrorProvider {

	String modelClass;
    
    ArrayList<Option> options;

    DataSourceDescription trainingData;
    DataSourceDescription testingData;
    DataSourceDescription validationData;

    public String getModelClass() {
		return modelClass;
	}
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

    public ArrayList<Option> getOptions() {
        return options;
    }
    public void setOptions(ArrayList<Option> options) {
        this.options = options;
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
    
}
