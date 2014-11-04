package org.pikater.core.experiments;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newoption.values.QuestionMarkRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Single datasource, search the space of parameters of single computation model.
 * Save the results of the best iteration of search
 * 
 * @author Klara
 */
public final class Input02 implements ITestExperiment {

	public ComputationDescription createDescription() {
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        
        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        //Create two options for single computing agent        
        NewOption optionS = new NewOption("L", 0.5);
        
        NewOption optionB = new NewOption("M",
        		new QuestionMarkRange(new DoubleValue(0), new DoubleValue(1), 20)
        		);

        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaMultilayerPerceptronCA.class.getName());
		comAgent.addOption(optionS);
		comAgent.addOption(optionB);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		comAgent.setValidationData(fileDataSource);
		
		NewOption optionF = new NewOption("F",10);
		
		EvaluationMethod em = new EvaluationMethod(
				CrossValidation.class.getName());
		em.addOption(optionF);
		
		comAgent.setEvaluationMethod(em);
		comAgent.setModel(null);

		Search search = new Search();
        search.setAgentType(Agent_RandomSearch.class.getName());
		
        
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);

        complex.setSearch(search);

        //Set error provider
        ErrorSourceDescription errorDescription = new ErrorSourceDescription();
        errorDescription.setOutputType(CoreConstant.SlotContent.ERRORS.getSlotName());
        errorDescription.setProvider(comAgent);
        complex.addError(errorDescription);

        // set DataSource
        // Note that the data provider is complex.
        // To save each iteration the data source would have to be comAgent
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setOutputType(CoreConstant.SlotContent.DATA.getSlotName());
		computingDataSource.setDataProvider(complex);

        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

        return comDescription;
	}
}
