package pikater.utility.boxTypes;

import jade.domain.FIPAAgentManagement.Search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pikater.ontology.description.ComputingAgent;
import pikater.ontology.description.DifferenceVisualizer;
import pikater.ontology.description.FileDataProvider;
import pikater.ontology.description.FileVisualizer;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UDoubleInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UDoubleParameter;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerParameter;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UStringParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataProviderSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.ParametersProviderSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class BoxesModel {
	
	private ArrayList<Box> boxes = new ArrayList<Box>();

	public String fileInput = "Agent_FileInput";
	public String randomSearch = "Agent_RandomSearch";
	public String simulatedAnnealing = "Agent_SimulatedAnnealing";
	public String crossValidation = "Agent_CrossValidation";
	public String visualizer = "Agent_Visualizer";
	public String differenceVisualizer = "Agent_DifferenceVisualizer";
	

	public ArrayList<Box> getBoxes() {
		return boxes;
	}
	public void setBoxes(ArrayList<Box> boxes) {
		this.boxes = boxes;
	}

	public Box getBoxByName(String name) {
		
		for (Box box : boxes) {
			if (box.getName() == name) {
				return box;
			}
		}

		return null;
	}


	public void exportXML() throws IOException {
		
		for (Box boxI : boxes) {

			File fileI = new File("src/" + boxI.getAgentName() + ".xml");
			boxI.exportXML(fileI);
		}
	}

	public static BoxesModel importXML() throws IOException {

		//File file = new File("src/" + "pikater/Agent_FileInput" + ".xml");
		//Box b = Box.importXML(file);

		ArrayList<Box> boxes = Export.declareBoxes();

		BoxesModel model = new BoxesModel();
		model.setBoxes(boxes);

		return model;
	}

}


class Export {

	private static String INPUT_TYPE = "Input";
	private static String SEARCHER_TYPE = "Searcher";
	private static String COMPUTING_TYPE = "Computing";
	private static String VISUALIZER_TYPE = "Visualizer";


	private static Box getInputBox() {

		String boxFilename =
				"pikater/Agent_FileInput";

		UStringParameter boxParam_File = new UStringParameter();
		boxParam_File.setName("FileName");
		boxParam_File.setValue("file.txt");

		UniversalSlot boxDataOutputSlots = new DataProviderSlot();
		boxDataOutputSlots.setSlotName("Dataset");

		Box box = new Box();
		box.setName("FileInput");
		box.setType(INPUT_TYPE);
		box.setOntology(FileDataProvider.class);
		box.setAgentName(boxFilename);
		box.addParameter(boxParam_File);
		box.addOutputSlot(boxDataOutputSlots);
		box.setPicture("picture0.jpg");
		box.setDescription("Box is a file data source.");

		return box;
	}

	private static Box getRandomSearchBox() {

		String boxFilename =
				"pikater/Agent_RandomSearch";

		UDoubleInterval intervalE = new UDoubleInterval();
		intervalE.setMin(0.0);
		intervalE.setMax(1.0);
		UDoubleParameter boxParam_E = new UDoubleParameter();
		boxParam_E.setName("E");
		boxParam_E.setValue(0.01);
		boxParam_E.setRange(intervalE);

		UIntegerInterval intervalM = new UIntegerInterval();
		intervalM.setMin(1);
		intervalM.setMax(100000);
		UIntegerParameter boxParam_M = new UIntegerParameter();
		boxParam_M.setValue(10);
		boxParam_M.setName("M");
		boxParam_M.setRange(intervalM);

		UniversalSlot boxparametersOutputSlots = new ParametersProviderSlot();
		boxparametersOutputSlots.setSlotName("Found parameters");

		Box box = new Box();
		box.setName("Random-Searcher");
		box.setType(SEARCHER_TYPE);
		box.setOntology(Search.class);
		box.setAgentName(boxFilename);
		box.addParameter(boxParam_E);
		box.addParameter(boxParam_M);
		box.addOutputSlot(boxparametersOutputSlots);
		box.setPicture("picture1.jpg");
		box.setDescription("Searcher is using to find values"
				+ " of parameters for computing agents. For search"
				+ "the solution is used random generator.");

		return box;
	}

