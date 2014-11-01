package org.pikater.core.experiments;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;

import java.util.ArrayList;
import java.util.List;
 
/**
 * Single datasource -> single computing agent -> single save.
 * 
 * @author Klara
 */
public final class Input01 implements ITestExperiment
{
	public ComputationDescription createDescription()
	{
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        
        //Specify a datasource
        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        //Create validation method for a computing agent
        EvaluationMethod evaluationMethod =
        		new EvaluationMethod(CrossValidation.class.getName());
        
        //Create cross validation option                
        NewOption optionF = new NewOption("F",8);
        
        evaluationMethod.addOption(optionF);
        
        
        //Create two options for single computing agent
        NewOption optionS = new NewOption("S",1);

        NewOption optionM = new NewOption("M",2);

        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption(optionS);
		comAgent.addOption(optionM);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		comAgent.setValidationData(fileDataSource);
		comAgent.setEvaluationMethod(evaluationMethod);
		//comAgent.setModel(70704);
		comAgent.setModel(null);

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
