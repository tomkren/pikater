package xmlGenerator;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.recommend.Agent_Basic;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Example: single datasource, search the space of parameters of single computation model
// Save the results of the best iteration of search
public final class Input05 {

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
		
		EvaluationMethod em =
				new EvaluationMethod(CrossValidation.class.getName());
		NewOption optionF = new NewOption("F",10);
		em.addOption(optionF);
		
		comAgent.setEvaluationMethod(em);

        Recommend recommender = new Recommend();
        recommender.setAgentType(Agent_Basic.class.getName());
        
        
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);
		complex.setRecommender(recommender);

        //Set error provider
        ErrorSourceDescription errorDescription=new ErrorSourceDescription();
        errorDescription.setOutputType(CoreConstant.Slot.SLOT_ERRORS.get());
        errorDescription.setProvider(comAgent);
        recommender.setErrors(new ArrayList<>(Arrays.asList( errorDescription)) );
        
        // set DataSource
        // Note that the data provider is complex.
        // To save each iteration the data source would have to be comAgent
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setOutputType("Data");
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
		
		System.out.println("Exporting Ontology input05 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = CoreConfiguration.getPath_KlarasInputs() + "input05"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
	}
}
