package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

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

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.RecommenderComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.AgentTypeEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.recommend.Recommend;

import java.util.Map;

/**
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class RecommenderStartComputationStrategy implements StartComputationStrategy{
	Agent_Manager myAgent;
	int batchID;
	int userID;
	RecommenderComputationNode computationNode;
	Map<String,ComputationOutputBuffer> inputs;
    NewOptions options;
    AID recommender;

    public RecommenderStartComputationStrategy (Agent_Manager manager,
			int batchID, int userID, RecommenderComputationNode computationNode){
		this.myAgent = manager;
        this.batchID = batchID;
        this.userID = userID;
        this.computationNode = computationNode;
	}

	public void execute(ComputationNode computation){
		Agent recommendedAgent = null;
		
		inputs = computationNode.getInputs();
		
		// create recommender agent
        if (recommender==null) {
            recommender = myAgent.createAgent(computationNode.getRecommenderClass());
        }

        if (inputs.get(CoreConstant.Slot.SLOT_ERRORS.get()).isBlocked()){
			inputs.get(CoreConstant.Slot.SLOT_ERRORS.get()).unblock();
		}
		
		// send message to recommender
		ACLMessage inform;
		try {
			inform = FIPAService.doFipaRequestClient(myAgent, prepareRequest(recommender));
			if (inform.getContent().equals("finished")){
				this.computationNode.computationFinished();
				return;
			}
			
			Result r = (Result) myAgent.getContentManager().extractContent(inform);			
			
			recommendedAgent = (Agent) r.getItems().get(0);
		} catch (FIPAException e) {
			myAgent.logException(e.getMessage(), e);
		} catch (UngroundedException e) {
			myAgent.logException(e.getMessage(), e);
		} catch (CodecException e) {
			myAgent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}
		
		// fill in the queues of CA
		AgentTypeEdge re = new AgentTypeEdge(recommendedAgent.getType());
		computationNode.addToOutputAndProcess(re, "agenttype", true, false);
		
        OptionEdge oe = new OptionEdge();
        oe.setOptions(recommendedAgent.getOptions());
		computationNode.addToOutputAndProcess(oe, "options", true, false);
        computationNode.computationFinished();
    }

	private ACLMessage prepareRequest(AID receiver){
		// send task to recommender:
		ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
		req.addReceiver(receiver);

		req.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		req.setLanguage(myAgent.getCodec().getName());
		req.setOntology(RecommendOntology.getInstance().getName());
		// request.setReplyByDate(new Date(System.currentTimeMillis() + 200));
		
		Datas datas = new Datas();
		String training = ((DataSourceEdge)inputs.get(CoreConstant.Slot.SLOT_TRAINING_DATA.get()).getNext()).getDataSourceId();
		String testing;
		if( inputs.get(CoreConstant.Slot.SLOT_TESTING_DATA.get()) == null){
			testing = training;							
		}
		else{
			testing = ((DataSourceEdge) inputs.get(CoreConstant.Slot.SLOT_TESTING_DATA.get()).getNext()).getDataSourceId();
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
        if (inputs.get(CoreConstant.Slot.SLOT_ERRORS.get()).hasNext()) {
            recommend.setPreviousError(((ErrorEdge) inputs.get(CoreConstant.Slot.SLOT_ERRORS.get()).getNext()).getEvaluation());
        }

		Action a = new Action();
		a.setAction(recommend);
		a.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(req, a);
			
		} catch (CodecException ce) {
			myAgent.logException(ce.getMessage(), ce);
		} catch (OntologyException ce) {
			myAgent.logException(ce.getMessage(), ce);			
		}

        return req;
	}

	private Agent getRecommenderFromNode(){

		Map<String,ComputationOutputBuffer> inputs = computationNode.getInputs();

		Agent agent = new Agent();
		agent.setType(computationNode.getRecommenderClass());
       if (options==null) {
           OptionEdge optionEdge = (OptionEdge) inputs.get("options").getNext();
           inputs.get("options").block();
           options = new NewOptions(optionEdge.getOptions());
       }
		agent.setOptions(options.getOptions());

		return agent;
	}

    public RecommenderComputationNode getComputationNode() {
        return computationNode;
    }
}
