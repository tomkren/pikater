package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ModelComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.AgentTypeEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.SolutionEdge;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.ExecuteTaskBehaviour;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class CAStartComputationStrategy implements StartComputationStrategy {
	
	Agent_Manager myAgent;
	int batchID;
	int experimentID;
	int userID;
	ModelComputationNode computationNode;
    NewOptions options;
    AgentTypeEdge agentTypeEdge;
	
	public CAStartComputationStrategy (Agent_Manager manager, int batchID,
			int experimentID, int userID, ModelComputationNode computationNode){
		this.myAgent = manager;
        this.batchID = batchID;
        this.experimentID = experimentID;
        this.userID = userID;
        this.computationNode = computationNode;
	}
	
	public void execute(ComputationNode computation) {    	
		ACLMessage originalRequest = myAgent.getComputation(batchID).getMessage();
		myAgent.addBehaviour(new ExecuteTaskBehaviour(myAgent, prepareRequest(), originalRequest, this,computationNode));
        computationNode.computationFinished();
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
		List<NewOption> options_list = new ArrayList<NewOption>();
		if(options==null){
			return res_options;
		}
		//if no solution values to fill - return the option
		if(solution.getValues() == null){
			res_options.setOptions(options);
			return res_options;
		}
        int currentSearchOptionNr=0;
        for (NewOption option:options)
        {
            if (option.isImmutable())
            {
                options_list.add(option);
            }
            else
            {
                for (Value value:option.getValuesWrapper().getValues()) {
                    IValueData typedValue = value.getCurrentValue();
                    if (typedValue instanceof QuestionMarkRange || typedValue instanceof QuestionMarkSet)
                    {
                        IValueData currentValue= solution.getValues().get(currentSearchOptionNr);
                        NewOption clone=option.clone();
                        clone.setValuesWrapper(new ValuesForOption(new Value(currentValue)));
                        options_list.add(clone);
                        currentSearchOptionNr++;
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
        	agentTypeEdge = (AgentTypeEdge)input.getNext();
            input.block();
        }
        agent.setType(agentTypeEdge.getAgentType());
        agent.setModel(agentTypeEdge.getModel());
        
        Task task = new Task();
		if (inputs.get("searchedoptions") != null){
            inputs.get("options").block();
            SolutionEdge solutionEdge = (SolutionEdge)inputs.get("searchedoptions").getNext();
            usedoptions =  fillOptionsWithSolution(options.getOptions(), solutionEdge.getOptions());
            task.setComputationID(solutionEdge.getComputationID());
		}
		agent.setOptions(usedoptions.getOptions());
		
		Datas datas = new Datas();	
		for (Map.Entry<String, ComputationOutputBuffer> in : inputs.entrySet())
		{
		    if (in.getValue().isData() ); 
		}
        DataSourceEdge trainingEdge=(DataSourceEdge)inputs.get(CoreConstant.Slot.SLOT_TRAINING_DATA.get()).getNext();
        DataSourceEdge testingEdge;
		String training = trainingEdge.getDataSourceId();
		if( inputs.get(CoreConstant.Slot.SLOT_TESTING_DATA.get()) == null){
			testingEdge=trainingEdge;
		}
		else{

			testingEdge = ((DataSourceEdge) inputs.get(CoreConstant.Slot.SLOT_TESTING_DATA.get()).getNext());
		}

        if (trainingEdge.isFile()) {
            datas.importExternalTrainFileName(training);
            String internalTrainFileName = DataManagerService
            		.translateExternalFilename(myAgent, userID, training);
            datas.importInternalTrainFileName(internalTrainFileName);
        }
        else
        {
            datas.importExternalTrainFileName(training);
            datas.importInternalTrainFileName(training);
        }
        if (testingEdge.isFile()) {
            datas.importExternalTestFileName(testingEdge.getDataSourceId());
            String fileName = DataManagerService
            		.translateExternalFilename(myAgent, userID, testingEdge.getDataSourceId());
            datas.importInternalTestFileName(fileName);
       }
        else
        {
            datas.importExternalTestFileName(testingEdge.getDataSourceId());
            datas.importInternalTestFileName(testingEdge.getDataSourceId());
        }

		ExpectedDuration duration = computationNode.getExpectedDuration();
		task.setBatchID(batchID);
		task.setExperimentID(experimentID);
		task.setUserID(userID);
		task.setSaveResults(true);
		task.setSaveMode("message");
		task.setNodeID(computationNode.getId());
		task.setAgent(agent);
		task.setDatas(datas);
		task.setExpectedDuration(duration);
		task.setPriority(computationNode.getPriority());
		task.setEvaluationMethod(computationNode.getEvaluationMethod());
		
		return task;
	}
	
	
	public ACLMessage execute2Message(ExecuteTask ex) {
		// create ACLMessage from Execute ontology action
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(myAgent.getCodec().getName());
		request.setOntology(TaskOntology.getInstance().getName());
		request.addReceiver(myAgent.getAgentByType(CoreAgents.PLANNER.getName()));
		
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	
		Action a = new Action();
		a.setAction(ex);
		a.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(request, a);
		} catch (CodecException e) {
			myAgent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}

        return request;
	}


}
