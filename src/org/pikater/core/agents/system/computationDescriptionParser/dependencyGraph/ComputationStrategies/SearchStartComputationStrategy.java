package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.SearchComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.OptionEdge;
import org.pikater.core.agents.system.manager.StartGettingParametersFromSearch;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.core.ontology.subtrees.option.Options;
import org.pikater.core.ontology.subtrees.search.GetParameters;
import org.pikater.core.ontology.subtrees.search.searchItems.BoolSItem;
import org.pikater.core.ontology.subtrees.search.searchItems.FloatSItem;
import org.pikater.core.ontology.subtrees.search.searchItems.IntSItem;
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
public class SearchStartComputationStrategy implements StartComputationStrategy{
	Agent_Manager myAgent;
	int computationId;
	int graphId;
	SearchComputationNode computationNode;


	public SearchStartComputationStrategy (Agent_Manager manager, int computationId,
			int graphId, SearchComputationNode computationNode){
		myAgent = manager;
		this.computationId = computationId;
        this.graphId = graphId;
        this.computationNode = computationNode;
	}

	public void execute(ComputationNode computation){
		ACLMessage originalRequest = myAgent.getComputation(graphId).getMessage();
		
		Agent search = getSearchFromNode();
		AID searchAID = myAgent.createAgent(search.getType());

		myAgent.addBehaviour(new StartGettingParametersFromSearch(myAgent, originalRequest, prepareRequest(searchAID), this));
    }


	public void finished(){
		// TODO mozna? Je tady neco potreba?
	}
	

	private ACLMessage prepareRequest(AID receiver){
		// prepare request for the search agent

		Map<String, ComputationOutputBuffer> inputs = computationNode.getInputs();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		msg.setLanguage(myAgent.getCodec().getName());
		msg.setOntology(myAgent.getOntology().getName());
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setConversationId(Integer.toString(graphId)+"_"+Integer.toString(computationId));

		GetParameters gp = new GetParameters();
		@SuppressWarnings("unchecked")
		List<SearchItem> schema = convertOptionsToSchema((ArrayList<Option>)inputs.get("childoptions").getNext());
		gp.setSchema(schema);
		gp.setSearch_options((ArrayList<Option>)inputs.get("options").getNext());

        Action a = new Action();
		a.setAction(gp);
		a.setActor(myAgent.getAID());

		try {
			myAgent.getContentManager().fillContent(msg, a);
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;		
	}

	private Agent getSearchFromNode(){

		Map<String,ComputationOutputBuffer> inputs = computationNode.getInputs();

		Agent agent = new Agent();
		agent.setType(computationNode.getModelClass());
		
		OptionEdge optionEdge = (OptionEdge)inputs.get("options").getNext();
	    Options options = new Options(optionEdge.getOptions());
		agent.setOptions(options.getList());

		return agent;
	}


	private void addOptionToSchema(Option opt, List schema){
		String[] values = ((String)opt.getUser_value()).split(",");
		int numArgs = values.length;
		if (!opt.getIs_a_set()) {
			if (opt.getData_type().equals("INT") || opt.getData_type().equals("MIXED")) {
				for (int i = 0; i < numArgs; i++) {
					if (values[i].equals("?")) {
						IntSItem itm = new IntSItem();
						itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
						itm.setMin(opt.getRange().getMin().intValue());
						itm.setMax(opt.getRange().getMax().intValue());
						schema.add(itm);
					}
				}
			}else if (opt.getData_type().equals("FLOAT")) {
				for (int i = 0; i < numArgs; i++) {
					if (values[i].equals("?")) {
						FloatSItem itm = new FloatSItem();
						itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
						itm.setMin(opt.getRange().getMin());
						itm.setMax(opt.getRange().getMax());
						schema.add(itm);
					}
				}
			}else if (opt.getData_type().equals("BOOLEAN")) {
				BoolSItem itm = new BoolSItem();
				itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
				schema.add(itm);
			}
		}else{
			for (int i = 0; i < numArgs; i++) {
				if (values[i].equals("?")) {
					SetSItem itm = new SetSItem();
					itm.setNumber_of_values_to_try(opt.getNumber_of_values_to_try());
					itm.setSet(opt.getSet());
					schema.add(itm);
				}
			}
		}
	}


	//Create schema of solutions from options (Convert options->schema)
	private List convertOptionsToSchema(List<Option> options){
		List<Option> new_schema = new ArrayList<Option>();
		if(options==null)
			return new_schema;
		java.util.Iterator<Option> itr = options.iterator();
		while (itr.hasNext()) {
			Option opt = (Option) itr.next();
			if(opt.getMutable())
				addOptionToSchema(opt, new_schema);
		}
		return new_schema;
	}

}
