package org.pikater.core.agents.experiment.search;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.SearchOntology;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.search.ExecuteParameters;
import org.pikater.core.ontology.subtrees.search.GetParameters;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;


/**
 * An abstract class for search agents.
 * 
 * @author Ondra, Klara
 *
 */
public abstract class Agent_Search extends Agent_AbstractExperiment {	

	private static final long serialVersionUID = 8637677510056974015L;
	private Codec codec = new SLCodec();
	private Ontology ontology = SearchOntology.getInstance();
	
	protected int queryBlockSize = 1;	

	private String conversationID;	
	private List<NewOption> searchOptions = null;
	private List<SearchItem> schema = null;
	
	/**
	 * Generates new solutions according to the parameters space search type.
	 * 
	 * @param solutions     last solutions
	 * @param evaluations   last evaluations of above solutions
	 * @return              new solutions
	 */
	
	protected abstract List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations); //returns List of Options
	
	/**
	 * 
	 * @return <code>true</code> if the search of parmeters is finished, 
	 *         <code>false</code> otherwise 
	 */
	protected abstract boolean isFinished();
		
	/**
	 *  Checks the evaluations and searches for the best
	 *  error_rate.
	 *  
	 *  @returns best error rate so far.
	 */
	protected abstract float updateFinished(float[][] evaluations);
		
	/**
	 * Loads the appropriate options of the specific search agent 
	 * before sending the first parameters.
	 */
	protected abstract void loadSearchOptions(); 
		
    /**
     * Returns list of ontologies that are used by the specific search agent.
     */
	@Override
	public java.util.List<Ontology> getOntologies() {
			
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();
		ontologies.add(SearchOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		
		return ontologies;
	}
	
	
	/**
	 * Initializes Agent_Manager agent.
	 * <p>
	 * Registers the search agent with yellow pages and starts its behaviours:
	 *   <ul>
	 *     <li>RequestServer behaviour that deals with requests from manager agent
	 *     <li>addAgentInfoBehaviour
	 *   </ul>     
	 */
	protected void setup() {

		initDefault();
		
		registerWithDF("Search");
		
		addBehaviour(new RequestServer(this));
		
		addAgentInfoBehaviour(getAgentInfo());

	} // end setup()

	
	/**
	 * Returns a schema (a list of {@link SearchItem}s) that is worked with in
	 * search agents. 
	 * 
	 * @return a schema or an empty list.
	 */
	protected List<SearchItem> getSchema() {
		if(schema != null) {
			return schema;
		} else {
			return new ArrayList<SearchItem>();
		}		
	}
	
	
	/**
	 * Returns options of the search agent itself.
	 * 
	 * @return option list or an empty list.
	 */
	protected NewOptions getSearchOptions() {

		if(searchOptions != null) {
			return new NewOptions(searchOptions);
		} else {
			return new NewOptions();
		}
	}
	
	
	/**
	 * Converts List of {@link Eval}s to an array of values (used when dealing 
	 * with fitness function) 
	 * - at the moment only error rate, root mean squared error 
	 * and kappa statistic.
	 *
	 * @param named_evals
	 * @return array of values without names (in a fixed order)
	 */
	private float[] namedEvalsToFitness(List<Eval> named_evals) {
		
		float[] res = new float[3]; //named_evals.size...
		
		for (Eval e : named_evals) {

			if(e.getName().equals(CoreConstant.Error.ERROR_RATE.name())) {
				res[0]=e.getValue();
            }
            if(e.getName().equals(CoreConstant.Error.ROOT_MEAN_SQUARED.name())) {
                res[1] = e.getValue();
            }
            if(e.getName().equals(CoreConstant.Error.KAPPA_STATISTIC.name())) {
                res[2] = -e.getValue();
            }
		}
		return res;
	}

	
	/**
	 * Converts the array of float values used for fitness to a list 
	 * of named Evals 
	 * - at the moment only error rate, root mean squared error 
	 * and kappa statistic.
	 *
	 * @param fitness	an array of size 3 of fitness values 
	 * 					in the fixed order:
	 * 					error rate, 
	 * 					root mean squared error 
	 * 					and kappa statistic.
	 * @return			List of Evals with the appropriate names
	 */
	private List<Eval> fitnessToNamedEvals(float[] fitness) {
		List<Eval> evals = new ArrayList<Eval>();
		
		Eval ev = new Eval();
		ev.setName(CoreConstant.Error.ERROR_RATE.name());
		ev.setValue(fitness[0]);
		evals.add(ev);
		
		Eval ev1 = new Eval();
		ev1.setName(CoreConstant.Error.ROOT_MEAN_SQUARED.name());			
		ev1.setValue(fitness[1]);				
		evals.add(ev1);
		
		Eval ev2 = new Eval();
		ev2.setName(CoreConstant.Error.KAPPA_STATISTIC.name());
		ev2.setValue(fitness[2]);
		evals.add(ev2);
		
		return evals;
	}
	
	
	/**
	 * AchieveREResponder behaviour that handles the request to generate new
	 * options for a computation agent.
	 *  
	 * @author Ondra
	 *
	 */
	private class RequestServer extends AchieveREResponder {
		private static final long serialVersionUID = 6214306716273574418L;
		GetOptions get_option_action;
		GetParameters get_next_parameters_action;
		
		public RequestServer(Agent a) {
			super(a, MessageTemplate
					.and(	MessageTemplate
									.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
							MessageTemplate.and(MessageTemplate
									.MatchPerformative(ACLMessage.REQUEST),
									MessageTemplate.and(MessageTemplate
											.MatchLanguage(codec.getName()),
											MessageTemplate.MatchOntology(ontology
													.getName())))));
			
			this.registerPrepareResultNotification(new Behaviour(a) {

				private static final long serialVersionUID = -5801676857376453194L;

				boolean cont;
				List<SearchSolution> solutions_new = null;
				float[][] evaluations = null;
				int queriesToProcess = 0;
				@Override
				public void action() {
					cont = false;
					if(get_option_action != null){
						ACLMessage reply = getParameters((ACLMessage)getDataStore().get(REQUEST_KEY));
						getDataStore().put(RESULT_NOTIFICATION_KEY, reply);
						return;
					}
					else if(get_next_parameters_action!=null){
						cont = true;
						ACLMessage requestMsg = (ACLMessage)getDataStore().get(REQUEST_KEY);
						if(queriesToProcess == 0){// we have ended or started one cycle of query
							
							if(solutions_new == null){
								// generate new solutions
							}else{
								// postprocess
								// update the state
								updateFinished(evaluations);
							}

							if (isFinished()) {
								// all evaluations finished
								solutions_new = null;
								evaluations = null;
								cont = false;
								
								// send the best error back to manager and 
								// to a recommender, if recommender is present

								ACLMessage originalRequest =(ACLMessage)getDataStore().get(REQUEST_KEY); 
								ACLMessage reply = originalRequest.createReply();
								reply.setPerformative(ACLMessage.INFORM);
								
								try {			
									Action a = (Action)myAgent.getContentManager().extractContent(originalRequest);								
									
									Evaluation evaluation = new Evaluation();
									float[] f = new float[3];
									f[0] = updateFinished(evaluations);
									evaluation.setEvaluations(fitnessToNamedEvals(f));
									
									Result result = new Result(a, evaluation);			

									getContentManager().fillContent(reply, result);
									
								} catch (CodecException e) {
									logException(e.getMessage(), e);
								} catch (OntologyException e) {
									logException(e.getMessage(), e);
								}
																								
								getDataStore().put(RESULT_NOTIFICATION_KEY, reply);
							}else{
								// new wave of evaluations - generating queries
								// -> send new solutions
								solutions_new = generateNewSolutions(solutions_new, evaluations);
								if(solutions_new != null) {
									evaluations = new float[solutions_new.size()][];
								} else
									solutions_new = new ArrayList<>();
								queriesToProcess = solutions_new.size();
								for(int i = 0; i < solutions_new.size(); i++){
									// send queries
									ExecuteParameters ep = new ExecuteParameters();

									List<SearchSolution> solution_list = new ArrayList<SearchSolution>(1);
									solution_list.add(solutions_new.get(i));
									ep.setSolutions(solution_list);

									Action action = new Action();
									action.setAction(ep);
									action.setActor(getAID());

									ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
									query.addReceiver(requestMsg.getSender());
									query.setLanguage(codec.getName());
									query.setOntology(ontology.getName());
									query.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
									// identify query and its replies
									query.setConversationId(conversationID + "_" + Integer.toString(i));
									try {
										getContentManager().fillContent(query, action);
									} catch (CodecException e) {
										logException(e.getMessage(), e);
									} catch (OntologyException e) {
										logException(e.getMessage(), e);
									}
									myAgent.send(query);
								}
							}
							
						}else{// wait for the computation results - replies to QUERY
							ACLMessage response = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));													
							if(response == null)
								block();//elseif no inform message - wait
							else {
								if (response.getConversationId().split("_").length <= 2){
									// other informs than containing CA results (error)
									return;
								}
								// evaluation received - a reply to a QUERY
								// assign inform to the correct query
								int id = Integer.parseInt(response.getConversationId().split("_")[2]);
								Result res;
								try {
									res = (Result)getContentManager().extractContent(response);
									Evaluation eval = (Evaluation) res.getValue();
									List<Eval> named_evals = eval.getEvaluations();
									evaluations[id]=namedEvalsToFitness(named_evals);
								} catch (UngroundedException e) {
									logException(e.getMessage(), e);
								} catch (CodecException e) {
									logException(e.getMessage(), e);
								} catch (OntologyException e) {
									logException(e.getMessage(), e);
								}
									queriesToProcess--;
							}
						}
					}
				}

				@Override
				public boolean done() {
					return !cont;
				}
				
			});
		}
		
		/**
		 * Handles a request from the request server.
		 * <p>
		 * Understands two ontologies:
		 *   <ul>
		 *     <li> {@link GetOptions} in a request for search's own options
		 *     <li> {@link GetParameters} request to start generating options
		 *          for a computational agent
		 *   </ul>
		 */
		@Override
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException{
			
			get_option_action = null;
			get_next_parameters_action = null;
			ContentElement content;
			try {
				content = getContentManager().extractContent(request);

				if (((Action) content).getAction() instanceof GetOptions) {
					get_option_action = (GetOptions) ((Action) content).getAction();
					return null;
					
				} else if (((Action) content).getAction() instanceof GetParameters){
					get_next_parameters_action = (GetParameters) ((Action) content).getAction();
					// start - set the options
					searchOptions = get_next_parameters_action.getSearchOptions();
					schema = get_next_parameters_action.getSchema();														
					loadSearchOptions();
					
					conversationID = request.getConversationId(); 
							
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					agree.setContent(Integer.toString(queryBlockSize));
					return agree; 
				}				
			} catch (UngroundedException e) {
				logException(e.getMessage(), e);
			} catch (CodecException e) {
				logException(e.getMessage(), e);
			} catch (OntologyException e) {
				logException(e.getMessage(), e);
			}
			throw new NotUnderstoodException("Not understood");
		}
	}


	/**
	 * Prepares an ACLMessage with search parameters in a response
	 * to a request containing GetParameters ontology.
	 * 
	 * @param request
	 * @return			ACLMessage containing parameters
	 */
	protected ACLMessage getParameters(ACLMessage request) {
		
		org.pikater.core.ontology.subtrees.management.Agent agent = null;
		agent = new org.pikater.core.ontology.subtrees.management.Agent();
		agent.setName(getLocalName());
		agent.setType(getAgentType());
		agent.setOptions(getAgentInfo().getOptions().getOptions());

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		try {
			// Prepare the content
			ContentElement content = getContentManager()
					.extractContent(request);
			Result result = new Result((Action) content, agent);

			getContentManager().fillContent(reply, result);
			
		} catch (ArrayIndexOutOfBoundsException e) {
			logException(e.getMessage(), e);
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		} catch (CodecException e) {
			logException(e.getMessage(), e);
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		}
		
		return reply;
	}
	
}
