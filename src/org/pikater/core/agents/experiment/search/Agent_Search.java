package org.pikater.core.agents.experiment.search;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.SearchOntology;
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
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;


public abstract class Agent_Search extends Agent_AbstractExperiment {	

	private static final long serialVersionUID = 8637677510056974015L;
	private Codec codec = new SLCodec();
	private Ontology ontology = SearchOntology.getInstance();
	
	protected int query_block_size = 1;	

	private String conversationID;	
	private List<NewOption> search_options = null;
	private List<SearchItem> schema = null;
	
	protected abstract List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations); //returns List of Options
	protected abstract boolean finished();
	protected abstract float updateFinished(float[][] evaluations);
	protected abstract void loadSearchOptions(); // load the appropriate options before sending the first parameters
	
	
	@Override
	public java.util.List<Ontology> getOntologies() {
			
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();
		ontologies.add(SearchOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		
		return ontologies;
	}
	
	protected void setup() {

		initDefault();
		
		registerWithDF("Search");
		
		addBehaviour(new RequestServer(this));
		
		addAgentInfoBehaviour(getAgentInfo());

	} // end setup()

	
	protected List<SearchItem> getSchema() {
		if(schema != null) {
			return schema;
		} else {
			return new ArrayList<SearchItem>();
		}
		
	}
	protected List<NewOption> getSearchOptions() {

		if(search_options != null) {
			return search_options;
		} else {
			return new ArrayList<NewOption>();
		}
	}
	
	/*Converts List of Evals to an array of values - at the moment only error_rate*/
	private float[] namedEvalsToFitness(List<Eval> named_evals) {
		
		float[] res = new float[3];//named_evals.size...
		
		for (Eval e : named_evals) {

			if(e.getName().equals("error_rate")) {
				res[0]=e.getValue();
            }
            if(e.getName().equals("root_mean_squared_error")) {
                res[1] = e.getValue();
            }
            if(e.getName().equals("kappa_statistic")) {
                res[2] = -e.getValue();
            }
		}
		return res;
	}

	
	private List<Eval> fitnessToNamedEvals(float[] fitness) {
		List<Eval> evals = new ArrayList<>();
		
		Eval ev = new Eval();
		ev.setName("error_rate");
		ev.setValue(fitness[0]);
		evals.add(ev);
		
		Eval ev1 = new Eval();
		ev1.setName("root_mean_squared_error");			
		ev1.setValue(fitness[1]);				
		evals.add(ev1);
		
		Eval ev2 = new Eval();
		ev2.setName("kappa_statistic");
		ev2.setValue(fitness[2]);
		evals.add(ev2);
		
		return evals;
	}
	
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
				/**
				 * 
				 */
				private static final long serialVersionUID = -5801676857376453194L;

				boolean cont;
				List<SearchSolution> solutions_new = null;
				float evaluations[][] = null;
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
						if(queriesToProcess == 0){//skoncili jsme nebo zacali jeden cyklus query
							
							if(solutions_new == null){
								// System.out.println("OK: Pars - Nove solutiony vygenerovat");
							}else{
								//postprocess
								// System.out.println("OK: Pars - Update");
								updateFinished(evaluations);
							}

							if (finished()) {
								//konec vsech evaluaci
								// System.out.println("OK: Pars - Ukoncovani");
								solutions_new = null;
								evaluations = null;
								cont = false;
								
								// send the best error back to manager and 
								// to a recommender, if recommender's present

								ACLMessage originalRequest =(ACLMessage)getDataStore().get(REQUEST_KEY); 
								ACLMessage reply = originalRequest.createReply();
								reply.setPerformative(ACLMessage.INFORM);
								
								try {			

									Action a = (Action)myAgent.getContentManager().extractContent(originalRequest);								
									
									Evaluation evaluation = new Evaluation();
									float[][] f = new float[solutions_new.size()][3];
									f[0][0] = updateFinished(evaluations);
									evaluation.setEvaluations(fitnessToNamedEvals(f[0]));
									
									Result result = new Result(a, evaluation);			

									getContentManager().fillContent(reply, result);
									
								} catch (CodecException e) {
									logError(e.getMessage(), e);
									e.printStackTrace();
								} catch (OntologyException e) {
									logError(e.getMessage(), e);
									e.printStackTrace();
								}
																								
								getDataStore().put(RESULT_NOTIFICATION_KEY, reply);
							}else{
								//nova vlna evaluaci - generovani query
								// System.out.println("OK: Pars - nove solutiony poslat");
								solutions_new = generateNewSolutions(solutions_new, evaluations);
								if(solutions_new!= null)
									evaluations = new float[solutions_new.size()][];
								queriesToProcess = solutions_new.size();
								for(int i = 0; i < solutions_new.size(); i++){
									//posli queries
									ExecuteParameters ep = new ExecuteParameters();

									//TODO zmena ExecuteParameters na jeden prvek
									List<SearchSolution> solution_list = new ArrayList<SearchSolution>(1);
									solution_list.add(solutions_new.get(i));
									ep.setSolutions(solution_list);

									Action action = new Action();
									action.setAction(ep);
									action.setActor(getAID());

									//nedalo by se to klonovat?
									ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
									query.addReceiver(requestMsg.getSender());
									query.setLanguage(codec.getName());
									query.setOntology(ontology.getName());
									query.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
									//identifikace query a jeho odpovedi!!!
									query.setConversationId(conversationID + "_" + Integer.toString(i));
									try {
										getContentManager().fillContent(query, action);
									} catch (CodecException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (OntologyException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									myAgent.send(query);

								}
							}
							
						}else{//Cekame na vypocty - odpovedi na QUERY
							//TODO: FAILURE
							//and protocol FIPANames.InteractionProtocol.FIPA_QUERY???
							ACLMessage response = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));													
							if(response == null)
								block();//elseif zadna zprava inform - cekej
							else{
								if (response.getConversationId().split("_").length <= 2){
									// other informs than containing CA results (error)
									return;
								}
								// System.out.println("!OK: Pars - Prisla evaluace");
								//prisla evaluace - odpoved na QUERY
								//prirad inform ke spravnemu query
								int id = Integer.parseInt(response.getConversationId().split("_")[2]);
								Result res;
								try {
									res = (Result)getContentManager().extractContent(response);
									Evaluation eval = (Evaluation) res.getValue();
									List<Eval> named_evals = eval.getEvaluations();
									evaluations[id]=namedEvalsToFitness(named_evals);
							} catch (UngroundedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CodecException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OntologyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
								
								queriesToProcess--;
                                                                //System.out.println("OK: " + queriesToProcess + " queries remaining");
							}
						}
					}
					//handle informs as query results
				}


				@Override
				public boolean done() {
					return !cont;
				}
				
			});
		}
		
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
					//zacatek - nastavani optionu
					search_options = get_next_parameters_action.getSearch_options();
					schema = get_next_parameters_action.getSchema();														
					loadSearchOptions();
					
					conversationID = request.getConversationId(); 
							
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					agree.setContent(Integer.toString(query_block_size));
					return agree; //or REFUSE, sometimes
					//return null;
				}				
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			throw new NotUnderstoodException("Not understood");
		}

	}
		

	
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
					.extractContent(request); // TODO exception block?
			Result result = new Result((Action) content, agent);

			getContentManager().fillContent(reply, result);
			
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		} catch (CodecException e) {
			e.printStackTrace();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		} catch (OntologyException e) {
			e.printStackTrace();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		}
		
		return reply;
	}

}
