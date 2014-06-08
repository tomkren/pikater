package org.pikater.core.agents.system;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.agents.system.computationDescriptionParser.Parser;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationGraph;
import org.pikater.core.ontology.batch.ExecuteBatch;
import org.pikater.core.ontology.description.ComputationDescription;

class ParserBehaviour extends AchieveREResponder {
	
	private static final long serialVersionUID = 4754473043512463873L;
	
	private Agent_Manager agent;
	private Codec codec = null;
	private Ontology ontology = null;

    public ParserBehaviour(
    		Agent_Manager agent_Manager, Codec codec, Ontology ontology) {
    	super(agent_Manager, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    	
		this.agent = agent_Manager;
		this.codec = codec;
		this.ontology = ontology;
    }
    
    @Override
    protected ACLMessage handleRequest(final ACLMessage request) throws NotUnderstoodException, RefuseException {
   
    	Concept object = null;
 
    	try {
        	//Serializable object = request.getContentObject();
            object = ((Action)(agent.getContentManager().extractContent(request))).getAction();
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
            
  
        ACLMessage reply = request.createReply();
        
    	if (object instanceof ExecuteBatch) {
    		
    		ExecuteBatch executeExperiment =
    				(ExecuteBatch) object;
    		ComputationDescription comDescription =
					executeExperiment.getDescription();
    		
    		Parser parser = new Parser(this.agent);
    		parser.parseRoots(comDescription);
    		
    		ComputationGraph computationGraph = parser.getComputationGraph();

    		computationGraph.startComputation();

    		// Problem parsed - reply OK
            reply.setPerformative(ACLMessage.INFORM);
            reply.setLanguage(codec.getName());
            reply.setOntology(ontology.getName());
            reply.setContent("OK");
        }
   
        return reply;

    }
}