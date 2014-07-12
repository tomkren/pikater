package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ModelComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.agents.system.manager.ExecuteTaskBehaviour;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.file.TranslateFilename;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.OptionList;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

/**
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class CAStartComputationStrategy implements StartComputationStrategy{
	
	Agent_Manager myAgent;
	int computationId; 
	int graphId;
	ModelComputationNode computationNode;
	
	public CAStartComputationStrategy (Agent_Manager manager, int computationId, 
			int graphId, ModelComputationNode computationNode){
		myAgent = manager;
		this.computationId = computationId;
        this.graphId = graphId;
        this.computationNode = computationNode;
	}
	
	public void execute(ComputationNode computation){    	
		ACLMessage originalRequest = myAgent.getComputation(graphId).getMessage();
		myAgent.addBehaviour(new ExecuteTaskBehaviour(myAgent, prepareRequest(), originalRequest, this));    	
    }		
	
	public void processError(ArrayList<Eval> errors){
        for (Eval eval:errors)
        {
            if (computationNode.ContainsOutput(eval.getName()))
            {
                ErrorEdge ee = new ErrorEdge(eval.getValue());
                computationNode.addToOutputAndProcess(ee, "errors");
            }
        }
	}
		
	public void processValidation(String dataSourceName){
    	DataSourceEdge dse = new DataSourceEdge();
    	dse.setDataSourceId(dataSourceName);
    	computationNode.addToOutputAndProcess(dse, "validation");
    }
		
	private ACLMessage prepareRequest(){
		ExecuteTask ex = new ExecuteTask();
		Task task = getTaskFromNode();
				
		ex.setTask(task);							

		return execute2Message(ex);
	}
	
	//Create new options from solution with filled ? values (convert solution->options) 
	private OptionList fillOptionsWithSolution(List<NewOption> options, SearchSolution solution){
		OptionList res_options = new OptionList();
		List<NewOption> options_list = new ArrayList<NewOption>();
		if(options==null){
			return res_options;
		}
		//if no solution values to fill - return the option
		if(solution.getValues() == null){
			res_options.set(options);
			return res_options;
		}
		java.util.Iterator<String> sol_itr = solution.getValues().iterator();
		
		for (NewOption optionI : options) {
			NewOption new_opt = optionI.clone();
			if(optionI.getIsMutable())
				new_opt.setValuesWrapper(null); //TODO: fillOptWithSolution(optionI, sol_itr));
			options_list.add(new_opt);
		}
		res_options.set(options_list);
		return res_options;
	}
	
	//Fill an option's ? with values in iterator
	private String fillOptWithSolution(NewOption opt, java.util.Iterator<String> solution_itr){		
		String res_values = "";
		String[] values = new String[0]; //((String)opt.getUser_value()).split(",");
		//TODO: Jakub

		for (int i = 0; i < values.length; i++) {
			if (values[i].equals("?")) {
				res_values+=(String)solution_itr.next();
			}else{
				res_values+=values[i];
			}
			if (i < values.length-1){
				res_values+=",";
			}
		}
		
		return res_values;
	}
	
	private Task getTaskFromNode(){
		
		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();
				
		Agent agent = new Agent();
        OptionEdge optionEdge = (OptionEdge)inputs.get("options").getNext();
        OptionList options = new OptionList(optionEdge.getOptions());

        // TODO zbavit se Options -> list instead
        agent.setType(computationNode.getModelClass());
		if (inputs.get("searchSolution") != null){
			SearchSolution ss = (SearchSolution)inputs.get("searchSolution").getNext();
			options =  fillOptionsWithSolution(options.getAll(), ss);
		}
		agent.setOptions(options.getAll());			
		
		Data data = new Data();
		String training = ((DataSourceEdge)inputs.get("training").getNext()).getDataSourceId();
		String testing = ((DataSourceEdge) inputs.get("testing").getNext()).getDataSourceId();				
		data.setExternal_train_file_name(training);
		data.setExternal_test_file_name(testing);
		data.setTestFileName(getHashOfFile(training, 1));
		data.setTrainFileName(getHashOfFile(testing, 1));
		
		Task task = new Task();
		task.setNodeId(computationNode.getId());
		task.setGraphId(graphId);
		task.setAgent(agent);
		task.setData(data);
		task.setEvaluationMethod(computationNode.getEvaluationMethod());
		
		return task;
	}
	
	public String getHashOfFile(String nameOfFile, int userID) {
		
		TranslateFilename translate = new TranslateFilename();
		translate.setExternalFilename(nameOfFile);
		translate.setUserID(userID);

		// create a request message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(myAgent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(myAgent.getCodec().getName());
		msg.setOntology(FilenameTranslationOntology.getInstance().getName());
		// We want to receive a reply in 30 secs
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
		//msg.setConversationId(problem.getGui_id() + agent.getLocalName());

		Action a = new Action();
		a.setAction(translate);
		a.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}


		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(myAgent, msg);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ContentElement content = null;
		String fileHash = null;

		try {
			content = myAgent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			myAgent.logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			myAgent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			myAgent.logError(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			
			fileHash = (String) result.getValue();
		}
		
		return fileHash;
	}
	
	public ACLMessage execute2Message(ExecuteTask ex) {
		// create ACLMessage from Execute ontology action
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(myAgent.getCodec().getName());
		request.setOntology(TaskOntology.getInstance().getName());
		request.addReceiver(myAgent.getAgentByType(AgentNames.PLANNER));
		
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	
		Action a = new Action();
		a.setAction(ex);
		a.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(request, a);
		} catch (CodecException e) {
			myAgent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			myAgent.logError(e.getMessage(), e);
		}
		
		return request;
	}


}
