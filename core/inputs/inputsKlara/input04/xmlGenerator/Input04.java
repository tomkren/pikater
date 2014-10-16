package xmlGenerator;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.recommend.Agent_NMTopRecommender;
import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Example: single datasource, search the space of parameters of single computation model
// Save the results of the best iteration of search
public final class Input04 {

	public static ComputationDescription createDescription() {
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        
        //Specify a datasource
        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);
        
        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(null);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		comAgent.setValidationData(fileDataSource);
		
		NewOption optionF = new NewOption("F", 10);
		
		EvaluationMethod em =
				new EvaluationMethod(CrossValidation.class.getName());
		em.addOption(optionF);
		
		comAgent.setEvaluationMethod(em);
		comAgent.setModel(null);

		Search search = new Search();
        search.setAgentType(Agent_RandomSearch.class.getName());
		
        Recommend recommender = new Recommend();
        recommender.setAgentType(Agent_NMTopRecommender.class.getName());
        
        
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);
		complex.setRecommender(recommender);

        complex.setSearch(search);

        //Set error provider
        ErrorSourceDescription errorDescription=new ErrorSourceDescription();
        errorDescription.setOutputType(CoreConstant.SlotContent.ERRORS.getSlotName());
        errorDescription.setProvider(comAgent);
        complex.setErrors(new ArrayList<>(Arrays.asList( errorDescription)) );

        // error from agent to recommender
        recommender.setErrors(new ArrayList<>(Arrays.asList( errorDescription)) );

        
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
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input04 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = CoreConfiguration.getKlarasInputsPath() + "input04"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
	}
}
