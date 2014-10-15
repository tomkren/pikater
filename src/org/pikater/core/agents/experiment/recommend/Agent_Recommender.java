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
 * The base class for all recommender agents. Provides the basic interface and setup.
 */
public abstract class Agent_Recommender extends Agent_AbstractExperiment {
	
	private static final long serialVersionUID = 1314060594137998065L;

	/**
	 * The abstract method which provides the recommendation of the best agent based on the
	 * input data.
	 * 
	 * @param data The data, for which the agent is recommended
	 * @return The description of the recommended agent
	 */
	protected abstract org.pikater.core.ontology.subtrees.management.Agent
		chooseBestAgent(Datas data);
	
	/**
	 * Returns the type of this agent.
	 * 
	 * @return The type of the recommendation agent
	 */
	protected abstract String getAgentType();
	
	/**
	 * Check whether the recommendation is finished
	 * 
	 * @return <code>true</code> if the recommendation is finished, <code>false</code> otherwise
	 *  
	 */
	protected abstract boolean finished();
	
	/**
	 * Updates the finished status
	 */
	protected abstract void updateFinished();
    
	public static Class<? extends Agent_ComputingAgent> DEFAULT_AGENT =
			Agent_WekaRBFNetworkCA.class;
	
	private org.pikater.core.ontology.subtrees.management.Agent myAgentOntology =
			new org.pikater.core.ontology.subtrees.management.Agent();
	

	/**
	 * Returns the list of ontologies used by all recommender agents
	 * 
	 * @return List of ontologies used by the recommender agents
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
	 * Setup for the recommender agents. 
	 * 
	 * Registers the agent to the DF and sets the ontologies. Adds a message template to only receive
	 * REQUESTs. Also adds the basic recommend behaviour.
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
     * Basic recommender behaviour. Message handling, option setting and callbacks to the actual
     * recommending methods of the particular recommenders. 
     */
	protected class RecommendBehaviour extends AchieveREResponder {

		private static final long serialVersionUID = -8353926385111974474L;
		private PikaterAgent agent;
		
		public RecommendBehaviour(PikaterAgent agent, MessageTemplate mt) {
			super(agent, mt);
			this.agent = agent;
		}

		/**
		 * Processes the REQUEST message, extracts the options of the recommender agent 
		 * and calls the <code>chooseBestAgent</code> method.
     * 
		 * @param The {@link ACLMessage} with the request.
		 * @return {@link ACLMessage} with the description of the recommended agent
		 * @throws NotUnderstoodException if the <code>request</code> is in an unsupported ontology
		 * @throws RefuseException if the agent decides to refuse the request
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
	 * Merges two sets of options. One of them are the actual option, the other contains the default
	 * options
	 * 
	 * @param options1CA options specified by the user
	 * @param options2 default options of the recommender agent
	 * @return list of all options, those not specified by the user are set to the default values
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
	 * Get options for the particular recommender agent based on its type
	 * 
	 * @return The list of options all the agent's options
	 * 
	 */
	public List<NewOption> getAgentOptions(String agentType) {
		
		AgentInfo agentInfo =
				DataManagerService.getAgentInfo(this, agentType);
		return agentInfo.getOptions().getOptions();
	}
	
	/**
	 * Creates a agent by using agent {@link Agent_ManagerAgent}
	 * 
	 * @return The {@link AID} of the newly created agent.
	 * 
	 */
	public AID createAgent(String type, String name, Arguments arguments) {
		
        return ManagerAgentService.createAgent(this,type,name,arguments);
	}
	
	/**
	 * Returns the parameters of the recommeder agent. Merges the user specified options and the
	 * default options.
	 * 
	 * @return The list of the agent's paramters.
	 */
	protected List<NewOption> getParameters() {
		List<NewOption> optFileOptions =
				this.getAgentInfo().getOptions().getOptions();
		return mergeOptions(myAgentOntology.getOptions(), optFileOptions);
	}
	
	/**
	 * 
	 * Computes the distance between two double values scaled to 0-1 by using the <code>min</code> and 
	 * <code>max</code> values.
	 * 
	 * @param v1 The first value
	 * @param v2 The second value
	 * @param min Minimum possible value for the first two parameters
	 * @param max Maximum possible value for the first two parameters
	 * @return The distance between {@code v1} and {@code v2} mapped to the 0-1 interval.
	 */
	protected double d(double v1, double v2, double min, double max) {
        // map the value to the 0,1 interval; 0 - the same, 1 - the most
        // different

        return Math.abs(v1 - v2) / (max - min);
    }
	
	/**
	 * Computes distance between categories. It is 0 in case both the categories are the same and
	 * 1 otherwise.
	 * 
	 * @param v1 The first category
	 * @param v2 The second category
	 * @return The distance between the categories
	 */
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
	
	/**
	 * Computes the distance between two boolean values. It is 0 of both the values are the same and
	 * 1 otherwise.
	 *  
	 * @param v1 The first value
	 * @param v2 The second value
	 * @return The distance between the two values
	 */
	protected int dBoolean(Boolean v1, Boolean v2) {
		if (v1 == v2) {
			return 0;
		}
		return 1;
	} 
}
