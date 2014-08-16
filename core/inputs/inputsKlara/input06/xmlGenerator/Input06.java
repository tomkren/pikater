package xmlGenerator;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_WeatherSplitter;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


//Example: Single datasource -> single computing agent. -> Single save
public final class Input06 {

	public static ComputationDescription createDescription() {
		
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

        
        
        //Create data-processing
        List<DataSourceDescription> dataSources = new ArrayList<DataSourceDescription>();
        dataSources.add(fileDataSource);
        
        DataProcessing dp = new DataProcessing();
		dp.setDataSources(dataSources);
		dp.setAgentType(Agent_WeatherSplitter.class.getName());

		// Data processed by dp are the new datasource
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setOutputType("Data");
		computingDataSource.setDataProvider(dp);
		
        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption(optionS);
		comAgent.addOption(optionM);
		comAgent.setTrainingData(computingDataSource);
		comAgent.setTestingData(computingDataSource);
		comAgent.setEvaluationMethod(evaluationMethod);
		
		DataSourceDescription computingAgentDataSource = new DataSourceDescription();
		computingAgentDataSource.setOutputType("DataCA");
		computingAgentDataSource.setDataProvider(comAgent);

        //Save labeled data
        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingAgentDataSource);

        //Our requirements for the description are ready, lets create new computation description
        List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

        return comDescription;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input06 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();
		
		String fileName = CoreConfiguration.INPUTS_KLARA_PATH + "input06"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
    }
}
