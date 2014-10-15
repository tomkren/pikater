package org.pikater.core.agents.system.computation.graph.strategies;

import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.ComputationNode;
import org.pikater.core.agents.system.computation.graph.DataProcessingComputationNode;
import org.pikater.core.agents.system.computation.graph.StartComputationStrategy;
import org.pikater.core.agents.system.computation.graph.edges.AgentTypeEdge;
import org.pikater.core.agents.system.computation.graph.edges.DataSourceEdge;
import org.pikater.core.agents.system.computation.graph.edges.OptionEdge;
import org.pikater.core.agents.system.computation.parser.ComputationOutputBuffer;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.ExecuteDataProcessingBehaviour;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

import java.util.Map;

/**
 * Strategy for data pre and post processing
 * User: Kuba
 * Date: 10.8.2014
 * Time: 16:00
 */
public class DataProcessingStrategy implements StartComputationStrategy {
    Agent_Manager myAgent;
    int batchID;
    int experimentID;
    private final int userID;
    DataProcessingComputationNode computationNode;
    NewOptions options;
    AgentTypeEdge agentTypeEdge;

    /**
     * Constructor
     * @param manager Manager agent
     * @param batchID  Id of the batch that this computation belongs to
     * @param experimentID Id of the experiment that this computation belongs to
     * @param userID User id of the owner of this experiment
     * @param computationNode Parent computation node
     */
    public DataProcessingStrategy(Agent_Manager manager, int batchID,
    		int experimentID, int userID,
    		DataProcessingComputationNode computationNode) {
        myAgent = manager;
        this.batchID = batchID;
        this.experimentID = experimentID;
        this.userID = userID;
        this.computationNode = computationNode;
    }

    /**
     *
     * @param computation Computation node with this strategy
     */
    @Override
    public void execute(ComputationNode computation) {
    	
        ACLMessage originalRequest =
        		myAgent.getComputation(batchID).getMessage();
        
        ExecuteDataProcessingBehaviour executeBehaviour =
        		new ExecuteDataProcessingBehaviour(myAgent, prepareRequest(),
        				originalRequest, computationNode);
        
        myAgent.addBehaviour(executeBehaviour);
        computationNode.computationFinished();
    }

    /**
     * Prepares the request
     * @return Request
     */
    private ACLMessage prepareRequest() {

        Task task = getTaskFromNode();
        
        ExecuteTask executeTask = new ExecuteTask();
        executeTask.setTask(task);

        return execute2Message(executeTask);
    }

    /**
     * Get the task from buffers of the computation node
     * @return Processing task
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
        NewOptions usedoptions = options;

        ComputationOutputBuffer input = inputs.get("agenttype");
        if (!input.isBlocked()) {
            agentTypeEdge = (AgentTypeEdge)input.getNext();
            input.block();
        }

        agent.setOptions(usedoptions.getOptions());

        Datas datas = new Datas();

        for (Object o : inputs.entrySet()) {
            Map.Entry<String, ComputationOutputBuffer> pairs = (Map.Entry) o;

            ComputationOutputBuffer cob = pairs.getValue();

            if (cob.isData()) {
                // add to Datas
                String dataName = ((DataSourceEdge) cob.getNext()).getDataSourceId();

                String internalFileName = DataManagerService
                        .translateExternalFilename(myAgent, userID, dataName);

                datas.addData(
                        new Data(dataName,
                                internalFileName,
                                pairs.getKey()
                        ));
            }
        }
       
        String agentClass = agentTypeEdge.getAgentType();
        agent.setName(agentClass);
        agent.setType(agentClass);
        
        Task task = new Task();
        task.setAgent(agent);
        task.setDatas(datas);
        task.setBatchID(batchID);
        task.setExperimentID(experimentID);
        task.setUserID(userID);
        task.setNodeID(computationNode.getId());
        task.setExpectedDuration(new ExpectedDuration());
        task.setSaveResults(false);
        task.setSaveMode("message");

        return task;
    }

    /**
     * Create ACLMessage from Execute ontology action
     * @param executeTask ExecuteTask ontology
     * @return Message
     */
    private ACLMessage execute2Message(ExecuteTask executeTask) {

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(myAgent.getCodec().getName());
        request.setOntology(TaskOntology.getInstance().getName());

        request.addReceiver(new AID(CoreAgents.PLANNER.getName(), false));

        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        Action action= new Action();
        action.setAction(executeTask);
        action.setActor(myAgent.getAID());

        try {
            myAgent.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            myAgent.logException(e.getMessage(), e);
        }
        return request;

    }

}
