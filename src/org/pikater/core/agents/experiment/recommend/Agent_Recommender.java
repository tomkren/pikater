package org.pikater.core.agents.experiment.recommend;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import org.pikater.core.agents.configuration.Argument;
import org.pikater.core.agents.configuration.Arguments;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.management.ManagerAgentCommunicator;
import org.pikater.shared.logging.Verbosity;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.RecomendOntology;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.oldPikaterMessages.*;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.recomend.Recommend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public abstract class Agent_Recommender extends Agent_AbstractExperiment {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314060594137998065L;

	protected abstract org.pikater.core.ontology.subtrees.management.Agent chooseBestAgent(Data data);
	protected abstract String getAgentType();
    
	private org.pikater.core.ontology.subtrees.management.Agent myAgentOntology =
			new org.pikater.core.ontology.subtrees.management.Agent();
	


    @Override
	public java.util.List<Ontology> getOntologies() {
		
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();

		ontologies.add(RecomendOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		
		return ontologies;
	}
	
    @Override
    protected void setup() {
    	
        initDefault();
        
        registerWithDF("Recommender");
        
        Ontology ontology = RecomendOntology.getInstance();
        
        // receive request
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchOntology(ontology.getName()), MessageTemplate.MatchPerformative(ACLMessage.REQUEST));        
		addBehaviour(new receiveRequest(this, mt));


		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendAgentInfo(getAgentInfo());

    }  // end setup()
         
	
	protected class receiveRequest extends AchieveREResponder {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8353926385111974474L;		
		
		public receiveRequest(Agent a, MessageTemplate mt) {
			super(a, mt);
			// TODO Auto-generated constructor stub
		}

        @Override
        protected ACLMessage handleRequest(ACLMessage request)
        	throws NotUnderstoodException, RefuseException {
        	
        	ACLMessage reply = request.createReply();
            Integer performative = ACLMessage.FAILURE;
            
        	try {        		
        		Action a = (Action) getContentManager().extractContent(request);

                if (a.getAction() instanceof Recommend) {
                    Recommend rec = (Recommend) a.getAction();
                    myAgentOntology = rec.getRecommender();
                    
                    // merge options with .opt file options
                    myAgentOntology.setOptions(getParameters());

                    log("options: " + myAgentOntology.optionsToString(), 2);

                    Data data = rec.getData();
                    
                    // Get metadata:
					Metadata metadata = null;    
					
					// if metatada are not yet in ontology		
					if (rec.getData().getMetadata() == null) {
						// or fetch them from database:
						GetMetadata gm = new GetMetadata();
						gm.setInternal_filename(rec.getData().getTestFileName());
						metadata = DataManagerService.getMetadata(myAgent, gm);
						data.setMetadata(metadata);
					}                            			

					// else TODO - overit, jestli jsou metadata OK, pripadne vygenerovat
					
					org.pikater.core.ontology.subtrees.management.Agent recommended_agent = chooseBestAgent(rec.getData());
                    
                    // fill options
                	recommended_agent.setOptions(mergeOptions(recommended_agent.getOptions(), getAgentOptions(recommended_agent.getType()) ));

        			log("********** Agent "
        					+ recommended_agent.getType()
        					+ " recommended. Options: "
        					+ recommended_agent.toGuiString()
        					+ "**********", Verbosity.MINIMAL);

                		// Prepare the content of inform message                       
        				Result result = new Result(a, recommended_agent);
        				try {
        					getContentManager().fillContent(reply, result);
        				} catch (CodecException ce) {
        					ce.printStackTrace();
        				} catch (OntologyException oe) {
        					oe.printStackTrace();
        				}

        				performative = ACLMessage.INFORM;
        		}
            } catch (OntologyException e) {
                e.printStackTrace();
            } catch (CodecException e) {
                e.printStackTrace();
			}

            reply.setPerformative(performative);

            return reply;
        }
    }				        
    
	private java.util.List<Option> mergeOptions(java.util.List<Option> o1_CA, java.util.List<Option> o2) {
		
		java.util.List<Option> new_options = new java.util.ArrayList<Option>();
		if (o1_CA != null) {

			// if this type of agent has got some options
			// update the options (merge them)

			// go through the CA options
			// replace the value and add it to the new options

			for (Option next_option : o2) {
				
				next_option.setValue(next_option.getDefault_value());
				
				for (Option next_CA_option : o1_CA) {

					if (next_option.getName().equals(next_CA_option.getName())) {
						// ostatni optiony zustanou puvodni (= ze souboru)			

						next_option.setUser_value(next_CA_option.getUser_value());

						// copy the value
                        if (next_CA_option.getValue() != null){ 
                        	next_option.setValue(next_CA_option.getValue());
                        }
                                               
                        if (next_CA_option.getData_type() != null){
                        	next_option.setData_type(next_CA_option.getData_type());                        
                        }
                        
						if (next_CA_option.getValue().contains("?")){
							// just in case the someone forgot to set opt to mutable
							next_option.setMutable(true);
						}
					}
				}
				
				if (next_option.getValue() != null){
					new_options.add(next_option);
				}
			}
		}
		return new_options;
	}
	
	protected java.util.List<Option> getAgentOptions(String agentType) {

		Ontology ontology = MessagesOntology.getInstance();
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		// find an agent according to type
		List agents = getAgentsByType(agentType);
		request.addReceiver((AID)agents.get(0));

		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		request.setLanguage(codec.getName());
		request.setOntology(ontology.getName());

		GetOptions get = new GetOptions();
		Action a = new Action();
		a.setAction(get);
		a.setActor(this.getAID());

		try {
			// Let JADE convert from Java objects to string
			getContentManager().fillContent(request, a);

			ACLMessage inform = FIPAService.doFipaRequestClient(this, request);

			if (inform == null) {
				return null;
			}

			Result r = (Result) getContentManager().extractContent(inform);

			return ((org.pikater.core.ontology.subtrees.management.Agent) r.getItems().get(0)).getOptions();

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return null;
	}

	public List getAgentsByType(String agentType) {				
		
		List Agents = new ArrayList(); // List of AIDs
		
		// Make the list of agents of given type
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(agentType);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			// System.out.println(getLocalName()+": Found the following " + agentType + " agents:");
			
			for (int i = 0; i < result.length; ++i) {
				AID aid = result[i].getName();
				Agents.add(aid);
			}
			
			while (Agents.size() < 1) {
				AID aid = createAgent(agentType, null, null);
				Agents.add(aid);
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
		
		return Agents;
		
	} // end getAgentsByType

	
	public AID createAgent(String type, String name, Arguments arguments) {
        ManagerAgentCommunicator communicator=new ManagerAgentCommunicator("agentManager");
        AID aid=communicator.createAgent(this,type,name,arguments);
        return aid;
	}
	
	protected java.util.List<Option> getParameters(){
		java.util.ArrayList<Option> optFileOptions = getParametersFromOptFile();
		return mergeOptions(myAgentOntology.getOptions(), optFileOptions);
	}
	
	protected String getOptFileName(){
		return System.getProperty("file.separator") + "options" +
				System.getProperty("file.separator") + getAgentType() +".opt";
	}

	private java.util.ArrayList<Option> getParametersFromOptFile(){
		// set default values of options
		// if values exceed intervals in .opt file -> warning

		// fill the Options vector:
		java.util.ArrayList<Option> optionsResult = new java.util.ArrayList<Option>();
		
		String optPath = System.getProperty("user.dir") +
				System.getProperty("file.separator") + "options" + 
				System.getProperty("file.separator") + getAgentType() + ".opt";

			// read options from file
			try {
				/* Sets up a file reader to read the options file */
				FileReader input = new FileReader(optPath);
				/*
				 * Filter FileReader through a Buffered read to read a line at a
				 * time
				 */
				BufferedReader bufRead = new BufferedReader(input);

				String line; // String that holds current file line
				// Read first line
				line = bufRead.readLine();

				// Read through file one line at time. Print line # and line
				while (line != null) {
					// parse the line
					String delims = "[ ]+";
					String[] params = line.split(delims, 11);

					if (params[0].equals("$")) {
						
						String dt = null; 										
						if (params[2].equals("boolean")) {
							dt = "BOOLEAN";
						}
						if (params[2].equals("float")) {
							dt = "FLOAT";
						}
						if (params[2].equals("int")) {
							dt = "INT";
						}
						if (params[2].equals("mixed")) {
							dt = "MIXED";
						}					
						
						float numArgsMin;
						float numArgsMax;
						float rangeMin = 0;
						float rangeMax = 0;
						String range;
						java.util.List<String> set = null;
						
						if (dt.equals("BOOLEAN")){
							numArgsMin = 1;
							numArgsMax = 1;
							range = null;						
						}
						else{
							numArgsMin = Float.parseFloat(params[3]);
							numArgsMax = Float.parseFloat(params[4]);
							range = params[5];

							if (range.equals("r")){
								rangeMin = Float.parseFloat(params[6]);
								rangeMax = Float.parseFloat(params[7]);
							}
							if (range.equals("s")){
								set = new java.util.ArrayList<String>();
								String[] s = params[6].split("[ ]+");
								for (int i=0; i<s.length; i++){
									set.add(s[i]);
								}
							}
						}
						
						Option o = new Option(params[1], dt,
								numArgsMin, numArgsMax,
								range, rangeMin, rangeMax, set,
								params[params.length-3],
								params[params.length-2],
								params[params.length-1]);
						
						optionsResult.add(o);
						
					}

					line = bufRead.readLine();

				}
				bufRead.close();
				
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return optionsResult;
	} // end getParameters
}
