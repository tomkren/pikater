package xmlGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;

//Example: single datasource, search the space of parameters of single computation model
// Save the results of the best iteration of search
public final class Input02 {

	public static ComputationDescription createDescription() {
		
        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription("weather.arff");

        //Create two options for single computing agent        
        NewOption optionS = new NewOption(
        		new IntegerValue(1), "S"); 
        
        NewOption optionB = new NewOption(
        		new QuestionMarkRange(new DoubleValue(0.0), new DoubleValue(100.0), 5),
        		"B");

        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption(optionS);
		comAgent.addOption(optionB);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		comAgent.setEvaluationMethod(new EvaluationMethod("Crossvalidation"));
		comAgent.setModel(new NewModel());

		Search search = new Search();
        search.setSearchClass("Agent_ChooseXValues");
		
        NewOption optionF = new NewOption(
        		new IntegerValue(10), "F"); 
		
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);

		complex.addOption(optionF);
        complex.setSearch(search);

        //Set error provider
        ErrorDescription errorDescription=new ErrorDescription();
        errorDescription.setType("RMSE");
        errorDescription.setProvider(comAgent);
        complex.setErrors(new ArrayList<ErrorDescription>(Arrays.asList( errorDescription)) );

        // set DataSource
        // Note that the data provider is complex.
        // To save each iteration the data source would have to be comAgent
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataOutputType("Data");
		computingDataSource.setDataProvider(complex);

        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

        return comDescription;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input02 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = Agent_GUIKlara.filePath + "input02"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
	}
}
