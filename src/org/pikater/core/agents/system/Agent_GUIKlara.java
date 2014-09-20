package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.duration.DurationService;
import org.pikater.core.agents.system.metadata.MetadataService;
import org.pikater.core.ontology.AccountOntology;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.DurationOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.dataset.SaveDataset;
import org.pikater.core.ontology.subtrees.duration.Duration;
import org.pikater.core.ontology.subtrees.duration.GetDuration;
import org.pikater.core.ontology.subtrees.task.KillTasks;
import org.pikater.shared.database.util.DataSetConverter;


public class Agent_GUIKlara extends PikaterAgent {

	private static final long serialVersionUID = -3908734088006529947L;
	private static final boolean DEBUG_MODE = true;
	private BufferedReader bufferedConsole=null;

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(DurationOntology.getInstance());
		ontologies.add(AccountOntology.getInstance());
		
		//for test purposes
		ontologies.add(ModelOntology.getInstance());
		
		return ontologies;
	}
	
	@Override
	protected void setup() {
		initDefault();
		registerWithDF(CoreAgents.GUI_KLARA_AGENT.getName());
		
		bufferedConsole=new BufferedReader(new InputStreamReader(System.in));

		if (DEBUG_MODE) {
			
			System.out.println("GUIKlara agent starts.");
			
			try {
				runFile(CoreConfiguration.getKlarasInputsPath() + "input.xml");
				
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
				logException("File not found.", e);
			}
			
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

	private void runAutomat() throws Exception {
		
		System.out.println(
				"--------------------------------------------------------------------------------\n" +
				"| System Pikater: Multiagents system                                           |\n" +
				"--------------------------------------------------------------------------------\n" +
				"  Hi I'm Klara's GUI console Agent ..." +
				"\n"
				);

		if (bufferedConsole == null) {

			System.out.println("Error, console not found.");

			try {
				DFService.deregister(this);
				
				System.out.println("Agent, will be termined.");
				doDelete();
				
			} catch (FIPAException e) {
				logException(e.getMessage(), e);
			}
			return;
		}

		System.out.println("Please enter your password: ");
		String inputPassword=bufferedConsole.readLine();
		String correctPassword="123";
		
		if(!inputPassword.equals(correctPassword)){
			System.err.println("Sorry, you're not Klara");
			return;
		}
		
		System.out.println(" I welcome you Klara !!!");

		String defaultFileName = "input.xml";
		File testFile = new File(CoreConfiguration.getKlarasInputsPath() + defaultFileName);

		if(testFile.exists() && !testFile.isDirectory()) {

			System.out.println(" Do you wish to run experiment from file "
					+ CoreConfiguration.getKlarasInputsPath() + defaultFileName + " ? (y/n)");
			System.out.print(">");

			if (bufferedConsole.readLine().equals("y")) {
				try {
					runFile(CoreConfiguration.getKlarasInputsPath() + defaultFileName);
					return;
				} catch (FileNotFoundException e) {
					System.out.println(" File not found.");
				}
			}

		}

		while (true) {

			System.out.print(">");
			
			String input = bufferedConsole.readLine();
			
			if (input.equals("--help")) {
				System.out.println(" Help:\n" +
						" Help             --help\n" +
						" Shutdown         --shutdown\n" +
						" Kill Batch       --kill [batchID]\n" +
						" Add dataset      --add-dataset [username] [description] <path>\n" +
						" Get duration     --dura\n"+
						" For test purposes --test "+
						" Run Experiment   --run <file.xml>\n"
						);
			
			} else if (input.startsWith("--shutdown")) {
				break;
			} else if(input.startsWith("--dura")){
				printDurationAgentResponse();
			} else if (input.startsWith("--run"))
			{
				// TODO: what about this?
			} else if (input.startsWith("--add-dataset")) {
				addDataset(input);
			} else if (input.startsWith("--kill")) {
				killBatch(input);
			} else {
				System.out.println(
						"Sorry, I don't understand you. \n" +
						" --help"
						);
			}
		}

	}

	private void killBatch(String input) {
		String[] cmd=input.split(" ");
		int batchID=-1;
		try{
			batchID=Integer.parseInt(cmd[cmd.length-1]);
			
			KillTasks killBatch = new KillTasks();
	        killBatch.setBatchID(batchID);

			Ontology taskOntology = TaskOntology.getInstance();

			try {
				AID planner = new AID(CoreAgents.PLANNER.getName(), false);

				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.addReceiver(planner);
				request.setLanguage(getCodec().getName());
				request.setOntology(taskOntology.getName());
				getContentManager().fillContent(request, new Action(planner, killBatch));

				ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);

				if((reply!=null)&&(reply.getPerformative()==ACLMessage.INFORM)){
					System.out.println("Planner responded Inform");
				}else{
					System.err.println("Planner couldn't perform the action");
				}
            
			} catch (CodecException | OntologyException | FIPAException e) {
				e.printStackTrace();
			}
           
		}catch(NumberFormatException nfe){
			System.err.println("Wrong number format of Batch ID");
		}
		
	}

	private void printDurationAgentResponse() {
		Duration dur = DurationService.getDuration(this, new GetDuration());
		logInfo("Last Duration info - Start "+(dur.getStart()!=null? dur.getStart().toString() : "no data  ")+":"+ dur.getDurationMiliseconds()+" ms , LR: "+dur.getLR_duration());
	}

	private void runFile(String fileName) throws FileNotFoundException {

		System.out.println("Loading experiment from file: " + fileName);
		
		ComputationDescription comDescription =
				ComputationDescription.importXML(new File(fileName));


		ExecuteBatch executeBatch = new ExecuteBatch(comDescription);

		// waiting to start of all agents
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e1) {
			logException(e1.getMessage(), e1);
		}

        AID receiver = new AID(CoreAgents.GUI_AGENTS_COMMUNICATOR.getName(), false);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAID());
        msg.addReceiver(receiver);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(ontology.getName());

        try {
			getContentManager().fillContent(msg, new Action(receiver, executeBatch));
			
			ACLMessage reply = FIPAService.doFipaRequestClient(this, msg, 10000);
			String replyText = (reply!=null)?reply.getContent():"null";
			
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
	 * 
	 * @param file
	 * @return The filename of the file to be saved
	 * @throws Exception 
	 */
	private String testAndAskForConversion(File file) throws Exception{
		String path = file.getAbsolutePath().toLowerCase();
		if(path.endsWith("arff")){
			return file.getAbsolutePath();
		}else{
			String newPath="";
			int inputType=-1;
			if(path.endsWith("xls")){
				newPath=file.getAbsolutePath().substring(0, path.lastIndexOf("xls"))+"arff";
				inputType=0;
				System.out.println("Input recognised as Excel (XLS) spreadsheet");
			}else if(path.endsWith("xlsx")){
				newPath=file.getAbsolutePath().substring(0, path.lastIndexOf("xlsx"))+"arff";
				System.out.println("Input recognised as Excel 2007 (XLSX) spreadsheet");
				inputType=1;
			}else{
				System.err.println("Not supported input file format!\nPlease use ARFF,XLS or XLSX formats");
				return null;
			}
			
			if((inputType==0)||(inputType==1)){
				System.out.println("Do you want to convert the document? (y/n)\nDOCUMENT WIHT FOLLOWING PATH WILL BE OVERWRITTEN: "+newPath);
			}
			String answer=bufferedConsole.readLine();
			if(answer != null && answer.equalsIgnoreCase("y")){
				System.out.println("Do you want to define any header file? (path / -)");
				answer=bufferedConsole.readLine();
				if(answer != null && !answer.equals("-")){
					String headerPath=answer;
					if(inputType==0){
						DataSetConverter.xlsToArff(new File(headerPath),new File(path), new File(newPath));
					}else if(inputType==1){
						DataSetConverter.xlsxToArff(new File(headerPath),new File(path), new File(newPath));
					}
				}else{
					if(inputType==0){
						DataSetConverter.xlsToArff(new File(path), new File(newPath));
					}else if(inputType==1){
						DataSetConverter.xlsxToArff(new File(path), new File(newPath));
					}
				}
				return newPath;
			}
			return null;
		}
	}
	
	private void addDataset(String cmd) throws Exception{
		int dataSetID = -1;
		int userID = -1;
		
		String[] cmdA=cmd.split(" ");
		if(cmdA.length==4){
			String username=cmdA[1];
			String description=cmdA[2];
			String filename=testAndAskForConversion(new File(cmdA[3]));
			userID = DataManagerService.getUserID(this, username);
			if(filename==null) return;
			dataSetID = this.sendRequestSaveDataSet(filename, userID, description);
		}else if(cmdA.length==3){
			String username=cmdA[1];
			String description="Dataset saved in KlaraGui";
			String filename=testAndAskForConversion(new File(cmdA[2]));
			userID = DataManagerService.getUserID(this, username);
			if(filename==null) return;
			dataSetID = this.sendRequestSaveDataSet(filename, userID, description);
		}else if(cmdA.length==2){
			String username="klara";
			String description="Dataset saved in KlaraGui";
			String filename=testAndAskForConversion(new File(cmdA[1]));
			userID = DataManagerService.getUserID(this, username);
			if(filename==null) return;
			dataSetID = this.sendRequestSaveDataSet(filename, userID, description);
		}else{
			System.err.println("Wrong parameters");
		}
		
		if(dataSetID!=-1){
			MetadataService.requestMetadataForDataset(this, dataSetID, userID);
		}
	}
	
	private int sendRequestSaveDataSet(String filename, int userID, String description){
		try {
        	AID dataManager = new AID(CoreAgents.DATA_MANAGER.getName(), false);
    		Ontology ontology = DataOntology.getInstance();
    		SaveDataset sd = new SaveDataset();
    		sd.setUserID(userID);
    		sd.setSourceFile(filename);
    		sd.setDescription(description);
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(dataManager);
            request.setLanguage(getCodec().getName());
            request.setOntology(ontology.getName());
            getContentManager().fillContent(request, new Action(dataManager, sd));
           
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
            if (reply == null){
                logSevere("Reply not received.");
                return -1;
            }
            else{
                logInfo("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
            	return (Integer)reply.getContentObject();
            }
        } catch (CodecException e) {
            logException("Codec error occurred: "+e.getMessage(), e);
        } catch (OntologyException e) {
            logException("Ontology error occurred: "+e.getMessage(), e);
        } catch (FIPAException e) {
            logException("FIPA error occurred: "+e.getMessage(), e);
        } catch (UnreadableException e) {
        	logException(e.getMessage(), e);
		}
		return -1;
	}
}

