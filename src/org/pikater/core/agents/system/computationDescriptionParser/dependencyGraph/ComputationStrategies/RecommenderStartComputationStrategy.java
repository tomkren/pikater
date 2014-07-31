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

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.RecommenderComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.SearchComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.AgentTypeEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.SolutionEdge;
import org.pikater.core.agents.system.manager.StartGettingParametersFromSearch;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.SearchOntology;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.recommend.Recommend;
import org.pikater.core.ontology.subtrees.search.GetParameters;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.ontology.subtrees.search.searchItems.SetSItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class RecommenderStartComputationStrategy implements StartComputationStrategy{
	Agent_Manager myAgent;
	int graphId;
	RecommenderComputationNode computationNode;
	Map<String,ComputationOutputBuffer> inputs;
    NewOptions options;
    AID recommender;

    public RecommenderStartComputationStrategy (Agent_Manager manager,
			int graphId, RecommenderComputationNode computationNode){
		myAgent = manager;
        this.graphId = graphId;
        this.computationNode = computationNode;
	}

	public void execute(ComputationNode computation){
		Agent recommendedAgent = null;
		
		inputs = computationNode.getInputs();
		
		// create recommender agent
        if (recommender==null) {
            recommender = myAgent.createAgent(computationNode.getRecommenderClass());
        }

        if (inputs.get("error").isBlocked()){
			inputs.get("error").unblock();
		}
		
		// send message to recommender
		ACLMessage inform;
		try {
			inform = FIPAService.doFipaRequestClient(myAgent, prepareRequest(recommender));
			Result r = (Result) myAgent.getContentManager().extractContent(inform);
			
			recommendedAgent = (Agent) r.getItems().get(0);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// fill in the queues of CA
		AgentTypeEdge re = new AgentTypeEdge(recommendedAgent.getType());
		computationNode.addToOutputAndProcess(re, "agenttype", true);
		
        OptionEdge oe = new OptionEdge();
        oe.setOptions(recommendedAgent.getOptions());
		computationNode.addToOutputAndProcess(oe, "options", true);
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

		Recommend recommend = new Recommend();
		recommend.setData(data);
		recommend.setRecommender(getRecommenderFromNode());
//        if (inputs.get("error").hasNext()) {
//            recommend.setPreviousError(((ErrorEdge) inputs.get("error").getNext()).getEvaluation());
//        }

		Action a = new Action();
		a.setAction(recommend);
		a.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(req, a);
			
		} catch (CodecException | OntologyException ce) {
			ce.printStackTrace();
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
