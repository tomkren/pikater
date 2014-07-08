package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.Options;
import org.pikater.core.ontology.subtrees.newOption.Value;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.value.StringValue;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent extends DataProcessing implements IDataProvider, IComputingAgent, IErrorProvider {

	private static final long serialVersionUID = 2127755171666013125L;

	private String agentType;
	private IModelDescription model;
    private List<NewOption> options = new ArrayList<NewOption>();
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
	
    public List<NewOption> getOptions() {
        return options;
    }
    public void setOptions(List<NewOption> options) {
    	
    	if (options == null) {
    		throw new NullPointerException("Argument options can't be null");
    	}
        this.options = options;
    }
    public void addOption(NewOption option) {
    	
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }


	@Override
	public List<NewOption> exportAllOptions() {
		
		NewOption agentTypeOption =
				new NewOption(new StringValue(agentType), "agentType");

		NewOption modelOption = new NewOption(
				new StringValue(model.getClass().getSimpleName()), "model");  // TODO

//		NewOption evaluationMethodOption = new NewOption( // TODO
//				new StringValue(evaluationMethod.toString()), "evaluationMethod");
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(agentTypeOption);
		options.add(modelOption);
		//options.add(evaluationMethodOption);
		options.addAll(this.options);
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
		Options optionsOntol = new Options(options);

		NewOption optAgentType = optionsOntol.getOptionByName("agentType");
		if (optAgentType != null) {
			StringValue valueAgentType = (StringValue)
					optAgentType.convertToSingleValue().getValue(); 
			this.agentType = valueAgentType.getValue();
		}

		NewOption optModel = optionsOntol.getOptionByName("model");
		if (optModel != null) {
			Value value = optModel.convertToSingleValue();
			if (value.getValue() instanceof StringValue) {
				StringValue stringValue = (StringValue) value.getValue();
				if (stringValue.equals(NewModel.class.getSimpleName())) {
					this.model = new NewModel();
				} else {
					throw new IllegalStateException();
				}
			} else if (value.getValue() instanceof IntegerValue) {
				IntegerValue integerValue = (IntegerValue) value.getValue();
				this.model = new ModelDescription(integerValue.getValue());
			} else {
				throw new IllegalStateException();
			}
		}

		NewOption optMethod = optionsOntol.getOptionByName("evaluationMethod");
		if (optMethod != null) {
			//TODO:
		}

		options.remove(optAgentType);
		options.remove(optModel);
		options.remove(optMethod);
		
		this.options = options;
		
	}
	
	@Override
	public List<ErrorDescription> exportAllErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			throw new IllegalArgumentException("Argument errors can be only null");
		}
	}	
    
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {

		List<DataSourceDescription> slots = new ArrayList<DataSourceDescription>();
		
		if (trainingData != null) {
			trainingData.setDataInputType("trainingData");
			slots.add(trainingData);
		}
		
		if (testingData != null) {
			testingData.setDataInputType("testingData");
			slots.add(testingData);
		}
		
		if (validationData != null) {
			validationData.setDataInputType("validationData");
			slots.add(validationData);
		}
		
		return slots;
	}
	
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		DataSourceDescriptions descriptions =
				new DataSourceDescriptions(dataSourceDescriptions);
		
		DataSourceDescription descriptinTrainingData = 
				descriptions.getDataSourceDescriptionIBynputType("trainingData");
		trainingData = descriptinTrainingData;

		DataSourceDescription descriptinTestingData = 
				descriptions.getDataSourceDescriptionIBynputType("testingData");
		testingData = descriptinTestingData;
		
		DataSourceDescription descriptinValidationData = 
				descriptions.getDataSourceDescriptionIBynputType("validationData");
		validationData = descriptinValidationData;
				
	}
	

}

