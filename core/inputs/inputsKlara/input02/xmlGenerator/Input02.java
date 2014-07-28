package xmlGenerator;

import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Example: single datasource, search the space of parameters of single computation model
// Save the results of the best iteration of search
public final class Input02 {

	public static ComputationDescription createDescription() {
		
        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription("weather.arff");

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
		
		EvaluationMethod em = new EvaluationMethod("Crossvalidation");
		NewOption optionF = new NewOption("F",10);
		em.addOption(optionF);
		
		comAgent.setEvaluationMethod(em);
		comAgent.setModel(null);

		Search search = new Search();
        search.setSearchClass(Agent_RandomSearch.class.getName());
		
        
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);

        complex.setSearch(search);

        //Set error provider
        ErrorDescription errorDescription=new ErrorDescription();
        errorDescription.setType("error");
        errorDescription.setProvider(comAgent);
        complex.setErrors(new ArrayList<>(Arrays.asList( errorDescription)) );

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
        comDescription.setPriority(6);
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
