package org.pikater.core.agents.system;

import java.io.File;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.VisualisationOntology;
import org.pikater.core.ontology.subtrees.visualisation.GenerateScatterPlot;
import org.pikater.shared.visualisation.SimpleVisualisator;

public class Agent_Visualisator extends PikaterAgent {

	private static final long serialVersionUID = 201239664153452059L;

	@Override
	public java.util.List<Ontology> getOntologies() {
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();
		ontologies.add(VisualisationOntology.getInstance());
		return ontologies;
	}
	
	@Override
	protected void setup() {
		try {
			initDefault();
			registerWithDF();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager().extractContent(request);

					if (a.getAction() instanceof GenerateScatterPlot) {
						return respondToGenerateScatterPlot(request, a);
					}

				} catch (OntologyException e) {
					logError("Problem extracting content: ",e);
				} catch (CodecException e) {
					logError("Codec problem: ",e);
				} catch (Exception e) {
					e.printStackTrace();
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: " + request.getContent());
				return failure;
			}

		});

	}
	
	private ACLMessage respondToGenerateScatterPlot(ACLMessage request,
			Action a) throws Exception {
		GenerateScatterPlot scp=(GenerateScatterPlot)a.getAction();
		
		SimpleVisualisator sv=new SimpleVisualisator();
		sv.createScatteredPlot(
				new File(scp.getDataSetPath()),
				new File(scp.getPlotPath())
				);
		
		ACLMessage success = request.createReply();
		success.setPerformative(ACLMessage.INFORM);
		return success;
	}

}
