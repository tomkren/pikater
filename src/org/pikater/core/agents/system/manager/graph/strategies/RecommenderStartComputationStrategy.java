package org.pikater.core.agents.system.manager.graph.strategies;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.graph.ComputationNode;
import org.pikater.core.agents.system.manager.graph.RecommenderComputationNode;
import org.pikater.core.agents.system.manager.graph.StartComputationStrategy;
import org.pikater.core.agents.system.manager.graph.edges.AgentTypeEdge;
import org.pikater.core.agents.system.manager.graph.edges.DataSourceEdge;
import org.pikater.core.agents.system.manager.graph.edges.ErrorEdge;
import org.pikater.core.agents.system.manager.graph.edges.OptionEdge;
import org.pikater.core.agents.system.manager.parser.ComputationOutputBuffer;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.core.ontology.subtrees.recommend.Recommend;

import java.util.Map;

/**
 * Strategy for recommendation
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class RecommenderStartComputationStrategy implements StartComputationStrategy{
	Agent_Manager myAgent;
	int userID;
	RecommenderComputationNode computationNode;
	Map<String,ComputationOutputBuffer> inputs;
    NewOptions options;
    AID recommender;

    /**
     *  @param manager Manager agent that will receive ExecuteTaskBehaviour behavior
     * @param userID User id of the owner of this experiemtn
     * @param computationNode Parent computation node
     */
    public RecommenderStartComputationStrategy(Agent_Manager manager,
    		int userID, RecommenderComputationNode computationNode) {
    	
		this.myAgent = manager;
        this.userID = userID;
        this.computationNode = computationNode;
	}

    /**
     *
     * @param computation Computation node with this strategy
     */
	public void execute(ComputationNode computation){
		Agent recommendedAgent;
		
		inputs = computationNode.getInputs();
		
		// create recommender agent
        if (recommender == null) {
            recommender = myAgent.createAgent(
            		computationNode.getRecommenderClass());
        }

		String errorSlotName =
				CoreConstant.SlotContent.ERRORS.getSlotName();
		
        if (inputs.get(errorSlotName).isBlocked()){
			inputs.get(errorSlotName).unblock();
		}
		
		// send message to recommender
		ACLMessage inform;
		try {
			inform = FIPAService.doFipaRequestClient(myAgent,
					prepareRequest(recommender));
			
			if (inform.getContent().equals("finished")) {
				this.computationNode.computationFinished();
				return;
			}
			
			Result result = (Result)
					myAgent.getContentManager().extractContent(inform);			
			
			recommendedAgent = (Agent) result.getItems().get(0);
		} catch (FIPAException e) {
			myAgent.logException(e.getMessage(), e);
			return;
		} catch (CodecException | OntologyException e) {
			myAgent.logException(e.getMessage(), e);
			return;
		}

        // fill in the queues of CA
		AgentTypeEdge re = new AgentTypeEdge(recommendedAgent.getType());
		computationNode.addToOutputAndProcess(re, "agenttype", true, false);
		
        OptionEdge oe = new OptionEdge();
        oe.setOptions(recommendedAgent.getOptions());
		computationNode.addToOutputAndProcess(oe, "options", true, false);
        computationNode.computationFinished();
    }

    /**
     * Prepares recommending request
     * @param receiver Receiver of request
     * @return Request
     */
	private ACLMessage prepareRequest(AID receiver){
		// send task to recommender:
		ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
		req.addReceiver(receiver);

		req.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		req.setLanguage(myAgent.getCodec().getName());
		req.setOntology(RecommendOntology.getInstance().getName());
		// request.setReplyByDate(new Date(System.currentTimeMillis() + 200));
		
		String trainingSlotName =
				CoreConstant.SlotContent.TRAINING_DATA.getSlotName();
		String tesingSlotName =
				CoreConstant.SlotContent.TESTING_DATA.getSlotName();
		String errorSlotName =
				CoreConstant.SlotContent.ERRORS.getSlotName();
		
		Datas datas = new Datas();
		DataSourceEdge trainingEdge = (DataSourceEdge)
				inputs.get(trainingSlotName).getNext();
		String training = trainingEdge.getDataSourceId();
		
		String testing;
		if( inputs.get(tesingSlotName) == null){
			testing = training;

		} else {
			DataSourceEdge testingEdge =
					(DataSourceEdge) inputs.get(tesingSlotName).getNext();
			testing = testingEdge.getDataSourceId();
		}
		
        String internalTrainFileName = DataManagerService
        		.translateExternalFilename(myAgent, userID, training);
        String internalTestFileName = DataManagerService
        		.translateExternalFilename(myAgent, userID, testing);

		datas.importExternalTrainFileName(training);
		datas.importExternalTestFileName(testing);
		datas.importInternalTrainFileName(internalTrainFileName);
		datas.importInternalTestFileName(internalTestFileName);

		Recommend recommend = new Recommend();
		recommend.setDatas(datas);
		recommend.setRecommender(getRecommenderFromNode());
		
        if (inputs.get(errorSlotName).hasNext()) {
        	ErrorEdge errorEdge =
        			(ErrorEdge) inputs.get(errorSlotName).getNext();
            recommend.setPreviousError(errorEdge.getEvaluation());
        }

		Action action = new Action();
		action.setAction(recommend);
		action.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(req, action);
			
		} catch (CodecException | OntologyException ce) {
			myAgent.logException(ce.getMessage(), ce);
		}

        return req;
	}

    /**
     * Gets recommender agent from node buffers
     * @return Recommender
     */
	private Agent getRecommenderFromNode()
	{
		Map<String,ComputationOutputBuffer> nodeInputs =
				computationNode.getInputs();

		Agent agent = new Agent();
		agent.setType(computationNode.getRecommenderClass());
        if (options == null) {
        	OptionEdge optionEdge = (OptionEdge)
        			nodeInputs.get("options").getNext();
            nodeInputs.get("options").block();
            options = new NewOptions(optionEdge.getOptions());
        }
		agent.setOptions(options.getOptions());
		return agent;
	}
}
