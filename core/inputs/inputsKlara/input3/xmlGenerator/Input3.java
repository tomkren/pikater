package xmlGenerator;

import java.io.FileNotFoundException;

import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.Converter;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.description.CARecSearchComplex;
import org.pikater.core.ontology.subtrees.description.ComputationDescription;
import org.pikater.core.ontology.subtrees.description.ComputingAgent;
import org.pikater.core.ontology.subtrees.description.DataSourceDescription;
import org.pikater.core.ontology.subtrees.description.FileDataProvider;
import org.pikater.core.ontology.subtrees.description.FileDataSaver;


public final class Input3 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input3 to Klara's input XML configuration file.");
		
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("?????.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent1 = new ComputingAgent();
		comAgent1.setModelClass("Agent_?????.class.getName()");
		comAgent1.setTrainingData(fileDataSource);
		comAgent1.setTestingData(fileDataSource);

		StepanuvOption optionEM1 = new StepanuvOption();
		optionEM1.setName("evaluation_method");
		optionEM1.setOption( new OptionValue(new String("CrossValidation")) );

		StepanuvOption optionOutput1 = new StepanuvOption();
		optionOutput1.setName("output");
		optionOutput1.setOption( new OptionValue(new String("evaluation_only")) );

		CARecSearchComplex complex1 = new CARecSearchComplex();
		complex1.setComputingAgent(comAgent1);
		complex1.addOption( Converter.toOption(optionEM1) );
		complex1.addOption( Converter.toOption(optionOutput1) );

		DataSourceDescription computingDataSource1 = new DataSourceDescription();
		computingDataSource1.setDataType("Data");
		computingDataSource1.setDataProvider(complex1);




		ComputingAgent comAgent2 = new ComputingAgent();
		comAgent2.setModelClass("Agent_?????.class.getName()");
		comAgent2.setTrainingData(computingDataSource1);
		comAgent2.setTestingData(computingDataSource1);

		StepanuvOption optionEM2 = new StepanuvOption();
		optionEM2.setName("evaluation_method");
		optionEM2.setOption( new OptionValue(new String("CrossValidation")) );

		StepanuvOption optionOutput2 = new StepanuvOption();
		optionOutput2.setName("output");
		optionOutput2.setOption( new OptionValue(new String("evaluation_only")) );

		CARecSearchComplex complex2 = new CARecSearchComplex();
		complex2.setComputingAgent(comAgent2);
		complex2.addOption( Converter.toOption(optionEM2) );
		complex2.addOption( Converter.toOption(optionOutput2) );

		DataSourceDescription computingDataSource2 = new DataSourceDescription();
		computingDataSource2.setDataType("Data");
		computingDataSource2.setDataProvider(complex2);



        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource2);

        ComputationDescription comDescription = new ComputationDescription();
        comDescription.addRootElement(saver);

		String fileName = Agent_GUIKlara.filePath + "input3"
				+ System.getProperty("file.separator")
				+ "input3.xml";

		comDescription.exportXML(fileName);


	}

}
