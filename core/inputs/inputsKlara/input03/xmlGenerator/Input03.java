package xmlGenerator;

import java.io.FileNotFoundException;

import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.batchDescription.CARecSearchComplex;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.value.StringValue;


public final class Input03 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input3 to Klara's input XML configuration file.");
		
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("?????.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent1 = new ComputingAgent();
		//comAgent1.setModelClass("Agent_?????.class.getName()");
		comAgent1.setTrainingData(fileDataSource);
		comAgent1.setTestingData(fileDataSource);

		NewOption optionEM1 = new NewOption(
				new StringValue("CrossValidation"),
				"evaluation_method");

		NewOption optionOutput1 = new NewOption(
				new StringValue("evaluation_only"),
				"output");

		CARecSearchComplex complex1 = new CARecSearchComplex();
		complex1.setComputingAgent(comAgent1);
		complex1.addOption(optionEM1);
		complex1.addOption(optionOutput1);

		DataSourceDescription computingDataSource1 = new DataSourceDescription();
		computingDataSource1.setDataOutputType("Data");
		computingDataSource1.setDataProvider(complex1);




		ComputingAgent comAgent2 = new ComputingAgent();
		//comAgent2.setModelClass("Agent_?????.class.getName()");
		comAgent2.setTrainingData(computingDataSource1);
		comAgent2.setTestingData(computingDataSource1);

		NewOption optionEM2 = new NewOption(
				new StringValue("evaluation_method"),
				"CrossValidation");

		NewOption optionOutput2 = new NewOption(
				new StringValue("evaluation_only"),
				"output");

		CARecSearchComplex complex2 = new CARecSearchComplex();
		complex2.setComputingAgent(comAgent2);
		complex2.addOption(optionEM2);
		complex2.addOption(optionOutput2);

		DataSourceDescription computingDataSource2 = new DataSourceDescription();
		computingDataSource2.setDataOutputType("Data");
		computingDataSource2.setDataProvider(complex2);



        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource2);

        ComputationDescription comDescription = new ComputationDescription();
        comDescription.addRootElement(saver);

		String fileName = Agent_GUIKlara.filePath + "input03"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);


	}

}
