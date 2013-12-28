package pikater.ontology.description;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent implements IDataProvider, IComputingAgent, IErrorProvider {

    String modelClass;
    ArrayList<Parameter> parameters;

    DataSourceDescription trainingData;
    DataSourceDescription testingData;
    DataSourceDescription validationData;

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
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
