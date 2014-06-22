package xmlGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.Converter;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.batchDescription.*;

//Example: single datasource, search the space of parameters of single computation model
// Save the results of the best iteration of search
public final class Input2 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input2 to Klara's input XML configuration file.");

        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription("weather.arff");

        //Create two options for single computing agent
        StepanuvOption optionS = new StepanuvOption();
        optionS.setName("S");
        optionS.setOption( new OptionValue(1) );

        StepanuvOption optionB = new StepanuvOption();
        optionB.setName("B");
        optionB.setOption( new OptionValue("?") );

        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption( Converter.toOption(optionS) );
		comAgent.addOption( Converter.toOption(optionB) );
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);

        //Define options for search and the search itself
		StepanuvOption optionN = new StepanuvOption();
		optionN.setName("N");
		optionN.setSynopsis("number_of_values_to_try");
		optionN.setOption( new OptionValue(5) );
		
		Search search = new Search();
        search.setSearchClass("Agent_ChooseXValues");
		search.addOption( Converter.toOption(optionN) );

        //Complex computation agent - options + search + simple CA
/*		StepanuvOption optionOutput = new StepanuvOption();
		optionOutput.setName("output");
		optionOutput.setOption( new OptionValue("evaluation_only") );
*/
		StepanuvOption optionF = new StepanuvOption();
		optionF.setName("F");
		optionF.setOption( new OptionValue(10) );
		
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);
//				complex.addOption( Converter.toOption(optionOutput) );
		complex.addOption( Converter.toOption(optionF) );
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
		computingDataSource.setDataType("Data");
		computingDataSource.setDataProvider(complex);

        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        List<FileDataSaver> roots = new ArrayList<>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

		String fileName = Agent_GUIKlara.filePath + "input2"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
	}
}
