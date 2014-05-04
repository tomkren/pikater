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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.actions.BatchOntology;
import org.pikater.core.ontology.batch.ExecuteBatch;
import org.pikater.core.ontology.description.ComputationDescription;


public class Agent_GUIKlara extends PikaterAgent {

	private static final long serialVersionUID = -3908734088006529947L;
	private static final boolean DEBUG_MODE = true;

	public static String filePath = "core"
			+ System.getProperty("file.separator") + "inputs"
			+ System.getProperty("file.separator") + "inputsKlara"
			+ System.getProperty("file.separator");

	@Override
	protected void setup() {
		initDefault();
		registerWithDF();

		this.getContentManager().registerLanguage(this.getCodec());
		this.getContentManager().registerOntology(BatchOntology.getInstance());

		if (DEBUG_MODE) {
			
			System.out.println("GUIKlara agent starts.");
			
			try {
				runFile(filePath + "input.xml");
				
			} catch (FileNotFoundException e) {
				System.out.println(" File not found.");
			}
			
		} else {
			
			runAutomat();
			
		}
	}

	private void runAutomat() {
		
		System.out.println(
				"--------------------------------------------------------------------------------\n" +
				"| System Pikater: Multiagents system                                           |\n" +
				"--------------------------------------------------------------------------------\n" +
				"  Hi I'm Klara's GUI console Agent ..." +
				"\n"
				);

		
		if (System.console() == null) {

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

		char[] inputPasswd =
				System.console().readPassword("Please enter your password: ");
		
		char[] correctPassword = { '1', '2', '3' };

		if (! Arrays.equals (inputPasswd, correctPassword)) {

			System.out.println(" Sorry you are not Klara :-(");
			return;
		}

		System.out.println(" I welcome you Klara !!!");

		String defaultFileName = "input.xml";
		File testFile = new File(filePath + defaultFileName);

		if(testFile.exists() && !testFile.isDirectory()) {

			System.out.println(" Do you wish to run experiment from file "
					+ filePath + defaultFileName + " ? (y/n)");
			System.out.print(">");

			if (System.console().readLine().equals("y")) {
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
			String input = System.console().readLine();

			if (input.equals("--help")) {
				System.out.println(" Help:\n" +
						" Help             --help\n" +
						" Shutdown         --shutdown\n" +
						" Run Experiment   --run <file.xml>\n"
						);
			
			} else if (input.startsWith("--shutdown")) {
				break;
	
			} else if (input.startsWith("--run")) {
				
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

}

