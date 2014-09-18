package xmlGenerator;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_WeatherSplitter;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchDescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class Input09 {

	public static ComputationDescription createDescription() {
		
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
		dataSourceSunny.setInputType(CoreConstant.Slot.SLOT_TRAINING_DATA.get());
		dataSourceSunny.setDataProvider(processing);

		DataSourceDescription dataSourceRainy = new DataSourceDescription();
		dataSourceRainy.setOutputType("rainyOutput");
		dataSourceRainy.setInputType(CoreConstant.Slot.SLOT_TESTING_DATA.get());
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
	
	public static void main(String[] args) throws FileNotFoundException {

		System.out
				.println("Exporting Ontology input09 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = CoreConfiguration.getPath_KlarasInputs() + "input09"
				+ System.getProperty("file.separator") + "input.xml";

		comDescription.exportXML(fileName);
	}
}
