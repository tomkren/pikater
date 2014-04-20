package org.pikater.core.ontology.description;


import org.pikater.core.ontology.messages.Option;

import jade.util.leap.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent implements IDataProvider, IComputingAgent, IErrorProvider {

	String modelClass;
    ArrayList options;

    DataSourceDescription trainingData;
    DataSourceDescription testingData;
    DataSourceDescription validationData;

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

}

