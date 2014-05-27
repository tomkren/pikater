package org.pikater.core.agents.system;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
// import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationGraph;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.management.ManagerAgentCommunicator;
import org.pikater.core.ontology.messages.Eval;
import org.pikater.core.ontology.messages.Evaluation;
import org.pikater.core.ontology.messages.Execute;
import org.pikater.core.ontology.messages.ExecuteParameters;
import org.pikater.core.ontology.messages.Results;
import org.pikater.core.ontology.messages.Task;
import org.pikater.core.ontology.messages.TaskOutput;
import org.pikater.core.ontology.messages.option.Option;
import org.pikater.core.ontology.metadata.Metadata;
import org.pikater.core.ontology.search.searchItems.BoolSItem;
import org.pikater.core.ontology.search.searchItems.FloatSItem;
import org.pikater.core.ontology.search.searchItems.IntSItem;
import org.pikater.core.ontology.search.searchItems.SetSItem;


public class Agent_Manager extends PikaterAgent {

	private static final long serialVersionUID = -5140758757320827589L;
	
	private final String NO_XML_OUTPUT ="no_xml_output";
	private boolean no_xml_output = true;
	protected Set<Subscription> subscriptions = new HashSet<Subscription>();
	private int problem_i = 0;
	protected HashMap<Integer, ComputationCollectionItem> computationCollection = 
			new HashMap<Integer, ComputationCollectionItem>();
	
	
	public ComputationCollectionItem getComputation(Integer id){
		return computationCollection.get(id);
	}
	
	protected void setup() {

    	initDefault();
    
    	registerWithDF("manager");
    			
		if (containsArgument(NO_XML_OUTPUT)) {
			if (isArgumentValueTrue(NO_XML_OUTPUT)){
				no_xml_output = true;
			}
			else{
				no_xml_output = false;
			}
		}			
		
		doWait(3000);	
		
		
		MessageTemplate subscriptionTemplate = 
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),
								MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
		
		addBehaviour (new SubscriptionResponder(this, subscriptionTemplate, new subscriptionManager()));
		
