package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

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
import org.pikater.core.agents.system.computationDescriptionParser.edges.AgentTypeEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.SolutionEdge;
import org.pikater.core.agents.system.manager.ExecuteTaskBehaviour;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.file.TranslateFilename;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class CAStartComputationStrategy implements StartComputationStrategy{
	
	Agent_Manager myAgent;
	int graphId;
	ModelComputationNode computationNode;
    NewOptions options;
	
	public CAStartComputationStrategy (Agent_Manager manager, 
			int graphId, ModelComputationNode computationNode){
		myAgent = manager;
        this.graphId = graphId;
        this.computationNode = computationNode;
	}
	
	public void execute(ComputationNode computation){    	
		ACLMessage originalRequest = myAgent.getComputation(graphId).getMessage();
		myAgent.addBehaviour(new ExecuteTaskBehaviour(myAgent, prepareRequest(), originalRequest, this));    	
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
	private NewOptions fillOptionsWithSolution(List<NewOption> options, SearchSolution solution){
        NewOptions res_options = new NewOptions();
		List<NewOption> options_list = new ArrayList<>();
		if(options==null){
			return res_options;
		}
		//if no solution values to fill - return the option
		if(solution.getValues() == null){
			res_options.setOptions(options);
			return res_options;
		}
        for (NewOption option:options)
        {
            if (option.isImmutable())
            {
                options_list.add(option);
            }
            else
            {
                for (int i=0;i<solution.getValues().size();i++)
                {
                    String currentName=solution.getNames().get(i);
                    if (currentName.equals(option.getName()))
                    {
                        IValueData currentValue= solution.getValues().get(i);
                        NewOption clone=option.clone();
                        clone.setValuesWrapper(new ValuesForOption(new Value(currentValue)));
                        options_list.add(clone);
                        break;
                    }
                }
            }
        }

		res_options.setOptions(options_list);
		return res_options;
	}

	
	private Task getTaskFromNode(){
		
		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();
				
		Agent agent = new Agent();
		ComputationOutputBuffer optionBuffer=inputs.get("options");
        if (!optionBuffer.isBlocked()) {
            OptionEdge optionEdge = (OptionEdge) optionBuffer.getNext();
            options = new NewOptions(optionEdge.getOptions());
        }
        NewOptions usedoptions=options;
        
        ComputationOutputBuffer input=inputs.get("agenttype");
        if (!input.isBlocked())
        {
        	AgentTypeEdge agentTypeEdge = (AgentTypeEdge)input.getNext();
            agent.setType(agentTypeEdge.getAgentType());
        }        
        
        Task task = new Task();
		if (inputs.get("searchedoptions") != null){
            inputs.get("options").block();
            SolutionEdge solutionEdge = (SolutionEdge)inputs.get("searchedoptions").getNext();
            usedoptions =  fillOptionsWithSolution(options.getOptions(), solutionEdge.getOptions());
            task.setComputationId(solutionEdge.getComputationID());
		}
		agent.setOptions(usedoptions.getOptions());
		
		Data data = new Data();
		String training = ((DataSourceEdge)inputs.get("training").getNext()).getDataSourceId();
		String testing;
		if( inputs.get("testing") == null){
			testing = training;							
		}
		else{
			testing = ((DataSourceEdge) inputs.get("testing").getNext()).getDataSourceId();
		}
		
		data.setExternal_train_file_name(training);
		data.setExternal_test_file_name(testing);
		data.setTestFileName(myAgent.getHashOfFile(training, 1));
		data.setTrainFileName(myAgent.getHashOfFile(testing, 1));

		IExpectedDuration duration = computationNode.getExpectedDuration();
		task.setSave_results(true);
		task.setNodeId(computationNode.getId());
		task.setGraphId(graphId);
		task.setAgent(agent);
		task.setData(data);
		task.setExpectedDuration(duration);
		task.setPriority(computationNode.getPriority());
		task.setExperimentID(computationNode.getExperimentID());
		task.setEvaluationMethod(computationNode.getEvaluationMethod());
		
		return task;
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
