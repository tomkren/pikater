package org.pikater.core.agents.system.computation.graph.strategies;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.ComputationNode;
import org.pikater.core.agents.system.computation.graph.ModelComputationNode;
import org.pikater.core.agents.system.computation.graph.StartComputationStrategy;
import org.pikater.core.agents.system.computation.graph.edges.AgentTypeEdge;
import org.pikater.core.agents.system.computation.graph.edges.DataSourceEdge;
import org.pikater.core.agents.system.computation.graph.edges.OptionEdge;
import org.pikater.core.agents.system.computation.graph.edges.SolutionEdge;
import org.pikater.core.agents.system.computation.parser.ComputationOutputBuffer;
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
 * This strategy triggers the computation - send to planner and waits for results by adding ExecuteTaskBehaviour
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

    /**
     * Constructor
     * 
     * @param manager Manager agent that will receive ExecuteTaskBehaviour behavior
     * @param batchID  Id of the batch that this computation belongs to
     * @param experimentID Id of the experiment that this computation belongs to
     * @param userID User id of the owner of this experiemtn
     * @param computationNode Parent computation node
     */
	public CAStartComputationStrategy (Agent_Manager manager, int batchID,
			int experimentID, int userID, ModelComputationNode computationNode) {
		this.myAgent = manager;
        this.batchID = batchID;
        this.experimentID = experimentID;
        this.userID = userID;
        this.computationNode = computationNode;
	}

    /**
     *
     * @param computation Computation node with this strategy
     */
	public void execute(ComputationNode computation) {
		
		ACLMessage originalRequest =
				myAgent.getComputation(batchID).getMessage();
		
		ExecuteTaskBehaviour exebahaviour = new ExecuteTaskBehaviour(
				myAgent, prepareRequest(), originalRequest, computationNode);
		
		myAgent.addBehaviour(exebahaviour);
        computationNode.computationFinished();
    }

    /**
     * Add validation to outputs
     * @param dataSourceName Generated id of the datasource with validation data
     */
	public void processValidation(String dataSourceName){
    	DataSourceEdge dse = new DataSourceEdge();
    	dse.setDataSourceId(dataSourceName);
    	computationNode.addToOutputAndProcess(dse, "validation");
    }

    /**
     * Prepare request for the manager
     * @return Message for manager
     */
	private ACLMessage prepareRequest(){
		ExecuteTask executeTask = new ExecuteTask();
		Task task = getTaskFromNode();
				
		executeTask.setTask(task);							

		return execute2Message(executeTask);
	}
	
	/**
	 * Create new options from solution with filled ?
	 * values (convert solution->options)
	 *  
	 * @param options Options
	 * @param solution Sollution to be filled
	 * @return NewOptions list
	 */
	private NewOptions fillOptionsWithSolution(List<NewOption> options,
			SearchSolution solution) {
		
        NewOptions result = new NewOptions();
		if (options == null) {
			return result;
		
		// if no solution values to fill - return the option
		} else if(solution.getValues() == null) {
			result.setOptions(options);
			return result;
		
		} else {
			List<NewOption> resultList = new ArrayList<>();
			int currentSearchOptionNr = 0;
	        for (NewOption option : options) {
	            if (option.isImmutable()) {
	                resultList.add(option);
	            } else {
	                for (Value value:option.getValuesWrapper().getValues()) {
	                    IValueData typedValue = value.getCurrentValue();
	                    if (typedValue instanceof QuestionMarkRange ||
	                    		typedValue instanceof QuestionMarkSet) {
	                    	/*
	                    	 * TODO: this is very much prone to errors because it makes the inner
	                    	 * state of NewOption inconsistent. You should use constructors (e.g.
	                    	 * "NewOption(String, Value)") because the option must have the type
	                    	 * defined for each value. Type is inferred in the example constructor.  
	                    	 */
	                        IValueData currentValue =
	                        		solution.getValues().get(currentSearchOptionNr);
	                        NewOption clone = option.clone();
	                        clone.setValuesWrapper(
	                        		new ValuesForOption(new Value(currentValue)));
	                        resultList.add(clone);
	                        currentSearchOptionNr++;
	                    }
	                }
	            }
	        }
	        result.setOptions(resultList);
			return result;
		}
	}


    /**
     * Gte task for CA from the buffers of this node
     * @return Task describing ML/DM experiment
     */
	private Task getTaskFromNode() {
		
		Map<String, ComputationOutputBuffer> inputs =
				computationNode.getInputs();
				
		Agent agent = new Agent();
		ComputationOutputBuffer optionBuffer = inputs.get("options");
        if (!optionBuffer.isBlocked()) {
            OptionEdge optionEdge = (OptionEdge) optionBuffer.getNext();
            options = new NewOptions(optionEdge.getOptions());
        }
        NewOptions usedoptions=options;
        
        ComputationOutputBuffer input = inputs.get("agenttype");
        if (!input.isBlocked()) {
        	agentTypeEdge = (AgentTypeEdge)input.getNext();
            input.block();
        }
        agent.setType(agentTypeEdge.getAgentType());
        agent.setModel(agentTypeEdge.getModel());
        
        Task task = new Task();
		if (inputs.get("searchedoptions") != null){
            inputs.get("options").block();
            SolutionEdge solutionEdge = (SolutionEdge)
            		inputs.get("searchedoptions").getNext();
            usedoptions =  fillOptionsWithSolution(
            		options.getOptions(), solutionEdge.getOptions());
            task.setComputationID(solutionEdge.getComputationID());
		}
		agent.setOptions(usedoptions.getOptions());
		
		Datas datas = new Datas();
        DataSourceEdge trainingEdge = (DataSourceEdge)inputs.get(CoreConstant.SlotContent.TRAINING_DATA.getSlotName()).getNext();
        DataSourceEdge testingEdge;
		String training = trainingEdge.getDataSourceId();
		if( inputs.get(CoreConstant.SlotContent.TESTING_DATA.getSlotName()) == null){
			testingEdge=trainingEdge;
		} else {

			testingEdge = ((DataSourceEdge) inputs.get(CoreConstant.SlotContent.TESTING_DATA.getSlotName()).getNext());
		}

        if (trainingEdge.isFile()) {
            datas.importExternalTrainFileName(training);
            String internalTrainFileName = DataManagerService
            		.translateExternalFilename(myAgent, userID, training);
            datas.importInternalTrainFileName(internalTrainFileName);
        } else {
            datas.importExternalTrainFileName(training);
            datas.importInternalTrainFileName(training);
        }
        if (testingEdge.isFile()) {
            datas.importExternalTestFileName(testingEdge.getDataSourceId());
            String fileName = DataManagerService
            		.translateExternalFilename(myAgent, userID, testingEdge.getDataSourceId());
            datas.importInternalTestFileName(fileName);
        } else {
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
	
	/**
	 * Creates ACLMessage from Execute ontology action
	 * 
	 * @param executeTask Ontology execute Task
	 * @return Execute message - request to CA
	 */
	private ACLMessage execute2Message(ExecuteTask executeTask) {
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(myAgent.getCodec().getName());
		request.setOntology(TaskOntology.getInstance().getName());
		request.addReceiver(myAgent.getAgentByType(CoreAgents.PLANNER.getName()));
		
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	
		Action action = new Action();
		action.setAction(executeTask);
		action.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(request, action);
		} catch (CodecException | OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}

        return request;
	}


}
