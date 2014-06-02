package org.pikater.core.agents.system;

import jade.content.AgentAction;
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
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.dataset.SaveDataset;
import org.pikater.core.ontology.subtrees.metadata.NewDataset;


public class Agent_GUIKlara extends PikaterAgent {

	private static final long serialVersionUID = -3908734088006529947L;
	private static final boolean DEBUG_MODE = false;
	private BufferedReader bufferedConsole=null;

	public static String filePath = "core"
			+ System.getProperty("file.separator") + "inputs"
			+ System.getProperty("file.separator") + "inputsKlara"
			+ System.getProperty("file.separator");

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		
		return ontologies;
	}
	
	@Override
	protected void setup() {
		initDefault();
		registerWithDF();
		
		bufferedConsole=new BufferedReader(new InputStreamReader(System.in));

		if (DEBUG_MODE) {
			
			System.out.println("GUIKlara agent starts.");
			
			try {
				runFile(filePath + "input.xml");
				
			} catch (FileNotFoundException e) {
				System.out.println(" File not found.");
			}
			
		} else {
			
			try {
				runAutomat();
			} catch (IOException e) {
				logError("Error with console in KlaraGUI");
			}
			
		}
	}

	private void runAutomat() throws IOException {
		
		System.out.println(
				"--------------------------------------------------------------------------------\n" +
				"| System Pikater: Multiagents system                                           |\n" +
				"--------------------------------------------------------------------------------\n" +
				"  Hi I'm Klara's GUI console Agent ..." +
				"\n"
				);

		//Changed for reason, that Eclipse doesn't support System.console()
		if (bufferedConsole == null) {

			System.out.println("Error, console not found.");

			try {
				DFService.deregister(this);
				
				System.out.println("Agent, will be termined.");
				doDelete();
				
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		File testFile = new File(filePath + defaultFileName);

		if(testFile.exists() && !testFile.isDirectory()) {

			System.out.println(" Do you wish to run experiment from file "
					+ filePath + defaultFileName + " ? (y/n)");
			System.out.print(">");

			if (bufferedConsole.readLine().equals("y")) {
				try {
					runFile(filePath + defaultFileName);
					return;
				} catch (FileNotFoundException e) {
					System.out.println(" File not found.");
				}
			}

		}

		while (true) {

			System.out.print(">");
			String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			if (input.equals("--help")) {
				System.out.println(" Help:\n" +
						" Help             --help\n" +
						" Shutdown         --shutdown\n" +
						" Add dataset      --add-dataset [username] [description] <path>\n" +
						" Run Experiment   --run <file.xml>\n"
						);
			
			} else if (input.startsWith("--shutdown")) {
				break;
	
			} else if (input.startsWith("--run")) {
			
			} else if (input.startsWith("--add-dataset")) {
				addDataset(input);
			} else {
				System.out.println(
						"Sorry, I don't understand you. \n" +
						" --help"
						);
			}
		}

	}

	private void runFile(String fileName) throws FileNotFoundException {

		System.out.println("Loading experiment from file: " + fileName);
		
		ComputationDescription comDescription =
				ComputationDescription.importXML(fileName);


		ExecuteBatch executeExpAction = new ExecuteBatch(comDescription);

		try {
			Thread.sleep(9000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        AID receiver = new AID(AgentNames.INSERTED_BATCHES_LOG, false);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(ontology.getName());

        try {
			getContentManager().fillContent(msg, new Action(receiver, executeExpAction));
			
			ACLMessage reply = FIPAService.doFipaRequestClient(this, msg, 10000);
			String replyText = reply.getContent();
			
			log("Reply: " + replyText);
			System.out.println("Reply: " + replyText);
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void addDataset(String cmd){
		int dataSetID=-1;
		
		String[] cmdA=cmd.split(" ");
		if(cmdA.length==4){
			String username=cmdA[1];
			String description=cmdA[2];
			String filename=new File(cmdA[3]).getAbsolutePath();
			dataSetID = this.sendRequestSaveDataSet(filename, username, description);
		}else if(cmdA.length==3){
			String username=cmdA[1];
			String description="Dataset saved in KlaraGui";
			String filename=new File(cmdA[2]).getAbsolutePath();
			dataSetID = this.sendRequestSaveDataSet(filename, username, description);
		}else if(cmdA.length==2){
			String username="klara";
			String description="Dataset saved in KlaraGui";
			String filename=new File(cmdA[1]).getAbsolutePath();
			dataSetID = this.sendRequestSaveDataSet(filename, username, description);
		}else{
			System.err.println("Wrong parameters");
		}
		
		if(dataSetID!=-1){
			this.sendRequestToComputeMetaDataForDataset(dataSetID);
		}
		
	}
	
	private int sendRequestSaveDataSet(String filename,String username,String description){
		try {
        	AID dataManager = new AID(AgentNames.DATA_MANAGER, false);
    		Ontology ontology = DataOntology.getInstance();
    		SaveDataset sd = new SaveDataset();
            //sd.setUserLogin("stepan");
    		sd.setUserLogin(username);
            //sd.setSourceFile("core/klaraguiagent/inputdataset/weather.arff");
    		sd.setSourceFile(filename);
            //sd.setDescription("Favourite Weather");
    		sd.setDescription(description);
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(dataManager);
            request.setLanguage(getCodec().getName());
            request.setOntology(ontology.getName());
            getContentManager().fillContent(request, new Action(dataManager, sd));
           
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
            if (reply == null){
                logError("Reply not received.");
                return -1;
            }
            else{
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
            	return (Integer)reply.getContentObject();
            }
        } catch (CodecException | OntologyException e) {
            logError("Ontology/codec error occurred: "+e.getMessage());
            e.printStackTrace();
        } catch (FIPAException e) {
            logError("FIPA error occurred: "+e.getMessage());
            e.printStackTrace();
        } catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
	private void sendRequestToComputeMetaDataForDataset(int dataSetID){
		AID receiver = new AID(AgentNames.FREDDIE, false);

        NewDataset nds = new NewDataset();
        
        //nds.setInternalFileName("28c7b9febbecff6ce207bcde29fc0eb8");
        //nds.setDataSetID(2301);
        nds.setDataSetID(dataSetID);
        log("Sending request to store metadata for DataSetID: "+dataSetID);
        
        try {
            ACLMessage request = makeActionRequest(receiver, nds);
           
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
            if (reply == null)
                logError("Reply not received.");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
        } catch (CodecException | OntologyException e) {
            logError("Ontology/codec error occurred: "+e.getMessage());
            e.printStackTrace();
        } catch (FIPAException e) {
            logError("FIPA error occurred: "+e.getMessage());
            e.printStackTrace();
        }
	}
	
	
	/** Naplni pozadavek na konkretni akci pro jednoho ciloveho agenta */
    private ACLMessage makeActionRequest(AID target, AgentAction action) throws CodecException, OntologyException {
    	Ontology ontology = MetadataOntology.getInstance();
    	
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(target);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(ontology.getName());
        getContentManager().fillContent(msg, new Action(target, action));
        return msg;
    }
	
	
}

