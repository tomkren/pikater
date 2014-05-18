package org.pikater.core.agents.experiment.search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.SearchOntology;
import org.pikater.core.ontology.subtrees.messages.Eval;
import org.pikater.core.ontology.subtrees.messages.Evaluation;
import org.pikater.core.ontology.subtrees.messages.ExecuteParameters;
import org.pikater.core.ontology.subtrees.messages.GetOptions;
import org.pikater.core.ontology.subtrees.messages.GetParameters;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;

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


public abstract class Agent_Search extends Agent_AbstractExperiment {	

	private static final long serialVersionUID = 8637677510056974015L;
	private Codec codec = new SLCodec();
	private Ontology ontology = MessagesOntology.getInstance();
	
	protected int query_block_size = 1;

	private List<Option> search_options = null;
	private List<SearchItem> schema = null;
	
	protected abstract List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations); //returns List of Options
	protected abstract boolean finished();
	protected abstract void updateFinished(float[][] evaluations);
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
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendAgentInfo(getAgentInfo());

	} // end setup()

	
	protected List<SearchItem> getSchema() {
		if(schema != null) {
			return schema;
		} else {
			return new ArrayList<SearchItem>();
		}
		
	}
	protected List<Option> getSearch_options() {

		if(search_options != null) {
			return search_options;
		} else {
			return new ArrayList<Option>();
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
					}
					if(get_next_parameters_action!=null){
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
								ACLMessage reply = ((ACLMessage)getDataStore().get(REQUEST_KEY)).createReply();
								reply.setPerformative(ACLMessage.INFORM);
								//TODO: co se posila zpet?
								reply.setContent("finished");
								
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
									query.setConversationId(Integer.toString(i));
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
								// System.out.println("!OK: Pars - Prisla evaluace");
								//prisla evaluace - odpoved na QUERY
								//prirad inform ke spravnemu query
								int id = Integer.parseInt(response.getConversationId());
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
		ACLMessage reply = request.createReply();
		
		org.pikater.core.ontology.subtrees.management.Agent agent = null;

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

			// list of ontology.messages.Option
			List<Option> _options = new ArrayList<Option>();
			agent = new org.pikater.core.ontology.subtrees.management.Agent();
			agent.setName(getLocalName());
			agent.setType(getAgentType());
			
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
					List<String> set = null;
					
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
							set = new ArrayList<String>();
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
					
					_options.add(o);
					
				}

				line = bufRead.readLine();

			}
			agent.setOptions(_options);
			bufRead.close();

			reply.setPerformative(ACLMessage.INFORM);

			// Prepare the content
			ContentElement content = getContentManager()
					.extractContent(request); // TODO exception block?
			Result result = new Result((Action) content, agent);

			getContentManager().fillContent(reply, result);
			
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		} catch (FileNotFoundException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(e.getMessage());
		}
		
		return reply;
	} // end getParameters

}
