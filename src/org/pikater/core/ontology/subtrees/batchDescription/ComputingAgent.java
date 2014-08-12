package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstants;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.LongTermDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ShortTimeDuration;
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
	private IExpectedDuration duration;
	
    private EvaluationMethod evaluationMethod;
    
	private DataSourceDescription trainingData;
    private DataSourceDescription testingData;
    private DataSourceDescription validationData;

    public ComputingAgent() {
    	
    	this.options = new ArrayList<NewOption>();
    	this.model = null;
    	this.duration = new LongTermDuration();
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

    public IExpectedDuration getDuration() {
		return duration;
	}
	public void setDuration(IExpectedDuration duration) {
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
    	trainingDataC.setDataInputType(CoreConstants.SLOT_TRAINING_DATA);
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
    	testingDataC.setDataInputType(CoreConstants.SLOT_TESTING_DATA);
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
    	validationDataC.setDataInputType(CoreConstants.SLOT_VALIDATION_DATA);
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
		List<NewOption> options = new ArrayList<NewOption>();

		NewOption modelOption = null;
		if (model == null) {
			modelOption = new NewOption(CoreConstants.MODEL, new NullValue());
		} else {
			modelOption = new NewOption(CoreConstants.MODEL, model);
		}
		options.add(modelOption);
		
		NewOption expectedDurationOption = new NewOption(
				CoreConstants.DURATION, duration.getClass().getSimpleName());
		
		options.add(expectedDurationOption);
		options.addAll(this.options);
		return options;
	}

	@Override
	public void importAllOptions(List<NewOption> options) {
		
		NewOptions optionsOntol = new NewOptions(options);

		//import model
		NewOption optModel = optionsOntol.fetchOptionByName(CoreConstants.MODEL);

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
		NewOption optDuration = optionsOntol.fetchOptionByName(CoreConstants.DURATION);
		StringValue valueMethod = (StringValue)
				optDuration.toSingleValue().getCurrentValue();
		if (valueMethod.getValue().equals(LongTermDuration.class.getSimpleName())) {
			this.duration = new LongTermDuration();
		} else if (valueMethod.getValue().equals(ShortTimeDuration.class.getSimpleName()) ) {
			this.duration = new ShortTimeDuration();
		} else {
			throw new IllegalStateException("Option doesn't contain correct type");
		}
		
		options.remove(optModel);
		options.remove(optDuration);
		
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
			trainingDataC.setDataInputType(CoreConstants.SLOT_TRAINING_DATA);
			trainingDataC.setDataOutputType(trainingData.getDataOutputType());
			trainingDataC.setDataProvider(trainingData.getDataProvider());
			slots.add(trainingDataC);
		}
		
		if (testingData != null) {
			DataSourceDescription testingDataC = new DataSourceDescription();
			testingDataC.setDataInputType(CoreConstants.SLOT_TESTING_DATA);
			testingDataC.setDataOutputType(testingData.getDataOutputType());
			testingDataC.setDataProvider(testingData.getDataProvider());
			slots.add(testingDataC);
		}
		
		if (validationData != null) {
			DataSourceDescription validationDataC = new DataSourceDescription();
			validationDataC.setDataInputType(CoreConstants.SLOT_VALIDATION_DATA);
			validationDataC.setDataOutputType(validationData.getDataOutputType());
			validationDataC.setDataProvider(validationData.getDataProvider());
			slots.add(validationDataC);
		}
		
		if (evaluationMethod != null) {
			DataSourceDescription evaluationMethodDataC = new DataSourceDescription();
			evaluationMethodDataC.setDataInputType(CoreConstants.SLOT_EVALUATION_METHOD);
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
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstants.SLOT_TRAINING_DATA);
		trainingData = descriptinTrainingData;

		DataSourceDescription descriptinTestingData = 
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstants.SLOT_TESTING_DATA);
		testingData = descriptinTestingData;
		
		DataSourceDescription descriptinValidationData = 
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstants.SLOT_VALIDATION_DATA);
		validationData = descriptinValidationData;
		
		DataSourceDescription descriptinevaluationMethodData = 
				descriptions.getDataSourceDescriptionIBynputType(
						CoreConstants.SLOT_EVALUATION_METHOD);
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

