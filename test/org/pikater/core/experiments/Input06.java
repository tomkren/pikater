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

/**
 * Single datasource -> single computing agent -> single save.
 * 
 * @author Klara
 */
public final class Input06 implements ITestExperiment {

	public ComputationDescription createDescription() {

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        
        FileDataProvider fileDataProvider2 = new FileDataProvider();
        fileDataProvider2.setFileURI("weather.arff");

		// Specify a datasource
		DataSourceDescription fileDataSource1 = new DataSourceDescription();
		fileDataSource1.setInputType("firstInput");
		fileDataSource1.setDataProvider(fileDataProvider);

		DataSourceDescription fileDataSource2 = new DataSourceDescription();
		fileDataSource2.setInputType("secondInput");
		fileDataSource2.setDataProvider(fileDataProvider2);
		
		// Create validation method for a computing agent
		EvaluationMethod evaluationMethod = new EvaluationMethod(
				CrossValidation.class.getName());

		// Create cross validation option
		NewOption optionF = new NewOption("F", 8);
		evaluationMethod.addOption(optionF);

		// Create two options for single computing agent
		NewOption optionS = new NewOption("S", 1);
		NewOption optionM = new NewOption("M", 2);

		// Create data-processing
		
		// Specify a datasource

		// PreProcessing
				
		DataProcessing dp = new DataProcessing();
		dp.setAgentType(Agent_WeatherSplitter.class.getName());
		dp.addDataSources(fileDataSource1);
		dp.addDataSources(fileDataSource2);

		dp.setAgentType(Agent_WeatherSplitter.class.getName());

		// Data processed by dp are the new datasource
		DataSourceDescription dataSourceSunny = new DataSourceDescription();
		dataSourceSunny.setOutputType("sunnyOutput");
		dataSourceSunny.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
		dataSourceSunny.setDataProvider(dp);

		DataSourceDescription dataSourceRainy = new DataSourceDescription();
		dataSourceRainy.setOutputType("rainyOutput");
		dataSourceRainy.setInputType(CoreConstant.SlotContent.TESTING_DATA.getSlotName());
		dataSourceRainy.setDataProvider(dp);
		
		// Create new computing agent, add options and datasource that we have
		// created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption(optionS);
		comAgent.addOption(optionM);
		comAgent.setTrainingData(dataSourceSunny);
		comAgent.setTestingData(dataSourceRainy);
		comAgent.setValidationData(dataSourceRainy);
		comAgent.setDataToLabel(dataSourceRainy);
		comAgent.setEvaluationMethod(evaluationMethod);

		DataSourceDescription computingAgentDataSource = new DataSourceDescription();
		computingAgentDataSource.setOutputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
		computingAgentDataSource.setDataProvider(comAgent);

		// Save labeled data
		FileDataSaver saver = new FileDataSaver();
		saver.setDataSource(computingAgentDataSource);

		// Our requirements for the description are ready, lets create new
		// computation description
		List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
		roots.add(saver);

		ComputationDescription comDescription = new ComputationDescription();
		comDescription.setRootElements(roots);

		return comDescription;
	}
}
