package cz.tomkren.pikater;

import cz.tomkren.pikater.tests.Net01;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.Agent_Duration;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.duration.DurationService;
import org.pikater.core.agents.system.metadata.MetadataService;
import org.pikater.core.agents.system.openml.OpenMLAgentService;
import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.*;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.duration.Duration;
import org.pikater.core.ontology.subtrees.duration.GetDuration;
import org.pikater.core.ontology.subtrees.openml.Dataset;
import org.pikater.core.ontology.subtrees.task.KillTasks;
import org.pikater.shared.database.exceptions.DataSetConverterException;
import org.pikater.shared.database.jpa.status.JPADatasetSource;
import org.pikater.shared.database.util.DataSetConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * User-defined agent to communicate with users
 *
 */
public class AgentTom extends PikaterAgent {

	private static final long serialVersionUID = -3908734088006529947L;
	private static final boolean DEBUG_MODE = false;
	private BufferedReader bufferedConsole = null;

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(DurationOntology.getInstance());
		ontologies.add(AccountOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());
		ontologies.add(OpenMLOntology.getInstance());

		return ontologies;
	}
	
	/**
	 * Agent setup
	 */
	@Override
	protected void setup() {
		initDefault();
		registerWithDF(CoreAgents.GUI_KLARA_AGENT.getName());
		
		bufferedConsole = new BufferedReader(
				new InputStreamReader(System.in));

		if (DEBUG_MODE) {
			
			System.out.println("GUIKlara agent starts.");
			
			ITestExperiment testExperiment = null;
			try {
				testExperiment = CoreConfiguration.getCurrentKlaraInput(); 
				
			} catch (Exception e) {
				logException(String.format("Could not load current input from '%s'.", CoreConfiguration.getConfigurationFileName()), e);
				return;
			}
			
			runFile(testExperiment.getClass().getName(), testExperiment.createDescription());
			
		} else {
			
			try {
				runAutomat();
			} catch (IOException e) {
				logException("Error with console in KlaraGUI", e);
			} catch (Exception e) {
				logException("General error...", e);
			}
			
		}
	}
	
	BufferedReader inputReader;

	/**
	 * Run user interactive automat to offers options and actions
	 * 
	 * @throws IOException 
	 * @throws DataSetConverterException 
	 */
	private void runAutomat() throws IOException, DataSetConverterException {

		System.out.println("Bond here!");

		if (!checkConsole()) {return;}

		//if (!checkPassword("123")) {return;}

		System.out.println(" I welcome you Klara !!! " + "Do you welcome me? ");

		doWait(8000);
		askPresetExample();

		while (true) {

			System.out.print(">");
			
			String input = bufferedConsole.readLine();
			
			if (input.equals("--help")) {
				System.out.println(" Help:\n" +
						" Help             --help\n" +
						" Shutdown         --shutdown\n" +
						" Kill Batch       --kill [batchID]\n" +
						" Add dataset      --add-dataset [username] "
						+ "[description] <path>\n" +
						" Get duration     --dura\n"+
						" For test purposes --test \n"+
						" OpenML interaction openml [list|import|export|help]\n" +
						" Run Experiment   --run <file.xml>\n"
						);
			
			} else if (input.startsWith("--shutdown")) {
				break;
				
			} else if(input.startsWith("--dura")) {
				printDurationAgentResponse();
				
			} else if (input.startsWith("--run")) {
				int index = input.indexOf("--run") + "--run ".length();
				String fileName = input.substring(index);
				String filePath = CoreConfiguration.getCurrentKlaraInputPath() + fileName;
				
				runFile(filePath, ComputationDescription.importXML(new File(filePath)));

			} else if (input.startsWith("--add-dataset")) {
				addDataset(input);
				
			} else if (input.startsWith("--kill")) {
				killBatch(input);
				
			} else if (input.startsWith("openml")) {
				openmlCommands(input);
				
			} else {
				System.out.println(
						"Sorry, I don't understand you. \n" +
						" --help"
						);
			}
		}

	}

	private boolean checkConsole() {
		if (bufferedConsole == null) {

			System.out.println("Error, console not found.");

			try {
				DFService.deregister(this);

				System.out.println("Agent, will be termined.");
				doDelete();

			} catch (FIPAException e) {
				logException(e.getMessage(), e);
			}
			return false;
		}
		return true;
	}

	private boolean checkPassword(String correctPassword) throws IOException {
		System.out.println("Please enter your password: ");
		String inputPassword = bufferedConsole.readLine();

		if(!inputPassword.equals(correctPassword)){
			System.err.println("Sorry, you're not Klara");
			return false;
		}

		return true;
	}

	private void askPresetExample() throws IOException {

		ITestExperiment testExperiment = new Net01();

		System.out.println(String.format(" Do you wish to run experiment '%s' ? (y/n)", testExperiment.getClass().getName()));
		System.out.print(">");

		if (bufferedConsole.readLine().equals("y")) {
			runFile(testExperiment.getClass().getName(), testExperiment.createDescription());
		}
	}

	private void askPresetExample_old() throws IOException {

		ITestExperiment testExperiment;
		try {
			testExperiment = CoreConfiguration.getCurrentKlaraInput();

		} catch (Exception e) {
			logException(String.format("Could not load current input from '%s'.", CoreConfiguration.getConfigurationFileName()), e);
			return;
		}

		System.out.println(String.format(" Do you wish to run experiment '%s' ? (y/n)", testExperiment.getClass().getName()));
		System.out.print(">");

		if (bufferedConsole.readLine().equals("y")) {
			runFile(testExperiment.getClass().getName(), testExperiment.createDescription());
		}
	}
	
	private void openmlCommands(String command) {
		String[] commands = command.split(" ");
		
		if (commands.length < 2) {
		   System.out.println("Not enough parameters");
		   return;
		}
		
		String cmd = commands[1]; // list or import or export
		if("list".equals(cmd)) {
			System.out.println("Getting list of available datasets at OpenMl.org");
			
			List<Dataset> availableDatasets = OpenMLAgentService.getDatasets(this);
			
			if(availableDatasets == null) {
				System.out.println("No datasets were found");
				return;
			}
			System.out.println("Found "+availableDatasets.size()+" datasets");
			System.out.println(String.format("%-5s %-40s %-7s %-20s","DID","Name","Type","Date"));
			for(Dataset d : availableDatasets) {
				System.out.println(String.format("%-5s %-40s %-7s %-20s",d.getDid(),d.getName(),d.getType(),d.getDate()));
			}
			
			return;
		} else if("import".equals(cmd)) {
			
			if(commands.length < 4) {
				System.out.println("You must define dataset ID (did) and destination file of the OpenMl.org dataset you wish to retrieve");
				return;
			}
			
			
			String sDid = commands[2];
			int did = -1;
			try {
				did = Integer.parseInt(sDid);
			} catch(NumberFormatException nfe){
				System.out.println(sDid + " is not a valid number");
			}
			
			String dst = commands[3];
	
			String downloadedPath = OpenMLAgentService.importDataset(this, did, dst);
			
			if (downloadedPath == null) {
				System.out.println("Something went wrong. You may check the DID you entered");
			} else {
				System.out.println("Dataset with DID " + did + " was downloaded to " + downloadedPath);
			}
			
		} else if("help".equals(cmd)) {
			System.out.println("List available datasets: openml list");
			System.out.println("Export dataset to OpenML.org: openml export <path> [name] [description] [type]");
			System.out.println("Import dataset from OpenML.org: openml import <did> <destination>");
			
		} else if("export".equals(cmd)) {
			
			if (commands.length < 3) {
				System.out.println("You must at least define path to the local filename");
				return;
			}
			
			String path = commands[2];
			
			String name = "";
			//if user added at least 4 commands openml+export+path+something, them we set name as the 4th parameter
			if (commands.length>=4){
				name = commands[3];
			}
			
			String description = "";
			if (commands.length >=5 ) {
				description = commands[4];
			}
			
			String type = "";
			if (commands.length >= 6) {
				type = commands[5];
			}
			
			int uploaded = OpenMLAgentService.exportDataset(this, name, description, path, type);
			if (uploaded == -1) {
				System.out.println("Something went wrong, when we tried to upload your file. Sorry :(");
			} else {
				System.out.println("OpenML ID of your dataset: " + uploaded);
			}
			
		} else {
			System.out.println("\""+cmd+"\" is invalid command for OpenML interaction.");
		}
	}

	/**
	 * Sends respond to kill all Task from Batch
	 * 
	 * @param input - input line
	 */
	private void killBatch(String input) {
		String[] cmd = input.split(" ");
		int batchID = -1;
		try {
			batchID=Integer.parseInt(cmd[cmd.length-1]);
			
			KillTasks killBatch = new KillTasks();
	        killBatch.setBatchID(batchID);

			Ontology taskOntology = TaskOntology.getInstance();

			AID planner = new AID(CoreAgents.PLANNER.getName(), false);

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(planner);
			request.setLanguage(getCodec().getName());
			request.setOntology(taskOntology.getName());
			
			Action action = new Action(planner, killBatch);
			getContentManager().fillContent(request, action);

			ACLMessage reply =
					FIPAService.doFipaRequestClient(this, request, 10000);

			if ((reply != null) &&
				(reply.getPerformative() == ACLMessage.INFORM)) {
				System.out.println("Planner responded Inform");
			} else {
				System.err.println("Planner couldn't perform the action");
			}
		} catch (CodecException | OntologyException | FIPAException e) {
			e.printStackTrace();
		} catch(NumberFormatException nfe) {
			System.err.println("Wrong number format of Batch ID");
		}
		
	}

	/**
	 * Prints Duration, information receives from {@link Agent_Duration}
	 */
	private void printDurationAgentResponse() {
		Duration duration =
				DurationService.getDuration(this, new GetDuration());
		
		String start;
		if (duration.getStart() != null) {
			start = duration.getStart().toString();
		} else {
			start = "no data  ";
		}
		
		logInfo(
				"Last Duration info - Start " +
				start + ":" + duration.getDurationMiliseconds() +
				" ms , LR: " + duration.getLR_duration()
				);
	}

	/**
	 * Loads a XML file, converts the XML structure to
	 * {@link ComputationDescription}, sends request to run this batch
	 * 
	 * @param file - name of XML file which contains new {@link Batch}
	 * @throws FileNotFoundException
	 */
	private void runFile(String file, ComputationDescription experiment) {
		System.out.println("Loading experiment from file: " + file);
		
		ExecuteBatch executeBatch = new ExecuteBatch(experiment);

		// waiting to start of all agents
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e1) {
			logException(e1.getMessage(), e1);
		}

        AID receiver = new AID(
        		CoreAgents.GUI_AGENTS_COMMUNICATOR.getName(), false);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAID());
        msg.addReceiver(receiver);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(ontology.getName());

        try {
        	Action action = new Action(receiver, executeBatch);
			getContentManager().fillContent(msg, action);
			
			ACLMessage reply =
					FIPAService.doFipaRequestClient(this, msg, 10000);
			
			String replyText;
			if (reply != null) {
				replyText = reply.getContent();
			} else {
				replyText = "null";
			}
			
			logInfo("Reply: " + replyText);
			
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		} catch (FIPAException e) {
			logException(e.getMessage(), e);
		}

	}
	
	/**
	 * Ensures the dataset conversion and interaction with user
	 * 
	 * @param file to convert
	 * @return The filename of the file to be saved
	 * @throws IOException 
	 * @throws DataSetConverterException 
	 */
	private String testAndAskForConversion(File file
			) throws IOException, DataSetConverterException {
		
		String path = file.getAbsolutePath().toLowerCase();
		
		if (path.endsWith("arff")) {
			return file.getAbsolutePath();
		} else {
			String newPath = "";
			int inputType = -1;
			
			if (path.endsWith("xls")) {
				newPath = file.getAbsolutePath().substring(0,
						path.lastIndexOf("xls")) + "arff";
				inputType = 0;
				System.out.println("Input recognised as Excel "
						+ "(XLS) spreadsheet");
				
			} else if(path.endsWith("xlsx")) {
				newPath = file.getAbsolutePath().substring(0,
						path.lastIndexOf("xlsx")) + "arff";
				inputType = 1;
				System.out.println("Input recognised as Excel "
						+ "2007 (XLSX) spreadsheet");
			} else {
				System.err.println("Not supported input file format!"
						+ "\nPlease use ARFF,XLS or XLSX formats");
				return null;
			}
			
			if ((inputType == 0) || (inputType==1)) {
				System.out.println(
						"Do you want to convert the document? (y/n)\n" +
						"DOCUMENT WIHT FOLLOWING PATH WILL BE OVERWRITTEN:" +
						" " + newPath);
			}
			String answer = bufferedConsole.readLine();
			if (answer != null && answer.equalsIgnoreCase("y")) {
				
				System.out.println("Do you want to define any header "
						+ "file? (path / -)");
				
				answer = bufferedConsole.readLine();
				if (answer != null && !answer.equals("-")) {
					String headerPath = answer;
					
					if(inputType == 0) {
						DataSetConverter.xlsToArff(
								new File(headerPath),
								new File(path),
								new File(newPath));
					} else if(inputType == 1) {
						DataSetConverter.xlsxToArff(
								new File(headerPath),
								new File(path),
								new File(newPath));
					}
				} else {
					if(inputType == 0){
						DataSetConverter.xlsToArff(
								new File(path),
								new File(newPath));
						
					} else if(inputType == 1){
						DataSetConverter.xlsxToArff(
								new File(path),
								new File(newPath));
					}
				}
				return newPath;
			}
			return null;
		}
	}
	
	/**
	 * Inserts a new dataset to system
	 * @throws DataSetConverterException 
	 * @throws IOException 
	 */
	private void addDataset(String cmd
			) throws IOException, DataSetConverterException {
		
		int dataSetID = -1;
		int userID = -1;
		
		String[] cmdA=cmd.split(" ");
		if (cmdA.length == 4) {
			
			String username = cmdA[1];
			String description = cmdA[2];
			String filename = testAndAskForConversion(new File(cmdA[3]));
			userID = DataManagerService.getUserID(this, username);
			
			if (filename == null) {
				return;
			}
			dataSetID = DataManagerService.sendRequestSaveDataSet(this,
					filename, userID, description, JPADatasetSource.USER_UPLOAD);
			
		} else if (cmdA.length == 3) {
			
			String username = cmdA[1];
			String description = "Dataset saved in KlaraGui";
			String filename = testAndAskForConversion(new File(cmdA[2]));
			userID = DataManagerService.getUserID(this, username);
			
			if (filename == null) {
				return;
			}
			dataSetID = DataManagerService.sendRequestSaveDataSet(this,
					filename, userID, description, JPADatasetSource.USER_UPLOAD);
			
		} else if (cmdA.length == 2) {
			String username="klara";
			String description="Dataset saved in KlaraGui";
			String filename=testAndAskForConversion(new File(cmdA[1]));
			userID = DataManagerService.getUserID(this, username);
			
			if (filename == null) {
				return;
			}
			dataSetID = DataManagerService.sendRequestSaveDataSet(this,
					filename, userID, description, JPADatasetSource.USER_UPLOAD);
		} else {
			System.err.println("Wrong parameters");
		}
		
		if (dataSetID != -1) {
			MetadataService.requestMetadataForDataset(this, dataSetID, userID);
		}
	}
	
}