		addBehaviour (new RequestServer(this));
		
	} // end setup
	

	protected void fillQueues(int graphId, int nodeId, ArrayList<TaskOutput> output){
		ComputationNode computationNode = getGraph(graphId).getNode(nodeId);
		
		java.util.Iterator<TaskOutput> itr = output.iterator();
		while (itr.hasNext()) {
			TaskOutput to = (TaskOutput) itr.next();
			DataSourceEdge dse = new DataSourceEdge();
			dse.setDataSourceId(to.getName());								
			computationNode.addToOutputAndProcess(dse, to.getType());			 			
		}			

	}	
	
	private ComputationGraph getGraph(int id){
		// TODO: napsat
		ComputationGraph cg = null;
		return cg;
	}
	
		
		
	public class subscriptionManager implements SubscriptionManager {
		public boolean register(Subscription s) {
			subscriptions.add(s);
			return true;
		}

		public boolean deregister(Subscription s) {
			subscriptions.remove(s);
			return true;
		}
	}

	
	private void ProcessNextQueryFromSearch(ACLMessage query, List options){
        ACLMessage req = createRequestFromSearchQuery(query, options);
        addBehaviour(new ExecuteTask(this, req, query));

	} // end ProcessNextQueryFromSearch
	
		
	protected ACLMessage createRequestFromSearchQuery(ACLMessage query, List options) {
    	ACLMessage request = null;
		
		try {
			ContentElement content = getContentManager().extractContent(query);
							
			ExecuteParameters ep = (ExecuteParameters) (((Action) content).getAction());
			
			// there is only one solution at the time
			Options opt = fillOptionsWithSolution(options, (SearchSolution)(ep.getSolutions().get(0)));
		
			// extract task from query
			Execute ex = new Execute();
			Task received_task = 
			// TODO ... potrebuju puvodni zpravu ...
					
			// add the new options to the task
			Agent ag = received_task.getAgent();							
			ag.setOptions(opt.getList());							
			received_task.setAgent(ag);
			
			// TODO set note
			// received_task.setNote(Integer.toString(taskNumber));
			// increaseTaskNumber();
			
			ex.setTask(received_task);							

			request = execute2Message(ex);
			
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
		return request;
	}
	
	
	protected void sendSubscription(ACLMessage result, ACLMessage originalMessage) {			
		// Prepare the subscription message to the request originator
		ACLMessage msgOut = originalMessage.createReply();
		msgOut.setPerformative(result.getPerformative());
		
		// copy content of inform message to a subscription
		try {
			getContentManager().fillContent(msgOut, getContentManager().extractContent(result));
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

		// go through every subscription
		java.util.Iterator<Subscription> it = subscriptions.iterator();
		while (it.hasNext()) {
			Subscription subscription = (Subscription) it.next();

			if (subscription.getMessage().getConversationId().equals(
					"subscription" + originalMessage.getConversationId())) {
				subscription.notify(msgOut);
			}
		}
		
	} // end sendSubscription
	

	
	protected class RequestServer extends CyclicBehaviour {

		private static final long serialVersionUID = -6257623790759885083L;

		private MessageTemplate requestMsgTemplate = MessageTemplate
				.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
										MessageTemplate.MatchOntology(ontology.getName()))));
		
		private MessageTemplate queryMsgTemplate = MessageTemplate
				.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
						MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
								MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
										MessageTemplate.MatchOntology(ontology.getName()))));

		
		public RequestServer(Agent agent) {			
			super(agent);
		}
						
				
		@Override 
		public void action() {
			
			ACLMessage request = receive(requestMsgTemplate);
			ACLMessage query = receive(queryMsgTemplate);			
						
			if (request != null || query != null) {
				log("message received from: " + request.getSender().getName());

				try {
					ContentElement content = getContentManager().extractContent(query);	
					if (query != null) {
						// new options received from search agent
						// check whether the query is correct
										
						if (((Action) content).getAction() instanceof ExecuteParameters) {					
							// manager received options to execute from search
							ProcessNextQueryFromSearch(query);
					    }
		            }

					if (request != null){
						if (((Action) content).getAction() instanceof Execute) {
		
							Execute execute = (Execute) (((Action) content).getAction());
		
							Task task = (Task) execute.getTask();

							ACLMessage agree = request.createReply();
							agree.setPerformative(ACLMessage.AGREE);
							String problemID = generateProblemID();				
							agree.setContent(problemID);
							
							send(agree);

							
							// TODO: zpracuj graf ...

							
							return;
						}
					}				
				} catch (UngroundedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CodecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (OntologyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				block();
			}

			/*
			ACLMessage result_msg = request.createReply();
			result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			send(result_msg);
			*/
			return;
		}
	}

	
	protected Execute node2Execute() {		
		// create Execute ontology action from graph node

	}
	
	
	public ACLMessage execute2Message(Execute execute) {		
		// create ACLMessage from Execute ontology action
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(codec.getName());
		request.setOntology(ontology.getName());
		request.addReceiver(getAgentByType("Planner"));
		
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	
		Action a = new Action();
		a.setAction(execute);
		a.setActor(this.getAID());

		try {
			getContentManager().fillContent(request, a);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		return request;
	}
	

	public AID getAgentByType(String agentType) {
		return (AID)getAgentByType(agentType, 1).get(0);
	}

	
	public List getAgentByType(String agentType, int n) {
		// returns list of AIDs
		
		List Agents = new ArrayList(); // List of AIDs
		
		// Make the list of agents of given type
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(agentType);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			log("Found the following " + agentType + " agents:");
			
			for (int i = 0; i < result.length; ++i) {
				AID aid = result[i].getName();
				if (Agents.size() < n){
					Agents.add(aid);
				}
			}
			
			while (Agents.size() < n) {
				// create agent
				AID aid = createAgent(agentType, null, null);
				Agents.add(aid);
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
		
		return Agents;
		
	} // end getAgentByType
	

	public AID createAgent(String type, String name, List options) {
        ManagerAgentCommunicator communicator=new ManagerAgentCommunicator("agentManager");
        AID aid=communicator.createAgent(this,type,name,options);
		return aid;		
	}

	
	protected Results prepareTaskResults(ACLMessage resultmsg, String problemID) {
		Results results = new Results();

		ContentElement content;
		try {
			content = getContentManager().extractContent(resultmsg);
			if (content instanceof Result) {
				Result result = (Result) content;
				
				List listOfResults = result.getItems();
				results.setProblem_id(problemID);
				results.setResults(listOfResults);				
				
				float sumError_rate = 0;
				float sumKappa_statistic = 0;
				float sumMean_absolute_error = 0;
				float sumRoot_mean_squared_error = 0;
				float sumRelative_absolute_error = 0; // percent
				float sumRoot_relative_squared_error = 0; // percent

				if (listOfResults == null) {
					// there were no tasks computed
					// leave the default values
					return null;
				} else {
					Iterator itr = listOfResults.iterator();
					while (itr.hasNext()) {
						Task next = (Task) itr.next();
						Evaluation evaluation;
						evaluation = next.getResult();
						
						results.setTask_id(next.getId()); // one of the tasks will do							
						
						// if the value has not been set by the CA, the sum
						// will < 0
						// error rate is a manadatory slot

						for (Eval next_eval : evaluation.getEvaluations() ) {

							if (next_eval.getName().equals("error_rate")){ 
								sumError_rate += next_eval.getValue();
							}
							
							if (next_eval.getName().equals("kappa_statistic")){ 
								sumKappa_statistic += next_eval.getValue();
							}

							if (next_eval.getName().equals("mean_absolute_error")){ 
								sumMean_absolute_error += next_eval.getValue();
							}
							
							if (next_eval.getName().equals("root_mean_squared_error")){ 
								sumRoot_mean_squared_error += next_eval.getValue();
							}
							
							if (next_eval.getName().equals("relative_absolute_error")){ 
								sumRelative_absolute_error += next_eval.getValue();
							}
							
							if (next_eval.getName().equals("root_relative_squared_error")){ 
								sumRoot_relative_squared_error += next_eval.getValue();
							}
						}

					}
					
					if (sumError_rate > -1) {
						results.setAvg_error_rate(sumError_rate
								/ listOfResults.size());
					}
					if (sumKappa_statistic > -1) {
						results.setAvg_kappa_statistic(sumKappa_statistic
								/ listOfResults.size());
					}
					if (sumMean_absolute_error > -1) {
						results
								.setAvg_mean_absolute_error(sumMean_absolute_error
										/ listOfResults.size());
					}
					if (sumRoot_mean_squared_error > -1) {
						results
								.setAvg_root_mean_squared_error(sumRoot_mean_squared_error
										/ listOfResults.size());
					}
					if (sumRelative_absolute_error > -1) {
						results
								.setAvg_relative_absolute_error(sumRelative_absolute_error
										/ listOfResults.size());
					}
					if (sumRoot_relative_squared_error > -1) {
						results
								.setAvg_root_relative_squared_error(sumRoot_relative_squared_error
										/ listOfResults.size());
					}
				}
			}			
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {			
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

		return results;

	} // prepareTaskResult

	
	protected boolean writeXMLResults(Results results) {
		String file_name = "xml" + System.getProperty("file.separator")
				+ getDateTimeXML() + "_" + results.getTask_id().getIdentificator() + ".xml";

		// create the "xml" directory, if it doesn't exist
		boolean exists = (new File("xml")).exists();
		if (!exists) {
			boolean success = (new File("xml")).mkdir();
			if (!success) {
				System.err.println(getLocalName() + ": Directory: " + "xml"
						+ " could not be created"); // TODO exception
			}
		}

		/* Generate the ExpML document */
		Document doc = new Document(new Element("result"));
		Element root = doc.getRootElement();

		List _results = results.getResults();
		if (_results != null) {
			Iterator itr = _results.iterator();
			while (itr.hasNext()) {
				Task next_task = (Task) itr.next();

				org.pikater.core.ontology.messages.Agent agent = next_task.getAgent();

				Element newExperiment = new Element("experiment");
				Element newSetting = new Element("setting");
				Element newAlgorithm = new Element("algorithm");
				newAlgorithm.setAttribute("name", agent.getType());
				newAlgorithm.setAttribute("libname", "weka");

				java.util.List<Option> Options = agent.getOptions();
				if (Options != null) {
					for (Option next_o : Options) {

						Element newParameter = new Element("parameter");
						newParameter.setAttribute("name", next_o.getName());

						String value = "";
						if (next_o.getValue() != null) {
							value = (String) next_o.getValue();
						}
						newParameter.setAttribute("value", value);

						newAlgorithm.addContent(newParameter);
					}
				}
				Element newDataSet = new Element("dataset");
				newDataSet.setAttribute("train", next_task.getData()
						.getExternal_train_file_name());
				newDataSet.setAttribute("test", next_task.getData()
						.getExternal_test_file_name());

				Element newEvaluation = new Element("evaluation");
								
				Element newMetric;
				for (Eval next_eval : next_task.getResult().getEvaluations() ) {

					newMetric = new Element("metric");					
					newMetric.setAttribute(next_eval.getName(), getXMLValue(next_eval.getValue()));
					
					newEvaluation.addContent(newMetric);
				}
								
				newExperiment.addContent(newSetting);
				newExperiment.addContent(newEvaluation);
				newSetting.addContent(newAlgorithm);
				newSetting.addContent(newDataSet);

				root.addContent(newExperiment);
			}
		}

		Element newStatistics = new Element("statistics");
		Element newMetric1 = new Element("metric");
		newMetric1.setAttribute("average_error_rate", getXMLValue(results
				.getAvg_error_rate()));
		Element newMetric2 = new Element("metric");
		newMetric2.setAttribute("average_kappa_statistic", getXMLValue(results
				.getAvg_kappa_statistic()));
		Element newMetric3 = new Element("metric");
		newMetric3.setAttribute("average_mean_absolute_error",
				getXMLValue(results.getAvg_mean_absolute_error()));
		Element newMetric4 = new Element("metric");
		newMetric4.setAttribute("average_root_mean_squared_error",
				getXMLValue(results.getAvg_root_mean_squared_error()));
		Element newMetric5 = new Element("metric");
		newMetric5.setAttribute("average_relative_absolute_error",
				getXMLValue(results.getAvg_relative_absolute_error()));
		Element newMetric6 = new Element("metric");
		newMetric6.setAttribute("average_root_relative_squared_error",
				getXMLValue(results.getAvg_root_relative_squared_error()));

		newStatistics.addContent(newMetric1);
		newStatistics.addContent(newMetric2);
		newStatistics.addContent(newMetric3);
		newStatistics.addContent(newMetric4);
		newStatistics.addContent(newMetric5);
		newStatistics.addContent(newMetric6);

		root.addContent(newStatistics);

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			FileWriter fw = new FileWriter(file_name);
			BufferedWriter fout = new BufferedWriter(fw);

			out.output(root, fout);

			fout.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	} // end writeXMLResults

	
	private String getXMLValue(float value) {
		if (value < 0) {
			return "NA";
		}
		return Double.toString(value);
	}

	
    private String getDateTimeXML() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    

	protected String generateProblemID() {		
		return Integer.toString(problem_i++);
	}


	private void addOptionToSchema(Option opt, List schema){
		String[] values = ((String)opt.getUser_value()).split(",");
		int numArgs = values.length;
		if (!opt.getIs_a_set()) {
			if (opt.getData_type().equals("INT") || opt.getData_type().equals("MIXED")) {
				for (int i = 0; i < numArgs; i++) {
					if (values[i].equals("?")) {
						IntSItem itm = new IntSItem();
						itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
						itm.setMin(opt.getRange().getMin().intValue());
						itm.setMax(opt.getRange().getMax().intValue());
						schema.add(itm);
					}
				}
			}else if (opt.getData_type().equals("FLOAT")) {
				for (int i = 0; i < numArgs; i++) {
					if (values[i].equals("?")) {
						FloatSItem itm = new FloatSItem();
						itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
						itm.setMin(opt.getRange().getMin());
						itm.setMax(opt.getRange().getMax());
						schema.add(itm);
					}
				}
			}else if (opt.getData_type().equals("BOOLEAN")) {
				BoolSItem itm = new BoolSItem();
				itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
				schema.add(itm);
			}
		}else{
			for (int i = 0; i < numArgs; i++) {
				if (values[i].equals("?")) {
					SetSItem itm = new SetSItem();
					itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
					itm.setSet(opt.getSet());
					schema.add(itm);
				}
			}
		}
	}


	private List getMutableOptions(List Options){
		List mutable = new ArrayList();
		Iterator itr = Options.iterator();
		while (itr.hasNext()) {
			Option o = (Option) itr.next();
			if (o.getMutable()){				
				mutable.add(o);
			}
		}
		return mutable;
	}

	
	//Create new options from solution with filled ? values (convert solution->options) 
	private Options fillOptionsWithSolution(List options, SearchSolution solution){
		Options res_options = new Options();
		List options_list = new ArrayList();
		if(options==null){
			return res_options;
		}
		//if no solution values to fill - return the option
		if(solution.getValues() == null){
			res_options.setList(options);
			return res_options;
		}
		Iterator sol_itr = solution.getValues().iterator();
		Iterator opt_itr = options.iterator();
		while (opt_itr.hasNext()) {
			Option opt = (Option) opt_itr.next();
			Option new_opt = opt.copyOption();
			if(opt.getMutable())
				new_opt.setValue(fillOptWithSolution(opt, sol_itr));
			options_list.add(new_opt);
		}
		res_options.setList(options_list);
		return res_options;
	}
	
	//Fill an option's ? with values in iterator
	private String fillOptWithSolution(Option opt, Iterator solution_itr){		
		String res_values = "";
		String[] values = ((String)opt.getUser_value()).split(",");
		int numArgs = values.length;
		for (int i = 0; i < numArgs; i++) {
			if (values[i].equals("?")) {
				res_values+=(String)solution_itr.next();
			}else{
				res_values+=values[i];
			}
			if (i < numArgs-1){
				res_values+=",";
			}
		}
		
		return res_values;
	}
	
	//Create schema of solutions from options (Convert options->schema)
	private List convertOptionsToSchema(List options){
		List new_schema = new ArrayList();
		if(options==null)
			return new_schema;
		Iterator itr = options.iterator();
		while (itr.hasNext()) {
			Option opt = (Option) itr.next();
			if(opt.getMutable())
				addOptionToSchema(opt, new_schema);
		}
		return new_schema;
	}
	
}




/*

	
	
	***************************************************************************
	
	
	
		protected List prepareTaskMessages(ACLMessage request, String problemId) {		

		List msgList = new ArrayList();
		// System.out.println("Agent "+getLocalName()+" failure :"+failure);

		ContentElement content;
		try {
			content = getContentManager().extractContent(request);
			// System.out.println("Agent " + getLocalName() + ": " + content);

			if (((Action) content).getAction() instanceof Solve) {
				
				Action action = (Action) content;
				Solve solve = (Solve) action.getAction();
				Problem problem = (Problem) solve.getProblem();

				problem.setId(new Id(problemId));

				int task_i = 0;
				Iterator d_itr = problem.getData().iterator();
				while (d_itr.hasNext()) {
					Data next_data = (Data) d_itr.next();

					if (next_data.getMetadata() != null) {
						next_data.getMetadata().setInternal_name(
								next_data.getTrain_file_name());
					}

					Iterator a_itr = problem.getAgents().iterator();
					while (a_itr.hasNext()) {												
						org.pikater.core.ontology.messages.Agent a_next =
								(org.pikater.core.ontology.messages.Agent) a_itr.next();
													
						org.pikater.core.ontology.messages.Agent a_next_copy =
								new org.pikater.core.ontology.messages.Agent();
						a_next_copy.setGui_id(a_next.getGui_id());
						a_next_copy.setName(a_next.getName());
						a_next_copy.setOptions(a_next.getOptions());
						a_next_copy.setType(a_next.getType());
						
						
						String agentType = a_next.getType();
						
						if (agentType.contains("?")) {
							// create recommender agent
							AID Recommender = createAgent(problem.getRecommender().getType(), null, null);
							
							// send task to recommender:
							ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
							req.addReceiver(Recommender);

							req.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

							req.setLanguage(codec.getName());
							req.setOntology(ontology.getName());
							// request.setReplyByDate(new Date(System.currentTimeMillis() + 200));

							Recommend recommend = new Recommend();
							recommend.setData(next_data);
							recommend.setRecommender(problem.getRecommender());
							
							Action a = new Action();
							a.setAction(recommend);
							a.setActor(this.getAID());

							try {
								getContentManager().fillContent(req, a);

								ACLMessage inform = FIPAService.doFipaRequestClient(this, req);

								Result r = (Result) getContentManager().extractContent(inform);

								// recommended agent from recommender
								a_next = (org.pikater.core.ontology.messages.Agent) r.getItems().get(0);
								a_next_copy = a_next;
								
							} catch (CodecException ce) {
								ce.printStackTrace();
							} catch (OntologyException oe) {
								oe.printStackTrace();
							} catch (FIPAException fe) {
								fe.printStackTrace();
							}							
							
						}													
						
						if (a_next != null) {
							Task task = new Task();
							task.setAgent(a_next_copy);
							task.setData(next_data);
							task.setProblem_id(new Id(problemId));
							task.setId(new Id(Integer.toString(task_i)));							
							task.setStart(getDateTime()); // if sent to options manager, overwritten							
							task.setGet_results(problem.getGet_results());
							task.setSave_results(problem.getSave_results());
							task.setGui_agent(problem.getGui_agent());
							task.setProblem_name(problem.getName());
							task.setEvaluation_method(problem.getEvaluation_method());
							task_i++;

							Execute ex = new Execute();
							ex.setTask(task);
							ex.setMethod(problem.getMethod());
							
							
							msgList.add(createRequestMessage(ex, getOptionsManager(ex)));
						}
					} // end while (iteration over files)

					// enter metadata to the table
					if (next_data.getMetadata() != null) {
						DataManagerService.saveMetadata(this, next_data
								.getMetadata());
					}

				} // end while (iteration over agents List)

			}
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

		return msgList;
	
	} // end prepareTaskMessages

	
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        Date date = new Date();
        return dateFormat.format(date);
    }

	
*/
