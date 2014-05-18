
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.Converter;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileDataSaver;
import org.pikater.core.ontology.description.Search;


public final class Input2 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input2 to Klara's input XML configuration file.");


		StepanuvOption optionS = new StepanuvOption();
		optionS.setName("S");
		optionS.setOption( new OptionValue(new Integer(1)) );
		
		StepanuvOption optionM = new StepanuvOption();
		optionM.setName("M");
		optionM.setOption( new OptionValue(new Integer(-1)) );
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setModelClass(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption( Converter.toOption(optionS) );
		comAgent.addOption( Converter.toOption(optionM) );
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);

		StepanuvOption optionSearchMethod = new StepanuvOption();
		optionSearchMethod.setName("search_method");
		optionSearchMethod.setOption( new OptionValue(new String("ChooseXValues")) );	

		StepanuvOption optionN = new StepanuvOption();
		optionN.setName("N");
		optionN.setSynopsis("number_of_values_to_try");
		optionN.setOption( new OptionValue(new Integer(5)) );
		
		Search search = new Search();
		search.addOption( Converter.toOption(optionSearchMethod) );
		search.addOption( Converter.toOption(optionN) );

		StepanuvOption optionEM = new StepanuvOption();
		optionEM.setName("evaluation_method");
		optionEM.setOption( new OptionValue(new String("CrossValidation")) );

		StepanuvOption optionOutput = new StepanuvOption();
		optionOutput.setName("output");
		optionOutput.setOption( new OptionValue(new String("evaluation_only")) );

		StepanuvOption optionF = new StepanuvOption();
		optionF.setName("F");
		optionF.setOption( new OptionValue(new Integer(10)) );
		
		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);
		complex.addOption( Converter.toOption(optionEM) );
		complex.addOption( Converter.toOption(optionOutput) );
		complex.addOption( Converter.toOption(optionF) );

		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataType("Data");
		computingDataSource.setDataProvider(complex);

        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        List<FileDataSaver> a = new ArrayList<FileDataSaver>();
        a.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(a);



		String fileName = Agent_GUIKlara.filePath + "input2"
				+ System.getProperty("file.separator")
				+ "input2.xml";

		comDescription.exportXML(fileName);
	}

}
