package org.pikater.core.agents.system.duration;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.DurationOntology;
import org.pikater.core.ontology.subtrees.duration.Duration;
import org.pikater.core.ontology.subtrees.duration.GetDuration;
import org.pikater.core.ontology.subtrees.duration.SetDuration;

public class DurationService extends FIPAService {

	static final Codec codec = new SLCodec();

	public static Duration getDuration(PikaterAgent agent, GetDuration gd) {

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DURATION, false));
		request.setOntology(DurationOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(gd);

		try {
			agent.getContentManager().fillContent(request, a);
		} catch (CodecException | OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		Duration duration=new Duration();
		
		try {
			ACLMessage reply = FIPAService.doFipaRequestClient(agent, request);

			// get Duration from the received message
			ContentElement content = agent.getContentManager().extractContent(reply);

			duration = (Duration) (((Result) content).getValue());

		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UngroundedException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}

		return duration;
	}
}