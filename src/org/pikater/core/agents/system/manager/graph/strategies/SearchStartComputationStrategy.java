package org.pikater.core.agents.system.manager.graph.strategies;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.manager.StartGettingParametersFromSearch;
import org.pikater.core.agents.system.manager.graph.ComputationNode;
import org.pikater.core.agents.system.manager.graph.SearchComputationNode;
import org.pikater.core.agents.system.manager.graph.StartComputationStrategy;
import org.pikater.core.agents.system.manager.graph.edges.ErrorEdge;
import org.pikater.core.agents.system.manager.graph.edges.OptionEdge;
import org.pikater.core.agents.system.manager.parser.ComputationOutputBuffer;
import org.pikater.core.ontology.SearchOntology;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.core.ontology.subtrees.newoption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.base.Value;
import org.pikater.core.ontology.subtrees.newoption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newoption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.GetParameters;
import org.pikater.core.ontology.subtrees.search.searchitems.IntervalSearchItem;
import org.pikater.core.ontology.subtrees.search.searchitems.SearchItem;
import org.pikater.core.ontology.subtrees.search.searchitems.SetSItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Startegy for searching - adds to manager StartGettingParametersFromSearch
 * User: Klara
 * Date: 18.5.2014
 * Time: 11:13
 */
public class SearchStartComputationStrategy implements StartComputationStrategy {
	Agent_Manager myAgent;
	int batchID;
	SearchComputationNode computationNode;
	Map<String,ComputationOutputBuffer> inputs;
    NewOptions options;
    OptionEdge childOptions;
    AID searchAID;

    /**
     * Constructor
     *
     * @param manager Manager agent that will receive StartGettingParametersFromSearch behavior
     * @param batchID  Id of the batch that this computation belongs to
     * @param computationNode Parent computation node
     */
	public SearchStartComputationStrategy (Agent_Manager manager,
			int batchID, SearchComputationNode computationNode) {
		myAgent = manager;
        this.batchID = batchID;
        this.computationNode = computationNode;
	}

    /**
     *
     * @param computation Computation node with this strategy
     */
	public void execute(ComputationNode computation) {
		
		ACLMessage originalRequest =
				myAgent.getComputation(batchID).getMessage();
		
        if (searchAID == null) {
            Agent search = getSearchFromNode();
            searchAID = myAgent.createAgent(search.getType(),
            		search.getName(), null);
        }

		inputs = computationNode.getInputs();
		
		if (inputs.get("error").isBlocked()){
			// start new parameter search
			
			StartGettingParametersFromSearch startSearch =
					new StartGettingParametersFromSearch( myAgent,
							prepareRequest(searchAID), this);
			
			myAgent.addBehaviour(startSearch);
			inputs.get("error").unblock();
		
		} else {
			// send results (errors) to search
			ErrorEdge errorEdge = (ErrorEdge)(inputs.get("error").getNext());
			
			String conversationID = batchID + "_" + computationNode.getId() +
					"_" + Integer.toString(errorEdge.getComputationId());
			
			ACLMessage query = myAgent.searchMessages.get(conversationID); 
			//remove from search messages, we wont need this anymore
            myAgent.searchMessages.remove(conversationID);
			ACLMessage inform = query.createReply();
			inform.setPerformative(ACLMessage.INFORM);

			try {			
			
				Action action = (Action)
						myAgent.getContentManager().extractContent(query);								
				
				Result result = new Result(action, errorEdge.getEvaluation());			

				myAgent.getContentManager().fillContent(inform, result);
			} catch (CodecException | OntologyException e) {
				myAgent.logException(e.getMessage(), e);
				e.printStackTrace();
			}

            myAgent.send(inform);

			computationNode.computationFinished();

		}
	
    }

    /**
     * Preperes request
     * @param receiver AID of the receiver
     * @return Request
     */
	private ACLMessage prepareRequest(AID receiver) {
		// prepare request for the search agent

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		msg.setLanguage(myAgent.getCodec().getName());
		msg.setOntology(SearchOntology.getInstance().getName());
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setConversationId(Integer.toString(batchID)+"_"+Integer.toString(computationNode.getId()));

		GetParameters getParameter = new GetParameters();
        if (childOptions == null) {
            childOptions = (OptionEdge)inputs.get("childoptions").getNext();
            inputs.get("childoptions").block();
        }
        
		List<SearchItem> schema =
				convertOptionsToSchema(childOptions.getOptions());
		getParameter.setSchema(schema);
		getParameter.setSearchOptions(options.getOptions());

        Action action = new Action();
		action.setAction(getParameter);
		action.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(msg, action);
		} catch (CodecException | OntologyException e) {
			myAgent.logException(e.getMessage(), e);
			e.printStackTrace();
		}

        return msg;
	}

    /**
     * Gets search agent from node buffers
     * @return Serach agent
     */
	private Agent getSearchFromNode(){

		Map<String,ComputationOutputBuffer> nodeInputs =
				computationNode.getInputs();

		Agent agent = new Agent();
		agent.setType(computationNode.getModelClass());
		
       if (options == null) {
           OptionEdge optionEdge = (OptionEdge)
        		   nodeInputs.get("options").getNext();
           nodeInputs.get("options").block();
           options = new NewOptions(optionEdge.getOptions());
       }
		agent.setOptions(options.getOptions());

		return agent;
	}


    /**
     * Adds options to schema
     * @param opt New option
     * @param schema Options to search
     */
	private void addOptionToSchema(NewOption opt, List<SearchItem> schema){
        ValuesForOption values = opt.getValuesWrapper();
		for (Value value:values.getValues()) {
            IValueData typedValue = value.getCurrentValue();
            
            if (typedValue instanceof QuestionMarkRange) {
            	
                QuestionMarkRange questionMarkRange =
                		(QuestionMarkRange) typedValue;
                IntervalSearchItem itm = new IntervalSearchItem();
                itm.setName(opt.getName());
                itm.setNumberOfValuesToTry(questionMarkRange.getCountOfValuesToTry());
                itm.setMin(questionMarkRange.getUserDefinedRestriction().getMinValue());
                itm.setMax(questionMarkRange.getUserDefinedRestriction().getMaxValue());
                schema.add(itm);
            
            } else if (typedValue instanceof QuestionMarkSet) {
            	
                QuestionMarkSet questionMarkSet = (QuestionMarkSet) typedValue;
                SetSItem itm = new SetSItem();
                itm.setName(opt.getName());
                itm.setNumberOfValuesToTry(questionMarkSet.getCountOfValuesToTry());
                itm.setSet(questionMarkSet.getUserDefinedRestriction().getValues());
                schema.add(itm);
            }
        }
	}

    /**
     * Create schema of solutions from options (Convert options->schema)
     * @param options List of new options
     * @return Items that will be searched
     */
	private List<SearchItem> convertOptionsToSchema(List<NewOption> options){
		List<SearchItem> result = new ArrayList<>();

		if (options == null) {
			return result;
		}
		
        for (NewOption optionI : options) {
            if (optionI.isMutable()) {
                addOptionToSchema(optionI, result);
            }
        }
		return result;
	}

    /**
     * Get computation node of this strategy
     * @return COmputationNode containing this strategy instance
     */
    public SearchComputationNode getComputationNode() {
        return computationNode;
    }
}
