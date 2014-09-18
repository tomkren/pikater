package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration.DurationType;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputingAgent extends DataProcessing implements IDataProvider, IComputingAgent, IErrorProvider {

	private static final long serialVersionUID = 2127755171666013125L;

	private String agentType;
    private List<NewOption> options;
	private Integer model; // null = new model
	private ExpectedDuration duration;
	
    private EvaluationMethod evaluationMethod;
    
	private DataSourceDescription trainingData;
    private DataSourceDescription testingData;
    private DataSourceDescription validationData;

    public ComputingAgent() {
    	
    	this.options = new ArrayList<NewOption>();
    	this.model = null;
    	this.duration = new ExpectedDuration();
    	this.evaluationMethod =
    			new EvaluationMethod(CrossValidation.class.getName());
    }
    
    public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public Integer getModel() {
		return model;
	}
	public void setModel(Integer model) {
		this.model = model;
	}    

    public ExpectedDuration getDuration() {
		return duration;
	}
	public void setDuration(ExpectedDuration duration) {
		this.duration = duration;
	}

	public DataSourceDescription getTrainingData() {
        return trainingData;
    }
    public void setTrainingData(DataSourceDescription trainingData) {
    	if (trainingData == null) {
    		throw new IllegalArgumentException("Argument trainingData can't be null");
    	}
    	DataSourceDescription trainingDataC = new DataSourceDescription(); 
    	trainingDataC.setInputType(CoreConstant.Slot.SLOT_TRAINING_DATA.get());
    	trainingDataC.setOutputType(trainingData.getOutputType());
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
    	testingDataC.setInputType(CoreConstant.Slot.SLOT_TESTING_DATA.get());
    	testingDataC.setOutputType(testingData.getOutputType());
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
    	validationDataC.setInputType(CoreConstant.Slot.SLOT_VALIDATION_DATA.get());
    	validationDataC.setOutputType(validationData.getOutputType());
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
    	List<NewOption> optionsList = new ArrayList<NewOption>();
    	for (NewOption optionI : options) {
    		if (optionI != null) {
    			optionsList.add(optionI);
    		}
    	}
        this.options = optionsList;
    }
    public void addOption(NewOption option) {
    	
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }


	@Override
	public List<NewOption> exportAllOptions() {
		List<NewOption> result = new ArrayList<NewOption>();

		NewOption modelOption = null;
		if (model == null) {
			modelOption = new NewOption(CoreConstant.Misc.MODEL.get(), new NullValue());
		} else {
			modelOption = new NewOption(CoreConstant.Misc.MODEL.get(), model);
		}
		result.add(modelOption);
		
		NewOption expectedDurationOption = new NewOption(
				CoreConstant.Misc.DURATION.get(), duration.getDurationType());
		
		result.add(expectedDurationOption);
		result.addAll(this.options);
		return result;
	}

	@Override
	public void importAllOptions(List<NewOption> options) {
		
		NewOptions optionsOntol = new NewOptions(options);

		//import model
		NewOption optModel = optionsOntol.fetchOptionByName(CoreConstant.Misc.MODEL.get());

		if (optModel != null) {
			Value value = optModel.toSingleValue();
			if (value.getCurrentValue() instanceof IntegerValue) {
				IntegerValue integerValue = (IntegerValue) value.getCurrentValue();
				this.model = integerValue.getValue();
			} else if (value.getCurrentValue() instanceof NullValue) {
				this.model = null;
			} else {
			throw new IllegalStateException("Option doesn't contain correct type");
			}
		}


		//import duration
		NewOption optDuration = optionsOntol.fetchOptionByName(CoreConstant.Misc.DURATION.get());
		StringValue valueMethod = (StringValue)
				optDuration.toSingleValue().getCurrentValue();
		
		this.duration = new ExpectedDuration();
		this.duration.setDurationType(DurationType.getDurationType(valueMethod.getValue()).name());
		
		options.remove(optModel);
		options.remove(optDuration);
		
		this.options = options;
		
	}
	
	@Override
	public List<ErrorSourceDescription> exportAllErrors() {
		return new ArrayList<ErrorSourceDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorSourceDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			throw new IllegalArgumentException("Argument errors can be only null");
		}
	}	
    
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {

		List<DataSourceDescription> slots = new ArrayList<DataSourceDescription>();
		
		if (trainingData != null) {
			DataSourceDescription trainingDataC = new DataSourceDescription();
			trainingDataC.setInputType(CoreConstant.Slot.SLOT_TRAINING_DATA.get());
			trainingDataC.setOutputType(trainingData.getOutputType());
			trainingDataC.setDataProvider(trainingData.getDataProvider());
			slots.add(trainingDataC);
		}
		
		if (testingData != null) {
			DataSourceDescription testingDataC = new DataSourceDescription();
			testingDataC.setInputType(CoreConstant.Slot.SLOT_TESTING_DATA.get());
			testingDataC.setOutputType(testingData.getOutputType());
			testingDataC.setDataProvider(testingData.getDataProvider());
			slots.add(testingDataC);
		}
		
		if (validationData != null) {
			DataSourceDescription validationDataC = new DataSourceDescription();
			validationDataC.setInputType(CoreConstant.Slot.SLOT_VALIDATION_DATA.get());
			validationDataC.setOutputType(validationData.getOutputType());
			validationDataC.setDataProvider(validationData.getDataProvider());
			slots.add(validationDataC);
		}
		
		if (evaluationMethod != null) {
			DataSourceDescription evaluationMethodDataC = new DataSourceDescription();
			evaluationMethodDataC.setInputType(CoreConstant.Slot.SLOT_EVALUATION_METHOD.get());
			evaluationMethodDataC.setOutputType("evaluationMethod");
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
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstant.Slot.SLOT_TRAINING_DATA.get());
		trainingData = descriptinTrainingData;

		DataSourceDescription descriptinTestingData = 
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstant.Slot.SLOT_TESTING_DATA.get());
		testingData = descriptinTestingData;
		
		DataSourceDescription descriptinValidationData = 
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstant.Slot.SLOT_VALIDATION_DATA.get());
		validationData = descriptinValidationData;
		
		DataSourceDescription descriptinevaluationMethodData = 
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstant.Slot.SLOT_EVALUATION_METHOD.get());
		evaluationMethod = (EvaluationMethod) descriptinevaluationMethodData.getDataProvider();
	}

	public ComputingAgent clone() {
		
		ComputingAgent comAgentColone = new ComputingAgent();
		comAgentColone.setId(this.getId());
		comAgentColone.setAgentType(this.getAgentType());
		NewOptions optionsOnt = new NewOptions(this.options);
		comAgentColone.setOptions(optionsOnt.clone().getOptions());
		comAgentColone.setModel(this.model);
		
		comAgentColone.setDuration(this.duration.clone());
		comAgentColone.setEvaluationMethod(this.evaluationMethod.clone());

		if (this.trainingData != null) {
			comAgentColone.setTrainingData(this.trainingData.clone());
		}
		if (this.testingData != null) {
			comAgentColone.setTestingData(this.testingData.clone());
		}
		if (this.validationData != null) {
			comAgentColone.setValidationData(this.validationData.clone());
		}
		return comAgentColone;
	}
	
}

