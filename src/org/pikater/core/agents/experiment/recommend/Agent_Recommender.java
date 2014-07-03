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

import org.pikater.core.agents.configuration.Arguments;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.managerAgent.ManagerAgentCommunicator;
import org.pikater.shared.logging.Verbosity;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.RecomendOntology;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.Options;
import org.pikater.core.ontology.subtrees.option.GetOptions;
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
        MessageTemplate mt = MessageTemplate.and(
        		MessageTemplate.MatchOntology(ontology.getName()),
        		MessageTemplate.MatchPerformative(ACLMessage.REQUEST));        
		addBehaviour(new receiveRequest(this, mt));


		sendAgentInfo(getAgentInfo());

    }
         
	
	protected class receiveRequest extends AchieveREResponder {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8353926385111974474L;		
		
		public receiveRequest(Agent a, MessageTemplate mt) {
			super(a, mt);
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

                    log("options: " + Options.exportToWeka(myAgentOntology.getOptions()), 2);

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
        					+ Options.exportToWeka(recommended_agent.getOptions())
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
    
	private java.util.List<NewOption> mergeOptions(java.util.List<NewOption> o1_CA, java.util.List<NewOption> o2) {
		
		java.util.List<NewOption> new_options = new java.util.ArrayList<NewOption>();
		if (o1_CA != null) {

			// if this type of agent has got some options
			// update the options (merge them)

			// go through the CA options
			// replace the value and add it to the new options

			for (NewOption o2I : o2) {
				
				o2I.setValue(o2I.getDefault_value());
				
				for (NewOption o1CAJ : o1_CA) {

					if (o2I.getName().equals(o1CAJ.getName())) {
						// ostatni optiony zustanou puvodni (= ze souboru)			

						o2I.setUser_value(o1CAJ.getUser_value());

						// copy the value
                        if (o1CAJ.getValue() != null){ 
                        	o2I.setValue(o1CAJ.getValue());
                        }
                                               
                        if (o1CAJ.getData_type() != null){
                        	o2I.setData_type(o1CAJ.getData_type());                        
                        }
                        
						if (o1CAJ.getValue().contains("?")){
							// just in case the someone forgot to set opt to mutable
							o2I.setMutable(true);
						}
					}
				}
				
				if (o2I.getValue() != null){
					new_options.add(o2I);
				}
			}
		}
		return new_options;
	}
	
	protected java.util.List<NewOption> getAgentOptions(String agentType) {

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
	
	protected java.util.List<NewOption> getParameters(){
		java.util.List<NewOption> optFileOptions =
				this.getAgentInfo().getOptions();
		return mergeOptions(myAgentOntology.getOptions(), optFileOptions);
	}
	
}