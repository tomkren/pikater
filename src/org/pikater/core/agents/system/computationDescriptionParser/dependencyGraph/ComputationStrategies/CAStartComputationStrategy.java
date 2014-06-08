package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.ExecuteTaskBehaviour;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.option.Options;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
import org.pikater.core.ontology.subtrees.task.Execute;
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
	ComputationNode computationNode;
	
	public CAStartComputationStrategy (Agent_Manager manager, int computationId, 
			int graphId, ComputationNode computationNode){
		myAgent = manager;
		this.computationId = computationId;
		this.computationNode = computationNode;
	}
	
	public void execute(ComputationNode computation){    	
		ACLMessage originalRequest = myAgent.getComputation(computationId).getMessage();						
		myAgent.addBehaviour(new ExecuteTaskBehaviour(myAgent, prepareRequest(), originalRequest, this));    	
    }		
	
	public void processError(ArrayList<Eval> errors){
		 ErrorEdge ee = new ErrorEdge();
		 ee.setErrors(errors);	
		 computationNode.addToOutputAndProcess(ee, "errors");
	}
		
	public void processValidation(String dataSourceName){
    	DataSourceEdge dse = new DataSourceEdge();
    	dse.setDataSourceId(dataSourceName);
    	computationNode.addToOutputAndProcess(dse, "validation");
    }
		
	private ACLMessage prepareRequest(){
		Execute ex = new Execute();
		Task task = getTaskFromNode();
				
		ex.setTask(task);							

		return myAgent.execute2Message(ex);
	}
	
	//Create new options from solution with filled ? values (convert solution->options) 
	private Options fillOptionsWithSolution(List<Option> options, SearchSolution solution){
		Options res_options = new Options();
		List<Option> options_list = new ArrayList<Option>();
		if(options==null){
			return res_options;
		}
		//if no solution values to fill - return the option
		if(solution.getValues() == null){
			res_options.setList(options);
			return res_options;
		}
		java.util.Iterator<String> sol_itr = solution.getValues().iterator();
		java.util.Iterator<Option> opt_itr = options.iterator();
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
	private String fillOptWithSolution(Option opt, java.util.Iterator<String> solution_itr){		
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
	
	private Task getTaskFromNode(){
		
		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();
				
		Agent agent = new Agent();
		agent.setType((String)inputs.get("modelType").getNext());
		
		Options options = (Options)inputs.get("options").getNext();
	    // TODO zbavit se Options -> list instead
		
		if (inputs.get("searchSolutution") != null){
			SearchSolution ss = (SearchSolution)inputs.get("searchSolutution").getNext();
			options =  fillOptionsWithSolution(options.getList(), ss);
		}
		agent.setOptions(options.getList());			
		
		Data data = new Data();
		data.setExternal_train_file_name((String)inputs.get("train").getNext());
		data.setExternal_test_file_name((String)inputs.get("test").getNext());				
		
		Task task = new Task();
		task.setNodeId(computationNode.getId());
		task.setGraphId(graphId);
		task.setAgent(agent);
		task.setData(data);
		task.setEvaluation_method((EvaluationMethod)inputs.get("evaluation_method").getNext());

		return task;
	}
	
}
