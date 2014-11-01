package org.pikater.core.experiments;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_WeatherSplitter;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchdescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchdescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;

public final class Input09 implements ITestExperiment {

	public ComputationDescription createDescription() {
		
	    FileDataProvider fileDataProvider = new FileDataProvider();
	    fileDataProvider.setFileURI("weather.arff");
	    
	    FileDataProvider fileDataProvider2 = new FileDataProvider();
	    fileDataProvider2.setFileURI("weather.arff");
    
		// Specify datasources for the first preprocessing 
		DataSourceDescription fileDataSource1 = new DataSourceDescription();
		fileDataSource1.setInputType("firstInput");
		fileDataSource1.setDataProvider(fileDataProvider);

		DataSourceDescription fileDataSource2 = new DataSourceDescription();
		fileDataSource2.setInputType("secondInput");
		fileDataSource2.setDataProvider(fileDataProvider2);

		// PreProcessing 1 ...
		DataProcessing processing = new DataProcessing();
		processing.setAgentType(Agent_WeatherSplitter.class.getName());
		processing.addDataSources(fileDataSource1);
		processing.addDataSources(fileDataSource2);
		
		DataSourceDescription dataSourceSunny = new DataSourceDescription();
		dataSourceSunny.setOutputType("sunnyOutput");
		dataSourceSunny.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
		dataSourceSunny.setDataProvider(processing);

		DataSourceDescription dataSourceRainy = new DataSourceDescription();
		dataSourceRainy.setOutputType("rainyOutput");
		dataSourceRainy.setInputType(CoreConstant.SlotContent.TESTING_DATA.getSlotName());
		dataSourceRainy.setDataProvider(processing);
						
        //Create validation method for a computing agent
        EvaluationMethod evaluationMethod =
        		new EvaluationMethod(CrossValidation.class.getName());
        
		//Create cross validation option                
        NewOption optionF = new NewOption("F",10);
        
        evaluationMethod.addOption(optionF);
        
		
		//Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.setTrainingData(dataSourceSunny);
		comAgent.setTestingData(dataSourceRainy);
		comAgent.setValidationData(dataSourceRainy);
		comAgent.setEvaluationMethod(evaluationMethod);
		
        //Labeled data labeled by our CA are the new datasource
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setOutputType("Data");
		computingDataSource.setDataProvider(comAgent);

        //Save labeled data
        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        //Our requirements for the description are ready, lets create new computation description
        List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

		return comDescription;
	}
}
