package org.pikater.core.agents.experiment.recommend;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
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

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.managerAgent.ManagerAgentCommunicator;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.subtrees.data.Data_;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.recommend.Recommend;



public abstract class Agent_Recommender extends Agent_AbstractExperiment {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314060594137998065L;

	protected abstract org.pikater.core.ontology.subtrees.management.Agent chooseBestAgent(Data_ data);
	protected abstract String getAgentType();
	protected abstract boolean finished();
	protected abstract void updateFinished();
    
	public static Class<? extends Agent_ComputingAgent> DEFAULT_AGENT = Agent_WekaRBFNetworkCA.class;
	
	private org.pikater.core.ontology.subtrees.management.Agent myAgentOntology =
			new org.pikater.core.ontology.subtrees.management.Agent();
	


    @Override
	public java.util.List<Ontology> getOntologies() {
		
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();

		ontologies.add(RecommendOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		
		return ontologies;
	}
	
    @Override
    protected void setup() {
    	
        initDefault();
        
        registerWithDF("Recommender");
        
        Ontology ontology = RecommendOntology.getInstance();
        
        // receive request
        MessageTemplate mt = MessageTemplate.and(
        		MessageTemplate.MatchOntology(ontology.getName()),
        		MessageTemplate.MatchPerformative(ACLMessage.REQUEST));        
		addBehaviour(new RecommendBehaviour(this, mt));


		addAgentInfoBehaviour(getAgentInfo());

    }
         
	
	protected class RecommendBehaviour extends AchieveREResponder {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8353926385111974474L;
		private PikaterAgent agent;
		
		public RecommendBehaviour(PikaterAgent agent, MessageTemplate mt) {
			super(agent, mt);
			this.agent = agent;
		}

        @Override
        protected ACLMessage handleRequest(ACLMessage request)
        	throws NotUnderstoodException, RefuseException {
        	
        	ACLMessage reply = request.createReply();
            
        	try {        		
        		Action a = (Action) getContentManager().extractContent(request);

                if (a.getAction() instanceof Recommend) {
 
					if (finished()){
						log("Recommendation finished.");
						reply.setPerformative(ACLMessage.INFORM);					
						reply.setContent("finished");
						return reply;
					}
                	
                	Recommend rec = (Recommend) a.getAction();
                    myAgentOntology = rec.getRecommender();
                    
                    // merge options with .opt file options
                    myAgentOntology.setOptions(getParameters());

                    log("options: " + NewOptions.exportToWeka(myAgentOntology.getOptions()), 2);

                    Data_ data = rec.getData();
                    
                    // Get metadata:
					Metadata metadata = null;    
					
					// if metatada are not yet in ontology		
					if (rec.getData().getMetadata() == null) {
						// or fetch them from database:
						GetMetadata gm = new GetMetadata();
						gm.setInternal_filename(rec.getData().getTestFileName());
						metadata = DataManagerService.getMetadata(agent, gm);
						data.setMetadata(metadata);
					}                            			

					// else TODO - overit, jestli jsou metadata OK, pripadne vygenerovat
					
					org.pikater.core.ontology.subtrees.management.Agent recommended_agent = chooseBestAgent(rec.getData());
                    
					if(recommended_agent==null){
						reply.setPerformative(ACLMessage.FAILURE);
						return reply;
					}
					
					//TODO:
                    // fill options
                	//recommended_agent.setOptions(mergeOptions(recommended_agent.getOptions(), getAgentOptions(recommended_agent.getType()) ));

        			//log("********** Agent "
        				//	+ recommended_agent.getType()
        				//	+ " recommended. Options: "
        				//	+ NewOptions.exportToWeka(recommended_agent.getOptions())
        				//	+ "**********", Verbosity.MINIMAL);

            		// Prepare the content of inform message                       
    				Result result = new Result(a, recommended_agent);
					reply.setPerformative(ACLMessage.INFORM);					
    				
    				updateFinished();
    				
    				try {
    					getContentManager().fillContent(reply, result);
    				} catch (CodecException ce) {
    					logError(ce.getMessage(), ce);
    				} catch (OntologyException oe) {
    					logError(oe.getMessage(), oe);
    				}    				
        		}
            } catch (OntologyException e) {
            	logError(e.getMessage(), e);
            } catch (CodecException e) {
            	logError(e.getMessage(), e);
			}

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
				
				o2I.resetToDefaultValue();
				
				for (NewOption o1CAJ : o1_CA) {

					if (o2I.getName().equals(o1CAJ.getName())) {
						// ostatni optiony zustanou puvodni (= ze souboru)			

						// copy the value
                        o2I.setValuesWrapper(o1CAJ.getValuesWrapper().clone());
					}
				}
				
				if (o2I.isValid(false)){
					new_options.add(o2I);
				}
			}
		}
		return new_options;
	}
	
	protected java.util.List<NewOption> getAgentOptions(String agentType) {

		Ontology ontology = AgentInfoOntology.getInstance();
		
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
			logError(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			logError(oe.getMessage(), oe);
		} catch (FIPAException fe) {
			logError(fe.getMessage(), fe);
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
			logError(fe.getMessage(), fe);
			return null;
		}
		
		return Agents;
		
	} // end getAgentsByType

	
	public AID createAgent(String type, String name, Arguments arguments) {
        ManagerAgentCommunicator communicator=new ManagerAgentCommunicator();
        AID aid=communicator.createAgent(this,type,name,arguments);
        return aid;
	}
	
	protected java.util.List<NewOption> getParameters(){
		java.util.List<NewOption> optFileOptions =
				this.getAgentInfo().getOptions().getOptions();
		return mergeOptions(myAgentOntology.getOptions(), optFileOptions);
	}
	
}