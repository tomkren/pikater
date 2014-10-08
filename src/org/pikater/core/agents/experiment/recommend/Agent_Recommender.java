package org.pikater.core.agents.experiment.recommend;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.ManagerAgentService;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.recommend.Recommend;


/**
 * 
 * Base recommender class for meta-learning recommendation
 * 
 */
public abstract class Agent_Recommender extends Agent_AbstractExperiment {
	
	private static final long serialVersionUID = 1314060594137998065L;

	/**
	 * Choose the best agent type for this type of data
	 * 
	 */
	protected abstract org.pikater.core.ontology.subtrees.management.Agent
		chooseBestAgent(Datas data);
	
	/**
	 * Get agent type
	 */
	protected abstract String getAgentType();
	
	/**
	 * Detects if the recommendation is finished
	 * 
	 */
	protected abstract boolean finished();
	
	/**
	 * Updates status finished
	 */
	protected abstract void updateFinished();
    
	public static Class<? extends Agent_ComputingAgent> DEFAULT_AGENT =
			Agent_WekaRBFNetworkCA.class;
	
	private org.pikater.core.ontology.subtrees.management.Agent myAgentOntology =
			new org.pikater.core.ontology.subtrees.management.Agent();
	

	/**
	 * Get ontologies which is using this agent
	 */
    @Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies =
				new ArrayList<>();

		ontologies.add(RecommendOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		
		return ontologies;
	}
	
	/**
	 * Agent setup
	 */
    @Override
    protected void setup() {
    	
        initDefault();
        
        registerWithDF("Recommender");
        
        Ontology ontology = RecommendOntology.getInstance();
        
        // receive request
        MessageTemplate template = MessageTemplate.and(
        		MessageTemplate.MatchOntology(ontology.getName()),
        		MessageTemplate.MatchPerformative(ACLMessage.REQUEST));        
		addBehaviour(new RecommendBehaviour(this, template));


		addAgentInfoBehaviour(getAgentInfo());

    }
         
	
    /**
     * 
     * Recommender behavior
     *
     */
	protected class RecommendBehaviour extends AchieveREResponder {

		private static final long serialVersionUID = -8353926385111974474L;
		private PikaterAgent agent;
		
		public RecommendBehaviour(PikaterAgent agent, MessageTemplate mt) {
			super(agent, mt);
			this.agent = agent;
		}

		/**
		 * Handles request - receive Recommend ontology
		 * @param request - message
		 * @return - OK message
		 * @throws NotUnderstoodException
		 * @throws RefuseException
		 */
        @Override
        protected ACLMessage handleRequest(ACLMessage request)
        	throws NotUnderstoodException, RefuseException {
        	
        	ACLMessage reply = request.createReply();
            
        	try {        		
        		Action a = (Action) getContentManager().extractContent(request);

                if (a.getAction() instanceof Recommend) {
 
					if (finished()){
						logInfo("Recommendation finished.");
						reply.setPerformative(ACLMessage.INFORM);					
						reply.setContent("finished");
						return reply;
					}
                	
                	Recommend rec = (Recommend) a.getAction();
                    myAgentOntology = rec.getRecommender();
                    
                    myAgentOntology.setOptions(getParameters());

                    String wekaOptionsString =
                    		NewOptions.exportToWeka(
                    				myAgentOntology.getOptions());
                    
                    logSevere("options: " + wekaOptionsString);

                    Datas datas = rec.getDatas();
                    
                    // Get metadata:
					Metadata metadata;
					
					// if metatada are not yet in ontology		
					if (rec.getDatas().getMetadata() == null) {
						// or fetch them from database:
						GetMetadata gm = new GetMetadata();
						gm.setInternal_filename(rec.getDatas().exportInternalTestFileName());
						metadata = DataManagerService.getMetadata(agent, gm);
						datas.setMetadata(metadata);
					}                            			
					
					org.pikater.core.ontology.subtrees.management.Agent recommendedAgent =
							chooseBestAgent(rec.getDatas());
                    
					if(recommendedAgent == null){
						reply.setPerformative(ACLMessage.FAILURE);
						return reply;
					}
					

                    // Fill options
/*
					agent.logError(recommended_agent.getType());
					List<NewOption> options = getAgentOptions(recommended_agent.getType());
					
					List<NewOption> recommendedAgentOptions = recommended_agent.getOptions();
					
					List<NewOption> mergedOptions =
							mergeOptions(options, recommendedAgentOptions);
					recommended_agent.setOptions(mergedOptions);
*/					
            		// Prepare the content of inform message                       
    				Result result = new Result(a, recommendedAgent);
					reply.setPerformative(ACLMessage.INFORM);					
    				
    				updateFinished();
    				
    				try {
    					getContentManager().fillContent(reply, result);
    				} catch (CodecException | OntologyException ce) {
    					logException(ce.getMessage(), ce);
    				}
                }
            } catch (OntologyException | CodecException e) {
            	logException(e.getMessage(), e);
            }

            return reply;
        }
    }				        
    
	/**
	 * Options merging
	 * 
	 * @param options1CA - options of Computing Agent
	 * @param options2 - second options
	 * @return - merged options
	 */
	private List<NewOption> mergeOptions(List<NewOption> options1CA,
			List<NewOption> options2) {
		
		List<NewOption> newOptions = new ArrayList<>();
		if (options1CA != null) {

			// if this type of agent has got some options
			// update the options (merge them)

			// go through the CA options
			// replace the value and add it to the new options

			for (NewOption option2I : options2) {
				
				option2I.resetToDefaultValue();
				
				for (NewOption option1CAJ : options1CA) {

					if (option2I.getName().equals(option1CAJ.getName())) {

						// copy the value
						ValuesForOption valForOption =
								option1CAJ.getValuesWrapper().clone();
                        option2I.setValuesWrapper(valForOption);
					}
				}
				
				if (option2I.isValid(false)) {
					newOptions.add(option2I);
				}
			}
		}
		return newOptions;
	}

	/**
	 * Get options of the concrete agent type
	 * 
	 */
	public List<NewOption> getAgentOptions(String agentType) {
		
		AgentInfo agentInfo =
				DataManagerService.getAgentInfo(this, agentType);
		return agentInfo.getOptions().getOptions();
	}
	
	/**
	 * Creates a agent by using agent {@link Agent_ManagerAgent}
	 */
	public AID createAgent(String type, String name, Arguments arguments) {
		
        return ManagerAgentService.createAgent(this,type,name,arguments);
	}
	
	/**
	 * Get parameters - merged options with default
	 */
	protected List<NewOption> getParameters() {
		List<NewOption> optFileOptions =
				this.getAgentInfo().getOptions().getOptions();
		return mergeOptions(myAgentOntology.getOptions(), optFileOptions);
	}
	
	protected double d(double v1, double v2, double min, double max) {
        // map the value to the 0,1 interval; 0 - the same, 1 - the most
        // different

        return Math.abs(v1 - v2) / (max - min);
    }
	
	protected int dCategory(String v1, String v2) {
		
		if ((v1 == null) && (v2 == null)) {
			return 0;
		}
		else if ((v1 == null) || (v2 == null)) {
			return 1;
		} else {
			if (v1.equals(v2)) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	protected int dBoolean(Boolean v1, Boolean v2) {
		if (v1 == v2) {
			return 0;
		}
		return 1;
	} 
}
