package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent extends AbstractDataProcessing implements IDataProvider, IComputingAgent, IErrorProvider {

	private static final long serialVersionUID = 2127755171666013125L;

	private String agentType;
	private IModelDescription model;
    private List<Option> options = new ArrayList<Option>();
    private EvaluationMethod evaluationMethod;
    
	private DataSourceDescription trainingData;
    private DataSourceDescription testingData;
    private DataSourceDescription validationData;

    public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public IModelDescription getModel() {
		return model;
	}
	public void setModel(IModelDescription model) {
		this.model = model;
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
	
	public EvaluationMethod getEvaluationMethod() {
		return evaluationMethod;
	}
	public void setEvaluationMethod(EvaluationMethod evaluationMethod) {
		this.evaluationMethod = evaluationMethod;
	}
	
    public List<Option> getOptions() {
        return options;
    }
    public void setOptions(List<Option> options) {
    	
    	if (options == null) {
    		throw new NullPointerException("Argument options can't be null");
    	}
        this.options = options;
    }
    public void addOption(Option option) {
    	
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }


	@Override
	public List<Option> getUniversalOptions() {
		
		Option agentTypeOption = new Option();
		agentTypeOption.setName("agentType");
		agentTypeOption.setValue(agentType);

		Option modelOption = new Option();
		modelOption.setName("model");
		modelOption.setValue(model.toString()); // TODO

		Option evaluationMethodOption = new Option();
		evaluationMethodOption.setName("evaluationMethod");
		evaluationMethodOption.setValue(evaluationMethod.toString()); // TODO

		
		List<Option> options = new ArrayList<Option>();
		options.add(agentTypeOption);
		options.add(modelOption);
		options.add(evaluationMethodOption);
		options.addAll(this.options);
		return options;
	}
	@Override
	public void setUniversalOptions(List<Option> options) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<ErrorDescription> getUniversalErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void setUniversalErrors(List<ErrorDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			throw new IllegalArgumentException("Argument errors can be only null");
		}
		
	}	
    
	@Override
	public List<Slot> getInputSlots() {
		
		Slot trainingDataSlot = new Slot();
		trainingDataSlot.setInputDataType("trainingData");
		trainingDataSlot.setOutputDataType(trainingData.getDataType());
		trainingDataSlot.setAbstractDataProcessing(
				trainingData.getDataProvider());

		Slot testingDataSlot = new Slot();
		testingDataSlot.setInputDataType("testingData");
		testingDataSlot.setOutputDataType(testingData.getDataType());
		testingDataSlot.setAbstractDataProcessing(
				(AbstractDataProcessing) testingData.getDataProvider());

		Slot validationDataSlot = new Slot();
		validationDataSlot.setInputDataType("validationData");
		validationDataSlot.setOutputDataType(validationData.getDataType());
		validationDataSlot.setAbstractDataProcessing(
				(AbstractDataProcessing) testingData.getDataProvider());

		List<Slot> slots = new ArrayList<Slot>();
		slots.add(trainingDataSlot);
		slots.add(testingDataSlot);
		slots.add(validationDataSlot);
		
		return slots;
	}
	
	@Override
	public void setUniversalInputSlots(List<Slot> universalInputSlots) {
		// TODO Auto-generated method stub
		
	}
	

}

