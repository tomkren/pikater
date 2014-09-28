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

import java.util.Iterator;
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
     *
     * @param manager Manager agent
     * @param batchID  Id of the batch that this computation belongs to
     * @param experimentID Id of the experiment that this computation belongs to
     * @param userID User id of the owner of this experiemtn
     * @param computationNode Parent computation node
     */
    public DataProcessingStrategy(Agent_Manager manager, int batchID,
    		int experimentID, int userID, DataProcessingComputationNode computationNode) {
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
        ACLMessage originalRequest = myAgent.getComputation(batchID).getMessage();
        myAgent.addBehaviour(new ExecuteDataProcessingBehaviour(myAgent, prepareRequest(), originalRequest, this,computationNode));
        computationNode.computationFinished();
    }

    private ACLMessage prepareRequest() {
        ExecuteTask ex = new ExecuteTask();
        Task task = getTaskFromNode();

        ex.setTask(task);

        return execute2Message(ex);
    }

    private Task getTaskFromNode() {

        Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();

        Agent agent = new Agent();
        ComputationOutputBuffer optionBuffer = inputs.get("options");
        if (!optionBuffer.isBlocked()) {
            OptionEdge optionEdge = (OptionEdge) optionBuffer.getNext();
            options = new NewOptions(optionEdge.getOptions());
        }
        NewOptions usedoptions = options;

        ComputationOutputBuffer input=inputs.get("agenttype");
        if (!input.isBlocked())
        {
            agentTypeEdge = (AgentTypeEdge)input.getNext();
            input.block();
        }

        Task task = new Task();
        // uncomment and implement if preprocessing should be searchable
//        if (inputs.get("searchedoptions") != null){
//            inputs.get("options").block();
//            SolutionEdge solutionEdge = (SolutionEdge)inputs.get("searchedoptions").getNext();
//            usedoptions =  fillOptionsWithSolution(options.getOptions(), solutionEdge.getOptions());
//            task.setComputationID(solutionEdge.getComputationID());
//        }
        agent.setOptions(usedoptions.getOptions());

        Datas datas = new Datas();
        
        Iterator it = inputs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ComputationOutputBuffer> pairs = (Map.Entry)it.next();

            ComputationOutputBuffer cob = (ComputationOutputBuffer) pairs.getValue();
            
            if (cob.isData() ){
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

    private ACLMessage execute2Message(ExecuteTask ex) {
        // create ACLMessage from Execute ontology action

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(myAgent.getCodec().getName());
        request.setOntology(TaskOntology.getInstance().getName());

        request.addReceiver(new AID(CoreAgents.PLANNER.getName(), false));

        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        Action a = new Action();
        a.setAction(ex);
        a.setActor(myAgent.getAID());

        try {
            myAgent.getContentManager().fillContent(request, a);
        } catch (Codec.CodecException e) {
            myAgent.logException(e.getMessage(), e);
        } catch (OntologyException e) {
            myAgent.logException(e.getMessage(), e);
        }
        return request;

    }

}
