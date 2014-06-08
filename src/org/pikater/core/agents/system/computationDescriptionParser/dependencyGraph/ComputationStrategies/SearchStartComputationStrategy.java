package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.ExecuteTaskBehaviour;
import org.pikater.core.agents.system.StartGettingParametersFromSearch;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.ontology.messages.Agent;
import org.pikater.core.ontology.messages.Data;
import org.pikater.core.ontology.messages.Eval;
import org.pikater.core.ontology.messages.EvaluationMethod;
import org.pikater.core.ontology.messages.Execute;
import org.pikater.core.ontology.messages.GetParameters;
import org.pikater.core.ontology.messages.Task;
import org.pikater.core.ontology.messages.TaskOutput;
import org.pikater.core.ontology.messages.option.Option;
import org.pikater.core.ontology.search.searchItems.BoolSItem;
import org.pikater.core.ontology.search.searchItems.FloatSItem;
import org.pikater.core.ontology.search.searchItems.IntSItem;
import org.pikater.core.ontology.search.searchItems.SetSItem;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.EdgeValue;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.management.ManagerAgentCommunicator;

/**
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class SearchStartComputationStrategy implements StartComputationStrategy{
	
	Agent_Manager myAgent;
	int computationId; 
	int graphId;
	ComputationNode computationNode;
	
	
	public SearchStartComputationStrategy (Agent_Manager manager, int computationId, 
			int graphId, ComputationNode computationNode){
		myAgent = manager;
		this.computationId = computationId;
		this.computationNode = computationNode;
	}
	
	public void execute(ComputationNode computation){    	
		ACLMessage originalRequest = myAgent.getComputation(computationId).getMessage();
				                    
		Agent search = getSearchFromNode();
		AID searchAID = createSearchAgent(search);
        
		List<Option> mutable_options = getMutableOptions(search.getOptions());        
		
		myAgent.addBehaviour(new StartGettingParametersFromSearch(myAgent, originalRequest, prepareRequest(searchAID), this));    	
    }		
		
					
	public void processFinished(){
		TODO
	}
	
	private AID createSearchAgent(Agent search){
		ManagerAgentCommunicator communicator=new ManagerAgentCommunicator("agentManager");
		String type = search.getType();
		return communicator.createAgent(myAgent,type,null,null);
	}

	
	private ACLMessage prepareRequest(AID receiver){
		// prepare request for the search agent				
		
		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);							
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		GetParameters gp = new GetParameters();
		List schema = convertOptionsToSchema((ArrayList<Option>)inputs.get("CAoptions").getNext());
		gp.setSchema(schema);
		gp.setSearch_options((ArrayList<Option>)inputs.get("options").getNext());

        Action a = new Action();
		a.setAction(gp);
		a.setActor(myAgent.getAID());

		myAgent.getContentManager().fillContent(msg, a);
		
		return msg;
	}
	
	private Task getTaskFromNode(){
		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();
				
		Agent agent = new Agent();
		agent.setType((String)inputs.get("modelType").getNext());
		agent.setOptions((ArrayList<Option>)inputs.get("options").getNext());
		
		Data data = new Data();
		data.setExternal_train_file_name((String)inputs.get("train").getNext());
		data.setExternal_test_file_name((String)inputs.get("test").getNext());
		
		Task task = new Task();
		task.setNodeId(computationNode.getId());
		task.setGraphId(graphId);
		task.setAgent(agent);
		task.setData(data);
		task.setEvaluation_method((EvaluationMethod)inputs.get("evaluation_method").getNext());
		
		// TODO set note
		// received_task.setNote(Integer.toString(taskNumber));
		// increaseTaskNumber();	
		
		return task;
	}

	private Agent getSearchFromNode(){		
		
		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();
		
		Agent agent = new Agent();
		agent.setType((String)inputs.get("searchType").getNext());
		agent.setOptions((ArrayList<Option>)inputs.get("options").getNext());
		
		return agent;				
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
	
		
	//Create schema of solutions from options (Convert options->schema)
	private List convertOptionsToSchema(List<Option> options){
		List<Option> new_schema = new ArrayList<Option>();
		if(options==null)
			return new_schema;
		java.util.Iterator<Option> itr = options.iterator();
		while (itr.hasNext()) {
			Option opt = (Option) itr.next();
			if(opt.getMutable())
				addOptionToSchema(opt, new_schema);
		}
		return new_schema;
	}	
	
	private List<Option> getMutableOptions(List<Option> Options){
		List<Option> mutable = new ArrayList<Option>();
		java.util.Iterator<Option> itr = Options.iterator();
		while (itr.hasNext()) {
			Option o = (Option) itr.next();
			if (o.getMutable()){				
				mutable.add(o);
			}
		}
		return mutable;
	}
	
}
