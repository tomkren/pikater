package xmlGenerator;

import jade.util.leap.ArrayList;

import java.io.FileNotFoundException;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileDataSaver;


public final class Input1 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input1 to Klara's input XML configuration file.");

		StepanuvOption optionComMethod = new StepanuvOption();
		optionComMethod.setName("computing_method");
		optionComMethod.setSynopsis("computing_method");
		optionComMethod.setOption( new OptionValue(new String("RBFNetwork")) );
		
		StepanuvOption optionS = new StepanuvOption();
		optionS.setName("S");
		optionS.setOption( new OptionValue(new Integer(1)) );
		
		StepanuvOption optionM = new StepanuvOption();
		optionM.setName("M");
		optionM.setOption( new OptionValue(new Float(0.4)) );
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setOptions(new ArrayList());
		comAgent.addOption(optionS.toOption());
		comAgent.addOption(optionM.toOption());
		comAgent.addOption(optionComMethod.toOption());
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		comAgent.setModelClass(Agent_WekaCA.class.getName());
/*
		StepanuvOption optionSearchMethod = new StepanuvOption();
		optionSearchMethod.setName("search_method");
		optionSearchMethod.setOption( new OptionValue(new String("ChooseXValues")) );	

		StepanuvOption optionN = new StepanuvOption();
		optionN.setName("N");
		optionN.setSynopsis("number_of_values_to_try");
		optionN.setOption( new OptionValue(new Integer(5)) );
		
		Search search = new Search();
		search.setOptions(new ArrayList());
		search.addOption(optionSearchMethod.toOption());
		search.addOption(optionN.toOption());
*/
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
		complex.setOptions(new ArrayList());
		complex.addOption(optionEM.toOption());
		complex.addOption(optionOutput.toOption());
		complex.addOption(optionF.toOption());

		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataProvider(complex);

        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        ArrayList a = new ArrayList();
        a.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(a);

		String fileName = Agent_GUIKlara.filePath + "input1"
				+ System.getProperty("file.separator")
				+ "input1.xml";

		comDescription.exportXML(fileName);


	}

}