	private static Box getSimulatedAnnealingBox() {

		String boxFilename =
				"pikater/Agent_SimulatedAnnealing";

		UDoubleInterval intervalE = new UDoubleInterval();
		intervalE.setMin(0.0);
		intervalE.setMax(1.0);
		UDoubleParameter boxParam_E = new UDoubleParameter();
		boxParam_E.setName("E");
		boxParam_E.setValue(0.1);
		boxParam_E.setRange(intervalE);

		UIntegerInterval intervalM = new UIntegerInterval();
		intervalM.setMin(1);
		intervalM.setMax(1000);
		UIntegerParameter boxParam_M = new UIntegerParameter();
		boxParam_M.setName("M");
		boxParam_M.setValue(50);
		boxParam_M.setRange(intervalM);

		UDoubleInterval intervalT = new UDoubleInterval();
		intervalT.setMin(0.0);
		intervalT.setMax(100.0);
		UDoubleParameter boxParam_T = new UDoubleParameter();
		boxParam_T.setName("T");
		boxParam_T.setValue(1);
		boxParam_T.setRange(intervalT);

		UDoubleInterval intervalS = new UDoubleInterval();
		intervalS.setMin(0.0);
		intervalS.setMax(1.0);
		UDoubleParameter boxParam_S = new UDoubleParameter();
		boxParam_S.setName("S");
		boxParam_S.setValue(0.5);
		boxParam_S.setRange(intervalS);

		UniversalSlot boxparametersOutputSlots = new ParametersProviderSlot();
		boxparametersOutputSlots.setSlotName("Found parameters");

		Box box = new Box();
		box.setName("SimulatedAnnealing-Searcher");
		box.setType(SEARCHER_TYPE);
		box.setAgentName(boxFilename);
		box.addParameter(boxParam_E);
		box.addParameter(boxParam_M);
		box.addParameter(boxParam_S);
		box.addParameter(boxParam_T);
		box.addOutputSlot(boxparametersOutputSlots);
		box.setPicture("picture2.jpg");
		box.setDescription("Searcher is using to find values "
				+ "of parameters for computing agents. For search the "
				+ "solution is used method simulated annaling.");

		return box;
	}


	private static Box getCrossValidationBox() {

		String boxFilename =
				"pikater/Agent_CrossValidation";

		UIntegerInterval interval = new UIntegerInterval();
		interval.setMin(1);
		interval.setMax(100);
		UIntegerParameter boxParam_F = new UIntegerParameter();
		boxParam_F.setName("F");
		boxParam_F.setValue(5);
		boxParam_F.setRange(interval);

		UniversalSlot boxTrainingDataInputSlots = new DataConsumerSlot();
		boxTrainingDataInputSlots.setSlotName("Training data");

		UniversalSlot boxTestingDataInputSlots = new DataConsumerSlot();
		boxTestingDataInputSlots.setSlotName("Testing data");

		UniversalSlot boxValidationDataInputSlots = new DataConsumerSlot();
		boxValidationDataInputSlots.setSlotName("Validation data");

		UniversalSlot boxdataOutputSlots = new DataProviderSlot();
		boxdataOutputSlots.setSlotName("Computed data");

		Box box = new Box();
		box.setName("CrossValidation-Method");
		box.setType(COMPUTING_TYPE);
		box.setOntology(ComputingAgent.class);
		box.setAgentName(boxFilename);
		box.addParameter(boxParam_F);
		box.addInputSlot(boxTrainingDataInputSlots);
		box.addInputSlot(boxTestingDataInputSlots);
		box.addInputSlot(boxValidationDataInputSlots);
		box.addOutputSlot(boxdataOutputSlots);
		box.setPicture("picture3.jpg");
		box.setDescription("Computing agent which used for training neural "
				+ "networks deterministic library WEKA. As a training method "
				+ "is used default Cross Validation WEKA method.");

		return box;
	}

	private static Box getVisualizerBox() {

		String boxFilename =
				"pikater/Agent_Visualizer";

		UniversalSlot boxDataInputSlots = new DataConsumerSlot();
		boxDataInputSlots.setSlotName("Data Source");

		Box box = new Box();
		box.setName("Visualizer");
		box.setType(VISUALIZER_TYPE);
		box.setOntology(FileVisualizer.class);
		box.setAgentName(boxFilename);
		box.addInputSlot(boxDataInputSlots);
		box.setPicture("picture4.jpg");
		box.setDescription("Visualiser shows dates from dataSource.");

		return box;
	}

	private static Box getDifferenceVisualizerBox() {

		String boxFilename =
				"pikater/Agent_DifferenceVisualizer";

		UniversalSlot boxTargetInputSlots = new DataConsumerSlot();
		boxTargetInputSlots.setSlotName("Target Data Source");

		UniversalSlot boxModelInputSlots = new DataConsumerSlot();
		boxModelInputSlots.setSlotName("Model Data Source");

		Box box = new Box();
		box.setName("DifferenceVisualizer");
		box.setType(VISUALIZER_TYPE);
		box.setOntology(DifferenceVisualizer.class);
		box.setAgentName(boxFilename);
		box.addInputSlot(boxTargetInputSlots);
		box.addInputSlot(boxModelInputSlots);
		box.setPicture("picture5.jpg");
		box.setDescription("Visualiser shows difference between "
				+ "target data input and model data input.");

		return box;
	}

	public static ArrayList<Box> declareBoxes()  {

		ArrayList<Box> boxes = new ArrayList<Box>();
		boxes.add(getInputBox());
		boxes.add(getRandomSearchBox());
		boxes.add(getSimulatedAnnealingBox());
		boxes.add(getCrossValidationBox());
		boxes.add(getVisualizerBox());
		boxes.add(getDifferenceVisualizerBox());
		
		return boxes;
	}
	
}
