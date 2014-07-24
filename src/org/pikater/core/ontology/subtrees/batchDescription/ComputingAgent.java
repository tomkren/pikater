package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent extends DataProcessing implements IDataProvider, IComputingAgent, IErrorProvider {

	private static final long serialVersionUID = 2127755171666013125L;

	private String agentType;
    private List<NewOption> options;
	private IModelDescription model;
    private EvaluationMethod evaluationMethod;
    
	private DataSourceDescription trainingData;
    private DataSourceDescription testingData;
    private DataSourceDescription validationData;

    public ComputingAgent() {
    	
    	this.options = new ArrayList<NewOption>();
    	this.model = new NewModel();
    	this.evaluationMethod = new EvaluationMethod();
    }
    
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
    	if (trainingData == null) {
    		throw new IllegalArgumentException("Argument trainingData can't be null");
    	}
    	DataSourceDescription trainingDataC = new DataSourceDescription(); 
    	trainingDataC.setDataInputType("trainingData");
    	trainingDataC.setDataOutputType(trainingData.getDataOutputType());
    	trainingDataC.setDataProvider(trainingData.getDataProvider());
    	
        this.trainingData = trainingDataC;
    }

    public DataSourceDescription getTestingData() {
        return testingData;
    }
    public void setTestingData(DataSourceDescription testingData) {
    	if (testingData == null) {
    		throw new IllegalArgumentException("Argument testingData can't be null");
    	}
    	DataSourceDescription testingDataC = new DataSourceDescription(); 
    	testingDataC.setDataInputType("testingData");
    	testingDataC.setDataOutputType(testingData.getDataOutputType());
    	testingDataC.setDataProvider(testingData.getDataProvider());

        this.testingData = testingDataC;
    }

    public DataSourceDescription getValidationData() {
        return validationData;
    }
    public void setValidationData(DataSourceDescription validationData) {
    	if (validationData == null) {
    		throw new IllegalArgumentException("Argument validationData can't be null");
    	}
    	DataSourceDescription validationDataC = new DataSourceDescription(); 
    	validationDataC.setDataInputType("validationData");
    	validationDataC.setDataOutputType(validationData.getDataOutputType());
    	validationDataC.setDataProvider(validationData.getDataProvider());

        this.validationData = validationDataC;
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
				new NewOption("agentType",agentType);

		NewOption modelOption = new NewOption( "model",model.getClass().getSimpleName());

		//NewOption evaluationMethodOption = new NewOption( "evaluationMethod",evaluationMethod.getType());
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(agentTypeOption);
		options.add(modelOption);
		options.addAll(this.options);
		return options;
	}

	@Override
	public void importAllOptions(List<NewOption> options) {
		
		NewOptions optionsOntol = new NewOptions(options);

		//import agentType
		NewOption optAgentType = optionsOntol.getOptionByName("agentType");
		if (optAgentType != null) {
			StringValue valueAgentType = (StringValue)
					optAgentType.toSingleValue().getCurrentValue(); 
			this.agentType = valueAgentType.getValue();
		}

		//import model
		NewOption optModel = optionsOntol.getOptionByName("model");
		if (optModel != null) {
			Value value = optModel.toSingleValue();
			if (value.getCurrentValue() instanceof StringValue) {
				StringValue stringValue = (StringValue) value.getCurrentValue();
				if (stringValue.getValue().equals(NewModel.class.getSimpleName())) {
					this.model = new NewModel();
				} else {
					throw new IllegalStateException("Option doesn't contain correct value");
				}
			} else if (value.getCurrentValue() instanceof IntegerValue) {
				IntegerValue integerValue = (IntegerValue) value.getCurrentValue();
				this.model = new ModelDescription(integerValue.getValue());
			} else {
				throw new IllegalStateException("Option doesn't contain correct type");
			}
		}

		//import evaluationMethod
		//NewOption optMethod = optionsOntol.getOptionByName("evaluationMethod");
		//StringValue valueMethod = (StringValue)
		//		optMethod.toSingleValue().getCurrentValue();
		//this.evaluationMethod = new EvaluationMethod(
		//		valueMethod.getValue() );
		
		options.remove(optAgentType);
		options.remove(optModel);
		
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
			DataSourceDescription trainingDataC = new DataSourceDescription();
			trainingDataC.setDataInputType("trainingData");
			trainingDataC.setDataOutputType(trainingData.getDataOutputType());
			trainingDataC.setDataProvider(trainingData.getDataProvider());
			slots.add(trainingDataC);
		}
		
		if (testingData != null) {
			DataSourceDescription testingDataC = new DataSourceDescription();
			testingDataC.setDataInputType("testingData");
			testingDataC.setDataOutputType(testingData.getDataOutputType());
			testingDataC.setDataProvider(testingData.getDataProvider());
			slots.add(testingDataC);
		}
		
		if (validationData != null) {
			DataSourceDescription validationDataC = new DataSourceDescription();
			validationDataC.setDataInputType("validationData");
			validationDataC.setDataOutputType(validationData.getDataOutputType());
			validationDataC.setDataProvider(validationData.getDataProvider());
			slots.add(validationDataC);
		}
		
		if (evaluationMethod != null) {
			DataSourceDescription evaluationMethodDataC = new DataSourceDescription();
			evaluationMethodDataC.setDataInputType("evaluationMethod");
			evaluationMethodDataC.setDataOutputType("evaluationMethod");
			evaluationMethodDataC.setDataProvider(evaluationMethod);
			slots.add(evaluationMethodDataC);

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
		
		DataSourceDescription descriptinevaluationMethodData = 
				descriptions.getDataSourceDescriptionIBynputType("evaluationMethod");
		evaluationMethod = (EvaluationMethod) descriptinevaluationMethodData.getDataProvider();
	}

}

