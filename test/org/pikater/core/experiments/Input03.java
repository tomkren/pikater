package org.pikater.core.experiments;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;

public final class Input03 implements ITestExperiment {

	public ComputationDescription createDescription() {
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        //Create validation method for a computing agent
        EvaluationMethod evaluationMethod1 =
        		new EvaluationMethod(CrossValidation.class.getName());
        
        //Create cross validation option                
        NewOption optionF1 = new NewOption("F",8);
        
        evaluationMethod1.addOption(optionF1);
        
		ComputingAgent comAgent1 = new ComputingAgent();
		comAgent1.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent1.setTrainingData(fileDataSource);
		comAgent1.setTestingData(fileDataSource);
		comAgent1.setValidationData(fileDataSource);
		comAgent1.setModel(null);
		comAgent1.setEvaluationMethod(evaluationMethod1);
		
		/* NewOption optionOutput1 = new NewOption(
				CoreConstant.Output.DEFAULT.name(), CoreConstant.Output.EVALUATION_ONLY.name());
				 */

		CARecSearchComplex complex1 = new CARecSearchComplex();
		complex1.setComputingAgent(comAgent1);

		// complex1.addOption(optionOutput1);

		DataSourceDescription computingDataSource1 = new DataSourceDescription();
		computingDataSource1.setOutputType("Data");
		computingDataSource1.setDataProvider(complex1);


        //Create validation method for a computing agent
        EvaluationMethod evaluationMethod2 =
        		new EvaluationMethod(CrossValidation.class.getName());
        
        //Create cross validation option                
        NewOption optionF2 = new NewOption("F",8);
        
        evaluationMethod2.addOption(optionF2);
        
		ComputingAgent comAgent2 = new ComputingAgent();
		comAgent2.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent2.setTrainingData(computingDataSource1);
		comAgent2.setTestingData(computingDataSource1);
		comAgent2.setValidationData(fileDataSource);
		comAgent2.setModel(null);
		comAgent2.setEvaluationMethod(evaluationMethod2);

		/*
		NewOption optionOutput2 = new NewOption(
				CoreConstant.Output.DEFAULT.name(), CoreConstant.Output.EVALUATION_ONLY.name());
		*/

		CARecSearchComplex complex2 = new CARecSearchComplex();
		complex2.setComputingAgent(comAgent2);
		// complex2.addOption(optionOutput2);

		DataSourceDescription computingDataSource2 = new DataSourceDescription();
		computingDataSource2.setOutputType("Data");
		computingDataSource2.setDataProvider(complex2);



        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource2);

        ComputationDescription comDescription = new ComputationDescription();
        comDescription.addRootElement(saver);
		
        return comDescription;
	}
}
