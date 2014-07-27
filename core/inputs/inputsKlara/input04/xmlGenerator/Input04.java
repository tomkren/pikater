package xmlGenerator;

import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.batchDescription.model.NewModel;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Example: single datasource, search the space of parameters of single computation model
// Save the results of the best iteration of search
public final class Input04 {

	public static ComputationDescription createDescription() {
		
        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription("weather.arff");
        
        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(null);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		
		EvaluationMethod em = new EvaluationMethod("Crossvalidation");
		NewOption optionF = new NewOption("F",10);
		em.addOption(optionF);
		
		comAgent.setEvaluationMethod(em);
		comAgent.setModel(new NewModel());

		Search search = new Search();
        search.setSearchClass("Agent_RandomSearch");
		
        Recommend recommender = new Recommend();
        recommender.setRecommenderClass("Agent_NMTopRecommender");
        
        
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);
		complex.setRecommender(recommender);

        complex.setSearch(search);

        //Set error provider
        ErrorDescription errorDescription=new ErrorDescription();
        errorDescription.setType("error");
        errorDescription.setProvider(comAgent);
        complex.setErrors(new ArrayList<>(Arrays.asList( errorDescription)) );

        // error from search to recommender
        ErrorDescription searchErrorDescription=new ErrorDescription();
        searchErrorDescription.setType("error");
        searchErrorDescription.setProvider(search);
        recommender.setErrors(new ArrayList<>(Arrays.asList( errorDescription)) );

        
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
		
		System.out.println("Exporting Ontology input04 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = Agent_GUIKlara.filePath + "input04"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
	}
}
